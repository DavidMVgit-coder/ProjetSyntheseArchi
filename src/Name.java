import java.util.Objects;

public final class Name {
    private final String label;

    public Name(String label) {
        if (label == null || label.isBlank()) {
            throw new InvalidNameException();
        }
        this.label = label;
    }

    public String value() {
        return label;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Name)) return false;
        return label.equals(((Name) other).label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }

    @Override
    public String toString() {
        return label;
    }
}