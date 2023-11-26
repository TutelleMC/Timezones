package io.github.metriximor.timezones.models;

import org.jetbrains.annotations.NotNull;

/**
 * @param offset in ticks (1 second = 20 ticks)
 */
public record Timezone(
        @NotNull Location2D meridian,
        int offset
) {
}
