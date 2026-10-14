package domain.exception;

public class InvalidNameException extends RuntimeException {
    public InvalidNameException() {
        super("Le domain.nom ne peut pas etre vide.");
    }
}