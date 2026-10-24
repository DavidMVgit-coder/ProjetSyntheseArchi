package hero.domain;

public final class Progression {

    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 5;

    private static final int[] XP_REQUIRED_PER_LEVEL = {0, 300, 900, 2700, 6500};

    private int level = MIN_LEVEL;
    private int experience = 0;

    public int gainExperience(int experiencePoints) {
        if (experiencePoints < 0) {
            throw new IllegalArgumentException("L'expérience gagnée ne peut pas être négative.");
        }

        experience += experiencePoints;

        int levelsGained = 0;

        while (level < MAX_LEVEL && experience >= XP_REQUIRED_PER_LEVEL[level]) {
            level++;
            levelsGained++;
        }

        return levelsGained;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }
}
