package lobby.application.selectHero;

import java.util.UUID;

public class HeroInGameException extends RuntimeException {

    public HeroInGameException(UUID heroId) {
        super("Le héros '" + heroId + "' est déjà engagé dans une partie en cours.");
    }
}
