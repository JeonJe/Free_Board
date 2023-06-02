package ebrain.board.mapper;

import ebrain.board.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<CategoryVO> getAllCategory();
    String getCategoryNameById(int categoryId);
}
