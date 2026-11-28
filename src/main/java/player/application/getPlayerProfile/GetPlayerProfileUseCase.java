package player.application.getPlayerProfile;

import player.application.PlayerNotFoundException;
import player.application.port.PlayerRepository;
import player.domain.Player;

import java.util.Objects;
import java.util.UUID;

public class GetPlayerProfileUseCase {

    private final PlayerRepository repository;

    public GetPlayerProfileUseCase(PlayerRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public PlayerProfileOutput execute(String requestedPlayerId, UUID authenticatedPlayerId) {
        Objects.requireNonNull(authenticatedPlayerId, "Le joueur authentifié est obligatoire.");

        if (!isOwnProfile(requestedPlayerId, authenticatedPlayerId)) {
            throw new ProfileAccessForbiddenException();
        }

        Player player = repository.findById(authenticatedPlayerId)
                .orElseThrow(() -> new PlayerNotFoundException(authenticatedPlayerId));

        return new PlayerProfileOutput(
                player.getId(),
                player.getPseudonym().getValue(),
                player.getEmail().getValue());
    }

    private static boolean isOwnProfile(String requestedPlayerId, UUID authenticatedPlayerId) {
        if (requestedPlayerId == null) {
            return false;
        }
        try {
            return UUID.fromString(requestedPlayerId).equals(authenticatedPlayerId);
        } catch (IllegalArgumentException notAnId) {
            return false;
        }
    }
}
