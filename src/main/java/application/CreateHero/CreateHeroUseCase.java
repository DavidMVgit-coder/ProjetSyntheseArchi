package application.CreateHero;

import application.InvalidHeroInputException;
import application.port.HeroRepository;
import domain.caracteristiques.Characteristic;
import domain.caracteristiques.Species;
import domain.classe.HeroClass;
import domain.entites.Hero;
import domain.exception.HeroAlreadyExistsException;
import domain.factory.HeroFactory;
import domain.nom.HeroName;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public class CreateHeroUseCase {

    private final HeroRepository repository;
    private final HeroOutputMapper outputMapper;

    public CreateHeroUseCase(HeroRepository repository) {
        this(repository, new HeroOutputMapper());
    }

    CreateHeroUseCase(HeroRepository repository, HeroOutputMapper outputMapper) {
        this.repository = repository;
        this.outputMapper = outputMapper;
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
