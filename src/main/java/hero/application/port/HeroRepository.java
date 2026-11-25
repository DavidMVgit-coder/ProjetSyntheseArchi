package hero.application.port;

import hero.domain.Hero;
import hero.domain.HeroName;

import java.util.Optional;
import java.util.UUID;

public interface HeroRepository {

    void save(Hero hero);

    boolean existsByName(HeroName name);

    Optional<Hero> fetchById(UUID heroId);
}
