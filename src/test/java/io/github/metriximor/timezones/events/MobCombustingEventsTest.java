package io.github.metriximor.timezones.events;

import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MobCombustingEventsTest {
    @Test
    void testBurnsInDaylightWorksAsExpected() {
        assertTrue(MobCombustingEvents.burnsInDaylight(EntityType.ZOMBIE));
        assertFalse(MobCombustingEvents.burnsInDaylight(EntityType.VILLAGER));
    }
}