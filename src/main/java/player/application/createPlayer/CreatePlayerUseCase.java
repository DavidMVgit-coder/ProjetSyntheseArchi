package player.application.createPlayer;

import player.application.port.PasswordHasher;
import player.application.port.PlayerRepository;
import player.domain.Email;
import player.domain.PasswordHash;
import player.domain.Player;
import player.domain.PlayerFactory;
import player.domain.Pseudonym;
import player.domain.RawPassword;
import player.domain.exception.InvalidPlayerDataException;

import java.util.Objects;

public class CreatePlayerUseCase {

    private final PlayerRepository repository;
    private final PasswordHasher passwordHasher;

    public CreatePlayerUseCase(PlayerRepository repository, PasswordHasher passwordHasher) {
        this.repository = Objects.requireNonNull(repository);
        this.passwordHasher = Objects.requireNonNull(passwordHasher);
    }

    public CreatePlayerOutput execute(CreatePlayerCommand command) {
        if (command == null) {
            throw new InvalidPlayerDataException("Le corps de la requête est obligatoire.");
        }

        Pseudonym pseudonym = new Pseudonym(command.pseudonym());
        Email email = new Email(command.email());
        RawPassword password = new RawPassword(command.password());

        PasswordHash hash = passwordHasher.hash(password);
        Player player = PlayerFactory.create(pseudonym, email, hash);
        repository.create(player);

        return new CreatePlayerOutput(player.getId(), pseudonym.getValue(), email.getValue());
    }
}
