package item.domain.equipment;

import java.util.Objects;
import java.util.Optional;

import hero.domain.HeroClass;
import item.domain.armor.Armor;
import item.domain.inventory.Inventory;
import item.domain.inventory.InventoryItem;
import item.domain.weapon.Weapon;
import item.domain.exception.InvalidEquipmentForClassException;
import item.domain.exception.ItemNotOwnedException;

public class HeroEquipment {
    private final HeroClass heroClass;
    private final Inventory inventory;
    private Weapon equippedWeapon;
    private Armor equippedArmor;

    public HeroEquipment(HeroClass heroClass, Inventory inventory) {
        this.heroClass = Objects.requireNonNull(heroClass, "La classe du hero est requise.");
        this.inventory = Objects.requireNonNull(inventory, "L'inventaire est requis");
    }


    public void equipWeapon(Weapon weapon) {
        validatePossession(weapon);
        validateClassRestriction(weapon);
        this.equippedWeapon = weapon;
    }

    public void equipArmor(Armor armor) {
        validatePossession(armor);
        validateClassRestriction(armor);
        this.equippedArmor = armor;
    }

    private void validatePossession(InventoryItem equipment) {
        Objects.requireNonNull(equipment, "L'équipement est requis.");
        if (!inventory.contains(equipment)) {
            throw new ItemNotOwnedException(equipment);
        }
    }

    private void validateClassRestriction(Equipment equipment) {
        if (!equipment.canBeEquippedBy(this.heroClass)) {
            throw new InvalidEquipmentForClassException(equipment, this.heroClass);
        }
    }

    public Optional<Weapon> getEquippedWeapon() {
        return Optional.ofNullable(equippedWeapon);
    }

    public Optional<Armor> getEquippedArmor() {
        return Optional.ofNullable(equippedArmor);
    }
}
