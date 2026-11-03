package hero.domain.exception;

import hero.domain.Characteristic;

public class MissingValueCharacteristicException extends RuntimeException {

    private final Characteristic characteristic;

    public MissingValueCharacteristicException(Characteristic characteristic) {
        super("Il manque une valeur pour " + characteristic + ".");
        this.characteristic = characteristic;
    }

    public Characteristic getCharacteristic() {
        return characteristic;
    }
}
