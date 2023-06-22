package ebrain.board.response;

import ebrain.board.vo.AttachmentVO;
import ebrain.board.vo.BoardVO;
import ebrain.board.vo.CommentVO;
import lombok.Data;

import java.util.List;

/**
 * 게시글 상세 정보 응답을 나타내는 클래스입니다.
 */
@Data
public class BoardInfoResponse {
    /**
     * 게시글 정보를 나타내는 필드입니다.
     */
    private BoardVO board;

    /**
     * 카테고리 이름을 나타내는 필드입니다.
     */
    private String categoryName;

    /**
     * 댓글 목록을 나타내는 필드입니다.
     */
    private List<CommentVO> comments;

    /**
     * 첨부 파일 목록을 나타내는 필드입니다.
     */
    private List<AttachmentVO> attachments;
}