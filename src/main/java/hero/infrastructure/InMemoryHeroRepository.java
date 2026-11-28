package hero.infrastructure;

import hero.application.port.HeroRepository;
import hero.domain.Hero;
import hero.domain.HeroName;
import hero.domain.exception.HeroAlreadyExistsException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class InMemoryHeroRepository implements HeroRepository {

    private final Map<UUID, Hero> heroes = new HashMap<>();
    private final Set<HeroName> names = new HashSet<>();

    @Override
    public synchronized void create(Hero hero) {
        if (names.contains(hero.getName())) {
            throw new HeroAlreadyExistsException(hero.getName());
        }
        heroes.put(hero.getHeroId(), hero);
        names.add(hero.getName());
    }

    @Override
    public synchronized Optional<Hero> fetchById(UUID heroId) {
        return Optional.ofNullable(heroes.get(heroId));
    }
}
