package io.github.metriximor.timezones.events;

import io.github.metriximor.timezones.services.StateService;
import io.github.metriximor.timezones.services.TimeService;
import io.github.metriximor.timezones.services.TimezonesService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.logging.Logger;

import static io.github.metriximor.timezones.models.Location2D.fromLocation;

@RequiredArgsConstructor
public class MobCombustingEvents implements Listener {
    private final Logger logger;
    private final TimezonesService timezonesService;
    private final StateService stateService;

    @EventHandler(priority = EventPriority.HIGH)
    private void onEntityCombust(@NotNull final EntityCombustEvent event) {
        if (!stateService.isEnabled()) {
            return;
        }

        if (event instanceof EntityCombustByEntityEvent || event instanceof EntityCombustByBlockEvent) {
            return;
        }

        final var entity = event.getEntity();
        if (!burnsInDaylight(entity.getType())) { //let the entity burn
            return;
        }

        final var world = event.getEntity().getWorld();
        final var timezone = timezonesService
                .findNearestTimezone(world.getName(), fromLocation(event.getEntity().getLocation()));

        if (timezone.isEmpty()) { // entity is in a world with no timezones
            return;
        }

        var shouldCancelSpawn = TimeService.isNightAt(timezone.get(), world.getTime());
        logger.finer("Preventing mob %s at %s from being set on fire: %s".formatted(entity, entity.getLocation(), shouldCancelSpawn));
        event.setCancelled(shouldCancelSpawn);
    }

    protected static boolean burnsInDaylight(@NotNull final EntityType type) {
        final var mobsThatBurnInDaylight = Set.of(
                EntityType.DROWNED,
                EntityType.MAGMA_CUBE,
                EntityType.PHANTOM,
                EntityType.SKELETON,
                EntityType.STRAY,
                EntityType.ZOMBIE_VILLAGER,
                EntityType.ZOMBIE
        );
        return mobsThatBurnInDaylight.contains(type);
    }
}
