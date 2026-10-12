package Hero.api.application.CreateHero;
import Hero.api.application.port.HeroRepository;
import Hero.api.domain.entites.Hero;
import Hero.api.domain.exception.HeroExistException;
import Hero.api.domain.value_object.Characteristics;

public class CreateHeroUseCase{
    private final HeroRepository repository;
    public  CreateHeroUseCase(HeroRepository repository){
        this.repository = repository;

    }

    public HeroCreateOutput executer(CreateHeroCommand command) {
        if (repository.verifyName(command.name())){
            throw new HeroExistException(command.name());
        }
        Hero hero =  new Hero(
                command.name(),
                new Characteristics("Force",command.force()),
                new Characteristics("Dexteite",command.dexterite()),
                new Characteristics("Constitution", command.constitution()),
                new Characteristics("Intelligence", command.intelligence()),
                new Characteristics("Sagesse", command.sagesse()),
                new Characteristics("Charisme", command.charisme())
        );
        repository.save(hero);

        return new HeroCreateOutput(hero.getHeroId(), hero.getName());
    }
}


