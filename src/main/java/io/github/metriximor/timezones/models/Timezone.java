package io.github.metriximor.timezones.models;

import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.jetbrains.annotations.NotNull;

/**
 * @param offset In hours
 */
public record Timezone(
        @NotNull String id,
        @NotNull BlockVector2 upperLeftCorner,
        @NotNull BlockVector2 lowerRightCorner,
        int offset,
        @NotNull WorldConfig worldConfig
) {
    @NotNull
    public ProtectedRegion toRegion() {
        var region = new ProtectedCuboidRegion(
                id,
                upperLeftCorner.toBlockVector3(worldConfig.maxWorldHeight()),
                lowerRightCorner.toBlockVector3(worldConfig.minWorldHeight())
        );
        var timeOffset = offset * 1000;
        region.setFlag(Flags.TIME_LOCK, (timeOffset > 0) ? "+" + timeOffset : Integer.toString(timeOffset));
        return region;
    }
}
