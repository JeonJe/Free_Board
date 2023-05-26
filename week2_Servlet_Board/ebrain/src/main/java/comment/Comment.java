package comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment {
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


    public Comment() {
    }

    public Comment(int commentId, int boardId, String writer, String content, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.boardId = boardId;
        this.writer = writer;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
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
}