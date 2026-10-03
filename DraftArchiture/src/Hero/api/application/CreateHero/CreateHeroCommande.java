package Hero.api.application.CreateHero;

public record CreateHeroCommande(
        String nom,
        int force,
        int dexterite,
        int constitution,
        int intelligence,
        int sagesse,
        int charisme
) {

}
