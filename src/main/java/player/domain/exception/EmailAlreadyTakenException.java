package player.domain.exception;

public class EmailAlreadyTakenException extends RuntimeException {

    public EmailAlreadyTakenException(String email) {
        super("Le courriel '" + email + "' est déjà utilisé.");
    }
}
