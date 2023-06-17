package ebrain.board.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * 댓글 정보를 담는 VO 클래스
 */
@Data
public class CommentVO {
    /**
     * 댓글 ID
     */
    private int commentId;
    /**
     * 댓글이 속한 게시물의 ID
     */
    private int boardId;
    /**
     * 댓글 작성자
     */
    private String writer;
    /**
     * 댓글 내용
     */
    private String content;
    /**
     * 댓글 작성 시간
     */
    private LocalDateTime createdAt;

}
