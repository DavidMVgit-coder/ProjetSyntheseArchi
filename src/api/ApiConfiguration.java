package api;

import application.CreateHero.CreateHeroUseCase;
import application.GetHero.GetHeroUseCase;
import application.port.HeroRepository;
import infrastructure.InMemoryHeroRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfiguration {

    @Bean
    public HeroRepository heroRepository() {
        return new InMemoryHeroRepository();
    }

    @Bean
    public CreateHeroUseCase createHeroUseCase(HeroRepository repository) {
        return new CreateHeroUseCase(repository);
    }

    @Bean
    public GetHeroUseCase getHeroUseCase(HeroRepository repository) {
        return new GetHeroUseCase(repository);
    }
}
