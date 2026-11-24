package player.application.port;

import player.domain.PasswordHash;
import player.domain.RawPassword;

/**
 * Port : l'application sait qu'il faut hacher un mot de passe, pas avec quel algorithme.
 * Implémenté par l'infrastructure (Argon2id) ; remplaçable par un faux dans les tests.
 */
public interface PasswordHasher {

    PasswordHash hash(RawPassword password);

    boolean matches(RawPassword password, PasswordHash hash);
}
