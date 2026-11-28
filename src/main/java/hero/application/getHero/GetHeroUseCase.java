package hero.application.getHero;

import hero.application.HeroOutput;
import hero.application.HeroOutputMapper;
import hero.application.port.HeroRepository;

import java.util.Objects;
import java.util.UUID;

public class GetHeroUseCase {

    private final HeroRepository repository;
    private final HeroOutputMapper outputMapper = new HeroOutputMapper();

    public GetHeroUseCase(HeroRepository repository) {
        this.repository = Objects.requireNonNull(repository);
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
