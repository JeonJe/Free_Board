package comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment {
    private int commentID;
    private int boardId;
    private String writer;
    private String content;
    private LocalDateTime createdAt;

    public Comment() {
    }

    public Comment(int commentID, int boardId, String writer, String content, LocalDateTime createdAt) {
        this.commentID = commentID;
        this.boardId = boardId;
        this.writer = writer;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
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
