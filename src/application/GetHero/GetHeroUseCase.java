package application.GetHero;

import application.CreateHero.HeroOutput;
import application.CreateHero.HeroOutputMapper;
import application.port.HeroRepository;

import java.util.UUID;

public class GetHeroUseCase {

    private final HeroRepository repository;
    private final HeroOutputMapper outputMapper;

    public GetHeroUseCase(HeroRepository repository) {
        this(repository, new HeroOutputMapper());
    }

    GetHeroUseCase(HeroRepository repository, HeroOutputMapper outputMapper) {
        this.repository = repository;
        this.outputMapper = outputMapper;
    }

    public HeroOutput execute(String id) {
        UUID heroId;
        try {
            heroId = UUID.fromString(id);
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new InvalidHeroIdException(id);
        }

        return repository.fetchById(heroId)
                .map(outputMapper::toOutput)
                .orElseThrow(() -> new HeroNotFoundException(id));
    }
}
