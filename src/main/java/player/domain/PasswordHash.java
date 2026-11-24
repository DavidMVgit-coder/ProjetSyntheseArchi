package player.domain;

import java.util.Objects;

public record PasswordHash(String value) {

    public PasswordHash {
        Objects.requireNonNull(value, "Le hachage est obligatoire.");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Le hachage ne peut pas être vide.");
        }
    }

    @Override
    public String toString() {
        return "PasswordHash[***]";
    }
}
