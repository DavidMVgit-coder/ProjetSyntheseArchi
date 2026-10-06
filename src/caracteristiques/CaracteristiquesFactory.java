package caracteristiques;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class CaracteristiquesFactory {

    private CaracteristiquesFactory() {
    }

    public static JeuDeCaracteristiques creer(Map<Caracteristique, Integer> valeursDeBase, Espece espece) {
        Objects.requireNonNull(valeursDeBase);
        Objects.requireNonNull(espece);

        Map<Caracteristique, Integer> valeurs = new EnumMap<>(Caracteristique.class);
        for (Caracteristique caracteristique : Caracteristique.values()) {
            int valeurDeBase = extraireValeurDeBase(valeursDeBase, caracteristique);
            int bonusEspece = espece.bonusPour(caracteristique);
            int valeurFinale = Math.min(valeurDeBase + bonusEspece, Caracteristique.VALEUR_MAX_ABSOLUE);
            valeurs.put(caracteristique, valeurFinale);
        }
        return new JeuDeCaracteristiques(valeurs);
    }

    private static int extraireValeurDeBase(Map<Caracteristique, Integer> valeursDeBase,
                                             Caracteristique caracteristique) {
        Integer valeur = valeursDeBase.get(caracteristique);
        if (valeur == null) {
            throw new IllegalArgumentException("Il manque une valeur pour " + caracteristique + ".");
        }
        if (valeur < Caracteristique.VALEUR_MIN_CREATION || valeur > Caracteristique.VALEUR_MAX_CREATION) {
            throw new IllegalArgumentException(
                    caracteristique + " doit être compris entre " + Caracteristique.VALEUR_MIN_CREATION
                            + " et " + Caracteristique.VALEUR_MAX_CREATION + ".");
        }
        return valeur;
    }
}
