package io.github.metriximor.timezones.services;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import io.github.metriximor.timezones.models.Timezone;
import io.github.metriximor.timezones.models.WorldConfig;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class RegionService {
    private static final String TIMEZONES_REGION_ID = "timezones";
    private final WorldGuard worldGuard = WorldGuard.getInstance();
    private final Logger logger;
    private final TimezonesService timezonesService;


    public void registerRegions(final World world, final WorldConfig worldConfig) {
        var regionManager = getRegionManager(world);

        var regions = mapWithParent(timezonesService.instantiateTimezones(worldConfig));
        if (regions.size() == 1) {
            logger.warning("Didn't register any regions since none were instantiated.");
            return;
        }

        regions.forEach(regionManager::addRegion);
        logger.info(String.format("All regions added successfully to world: %s", world.getName()));
    }

    public void unregisterRegions(@NotNull final World world) {
        var regionManager = getRegionManager(world);
        regionManager.removeRegion(TIMEZONES_REGION_ID, RemovalStrategy.REMOVE_CHILDREN);
    }

    @NotNull
    private List<ProtectedRegion> mapWithParent(@NotNull final List<Timezone> timezones) {
        final var parentRegion = new ProtectedCuboidRegion(TIMEZONES_REGION_ID, BlockVector3.ZERO, BlockVector3.ONE);
        return timezones.stream().map(timezone -> {
            var region = timezone.toRegion();
            try {
                region.setParent(parentRegion);
                return region;
            } catch (ProtectedRegion.CircularInheritanceException e) {
                logger.severe("Failed to enable timezone %s due to error: %s".formatted(timezone, e));
                return null;
            }
        }).filter(Objects::nonNull).toList();
    }

    private RegionContainer getRegionContainer() {
        return worldGuard.getPlatform().getRegionContainer();
    }

    private RegionManager getRegionManager(@NotNull final World world) {
        var regionManager = getRegionContainer().get(BukkitAdapter.adapt(world));
        if (regionManager == null) {
            throw new IllegalArgumentException(String.format("Region manager for world %s is not available!", world));
        }
        return regionManager;
    }
}
