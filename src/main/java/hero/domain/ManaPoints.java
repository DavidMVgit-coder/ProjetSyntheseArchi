package hero.domain;

public final class ManaPoints {

    private final int maximum;
    private int current;

    public ManaPoints(int maximum) {
        if (maximum < 0) {
            throw new IllegalArgumentException("Le maximum de magie ne peut pas être négatif.");
        }

        this.maximum = maximum;
        this.current = maximum;
    }

    public boolean canCast(int cost) {
        return cost <= current;
    }

    public void consume(int cost) {
        if (!canCast(cost)) {
            throw new IllegalStateException("Magie insuffisante pour lancer ce sort.");
        }

        current -= cost;
    }

    public void regenerate(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("La magie regagnée ne peut pas être négative.");
        }

        current = Math.min(maximum, current + amount);
    }

    public void restoreFully() {
        current = maximum;
    }

    public int getCurrent() {
        return current;
    }

    public int getMaximum() {
        return maximum;
    }

    @Override
    public String toString() {
        return current + "/" + maximum + " MP";
    }
}
