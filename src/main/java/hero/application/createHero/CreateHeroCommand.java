package hero.application.createHero;

import hero.domain.Characteristic;
import hero.domain.Species;
import hero.domain.HeroClass;
import hero.domain.HeroName;

import java.util.Map;

public record CreateHeroCommand(
        HeroName name,
        Species species,
        HeroClass heroClass,
        Map<Characteristic, Integer> baseValues) {
}
