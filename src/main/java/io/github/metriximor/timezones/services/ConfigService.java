package io.github.metriximor.timezones.services;

import io.github.metriximor.timezones.models.configs.PluginConfig;
import io.github.metriximor.timezones.models.configs.WorldConfig;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.logging.Logger;

@Getter
public class ConfigService {
    private static final String PLUGIN = "plugin";
    private static final String WORLDS = "worlds";
    private final PluginConfig pluginConfig;

    public ConfigService(@NotNull final Logger logger,
                         @NotNull final WorldsService worldsService,
                         @NotNull final FileConfiguration config) {
        this.pluginConfig = config.getSerializable(PLUGIN, PluginConfig.class, PluginConfig.getDefaultPluginConfig());

        var worlds = config.getMapList(WORLDS);
        if (worlds.isEmpty()) {
            logger.warning("No worlds configured in config!");
            return;
        }
        for (var world : worlds) {
            if (!WorldConfig.isValid(world)) {
                logger.severe("Not a valid world configuration: %s".formatted(world));
                continue;
            }
            //noinspection unchecked
            var worldConfig = WorldConfig.deserialize((Map<String, Object>) world);
            logger.info("Loaded info about %s: %s".formatted(worldConfig.name(), worldConfig));
            worldsService.addWorldConfig(worldConfig);
        }
    }
}
