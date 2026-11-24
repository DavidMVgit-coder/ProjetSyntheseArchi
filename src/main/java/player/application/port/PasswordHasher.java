package player.application.port;

import player.domain.PasswordHash;
import player.domain.RawPassword;


public interface PasswordHasher {

    PasswordHash hash(RawPassword password);

    boolean matches(RawPassword password, PasswordHash hash);
}
