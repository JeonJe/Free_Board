package comment;

import org.apache.ibatis.session.SqlSession;
import utils.DBUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class CommentDAO {

    /**
     * Save the Comment Information
     *
     * @param comment
     * @throws Exception
     */
    public void saveComment(Comment comment) throws Exception {
        SqlSession session = null;
        try {
            session = DBUtils.openSession();
            LocalDateTime currentTime = LocalDateTime.now();
            comment.setCreatedAt(currentTime);

            session.insert("CommentMapper.save", comment);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.sessionClose(session);
        }
    }

    /**
     * Get Comment Information by Board Id
     * @param id
     * @return
     * @throws Exception
     */
    public List<Comment> getCommentsByBoardId(int boardId) throws Exception {
        SqlSession session = null;
        List<Comment> comments = null;
        try {
            session = DBUtils.openSession();
            comments = session.selectList("CommentMapper.getCommentsByBoardId", boardId);

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            DBUtils.sessionClose(session);
        }
        return comments;
    }
}
