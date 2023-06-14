package ebrain.board.controller;

import ebrain.board.reponse.APIResponse;
import ebrain.board.exception.PasswordInvalidException;
import ebrain.board.reponse.SearchResponse;
import ebrain.board.service.AttachmentService;
import ebrain.board.service.CategoryService;
import ebrain.board.service.CommentService;
import ebrain.board.vo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private static final String SQL_ERROR_MESSAGE = "SQL 오류";
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
        APIResponse apiResponse = new APIResponse();
        if (searchConditionParams == null) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("유효한 검색조건이 아닙니다.");

            return ResponseEntity.badRequest().body(apiResponse);
        }

        try {
            Integer pageOffSet = (searchConditionParams.getCurrentPage() - 1) * searchConditionParams.getPageSize();
            searchConditionParams.setOffset(pageOffSet);
            List<BoardVO> searchBoards = boardService.searchBoards(searchConditionParams);
            List<CategoryVO> categories = categoryService.getAllCategory();
            int totalCount = boardService.countSearchBoards(searchConditionParams);

            SearchResponse response = new SearchResponse();
            response.setSearchCondition(searchConditionParams);
            response.setSearchBoards(searchBoards);
            response.setCategories(categories);
            response.setTotalCount(totalCount);

            //TODO : total 페이지를 가능하면 화면쪽에서 현재페이지 번호와 몇개 가져오는지 화면에서 전달하면 서버는
            //짜르는거 자체를 프론트에서 pageSize자체를 받아오면 됨 , 오프셋도 화면에서 처리 가능
            apiResponse.setSuccess(true);
            apiResponse.setData(Collections.singletonList(response));
            return ResponseEntity.ok(apiResponse);

        } catch (SQLException e) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Internal server error");
            apiResponse.setData(Collections.singletonList(SQL_ERROR_MESSAGE));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
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
    public ResponseEntity<Map<String, Object>> getBoardInfo(@ModelAttribute SearchConditionVO searchConditionParams,
                                          @RequestParam(value = "boardId", required = true) Integer boardId) throws SQLException {

        if (searchConditionParams == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(Collections.singletonMap("error", "유효하지 않은 검색 조건입니다."));
        }

        try {
            BoardVO board = boardService.getBoardInfoByBoardId(boardId);
            List<CommentVO> comments = commentService.getCommentsByBoardId(boardId);
            List<AttachmentVO> attachments = attachmentService.getAttachmentsByBoardId(boardId);
//            Map<String, Object> searchCondition = setSearchCondition(searchConditionParams);

            Map<String, Object> response = new HashMap<>();
            response.put("board", board);
            response.put("attachments", attachments);
            response.put("comments", comments);
//            response.put("searchCondition", searchCondition);

            return ResponseEntity.ok(response);

        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(Collections.singletonMap("error", SQL_ERROR_MESSAGE));
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
    @PostMapping("/save")
    public ResponseEntity<String> saveBoardInfo(@ModelAttribute BoardVO board,
                                                             @RequestParam(value="files", required = false) List<MultipartFile> files) throws Exception {
        if (board == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("저장하려는 정보가 없습니다");
        }

        try {
            boardService.saveBoard(board, files);
            return ResponseEntity.ok("게시글 저장 성공");

        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(SQL_ERROR_MESSAGE);
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
    public ResponseEntity<Map<String, Object>> addComment(
            @ModelAttribute SearchConditionVO searchConditionParams,
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
//            Map<String, Object> searchCondition = setSearchCondition(searchConditionParams);

            Map<String, Object> response = new HashMap<>();
            response.put("board", board);
            response.put("attachments", attachments);
            response.put("comments", comments);
//            response.put("searchCondition", searchCondition);

            return ResponseEntity.ok(response);

        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(Collections.singletonMap("error", SQL_ERROR_MESSAGE));
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
    public ResponseEntity<String> deleteBoard(
            @RequestParam(value = "boardId", required = true) Integer boardId,
            @RequestParam(value = "password", required = true) String password) {
        try {
            boardService.deleteBoard(boardId, password);
            return ResponseEntity.ok("삭제에 성공하였습니다.");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(SQL_ERROR_MESSAGE);
        } catch (PasswordInvalidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
    @PostMapping("/board/update")
    public ResponseEntity<String> updateBoard(@ModelAttribute BoardVO newBoard,
                                              @RequestParam(value = "files", required = false ) List<MultipartFile> files,
                                              @RequestParam(value = "deletedAttachmentIds", required = false) List<Integer> deletedAttachmentIds
    ) throws Exception {
        if (newBoard == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("저장하려는 정보가 없습니다");
        }

        try {
            boardService.updateBoard(newBoard, files, deletedAttachmentIds);
            return ResponseEntity.ok("게시글 수정 성공");

        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(SQL_ERROR_MESSAGE);
        } catch (PasswordInvalidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
