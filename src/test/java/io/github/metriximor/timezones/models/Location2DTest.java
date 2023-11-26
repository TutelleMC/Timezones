package io.github.metriximor.timezones.models;

import de.jilocasin.nearestneighbour.kdtree.KdPoint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Location2DTest {
    @Test
    void fromKdPointWorks() {
        final var kdPoint = new KdPoint<>(40.0, 180.0);
        assertEquals(new Location2D(40.0, 180.0), Location2D.fromKdPoint(kdPoint));
    }
}