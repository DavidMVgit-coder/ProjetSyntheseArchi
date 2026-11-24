package player.infrastructure;

import player.application.port.PlayerRepository;
import player.domain.Email;
import player.domain.Player;
import player.domain.Pseudonym;
import player.domain.exception.EmailAlreadyTakenException;
import player.domain.exception.PseudonymAlreadyTakenException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class InMemoryPlayerRepository implements PlayerRepository {

    private final Map<UUID, Player> players = new HashMap<>();
    // Pseudonym et Email se comparent sans tenir compte des majuscules (c'est le domaine qui le décide, via equals).
    private final Set<Pseudonym> pseudonyms = new HashSet<>();
    private final Set<Email> emails = new HashSet<>();

    @Override
    public synchronized void create(Player player) {
        // Toujours le pseudonyme d'abord, puis le courriel : le résultat ne dépend pas de l'ordre d'insertion.
        if (pseudonyms.contains(player.getPseudonym())) {
            throw new PseudonymAlreadyTakenException(player.getPseudonym().getValue());
        }
        if (emails.contains(player.getEmail())) {
            throw new EmailAlreadyTakenException(player.getEmail().getValue());
        }
        players.put(player.getId(), player);
        pseudonyms.add(player.getPseudonym());
        emails.add(player.getEmail());
    }
}
