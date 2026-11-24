package player.domain.exception;

/** Valeur bien formée mais refusée par une règle métier (422 PASSWORD_TOO_SHORT). */
public class PasswordTooShortException extends RuntimeException {

    public PasswordTooShortException(int minLength) {
        super("Le mot de passe doit contenir au moins " + minLength + " caractères.");
    }
}
