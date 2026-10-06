package nom;

import java.util.Objects;

public final class NomHeros {

    private static final int LONGUEUR_MIN = 2;
    private static final int LONGUEUR_MAX = 30;

    private final String valeur;

    public NomHeros(String valeur) {
        if (valeur == null) {
            throw new IllegalArgumentException("Le nom du héros est obligatoire.");
        }
        if (valeur.length() < LONGUEUR_MIN || valeur.length() > LONGUEUR_MAX) {
            throw new IllegalArgumentException(
                    "Le nom doit contenir entre " + LONGUEUR_MIN + " et " + LONGUEUR_MAX + " caractères.");
        }
        if (!estFormatValide(valeur)) {
            throw new IllegalArgumentException(
                    "Le nom ne peut contenir que des lettres et des espaces, sans espace au début ou à la fin.");
        }
        this.valeur = valeur;
    }

    private static boolean estFormatValide(String valeur) {
        if (valeur.charAt(0) == ' ' || valeur.charAt(valeur.length() - 1) == ' ') {
            return false;
        }
        boolean espacePrecedent = false;
        for (int i = 0; i < valeur.length(); i++) {
            char caractere = valeur.charAt(i);
            if (caractere == ' ') {
                if (espacePrecedent) {
                    return false;
                }
                espacePrecedent = true;
            } else if (!Character.isLetter(caractere)) {
                return false;
            } else {
                espacePrecedent = false;
            }
        }
        return true;
    }

    public String getValeur() {
        return valeur;
    }

    @Override
    public boolean equals(Object autre) {
        if (this == autre) {
            return true;
        }
        if (!(autre instanceof NomHeros)) {
            return false;
        }
        return valeur.equals(((NomHeros) autre).valeur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valeur);
    }

    @Override
    public String toString() {
        return valeur;
    }
}
