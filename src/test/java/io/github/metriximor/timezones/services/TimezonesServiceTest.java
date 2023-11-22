package io.github.metriximor.timezones.services;

import io.github.metriximor.timezones.Utils;
import io.github.metriximor.timezones.models.Timezone;
import io.github.metriximor.timezones.models.WorldConfig;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TimezonesServiceTest {
    private final Logger loggerMock = mock(Logger.class);
    private final WorldConfig worldConfig = Utils.getWorldConfig();
    private final TimezonesService timezonesService = new TimezonesService(loggerMock);

    @Test
    void testTimezonesAreInstantiatedWithCorrectOffsets() {
        var expectedOffsets = new int[]{-11, -10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        var offsets = timezonesService.instantiateTimezones(worldConfig).stream()
                .map(Timezone::offset).iterator();
        Arrays.stream(expectedOffsets)
                .sequential().forEach(expectedOffset -> assertEquals(expectedOffset, offsets.next()));
        assertFalse(offsets.hasNext());
    }

    @Test
    void testInstantiateTimezonesInstantiatedTheCorrectAmountOfTimezones() {
        assertEquals(23, timezonesService.instantiateTimezones(worldConfig).size());
    }

    @Test
    void testInstantiateTimezonesCorrectlyFetchesTheXInterval() {
        var timezones = timezonesService.instantiateTimezones(worldConfig);
        assertEquals(-299, timezones.get(0).upperLeftCorner().getX());
        assertEquals(-275, timezones.get(0).lowerRightCorner().getX());
        assertEquals(-11, timezones.get(0).offset());

        assertEquals(-49, timezones.get(10).upperLeftCorner().getX());
        assertEquals(-25, timezones.get(10).lowerRightCorner().getX());
        assertEquals(-1, timezones.get(10).offset());

        assertEquals(1, timezones.get(11).upperLeftCorner().getX());
        assertEquals(25, timezones.get(11).lowerRightCorner().getX());
        assertEquals(1, timezones.get(11).offset());

        assertEquals(276, timezones.get(22).upperLeftCorner().getX());
        assertEquals(300, timezones.get(22).lowerRightCorner().getX());
        assertEquals(12, timezones.get(22).offset());
    }
}