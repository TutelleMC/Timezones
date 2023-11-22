package io.github.metriximor.timezones;

import io.github.metriximor.timezones.models.WorldConfig;
import io.github.metriximor.timezones.services.RegionService;
import io.github.metriximor.timezones.services.TimezonesService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@SuppressWarnings("unused")
public final class TimezonesPlugin extends JavaPlugin {
    private final Logger logger = getLogger();
    private final WorldConfig worldConfig = new WorldConfig(600, 300, 24, -64, 320);
    private final TimezonesService timezonesService = new TimezonesService(logger);
    private final RegionService regionService = new RegionService(logger, timezonesService);

    @Override
    public void onEnable() {
        logger.info("Initializing Timezones plugin...");
        var world = Bukkit.getWorld("world");
        regionService.registerRegions(world, worldConfig);
        logger.info("Timezones Plugin loaded successfully");
    }
}
