package ebrain.board.service;

import ebrain.board.mapper.CommentRepository;
import ebrain.board.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 댓글 관련 기능을 제공하는 서비스 클래스
 */
@Service
public class CommentService {

    /**
     * 댓글 저장소 객체
     */
    private final CommentRepository commentRepository;

    /**
     * CommentService 생성자입니다.
     *
     * @param commentRepository CommentRepository 객체
     */
    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    /**
     * 댓글을 저장하는 메서드입니다.
     *
     * @param params 저장할 댓글 정보를 담은 맵
     * @throws SQLException SQL 예외가 발생한 경우
     */
    public void saveComment(Map<String, Object> params) throws SQLException {

        CommentVO comment = new CommentVO();

        comment.setBoardId((Integer) params.get("boardId"));
        comment.setContent((String) params.get("content"));
        comment.setWriter("-");

        commentRepository.saveComment(comment);
    }

    /**
     * 주어진 게시글 ID에 해당하는 모든 댓글을 조회하는 메서드입니다.
     *
     * @param boardId 조회할 게시글의 ID
     * @return 조회된 모든 댓글 리스트
     * @throws SQLException SQL 예외가 발생한 경우
     */
    public List<CommentVO> getCommentsByBoardId(int boardId) throws SQLException {
        return commentRepository.getCommentsByBoardId(boardId);
    }
}