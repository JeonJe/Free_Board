package ebrain.board.vo;

import lombok.Data;

/**
 * 카테고리 정보를 담는 VO 클래스
 */
@Data
public class CategoryVO {
    /**
     * 카테고리 ID
     */
    private int categoryId;
    /**
     * 카테고리 이름
     */
    private String categoryName;
}