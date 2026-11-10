package domain.exception;

public class SessionFullException extends RuntimeException {

    public SessionFullException() {
        super("La session est complète. Maximum 4 joueurs.");
    }
}