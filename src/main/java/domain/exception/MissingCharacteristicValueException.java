package domain.exception;

import domain.caracteristiques.Characteristic;

public class MissingCharacteristicValueException extends DomainException {

    private final Characteristic characteristic;

    public MissingCharacteristicValueException(Characteristic characteristic) {
        super("Il manque une valeur pour " + characteristic + ".");
        this.characteristic = characteristic;
    }

    public Characteristic getCharacteristic() {
        return characteristic;
    }
}
