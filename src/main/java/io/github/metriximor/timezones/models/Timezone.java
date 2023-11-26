package io.github.metriximor.timezones.models;

import org.jetbrains.annotations.NotNull;

/**
 * @param offset in ticks (1 second = 20 ticks)
 */
public record Timezone(
        @NotNull Location2D meridian,
        int offset
) {
    public long currentTime() {
        return offset + 24000; // If we don't add 24k, we could have negative 12k time, which leads to funky results
    }
}
