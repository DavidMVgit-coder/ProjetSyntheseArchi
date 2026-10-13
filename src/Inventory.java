import java.util.ArrayList;
import Weapon.Item;

public class Inventory {
    private final int MAX_CAPACITE = 10;
    private ArrayList<Item>  items  = new ArrayList<>();

    public void addItem(Item items){
        if(items.size()){
            throw new InventoryFullException(MAX_CAPACITE);
        }
        items.add(items);
    }

    public boolean removeItem(Item item){
        return item.remove(item);
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    
}
