package games.voided.voidaesp.paper.engine;

import games.voided.voidaesp.core.config.ConfigManager;
import games.voided.voidaesp.core.engine.AsyncRunner;
import games.voided.voidaesp.core.engine.Engine;
import games.voided.voidaesp.core.engine.SimpleEngine;
import games.voided.voidaesp.core.players.PlayerRegistry;
import games.voided.voidaesp.paper.PaperParticleSpawner;
import games.voided.voidaesp.paper.VoidAESP;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.function.IntSupplier;

public class PaperSimpleEngine implements Engine {
    private final AsyncScheduler asyncScheduler;
    private final BukkitScheduler bukkitScheduler;
    private final VoidAESP plugin;
    //private final BukkitESM entitySnapshotManager;
    private final SimpleEngine delegate;

    public PaperSimpleEngine(VoidAESP plugin, ConfigManager cfg, IntSupplier currentTickSupplier) {
        this.plugin = plugin;
        asyncScheduler = plugin.getServer().getAsyncScheduler();
        bukkitScheduler = plugin.getServer().getScheduler();
        delegate = new SimpleEngine(cfg, new PaperParticleSpawner(), currentTickSupplier, new PaperAsyncRunner(asyncScheduler));

        //forceEntityLocationUpdate();
    }

    @Override
    public void tick() {
        delegate.tick();
    }

    //should be folia compatible too
    public static class PaperAsyncRunner implements AsyncRunner {
        private final AsyncScheduler asyncScheduler;

        public PaperAsyncRunner(AsyncScheduler asyncScheduler) {
            this.asyncScheduler = asyncScheduler;
        }

        public void runNow(Runnable task) {
            asyncScheduler.runNow(VoidAESP.get(), (ignored) -> task.run());
        }
    }
}
