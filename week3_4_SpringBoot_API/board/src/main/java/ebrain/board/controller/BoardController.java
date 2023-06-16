package ebrain.board.controller;

import ebrain.board.reponse.APIResponse;
import ebrain.board.exception.PasswordInvalidException;
import ebrain.board.reponse.BoardInfoResponse;
import ebrain.board.reponse.BoardSearchResponse;
import ebrain.board.service.AttachmentService;
import ebrain.board.service.CategoryService;
import ebrain.board.service.CommentService;
import ebrain.board.utils.BoardUtils;
import ebrain.board.vo.*;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ebrain.board.service.BoardService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BoardController class
 * 게시판과 관련된 모든 요청을 처리하고 json 데이터 반환
 */
@CrossOrigin(origins = "http://localhost:8082")
@RestController

public class BoardController {

    private static final String SQL_ERROR_MESSAGE = "SQL 오류가 발생하였습니다.";
    /**
     * 게시글 관련 비지니스 로직 수행
     */
    private final BoardService boardService;
    /**
     * 게시판 카테고리 관련 비지니스 로직 수행
     */
    private final CategoryService categoryService;
    /**
     * 게시판 댓글 관련 비지니스 로직 수행
     */
    private final CommentService commentService;
    /**
     * 게시판 첨부파일 관련 비지니스 로직 수행
     */
    private final AttachmentService attachmentService;

    @Value("${UPLOAD_PATH}")
    private String UPLOAD_PATH;

    /**
     * 생성자 주입
     *
     * @param boardService      게시글 서비스
     * @param categoryService   카테고리 서비스
     * @param commentService    댓글 서비스
     * @param attachmentService 첨부파일 서비스
     */
    @Autowired
    public BoardController(BoardService boardService, CategoryService categoryService, CommentService commentService, AttachmentService attachmentService) {
        this.boardService = boardService;
        this.categoryService = categoryService;
        this.commentService = commentService;
        this.attachmentService = attachmentService;
    }


    /**
     * 게시글 목록을 조회하는 요청을 처리하는 메소드입니다.
     *
     * @param searchConditionParams 검색 조건 정보를 담고 있는 SearchConditionVO 객체
     * @return 게시글 목록과 검색 조건을 담고 있는 ResponseEntity 객체
     * @throws SQLException SQL 예외 발생 시
     */

    @GetMapping("/board/list")
    public ResponseEntity<APIResponse> getBoardList(
            @ModelAttribute SearchConditionVO searchConditionParams) throws SQLException {

        if (searchConditionParams == null) {
            return BoardUtils.createBadRequestResponse("유효한 검색조건이 아닙니다.");
        }

        try {
            List<BoardVO> searchBoards = boardService.searchBoards(searchConditionParams);
            List<CategoryVO> categories = categoryService.getAllCategory();
            int totalCount = boardService.countSearchBoards(searchConditionParams);

            BoardSearchResponse boardSearchResponse = new BoardSearchResponse();
            boardSearchResponse.setSearchCondition(searchConditionParams);
            boardSearchResponse.setSearchBoards(searchBoards);
            boardSearchResponse.setCategories(categories);
            boardSearchResponse.setTotalCount(totalCount);

            return BoardUtils.createOkResponse(boardSearchResponse);


        } catch (SQLException e) {
            return BoardUtils.createInternalServerErrorResponse(SQL_ERROR_MESSAGE);
        }
    }




    /**
     * 게시글 상세 정보를 조회하는 요청을 처리하는 메소드입니다.
     *
     * @param searchConditionParams 검색 조건 정보를 담고 있는 SearchConditionVO 객체
     * @param boardId               조회할 게시글의 ID
     * @return 게시글 상세 정보와 검색 조건을 담고 있는 ResponseEntity 객체
     */
    @GetMapping("/board/view")
    public ResponseEntity<APIResponse> getBoardInfo(@ModelAttribute SearchConditionVO searchConditionParams,
                                          @RequestParam(value = "boardId", required = true) Integer boardId) throws SQLException {

        if (searchConditionParams == null) {
            return BoardUtils.createBadRequestResponse("유효한 검색조건이 아닙니다.");
        }

        try {
            BoardVO board = boardService.getBoardInfoByBoardId(boardId);
            List<CommentVO> comments = commentService.getCommentsByBoardId(boardId);
            List<AttachmentVO> attachments = attachmentService.getAttachmentsByBoardId(boardId);

            BoardInfoResponse boardInfoResponse = new BoardInfoResponse();
            boardInfoResponse.setBoard(board);
            boardInfoResponse.setComments(comments);
            boardInfoResponse.setAttachments(attachments);

            return BoardUtils.createOkResponse(boardInfoResponse);

        } catch (SQLException e) {
            return BoardUtils.createInternalServerErrorResponse(SQL_ERROR_MESSAGE);
        }

    }

    /**
     * 게시글을 저장하는 요청을 처리합니다.
     *
     * @param board 게시글 정보를 담고 있는 BoardVO 객체
     * @param files 업로드된 첨부 파일 목록을 담고 있는 List<MultipartFile> 객체
     * @return ResponseEntity 객체
     * @throws Exception 예외 발생 시
     */
    @PostMapping("/board/save")
    public ResponseEntity<APIResponse> saveBoardInfo(@ModelAttribute BoardVO board,
                                                             @RequestParam(value="files", required = false) List<MultipartFile> files) throws Exception {
        if (board == null) {
            return BoardUtils.createBadRequestResponse("게시글 정보가 필요합니다.");
        }

        try {
            boardService.saveBoard(board, files);
            return BoardUtils.createOkResponse("게시글 저장 성공");

        } catch (SQLException e) {
            return BoardUtils.createInternalServerErrorResponse(SQL_ERROR_MESSAGE);
        }
    }

    /**
     * 댓글을 추가하는 요청을 처리합니다.
     *
     * @param searchConditionParams 검색 조건 정보를 담고 있는 SearchConditionVO 객체
     * @param requestBody           요청 본문에 포함된 데이터를 담고 있는 Map 객체
     * @return 게시글 상세 정보와 검색 조건을 담고 있는 ResponseEntity 객체
     * @throws SQLException SQL 예외 발생 시
     */
    @PostMapping("/board/add-comment")
    public ResponseEntity<APIResponse> addComment(
            @RequestBody Map<String, Object> requestBody) throws SQLException {

        int boardId = (Integer) requestBody.get("boardId");
        String content = (String) requestBody.get("content");

        Map<String, Object> params = new HashMap<>();
        params.put("boardId", boardId);
        params.put("content", content);

        try {
            commentService.saveComment(params);

            BoardVO board = boardService.getBoardInfoByBoardId(boardId);
            List<CommentVO> comments = commentService.getCommentsByBoardId(boardId);
            List<AttachmentVO> attachments = attachmentService.getAttachmentsByBoardId(boardId);

            BoardInfoResponse boardInfoResponse = new BoardInfoResponse();
            boardInfoResponse.setBoard(board);
            boardInfoResponse.setComments(comments);
            boardInfoResponse.setAttachments(attachments);

            return BoardUtils.createOkResponse(boardInfoResponse);

        } catch (SQLException e) {
            return BoardUtils.createInternalServerErrorResponse(SQL_ERROR_MESSAGE);
        }

    }

    /**
     * 게시글 삭제 요청을 처리합니다.
     *
     * @param boardId  삭제할 게시글의 ID를 나타내는 Integer 값
     * @param password 게시글을 삭제하기 위한 비밀번호를 나타내는 String 값
     * @return ResponseEntity 객체
     */
    @PostMapping("/board/delete")
    public ResponseEntity<APIResponse> deleteBoard(
            @RequestBody Map<String, Object> requestBody) {
        int boardId = (Integer) requestBody.get("boardId");
        String password = (String) requestBody.get("password");

        try {
            boardService.deleteBoard(boardId, password);
            return BoardUtils.createOkResponse("삭제에 성공하였습니다.");
        } catch (SQLException e) {
            return BoardUtils.createInternalServerErrorResponse(SQL_ERROR_MESSAGE);
        } catch (PasswordInvalidException e) {
            return BoardUtils.createBadRequestResponse(e.getMessage());
        }
    }

    /**
     * 게시글을 수정하는 요청을 처리하는 메소드입니다.
     *
     * @param newBoard             수정할 게시글 정보를 담고 있는 BoardVO 객체
     * @param files                업로드된 첨부 파일 목록을 담고 있는 List<MultipartFile> 객체
     * @param deletedAttachmentIds 삭제할 첨부 파일의 ID 목록을 담고 있는 List<Integer> 객체
     * @return 응답 상태와 메시지를 담고 있는 ResponseEntity 객체
     * @throws Exception 게시글 수정 과정에서 발생하는 예외
     */
    @PostMapping(value = "/board/update")
    public ResponseEntity<APIResponse> updateBoard(
//    @RequestParam(value = "files", required = false ) List<MultipartFile> files,
//   @RequestPart(value = "deletedAttachmentIds", required = false) List<Integer> deletedAttachmentIds,
    @ModelAttribute BoardInfoVO newBoard
    ) throws Exception {
        if (newBoard == null) {
            return BoardUtils.createBadRequestResponse("저장하려는 정보가 없습니다");
        }

        try {
            boardService.updateBoard(newBoard);
            return BoardUtils.createOkResponse("수정에 성공하였습니다.");

        } catch (SQLException e) {
            return BoardUtils.createInternalServerErrorResponse(SQL_ERROR_MESSAGE);
        } catch (PasswordInvalidException e) {
            return BoardUtils.createBadRequestResponse(e.getMessage());
        }
    }

    @GetMapping("/attachment/download")
    public ResponseEntity<Resource> attachmentDownload(@RequestParam(value = "attachmentId", required = true ) Integer attachmentId)
     throws Exception{

        try {
            AttachmentVO attachment = attachmentService.getAttachmentByAttachmentId(attachmentId);
            if (attachment == null) {
                return ResponseEntity.notFound().build();
            }
            return BoardUtils.fileDownload(attachment, UPLOAD_PATH);

        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/category/list")
    public ResponseEntity<APIResponse> getCategoryList(){
        try{
            List<CategoryVO> categories = categoryService.getAllCategory();
            return BoardUtils.createOkResponse(categories);

        }catch(SQLException e){
            return BoardUtils.createInternalServerErrorResponse(SQL_ERROR_MESSAGE);
        }

    }
}
