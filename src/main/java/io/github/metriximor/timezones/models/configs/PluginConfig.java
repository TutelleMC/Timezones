package io.github.metriximor.timezones.models.configs;

public record PluginConfig(
        int amountOfBlocksForGradualLongitudeTimeOffset,
        int amountOfTicksTillUpdate
) {
    public static PluginConfig getDefaultPluginConfig() {
        return new PluginConfig(10, 20);
    }
}
