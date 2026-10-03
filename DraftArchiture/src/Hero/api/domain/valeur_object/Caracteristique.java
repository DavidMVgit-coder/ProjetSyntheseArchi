package Hero.api.domain.valeur_object;
import Hero.api.domain.exception.InvalidCharacteristicException;
public class Caracteristique {
    private String nom;
    private int valeur;
    private int motificateur;
    public  Caracteristique(String nom, int valeur){
        if (nom == null || nom.isBlank()){
            throw  new InvalidCharacteristicException();
        }
        this.nom =  nom ;
        this.valeur = valeur;
        this.motificateur = (valeur - 10) / 2 ;

    }
}
