package caracteristiques;

import java.util.Map;

public final class JeuDeCaracteristiques {

    private final Map<Caracteristique, Integer> valeurs;

    JeuDeCaracteristiques(Map<Caracteristique, Integer> valeurs) {
        this.valeurs = valeurs;
    }

    public int getValeur(Caracteristique caracteristique) {
        return valeurs.get(caracteristique);
    }

    public int getModificateur(Caracteristique caracteristique) {
        return Caracteristique.calculerModificateur(getValeur(caracteristique));
    }
}
