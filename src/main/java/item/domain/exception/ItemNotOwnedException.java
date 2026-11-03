package item.domain.exception;

import item.domain.inventory.InventoryItem;

public class ItemNotOwnedException extends RuntimeException {
    public ItemNotOwnedException(InventoryItem item) {
        super("Le héros ne possède pas l’objet '" + item.getDisplayName() + "' dans son inventaire.");
    }
}
