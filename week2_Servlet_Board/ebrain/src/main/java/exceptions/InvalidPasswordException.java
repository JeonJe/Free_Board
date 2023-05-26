package exceptions;

public class InvalidPasswordException extends Exception {
    /**
     * Exception to password Invalidation
     * @param message
     */
    public InvalidPasswordException(String message) {
        super(message);
    }

}