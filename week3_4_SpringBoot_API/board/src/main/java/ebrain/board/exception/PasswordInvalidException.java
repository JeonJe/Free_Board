package ebrain.board.exception;

/**
 * 예외: 비밀번호 유효성 검증 실패
 */
public class PasswordInvalidException extends Exception {
    /**
     * 생성자: 비밀번호 유효성 검증 실패에 대한 예외를 생성합니다.
     *
     * @param message 예외 메시지
     */
    public PasswordInvalidException(String message) {
        super(message);
    }
}