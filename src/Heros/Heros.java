package Heros;

import java.util.Objects;

import caracteristiques.Caracteristique;
import caracteristiques.Espece;
import caracteristiques.JeuDeCaracteristiques;
import classe.Classe;
import magie.PointsDeMagie;
import nom.NomHeros;
import experience.Progression;
import vie.PointsDeVie;

public class Heros {

    private final NomHeros nom;
    private final Espece espece;
    private final Classe classe;
    private final JeuDeCaracteristiques caracteristiques;
    private final PointsDeVie pointsDeVie;
    private final PointsDeMagie pointsDeMagie;
    private final Progression progression;

    Heros(NomHeros nom, Espece espece, Classe classe, JeuDeCaracteristiques caracteristiques,
          PointsDeVie pointsDeVie, PointsDeMagie pointsDeMagie, Progression progression) {
        this.nom = Objects.requireNonNull(nom);
        this.espece = Objects.requireNonNull(espece);
        this.classe = Objects.requireNonNull(classe);
        this.caracteristiques = Objects.requireNonNull(caracteristiques);
        this.pointsDeVie = Objects.requireNonNull(pointsDeVie);
        this.pointsDeMagie = Objects.requireNonNull(pointsDeMagie);
        this.progression = Objects.requireNonNull(progression);
    }

    public void subirDegats(int degats) {
        pointsDeVie.subirDegats(degats);
    }

    public void soigner(int montant) {
        pointsDeVie.soigner(montant);
    }

    public boolean estHorsCombat() {
        return pointsDeVie.estHorsCombat();
    }

    public boolean peutLancerSort(int coutEnMagie) {
        return pointsDeMagie.peutLancer(coutEnMagie);
    }

    public void consommerMagie(int coutEnMagie) {
        pointsDeMagie.consommer(coutEnMagie);
    }

    public void regagnerMagie(int montant) {
        pointsDeMagie.regagner(montant);
    }

    public void repos() {
        pointsDeVie.restaurerCompletement();
        pointsDeMagie.restaurerCompletement();
    }

    public void gagnerExperience(int pointsExperience) {
        int niveauxGagnes = progression.gagnerExperience(pointsExperience);
        for (int i = 0; i < niveauxGagnes; i++) {
            pointsDeVie.augmenterMaximum(calculerGainPvParNiveau());
        }
    }

    private int calculerGainPvParNiveau() {
        int modificateurConstitution = caracteristiques.getModificateur(Caracteristique.CONSTITUTION);
        return Math.max(1, classe.getPvBase() / 2 + modificateurConstitution);
    }

    public String getNom() {
        return nom.getValeur();
    }

    public Espece getEspece() {
        return espece;
    }

    public Classe getClasse() {
        return classe;
    }

    public int getValeur(Caracteristique caracteristique) {
        return caracteristiques.getValeur(caracteristique);
    }

    public int getModificateur(Caracteristique caracteristique) {
        return caracteristiques.getModificateur(caracteristique);
    }

    public int getNiveau() {
        return progression.getNiveau();
    }

    public int getExperience() {
        return progression.getExperience();
    }

    public int getPvActuels() {
        return pointsDeVie.getActuels();
    }

    public int getPvMax() {
        return pointsDeVie.getMaximum();
    }

    public int getPmActuels() {
        return pointsDeMagie.getActuels();
    }

    public int getPmMax() {
        return pointsDeMagie.getMaximum();
    }

    @Override
    public String toString() {
        return nom + " (niveau " + getNiveau() + " " + classe + " " + espece + ") — "
                + pointsDeVie + ", " + pointsDeMagie;
    }
}
