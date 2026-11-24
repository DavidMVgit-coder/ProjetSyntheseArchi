package player.application.login;

import player.application.port.PasswordHasher;
import player.application.port.PlayerRepository;
import player.application.port.TokenService;
import player.domain.Email;
import player.domain.Player;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class LoginUseCase {

    private final PlayerRepository players;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;
    // Hash d'un compte qui n'existe pas : le temps de calcul est le même qu'il y ait un compte ou non.
    private final String unknownAccountHash;

    public LoginUseCase(PlayerRepository players, PasswordHasher passwordHasher, TokenService tokenService) {
        this.players = Objects.requireNonNull(players);
        this.passwordHasher = Objects.requireNonNull(passwordHasher);
        this.tokenService = Objects.requireNonNull(tokenService);
        this.unknownAccountHash = passwordHasher.hash(UUID.randomUUID().toString());
    }

    public LoginOutput execute(LoginCommand command) {
        if (command == null
                || command.email() == null || command.email().isBlank()
                || command.password() == null || command.password().isEmpty()) {
            throw new InvalidLoginInputException();
        }

        Optional<Player> player = players.findByEmail(new Email(command.email()));
        String hashToCheck = player.map(Player::passwordHash).orElse(unknownAccountHash);
        boolean passwordMatches = passwordHasher.matches(command.password(), hashToCheck);

        if (player.isEmpty() || !passwordMatches) {
            throw new InvalidCredentialsException();
        }

        UUID playerId = player.get().id();
        return new LoginOutput(playerId, tokenService.issue(playerId));
    }
}

