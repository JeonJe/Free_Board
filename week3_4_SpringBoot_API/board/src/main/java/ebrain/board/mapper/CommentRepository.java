package ebrain.board.mapper;

import ebrain.board.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 게시글 댓글과 관련된 데이터베이스 작업을 담당하는 인터페이스
 */
@Mapper
public interface CommentRepository {

    /**
     * 특정 게시글의 모든 댓글을 데이터베이스에서 조회하는 메서드
     *
     * @param boardId 조회하고자 하는 게시글의 ID
     * @return 조회된 댓글의 리스트
     */
    List<CommentVO> getCommentsByBoardId(int boardId);

    /**
     * 게시글에 새로운 댓글을 저장하는 메서드
     *
     * @param comment 저장할 댓글 정보
     */
    void saveComment(CommentVO comment);

}