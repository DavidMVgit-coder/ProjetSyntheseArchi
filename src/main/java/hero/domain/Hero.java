package hero.domain;

import java.util.Objects;
import java.util.UUID;

public class Hero {

    private final UUID heroId;
    private final HeroName name;
    private final Species species;
    private final HeroClass heroClass;
    private final CharacteristicSet characteristics;
    private final HealthPoints healthPoints;
    private final ManaPoints manaPoints;
    private final Progression progression;


    public Hero(HeroName name, Species species, HeroClass heroClass, CharacteristicSet characteristics,
                HealthPoints healthPoints, ManaPoints manaPoints, Progression progression)
    {
        this.heroId = UUID.randomUUID();
        this.name = Objects.requireNonNull(name);
        this.species = Objects.requireNonNull(species);
        this.heroClass = Objects.requireNonNull(heroClass);
        this.characteristics = Objects.requireNonNull(characteristics);
        this.healthPoints = Objects.requireNonNull(healthPoints);
        this.manaPoints = Objects.requireNonNull(manaPoints);
        this.progression = Objects.requireNonNull(progression);
    }


    public void takeDamage(int damage) {
        healthPoints.takeDamage(damage);
    }

    public void heal(int amount) {
        healthPoints.heal(amount);
    }

    public boolean isUnconscious() {
        return healthPoints.isUnconscious();
    }

    public boolean canCastSpell(int manaCost) {
        return manaPoints.canCast(manaCost);
    }

    public void consumeMana(int manaCost) {
        manaPoints.consume(manaCost);
    }

    public void regenerateMana(int amount) {
        manaPoints.regenerate(amount);
    }

    public void rest() {
        healthPoints.restoreFully();
        manaPoints.restoreFully();
    }

    public void gainExperience(int experiencePoints) {
        int levelsGained = progression.gainExperience(experiencePoints);

        for (int i = 0; i < levelsGained; i++) {
            healthPoints.increaseMaximum(calculateHpGainPerLevel());
        }
    }

    private int calculateHpGainPerLevel() {
        int constitutionModifier = characteristics.getModifier(Characteristic.CONSTITUTION);

        return Math.max(1, (heroClass.getBaseHp() / 2) + constitutionModifier);
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

    public HealthPoints getHealthPoints() {
        return healthPoints;
    }

    public ManaPoints getManaPoints() {
        return manaPoints;
    }

    public Progression getProgression() {
        return progression;
    }

    @Override
    public String toString() {
        return name + " (Niveau " + progression.getLevel() + " " + heroClass + " " + species + ") — "
                + healthPoints + ", " + manaPoints;
    }
}

