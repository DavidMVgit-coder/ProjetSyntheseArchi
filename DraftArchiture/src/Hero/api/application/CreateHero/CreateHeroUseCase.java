package Hero.api.application.CreateHero;
import Hero.api.application.port.HeroRepository;
import Hero.api.domain.entites.Hero;
import Hero.api.domain.exception.HeroExistException;
import Hero.api.domain.valeur_object.Caracteristique;

public class CreateHeroUseCase{
    private final HeroRepository repository;
    public  CreateHeroUseCase(HeroRepository repository){
        this.repository = repository;

    }

    public HeroCreationOutput executer(CreateHeroCommande command) {
        if (repository.verifierNom(command.nom())){
            throw new HeroExistException(command.nom());
        }
        Hero hero =  new Hero(
                command.nom(),
                new Caracteristique("Force",command.force()),
                new Caracteristique("Dexteite",command.dexterite()),
                new Caracteristique("Constitution", command.constitution()),
                new Caracteristique("Intelligence", command.intelligence()),
                new Caracteristique("Sagesse", command.sagesse()),
                new Caracteristique("Charisme", command.charisme())
        );
        repository.sauvegader(hero);

        return new HeroCreationOutput(hero.getHeroId(), hero.getNom());
    }
}


