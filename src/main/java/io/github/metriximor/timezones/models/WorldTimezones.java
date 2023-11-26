package io.github.metriximor.timezones.models;

import de.jilocasin.nearestneighbour.kdtree.KdTree;
import de.jilocasin.nearestneighbour.nnsolver.NNSolver;
import io.github.metriximor.timezones.models.configs.WorldConfig;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.github.metriximor.timezones.models.Location2D.fromKdPoint;

public class WorldTimezones {
    @Getter
    @NotNull
    private final WorldConfig worldConfig;
    @Getter
    @NotNull
    private final Map<Location2D, Timezone> timezones;
    @NotNull
    private final NNSolver<Double> solver;

    public WorldTimezones(@NotNull final WorldConfig worldConfig,
                          @NotNull final Map<Location2D, Timezone> timezones) {
        if (timezones.isEmpty()) {
            throw new IllegalArgumentException("timezones can't be empty");
        }
        this.worldConfig = worldConfig;
        this.timezones = timezones;
        this.solver = new NNSolver<>(new KdTree<>(timezones.keySet().stream().map(Location2D::toKdPoint).toList()));
    }

    @NotNull
    public Timezone findNearestTimezone(@NotNull final Location2D location) {
        final var nearestTimezoneKey = fromKdPoint(solver.findNearestPoint(location.toKdPoint()));
        return timezones.get(nearestTimezoneKey);
    }
}
