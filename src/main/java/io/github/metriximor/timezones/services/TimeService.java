package io.github.metriximor.timezones.services;

import io.github.metriximor.timezones.models.Timezone;
import org.jetbrains.annotations.NotNull;

public class TimeService {
    private static final int SUNRISE_TICK = 0;
    private static final int SUNSET_TICK = 12000;
    private static final int TICKS_IN_A_DAY = 24000;

    public static boolean isDayAt(@NotNull final Timezone timezone, final long worldTime) {
        var currentTime = getCurrentDayTime(timezone, worldTime);
        return currentTime >= SUNRISE_TICK && currentTime < SUNSET_TICK;
    }

    public static boolean isNightAt(@NotNull final Timezone timezone, final long worldTime) {
        return !isDayAt(timezone, worldTime);
    }

    public static long getCurrentDayTime(@NotNull final Timezone timezone, long worldTime) {
        var currentTime = (timezone.offset() + worldTime) % TICKS_IN_A_DAY;
        if (currentTime < 0) {
            // If we don't add 24k, we could have negative 12k time, which leads to funky results
            return currentTime + TICKS_IN_A_DAY;
        }
        return currentTime;
    }

    public static long getCurrentDayOffset(@NotNull final Timezone timezone, long worldTime) {
        if (worldTime <= TICKS_IN_A_DAY) {
            return timezone.offset() + TICKS_IN_A_DAY;
        }
        return timezone.offset();
    }
}
