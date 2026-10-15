package hero.domain;

import java.util.Objects;
import java.util.UUID;

public final class HeroId {

    private final UUID value;

    private HeroId(UUID value) {
        this.value = Objects.requireNonNull(value);
    }

    /** Génère un nouvel identifiant (création d'un héros). */
    public static HeroId generate() {
        return new HeroId(UUID.randomUUID());
    }

    /** Reconstruit un identifiant existant (réhydratation depuis la base de données). */
    public static HeroId of(UUID value) {
        return new HeroId(value);
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeroId other)) return false;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
