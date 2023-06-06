package ebrain.board.mapper;

import ebrain.board.vo.BoardVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
/**
 * 게시글과 관련된 데이터베이스 작업을 담당하는 인터페이스
 */
@Mapper
public interface BoardRepository {
    /**
     * 주어진 검색 조건에 맞는 게시글을 검색하는 메서드
     *
     * @param params 검색 조건을 담은 맵 객체
     * @return 검색된 게시글의 리스트
     */
    List<BoardVO> searchBoards(Map<String, Object> params);

    /**
     * 주어진 검색 조건에 맞는 게시글의 개수를 반환하는 메서드
     *
     * @param params 검색 조건을 담은 맵 객체
     * @return 검색된 게시글의 개수
     */
    int countSearchBoards(Map<String, Object> params);

    /**
     * 게시글의 조회수를 증가시키는 메서드
     *
     * @param params 조회수를 증가시킬 게시글의 정보를 담은 맵 객체
     */
    void updateVisitCount(Map<String, Object> params);

    /**
     * 주어진 게시글 ID에 해당하는 게시글의 정보를 조회하는 메서드
     *
     * @param boardId 조회할 게시글의 ID
     * @return 조회된 게시글의 정보
     */
    BoardVO getBoardInfoById(int boardId);

    /**
     * 주어진 게시글 ID와 입력된 비밀번호가 일치하는지 확인하는 메서드
     *
     * @param boardId        게시글의 ID
     * @param enteredPassword 입력된 비밀번호
     * @return 비밀번호 일치 여부
     */
    boolean validatePassword(int boardId, String enteredPassword);

    /**
     * 게시글을 수정하는 메서드
     *
     * @param board 수정할 게시글의 정보
     */
    void updateBoard(BoardVO board);

    /**
     * 주어진 게시글 ID에 해당하는 게시글을 삭제하는 메서드
     *
     * @param boardId 삭제할 게시글의 ID
     */
    void deleteBoard(int boardId);

    /**
     * 새로운 게시글을 저장하는 메서드
     *
     * @param board 저장할 게시글의 정보
     * @return 저장된 게시글의 ID
     */
    int saveBoard(BoardVO board);

}
