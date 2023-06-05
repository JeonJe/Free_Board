package ebrain.board.controller;

import ebrain.board.service.CategoryService;
import ebrain.board.service.CommentService;
import ebrain.board.vo.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import ebrain.board.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BoardController class
 * <p>
 * 게시판과 관련된 모든 요청을 처리하는 역할
 * 게시글 생성, 게시글 등록, 게시글 수정, 게시글 삭제, 게시글 보기 등 수행
 * 수행 내용에 따라 각 서비스를 사용하여 뷰를 반환
 */
@Controller
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

    /**
     * 생성자 주입
     *
     * @param boardService
     * @param categoryService
     * @param commentService
     */
    @Autowired
    public BoardController(BoardService boardService, CategoryService categoryService, CommentService commentService) {
        this.boardService = boardService;
        this.categoryService = categoryService;
        this.commentService = commentService;
    }


    @GetMapping("/list")
    public String getBoardList(
            @ModelAttribute SearchConditionVO searchConditionParams,
            Model model) {

        Map<String, Object> searchCondition = setSearchCondition(searchConditionParams);
        Map<String, Object> searchBoardsResult = boardService.searchBoards(searchCondition);
        List<CategoryVO> categories = categoryService.getAllCategory();
        List<BoardVO> boards = (List<BoardVO>) searchBoardsResult.get("boards");
        int pageSize = (int) searchCondition.get("pageSize");
        int totalCount = (int) searchBoardsResult.get("totalCount");
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        model.addAttribute("searchCondition", searchCondition);
        model.addAttribute("searchBoards", boards);
        model.addAttribute("categories", categories);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPages", totalPages);

        return "boardGetList";
    }

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
     * 게시글 등록 버튼을 눌렀을 때 카테고리 목록을 가져와서 뷰를 보여주는 함수
     *
     * @param model 데이터베이스의 카테고리 목록을 가지고 있는 모델
     * @return 게시글 작성 페이지 boardWriteInfo.jsp 반환
     */

    @GetMapping("/write")
    public String clickBoardWriteButton(@ModelAttribute SearchConditionVO searchConditionParams,
                                        Model model) {

        List<CategoryVO> categories = categoryService.getAllCategory();
        Map<String, Object> searchCondition = setSearchCondition(searchConditionParams);

        model.addAttribute("categories", categories);
        model.addAttribute("searchCondition", searchCondition);
        System.out.println("write button 클릭 시 searchCondition = " + searchCondition);
        return "boardWriteInfo";
    }

    /**
     * 게시글 상세 내용을 가져오는 함수
     *
     * @param request 게시글 ID를 포함하는 request
     * @param model   게시글 ID의 정보를 담고 있는 모델
     * @return 게시글 상세 내용을 보여주는 페이지 boardGetInfo.jsp 반환
     */

    @GetMapping("/view")
    public String getBoardInfo(@ModelAttribute SearchConditionVO searchConditionParams,
                               @RequestParam("id") Integer boardId
            , Model model) {

        Map<String, Object> resultMap = boardService.getBoardInfoByBoardId(boardId);
        Map<String, Object> searchCondition = setSearchCondition(searchConditionParams);

        model.addAttribute("board", resultMap.get("board"));
        model.addAttribute("attachments", resultMap.get("attachments"));
        model.addAttribute("comments", resultMap.get("comments"));
        model.addAttribute("searchCondition", searchCondition);

        return "boardGetInfo";
    }

    /**
     * 게시글 내용을 저장하는 함수
     *
     * @param categoryValue   카테고리번호
     * @param writer          게시글 작성자
     * @param password        게시글 비밀번호
     * @param confirmPassword 게시글 비밀번호 확인
     * @param title           제목
     * @param content         내용
     * @param files           1,2,3 첨부파일
     * @return 파일 저장 후 게시글 목록을 보여주는 boardGetList.jsp 반환
     * @throws Exception
     */
    @PostMapping("/save")
    //TODO : category_id 잘못, value
    public String saveBoardInfo(@RequestParam("category_id") String categoryValue,
                                @RequestParam("writer") String writer,
                                @RequestParam("password") String password,
                                @RequestParam("confirmPassword") String confirmPassword,
                                @RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam("file1") MultipartFile file1,
                                @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3
    ) throws Exception {
        //TODO : Q. 스프링에서는 HttpServletRequest를 전달하지않고 @RequestParam을 전달? 다수의 인자를 전달받을 때도 적용?
        //TODO : attachment Service를 Controller에서 사용하는지 boardService의 saveBoard 안에서 사용하는지? -> 현재는 saveBoard안에서 처리
        //VO를 만들어서 requestparam전달하기
        //body에서 스프링이 알아서 변환 , 파라미터 핸들링 기법 확인하기
        MultipartFile[] files = {file1, file2, file3};
        boardService.saveBoard(categoryValue, writer, password, confirmPassword, title, content, files);

        return "boardGetList";
    }

    /**
     * 게시판에 댓글을 추가하는 함수
     *
     * @param request 게시판 ID와 댓글 내용을 포함하는 request
     * @param model   게시글 내용을 보여주기 위해 게시글 내용을 담고 있는 모델
     * @return 게시글 상세 정보를 나타내는 페이지 boardGetInfo.jsp 반환
     */
    @PostMapping("/add-comment")
    public String addComment(HttpServletRequest request, Model model) {

        int boardId = Integer.parseInt(request.getParameter("id"));
        String commentContent = request.getParameter("content");

        Map<String, Object> params = new HashMap<>();
        params.put("boardId", boardId);
        params.put("content", commentContent);

        commentService.saveComment(params);

        Map<String, Object> resultMap = boardService.getBoardInfoByBoardId(boardId);
        model.addAttribute("board", resultMap.get("board"));
        model.addAttribute("attachments", resultMap.get("attachments"));
        model.addAttribute("comments", resultMap.get("comments"));

        return "boardGetInfo";
    }


}
