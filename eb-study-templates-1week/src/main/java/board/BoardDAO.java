package board;
import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class BoardDAO {

    static final String DB_URL = "jdbc:mysql://localhost:3306/ebrainsoft_study";
    static final String USER = "ebsoft";
    static final String PASS = "ebsoft";
    public void save(Board board) throws Exception {

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Class.forName("com.mysql.jdbc.Driver");
            LocalDateTime currentTime = LocalDateTime.now();
            board.setCreatedAt(currentTime);
            board.setModifiedAt(currentTime);

            String sql = "INSERT INTO board (category, writer, password, title, content, createdAt, modifiedAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, board.getCategory());
            pstmt.setString(2, board.getWriter());
            pstmt.setString(3, board.getPassword());
            pstmt.setString(4, board.getTitle());
            pstmt.setString(5, board.getContent());
            pstmt.setTimestamp(6, Timestamp.valueOf(board.getCreatedAt()));
            pstmt.setTimestamp(7, Timestamp.valueOf(board.getModifiedAt()));
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
