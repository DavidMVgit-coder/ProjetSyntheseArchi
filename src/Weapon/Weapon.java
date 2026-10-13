package Weapon;

import java.util.Objects;

public class Weapon extends Item {

    private typeWeapon type;
    private int damage;

    public Weapon(typeWeapon type) {
        super(Objects.requireNonNull(type).getDisplayName());
        this.type = type;
    }

    public int getDamage() {
        return damage;
    }

    

    

}
