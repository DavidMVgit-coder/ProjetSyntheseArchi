package api.hero;

import application.CreateHero.HeroOutput;

public final class HeroResponseMapper {

    public HeroResponse toResponse(HeroOutput output) {
        return new HeroResponse(
                output.id(),
                output.name(),
                output.species(),
                output.heroClass(),
                output.level(),
                output.currentHitPoints(),
                output.maxHitPoints(),
                output.currentMana(),
                output.maxMana());
    }
}
