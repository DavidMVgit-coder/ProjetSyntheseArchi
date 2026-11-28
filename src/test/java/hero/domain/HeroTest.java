package hero.domain;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HeroTest {

    @Test
    void takesDamageWithoutGoingBelowZero() {
        Hero hero = warrior();

        hero.takeDamage(5);
        assertEquals(7, hero.getHealthPoints().getCurrent());

        hero.takeDamage(100);
        assertEquals(0, hero.getHealthPoints().getCurrent());
        assertTrue(hero.isUnconscious());
    }

    @Test
    void healsWithoutExceedingMaximum() {
        Hero hero = warrior();
        hero.takeDamage(5);

        hero.heal(100);

        assertEquals(12, hero.getHealthPoints().getCurrent());
        assertFalse(hero.isUnconscious());
    }

    @Test
    void consumesAndRegeneratesMana() {
        Hero hero = mage();

        hero.consumeMana(7);
        assertEquals(3, hero.getManaPoints().getCurrent());
        assertFalse(hero.canCastSpell(4));

        hero.regenerateMana(100);
        assertEquals(10, hero.getManaPoints().getCurrent());
    }

    @Test
    void refusesASpellWithoutEnoughMana() {
        Hero hero = mage();

        assertThrows(IllegalStateException.class, () -> hero.consumeMana(11));
        assertEquals(10, hero.getManaPoints().getCurrent());
    }

    @Test
    void restRestoresHealthAndMana() {
        Hero hero = mage();
        hero.takeDamage(3);
        hero.consumeMana(10);

        hero.rest();

        assertEquals(hero.getHealthPoints().getMaximum(), hero.getHealthPoints().getCurrent());
        assertEquals(10, hero.getManaPoints().getCurrent());
    }

    @Test
    void gainsMaximumHealthForEachLevelGained() {
        Hero hero = warrior();

        hero.gainExperience(900);

        assertEquals(3, hero.getProgression().getLevel());
        assertEquals(12 + 2 * 6, hero.getHealthPoints().getMaximum());
        assertEquals(12 + 2 * 6, hero.getHealthPoints().getCurrent());
    }

    @Test
    void cannotBeModifiedThroughAnExposedValue() {
        Hero hero = warrior();
        HealthPoints exposed = hero.getHealthPoints();

        exposed.takeDamage(5);
        hero.getProgression().gainExperience(300);

        assertEquals(12, hero.getHealthPoints().getCurrent());
        assertEquals(1, hero.getProgression().getLevel());
    }

    @Test
    void anExposedValueIsASnapshotThatDoesNotChangeAfterwards() {
        Hero hero = warrior();
        HealthPoints before = hero.getHealthPoints();

        hero.takeDamage(5);

        assertEquals(12, before.getCurrent());
        assertEquals(7, hero.getHealthPoints().getCurrent());
    }

    private static Hero warrior() {
        return HeroFactory.createHeroLevel1(new HeroName("Bram"), Species.HUMAN, HeroClass.WARRIOR, values(10));
    }

    private static Hero mage() {
        return HeroFactory.createHeroLevel1(new HeroName("Lyra"), Species.HUMAN, HeroClass.MAGE, values(10));
    }

    private static Map<Characteristic, Integer> values(int value) {
        Map<Characteristic, Integer> values = new EnumMap<>(Characteristic.class);
        for (Characteristic characteristic : Characteristic.values()) {
            values.put(characteristic, value);
        }
        return values;
    }
}
