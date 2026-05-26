package games.voided.voidaesp.core.config.engine;

import games.voided.voidaesp.core.config.Config;
import games.voided.voidaesp.core.config.ConfigLoadException;
import games.voided.voidaesp.core.config.ConfigReader;
import org.spongepowered.configurate.ConfigurationNode;

public record EngineConfig(EngineMode mode, SimpleEngineConfig simpleConfig) implements Config {
    public static EngineConfig load(ConfigurationNode root) {
        ConfigurationNode node = ConfigReader.node(root, "engine");
        String modeName = ConfigReader.string(ConfigReader.node(node, "mode"), "engine.mode");
        EngineMode mode = EngineMode.fromString(modeName);
        if (mode == null) {
            throw new ConfigLoadException("engine.mode has unsupported value '" + modeName + "'");
        }
        return new EngineConfig(mode, SimpleEngineConfig.load(ConfigReader.node(node, "simple")));
    }

    public EngineMode getMode() {
        return mode;
    }
}
