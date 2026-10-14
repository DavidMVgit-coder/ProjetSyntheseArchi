package domain.equipment;

import domain.classe.HeroClass;

public interface Equipement extends InventoryItem {
    boolean canBeEquippedBy(HeroClass heroClass);
}
