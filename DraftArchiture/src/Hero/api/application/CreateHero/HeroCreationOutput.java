package Hero.api.application.CreateHero;

import java.util.UUID;

public class HeroCreationOutput{
   private final UUID heroId;
   private final String nom;
   public HeroCreationOutput(UUID heroId, String nom){
       this.heroId = heroId;
       this.nom = nom;
   }

    public UUID getHeroId() {
        return heroId;
    }

    public String getNom() {
        return nom;
    }
}


