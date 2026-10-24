package item.domain;

import item.domain.inventory.InventoryItem;

/**
 * Item
 */
public class Item implements InventoryItem {
    private final String name;

    public Item(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public String toString() {
        return "Item [nom=" + name + "]";
    }

    

}
