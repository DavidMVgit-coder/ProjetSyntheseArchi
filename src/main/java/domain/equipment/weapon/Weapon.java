package domain.equipment.weapon;

import java.util.Objects;

import domain.classe.HeroClass;
import domain.equipment.Equipement;

public class Weapon extends Item implements Equipement {

    private TypeWeapon type;
    private int damage;

    public Weapon(TypeWeapon type) {
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
