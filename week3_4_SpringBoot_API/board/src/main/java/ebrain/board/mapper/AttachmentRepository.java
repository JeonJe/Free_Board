package ebrain.board.mapper;

import ebrain.board.vo.AttachmentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
 * 첨부파일과 관련된 데이터베이스 작업을 담당하는 인터페이스
 */
@Mapper
public interface AttachmentRepository {
    /**
     * 첨부파일을 데이터베이스에 저장하는 메서드
     *
     * @param attachment 첨부파일 정보가 담긴 AttachmentVO 객체
     */
    void saveAttachment(AttachmentVO attachment);

    /**
     * 특정 게시글에 첨부된 파일들의 정보를 데이터베이스에서 조회하는 메서드
     *
     * @param boardId 첨부파일을 조회하고자 하는 게시글의 ID
     * @return 게시글에 첨부된 파일 정보들의 리스트
     */
    List<AttachmentVO> getAttachmentsByBoardId(int boardId);

    /**
     * 특정 첨부파일의 정보를 데이터베이스에서 삭제하는 메서드
     *
     * @param attachmentId 삭제하고자 하는 첨부파일의 ID
     */
    void deleteAttachmentByAttachmentId(int attachmentId);

    /**
     * 특정 첨부파일의 정보를 데이터베이스에서 조회하는 메서드
     *
     * @param attachmentId 조회하고자 하는 첨부파일의 ID
     * @return 조회된 첨부파일의 정보
     */
    AttachmentVO getAttachmentInfoByAttachmentId(int attachmentId);
}
