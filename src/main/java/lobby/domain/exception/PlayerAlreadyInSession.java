package domain.exception;

public class PlayerAlreadyInSessionException extends RuntimeException {

    public PlayerAlreadyInSessionException() {
        super("Le joueur est déjà dans cette session.");
    }
}