package player.domain;

import player.domain.exception.InvalidPlayerDataException;

import java.util.Locale;
import java.util.regex.Pattern;


public final class Pseudonym {

    private static final Pattern FORMAT = Pattern.compile("[\\p{L}0-9_]{3,20}");

    private final String value;

    public Pseudonym(String value) {
        if (value == null) {
            throw new InvalidPlayerDataException("Le pseudonyme est obligatoire.");
        }
        if (!FORMAT.matcher(value).matches()) {
            throw new InvalidPlayerDataException(
                    "Le pseudonyme doit contenir de 3 à 20 caractères : lettres, chiffres ou tiret bas.");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String key() {
        return value.toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Pseudonym pseudonym && key().equals(pseudonym.key());
    }

    @Override
    public int hashCode() {
        return key().hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
