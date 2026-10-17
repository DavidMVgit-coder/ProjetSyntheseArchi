package domain.exception;

import domain.classe.HeroClass;
import domain.equipment.Equipement;

public class InvalidEquipmentForClassException extends RuntimeException {
    public InvalidEquipmentForClassException(Equipement equipment, HeroClass heroClass) {
        super(String.format("L'équipement '%s' ne convient pas à la classe %s.", equipment.getDisplayName(), heroClass.name()));
    }
}
