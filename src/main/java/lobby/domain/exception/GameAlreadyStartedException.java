package lobby.domain.exception;

public class GameAlreadyStartedException extends RuntimeException {

    public GameAlreadyStartedException() {
        super("La partie de ce lobby est déjà lancée.");
    }
}
