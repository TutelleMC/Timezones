package io.github.metriximor.timezones.services;

import io.github.metriximor.timezones.models.Location2D;
import io.github.metriximor.timezones.models.Timezone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeServiceTest {
    @Test
    void testIsDayAtAndIsNightAtWorksWithNoOffset() {
        var zeroZero = new Location2D(0, 0);
        var timezone = new Timezone(zeroZero, 0);
        assertTrue(TimeService.isDayAt(timezone, 0));
        assertFalse(TimeService.isNightAt(timezone, 0));

        assertTrue(TimeService.isDayAt(timezone, 6000));
        assertFalse(TimeService.isNightAt(timezone, 6000));

        assertFalse(TimeService.isDayAt(timezone, 12000));
        assertTrue(TimeService.isNightAt(timezone, 12000));

        assertFalse(TimeService.isDayAt(timezone, 18000));
        assertTrue(TimeService.isNightAt(timezone, 18000));
    }

    @Test
    void testIsDayAtAndIsNightAtWorksWhenItsActuallyDayWithPositiveOffset() {
        var eastEnd = new Location2D(500, 0);
        var timezone = new Timezone(eastEnd, +12000);
        assertFalse(TimeService.isDayAt(timezone, 0));
        assertTrue(TimeService.isNightAt(timezone, 0));

        assertFalse(TimeService.isDayAt(timezone, 6000));
        assertTrue(TimeService.isNightAt(timezone, 6000));

        assertTrue(TimeService.isDayAt(timezone, 12000));
        assertFalse(TimeService.isNightAt(timezone, 12000));

        assertTrue(TimeService.isDayAt(timezone, 18000));
        assertFalse(TimeService.isNightAt(timezone, 18000));
    }

    @Test
    void testIsDayAtAndIsNightAtWorksWhenItsActuallyDayWithNegativeOffset() {
        var westEnd = new Location2D(-500, 0);
        var timezone = new Timezone(westEnd, -12000);
        assertFalse(TimeService.isDayAt(timezone, 0));
        assertTrue(TimeService.isNightAt(timezone, 0));

        assertFalse(TimeService.isDayAt(timezone, 6000));
        assertTrue(TimeService.isNightAt(timezone, 6000));

        assertTrue(TimeService.isDayAt(timezone, 12000));
        assertFalse(TimeService.isNightAt(timezone, 12000));

        assertTrue(TimeService.isDayAt(timezone, 18000));
        assertFalse(TimeService.isNightAt(timezone, 18000));
    }

    @Test
    void testGetCurrentTimeWorksWithZeroOffset() {
        var zeroZero = new Location2D(0, 0);
        var timezone = new Timezone(zeroZero, 0);
        assertEquals(0, TimeService.getCurrentDayTime(timezone, 0));
        assertEquals(24000, TimeService.getCurrentDayOffset(timezone, 0));

        assertEquals(6000, TimeService.getCurrentDayTime(timezone, 6000));
        assertEquals(24000, TimeService.getCurrentDayOffset(timezone, 6000));

        assertEquals(12000, TimeService.getCurrentDayTime(timezone, 12000));
        assertEquals(24000, TimeService.getCurrentDayOffset(timezone, 12000));

        assertEquals(18000, TimeService.getCurrentDayTime(timezone, 18000));
        assertEquals(24000, TimeService.getCurrentDayOffset(timezone, 18000));
    }

    @Test
    void testGetCurrentTimeWorksWithPositiveOffset() {
        var eastEnd = new Location2D(500, 0);
        var timezone = new Timezone(eastEnd, +12000);
        assertEquals(12000, TimeService.getCurrentDayTime(timezone, 0));
        assertEquals(36000, TimeService.getCurrentDayOffset(timezone, 0));

        assertEquals(18000, TimeService.getCurrentDayTime(timezone, 6000));
        assertEquals(36000, TimeService.getCurrentDayOffset(timezone, 6000));

        assertEquals(0, TimeService.getCurrentDayTime(timezone, 12000));
        assertEquals(36000, TimeService.getCurrentDayOffset(timezone, 12000));


        assertEquals(6000, TimeService.getCurrentDayTime(timezone, 18000));
        assertEquals(36000, TimeService.getCurrentDayOffset(timezone, 18000));


        assertEquals(12000, TimeService.getCurrentDayTime(timezone, 24000));
        assertEquals(36000, TimeService.getCurrentDayOffset(timezone, 24000));

        assertEquals(18000, TimeService.getCurrentDayTime(timezone, 30000));
        assertEquals(12000, TimeService.getCurrentDayOffset(timezone, 30000));

        assertEquals(0, TimeService.getCurrentDayTime(timezone, 36000));
        assertEquals(12000, TimeService.getCurrentDayOffset(timezone, 36000));

        assertEquals(6000, TimeService.getCurrentDayTime(timezone, 42000));
        assertEquals(12000, TimeService.getCurrentDayOffset(timezone, 42000));
    }

    @Test
    void testGetCurrentTimeWorksWithNegativeOffset() {
        var westEnd = new Location2D(500, 0);
        var timezone = new Timezone(westEnd, -12000);
        assertEquals(12000, TimeService.getCurrentDayTime(timezone, 0));
        assertEquals(12000, TimeService.getCurrentDayOffset(timezone, 0));

        assertEquals(18000, TimeService.getCurrentDayTime(timezone, 6000));
        assertEquals(12000, TimeService.getCurrentDayOffset(timezone, 6000));

        assertEquals(0, TimeService.getCurrentDayTime(timezone, 12000));
        assertEquals(12000, TimeService.getCurrentDayOffset(timezone, 12000));

        assertEquals(6000, TimeService.getCurrentDayTime(timezone, 18000));
        assertEquals(12000, TimeService.getCurrentDayOffset(timezone, 18000));


        assertEquals(12000, TimeService.getCurrentDayTime(timezone, 24000));
        assertEquals(12000, TimeService.getCurrentDayOffset(timezone, 24000));

        assertEquals(18000, TimeService.getCurrentDayTime(timezone, 30000));
        assertEquals(-12000, TimeService.getCurrentDayOffset(timezone, 30000));

        assertEquals(0, TimeService.getCurrentDayTime(timezone, 36000));
        assertEquals(-12000, TimeService.getCurrentDayOffset(timezone, 36000));

        assertEquals(6000, TimeService.getCurrentDayTime(timezone, 42000));
        assertEquals(-12000, TimeService.getCurrentDayOffset(timezone, 42000));
    }
}