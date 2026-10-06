package Hero.api.domain.value_object;
import Hero.api.domain.exception.InvalidCharacteristicException;
public class Characteristics{
    private String name;
    private int value;
    private int motificator;
    public  Characteristics(String name, int value){
        if (name == null || name.isBlank()){
            throw  new InvalidCharacteristicException();
        }
        this.name =  name;
        this.value = value;
        this.motificator = (value - 10) / 2 ;

    }
}
