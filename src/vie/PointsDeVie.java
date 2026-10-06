package vie;

public final class PointsDeVie {

    private int maximum;
    private int actuels;

    public PointsDeVie(int maximum) {
        if (maximum < 1) {
            throw new IllegalArgumentException("Le maximum de PV doit être d'au moins 1.");
        }
        this.maximum = maximum;
        this.actuels = maximum;
    }

    public void subirDegats(int degats) {
        if (degats < 0) {
            throw new IllegalArgumentException("Les dégâts ne peuvent pas être négatifs.");
        }
        actuels = Math.max(0, actuels - degats);
    }

    public void soigner(int montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Le soin ne peut pas être négatif.");
        }
        actuels = Math.min(maximum, actuels + montant);
    }

    public void augmenterMaximum(int gain) {
        if (gain < 0) {
            throw new IllegalArgumentException("Le gain de PV ne peut pas être négatif.");
        }
        maximum += gain;
        actuels += gain;
    }

    public void restaurerCompletement() {
        actuels = maximum;
    }

    public boolean estHorsCombat() {
        return actuels == 0;
    }

    public int getActuels() {
        return actuels;
    }

    public int getMaximum() {
        return maximum;
    }

    @Override
    public String toString() {
        return actuels + "/" + maximum + " PV";
    }
}
