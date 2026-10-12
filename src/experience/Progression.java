package experience;

public final class Progression {

    public static final int NIVEAU_MIN = 1;
    public static final int NIVEAU_MAX = 5;

    private static final int[] XP_REQUIS_PAR_NIVEAU = {0, 300, 900, 2700, 6500};

    private int niveau = NIVEAU_MIN;
    private int experience = 0;

    public int gagnerExperience(int pointsExperience) {
        if (pointsExperience < 0) {
            throw new IllegalArgumentException("L'expérience gagnée ne peut pas être négative.");
        }
        experience += pointsExperience;

        int niveauxGagnes = 0;
        while (niveau < NIVEAU_MAX && experience >= XP_REQUIS_PAR_NIVEAU[niveau]) {
            niveau++;
            niveauxGagnes++;
        }
        return niveauxGagnes;
    }

    public int getNiveau() {
        return niveau;
    }

    public int getExperience() {
        return experience;
    }
}
