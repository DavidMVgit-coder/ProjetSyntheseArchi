package player.domain.exception;

public class InvalidPlayerDataException extends RuntimeException {

    public InvalidPlayerDataException(String message) {
        super(message);
    }
}
