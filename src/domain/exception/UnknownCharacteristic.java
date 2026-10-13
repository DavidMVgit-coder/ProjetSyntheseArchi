package domain.exception;

public class UnknownCharacteristic extends RuntimeException {
  public UnknownCharacteristic(String message) {
    super(message);
  }
}
