package domain.caracteristiques;

public enum Characteristic {
    STRENGTH,
    DEXTERITY,
    CONSTITUTION,
    INTELLIGENCE,
    WISDOM,
    CHARISMA;

    public static final int MIN_CREATION_VALUE = 3;
    public static final int MAX_CREATION_VALUE = 18;
    public static final int ABSOLUTE_MAX_VALUE = 20;

    public static int calculateModifier(int value) {
        return Math.floorDiv(value - 10, 2);
    }
}
