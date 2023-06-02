package ebrain.board.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class CommentVO {
    /**
     * Command ID
     */
    private int commentId;
    /**
     * ID of the board containing the comment
     */
    private int boardId;
    /**
     * Comment Writer
     */
    private String writer;
    /**
     * Comment Content
     */
    private String content;
    /**
     * Comment Created time
     */
    private LocalDateTime createdAt;

    public String getCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return createdAt.format(formatter);
    }

}
