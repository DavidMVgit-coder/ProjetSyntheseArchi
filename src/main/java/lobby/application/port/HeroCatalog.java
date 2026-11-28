package lobby.application.port;

import java.util.Optional;
import java.util.UUID;


public interface HeroCatalog {

    Optional<HeroSummary> findById(UUID heroId);
}
