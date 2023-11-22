package io.github.metriximor.timezones;

import io.github.metriximor.timezones.models.WorldConfig;

public class Utils {
    private Utils() {
    }

    public static WorldConfig getWorldConfig() {
        return new WorldConfig(600, 300, 24, -64, 320);
    }
}
