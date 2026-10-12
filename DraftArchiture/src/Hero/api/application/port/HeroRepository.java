package Hero.api.application.port;
import Hero.api.domain.entites.Hero;

public interface HeroRepository {
    void save(Hero hero);
    boolean verifyName(String name);
}
