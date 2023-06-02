package ebrain.board.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class BoardVO {
    /**
     * Board ID
     */
    private int boardId;
    /**
     * Category ID of the board
     */
    private int categoryId;
    /**
     * Writer of the board
     */
    private String writer;
    /**
     * Password of the board
     */
    private String password;
    /**
     * Title of the board
     */
    private String title;
    /**
     * Content of the board
     */
    private String content;
    /**
     * Created Time of the board
     */
    private LocalDateTime createdAt;
    /**
     * Modified Time of the board
     */
    private LocalDateTime modifiedAt;
    /**
     * Visit count of the board
     */
    private int visitCount;

    public String getCreatedAt() {
        if (createdAt == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return createdAt.format(formatter);
    }

    public String getModifiedAt() {
        if (modifiedAt == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return modifiedAt.format(formatter);
    }



}
