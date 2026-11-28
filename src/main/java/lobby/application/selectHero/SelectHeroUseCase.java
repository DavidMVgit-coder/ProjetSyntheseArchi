package lobby.application.selectHero;

import lobby.application.ForbiddenActionException;
import lobby.application.LobbyNotFoundException;
import lobby.application.port.ActiveGames;
import lobby.application.port.HeroCatalog;
import lobby.application.port.HeroSummary;
import lobby.application.port.LobbyRepository;
import lobby.domain.Lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SelectHeroUseCase {

    private final LobbyRepository lobbyRepository;
    private final HeroCatalog heroCatalog;
    private final ActiveGames activeGames;

    public SelectHeroUseCase(LobbyRepository lobbyRepository, HeroCatalog heroCatalog, ActiveGames activeGames) {
        this.lobbyRepository = Objects.requireNonNull(lobbyRepository);
        this.heroCatalog = Objects.requireNonNull(heroCatalog);
        this.activeGames = Objects.requireNonNull(activeGames);
    }

    public SelectHeroOutput execute(UUID lobbyId, UUID playerId, UUID heroId, UUID authenticatedPlayerId) {
        Lobby lobby = lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new LobbyNotFoundException(lobbyId));

        if (!playerId.equals(authenticatedPlayerId)) {
            throw new ForbiddenActionException("Un joueur ne peut sélectionner un héros que pour lui-même.");
        }

        HeroSummary hero = heroCatalog.findById(heroId)
                .orElseThrow(() -> new UnknownHeroException(heroId));

        if (!hero.ownerId().equals(authenticatedPlayerId)) {
            throw new ForbiddenActionException("Un joueur ne peut sélectionner que ses propres héros.");
        }

        if (activeGames.isHeroInActiveGame(heroId)) {
            throw new HeroInGameException(heroId);
        }

        lobby.selectHero(playerId, heroId);
        lobbyRepository.save(lobby);

        return toOutput(lobby);
    }

    private SelectHeroOutput toOutput(Lobby lobby) {
        List<SelectHeroOutput.Member> players = new ArrayList<>();
        for (UUID memberId : lobby.getMemberIds()) {
            UUID selectedHeroId = lobby.getSelectedHeroId(memberId).orElse(null);
            players.add(new SelectHeroOutput.Member(memberId, selectedHeroId));
        }

        return new SelectHeroOutput(
                lobby.getId(),
                lobby.getHostId(),
                lobby.getStatus().name(),
                players,
                lobby.getAvailableSlots());
    }
}
