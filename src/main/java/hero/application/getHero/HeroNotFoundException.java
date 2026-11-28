package hero.application.getHero;

public class HeroNotFoundException extends RuntimeException {

    public HeroNotFoundException(String id) {
        super("Aucun héros ne correspond à l'identifiant '" + id + "'.");
    }
}
