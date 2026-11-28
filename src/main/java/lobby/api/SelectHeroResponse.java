package lobby.api;

import lobby.application.selectHero.SelectHeroOutput;

import java.util.List;
import java.util.UUID;

public record SelectHeroResponse(
        UUID id,
        UUID hostId,
        String status,
        List<Player> players,
        int availableSlots) {

    public record Player(UUID playerId, UUID heroId) {
    }

    public static SelectHeroResponse from(SelectHeroOutput output) {
        List<Player> players = output.players().stream()
                .map(member -> new Player(member.playerId(), member.heroId()))
                .toList();
        return new SelectHeroResponse(output.id(), output.hostId(), output.status(),
                players, output.availableSlots());
    }
}
