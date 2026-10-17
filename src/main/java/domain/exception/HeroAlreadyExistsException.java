package domain.exception;

import domain.nom.HeroName;

public class HeroAlreadyExistsException extends DomainException {

    public HeroAlreadyExistsException(HeroName name) {
        super("Le héros '" + name + "' existe déjà.");
    }
}
