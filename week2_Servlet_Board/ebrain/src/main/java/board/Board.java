package board;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Board {
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

    public Board() {

    }


    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return createdAt.format(formatter);
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return modifiedAt.format(formatter);
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }
}
