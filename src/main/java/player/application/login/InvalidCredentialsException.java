package player.application.login;

public class InvalidCredentialsException extends RuntimeException {

    public static final String MESSAGE = "Courriel ou mot de passe invalide.";

    public InvalidCredentialsException() {
        super(MESSAGE);
    }
}
