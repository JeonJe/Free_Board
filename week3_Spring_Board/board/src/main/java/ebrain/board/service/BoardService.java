package ebrain.board.service;

import ebrain.board.exception.FormValidationInvalidException;
import ebrain.board.exception.PasswordInvalidException;
import ebrain.board.mapper.AttachmentRepository;
import ebrain.board.mapper.BoardRepository;

import ebrain.board.utils.BoardUtils;
import ebrain.board.vo.AttachmentVO;
import ebrain.board.vo.BoardVO;


import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private BoardRepository boardRepository;


    @Autowired
    private AttachmentRepository attachmentRepository;


    public List<BoardVO> searchBoards(Map<String, Object> params) throws SQLException {
        return boardRepository.searchBoards(params);
    }

    public int countSearchBoards(Map<String, Object> searchParams) throws SQLException {
        return boardRepository.countSearchBoards(searchParams);
    }


    public void saveBoard(BoardVO board, List<MultipartFile> files) throws Exception {


        if (!BoardUtils.checkFormValidation(board.getWriter(), board.getPassword(),
                board.getConfirmPassword(), board.getTitle(), board.getContent())) {
            throw new FormValidationInvalidException("폼 유효성 검증에 실패하였습니다.");
        }

        String uploadPath = ResourceBundle.getBundle("application").getString("UPLOAD_PATH");
        String hashedPassword = BoardUtils.hashPassword(board.getPassword());
        board.setPassword(hashedPassword);
        board.setConfirmPassword(hashedPassword);

        boardRepository.saveBoard(board);
        int boardId = board.getBoardId();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String originName = file.getOriginalFilename();
                String numberedFileName = BoardUtils.uploadFile(file, uploadPath);
                AttachmentVO attachment = new AttachmentVO();
                attachment.setBoardId(boardId);
                attachment.setFileName(numberedFileName);
                attachment.setOriginName(originName);
                attachmentRepository.saveAttachment(attachment);
            }
        }
    }

    public void updateBoard(BoardVO board, List<MultipartFile> files,
                            List<Integer> deletedAttachmentIds) throws Exception {

        if (!BoardUtils.checkFormValidation(board.getWriter(), board.getPassword(),
                board.getPassword(), board.getTitle(), board.getContent())) {
            throw new FormValidationInvalidException("폼 유효성 검증에 실패하였습니다.");
        }

        String uploadPath = ResourceBundle.getBundle("application").getString("UPLOAD_PATH");
        String hashedPassword = BoardUtils.hashPassword(board.getPassword());
        board.setPassword(hashedPassword);
        board.setConfirmPassword(hashedPassword);

        for (Integer deletedId : deletedAttachmentIds) {
            String deletedFileName = attachmentRepository.
                    getAttachmentInfoByAttachmentId(deletedId).getFileName();

            //업로드 폴더에서 파일 삭제
            if (deletedFileName != null) {
                File file = new File(uploadPath + '/' + deletedFileName);
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
                String numberedFileName = BoardUtils.uploadFile(file, uploadPath);
                AttachmentVO attachment = new AttachmentVO();
                attachment.setBoardId(board.getBoardId());
                attachment.setFileName(numberedFileName);
                attachment.setOriginName(originName);
                attachmentRepository.saveAttachment(attachment);
            }
        }
    }


    public BoardVO getBoardInfoByBoardId(int boardId) throws SQLException {

        BoardVO board = boardRepository.getBoardInfoById(boardId);

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("boardId", boardId);
        paramsMap.put("currentVisitCount", board.getVisitCount());
        boardRepository.updateVisitCount(paramsMap);

        return board;
    }

    public void deleteBoard(int boardId, String password) throws SQLException, PasswordInvalidException {

        String hashedPassword = BoardUtils.hashPassword(password);

        if (!boardRepository.validatePassword(boardId, hashedPassword)) {
            throw new PasswordInvalidException("패스워드가 틀렸습니다.");
        }

        boardRepository.deleteBoard(boardId);
    }

}
