package exceptions;

public class PasswordInvalidException extends Exception {
    /**
     * Exception to password Invalidation
     * @param message
     */
    public PasswordInvalidException(String message) {
        super(message);
    }

}