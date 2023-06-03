package ebrain.board.vo;

import lombok.Data;

/**
 * 첨부 파일 정보를 담는 VO 클래스
 */
@Data
public class AttachmentVO {
    /**
     * 첨부 파일 ID
     */
    private int attachmentId;
    /**
     * 첨부 파일을 포함하는 게시물의 ID
     */
    private int boardId;
    /**
     * 서버에 저장된 중복되지 않는 파일명
     */
    private String fileName;
    /**
     * 사용자가 업로드한 파일의 원본 이름
     */
    private String originName;
}