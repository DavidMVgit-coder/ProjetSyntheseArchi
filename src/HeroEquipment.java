import java.util.Objects;
import java.util.Optional;
// TODO: Mettre le bon nom pour HeroClass.
public class HeroEquipment {
    private final HeroClass heroClass;
    private final Inventory inventory;
    private Weapon equippedWeapon;
    private Armor equippedArmor;

    public HeroEquipment(HeroClass heroClass, Inventory inventory) {
        this.heroClass = Objects.requireNonNull(heroClass, "The hero's class is required.");
        this.inventory = Objects.requireNonNull(inventory, "The inventory is required.");
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

    private void validatePossession(Equipment equipment) {
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