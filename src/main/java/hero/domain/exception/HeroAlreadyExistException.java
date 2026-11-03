package hero.domain.exception;

import hero.domain.HeroName;

public class HeroAlreadyExistException extends RuntimeException {

    private final HeroName name;

    public HeroAlreadyExistException(HeroName name) {
        super("Le hero '" + name + "' existe déjà.");
        this.name = name;
    }

    public HeroName getName() {
        return name;
    }
}
