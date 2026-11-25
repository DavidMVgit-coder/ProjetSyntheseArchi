package hero.domain.exception;

public class InvalidHeroNameException extends DomainException {

    public InvalidHeroNameException(String message) {
        super(message);
    }
}
