package player.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Racine d'agrégat : un joueur. Son identité est son identifiant.
 * Il ne détient jamais le mot de passe en clair, seulement son hachage.
 */
public final class Player {

    private final UUID id;
    private final Pseudonym pseudonym;
    private final Email email;
    private final PasswordHash passwordHash;

    public Player(Pseudonym pseudonym, Email email, PasswordHash passwordHash) {
        this.id = UUID.randomUUID();
        this.pseudonym = Objects.requireNonNull(pseudonym, "Le pseudonyme est obligatoire.");
        this.email = Objects.requireNonNull(email, "Le courriel est obligatoire.");
        this.passwordHash = Objects.requireNonNull(passwordHash, "Le hachage est obligatoire.");
    }

    public UUID getId() {
        return id;
    }

    public Pseudonym getPseudonym() {
        return pseudonym;
    }

    public Email getEmail() {
        return email;
    }

    public PasswordHash getPasswordHash() {
        return passwordHash;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Player player && id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
