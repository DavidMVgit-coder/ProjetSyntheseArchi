package hero.domain.exception;

public class UnknownCharacteristicException extends RuntimeException {
  public UnknownCharacteristicException(String message) {
    super(message);
  }
}
