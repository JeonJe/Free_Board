package ebrain.board.dto;
import org.apache.ibatis.type.Alias;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Alias("Board")
@Getter
@Setter
@ToString
public class BoardDTO {
    /**
     * 게시판 ID
     */
    private int boardId;
    /**
     * 게시판의 카테고리 ID
     */
    private int categoryId;
    /**
     * 게시판 작성자
     */
    private String writer;
    /**
     * 게시판 비밀번호
     */
    private String password;
    /**
     * 게시판 제목
     */
    private String title;
    /**
     * 게시판 내용
     */
    private String content;
    /**
     * 게시판 작성 시간
     */
    private LocalDateTime createdAt;
    /**
     * 게시판 수정 시간
     */
    private LocalDateTime modifiedAt;
    /**
     * 게시판 방문 횟수
     */
    private int visitCount;

    public BoardDTO() {

    }
}
