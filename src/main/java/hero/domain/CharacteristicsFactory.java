package hero.domain;

import hero.domain.exception.MissingValueCharacteristicException;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class CharacteristicsFactory {

    private CharacteristicsFactory() {
    }

    public static CharacteristicSet create(Map<Characteristic, Integer> baseValues, Species species) {
        Objects.requireNonNull(baseValues);
        Objects.requireNonNull(species);

        Map<Characteristic, Integer> values = new EnumMap<>(Characteristic.class);
        for (Characteristic characteristic : Characteristic.values()) {
            int baseValue = extractBaseValue(baseValues, characteristic);
            int speciesBonus = species.bonusFor(characteristic);
            int finalValue = Math.min(baseValue + speciesBonus, Characteristic.ABSOLUTE_MAX_VALUE);
            values.put(characteristic, finalValue);
        }

        return new CharacteristicSet(values);
    }

    private static int extractBaseValue(Map<Characteristic, Integer> baseValues,
                                        Characteristic characteristic) {
        Integer value = baseValues.get(characteristic);
        if (value == null) {
            throw new MissingValueCharacteristicException( characteristic);
        }

        if (value < Characteristic.MIN_CREATION_VALUE || value > Characteristic.MAX_CREATION_VALUE) {
            throw new IllegalArgumentException(
                    characteristic + " doit être compris entre " + Characteristic.MIN_CREATION_VALUE
                            + " et " + Characteristic.MAX_CREATION_VALUE + ".");
        }

        return value;
    }
}
