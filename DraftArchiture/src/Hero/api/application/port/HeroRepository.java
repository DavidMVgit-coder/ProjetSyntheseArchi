package Hero.api.application.port;
import Hero.api.domain.entites.Hero;

public interface HeroRepository {
    void sauvegader(Hero hero);
    boolean verifierNom(String nom);
}
