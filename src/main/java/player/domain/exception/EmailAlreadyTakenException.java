package player.domain.exception;

/** Conflit : le courriel existe déjà (409 EMAIL_TAKEN). */
public class EmailAlreadyTakenException extends RuntimeException {

    public EmailAlreadyTakenException(String email) {
        super("Le courriel '" + email + "' est déjà utilisé.");
    }
}
