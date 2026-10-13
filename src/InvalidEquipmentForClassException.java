public class InvalidEquipmentForClassException extends RuntimeException {
    public InvalidEquipmentForClassException(HeroEquipment equipment, HeroClass heroClass) {
        super(String.format("L'équipement '%s' ne convient pas à la classe %s.", equipment.getName().value(), heroClass.name()));
    }
}