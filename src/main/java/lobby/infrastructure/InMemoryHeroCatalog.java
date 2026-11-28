package lobby.infrastructure;

import lobby.application.port.HeroCatalog;
import lobby.application.port.HeroSummary;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Version temporaire : les héros n'ont pas encore de propriétaire dans le module hero.
 * À remplacer par un adaptateur vers le module hero quand le propriétaire existera.
 */
public class InMemoryHeroCatalog implements HeroCatalog {

    private final Map<UUID, HeroSummary> heroes = new HashMap<>();

    public synchronized void add(HeroSummary hero) {
        heroes.put(hero.id(), hero);
    }

    @Override
    public synchronized Optional<HeroSummary> findById(UUID heroId) {
        return Optional.ofNullable(heroes.get(heroId));
    }
}
