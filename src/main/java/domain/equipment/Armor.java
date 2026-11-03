package domain.equipment;

import domain.classe.HeroClass;
import domain.exception.InvalidProtectionException;

import java.util.Objects;

public class Armor implements Equipement {
    private final ArmorName armorName;
    private final ArmorType armorType;
    private final int protection;

    public Armor(ArmorName armorName, ArmorType armorType, int protection) {
        this.armorName = Objects.requireNonNull(armorName, "Le nom de l'armure est requise");
        this.armorType = Objects.requireNonNull(armorType, "Le type d'armure est requis");
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

    public ArmorName getName() {
        return armorName;
    }

    @Override
    public String getDisplayName() {
        return armorName.value();
    }

    @Override
    public boolean canBeEquippedBy(HeroClass heroClass) {
        return ArmorCompatibilityRules.isAllowed(heroClass, armorType);
    }
}
