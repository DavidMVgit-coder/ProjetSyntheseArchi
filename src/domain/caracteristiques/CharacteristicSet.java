package domain.caracteristiques;

import java.util.Map;

public final class CharacteristicSet {

    private final Map<Characteristic, Integer> values;

    CharacteristicSet(Map<Characteristic, Integer> values) {
        this.values = values;
    }

    public int getValue(Characteristic characteristic) {
        return values.get(characteristic);
    }

    public int getModifier(Characteristic characteristic) {
        return Characteristic.calculateModifier(getValue(characteristic));
    }
}
