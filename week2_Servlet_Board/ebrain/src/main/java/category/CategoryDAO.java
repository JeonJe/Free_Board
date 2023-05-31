package category;

import org.apache.ibatis.session.SqlSession;
import utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    /**
     * Get All Categories Information
     * @return
     * @throws Exception
     */
    public static List<Category> getAllCategory() throws Exception {
        List<Category> categories = new ArrayList<>();
        SqlSession session = null;

        try {
            session = DBUtils.openSession();
            categories = session.selectList("CategoryMapper.getAllCategory");

        } finally {
            DBUtils.sessionClose(session);
        }
        return categories;
    }

    /**
     * Get Category Name By Category Id
     *
     * @param categoryId
     * @return
     * @throws Exception
     */
    public static String getCategoryNameById(int categoryId) throws Exception {
        String categoryName = null;
        SqlSession session = null;

        try {
            session = DBUtils.openSession();
            categoryName = session.selectOne("CategoryMapper.getCategoryNameById", categoryId);

        } finally {
            DBUtils.sessionClose(session);
        }
        return categoryName;

    }
}
