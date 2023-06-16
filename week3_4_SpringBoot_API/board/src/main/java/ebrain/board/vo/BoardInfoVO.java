package ebrain.board.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoardInfoVO {
    /**
     * 게시물 ID
     */
    private int boardId;
    /**
     * 게시물의 카테고리 ID
     */
    private int categoryId;
    /**
     * 카테고리 이름
     */
    private String categoryName;
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

    private List<Integer> deletedAttachmentIds;

    private List<MultipartFile> files;



}