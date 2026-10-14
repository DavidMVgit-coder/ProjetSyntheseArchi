package application.port;

import domain.entites.Hero;
import domain.nom.HeroName;

public interface HeroRepository {

    void save(Hero hero);

    boolean existsByName(HeroName name);
}
