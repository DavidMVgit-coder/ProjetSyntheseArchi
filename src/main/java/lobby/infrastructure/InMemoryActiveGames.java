package lobby.infrastructure;

import lobby.application.port.ActiveGames;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Version temporaire : les parties n'existent pas encore (story L6).
 * À remplacer par un adaptateur vers le module des parties.
 */
public class InMemoryActiveGames implements ActiveGames {

    private final Set<UUID> heroesInGame = new HashSet<>();

    public synchronized void add(UUID heroId) {
        heroesInGame.add(heroId);
    }

    @Override
    public synchronized boolean isHeroInActiveGame(UUID heroId) {
        return heroesInGame.contains(heroId);
    }
}
