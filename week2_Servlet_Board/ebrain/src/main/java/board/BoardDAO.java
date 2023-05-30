package board;

import exceptions.PasswordInvalidException;
import org.apache.ibatis.session.SqlSession;
import utils.DBUtils;

import java.sql.Timestamp;
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
    public boolean validatePassword(int boardId, String enteredPassword) throws PasswordInvalidException {

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
                throw new PasswordInvalidException("비밀번호가 틀립니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.sessionClose(session);
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
