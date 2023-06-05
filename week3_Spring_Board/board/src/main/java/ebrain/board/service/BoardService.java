package ebrain.board.service;

import ebrain.board.exception.FormValidationInvalidException;
import ebrain.board.mapper.AttachmentRepository;
import ebrain.board.mapper.BoardRepository;
import ebrain.board.mapper.CategoryRepository;
import ebrain.board.mapper.CommentRepository;
import ebrain.board.utils.BoardUtils;
import ebrain.board.vo.AttachmentVO;
import ebrain.board.vo.BoardVO;

import ebrain.board.vo.CommentVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.*;

/**
 * 게시판 관련 비지니스로직을 처리하는 서비스 클래스
 */
@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private CategoryRepository categoryMapper;


    public Map<String, Object> searchBoards(Map<String, Object> params) {
        Map<String, Object> resultMap = new HashMap<>();
        List<BoardVO> boards = boardRepository.searchBoards(params);
        int totalCount = boardRepository.countSearchBoards(params);
        resultMap.put("boards", boards);
        resultMap.put("totalCount", totalCount);
        return resultMap;
    }



    public void saveBoard(String categoryValue, String writer, String password, String confirmPassword, String title, String content, MultipartFile[] files) throws Exception {
        int categoryId = Integer.parseInt(categoryValue);
        String hashedPassword = BoardUtils.hashPassword(password);

        if (!BoardUtils.checkFormValidation(writer, password, confirmPassword, title, content)) {
            throw new FormValidationInvalidException("폼 유효성 검증에 실패하였습니다.");
        }

        BoardVO board = new BoardVO();
        board.setCategoryId(categoryId);
        board.setWriter(writer);
        board.setPassword(hashedPassword);
        board.setTitle(title);
        board.setContent(content);
        board.setVisitCount(0);
        LocalDateTime currentTime = LocalDateTime.now();
        board.setCreatedAt(currentTime);
        board.setModifiedAt(currentTime);

        boardRepository.saveBoard(board);
        int boardId = board.getBoardId(); // Assuming you have a getter

        String uploadPath = ResourceBundle.getBundle("application").getString("UPLOAD_PATH");
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String numberedFileName = BoardUtils.uploadFile(file, uploadPath); // Make sure this method accepts MultipartFile
                AttachmentVO attachment = new AttachmentVO();
                attachment.setBoardId(boardId);
                attachment.setFileName(numberedFileName);
                attachment.setOriginName(file.getOriginalFilename());
                attachmentRepository.saveAttachment(attachment);
            }
        }
    }

    //boardVO로 리턴하게끔 개선 가능

    public BoardVO getBoardInfoByBoardId(int boardId) {
        Map<String, Object> resultMap = new HashMap<>();
        BoardVO board = boardRepository.getBoardInfoById(boardId);

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("boardId", boardId);
        paramsMap.put("currentVisitCount", board.getVisitCount());
        boardRepository.updateVisitCount(paramsMap);

//        List<CommentVO> comments = commentRepository.getCommentsByBoardId(boardId);
//        List<AttachmentVO> attachments = attachmentRepository.getAttachmentsByBoardId(boardId);
//        String categoryName = categoryMapper.getCategoryNameById(boardId);

//        resultMap.put("board", board);
//        resultMap.put("comments", comments);
//        resultMap.put("attachments", attachments);
//        resultMap.put("categoryName", categoryName);
        return board;
    }
}
