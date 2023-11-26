package io.github.metriximor.timezones.models.configs;

import org.jetbrains.annotations.NotNull;

public record WorldConfig(
        @NotNull String name,
        int width,
        int height,
        int xCenter,
        int minWorldHeight,
        int maxWorldHeight
) {
    public static WorldConfig getDefaultWorldConfig() {
        return new WorldConfig("world", 600, 300, 0, -64, 320);
    }
}
