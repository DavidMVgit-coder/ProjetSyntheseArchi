package player.application.domain.exception;

public class InvalidCredentialsException extends RuntimeException {

    public static final String MESSAGE = "Courriel ou mot de passe invalide.";

    public InvalidCredentialsException() {
        super(MESSAGE);
    }
}