package player.application.getPlayerProfile;

import java.util.UUID;

/** Profil renvoyé au joueur : jamais de mot de passe ni de hachage. */
public record PlayerProfileOutput(UUID id, String pseudonym, String email) {
}
