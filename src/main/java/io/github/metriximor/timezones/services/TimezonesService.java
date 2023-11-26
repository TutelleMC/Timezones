package io.github.metriximor.timezones.services;

import io.github.metriximor.timezones.models.Location2D;
import io.github.metriximor.timezones.models.Partition;
import io.github.metriximor.timezones.models.Timezone;
import io.github.metriximor.timezones.models.WorldTimezones;
import io.github.metriximor.timezones.models.configs.PluginConfig;
import io.github.metriximor.timezones.models.configs.WorldConfig;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class TimezonesService {
    private static final int TICKS_PER_MINECRAFT_DAY = 24000;

    private final Logger logger;
    private final PluginConfig pluginConfig;
    private final WorldsService worldsService;
    private final Map<String, WorldTimezones> worldTimezones = new HashMap<>();

    @NotNull
    public Optional<Timezone> findNearestTimezone(@NotNull final String world, @NotNull final Location2D location) {
        var worldTimezone = worldTimezones.get(world);
        if (worldTimezone == null) {
            return Optional.empty();
        }
        return Optional.of(worldTimezone.findNearestTimezone(location));
    }

    @NotNull
    public WorldTimezones instantiateTimezones(@NotNull final WorldConfig worldConfig) {
        final Map<Location2D, Timezone> timezones = new HashMap<>();
        final int amountOfGradualTimezones = worldConfig.width() / pluginConfig.amountOfBlocksForGradualLongitudeTimeOffset();
        final var timezonesXCenters = new Partition(
                worldConfig.width(),
                amountOfGradualTimezones,
                -(worldConfig.width() / 2) + worldConfig.xCenter()
        ).getAllCenterPoints();
        final var timezoneOffsets = new Partition(
                TICKS_PER_MINECRAFT_DAY,
                amountOfGradualTimezones,
                -TICKS_PER_MINECRAFT_DAY / 2
        ).getAllCenterPoints();

        assert timezonesXCenters.size() == timezoneOffsets.size();
        for (int i = 0; i <= amountOfGradualTimezones; i += 1) {
            final double x = timezonesXCenters.get(i);
            final var location = new Location2D(x, 0.0);
            final int offset = timezoneOffsets.get(i).intValue();
            final var timezone = new Timezone(location, offset);

            logger.fine(String.format("Added new timezone: %s", timezone));
            timezones.put(location, timezone);
        }

        final var worldTimezones = new WorldTimezones(worldConfig, timezones);
        this.worldTimezones.put(worldConfig.name(), worldTimezones);
        return worldTimezones;
    }

    public boolean areAllWorldsInstantiated() {
        return worldsService.getAllWorldConfigs().stream()
                .allMatch(worldConfig -> worldTimezones.containsKey(worldConfig.name()));
    }

    public void instantiateTimezonesInAllWorlds() {
        worldsService.getAllWorldConfigs().forEach(this::instantiateTimezones);
    }

}
