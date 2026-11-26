package hero.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProgressionTest {

    @Test
    void startsAtMinimumLevelWithoutExperience() {
        Progression progression = new Progression();

        assertEquals(Progression.MIN_LEVEL, progression.getLevel());
        assertEquals(0, progression.getExperience());
    }

    @Test
    void staysAtSameLevelBelowNextThreshold() {
        Progression progression = new Progression().gainExperience(299);

        assertEquals(1, progression.getLevel());
    }

    @Test
    void gainsOneLevelWhenReachingExactThreshold() {
        Progression progression = new Progression().gainExperience(300);

        assertEquals(2, progression.getLevel());
    }

    @Test
    void gainsSeveralLevelsAtOnce() {
        Progression progression = new Progression().gainExperience(2700);

        assertEquals(4, progression.getLevel());
    }

    @Test
    void accumulatesExperienceAcrossGains() {
        Progression progression = new Progression().gainExperience(200).gainExperience(100);

        assertEquals(2, progression.getLevel());
        assertEquals(300, progression.getExperience());
    }

    @Test
    void neverExceedsMaximumLevel() {
        Progression progression = new Progression().gainExperience(100_000);

        assertEquals(Progression.MAX_LEVEL, progression.getLevel());
        assertEquals(Progression.MAX_LEVEL, progression.gainExperience(10_000).getLevel());
    }

    @Test
    void leavesTheOriginalUnchanged() {
        Progression original = new Progression();

        original.gainExperience(300);

        assertEquals(new Progression(), original);
    }

    @Test
    void rejectsNegativeExperience() {
        Progression progression = new Progression();

        assertThrows(IllegalArgumentException.class, () -> progression.gainExperience(-1));
    }
}
