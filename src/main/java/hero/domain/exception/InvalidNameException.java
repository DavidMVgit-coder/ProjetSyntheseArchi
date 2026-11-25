package hero.domain.exception;

public class InvalidNameException extends DomainException {

    public InvalidNameException() {
        super("Le nom ne peut pas être vide.");
    }
}
