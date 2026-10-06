package Hero.api.infra;
import Hero.api.application.port.HeroRepository;
import Hero.api.domain.entites.Hero;
import java.util.*;

public class InMemoryHeroRepository  implements HeroRepository {
    private final  HashSet<Hero> heros =  new HashSet<>();

    @Override
    public void save(Hero hero) {
        heros.add(hero);

    }

    @Override
    public boolean verifyName(String name) {
        for (Hero hero : heros){
            if (hero.getName().equals(name)){
                return true;
            }

        }
        return false;
    }
}
