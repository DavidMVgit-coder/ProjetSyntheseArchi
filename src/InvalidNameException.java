public class InvalidNameException extends RuntimeException {
    public InvalidNameException() {
        super("Le nom ne peut pas etre vide.");
    }
}