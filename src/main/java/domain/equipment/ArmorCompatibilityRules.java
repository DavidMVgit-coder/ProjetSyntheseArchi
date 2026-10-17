package domain.equipment;

import domain.classe.HeroClass;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

final class ArmorCompatibilityRules {
    private static final Map<HeroClass, Set<ArmorType>> ALLOWED = createAllowedArmorTypes();

    private ArmorCompatibilityRules() {
    }

    private static Map<HeroClass, Set<ArmorType>> createAllowedArmorTypes() {
        Map<HeroClass, Set<ArmorType>> allowed = new EnumMap<>(HeroClass.class);
        allowed.put(HeroClass.WARRIOR, EnumSet.of(ArmorType.LIGHT, ArmorType.HEAVY));
        allowed.put(HeroClass.CLERIC, EnumSet.of(ArmorType.LIGHT, ArmorType.HEAVY));
        allowed.put(HeroClass.RANGER, EnumSet.of(ArmorType.LIGHT));
        allowed.put(HeroClass.MAGE, EnumSet.of(ArmorType.NONE));
        return allowed;
    }

    static boolean isAllowed(HeroClass heroClass, ArmorType type) {
        Objects.requireNonNull(heroClass, "La classe du héros est requise.");
        Objects.requireNonNull(type, "Le type d'armure est requis.");
        return ALLOWED.getOrDefault(heroClass, Set.of()).contains(type);
    }
}
