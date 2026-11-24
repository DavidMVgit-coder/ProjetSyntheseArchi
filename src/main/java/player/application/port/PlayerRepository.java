package player.application.port;

import player.domain.Player;

public interface PlayerRepository {

    void create(Player player);
}
