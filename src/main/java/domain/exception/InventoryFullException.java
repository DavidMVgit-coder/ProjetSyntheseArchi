package domain.exception;

public class InventoryFullException extends RuntimeException {

    public InventoryFullException(int MAX_CAPACITY) {
        super("L'inventaire est remplie.");
    }
}