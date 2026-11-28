package hero.domain;

public final class ManaPoints {

    private final int maximum;
    private final int current;

    public ManaPoints(int maximum) {
        if (maximum < 0) {
            throw new IllegalArgumentException("Le maximum de magie ne peut pas être négatif.");
        }

        this.maximum = maximum;
        this.current = maximum;
    }

    private ManaPoints(int maximum, int current) {
        this.maximum = maximum;
        this.current = current;
    }

    public boolean canCast(int cost) {
        return cost <= current;
    }

    public ManaPoints consume(int cost) {
        if (!canCast(cost)) {
            throw new IllegalStateException("Magie insuffisante pour lancer ce sort.");
        }

        return new ManaPoints(maximum, current - cost);
    }

    public ManaPoints regenerate(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("La magie regagnée ne peut pas être négative.");
        }

        return new ManaPoints(maximum, Math.min(maximum, current + amount));
    }

    public ManaPoints restoreFully() {
        return new ManaPoints(maximum, maximum);
    }

    public int getCurrent() {
        return current;
    }

    public int getMaximum() {
        return maximum;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ManaPoints that && maximum == that.maximum && current == that.current;
    }

    @Override
    public int hashCode() {
        return 31 * maximum + current;
    }

    @Override
    public String toString() {
        return current + "/" + maximum + " MP";
    }
}
