package io.github.metriximor.timezones.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.logging.Logger;

@RequiredArgsConstructor
public class StateService {
    private final Logger logger;
    private final TimezonesService timezonesService;
    private final PlayerService playerService;
    private final TasksService tasksService;

    @Getter
    private boolean isEnabled = false; // starts out disabled by default

    public void toggleState() {
        if (isEnabled) {
            isEnabled = disable();
        } else {
            isEnabled = enable();
        }
    }

    private boolean enable() {
        logger.info("Enabling Timezones.");
        if (!timezonesService.areAllWorldsInstantiated()) {
            timezonesService.instantiateTimezonesInAllWorlds();
        }
        tasksService.startupTasks();
        return true;
    }

    private boolean disable() {
        logger.info("Disabling Timezones.");
        playerService.resetPlayerTimeForAllPlayers();
        tasksService.stopTasks();
        return false;
    }
}
