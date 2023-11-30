package io.github.metriximor.timezones.models.configs;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record WorldConfig(
        @NotNull String name,
        int width,
        int height,
        int xCenter
) implements ConfigurationSerializable {
    private static final String NAME = "name";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String X_CENTER = "x_offset";

    public static WorldConfig getDefaultWorldConfig() {
        return new WorldConfig("world", 600, 300, 0);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();

        data.put(NAME, name);
        data.put(WIDTH, width);
        data.put(HEIGHT, height);
        data.put(X_CENTER, xCenter);

        return data;
    }

    public static WorldConfig deserialize(Map<String, Object> args) {
        return new WorldConfig(
                (String) args.get(NAME),
                (int) args.get(WIDTH),
                (int) args.get(HEIGHT),
                (int) args.get(X_CENTER)
        );
    }

    public static boolean isValid(@NotNull Object data) {
        if (!(data instanceof Map<?, ?> map)) {
            return false;
        }
        return (map.containsKey(NAME) && (map.get(NAME) instanceof String)) &&
                (map.containsKey(WIDTH) && (map.get(WIDTH) instanceof Integer)) &&
                (map.containsKey(HEIGHT) && (map.get(HEIGHT) instanceof Integer)) &&
                (map.containsKey(X_CENTER) && (map.get(X_CENTER) instanceof Integer));
    }
}
