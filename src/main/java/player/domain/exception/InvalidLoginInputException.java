package player.application.domain.exception;


public class InvalidLoginInputException extends RuntimeException {

    public InvalidLoginInputException() {
        super("Le courriel et le mot de passe sont obligatoires.");
    }
}
