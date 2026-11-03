package hero.domain;

public enum Species {

    HUMAN(1, 1, 1, 1, 1, 1),
    ELF(0, 2, 0, 1, 0, 0),
    DWARF(1, 0, 2, 0, 0, 0),
    ORC(2, 0, 1, -1, 0, 0);

    private final int strengthBonus;
    private final int dexterityBonus;
    private final int constitutionBonus;
    private final int intelligenceBonus;
    private final int wisdomBonus;
    private final int charismaBonus;

    Species(int strengthBonus, int dexterityBonus, int constitutionBonus,
            int intelligenceBonus, int wisdomBonus, int charismaBonus) {
        this.strengthBonus = strengthBonus;
        this.dexterityBonus = dexterityBonus;
        this.constitutionBonus = constitutionBonus;
        this.intelligenceBonus = intelligenceBonus;
        this.wisdomBonus = wisdomBonus;
        this.charismaBonus = charismaBonus;
    }

    public int bonusFor(Characteristic characteristic) {
        switch (characteristic) {
            case STRENGTH:
                return strengthBonus;
            case DEXTERITY:
                return dexterityBonus;
            case CONSTITUTION:
                return constitutionBonus;
            case INTELLIGENCE:
                return intelligenceBonus;
            case WISDOM:
                return wisdomBonus;
            case CHARISMA:
                return charismaBonus;
            default:
                throw new IllegalStateException("Caractéristique inconnue: " + characteristic);
        }
    }
}
