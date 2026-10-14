package application.CreateHero;

import application.port.HeroRepository;
import domain.entites.Hero;
import domain.exception.HeroExistException;
import domain.factory.HeroFactory;

public class CreateHeroUseCase {

    private final HeroRepository repository;

    public CreateHeroUseCase(HeroRepository repository) {
        this.repository = repository;
    }

    public HeroCreationOutput execute(CreateHeroCommand command) {

        if (repository.existsByName(command.name())) {
            throw new HeroExistException(command.name());
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
