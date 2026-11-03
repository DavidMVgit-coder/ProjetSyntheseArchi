package hero.domain;

public final class HealthPoints {

    private int maximum;
    private int current;

    public HealthPoints(int maximum) {
        if (maximum < 1) {
            throw new IllegalArgumentException("Le maximum de PV doit être d'au moins 1.");
        }

        this.maximum = maximum;
        this.current = maximum;
    }

    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Les dégâts ne peuvent pas être négatifs.");
        }

        current = Math.max(0, current - damage);
    }

    public void heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Le soin ne peut pas être négatif.");
        }

        current = Math.min(maximum, current + amount);
    }

    public void increaseMaximum(int gain) {
        if (gain < 0) {
            throw new IllegalArgumentException("Le gain de PV ne peut pas être négatif.");
        }

        maximum += gain;
        current += gain;
    }

    public void restoreFully() {
        current = maximum;
    }

    public boolean isUnconscious() {
        return current == 0;
    }

    public int getCurrent() {
        return current;
    }

    public int getMaximum() {
        return maximum;
    }

    @Override
    public String toString() {
        return current + "/" + maximum + " HP";
    }
}
