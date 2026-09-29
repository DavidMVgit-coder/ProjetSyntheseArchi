import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

// TODO: mettre le bon nom de classe pour HeroClass
final class ArmorCompatibilityRules {
    private static final Map<HeroClass, Set<ArmorType>> ALLOWED = new EnumMap<>(HeroClass.class);

    static {
        ALLOWED.put(HeroClass.WARRIOR, EnumSet.of(ArmorType.LIGHT, ArmorType.HEAVY));
        ALLOWED.put(HeroClass.CLERIC, EnumSet.of(ArmorType.LIGHT, ArmorType.HEAVY));
        ALLOWED.put(HeroClass.RANGER, EnumSet.of(ArmorType.LIGHT));
        ALLOWED.put(HeroClass.MAGE, EnumSet.of(ArmorType.NONE));
    }

    private ArmorCompatibilityRules() {
    }

    static boolean isAllowed(HeroClass heroClass, ArmorType type) {
        return ALLOWED.getOrDefault(heroClass, Set.of()).contains(type);
    }
}