package lobby.application.port;

import java.util.UUID;

public interface ActiveGames {

    boolean isHeroInActiveGame(UUID heroId);
}
