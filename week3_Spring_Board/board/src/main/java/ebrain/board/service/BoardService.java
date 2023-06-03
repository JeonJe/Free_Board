package ebrain.board.service;

import ebrain.board.exception.FormValidationInvalidException;
import ebrain.board.mapper.AttachmentMapper;
import ebrain.board.mapper.BoardMapper;
import ebrain.board.mapper.CategoryMapper;
import ebrain.board.mapper.CommentMapper;
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
    private BoardMapper boardMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 주어진 검색 조건에 맞는 게시글을 검색하는 메서드
     *
     * @param params 검색 조건을 담은 맵 객체
     * @return 검색된 게시글과 총 개수를 담은 맵 객체
     */
    public Map<String, Object> searchBoards(Map<String, Object> params) {
        Map<String, Object> resultMap = new HashMap<>();
        List<BoardVO> boards = boardMapper.searchBoards(params);
        int totalCount = boardMapper.countSearchBoards(params);
        resultMap.put("boards", boards);
        resultMap.put("totalCount", totalCount);
        return resultMap;
    }


    /**
     * 게시글을 저장하는 메서드
     *
     * @param categoryValue    게시글의 카테고리 ID를 나타내는 문자열
     * @param writer           게시글 작성자
     * @param password         게시글 비밀번호
     * @param confirmPassword  게시글 비밀번호 확인
     * @param title            게시글 제목
     * @param content          게시글 내용
     * @param files            첨부 파일 배열
     * @throws Exception 예외 발생 시
     */
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

        boardMapper.saveBoard(board);
        int boardId = board.getBoardId(); // Assuming you have a getter

        String uploadPath = ResourceBundle.getBundle("application").getString("UPLOAD_PATH");
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String numberedFileName = BoardUtils.uploadFile(file, uploadPath); // Make sure this method accepts MultipartFile
                AttachmentVO attachment = new AttachmentVO();
                attachment.setBoardId(boardId);
                attachment.setFileName(numberedFileName);
                attachment.setOriginName(file.getOriginalFilename());
                attachmentMapper.saveAttachment(attachment);
            }
        }
    }

    /**
     * 주어진 게시글 ID에 해당하는 게시글 정보를 조회하는 메서드
     *
     * @param boardId 조회할 게시글의 ID
     * @return 조회된 게시글과 관련된 정보를 담은 맵 객체
     */
    public Map<String, Object> getBoardInfoByBoardId(int boardId) {
        Map<String, Object> resultMap = new HashMap<>();
        BoardVO board = boardMapper.getBoardInfoById(boardId);

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("boardId", boardId);
        paramsMap.put("currentVisitCount", board.getVisitCount());
        boardMapper.updateVisitCount(paramsMap);

        List<CommentVO> comments = commentMapper.getCommentsByBoardId(boardId);
        List<AttachmentVO> attachments = attachmentMapper.getAttachmentsByBoardId(boardId);
        String categoryName = categoryMapper.getCategoryNameById(boardId);

        resultMap.put("board", board);
        resultMap.put("comments", comments);
        resultMap.put("attachments", attachments);
        resultMap.put("categoryName", categoryName);
        return resultMap;
    }
}
