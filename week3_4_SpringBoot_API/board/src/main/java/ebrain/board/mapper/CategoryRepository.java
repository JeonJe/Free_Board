package ebrain.board.mapper;

import ebrain.board.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 게시글 카테고리와 관련된 데이터베이스 작업을 담당하는 인터페이스
 */
@Mapper
public interface CategoryRepository {

    /**
     * 모든 게시글 카테고리를 데이터베이스에서 조회하는 메서드
     *
     * @return 모든 게시글 카테고리의 리스트
     */
    List<CategoryVO> getAllCategory();

    /**
     * 특정 카테고리 ID에 해당하는 카테고리 이름을 데이터베이스에서 조회하는 메서드
     *
     * @param categoryId 조회하고자 하는 카테고리의 ID
     * @return 조회된 카테고리의 이름
     */
    String getCategoryNameByCategoryId(int categoryId);
}