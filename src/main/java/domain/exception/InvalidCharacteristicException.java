package domain.exception;

import domain.caracteristiques.Characteristic;

public class InvalidCharacteristicException extends DomainException {

    public InvalidCharacteristicException() {
        super("La valeur de la caractéristique est invalide.");
    }

    public InvalidCharacteristicException(Characteristic characteristic, int value) {
        super("La caractéristique " + characteristic + " doit être comprise entre "
                + Characteristic.MIN_CREATION_VALUE + " et " + Characteristic.MAX_CREATION_VALUE
                + " (valeur reçue : " + value + ").");
    }
}
