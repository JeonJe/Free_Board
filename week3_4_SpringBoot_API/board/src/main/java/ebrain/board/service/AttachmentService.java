package ebrain.board.service;

import ebrain.board.mapper.AttachmentRepository;
import ebrain.board.mapper.CategoryRepository;
import ebrain.board.vo.AttachmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class AttachmentService {

    private final AttachmentRepository attachmentMapper;

    @Autowired
    public AttachmentService(AttachmentRepository attachmentMapper) throws SQLException {
        this.attachmentMapper = attachmentMapper;
    }

    public List<AttachmentVO> getAttachmentsByBoardId(int boardId) throws SQLException {
        return attachmentMapper.getAttachmentsByBoardId(boardId);
    }
}
