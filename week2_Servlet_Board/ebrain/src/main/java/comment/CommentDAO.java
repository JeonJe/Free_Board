package comment;

import utils.DBUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {
    /**
     * Save the Comment Information
     *
     * @param comment
     * @throws Exception
     */
    public void save(Comment comment) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            LocalDateTime currentTime = LocalDateTime.now();
            comment.setCreatedAt(currentTime);

            String sql = "INSERT INTO comment (writer, content, created_at, board_id) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, comment.getWriter());
            pstmt.setString(2, comment.getContent());
            pstmt.setString(3, comment.getCreatedAt());
            pstmt.setInt(4, comment.getBoardId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeConnection(conn);
        }

    }

    /**
     * Get Comment Information by Board Id
     *
     * @param id
     * @return
     * @throws Exception
     */
    public List<Comment> getCommentsByBoardId(int id) throws Exception {
        List<Comment> comments = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT * FROM comment WHERE board_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int commentId = rs.getInt("comment_id");

                String writer = rs.getString("writer");
                String content = rs.getString("content");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                Comment comment = new Comment(commentId, id, writer, content, createdAt);
                comments.add(comment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeConnection(conn);
        }
        return comments;
    }
}
