public class InvalidProtectionException extends RuntimeException {
    public InvalidProtectionException(int protection) {
        super("La protection d'une armure ne peut pas etre negative (valeur recu: " + protection + ").");
    }
}