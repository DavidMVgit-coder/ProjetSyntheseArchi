import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

// TODO: mettre le bon nom de classe pour HeroClass
final class ArmorCompatibilityRules {
    private  final Map<HeroClass, Set<ArmorType>> allowed = new EnumMap<>(HeroClass.class);

    ArmorCompatibilityRules() {
        allowed.put(HeroClass.WARRIOR, EnumSet.of(ArmorType.LIGHT, ArmorType.HEAVY));
        allowed.put(HeroClass.CLERIC, EnumSet.of(ArmorType.LIGHT, ArmorType.HEAVY));
        allowed.put(HeroClass.RANGER, EnumSet.of(ArmorType.LIGHT));
        allowed.put(HeroClass.MAGE, EnumSet.of(ArmorType.NONE));
    }


     boolean isAllowed(HeroClass heroClass, ArmorType type) {
        return allowed.getOrDefault(heroClass, Set.of()).contains(type);
    }
}