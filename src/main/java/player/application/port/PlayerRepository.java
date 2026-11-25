package player.application.port;

import player.domain.Email;
import player.domain.Player;

import java.util.Optional;

public interface PlayerRepository {

    void create(Player player);

    Optional<Player> findByEmail(Email email);
}
