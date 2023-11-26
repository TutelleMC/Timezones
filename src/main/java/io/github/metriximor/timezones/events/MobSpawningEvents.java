package io.github.metriximor.timezones.events;

import io.github.metriximor.timezones.services.StateService;
import io.github.metriximor.timezones.services.TimeService;
import io.github.metriximor.timezones.services.TimezonesService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

import static io.github.metriximor.timezones.models.Location2D.fromLocation;
import static org.bukkit.event.entity.CreatureSpawnEvent.*;
import static org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.DEFAULT;
import static org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.NATURAL;

@RequiredArgsConstructor
public class MobSpawningEvents implements Listener {
    private final Logger logger;
    private final TimezonesService timezonesService;
    private final StateService stateService;

    @EventHandler
    public void onMobSpawning(@NotNull final CreatureSpawnEvent event) {
        if (!stateService.isEnabled()) {
            return;
        }
        if (!(event.getEntity() instanceof Monster)) {
            // we only care about monster spawns
            return;
        }

        if (!isNaturalOrDefaultSpawnReason(event.getSpawnReason())) {
            // we don't interfere with non-natural spawn reasons
            return;
        }

        var world = event.getEntity().getWorld();
        var nearest = timezonesService.findNearestTimezone(world.getName(), fromLocation(event.getLocation()));
        if (nearest.isEmpty()) {
            return;
        }
        var isDayAt = TimeService.isDayAt(nearest.get(), world.getTime());
        logger.finer("Mob %s at %s was prevented from spawning: %s".formatted(event.getEntity(), event.getLocation(), isDayAt));
        event.setCancelled(isDayAt);
    }

    private static boolean isNaturalOrDefaultSpawnReason(@NotNull final SpawnReason spawnReason) {
        return spawnReason.equals(DEFAULT) || spawnReason.equals(NATURAL);
    }
}
