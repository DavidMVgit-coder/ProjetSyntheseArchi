package Hero.api.domain.exception;

public class InvalidCharacteristicException extends RuntimeException{
    public InvalidCharacteristicException(){
        super("Le nom de la caracteristique est invalide");
    }

}
