package application.port;

import domain.entites.Hero;
import domain.nom.HeroName;

import java.util.Optional;
import java.util.UUID;

public interface HeroRepository {

    void save(Hero hero);

    boolean existsByName(HeroName name);

    Optional<Hero> fetchById(UUID heroId);
}
