package player.domain.exception;

public class PseudonymAlreadyTakenException extends RuntimeException {

    public PseudonymAlreadyTakenException(String pseudonym) {
        super("Le pseudonyme '" + pseudonym + "' est déjà utilisé.");
    }
}
