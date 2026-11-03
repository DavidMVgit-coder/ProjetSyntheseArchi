package hero.application.createHero;

import hero.domain.CharacteristicSet;
import hero.domain.Species;
import hero.domain.HeroClass;
import hero.domain.ManaPoints;
import hero.domain.HeroName;
import hero.domain.HealthPoints;

import java.util.UUID;

public class HeroCreationOutput {

    private final UUID heroId;
    private final HeroName name;
    private final Species species;
    private final HeroClass heroClass;
    private final CharacteristicSet characteristics;
    private final HealthPoints maxHealthPoints;
    private final ManaPoints maxManaPoints;

    public HeroCreationOutput(UUID heroId, HeroName name, Species species, HeroClass heroClass,CharacteristicSet characteristics,HealthPoints maxHealthPoints, ManaPoints maxManaPoints) {
        this.heroId = heroId;
        this.name = name;
        this.species = species;
        this.heroClass = heroClass;
        this.characteristics = characteristics;
        this.maxHealthPoints = maxHealthPoints;
        this.maxManaPoints = maxManaPoints;
    }

    public UUID getHeroId() {
        return heroId;
    }

    public HeroName getName() {
        return name;
    }

    public Species getSpecies() {
        return species;
    }

    public HeroClass getHeroClass() {
        return heroClass;
    }

    public CharacteristicSet getCharacteristics() {
        return characteristics;
    }

    public HealthPoints getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public ManaPoints getMaxManaPoints() {
        return maxManaPoints;
    }
}
