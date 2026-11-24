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
    private final Set<Pseudonym> pseudonyms = new HashSet<>();
    private final Set<Email> emails = new HashSet<>();

    @Override
    public synchronized void create(Player player) {
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
