package Hero.api.application.CreateHero;

public record CreateHeroCommand(
        String name,
        int force,
        int dexterite,
        int constitution,
        int intelligence,
        int sagesse,
        int charisme
) {

}
