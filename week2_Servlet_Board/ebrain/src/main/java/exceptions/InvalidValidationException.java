package exceptions;

public class InvalidValidationException extends Exception{
    /**
     * Exception to form Invalidation
     * @param message
     */
    public InvalidValidationException(String message) {
        super(message);
    }
}
