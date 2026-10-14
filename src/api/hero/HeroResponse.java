package api.hero;

public record HeroResponse(
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
