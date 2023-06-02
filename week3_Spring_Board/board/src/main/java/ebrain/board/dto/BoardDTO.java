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

    public BoardDTO() {

    }
}
