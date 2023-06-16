package ebrain.board.service;

import ebrain.board.exception.FormValidationInvalidException;
import ebrain.board.exception.PasswordInvalidException;
import ebrain.board.mapper.AttachmentRepository;
import ebrain.board.mapper.BoardRepository;

import ebrain.board.utils.BoardUtils;
import ebrain.board.vo.AttachmentVO;
import ebrain.board.vo.BoardVO;


import ebrain.board.vo.SearchConditionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.sql.SQLException;
import java.util.*;

/**
 * 게시판 관련 비지니스로직을 처리하는 서비스 클래스
 */
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * BoardService 생성자입니다.
     *
     * @param boardRepository      BoardRepository 객체
     * @param attachmentRepository AttachmentRepository 객체
     */
    private final AttachmentRepository attachmentRepository;

    private final CategoryService categoryService;

    /**
     * 파일 업로드 경로
     */
    @Value("${UPLOAD_PATH}")
    private String UPLOAD_PATH;

    @Autowired
    public BoardService(BoardRepository boardRepository, AttachmentRepository attachmentRepository, CategoryService categoryService) {
        this.boardRepository = boardRepository;
        this.attachmentRepository = attachmentRepository;
        this.categoryService = categoryService;
    }
    /**
     * 검색 조건에 해당하는 게시글 목록을 조회하는 메서드입니다.
     *
     * @param params 검색 조건을 담은 맵
     * @return 검색 결과로 조회된 게시글 목록
     * @throws SQLException SQL 예외 발생 시
     */
    public List<BoardVO> searchBoards(SearchConditionVO searchParams) throws SQLException {

        return boardRepository.searchBoards(searchParams);
    }
    /**
     * 검색 조건에 해당하는 게시글의 총 개수를 조회하는 메서드입니다.
     *
     * @param searchParams 검색 조건을 담은 맵
     * @return 검색 결과로 조회된 게시글의 총 개수
     * @throws SQLException SQL 예외 발생 시
     */
    public int countSearchBoards(SearchConditionVO searchParams) throws SQLException {
        return boardRepository.countSearchBoards(searchParams);
    }

    /**
     * 게시글을 저장하는 메서드입니다.
     *
     * @param board 게시글 정보를 담은 BoardVO 객체
     * @param files 업로드된 첨부 파일 목록을 담은 List<MultipartFile> 객체
     * @throws Exception 예외 발생 시
     */
    public void saveBoard(BoardVO board, List<MultipartFile> files) throws Exception {

        if (!BoardUtils.checkFormValidation(board.getWriter(), board.getPassword(),
                board.getConfirmPassword(), board.getTitle(), board.getContent())) {
            throw new FormValidationInvalidException("폼 유효성 검증에 실패하였습니다.");
        }

        String hashedPassword = BoardUtils.hashPassword(board.getPassword());
        board.setPassword(hashedPassword);
        board.setConfirmPassword(hashedPassword);

        boardRepository.saveBoard(board);
        int boardId = board.getBoardId();

        if (files != null){
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String originName = file.getOriginalFilename();
                    String numberedFileName = BoardUtils.uploadFile(file, UPLOAD_PATH);
                    AttachmentVO attachment = new AttachmentVO();
                    attachment.setBoardId(boardId);
                    attachment.setFileName(numberedFileName);
                    attachment.setOriginName(originName);
                    attachmentRepository.saveAttachment(attachment);
                }
            }
        }

    }
    /**
     * 게시글을 수정하는 메서드입니다.
     *
     * @param board                수정할 게시글 정보를 담은 BoardVO 객체
     * @param files                업로드된 첨부 파일 목록을 담은 List<MultipartFile> 객체
     * @param deletedAttachmentIds 삭제할 첨부 파일의 ID 목록을 담은 List<Integer> 객체
     * @throws Exception 게시글 수정 과정에서 발생하는 예외
     */
    public void updateBoard(BoardVO board, List<MultipartFile> files,
                            List<Integer> deletedAttachmentIds) throws Exception {

        if (!BoardUtils.checkFormValidation(board.getWriter(), board.getPassword(),
                board.getPassword(), board.getTitle(), board.getContent())) {
            throw new FormValidationInvalidException("폼 유효성 검증에 실패하였습니다.");
        }

        String hashedPassword = BoardUtils.hashPassword(board.getPassword());
        board.setPassword(hashedPassword);
        board.setConfirmPassword(hashedPassword);

        for (Integer deletedId : deletedAttachmentIds) {
            String deletedFileName = attachmentRepository.
                    getAttachmentInfoByAttachmentId(deletedId).getFileName();

            //업로드 폴더에서 파일 삭제
            if (deletedFileName != null) {
                File file = new File(UPLOAD_PATH + '/' + deletedFileName);
                if (file.exists()) {
                    file.delete();
                }
            }
            //데이터베이스서 첨부파일 정보 삭제
            attachmentRepository.deleteAttachmentByAttachmentId(deletedId);
        }

        boardRepository.updateBoard(board);

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                //새로운 파일 업로드 및 데이터베이스 저장
                String originName = file.getOriginalFilename();
                String numberedFileName = BoardUtils.uploadFile(file, UPLOAD_PATH);
                AttachmentVO attachment = new AttachmentVO();
                attachment.setBoardId(board.getBoardId());
                attachment.setFileName(numberedFileName);
                attachment.setOriginName(originName);
                attachmentRepository.saveAttachment(attachment);
            }
        }
    }

    /**
     * 주어진 게시글 ID에 해당하는 게시글의 상세 정보를 조회하는 메서드입니다.
     * 조회 시 해당 게시글의 방문수(visitCount)도 증가합니다.
     *
     * @param boardId 조회할 게시글의 ID
     * @return 조회된 게시글의 상세 정보
     * @throws SQLException SQL 예외 발생 시
     */
    public BoardVO getBoardInfoByBoardId(int boardId) throws SQLException {

        BoardVO board = boardRepository.getBoardInfoById(boardId);
        board.setCategoryName(categoryService.getCategoryNameByCategoryId(board.getCategoryId()));

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("boardId", boardId);
        paramsMap.put("currentVisitCount", board.getVisitCount());
        boardRepository.updateVisitCount(paramsMap);

        return board;
    }
    /**
     * 게시글을 삭제하는 메서드입니다.
     *
     * @param boardId  삭제할 게시글의 ID
     * @param password 게시글을 삭제하기 위한 비밀번호
     * @throws SQLException            SQL 예외 발생 시
     * @throws PasswordInvalidException 비밀번호가 일치하지 않을 경우
     */
    public void deleteBoard(int boardId, String password) throws SQLException, PasswordInvalidException {

        String hashedPassword = BoardUtils.hashPassword(password);

        if (!boardRepository.validatePassword(boardId, hashedPassword)) {
            throw new PasswordInvalidException("패스워드가 틀렸습니다.");
        }

        boardRepository.deleteBoard(boardId);
    }

}
