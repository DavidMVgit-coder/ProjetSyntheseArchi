package hero.infrastructure;

import hero.application.port.HeroRepository;
import hero.domain.Hero;
import hero.domain.HeroName;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InMemoryHeroRepository implements HeroRepository {

    private final List<Hero> heroes = new ArrayList<>();

    @Override
    public void save(Hero hero) {
        heroes.add(hero);
    }

    @Override
    public boolean existsByName(HeroName name) {
        for (Hero hero : heroes) {
            if (hero.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Optional<Hero> fetchById(UUID heroId) {
        return heroes.stream()
                .filter(hero -> hero.getHeroId().equals(heroId))
                .findFirst();
    }
}

