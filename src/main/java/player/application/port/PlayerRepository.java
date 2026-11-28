package player.application.port;

import player.domain.Email;
import player.domain.Player;

import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository {

    void create(Player player);

    Optional<Player> findById(UUID id);

    Optional<Player> findByEmail(Email email);
}
