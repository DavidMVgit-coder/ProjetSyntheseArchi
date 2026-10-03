package Hero.api.domain.exception;

public class InvalidHeroNameException extends  RuntimeException{
    public  InvalidHeroNameException(){
        super("Le nom est invalide");
    }
}
