package api;

import api.error.ApiExceptionHandler;
import api.lobby.LobbyResource;
import hero.api.HeroResource;
import hero.application.createHero.CreateHeroUseCase;
import hero.application.getHero.GetHeroUseCase;
import hero.application.port.HeroRepository;
import hero.infrastructure.InMemoryHeroRepository;
import lobby.application.createLobby.CreateLobbyUseCase;
import lobby.application.leaveLobby.LeaveLobbyUseCase;
import lobby.application.port.LobbyRepository;
import lobby.infrastructure.InMemoryLobbyRepository;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import player.api.PlayerResource;
import player.application.createPlayer.CreatePlayerUseCase;
import player.application.port.PasswordHasher;
import player.application.port.PlayerRepository;
import player.infrastructure.Argon2PasswordHasher;
import player.infrastructure.InMemoryPlayerRepository;

public class ApiConfiguration extends ResourceConfig {

    public ApiConfiguration() {
        property(ServerProperties.WADL_FEATURE_DISABLE, true);
        HeroRepository repository = new InMemoryHeroRepository();
        LobbyRepository lobbyRepository = new InMemoryLobbyRepository();
        PlayerRepository playerRepository = new InMemoryPlayerRepository();
        PasswordHasher passwordHasher = new Argon2PasswordHasher();

        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(new CreateHeroUseCase(repository)).to(CreateHeroUseCase.class);
                bind(new GetHeroUseCase(repository)).to(GetHeroUseCase.class);
                bind(new CreateLobbyUseCase(lobbyRepository)).to(CreateLobbyUseCase.class);
                bind(new LeaveLobbyUseCase(lobbyRepository)).to(LeaveLobbyUseCase.class);
                bind(new CreatePlayerUseCase(playerRepository, passwordHasher)).to(CreatePlayerUseCase.class);
            }
        });

        register(HeroResource.class);
        register(LobbyResource.class);
        register(PlayerResource.class);
        register(ApiExceptionHandler.class);
        register(JsonConfiguration.class);
        register(JacksonFeature.withoutExceptionMappers());
    }
}
