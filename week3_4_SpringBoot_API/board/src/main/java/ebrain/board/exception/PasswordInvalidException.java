package ebrain.board.exception;

public class PasswordInvalidException extends Exception {
    /**
     * 비밀번호 유효성 검증 실패에 대한 예외
     * @param message 예외 메시지
     */
    public PasswordInvalidException(String message) {
        super(message);
    }
}