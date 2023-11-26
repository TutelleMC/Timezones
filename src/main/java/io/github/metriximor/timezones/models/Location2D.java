package io.github.metriximor.timezones.models;

import de.jilocasin.nearestneighbour.kdtree.KdPoint;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public record Location2D(double x, double z) {
    @NotNull
    public static Location2D fromLocation(@NotNull final Location location) {
        return new Location2D(location.getX(), location.getZ());
    }

    @NotNull
    public static Location2D fromKdPoint(@NotNull final KdPoint<Double> kdPoint) {
        return new Location2D(kdPoint.getAxisValue(0), kdPoint.getAxisValue(1));
    }

    @NotNull
    public KdPoint<Double> toKdPoint() {
        return new KdPoint<>(x, z);
    }
}
