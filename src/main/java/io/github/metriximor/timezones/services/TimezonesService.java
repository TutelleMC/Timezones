package io.github.metriximor.timezones.services;

import com.sk89q.worldedit.math.BlockVector2;
import io.github.metriximor.timezones.models.Partition;
import io.github.metriximor.timezones.models.Timezone;
import io.github.metriximor.timezones.models.WorldConfig;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class TimezonesService {
    private final Logger logger;

    @NotNull
    public List<Timezone> instantiateTimezones(final WorldConfig worldConfig) {
        final List<Timezone> timezones = new ArrayList<>();
        final var timezonesXRanges = new Partition(
                worldConfig.width(),
                worldConfig.amountOfTimezones(),
                -299
        ).getAllIntervals();

        for (int i = 0; i < worldConfig.amountOfTimezones(); i += 1) {
            final int offset = -(worldConfig.amountOfTimezones() / 2) + 1 + i;
            if (offset == 0) {
                continue;
            }
            final var timezone = instantiateTimezone(worldConfig, i, offset, timezonesXRanges.get(i));

            logger.fine(String.format("Added new timezone: %s", timezone));
            timezones.add(timezone);
        }
        return timezones;
    }

    @NotNull
    private static Timezone instantiateTimezone(@NotNull final WorldConfig worldConfig,
                                                final int i,
                                                final int offset,
                                                @NotNull final Partition.Interval interval) {
        final var id = String.format("equatorial_%s", i);
        final var upperLeftCorner = BlockVector2.at(
                interval.lowerBound(),
                worldConfig.height()
        );
        final var lowerRightCorner = BlockVector2.at(
                interval.upperBound(),
                -worldConfig.height()
        );
        return new Timezone(id, upperLeftCorner, lowerRightCorner, offset, worldConfig);
    }
}
