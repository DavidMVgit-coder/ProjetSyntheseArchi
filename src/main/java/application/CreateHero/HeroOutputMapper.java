package application.CreateHero;

import domain.entites.Hero;

public final class HeroOutputMapper {

    public HeroOutput toOutput(Hero hero) {
        return new HeroOutput(
                hero.getHeroId().toString(),
                hero.getName().getValue(),
                hero.getSpecies().name(),
                hero.getHeroClass().name(),
                hero.getProgression().getLevel(),
                hero.getHealthPoints().getCurrent(),
                hero.getHealthPoints().getMaximum(),
                hero.getManaPoints().getCurrent(),
                hero.getManaPoints().getMaximum());
    }
}
