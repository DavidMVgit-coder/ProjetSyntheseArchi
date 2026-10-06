
import Hero.api.application.CreateHero.*;
import Hero.api.domain.exception.*;
import Hero.api.infra.InMemoryHeroRepository;

void main() {
    InMemoryHeroRepository repository =  new InMemoryHeroRepository();
    CreateHeroUseCase useCase = new CreateHeroUseCase(repository);
    CreateHeroCommand command = new CreateHeroCommand("Davifghjk",2,8,1,96,9,4);

    try {
        useCase.executer(new CreateHeroCommand("nasghh",98,98,65,9,6,8));
        useCase.executer(new CreateHeroCommand("Yannick",98,98,65,9,6,8));
        HeroCreateOutput output =  useCase.executer(command);
        System.out.println("L'hero : " + output.getName() + "  son Id: " + output.getHeroId() );


    }catch (HeroExistException e){
        System.out.println("hela; " + e.getMessage());
    }catch (InvalidHeroNameException e ){
        System.out.println(e.getMessage());
    }


}
