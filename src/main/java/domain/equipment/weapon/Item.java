package domain.equipment.weapon;

import domain.equipment.InventoryItem;

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
        return "Item [name=" + name + "]";
    }

    

}
