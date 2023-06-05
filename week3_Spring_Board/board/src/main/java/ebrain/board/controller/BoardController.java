package ebrain.board.controller;

import ebrain.board.service.AttachmentService;
import ebrain.board.service.CategoryService;
import ebrain.board.service.CommentService;
import ebrain.board.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ebrain.board.service.BoardService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BoardController class
 * 게시판과 관련된 모든 요청을 처리하고 json 데이터 반환
 */
@RestController
public class BoardController {
    private static final Logger log = LoggerFactory.getLogger(BoardController.class);
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
     * 게시글 검색 조건을 Map에 설정하는 함수
     * @param searchConditionParams 검색어 조건 (현재페이지, 시작일, 종료일, 검색어, 카테고리)
     * @return 검색조건을 설정한 Map 반환
     */
    private Map<String, Object> setSearchCondition(SearchConditionVO searchConditionParams) {

        int pageSize = 10;
        int currentPage = searchConditionParams.getPage() != null ? searchConditionParams.getPage() : 1;
        int category = searchConditionParams.getCategory() != null ? searchConditionParams.getCategory() : 0;
        LocalDate startDate = searchConditionParams.getStartDate() != null ? searchConditionParams.getStartDate() : LocalDate.now().minusYears(1);
        LocalDate endDate = searchConditionParams.getEndDate() != null ? searchConditionParams.getEndDate() : LocalDate.now();
        String searchText = searchConditionParams.getSearchText() != null ? searchConditionParams.getSearchText() : "";

        Map<String, Object> searchCondition = new HashMap<>();
        searchCondition.put("startDate", startDate);
        searchCondition.put("endDate", endDate);
        searchCondition.put("category", category);
        searchCondition.put("searchText", searchText);
        searchCondition.put("currentPage", currentPage);
        searchCondition.put("pageSize", pageSize);
        searchCondition.put("offset", (currentPage - 1) * pageSize);

        return searchCondition;
    }
    /**
     * 게시글 목록을 조회하는 함수
     *
     * @param searchConditionParams 검색 조건
     * @return 게시글 목록 및 관련 정보를 담은 ResponseEntity
     */
    @GetMapping("/list")
    public ResponseEntity<?> getBoardList(
            @ModelAttribute SearchConditionVO searchConditionParams) {

        if (searchConditionParams == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 검색 조건입니다.");
        }

        Map<String, Object> searchCondition = setSearchCondition(searchConditionParams);
        Map<String, Object> searchBoardsResult = boardService.searchBoards(searchCondition);
        if (searchBoardsResult == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("검색에 실패하였습니다.");
        }

        List<CategoryVO> categories = categoryService.getAllCategory();
        List<BoardVO> boards = (List<BoardVO>) searchBoardsResult.get("boards");
        int totalCount = (int) searchBoardsResult.get("totalCount");
        int pageSize = (int) searchCondition.get("pageSize");
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        Map<String, Object> response = new HashMap<>();
        response.put("searchCondition", searchCondition);
        response.put("searchBoards", boards);
        response.put("categories", categories);
        response.put("totalCount", totalCount);
        response.put("totalPages", totalPages);

        return ResponseEntity.ok(response);
    }

    /**
     * 게시글 작성 페이지로 이동하는 함수
     *
     * @param searchConditionParams 검색 조건
     * @return 게시글 작성 페이지 관련 정보를 담은 ResponseEntity
     */
    @GetMapping("/write")
    public ResponseEntity<?> clickBoardWriteButton(@RequestBody SearchConditionVO searchConditionParams
    ) {
        if (searchConditionParams == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 검색 조건입니다.");
        }

        List<CategoryVO> categories = categoryService.getAllCategory();
        Map<String, Object> searchCondition = setSearchCondition(searchConditionParams);

        Map<String, Object> response = new HashMap<>();
        response.put("categories", categories);
        response.put("searchCondition", searchCondition);
        return ResponseEntity.ok(response);
    }

    /**
     * 게시글 상세 정보를 조회하는 함수
     *
     * @param searchConditionParams 검색 조건
     * @param boardId              게시글 ID
     * @return 게시글 상세 정보 및 관련 정보를 담은 ResponseEntity
     */
    @GetMapping("/view")
    public ResponseEntity<?> getBoardInfo(@ModelAttribute SearchConditionVO searchConditionParams,
                                          @RequestParam(value = "boardId", required = true) Integer boardId) {

        if (searchConditionParams == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 검색 조건입니다.");
        }

        BoardVO board = boardService.getBoardInfoByBoardId(boardId);
        List<CommentVO> comments = commentService.getCommentsByBoardId(boardId);
        List<AttachmentVO> attachments = attachmentService.getAttachmentsByBoardId(boardId);
        Map<String, Object> searchCondition = setSearchCondition(searchConditionParams);

        Map<String, Object> response = new HashMap<>();
        response.put("board", board);
        response.put("attachments", attachments);
        response.put("comments", comments);
        response.put("searchCondition", searchCondition);

        return ResponseEntity.ok(response);
    }

//    @PostMapping("/save")
//    public String saveBoardInfo(@RequestParam("category_id") String categoryValue,
//                                @RequestParam("writer") String writer,
//                                @RequestParam("password") String password,
//                                @RequestParam("confirmPassword") String confirmPassword,
//                                @RequestParam("title") String title,
//                                @RequestParam("content") String content,
//                                @RequestParam("file1") MultipartFile file1,
//                                @RequestParam("file2") MultipartFile file2,
//                                @RequestParam("file3") MultipartFile file3
//    ) throws Exception {
//        //VO를 만들어서 requestparam전달하기
//        //body에서 스프링이 알아서 변환 , 파라미터 핸들링 기법 확인하기
//        MultipartFile[] files = {file1, file2, file3};
//        boardService.saveBoard(categoryValue, writer, password, confirmPassword, title, content, files);
//
//        return "boardGetList";
//    }
//

    /**
     * 댓글을 추가하는 함수
     *
     * @param searchConditionParams  검색 조건
     * @param requestBody           요청 바디 데이터
     * @return 댓글 추가 후 게시글 상세 정보 및 관련 정보를 담은 ResponseEntity
     */
    @PostMapping("/add-comment")
    public ResponseEntity<?> addComment(
            @ModelAttribute SearchConditionVO searchConditionParams,
            @RequestBody Map<String, Object> requestBody) {

        int boardId = (Integer) requestBody.get("boardId");
        String content = (String) requestBody.get("content");

        Map<String, Object> params = new HashMap<>();
        params.put("boardId", boardId);
        params.put("content", content);

        commentService.saveComment(params);

        BoardVO board = boardService.getBoardInfoByBoardId(boardId);
        List<CommentVO> comments = commentService.getCommentsByBoardId(boardId);
        List<AttachmentVO> attachments = attachmentService.getAttachmentsByBoardId(boardId);
        Map<String, Object> searchCondition = setSearchCondition(searchConditionParams);

        Map<String, Object> response = new HashMap<>();
        response.put("board", board);
        response.put("attachments", attachments);
        response.put("comments", comments);
        response.put("searchCondition", searchCondition);

        return ResponseEntity.ok(response);
    }


}
