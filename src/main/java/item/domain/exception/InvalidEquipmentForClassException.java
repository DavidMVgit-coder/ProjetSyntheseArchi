package item.domain.exception;

import hero.domain.HeroClass;
import item.domain.equipment.Equipment;

public class InvalidEquipmentForClassException extends RuntimeException {
    public InvalidEquipmentForClassException(Equipment equipment, HeroClass heroClass) {
        super(String.format("L'équipement '%s' ne convient pas à la classe %s.", equipment.getDisplayName(), heroClass.name()));
    }
}
