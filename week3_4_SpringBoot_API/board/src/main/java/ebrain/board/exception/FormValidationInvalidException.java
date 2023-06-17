package ebrain.board.exception;

/**
 * 예외: 폼 유효성 검증 실패
 */
public class FormValidationInvalidException extends Exception{
    /**
     * 생성자: 폼 유효성 검증 실패에 대한 예외를 생성합니다.
     *
     * @param message 예외 메시지
     */
    public FormValidationInvalidException(String message) {
        super(message);
    }
}