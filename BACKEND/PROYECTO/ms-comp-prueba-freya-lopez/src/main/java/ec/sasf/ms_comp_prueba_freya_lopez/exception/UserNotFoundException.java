package ec.sasf.ms_comp_prueba_freya_lopez.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
            super();
        }

    public UserNotFoundException(String message) {
            super(message);
        }

    public UserNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

    public UserNotFoundException(Throwable cause) {
            super(cause);
        }
}
