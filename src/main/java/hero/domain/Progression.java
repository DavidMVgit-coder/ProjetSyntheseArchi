package hero.domain;

import java.util.Arrays;

public final class Progression {

    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 5;

    private static final int[] XP_REQUIRED_PER_LEVEL = {0, 300, 900, 2700, 6500};

    private final int level;
    private final int experience;

    public Progression() {
        this(MIN_LEVEL, 0);
    }

    private Progression(int level, int experience) {
        this.level = level;
        this.experience = experience;
    }

    public Progression gainExperience(int experiencePoints) {
        if (experiencePoints < 0) {
            throw new IllegalArgumentException("L'expérience gagnée ne peut pas être négative.");
        }

        int newExperience = experience + experiencePoints;
        return new Progression(levelForExperience(newExperience), newExperience);
    }

    private static int levelForExperience(int experience) {
        return (int) Arrays.stream(XP_REQUIRED_PER_LEVEL)
                .filter(required -> experience >= required)
                .count();
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Progression that && level == that.level && experience == that.experience;
    }

    @Override
    public int hashCode() {
        return 31 * level + experience;
    }
}
