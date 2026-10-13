import domain.classe.HeroClass;

public class InvalidEquipmentForClassException extends RuntimeException {
    public InvalidEquipmentForClassException(Equipment equipment, HeroClass heroClass) {
        super(String.format("L'équipement '%s' ne convient pas à la domain.classe %s.", equipment.getName().value(), heroClass.name()));
    }
}