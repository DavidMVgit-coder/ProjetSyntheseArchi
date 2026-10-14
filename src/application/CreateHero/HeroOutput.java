package application.CreateHero;

public record HeroOutput(
        String id,
        String name,
        String species,
        String heroClass,
        int level,
        int currentHitPoints,
        int maxHitPoints,
        int currentMana,
        int maxMana) {
}
