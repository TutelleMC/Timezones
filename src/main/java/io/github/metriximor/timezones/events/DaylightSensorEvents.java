package io.github.metriximor.timezones.events;

import io.github.metriximor.timezones.models.Timezone;
import io.github.metriximor.timezones.services.StateService;
import io.github.metriximor.timezones.services.TimeService;
import io.github.metriximor.timezones.services.TimezonesService;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.data.type.DaylightDetector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

import static io.github.metriximor.timezones.models.Location2D.fromLocation;

@RequiredArgsConstructor
public class DaylightSensorEvents implements Listener {
    private static final int MAX_POWER = 15;
    private static final int NO_POWER = 0;
    private final Logger logger;
    private final StateService stateService;
    private final TimezonesService timezonesService;

    @EventHandler
    void onDaylightSensorUpdate(@NotNull final BlockRedstoneEvent event) {
        if (!stateService.isEnabled()) {
            return;
        }

        var block = event.getBlock();
        if (!(block.getState().getBlockData() instanceof DaylightDetector daylightDetector)) {
            logger.finest("Not tracking daylight detector at %s because its type %s is not daylight detector".formatted(block.getLocation(), block.getType()));
            return;
        }
        var isNightSensor = daylightDetector.isInverted();

        var world = block.getWorld();
        var location = block.getLocation();
        var timezone = timezonesService.findNearestTimezone(world.getName(), fromLocation(location));
        if (timezone.isEmpty()) {
            return;
        }
        var isDayAtZeroZero = TimeService.isDayAt(Timezone.greenwich(), world.getFullTime());
        var isDay = TimeService.isDayAt(timezone.get(), world.getFullTime());

        final int newCurrent;
        if (isDayAtZeroZero) {
            newCurrent = getNewCurrent(isDay, isNightSensor);
        } else {
            newCurrent = getNewCurrent(isDay, !isNightSensor);
        }

        logger.finer("Daylight sensor at %s was set to current %s".formatted(location, newCurrent));
        event.setNewCurrent(newCurrent);
    }

    // if minecraft is day (for server time) and the daylight detector is inverted, it will not poll.
    // a solution to this problem is to invert the sensor functionality, so at the opposite ends of the world
    // we let people set to daylight mode for night and nighttime mode for day
    // in the future if this is really important we can always try to have timezones emit custom events on
    // sunset and sunrise, and then hook into those to force a change in the daylight detector
    private static int getNewCurrent(boolean isDay, boolean isNightSensor) {
        final int newCurrent;
        if (isDay) {
            newCurrent = isNightSensor ? NO_POWER : MAX_POWER;
        } else {
            newCurrent = isNightSensor ? MAX_POWER : NO_POWER;
        }
        return newCurrent;
    }
}
