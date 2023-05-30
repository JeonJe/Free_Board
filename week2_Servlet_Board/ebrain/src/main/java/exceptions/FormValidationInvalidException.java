package exceptions;

public class FormValidationInvalidException extends Exception{
    /**
     * Exception to form Invalidation
     * @param message
     */
    public FormValidationInvalidException(String message) {
        super(message);
    }
}
