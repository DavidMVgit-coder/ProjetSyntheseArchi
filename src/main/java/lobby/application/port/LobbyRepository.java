package lobby.application.port;

import lobby.domain.Lobby;

import java.util.Optional;
import java.util.UUID;

public interface LobbyRepository {

    /**
     * Enregistre le nouveau lobby uniquement si son hôte n'est membre d'aucun
     * lobby ouvert. La vérification et l'enregistrement doivent être atomiques.
     *
     * @throws lobby.domain.exception.PlayerAlreadyInOpenLobbyException si l'hôte
     *         est déjà membre d'un lobby ouvert
     */
    void create(Lobby lobby);

    Optional<Lobby> findById(UUID id);
}
