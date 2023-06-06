package ebrain.board.exception;

public class FormValidationInvalidException extends Exception{
    /**
     * 폼 유효성 검증 실패에 대한 예외
     * @param message 예외 메시지
     */
    public FormValidationInvalidException(String message) {
        super(message);
    }
}