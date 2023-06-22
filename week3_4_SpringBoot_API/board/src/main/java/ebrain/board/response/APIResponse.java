package ebrain.board.response;

import lombok.Data;


/**
 * API 응답을 나타내는 클래스입니다.
 */
@Data
public class APIResponse {
    /**
     * 요청의 성공 여부를 나타내는 필드입니다.
     */
    private boolean success;

    /**
     * 응답 메시지를 나타내는 필드입니다.
     */
    private String message;

    /**
     * 응답 데이터를 나타내는 필드입니다.
     */
    private Object data;
}
