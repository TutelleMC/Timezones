package io.github.metriximor.timezones.tasks;

import io.github.metriximor.timezones.services.PlayerService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OnlinePlayerTimeUpdaterTask implements Runnable {
    private final PlayerService playerService;

    @Override
    public void run() {
        playerService.refreshPlayerTimeForAllPlayers();
    }
}
