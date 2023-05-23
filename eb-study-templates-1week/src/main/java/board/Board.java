package board;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Board {
    /**
     * 게시글 ID
     */
    private int boardId;
    /**
     *게시글 카테고리
     */
    private String category;
    /**
     *게시글 작성자
     */
    private String writer;
    /**
     *게시글 비밀번호
     */
    private String password;
    /**
     *게시글 제목
     */
    private String title;
    /**
     *게시글 내용
     */
    private String content;
    /**
     *게시글 생성시간
     */
    private LocalDateTime createdAt;
    /**
     *게시글 수정시간
     */
    private LocalDateTime modifiedAt;
    /**
     *게시글 조회수
     */
    private int visitCount;

    public Board() {

    }

    public Board(int boardId, String category, String writer, String password, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, int visitCount) {
        this.boardId = boardId;
        this.category = category;
        this.writer = writer;
        this.password = password;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.visitCount = visitCount;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
