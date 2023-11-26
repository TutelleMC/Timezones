package io.github.metriximor.timezones.services;

import io.github.metriximor.timezones.models.Location2D;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

@RequiredArgsConstructor
public class PlayerService {
    private final Logger logger;
    private final TimezonesService timezonesService;

    public void refreshPlayerTimeForAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::refreshPlayerTime);
    }

    public void refreshPlayerTime(@NotNull final Player player) {
        var location = player.getLocation();
        var world = location.getWorld().getName();
        var nearestTimezone = timezonesService.findNearestTimezone(world, Location2D.fromLocation(player.getLocation()));
        if (nearestTimezone.isEmpty()) { // World doesn't have timezones set up
            return;
        }
        var relativePlayerTime = nearestTimezone.get().currentTime();
        logger.finest("Refreshed player %s relative time to %s".formatted(player.getName(), relativePlayerTime));
        player.setPlayerTime(relativePlayerTime, true);
    }

    public void resetPlayerTimeForAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(Player::resetPlayerTime);
    }
}
