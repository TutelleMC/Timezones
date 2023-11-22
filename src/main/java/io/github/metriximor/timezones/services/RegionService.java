package io.github.metriximor.timezones.services;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import io.github.metriximor.timezones.models.Timezone;
import io.github.metriximor.timezones.models.WorldConfig;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;

import java.util.logging.Logger;

@RequiredArgsConstructor
public class RegionService {
    private final WorldGuard worldGuard = WorldGuard.getInstance();
    private final Logger logger;
    private final TimezonesService timezonesService;

    public void registerRegions(final World world, final WorldConfig worldConfig) {
        var regionManager = getRegionContainer().get(BukkitAdapter.adapt(world));
        if (regionManager == null) {
            throw new IllegalArgumentException(String.format("Region manager for world %s is not available!", world));
        }

        timezonesService.instantiateTimezones(worldConfig)
                .stream()
                .map(Timezone::toRegion)
                .forEach(regionManager::addRegion);
        logger.info(String.format("All regions added successfully to world: %s", world.getName()));
    }

    private RegionContainer getRegionContainer() {
         return worldGuard.getPlatform().getRegionContainer();
    }
}
