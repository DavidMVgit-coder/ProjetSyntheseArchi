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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class InMemoryPlayerRepository implements PlayerRepository {

    private final Map<UUID, Player> players = new HashMap<>();
    private final Set<Pseudonym> pseudonyms = new HashSet<>();
    private final Map<Email, Player> playersByEmail = new HashMap<>();

    @Override
    public synchronized void create(Player player) {
        if (pseudonyms.contains(player.getPseudonym())) {
            throw new PseudonymAlreadyTakenException(player.getPseudonym().getValue());
        }
        if (playersByEmail.containsKey(player.getEmail())) {
            throw new EmailAlreadyTakenException(player.getEmail().getValue());
        }
        players.put(player.getId(), player);
        pseudonyms.add(player.getPseudonym());
        playersByEmail.put(player.getEmail(), player);
    }

    @Override
    public synchronized Optional<Player> findById(UUID id) {
        return Optional.ofNullable(players.get(id));
    }

    @Override
    public synchronized Optional<Player> findByEmail(Email email) {
        return Optional.ofNullable(playersByEmail.get(email));
    }
}
