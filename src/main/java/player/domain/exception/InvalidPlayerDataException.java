package player.domain.exception;

/** Champ absent ou mal formé (400 INVALID_REQUEST). */
public class InvalidPlayerDataException extends RuntimeException {

    public InvalidPlayerDataException(String message) {
        super(message);
    }
}
