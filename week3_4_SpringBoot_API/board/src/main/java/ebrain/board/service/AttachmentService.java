package ebrain.board.service;

import ebrain.board.mapper.AttachmentRepository;
import ebrain.board.mapper.CategoryRepository;
import ebrain.board.vo.AttachmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;


/**
 * 첨부파일 서비스를 제공하는 클래스입니다.
 */
@Service
public class AttachmentService {

    private final AttachmentRepository attachmentMapper;



    /**
     * AttachmentService의 생성자입니다.
     *
     * @param attachmentMapper AttachmentRepository 객체
     * @throws SQLException SQL 예외
     */
    @Autowired
    public AttachmentService(AttachmentRepository attachmentMapper) throws SQLException {
        this.attachmentMapper = attachmentMapper;
    }

    /**
     * 게시물 ID에 해당하는 첨부파일 목록을 가져옵니다.
     *
     * @param boardId 게시물 ID
     * @return 첨부파일 목록
     * @throws SQLException SQL 예외
     */
    public List<AttachmentVO> getAttachmentsByBoardId(int boardId) throws SQLException {
        return attachmentMapper.getAttachmentsByBoardId(boardId);
    }

    public AttachmentVO getAttachmentByAttachmentId(int attachmentId) throws SQLException{
        return attachmentMapper.getAttachmentInfoByAttachmentId(attachmentId);
    }


}