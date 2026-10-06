package classe;

import caracteristiques.Caracteristique;

public enum Classe {

    GUERRIER(12, 0, Caracteristique.FORCE),
    RODEUR(10, 0, Caracteristique.DEXTERITE),
    MAGE(6, 10, Caracteristique.INTELLIGENCE),
    CLERC(10, 8, Caracteristique.SAGESSE);

    private final int pvBase;
    private final int magieMaxNiveau1;
    private final Caracteristique aptitudeDominante;

    Classe(int pvBase, int magieMaxNiveau1, Caracteristique aptitudeDominante) {
        this.pvBase = pvBase;
        this.magieMaxNiveau1 = magieMaxNiveau1;
        this.aptitudeDominante = aptitudeDominante;
    }

    public int getPvBase() {
        return pvBase;
    }

    public int getMagieMaxNiveau1() {
        return magieMaxNiveau1;
    }

    public Caracteristique getAptitudeDominante() {
        return aptitudeDominante;
    }
}
