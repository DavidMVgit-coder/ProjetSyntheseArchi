package player.domain.exception;

/** Conflit : le pseudonyme existe déjà (409 PSEUDONYM_TAKEN). */
public class PseudonymAlreadyTakenException extends RuntimeException {

    public PseudonymAlreadyTakenException(String pseudonym) {
        super("Le pseudonyme '" + pseudonym + "' est déjà utilisé.");
    }
}
