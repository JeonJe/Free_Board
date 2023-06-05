package ebrain.board.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchConditionVO {
    private Integer category;
    private Integer page;
    private String searchText;
    private LocalDate startDate;
    private LocalDate endDate;
}
