package hero.domain.exception;

import hero.domain.HeroName;

public class HeroAlreadyExistsException extends DomainException {

    public HeroAlreadyExistsException(HeroName name) {
        super("Le héros '" + name + "' existe déjà.");
    }
}
