package board;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {

    static final String DB_URL = "jdbc:mysql://localhost:3306/ebrainsoft_study";
    static final String USER = "ebsoft";
    static final String PASS = "ebsoft";

    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public void save(Board board) throws Exception {
        try (Connection conn = getConnection()) {

            LocalDateTime currentTime = LocalDateTime.now();
            board.setCreatedAt(currentTime);
            board.setModifiedAt(currentTime);

            String sql = "INSERT INTO board (category, writer, password, title, content, createdAt, modifiedAt,visitCount) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, board.getCategory());
            pstmt.setString(2, board.getWriter());
            pstmt.setString(3, board.getPassword());
            pstmt.setString(4, board.getTitle());
            pstmt.setString(5, board.getContent());
            pstmt.setString(6, board.getCreatedAt());
            pstmt.setString(7, board.getModifiedAt());
            pstmt.setInt(8, board.getVisitCount());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Board> searchBoards(LocalDate startDate, LocalDate endDate, String category, String search, int currentPage, int pageSize) throws Exception {
        List<Board> boards = new ArrayList<>();

        try (Connection conn = getConnection()) {
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM board WHERE 1=1");
            List<Object> params = new ArrayList<>();

            //특정 카테고리 선택 시
            if (category != null && !category.isEmpty() && !category.equalsIgnoreCase("all")) {
                sqlBuilder.append(" AND category = ?");
                params.add(category);
            }
            //특정 검색어 입력 시
            if (search != null && !search.isEmpty()) {
                sqlBuilder.append(" AND (title LIKE ? OR content LIKE ? OR writer LIKE ?)");
                params.add("%" + search + "%");
                params.add("%" + search + "%");
                params.add("%" + search + "%");
            }
            //등록일 입력 시
            if (startDate != null && endDate != null) {
                sqlBuilder.append(" AND createdAt BETWEEN ? AND ?");
                params.add(Timestamp.valueOf(startDate.atStartOfDay()));
                params.add(Timestamp.valueOf(endDate.atStartOfDay().plusDays(1)));
            }

            int offset = (currentPage - 1) * pageSize;
            sqlBuilder.append(" ORDER BY createdAt DESC LIMIT ?, ?");
            params.add(offset);
            params.add(pageSize);

            String sql = sqlBuilder.toString();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int boardId = rs.getInt("board_id");
                String rsCategory = rs.getString("category");
                String writer = rs.getString("writer");
                String password = rs.getString("password");
                String title = rs.getString("title");
                String content = rs.getString("content");
                LocalDateTime createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
                LocalDateTime modifiedAt = rs.getTimestamp("modifiedAt").toLocalDateTime();
                int visitCount = rs.getInt("visitCount");

                Board board = new Board(boardId, rsCategory, writer, password, title, content, createdAt, modifiedAt, visitCount);
                boards.add(board);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boards;
    }

    public int countBoards(LocalDate startDate, LocalDate endDate, String category, String search) throws Exception {
        int totalCount = 0;

        try (Connection conn = getConnection()) {
            StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(*) FROM board WHERE 1=1");
            List<Object> params = new ArrayList<>();

            //특정 카테고리 선택 시
            if (category != null && !category.isEmpty() && !category.equalsIgnoreCase("all")) {
                sqlBuilder.append(" AND category = ?");
                params.add(category);
            }
            //특정 검색어 입력 시
            if (search != null && !search.isEmpty()) {
                sqlBuilder.append(" AND (title LIKE ? OR content LIKE ? OR writer LIKE ?)");
                params.add("%" + search + "%");
                params.add("%" + search + "%");
                params.add("%" + search + "%");
            }
            //등록일 입력 시
            if (startDate != null && endDate != null) {
                sqlBuilder.append(" AND createdAt BETWEEN ? AND ?");
                params.add(Timestamp.valueOf(startDate.atStartOfDay()));
                params.add(Timestamp.valueOf(endDate.atStartOfDay().plusDays(1)));
            }

            String sql = sqlBuilder.toString();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCount;

    }

    public Board getBoardById(int id) throws Exception {
        Board board = null;
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM board WHERE board_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()){
                board = new Board();
                board.setBoardId(rs.getInt("board_id"));
                board.setCategory(rs.getString("category"));
                board.setWriter(rs.getString("writer"));
                board.setPassword(rs.getString("password"));
                board.setTitle(rs.getString("title"));
                board.setContent(rs.getString("content"));
                board.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                board.setModifiedAt(rs.getTimestamp("modifiedAt").toLocalDateTime());
                board.setVisitCount(rs.getInt("visitCount"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return board;
    }
}
