package io.github.metriximor.timezones;

import io.github.metriximor.timezones.commands.TimezoneCommand;
import io.github.metriximor.timezones.models.WorldConfig;
import io.github.metriximor.timezones.services.RegionService;
import io.github.metriximor.timezones.services.TimezonesService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public final class TimezonesPlugin extends JavaPlugin {
    private final Logger logger = getLogger();
    private final TimezonesService timezonesService = new TimezonesService(logger);
    private final RegionService regionService = new RegionService(logger, timezonesService);
    private final TimezoneCommand timezoneCommand = new TimezoneCommand(getVersion(), regionService, logger);

    @Override
    public void onEnable() {
        logger.info("Initializing Timezones plugin...");
        Objects.requireNonNull(this.getCommand("timezone")).setExecutor(timezoneCommand);

        var world = Bukkit.getWorld("world");
        regionService.registerRegions(world, getWorldConfig());
        logger.info("Timezones Plugin loaded successfully");
    }

    public static WorldConfig getWorldConfig() {
        return new WorldConfig(600, 300, 24, -64, 320);
    }

    private static String getVersion() {
        final String version = TimezonesPlugin.class.getPackage().getImplementationVersion();
        return version == null ? "dev" : version;
    }
}
