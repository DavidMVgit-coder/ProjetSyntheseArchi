package Hero.api.domain.exception;

public class HeroExistException extends RuntimeException {
    private final String nom;
    public  HeroExistException(String nom){
        super("L'héro : "+ nom + " existe déjà");
        this.nom =  nom;
    }

    public String getNom() {
        return nom;
    }
}
