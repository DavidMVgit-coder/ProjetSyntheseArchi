package domain.exception;

public class PlayerNotInSessionException extends RuntimeException {

    public PlayerNotInSessionException() {
        super("Le joueur n'appartient pas à cette session.");
    }
}