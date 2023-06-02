package ebrain.board.service;

import ebrain.board.mapper.CategoryMapper;
import ebrain.board.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryService  {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<CategoryVO> getAllCategory(){
        return categoryMapper.getAllCategory();

    }
    public String getCategoryNameById(int categoryId){
        return categoryMapper.getCategoryNameById(categoryId);
    }

}
