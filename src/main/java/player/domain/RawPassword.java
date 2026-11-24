package player.domain;

import player.domain.exception.InvalidPlayerDataException;
import player.domain.exception.PasswordTooShortException;


public final class RawPassword {

    public static final int MIN_LENGTH = 8;

    private final String value;

    public RawPassword(String value) {
        if (value == null) {
            throw new InvalidPlayerDataException("Le mot de passe est obligatoire.");
        }
        if (value.length() < MIN_LENGTH) {
            throw new PasswordTooShortException(MIN_LENGTH);
        }
        this.value = value;
    }

    /** À utiliser seulement par le hacheur. */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "RawPassword[***]";
    }
}
