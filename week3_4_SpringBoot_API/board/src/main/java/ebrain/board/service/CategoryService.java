package ebrain.board.service;

import ebrain.board.mapper.CategoryRepository;
import ebrain.board.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * 카테고리 관련 기능을 제공하는 서비스 클래스
 */
@Service
public class CategoryService  {

    private final CategoryRepository categoryMapper;

    /**
     * CategoryService 생성자입니다.
     *
     * @param categoryMapper CategoryRepository 객체
     */
    @Autowired
    public CategoryService(CategoryRepository categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    /**
     * 모든 카테고리를 조회하는 메서드입니다.
     *
     * @return 모든 카테고리 리스트
     * @throws SQLException SQL 예외가 발생한 경우
     */
    public List<CategoryVO> getAllCategory() throws SQLException {
        return categoryMapper.getAllCategory();
    }

    /**
     * 주어진 카테고리 ID에 해당하는 카테고리 이름을 조회하는 메서드입니다.
     *
     * @param categoryId 카테고리 ID
     * @return 카테고리 이름
     * @throws SQLException SQL 예외가 발생한 경우
     */
    public String getCategoryNameByCategoryId(int categoryId) throws SQLException {
        return categoryMapper.getCategoryNameByCategoryId(categoryId);
    }

}
