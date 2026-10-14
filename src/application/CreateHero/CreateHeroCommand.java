package application.CreateHero;

import domain.caracteristiques.Characteristic;
import domain.caracteristiques.Species;
import domain.classe.HeroClass;
import domain.nom.HeroName;

import java.util.Map;

public record CreateHeroCommand(
        HeroName name,
        Species species,
        HeroClass heroClass,
        Map<Characteristic, Integer> baseValues) {
}
