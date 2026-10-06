package Heros;

import java.util.Map;
import java.util.Objects;

import caracteristiques.Caracteristique;
import caracteristiques.CaracteristiquesFactory;
import caracteristiques.Espece;
import caracteristiques.JeuDeCaracteristiques;
import classe.Classe;
import magie.PointsDeMagie;
import nom.NomHeros;
import experience.Progression;
import vie.PointsDeVie;

public final class HerosFactory {

    private HerosFactory() {
    }

    public static Heros creerNiveau1(String nom, Espece espece, Classe classe,
                                      Map<Caracteristique, Integer> valeursDeBase) {
        Objects.requireNonNull(espece);
        Objects.requireNonNull(classe);

        NomHeros nomValide = new NomHeros(nom);
        JeuDeCaracteristiques caracteristiques = CaracteristiquesFactory.creer(valeursDeBase, espece);
        PointsDeVie pointsDeVie = new PointsDeVie(calculerPvMaxNiveau1(classe, caracteristiques));
        PointsDeMagie pointsDeMagie = new PointsDeMagie(classe.getMagieMaxNiveau1());
        Progression progression = new Progression();

        return new Heros(nomValide, espece, classe, caracteristiques, pointsDeVie, pointsDeMagie, progression);
    }

    private static int calculerPvMaxNiveau1(Classe classe, JeuDeCaracteristiques caracteristiques) {
        int modificateurConstitution = caracteristiques.getModificateur(Caracteristique.CONSTITUTION);
        return Math.max(1, classe.getPvBase() + modificateurConstitution);
    }
}
