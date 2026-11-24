package player.application.port;

import player.domain.Player;

public interface PlayerRepository {

    /**
     * Enregistre le nouveau joueur uniquement si son pseudonyme et son courriel ne sont pas déjà
     * utilisés (sans tenir compte des majuscules). La vérification et l'enregistrement doivent être atomiques.
     *
     * @throws player.domain.exception.PseudonymAlreadyTakenException si le pseudonyme existe déjà
     * @throws player.domain.exception.EmailAlreadyTakenException si le courriel existe déjà
     */
    void create(Player player);
}
