package player.application.login;

import player.application.port.PasswordHasher;
import player.application.port.PlayerRepository;
import player.application.port.TokenService;
import player.domain.Email;
import player.domain.PasswordHash;
import player.domain.Player;
import player.domain.RawPassword;
import player.domain.exception.InvalidPlayerDataException;
import player.domain.exception.PasswordTooShortException;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class LoginUseCase {

    private final PlayerRepository players;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;
    private final PasswordHash unknownAccountHash;

    public LoginUseCase(PlayerRepository players, PasswordHasher passwordHasher, TokenService tokenService) {
        this.players = Objects.requireNonNull(players);
        this.passwordHasher = Objects.requireNonNull(passwordHasher);
        this.tokenService = Objects.requireNonNull(tokenService);
        this.unknownAccountHash = passwordHasher.hash(new RawPassword(UUID.randomUUID().toString()));
    }

    public LoginOutput execute(LoginCommand command) {
        if (command == null
                || command.email() == null || command.email().isBlank()
                || command.password() == null || command.password().isEmpty()) {
            throw new InvalidLoginInputException();
        }

        Optional<Player> player = findByEmail(command.email());
        PasswordHash hashToCheck = player.map(Player::getPasswordHash).orElse(unknownAccountHash);
        boolean passwordMatches = matches(command.password(), hashToCheck);

        if (player.isEmpty() || !passwordMatches) {
            throw new InvalidCredentialsException();
        }

        UUID playerId = player.get().getId();
        return new LoginOutput(playerId, tokenService.issue(playerId));
    }

    private Optional<Player> findByEmail(String email) {
        try {
            return players.findByEmail(new Email(email));
        } catch (InvalidPlayerDataException malformedEmail) {
            return Optional.empty();
        }
    }

    private boolean matches(String password, PasswordHash hash) {
        try {
            return passwordHasher.matches(new RawPassword(password), hash);
        } catch (PasswordTooShortException tooShort) {
            return false;
        }
    }
}
