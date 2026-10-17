package domain.equipment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import domain.exception.InventoryFullException;

public class Inventory {
    private static final int MAX_CAPACITY = 10;
    private final List<InventoryItem> items = new ArrayList<>();

    public void addItem(InventoryItem item) {
        Objects.requireNonNull(item, "L'objet est requis.");
        if (items.size() >= MAX_CAPACITY) {
            throw new InventoryFullException(MAX_CAPACITY);
        }
        items.add(item);
    }

    public boolean removeItem(InventoryItem item) {
        return items.remove(item);
    }

    public boolean contains(InventoryItem item) {
        return items.contains(item);
    }

    public List<InventoryItem> getItems() {
        return List.copyOf(items);
    }
}
