package hero.application.createHero;

import hero.application.HeroOutput;
import hero.application.HeroOutputMapper;
import hero.application.InvalidHeroInputException;
import hero.application.port.HeroRepository;
import hero.domain.Characteristic;
import hero.domain.Hero;
import hero.domain.HeroClass;
import hero.domain.HeroFactory;
import hero.domain.HeroName;
import hero.domain.Species;
import hero.domain.exception.HeroAlreadyExistsException;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CreateHeroUseCase {

    private final HeroRepository repository;
    private final HeroOutputMapper outputMapper = new HeroOutputMapper();

    public CreateHeroUseCase(HeroRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public HeroOutput execute(CreateHeroCommand command) {
        HeroName name = new HeroName(command.name());
        Species species = parseEnum(command.species(), Species.class, "species");
        HeroClass heroClass = parseEnum(command.heroClass(), HeroClass.class, "heroClass");

        if (repository.existsByName(name)) {
            throw new HeroAlreadyExistsException(name);
        }

        Map<Characteristic, Integer> baseValues = new EnumMap<>(Characteristic.class);
        baseValues.put(Characteristic.STRENGTH, command.strength());
        baseValues.put(Characteristic.DEXTERITY, command.dexterity());
        baseValues.put(Characteristic.CONSTITUTION, command.constitution());
        baseValues.put(Characteristic.INTELLIGENCE, command.intelligence());
        baseValues.put(Characteristic.WISDOM, command.wisdom());
        baseValues.put(Characteristic.CHARISMA, command.charisma());

        Hero hero = HeroFactory.createHeroLevel1(name, species, heroClass, baseValues);
        repository.save(hero);
        return outputMapper.toOutput(hero);
    }

    private static <E extends Enum<E>> E parseEnum(String value, Class<E> enumType, String field) {
        if (value == null || value.isBlank()) {
            throw new InvalidHeroInputException("Le champ '" + field + "' est obligatoire.");
        }

        try {
            return Enum.valueOf(enumType, value.strip().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new InvalidHeroInputException(
                    "La valeur '" + value + "' n'est pas valide pour le champ '" + field + "'.");
        }
    }
}
