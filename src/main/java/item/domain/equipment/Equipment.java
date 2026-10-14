package item.domain.equipment;

import hero.domain.HeroClass;
import item.domain.inventory.InventoryItem;

public interface Equipment extends InventoryItem {
    boolean canBeEquippedBy(HeroClass heroClass);
}
