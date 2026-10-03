
import Hero.api.application.CreateHero.*;
import Hero.api.domain.exception.*;
import Hero.api.infra.InMemoryHeroRepository;

void main() {
    InMemoryHeroRepository repository =  new InMemoryHeroRepository();
    CreateHeroUseCase useCase = new CreateHeroUseCase(repository);
    CreateHeroCommande command = new CreateHeroCommande("Davifghjk",2,8,1,96,9,4);

    try {
        useCase.executer(new CreateHeroCommande("nasghh",98,98,65,9,6,8));
        useCase.executer(new CreateHeroCommande("Yannick",98,98,65,9,6,8));
        HeroCreationOutput output =  useCase.executer(command);
        System.out.println("L'hero : " + output.getNom() + "  son Id: " + output.getHeroId() );


    }catch (HeroExistException e){
        System.out.println("hela; " + e.getMessage());
    }catch (InvalidHeroNameException e ){
        System.out.println(e.getMessage());
    }


}
