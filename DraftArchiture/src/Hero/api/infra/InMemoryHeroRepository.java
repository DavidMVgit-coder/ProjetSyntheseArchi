package Hero.api.infra;
import Hero.api.application.port.HeroRepository;
import Hero.api.domain.entites.Hero;


import java.util.*;

public class InMemoryHeroRepository  implements HeroRepository {
    private final  HashSet<Hero> heros =  new HashSet<>();


    @Override
    public void sauvegader(Hero hero) {
        heros.add(hero);



    }

    @Override
    public boolean verifierNom(String nom) {
        for (Hero hero : heros){
            if (hero.getNom().equals(nom)){
                return true;
            }

        }
        return false;
    }
}
