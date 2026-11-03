package hero.domain;

public enum HeroClass {

    WARRIOR(12, 0, Characteristic.STRENGTH),
    RANGER(10, 0, Characteristic.DEXTERITY),
    MAGE(6, 10, Characteristic.INTELLIGENCE),
    CLERIC(10, 8, Characteristic.WISDOM);

    private final int baseHp;
    private final int maxManaLevel1;
    private final Characteristic dominantAttribute;

    HeroClass(int baseHp, int maxManaLevel1, Characteristic dominantAttribute) {
        this.baseHp = baseHp;
        this.maxManaLevel1 = maxManaLevel1;
        this.dominantAttribute = dominantAttribute;
    }

    public int getBaseHp() {
        return baseHp;
    }

    public int getMaxManaLevel1() {
        return maxManaLevel1;
    }

    public Characteristic getDominantAttribute() {
        return dominantAttribute;
    }
}
