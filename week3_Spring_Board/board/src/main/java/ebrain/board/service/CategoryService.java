package ebrain.board.service;

import ebrain.board.mapper.CategoryMapper;
import ebrain.board.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 카테고리 관련 기능을 제공하는 서비스 클래스
 */
@Service
public class CategoryService  {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 모든 카테고리를 조회하는 메서드
     *
     * @return 모든 카테고리 정보를 담은 리스트
     */
    public List<CategoryVO> getAllCategory(){
        return categoryMapper.getAllCategory();
    }

    /**
     * 주어진 카테고리 ID에 해당하는 카테고리 이름을 조회하는 메서드
     *
     * @param categoryId 조회할 카테고리의 ID
     * @return 조회된 카테고리 이름
     */
    public String getCategoryNameById(int categoryId){
        return categoryMapper.getCategoryNameById(categoryId);
    }

}
