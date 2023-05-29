package board;

import exceptions.InvalidPasswordException;
import org.apache.ibatis.session.SqlSession;
import utils.DBUtils;
import utils.StringUtils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardDAO {

    /**
     * Save the Board Information
     *
     * @param board
     * @return
     * @throws Exception
     */
    public int saveBoard(Board board) throws Exception {
        int boardId = 0;
        SqlSession session = null;

        try {
            session = DBUtils.openSession();
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
            currentTime.format(formatter);
            board.setCreatedAt(currentTime);
            board.setModifiedAt(currentTime);

            session.insert("BoardMapper.saveBoard", board);
            session.commit();
            boardId = board.getBoardId();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.sessionClose(session);
        }
        return boardId;
    }

    /**
     * Delete the Board by Board ID
     *
     * @param boardId
     * @throws Exception
     */
    public void deleteBoard(int boardId) throws Exception {

        SqlSession session = null;
        try {
            session = DBUtils.openSession();
            session.delete("BoardMapper.deleteBoard", boardId);
            session.commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.sessionClose(session);
        }
    }

    /**
     * Update the Board Information
     *
     * @param board
     * @throws Exception
     */
    public void updateBoard(Board board) throws Exception {

        SqlSession session = null;
        try {
            session = DBUtils.openSession();
            session.update("BoardMapper.updateBoard", board);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.sessionClose(session);
        }
    }

    /**
     * Check the password
     *
     * @param boardId
     * @param enteredPassword
     * @return
     * @throws Exception
     */
    public boolean validatePassword(int boardId, String enteredPassword) throws InvalidPasswordException {
//        Connection conn = null;
//        try {
//            conn = DBUtils.getConnection();
//            String sql = "SELECT password FROM board WHERE board_id = ? AND password = ?";
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, boardId);
//            pstmt.setString(2, enteredPassword);
//            ResultSet rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                return true;
//            } else {
//                throw new InvalidPasswordException("비밀번호가 틀립니다.");
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        } finally {
//            DBUtils.closeConnection(conn);
//        }
        SqlSession session = null;

        try {
            session = DBUtils.openSession();
            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put("boardId", boardId);
            parameterMap.put("enteredPassword", enteredPassword);
            boolean result = session.selectOne("BoardMapper.validatePassword", parameterMap);

            if (result) {
                return true;
            } else {
                throw new InvalidPasswordException("비밀번호가 틀립니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.sessionClose(session);
        }
    }

    /**
     * Generate SQL statement with search condition
     *
     * @param sqlBuilder
     * @param params
     * @param startDate
     * @param endDate
     * @param category
     * @param searchText
     */
    private void applySearchConditions(StringBuilder sqlBuilder, List<Object> params, LocalDate startDate, LocalDate endDate, int category, String searchText) {

        //enter category
        if (category != 0) {
            sqlBuilder.append(" AND category_id = ?");
            params.add(category);
        }
        //enter search keyword
        if (!StringUtils.isNullOrEmpty(searchText) && !searchText.equals("null")) {
            sqlBuilder.append(" AND (title LIKE ? OR content LIKE ? OR writer LIKE ?)");
            params.add("%" + searchText + "%");
            params.add("%" + searchText + "%");
            params.add("%" + searchText + "%");
        }
        //enter start & end date
        if (startDate != null && endDate != null) {
            sqlBuilder.append(" AND created_at BETWEEN ? AND ?");
            params.add(Timestamp.valueOf(startDate.atStartOfDay()));
            params.add(Timestamp.valueOf(endDate.atStartOfDay().plusDays(1)));
        }
    }

    /**
     * Search for Boards that meet the search condition
     *
     * @param startDate
     * @param endDate
     * @param category
     * @param searchText
     * @param currentPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    public Map<String, Object> searchBoards(LocalDate startDate, LocalDate endDate, int category, String searchText, int currentPage, int pageSize) throws Exception {
        SqlSession session = null;
        Map<String, Object> resultMap = new HashMap<>();

        try {
            session = DBUtils.openSession();
            Map<String, Object> paramMap = new HashMap<>();
            Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
            Timestamp endTimestamp = Timestamp.valueOf(endDate.atStartOfDay().plusDays(1));

            paramMap.put("startDate", startTimestamp);
            paramMap.put("endDate", endTimestamp);
            paramMap.put("category", category);
            paramMap.put("searchText", searchText);
            paramMap.put("pageSize", pageSize);
            paramMap.put("offset", (currentPage - 1) * pageSize);

            List<Board> boards = session.selectList("BoardMapper.searchBoards", paramMap);
            int totalCount = session.selectOne("BoardMapper.countSearchBoards", paramMap);

            resultMap.put("boards", boards);
            resultMap.put("totalCount", totalCount);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.sessionClose(session);
        }
        return resultMap;
    }

    /**
     * Get Board Information By Board ID
     *
     * @param boardId
     * @return
     * @throws Exception
     */

    public Board getBoardInfoById(int boardId) throws Exception {
        SqlSession session = null;

        Board board = null;
        try {
            session = DBUtils.openSession();
            board = session.selectOne("BoardMapper.getBoardInfoById", boardId);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.sessionClose(session);
        }
        return board;
    }

    /**
     * Update Board view count
     *
     * @param boardId
     * @param currentVisitCount
     * @throws Exception
     */
    public void updateVisitCount(int boardId, int currentVisitCount) throws Exception {
        SqlSession session = null;
        try {
            session = DBUtils.openSession();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("boardId", boardId);
            paramMap.put("currentVisitCount", currentVisitCount );

            session.update("BoardMapper.updateVisitCount", paramMap);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.sessionClose(session);
        }
    }
}
