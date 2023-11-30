package io.github.metriximor.timezones.models.configs;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record PluginConfig(
        int amountOfBlocksForGradualLongitudeTimeOffset,
        int amountOfTicksTillUpdate
) implements ConfigurationSerializable {
    private static final String BLOCKS_PER_LONGITUDE = "amount_of_blocks_for_gradual_longitude_time_offset";
    private static final String TICKS_TILL_UPDATE = "amount_of_ticks_per_player_update";
    public static PluginConfig getDefaultPluginConfig() {
        return new PluginConfig(10, 20);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put(BLOCKS_PER_LONGITUDE, this.amountOfBlocksForGradualLongitudeTimeOffset);
        data.put(TICKS_TILL_UPDATE, this.amountOfTicksTillUpdate);
        return data;
    }

    @SuppressWarnings("unused")
    public static PluginConfig deserialize(Map<String, Object> args) {
        return new PluginConfig(
                (int) args.get(BLOCKS_PER_LONGITUDE),
                (int) args.get(TICKS_TILL_UPDATE)
        );
    }
}
