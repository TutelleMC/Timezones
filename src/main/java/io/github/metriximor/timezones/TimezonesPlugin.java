package io.github.metriximor.timezones;

import io.github.metriximor.timezones.commands.TimezoneCommand;
import io.github.metriximor.timezones.events.MobCombustingEvents;
import io.github.metriximor.timezones.events.MobSpawningEvents;
import io.github.metriximor.timezones.events.PlayerTimeUpdaterEvents;
import io.github.metriximor.timezones.models.configs.PluginConfig;
import io.github.metriximor.timezones.models.configs.WorldConfig;
import io.github.metriximor.timezones.services.*;
import io.github.metriximor.timezones.tasks.OnlinePlayerTimeUpdaterTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

import static io.github.metriximor.timezones.models.configs.PluginConfig.getDefaultPluginConfig;

@SuppressWarnings("unused")
public final class TimezonesPlugin extends JavaPlugin {
    private final Logger logger = getLogger();
    private final PluginConfig pluginConfig = getDefaultPluginConfig();
    private final PluginManager pluginManager = getServer().getPluginManager();
    private final WorldsService worldsService = new WorldsService();
    private final TimezonesService timezonesService = new TimezonesService(logger, pluginConfig, worldsService);
    private final PlayerService playerService = new PlayerService(logger, timezonesService);
    private final OnlinePlayerTimeUpdaterTask onlinePlayerTimeUpdaterTask = new OnlinePlayerTimeUpdaterTask(playerService);
    private final TasksService tasksService = new TasksService(logger, Bukkit.getScheduler(), pluginConfig, onlinePlayerTimeUpdaterTask);
    private final StateService stateService = new StateService(logger, timezonesService, playerService, tasksService);
    private final PlayerTimeUpdaterEvents playerTimeUpdaterEvents = new PlayerTimeUpdaterEvents(playerService, stateService);
    private final MobCombustingEvents mobCombustingEvents = new MobCombustingEvents(logger, timezonesService, stateService);
    private final MobSpawningEvents mobSpawningEvents = new MobSpawningEvents(logger, timezonesService, stateService);
    private final TimezoneCommand timezoneCommand = new TimezoneCommand(getVersion(), stateService);

    @Override
    public void onEnable() {
        logger.info("Initializing Timezones plugin...");
        Objects.requireNonNull(this.getCommand("timezone")).setExecutor(timezoneCommand);
        pluginManager.registerEvents(playerTimeUpdaterEvents, this);
        pluginManager.registerEvents(mobCombustingEvents, this);
        pluginManager.registerEvents(mobSpawningEvents, this);

        worldsService.addWorldConfig(new WorldConfig("world", 1000, 500, 0, -64, 320));
        stateService.toggleState();

        logger.info("Timezones Plugin loaded successfully");
    }

    private static String getVersion() {
        final String version = System.getenv("VERSION");
        return version == null ? "dev" : version;
    }
}
