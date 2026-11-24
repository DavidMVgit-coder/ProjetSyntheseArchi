package player.application.createPlayer;

import player.application.port.PasswordHasher;
import player.domain.PasswordHash;
import player.domain.RawPassword;

public class FakePasswordHasher implements PasswordHasher {

    @Override
    public PasswordHash hash(RawPassword password) {
        return new PasswordHash("fake$" + new StringBuilder(password.getValue()).reverse());
    }

    @Override
    public boolean matches(RawPassword password, PasswordHash hash) {
        return hash(password).equals(hash);
    }
}
