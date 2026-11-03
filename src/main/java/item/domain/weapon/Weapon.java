package item.domain.weapon;

import java.util.Objects;

import hero.domain.HeroClass;
import item.domain.Item;
import item.domain.equipment.Equipment;

public class Weapon extends Item implements Equipment {

    private WeaponType type;
    private int damage;

    public Weapon(WeaponType type) {
        super(Objects.requireNonNull(type).getDisplayName());
        this.type = type;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public boolean canBeEquippedBy(HeroClass heroClass) {
        Objects.requireNonNull(heroClass, "La classe du héros est requise.");
        return true;
    }

    

    

}
