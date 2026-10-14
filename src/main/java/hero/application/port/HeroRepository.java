package hero.application.port;

import hero.domain.Hero;
import hero.domain.HeroName;

public interface HeroRepository {

    void save(Hero hero);

    boolean existsByName(HeroName name);
}
