package player.infrastructure;

import com.password4j.Argon2Function;
import com.password4j.Password;
import com.password4j.types.Argon2;
import player.application.port.PasswordHasher;
import player.domain.PasswordHash;
import player.domain.RawPassword;

/**
 * Adaptateur Argon2id (librairie Password4j). Le hachage obtenu commence par « $argon2id$ »
 * et contient un sel aléatoire : deux mots de passe identiques donnent deux hachages différents.
 */
public class Argon2PasswordHasher implements PasswordHasher {

    // Paramètres recommandés par l'OWASP : 19 Mio de mémoire, 2 itérations, 1 thread.
    private static final Argon2Function ARGON2 = Argon2Function.getInstance(19 * 1024, 2, 1, 32, Argon2.ID);

    @Override
    public PasswordHash hash(RawPassword password) {
        String hash = Password.hash(password.getValue()).addRandomSalt(16).with(ARGON2).getResult();
        return new PasswordHash(hash);
    }

    @Override
    public boolean matches(RawPassword password, PasswordHash hash) {
        return Password.check(password.getValue(), hash.value())
                .with(Argon2Function.getInstanceFromHash(hash.value()));
    }
}
