import java.util.Objects;

public class Armor implements Equipment {
    // TODO: surement changer le nom des variables
    private final Name name;
    private final ArmorType armorType;
    private final int protection;

    public Armor(Name name, ArmorType armorType, int protection) {
        this.name = Objects.requireNonNull(name, "The armor's name is required.");
        this.armorType = Objects.requireNonNull(armorType, "The armor type is required.");
        if (protection < 0) {
            throw new InvalidProtectionException(protection);
        }
        this.protection = protection;
    }

    public int getProtection() {
        return protection;
    }

    public ArmorType getArmorType() {
        return armorType;
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    // TODO: set the correct class name
    public boolean canBeEquippedBy(HeroClass heroClass) {
        return ArmorCompatibilityRules.isAllowed(heroClass, armorType);
    }
}