package domain.factory;

import java.util.Map;
import java.util.Objects;
import domain.entites.Hero;
import domain.caracteristiques.*;
import domain.classe.HeroClass;
import domain.magie.ManaPoints;
import domain.experience.Progression;
import domain.nom.HeroName;
import domain.vie.HealthPoints;


public final class HeroFactory {

    private HeroFactory() {
    }

    public static Hero createHeroLevel1(HeroName name, Species species, HeroClass heroClass,
                                        Map<Characteristic, Integer> baseValues)
    {
        Objects.requireNonNull(species);
        Objects.requireNonNull(heroClass);

        CharacteristicSet characteristics = CharacteristicsFactory.create(baseValues, species);
        HealthPoints healthPoints = new HealthPoints(calculateMaxHpLevel1(heroClass, characteristics));
        ManaPoints manaPoints = new ManaPoints(heroClass.getMaxManaLevel1());
        Progression progression = new Progression();

        return new Hero(name, species, heroClass, characteristics, healthPoints, manaPoints, progression);
    }

    private static int calculateMaxHpLevel1(HeroClass heroClass, CharacteristicSet characteristics) {
        int constitutionModifier = characteristics.getModifier(Characteristic.CONSTITUTION);

        return Math.max(1, heroClass.getBaseHp() + constitutionModifier);
    }
}
