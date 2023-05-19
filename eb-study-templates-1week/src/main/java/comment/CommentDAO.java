package comment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    static final String DB_URL = "jdbc:mysql://localhost:3306/ebrainsoft_study";
    static final String USER = "ebsoft";
    static final String PASS = "ebsoft";

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public void save(Comment comment) throws Exception {

        try (Connection conn = getConnection()) {
            LocalDateTime currentTime = LocalDateTime.now();
            comment.setCreatedAt(currentTime);

            String sql = "INSERT INTO comment (writer, content, createdAt, board_id) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, comment.getWriter());
            pstmt.setString(2, comment.getContent());
            pstmt.setString(3, comment.getCreatedAt());
            pstmt.setInt(4, comment.getBoardId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Comment> getCommentsByBoardId(int id) throws Exception {
        List<Comment> comments = new ArrayList<>();

        try (Connection conn = getConnection()) {

            String sql = "SELECT * FROM comment WHERE board_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int commentId = rs.getInt("comment_id");

                String writer = rs.getString("writer");
                String content = rs.getString("content");
                LocalDateTime createdAt = rs.getTimestamp("createdAt").toLocalDateTime();

                Comment comment = new Comment(commentId, id, writer, content, createdAt);
                comments.add(comment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }
}
