package ebrain.board.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 게시물 정보를 담는 VO 클래스
 */
@Data
public class BoardVO {
    /**
     * 게시물 ID
     */
    private int boardId;
    /**
     * 게시물의 카테고리 ID
     */
    private int categoryId;
    /**
     * 게시물 작성자
     */
    private String writer;
    /**
     * 게시물 비밀번호
     */
    private String password;

    /**
     * 게시물 비밀번호 확인
     */
    private String confirmPassword;
    /**
     * 게시물 제목
     */
    private String title;
    /**
     * 게시물 내용
     */
    private String content;
    /**
     * 게시물 작성 시간
     */
    private LocalDateTime createdAt;
    /**
     * 게시물 수정 시간
     */
    private LocalDateTime modifiedAt;
    /**
     * 게시물 조회수
     */
    private int visitCount;

    /**
     * 게시물 작성 시간을 yyyy.MM.dd HH:mm 형식으로 반환하는 메서드
     * @return 게시물 작성 시간 문자열
     */
//    public String getCreatedAt() {
//        if (createdAt == null) {
//            return "";
//        }
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
//        return createdAt.format(formatter);
//    }

    /**
     * 게시물 수정 시간을 yyyy.MM.dd HH:mm 형식으로 반환하는 메서드
     * @return 게시물 수정 시간 문자열
     */
//    public String getModifiedAt() {
//        if (modifiedAt == null) {
//            return "";
//        }
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
//        return modifiedAt.format(formatter);
//    }
}
