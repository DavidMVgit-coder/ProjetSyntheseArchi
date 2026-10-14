package domain.exception;

import domain.caracteristiques.Characteristic;

public class MissingValueCharacteristic extends RuntimeException {

    private final Characteristic characteristic;

    public MissingValueCharacteristic(Characteristic characteristic) {
        super("Il manque une valeur pour " + characteristic + ".");
        this.characteristic = characteristic;
    }

    public Characteristic getCharacteristic() {
        return characteristic;
    }
}
