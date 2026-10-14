package domain.exception;

public class InvalidCharacteristicException extends RuntimeException {

    public InvalidCharacteristicException() {
        super("Le nom de la caractéristique est invalide.");
    }
}

