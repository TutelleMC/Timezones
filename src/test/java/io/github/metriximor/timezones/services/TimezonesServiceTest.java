package io.github.metriximor.timezones.services;

import io.github.metriximor.timezones.models.Location2D;
import io.github.metriximor.timezones.models.Timezone;
import io.github.metriximor.timezones.models.configs.PluginConfig;
import io.github.metriximor.timezones.models.configs.WorldConfig;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.logging.Logger;

import static io.github.metriximor.timezones.models.configs.WorldConfig.getDefaultWorldConfig;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TimezonesServiceTest {
    private final Logger loggerMock = mock(Logger.class);
    private final WorldConfig worldConfig = getDefaultWorldConfig();
    private final PluginConfig pluginConfig = new PluginConfig(25, 20);
    private final WorldsService worldsService = new WorldsService();
    private final TimezonesService timezonesService = new TimezonesService(loggerMock, pluginConfig, worldsService);

    @Test
    void testTimezonesAreInstantiatedWithCorrectOffsets() {
        var expectedOffsets = new int[]{
                -12, -11, -10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
        };
        var offsets = timezonesService.instantiateTimezones(worldConfig).getTimezones().values().stream()
                .map(Timezone::offset).sorted().iterator();
        Arrays.stream(expectedOffsets).sequential()
                .forEach(expectedOffset -> assertEquals(expectedOffset * 1000, offsets.next()));
        assertFalse(offsets.hasNext());
    }

    @Test
    void testInstantiateTimezonesInstantiatedTheCorrectAmountOfTimezones() {
        var result = timezonesService.instantiateTimezones(worldConfig).getTimezones();
        assertEquals(25, result.size(), "There should be 25 timezones, from -12 to 12 (including)");
    }

    @Test
    void testInstantiateTimezonesInstantiatedTheCorrectAmountOfTimezonesWithDifferentWorldSize() {
        final PluginConfig pluginConfig = new PluginConfig(10, 20);
        final TimezonesService timezonesService = new TimezonesService(loggerMock, pluginConfig, worldsService);
        var result = timezonesService.instantiateTimezones(new WorldConfig("world", 1000, 500, 0)).getTimezones();
        assertEquals(101, result.size(), "There should be 25 timezones, from -12 to 12 (including)");
    }

    @Test
    void testInstantiateTimezonesInstantiatedTheCorrectAmountOfTimezonesWhenThereAreMoreTimezonesThan24() {
        final PluginConfig pluginConfig = new PluginConfig(10, 20);
        final TimezonesService timezonesService = new TimezonesService(loggerMock, pluginConfig, worldsService);
        var result = timezonesService.instantiateTimezones(worldConfig).getTimezones();
        assertEquals(61, result.size(), "There should be 61 timezones, 600/10=60 plus 1 because the +12/-12 is duplicated");
    }

    @Test
    void testFindNearestTimezoneCanHandleANonExistentWorld() {
        var result = timezonesService.findNearestTimezone("worldDoesntExist", new Location2D(0, 0));
        assertEquals(Optional.empty(), result);
    }

    @Test
    void testFindNearestTimezoneWorks() {
        timezonesService.instantiateTimezones(worldConfig);
        var nearestPointToZeroZero = timezonesService.findNearestTimezone(worldConfig.name(), new Location2D(5, 10)).orElseThrow();
        assertEquals(0, nearestPointToZeroZero.offset());

        nearestPointToZeroZero = timezonesService.findNearestTimezone(worldConfig.name(), new Location2D(5, 300)).orElseThrow();
        assertEquals(0, nearestPointToZeroZero.offset());
    }
}