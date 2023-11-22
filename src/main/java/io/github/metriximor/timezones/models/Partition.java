package io.github.metriximor.timezones.models;

import java.util.ArrayList;
import java.util.List;

public record Partition(int size, int numberOfIntervals, int startingValue) {
    public record Interval(int lowerBound, int upperBound) {
    }

    public List<Interval> getAllIntervals() {
        final List<Interval> ranges = new ArrayList<>();
        final int intervalSize = (size / numberOfIntervals) - 1;
        int lowerBound = startingValue - 1;
        for (int i = 0; i < numberOfIntervals; i += 1) {
            lowerBound += 1;
            var upperBound = lowerBound + intervalSize;
            ranges.add(new Interval(lowerBound, upperBound));
            lowerBound = upperBound;
        }
        return ranges;
    }
}
