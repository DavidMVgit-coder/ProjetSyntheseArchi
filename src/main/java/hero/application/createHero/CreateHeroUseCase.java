package hero.application.createHero;

import hero.application.port.HeroRepository;
import hero.domain.Hero;
import hero.domain.exception.HeroAlreadyExistException;
import hero.domain.HeroFactory;

public class CreateHeroUseCase {

    private final HeroRepository repository;

    public CreateHeroUseCase(HeroRepository repository) {
        this.repository = repository;
    }

    public HeroCreationOutput execute(CreateHeroCommand command) {

        if (repository.existsByName(command.name())) {
            throw new HeroAlreadyExistException(command.name());
        }

        Hero hero = HeroFactory.createHeroLevel1(
                command.name(),
                command.species(),
                command.heroClass(),
                command.baseValues()
        );

        repository.save(hero);

        return new HeroCreationOutput(
                hero.getHeroId(),
                hero.getName(),
                hero.getSpecies(),
                hero.getHeroClass(),
                hero.getCharacteristics(),
                hero.getHealthPoints(),
                hero.getManaPoints()
        );
    }
}
