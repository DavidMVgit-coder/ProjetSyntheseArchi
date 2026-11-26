package hero.application.port;

import hero.domain.Hero;

import java.util.Optional;
import java.util.UUID;

public interface HeroRepository {

    void create(Hero hero);

    Optional<Hero> fetchById(UUID heroId);
}
