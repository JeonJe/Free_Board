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

    /**
     * 댓글 작성 시간을 형식에 맞추어 문자열로 반환
     * @return 형식에 맞는 댓글 작성 시간 문자열
     */
    public String getCreatedAt() {
        if (createdAt == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return createdAt.format(formatter);
    }
}
