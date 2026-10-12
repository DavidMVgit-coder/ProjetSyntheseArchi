package Hero.api.domain.entites;
import java.util.UUID;
import Hero.api.domain.value_object.Characteristics;
import java.util.regex.Pattern;
import Hero.api.domain.exception.InvalidHeroNameException;

public class Hero {
    private UUID heroId;
    private  String name;
    private Characteristics force;
    private Characteristics dexterite;
    private Characteristics constitution;
    private Characteristics intelligence;
    private Characteristics sagesse;
    private Characteristics charisme;
    public  Hero( String name,
                  Characteristics force,
                  Characteristics dexterite,
                  Characteristics constitution,
                  Characteristics intelligence,
                  Characteristics sagesse,
                  Characteristics charisme

                  ){

        if (!validName(name)){
            throw new InvalidHeroNameException();
        }
        this.heroId = UUID.randomUUID();
        this.name = name;
        this.force = force ;
        this.dexterite = dexterite;
        this.constitution = constitution;
        this.intelligence = intelligence;
        this.sagesse = sagesse;
        this.charisme = charisme;
    }

    public boolean validName(String name){
       Pattern VERIFY = Pattern.compile("[\\p{L} '\\-]+");

        if (name == null  || name.isBlank()){
            return false;
        }
        if (name.length() < 2 || name.length() > 30){
            return false;
        }
        if (name.charAt(0) == ' ' || name.charAt(name.length() - 1) == ' ') {
            return false;
        }
        return VERIFY.matcher(name).matches() ;
    }

    public UUID getHeroId() {
        return heroId;
    }

    public String getName() {
        return name;
    }
}
