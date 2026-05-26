package games.voided.voidaesp.core.config.engine;

import games.voided.voidaesp.core.config.Config;
import games.voided.voidaesp.core.config.ConfigLoadException;
import games.voided.voidaesp.core.config.ConfigReader;
import org.spongepowered.configurate.ConfigurationNode;

public record SimpleEngineConfig(int asyncProcessingThreads) implements Config {
    public static SimpleEngineConfig load(ConfigurationNode node) {
        int threads = ConfigReader.integer(ConfigReader.node(node, "async-processing-threads"), "engine.simple.async-processing-threads");
        if (threads < 1) {
            throw new ConfigLoadException("engine.simple.async-processing-threads must be at least 1");
        }
        return new SimpleEngineConfig(threads);
    }
}
