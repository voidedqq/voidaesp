package games.voided.voidaesp.paper.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import games.voided.locatables.Locatable;
import games.voided.locatables.MutableLocatable;
import games.voided.locatables.implementations.MutableLocatableImpl;
import games.voided.logs.Logger;
import games.voided.voidaesp.core.config.ConfigManager;
import games.voided.voidaesp.core.locatables.EntityLocatable;
import games.voided.voidaesp.core.players.PlayerData;
import games.voided.voidaesp.core.players.PlayerRegistry;
import games.voided.voidaesp.core.raycast.RaycastUtil;
import games.voided.voidaesp.core.view.AbstractBlockView;
import games.voided.voidaesp.paper.VoidAESP;
import games.voided.voidaesp.paper.UpdateChecker;
import games.voided.voidaesp.paper.staging.PacketEventsPaperBlockInfoResolver;

import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;

import net.strokkur.commands.*;
import net.strokkur.commands.arguments.StringArg;
import net.strokkur.commands.arguments.StringArgType;
import net.strokkur.commands.paper.Description;
import net.strokkur.commands.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

// Credit to Strokkur for making StrokkCommands, a non-hideous way to use the power of brigadier.

@Command("voidaesp")
@Aliases({"raesp", "antiesp", "reo"})
@Description("Command for management of the VoidAESP plugin")
@Permission("voidaesp.command")
public class VoidAESPCommand {
    @DefaultExecutes
    public void helpCommand(CommandSender sender) {
        sender.sendRichMessage("<white>VoidAESP <yellow>v" + VoidAESP.get().getDescription().getVersion());
        sender.sendRichMessage("<white>Commands:");
        sender.sendRichMessage("<green>/voidaesp reload <gray>- Reloads the config");
        sender.sendRichMessage("<green>/voidaesp config-values <gray>- Shows all config values");
        sender.sendRichMessage("<green>/voidaesp set <key> <value> <gray>- Sets a config value");
        sender.sendRichMessage("<green>/voidaesp add <key> <value> <gray>- Adds a value to a list config");
        sender.sendRichMessage("<green>/voidaesp remove <key> <value> <gray>- Removes a value from a list config");
    }

    @Executes("reload")
    void reloadCommand(CommandSender sender) {
        try {
            ConfigManager.get().load();
            sender.sendMessage("[VoidAESP] Config reloaded.");
        } catch (RuntimeException e) {
            sender.sendRichMessage("<red>[VoidAESP] Config reload rejected: <white>" + e.getMessage());
        }
    }

    @Executes("config-values")
    void configValuesCommand(CommandSender sender) {
        ConfigManager config = ConfigManager.get();
        //dynamic config values
        sender.sendMessage("[VoidAESP] Config values: ");

        for (var entry : config.getConfigValues().entrySet()) {
            String path = entry.getKey();
            Object val = entry.getValue();
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>" + path + "<gray> = <white>" + val));
        }
    }

    @Executes("set")
    void setCommand(@StringArg(StringArgType.STRING) String key, @StringArg(StringArgType.GREEDY) String value, CommandSender sender) {
        ConfigManager config = ConfigManager.get();
        ConfigManager.SetConfigResult result = config.setConfigValue(key, value);
        sendConfigMutationResult(sender, result, "Set", key, value);
    }

    @Executes("add")
    void addCommand(@StringArg(StringArgType.STRING) String key, @StringArg(StringArgType.GREEDY) String value, CommandSender sender) {
        ConfigManager config = ConfigManager.get();
        ConfigManager.SetConfigResult result = config.addConfigListValue(key, value);
        sendConfigMutationResult(sender, result, "Added", key, value);
    }

    @Executes("remove")
    void removeCommand(@StringArg(StringArgType.STRING) String key, @StringArg(StringArgType.GREEDY) String value, CommandSender sender) {
        ConfigManager config = ConfigManager.get();
        ConfigManager.SetConfigResult result = config.removeConfigListValue(key, value);
        sendConfigMutationResult(sender, result, "Removed", key, value);
    }

    @Executes("check-for-updates")
    void checkForUpdatesCommand(CommandSender sender) {
        UpdateChecker.checkForUpdates(VoidAESP.get(), sender);
    }

    @Executes("print-block-ids")
    void printBlockIDsCommand() {
        PacketEventsPaperBlockInfoResolver.get.iterateBlockIDs(true);
    }

    private void sendConfigMutationResult(CommandSender sender, ConfigManager.SetConfigResult result, String action, String key, String value) {
        if (!result.success()) {
            sender.sendRichMessage("<red>Invalid config change: <white>" + result.message());
            return;
        }
        sender.sendRichMessage("<white>" + action + " <green>" + value + "<white> for <green>" + key);
        if (result.restartRequired()) {
            sender.sendRichMessage("<yellow>This change was saved but requires a restart: <white>" + result.message());
        }
    }

    @Subcommand("test")
    static class TestCommands {

        @Executes("location-drift")
        void testCommand(CommandSender sender) {
            Player player = (Player) sender;
            PlayerData playerData = PlayerRegistry.getInstance().getPlayerData(player.getUniqueId());
            Entity closestEntity = player.getNearbyEntities(10,10,10).getFirst();
            if (closestEntity == null) return;
            player.sendRichMessage("Closest entity is "+closestEntity.getName());
            Locatable entityLocatable = playerData.entityView().getLocation(closestEntity.getUniqueId());
            Location bukkitLoc = closestEntity.getLocation().clone();
            player.sendRichMessage("Entity location according to PacketEvents is "+entityLocatable);
            player.sendRichMessage("Entity location according to Bukkit is "+bukkitLoc);
            double driftX = Math.abs(entityLocatable.x() - bukkitLoc.getX());
            double driftZ = Math.abs(entityLocatable.z() - bukkitLoc.getZ());
            if (driftX < 0.0005) driftX = 0;
            if (driftZ < 0.0005) driftZ = 0;
            Logger.debug("Drift is X: "+driftX+" Z: "+driftZ);
            sender.sendRichMessage("Drift is X: "+driftX+" Z: "+driftZ);
        }

        @Executes("benchmark")
        void debugCommand(Player player) throws CommandSyntaxException {
            //benchmark raycast speed by generating 1000 locatables normally distributed approx 50 blocks around the player and raycasting to them, then printing the average time taken

            Locatable[] locatables = new Locatable[1000];
            PlayerData playerData = PlayerRegistry.getInstance().getPlayerData(player.getUniqueId());
            Locatable playerLocatable = playerData.ownLocation();
            MutableLocatable unitDirection = new MutableLocatableImpl(playerLocatable.world(), 0, 0, 0);
            for (int i = 0; i < locatables.length; i++) {
                unitDirection.setX(Math.random() - 0.5);
                unitDirection.setY(Math.random() - 0.5);
                unitDirection.setZ(Math.random() - 0.5);
                unitDirection.normalize();
                unitDirection.scalarMultiply(50);
                locatables[i] = playerLocatable.clonePlainAndCentreIfBlockLocation().add(unitDirection);
            }
            Bukkit.getAsyncScheduler().runNow(VoidAESP.get(), (ignored) -> {
                long startTime = System.nanoTime();
                for (Locatable locatable : locatables) {
                    RaycastUtil.raycast(playerData, playerLocatable, locatable, 3, 0, 100, false, playerData.blockView(), 1, null);
                }
                long endTime = System.nanoTime();
                long duration = endTime - startTime;
                double averageTime = duration / (double) locatables.length;
                player.sendRichMessage("Average raycast time: " + averageTime + " nanoseconds");
                player.sendRichMessage("Total raycast time: " + duration + " nanoseconds");
            });
        }

        @Executes("loaded-chunks")
        void loadedChunksCommand(Player player) {
            PlayerData playerData = PlayerRegistry.getInstance().getPlayerData(player.getUniqueId());
            AbstractBlockView<?> pbsm = (AbstractBlockView<?>) playerData.blockView();
            player.sendMessage(pbsm.loadedChunkCount() +"chunks loaded");
        }

        @Executes("entity-id")
        void getFromEntityID(int entityID, Player player) {
            PlayerData playerData = PlayerRegistry.getInstance().getPlayerData(player.getUniqueId());
            EntityLocatable<?, ?> entityLocatable = playerData.entityView().getEntity(entityID);
            Entity bukkitEntity = SpigotConversionUtil.getEntityById(player.getWorld(), entityID);
            player.sendRichMessage("Entity with ID " + entityID + ":");
            player.sendRichMessage("According to Bukkit: " + bukkitEntity);
            player.sendRichMessage("Bukkit type: " + bukkitEntity.getAsString());
            player.sendRichMessage("According to PacketEvents: " + entityLocatable);
        }

        @DefaultExecutes
        public void helpCommand(@NotNull CommandSender sender) {
            sender.sendRichMessage("<white>Test subcommands:");
            sender.sendRichMessage("<green>/voidaesp test location-drift <gray>- Tests the drift between Bukkit and PacketEvents entity locations");
            sender.sendRichMessage("<green>/voidaesp test benchmark <gray>- Benchmarks raycast speed by raycasting to 1000 random locatables around the player and printing the average time taken");
            sender.sendRichMessage("<green>/voidaesp test loaded-chunks <gray>- Shows the number of chunks currently loaded in the player's block view");
        }
    }
}
