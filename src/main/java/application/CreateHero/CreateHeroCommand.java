package application.CreateHero;

public record CreateHeroCommand(
        String name,
        String species,
        String heroClass,
        int strength,
        int dexterity,
        int constitution,
        int intelligence,
        int wisdom,
        int charisma) {
}
