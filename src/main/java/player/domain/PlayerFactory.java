package player.domain;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class PlayerFactory {

    private static final String ID_NAMESPACE = "player:";

    private PlayerFactory() {
    }

    public static Player create(Pseudonym pseudonym, Email email, PasswordHash passwordHash) {
        return new Player(idFor(email), pseudonym, email, passwordHash);
    }

    // Identifiant déterministe dérivé du courriel : unique tant que le courriel l'est.
    private static UUID idFor(Email email) {
        byte[] name = (ID_NAMESPACE + email.key()).getBytes(StandardCharsets.UTF_8);
        return UUID.nameUUIDFromBytes(name);
    }
}
