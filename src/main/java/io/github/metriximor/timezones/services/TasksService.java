package io.github.metriximor.timezones.services;

import io.github.metriximor.timezones.TimezonesPlugin;
import io.github.metriximor.timezones.models.configs.PluginConfig;
import io.github.metriximor.timezones.tasks.OnlinePlayerTimeUpdaterTask;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class TasksService {
    private final Logger logger;
    private final BukkitScheduler scheduler;
    private final PluginConfig pluginConfig;
    private final OnlinePlayerTimeUpdaterTask onlinePlayerTimeUpdaterTask;
    private final Map<Integer, BukkitTask> tasks = new HashMap<>();

    public void startupTasks() {
        logger.info("Starting up online player relative time update task, every %s ticks.".formatted(pluginConfig.amountOfTicksTillUpdate()));
        var plugin = JavaPlugin.getPlugin(TimezonesPlugin.class);
        var playerUpdateTask = scheduler.runTaskTimer(plugin, onlinePlayerTimeUpdaterTask, 0, pluginConfig.amountOfTicksTillUpdate());
        tasks.put(playerUpdateTask.getTaskId(), playerUpdateTask);
    }

    public void stopTasks() {
        logger.info("Stopping online player relative time update task.");
        tasks.keySet().forEach(scheduler::cancelTask);
    }
}
