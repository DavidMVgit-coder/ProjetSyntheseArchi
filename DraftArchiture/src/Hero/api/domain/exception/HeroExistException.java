package Hero.api.domain.exception;

public class HeroExistException extends RuntimeException {
    private final String name;
    public  HeroExistException(String name){
        super("L'héro : "+ name + " existe déjà");
        this.name=  name;
    }

    public String getName() {
        return name;
    }
}
