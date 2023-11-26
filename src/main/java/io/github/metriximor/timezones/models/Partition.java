package io.github.metriximor.timezones.models;

import java.util.ArrayList;
import java.util.List;

public record Partition(int size, int numberOfIntervals, int startingValue) {
    public record Interval(int lowerBound, int upperBound) {
    }

    /**
     * Finds the non overlapping integer intervals given the Partition details.
     * <br>
     * For example, on a 600 size partition, with 24 intervals and a -299 starting value:
     * <table><thead><tr><th>lowerBound</th><th>upperBound</th></tr></thead><tbody><tr><td>-299</td><td>-275</td></tr><tr><td>-274</td><td>-250</td></tr><tr><td>-249</td><td>-225</td></tr><tr><td>-224</td><td>-200</td></tr><tr><td>-199</td><td>-175</td></tr><tr><td>-174</td><td>-150</td></tr><tr><td>-149</td><td>-125</td></tr><tr><td>-124</td><td>-100</td></tr><tr><td>-99</td><td>-75</td></tr><tr><td>-74</td><td>-50</td></tr><tr><td>-49</td><td>-25</td></tr><tr><td>-24</td><td>0</td></tr><tr><td>1</td><td>25</td></tr><tr><td>26</td><td>50</td></tr><tr><td>51</td><td>75</td></tr><tr><td>76</td><td>100</td></tr><tr><td>101</td><td>125</td></tr><tr><td>126</td><td>150</td></tr><tr><td>151</td><td>175</td></tr><tr><td>176</td><td>200</td></tr><tr><td>201</td><td>225</td></tr><tr><td>226</td><td>250</td></tr><tr><td>251</td><td>275</td></tr><tr><td>276</td><td>300</td></tr></tbody></table>
     */
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

    public List<Double> getAllCenterPoints() {
        final List<Double> centerPoints = new ArrayList<>();
        final double intervalSize = (double) size / numberOfIntervals;
        double value = startingValue;
        for (int i = 0; i <= numberOfIntervals; i += 1) {
            centerPoints.add(value);
            value += intervalSize;
        }
        return centerPoints;
    }
}
