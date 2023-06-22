package ebrain.board.response;

import ebrain.board.vo.BoardVO;
import ebrain.board.vo.CategoryVO;
import ebrain.board.vo.SearchConditionVO;
import lombok.Data;

import java.util.List;

/**
 * 게시글 검색 결과 응답을 나타내는 클래스입니다.
 */
//TODO : 한군데에서 묶어서 처리해도 됨,ignore json 패턴 참고

@Data
public class BoardSearchResponse {
    /**
     * 검색 조건을 나타내는 필드입니다.
     */
    private SearchConditionVO searchCondition;

    /**
     * 검색된 게시글 목록을 나타내는 필드입니다.
     */
    private List<BoardVO> searchBoards;

    /**
     * 카테고리 목록을 나타내는 필드입니다.
     */
    private List<CategoryVO> categories;

    /**
     * 검색된 게시글의 총 개수를 나타내는 필드입니다.
     */
    private int totalCount;
}