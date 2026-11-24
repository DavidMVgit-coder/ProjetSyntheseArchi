package player.domain.exception;

public class PasswordTooShortException extends RuntimeException {

    public PasswordTooShortException(int minLength) {
        super("Le mot de passe doit contenir au moins " + minLength + " caractères.");
    }
}
