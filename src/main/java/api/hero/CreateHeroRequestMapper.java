package api.hero;

import application.CreateHero.CreateHeroCommand;
import application.InvalidHeroInputException;

public final class CreateHeroRequestMapper {

    public CreateHeroCommand toCommand(CreateHeroRequest request) {
        if (request == null) {
            throw new InvalidHeroInputException("Le corps de la requête est obligatoire.");
        }

        return new CreateHeroCommand(
                request.name(),
                request.species(),
                request.heroClass(),
                request.strength(),
                request.dexterity(),
                request.constitution(),
                request.intelligence(),
                request.wisdom(),
                request.charisma());
    }
}
