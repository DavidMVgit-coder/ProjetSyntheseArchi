package application.GetHero;

public class InvalidHeroIdException extends RuntimeException {

    public InvalidHeroIdException(String id) {
        super("L'identifiant du héros '" + id + "' n'est pas un UUID valide.");
    }
}
