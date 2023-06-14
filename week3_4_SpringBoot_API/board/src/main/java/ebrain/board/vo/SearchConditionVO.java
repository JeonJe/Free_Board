package ebrain.board.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchConditionVO {
    /**
     * 검색할 카테고리의 ID
     */
    private Integer categoryId;


    /**
     * 검색 키워드
     */
    private String searchText;

    /**
     * 검색 시작 날짜
     */
    private LocalDate startDate;

    /**
     * 검색 종료 날짜
     */
    private LocalDate endDate;
    /**
     * 페이지 사이즈
     */
    private Integer pageSize;
    /**
     * 현재페이지
     */
    private Integer currentPage;

    /**
     * 페이지 offset
     */
    private Integer offset;




}
