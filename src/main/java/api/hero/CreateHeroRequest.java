package api.hero;

public record CreateHeroRequest(
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
