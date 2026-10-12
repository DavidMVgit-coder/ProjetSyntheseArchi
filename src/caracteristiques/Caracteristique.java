package caracteristiques;

public enum Caracteristique {
    FORCE,
    DEXTERITE,
    CONSTITUTION,
    INTELLIGENCE,
    SAGESSE,
    CHARISME;

    public static final int VALEUR_MIN_CREATION = 3;
    public static final int VALEUR_MAX_CREATION = 18;
    public static final int VALEUR_MAX_ABSOLUE = 20;

    public static int calculerModificateur(int valeur) {
        return Math.floorDiv(valeur - 10, 2);
    }
}
