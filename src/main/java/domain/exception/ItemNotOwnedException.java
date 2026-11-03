package domain.exception;

import domain.equipment.InventoryItem;

public class ItemNotOwnedException extends RuntimeException {
    public ItemNotOwnedException(InventoryItem item) {
        super("Le héros ne possède pas l’objet '" + item.getDisplayName() + "' dans son inventaire.");
    }
}
