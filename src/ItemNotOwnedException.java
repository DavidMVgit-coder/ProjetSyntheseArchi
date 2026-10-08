public class ItemNotOwnedException extends RuntimeException {
    public ItemNotOwnedException(InventoryItem item) {
        super("Le héros ne possède pas l’objet'" + item.getName().value() + "'dans son inventaire.");
    }
}