package player.domain;

import player.domain.exception.InvalidPlayerDataException;

import java.util.Locale;
import java.util.regex.Pattern;

/** Objet-valeur : adresse courriel valide. L'égalité ignore la casse. */
public final class Email {

    private static final Pattern FORMAT = Pattern.compile("[^@\\s]+@[^@\\s.]+(\\.[^@\\s.]+)+");
    private static final int MAX_LENGTH = 254;

    private final String value;

    public Email(String value) {
        if (value == null) {
            throw new InvalidPlayerDataException("Le courriel est obligatoire.");
        }
        if (value.length() > MAX_LENGTH || !FORMAT.matcher(value).matches()) {
            throw new InvalidPlayerDataException("Le courriel n'est pas une adresse valide.");
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
        return other instanceof Email email && key().equals(email.key());
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
