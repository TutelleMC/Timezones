package io.github.metriximor.timezones.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PartitionTest {
    @Test
    void testGetAllIntervalsWorksCorrectlyWithStartingValueZero() {
        var intervals = new Partition(600, 24, 0).getAllIntervals();
        assertEquals(24, intervals.size());
        assertEquals(0, intervals.get(0).lowerBound());
        assertEquals(599, intervals.get(intervals.size() - 1).upperBound());
    }

    @Test
    void testGetAllIntervalsWorksCorrectlyWithNonZeroStartingValue() {
        var intervals = new Partition(600, 24, -300).getAllIntervals();
        assertEquals(24, intervals.size());
        assertEquals(-300, intervals.get(0).lowerBound());
        assertEquals(299, intervals.get(intervals.size() - 1).upperBound());
    }

    @Test
    void testGetAllIntervalsRespectsTheNumberOfIntervals() {
        assertEquals(24, new Partition(600, 24, 0).getAllIntervals().size());
        assertEquals(12, new Partition(600, 12, 0).getAllIntervals().size());
        assertEquals(3, new Partition(12, 3, 0).getAllIntervals().size());
    }

    @Test
    void testIntervalsDoNotOverlap() {
        final var intervals = new Partition(600, 24, -300).getAllIntervals();
        var lowestValueSoFar = Integer.MIN_VALUE;
        for(final var interval : intervals) {
            assertTrue(lowestValueSoFar < interval.lowerBound());
            assertTrue(interval.lowerBound() < interval.upperBound());
            lowestValueSoFar = interval.upperBound();
        }
    }
}