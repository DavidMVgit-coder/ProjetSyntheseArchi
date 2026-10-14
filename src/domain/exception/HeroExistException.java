
package domain.exception;

import domain.nom.HeroName;

public class HeroExistException extends RuntimeException {

    private final HeroName name;

    public HeroExistException(HeroName name) {
        super("Le hero '" + name + "' existe déjà.");
        this.name = name;
    }

    public HeroName getName() {
        return name;
    }
}
