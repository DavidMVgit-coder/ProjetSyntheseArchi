package Hero.api.domain.entites;
import java.util.UUID;
import Hero.api.domain.valeur_object.Caracteristique;
import java.util.regex.Pattern;
import Hero.api.domain.exception.InvalidHeroNameException;

public class Hero {
    private UUID heroId;
    private  String nom;
    private Caracteristique force;
    private Caracteristique dexterite;
    private Caracteristique constitution;
    private Caracteristique intelligence;
    private Caracteristique sagesse;
    private Caracteristique charisme;
    public  Hero( String nom,
                  Caracteristique force,
                 Caracteristique dexterite,
                 Caracteristique constitution,
                 Caracteristique intelligence,
                 Caracteristique sagesse,
                 Caracteristique charisme

                  ){

        if (!valideNom(nom)){
            throw new InvalidHeroNameException();
        }
        this.heroId = UUID.randomUUID();
        this.nom = nom;
        this.force = force ;
        this.dexterite = dexterite;
        this.constitution = constitution;
        this.intelligence = intelligence;
        this.sagesse = sagesse;
        this.charisme = charisme;
    }

    public boolean valideNom(String nom){
       Pattern VERIFIER = Pattern.compile("[\\p{L} '\\-]+");

        if (nom == null  || nom.isBlank()){
            return false;
        }
        if (nom.length() < 2 || nom.length() > 30){
            return false;
        }
        if (nom.charAt(0) == ' ' || nom.charAt(nom.length() - 1) == ' ') {
            return false;
        }
        return VERIFIER.matcher(nom).matches() ;
    }

    public UUID getHeroId() {
        return heroId;
    }

    public String getNom() {
        return nom;
    }
}
