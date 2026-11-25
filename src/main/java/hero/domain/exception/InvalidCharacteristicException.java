package hero.domain.exception;

import hero.domain.Characteristic;

public class InvalidCharacteristicException extends DomainException {

    public InvalidCharacteristicException(Characteristic characteristic, int value) {
        super("La caractéristique " + characteristic + " doit être comprise entre "
                + Characteristic.MIN_CREATION_VALUE + " et " + Characteristic.MAX_CREATION_VALUE
                + " (valeur reçue : " + value + ").");
    }
}
