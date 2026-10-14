package domain.exception;

public class InvalidHeroNameException extends DomainException {

    public InvalidHeroNameException() {
        this("Le nom du héros est invalide.");
    }

    public InvalidHeroNameException(String message) {
        super(message);
    }
}
