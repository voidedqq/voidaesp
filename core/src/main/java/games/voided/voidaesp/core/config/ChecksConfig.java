package games.voided.voidaesp.core.config;

import games.voided.voidaesp.core.config.raycast.ChunkSectionConfig;
import games.voided.voidaesp.core.config.raycast.EntityConfig;
import games.voided.voidaesp.core.config.raycast.PlayerConfig;
import games.voided.voidaesp.core.config.raycast.SoundEffectsConfig;
import games.voided.voidaesp.core.config.raycast.TileEntityConfig;
import org.spongepowered.configurate.ConfigurationNode;

public record ChecksConfig(PlayerConfig playerConfig, EntityConfig entityConfig, TileEntityConfig tileEntityConfig, SoundEffectsConfig soundEffectsConfig, ChunkSectionConfig chunkSectionConfig) implements Config {
    public static ChecksConfig load(ConfigurationNode root) {
        ConfigurationNode checks = ConfigReader.node(root, "checks");
        return new ChecksConfig(
                PlayerConfig.load(ConfigReader.node(checks, "player"), "checks.player"),
                EntityConfig.load(ConfigReader.node(checks, "entity"), "checks.entity"),
                TileEntityConfig.load(ConfigReader.node(checks, "tile-entity"), "checks.tile-entity"),
                SoundEffectsConfig.load(ConfigReader.node(checks, "sound-effects"), "checks.sound-effects"),
                ChunkSectionConfig.load(ConfigReader.node(checks, "chunk-section"), "checks.chunk-section")
        );
    }

    public boolean hasRestartOnlyChanges(ChecksConfig startup) {
        return playerConfig.enabled() != startup.playerConfig.enabled()
                || entityConfig.enabled() != startup.entityConfig.enabled()
                || tileEntityConfig.enabled() != startup.tileEntityConfig.enabled()
                || chunkSectionConfig.enabled() != startup.chunkSectionConfig.enabled();
    }
}
