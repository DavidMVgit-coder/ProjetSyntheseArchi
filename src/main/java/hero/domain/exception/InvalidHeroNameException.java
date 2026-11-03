package hero.domain.exception;

public class InvalidHeroNameException extends RuntimeException {

    public InvalidHeroNameException() {
        super("Le nom du héro est invalide.");
    }
}
