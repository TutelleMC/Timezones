package io.github.metriximor.timezones.models;

import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldguard.protection.flags.Flags;
import io.github.metriximor.timezones.Utils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimezoneTest {
    @Test
    void testToRegionSetsTheTimeOffsetFlagCorrectlyWithPositiveOffset() {
        var upperLeft = BlockVector2.at(-1, -1);
        var bottomRight = BlockVector2.at(1, 1);
        var timezone = new Timezone("id", upperLeft, bottomRight, 1, Utils.getWorldConfig()).toRegion();
        var result = timezone.getFlag(Flags.TIME_LOCK);
        assertEquals("+1000", result);
    }


    @Test
    void testToRegionSetsTheTimeOffsetFlagCorrectlyWithNegativeOffset() {
        var upperLeft = BlockVector2.at(-1, -1);
        var bottomRight = BlockVector2.at(1, 1);
        var timezone = new Timezone("id", upperLeft, bottomRight, -1, Utils.getWorldConfig()).toRegion();
        var result = timezone.getFlag(Flags.TIME_LOCK);
        assertEquals("-1000", result);
    }
}
