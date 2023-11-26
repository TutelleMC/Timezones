package io.github.metriximor.timezones.events;

import io.github.metriximor.timezones.services.PlayerService;
import io.github.metriximor.timezones.services.StateService;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class PlayerTimeUpdaterEvents implements Listener {
    private final PlayerService playerService;
    private final StateService stateService;

    @EventHandler
    public void onPlayerJoin(@NotNull final PlayerJoinEvent event) {
        handlePlayerEvent(event);
    }

    @EventHandler
    public void onPlayerMoveWorlds(@NotNull final PlayerChangedWorldEvent event) {
        handlePlayerEvent(event);
    }

    @EventHandler
    public void onPlayerTeleport(@NotNull final PlayerTeleportEvent event) {
        handlePlayerEvent(event);
    }

    @EventHandler
    public void onPlayerRespawn(@NotNull final PlayerRespawnEvent event) {
        handlePlayerEvent(event);
    }

    private void handlePlayerEvent(@NotNull final PlayerEvent event) {
        if (stateService.isEnabled()) {
            playerService.refreshPlayerTime(event.getPlayer());
        }
    }
}
