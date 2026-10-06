package caracteristiques;

public enum Espece {

    HUMAIN(1, 1, 1, 1, 1, 1),
    ELFE(0, 2, 0, 1, 0, 0),
    NAIN(1, 0, 2, 0, 0, 0),
    ORC(2, 0, 1, -1, 0, 0);

    private final int bonusForce;
    private final int bonusDexterite;
    private final int bonusConstitution;
    private final int bonusIntelligence;
    private final int bonusSagesse;
    private final int bonusCharisme;

    Espece(int bonusForce, int bonusDexterite, int bonusConstitution,
           int bonusIntelligence, int bonusSagesse, int bonusCharisme) {
        this.bonusForce = bonusForce;
        this.bonusDexterite = bonusDexterite;
        this.bonusConstitution = bonusConstitution;
        this.bonusIntelligence = bonusIntelligence;
        this.bonusSagesse = bonusSagesse;
        this.bonusCharisme = bonusCharisme;
    }

    public int bonusPour(Caracteristique caracteristique) {
        switch (caracteristique) {
            case FORCE:
                return bonusForce;
            case DEXTERITE:
                return bonusDexterite;
            case CONSTITUTION:
                return bonusConstitution;
            case INTELLIGENCE:
                return bonusIntelligence;
            case SAGESSE:
                return bonusSagesse;
            case CHARISME:
                return bonusCharisme;
            default:
                throw new IllegalStateException("Caractéristique inconnue : " + caracteristique);
        }
    }
}
