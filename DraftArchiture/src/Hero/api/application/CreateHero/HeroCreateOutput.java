package Hero.api.application.CreateHero;

import java.util.UUID;

public class HeroCreateOutput{
   private final UUID heroId;
   private final String name;
   public HeroCreateOutput(UUID heroId, String name){
       this.heroId = heroId;
       this.name = name;
   }

    public UUID getHeroId() {
        return heroId;
    }

    public String getName() {
        return name;
    }
}


