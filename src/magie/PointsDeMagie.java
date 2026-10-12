package magie;

public final class PointsDeMagie {

    private final int maximum;
    private int actuels;

    public PointsDeMagie(int maximum) {
        if (maximum < 0) {
            throw new IllegalArgumentException("Le maximum de magie ne peut pas être négatif.");
        }
        this.maximum = maximum;
        this.actuels = maximum;
    }

    public boolean peutLancer(int cout) {
        return cout <= actuels;
    }

    public void consommer(int cout) {
        if (!peutLancer(cout)) {
            throw new IllegalStateException("Magie insuffisante pour lancer ce sort.");
        }
        actuels -= cout;
    }

    public void regagner(int montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("La magie regagnée ne peut pas être négative.");
        }
        actuels = Math.min(maximum, actuels + montant);
    }

    public void restaurerCompletement() {
        actuels = maximum;
    }

    public int getActuels() {
        return actuels;
    }

    public int getMaximum() {
        return maximum;
    }

    @Override
    public String toString() {
        return actuels + "/" + maximum + " PM";
    }
}
