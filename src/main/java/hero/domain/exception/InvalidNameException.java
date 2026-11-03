package hero.domain.exception;

public class InvalidNameException extends RuntimeException {
    public InvalidNameException() {
        super("Le nom ne peut pas être vide.");
    }
}