package ebrain.board.reponse;

import ebrain.board.vo.BoardVO;
import ebrain.board.vo.CategoryVO;
import ebrain.board.vo.SearchConditionVO;
import lombok.Data;

import java.util.List;

@Data

public class SearchResponse {
    private SearchConditionVO searchCondition;
    private List<BoardVO> searchBoards;
    private List<CategoryVO> categories;
    private int totalCount;
    private int totalPages;
}
