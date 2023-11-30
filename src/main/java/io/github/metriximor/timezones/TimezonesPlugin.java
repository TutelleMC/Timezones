package io.github.metriximor.timezones;

import io.github.metriximor.timezones.commands.TimezoneCommand;
import io.github.metriximor.timezones.events.DaylightSensorEvents;
import io.github.metriximor.timezones.events.MobCombustingEvents;
import io.github.metriximor.timezones.events.MobSpawningEvents;
import io.github.metriximor.timezones.events.PlayerTimeUpdaterEvents;
import io.github.metriximor.timezones.services.ConfigService;
import io.github.metriximor.timezones.services.*;
import io.github.metriximor.timezones.tasks.OnlinePlayerTimeUpdaterTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public final class TimezonesPlugin extends JavaPlugin {
    private final Logger logger = getLogger();
    private final PluginManager pluginManager = getServer().getPluginManager();
    private final WorldsService worldsService = new WorldsService();
    private final ConfigService configService = new ConfigService(logger, worldsService, getConfig());
    private final TimezonesService timezonesService = new TimezonesService(logger, configService.getPluginConfig(), worldsService);
    private final PlayerService playerService = new PlayerService(logger, timezonesService);
    private final OnlinePlayerTimeUpdaterTask onlinePlayerTimeUpdaterTask = new OnlinePlayerTimeUpdaterTask(playerService);
    private final TasksService tasksService = new TasksService(logger, Bukkit.getScheduler(), configService.getPluginConfig(), onlinePlayerTimeUpdaterTask);
    private final StateService stateService = new StateService(logger, timezonesService, playerService, tasksService);
    private final PlayerTimeUpdaterEvents playerTimeUpdaterEvents = new PlayerTimeUpdaterEvents(playerService, stateService);
    private final MobCombustingEvents mobCombustingEvents = new MobCombustingEvents(logger, timezonesService, stateService);
    private final MobSpawningEvents mobSpawningEvents = new MobSpawningEvents(logger, timezonesService, stateService);
    private final DaylightSensorEvents daylightSensorEvents = new DaylightSensorEvents(logger, stateService, timezonesService);
    private final TimezoneCommand timezoneCommand = new TimezoneCommand(getVersion(), stateService);

    @Override
    public void onEnable() {
        saveDefaultConfig();
        logger.info("Initializing Timezones plugin...");
        Objects.requireNonNull(this.getCommand("timezones")).setExecutor(timezoneCommand);

        pluginManager.registerEvents(playerTimeUpdaterEvents, this);
        pluginManager.registerEvents(mobCombustingEvents, this);
        pluginManager.registerEvents(mobSpawningEvents, this);
        pluginManager.registerEvents(daylightSensorEvents, this);

        stateService.toggleState();

        logger.info("Timezones Plugin loaded successfully");
    }

    private static String getVersion() {
        final String version = TimezonesPlugin.class.getPackage().getImplementationVersion();
        return version == null ? "dev" : version;
    }
}
