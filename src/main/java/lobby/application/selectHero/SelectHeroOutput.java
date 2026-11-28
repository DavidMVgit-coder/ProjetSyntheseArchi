package lobby.application.selectHero;

import java.util.List;
import java.util.UUID;

public record SelectHeroOutput(
        UUID id,
        UUID hostId,
        String status,
        List<Member> players,
        int availableSlots) {

    public record Member(UUID playerId, UUID heroId) {
    }
}
