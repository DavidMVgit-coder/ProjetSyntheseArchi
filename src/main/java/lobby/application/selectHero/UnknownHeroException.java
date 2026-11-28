package lobby.application.selectHero;

import java.util.UUID;

public class UnknownHeroException extends RuntimeException {

    public UnknownHeroException(UUID heroId) {
        super("Le héros '" + heroId + "' est introuvable.");
    }
}
