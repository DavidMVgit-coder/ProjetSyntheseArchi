package hero.domain;

public final class HealthPoints {

    private final int maximum;
    private final int current;

    public HealthPoints(int maximum) {
        if (maximum < 1) {
            throw new IllegalArgumentException("Le maximum de PV doit être d'au moins 1.");
        }

        this.maximum = maximum;
        this.current = maximum;
    }

    private HealthPoints(int maximum, int current) {
        this.maximum = maximum;
        this.current = current;
    }

    public HealthPoints takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Les dégâts ne peuvent pas être négatifs.");
        }

        return new HealthPoints(maximum, Math.max(0, current - damage));
    }

    public HealthPoints heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Le soin ne peut pas être négatif.");
        }

        return new HealthPoints(maximum, Math.min(maximum, current + amount));
    }

    public HealthPoints increaseMaximum(int gain) {
        if (gain < 0) {
            throw new IllegalArgumentException("Le gain de PV ne peut pas être négatif.");
        }

        return new HealthPoints(maximum + gain, current + gain);
    }

    public HealthPoints restoreFully() {
        return new HealthPoints(maximum, maximum);
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
    public boolean equals(Object other) {
        return other instanceof HealthPoints that && maximum == that.maximum && current == that.current;
    }

    @Override
    public int hashCode() {
        return 31 * maximum + current;
    }

    @Override
    public String toString() {
        return current + "/" + maximum + " HP";
    }
}
