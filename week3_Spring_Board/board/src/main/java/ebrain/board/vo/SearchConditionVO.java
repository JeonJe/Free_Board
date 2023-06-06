package ebrain.board.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchConditionVO {
    /**
     * 검색할 카테고리의 ID
     */
    private Integer category;

    /**
     * 검색 결과 페이지 번호
     */
    private Integer page;

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
}
