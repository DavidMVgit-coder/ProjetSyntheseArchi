package api;

import application.CreateHero.CreateHeroUseCase;
import application.GetHero.GetHeroUseCase;
import application.port.HeroRepository;
import api.error.ApiExceptionHandler;
import api.hero.HeroResource;
import infrastructure.InMemoryHeroRepository;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

public class ApiConfiguration extends ResourceConfig {

    public ApiConfiguration() {
        property(ServerProperties.WADL_FEATURE_DISABLE, true);
        HeroRepository repository = new InMemoryHeroRepository();

        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(new CreateHeroUseCase(repository)).to(CreateHeroUseCase.class);
                bind(new GetHeroUseCase(repository)).to(GetHeroUseCase.class);
            }
        });

        register(HeroResource.class);
        register(ApiExceptionHandler.class);
        register(JsonConfiguration.class);
        register(JacksonFeature.withoutExceptionMappers());
    }
}
