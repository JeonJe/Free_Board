package category;

import board.Board;
import utils.DBUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    /**
     * 카테고리 ID, 이름 목록 반환
     * @return
     * @throws Exception
     */
    public static List<Category> getAllCategory() throws Exception {
        List<Category> categories = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT * FROM category";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                Category category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                category.setCategoryName((rs.getString("category_name")));
                categories.add(category);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeConnection(conn);
        }
        return categories;
    }

    /**
     * 카테고리ID로 카테고리 이름 반환
     * @param id
     * @return
     * @throws Exception
     */
    public static String getCategoryNameById(int id) throws Exception {

        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT * FROM category WHERE category_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                return rs.getString("category_name");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeConnection(conn);
        }
        return null;
    }
}
