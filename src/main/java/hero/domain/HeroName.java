package hero.domain;

import java.util.Objects;

public final class HeroName {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 30;

    private final String value;

    public HeroName(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Le nom du héro est obligatoire.");
        }

        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Le nom doit contenir entre " + MIN_LENGTH + " et " + MAX_LENGTH + " caractères.");
        }

        if (!isValidFormat(value)) {
            throw new IllegalArgumentException(
                    "Le nom ne peut contenir que des lettres et des espaces, sans espace au début ou à la fin.");
        }

        this.value = value;
    }

    private static boolean isValidFormat(String value) {
        if (value.charAt(0) == ' ' || value.charAt(value.length() - 1) == ' ') {
            return false;
        }

        boolean previousSpace = false;

        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            if (character == ' ') {
                if (previousSpace) {
                    return false;
                }
                previousSpace = true;
            } else if (!Character.isLetter(character)) {
                return false;
            } else {
                previousSpace = false;
            }
        }

        return true;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof HeroName)) {
            return false;
        }

        return value.equals(((HeroName) other).value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
