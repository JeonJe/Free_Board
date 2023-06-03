package ebrain.board.controller;

import ebrain.board.service.CategoryService;
import ebrain.board.service.CommentService;
import ebrain.board.utils.StringUtils;
import ebrain.board.vo.AttachmentVO;
import ebrain.board.vo.CategoryVO;
import ebrain.board.vo.CommentVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import ebrain.board.service.BoardService;
import ebrain.board.vo.BoardVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BoardController class
 *
 * 게시판과 관련된 모든 요청을 처리하는 역할
 * 게시글 생성, 게시글 등록, 게시글 수정, 게시글 삭제, 게시글 보기 등 수행
 * 수행 내용에 따라 각 서비스를 사용하여 뷰를 반환
 */
@Controller
public class BoardController {
    /**
     * 게시글 관련 비지니스 로직 수행
     */
    @Autowired
    private BoardService boardService;
    /**
     * 게시판 카테고리 관련 비지니스 로직 수행
     */
    @Autowired
    private CategoryService categoryService;
    /**
     * 게시판 댓글 관련 비지니스 로직 수행
     */
    @Autowired
    private CommentService commentService;

    /**
     * 검색조건에 따라 게시글 목록을 반환하는 함수
     * @param request 검색 조건이 포함된 request
     * @param model 검색 조건에 해당하는 목록들과 검색 조건을 담고 있는 모델
     * @return 게시글 목록을 나타내는 boardGetList.jsp 반환
     */
    @GetMapping("/list")
    public String BoardGetList(HttpServletRequest request, Model model) {

        int currentPage = 1;
        int pageSize = 10;
        int category = 0;

        String currentPageParam = request.getParameter("page");
        String categoryParam = request.getParameter("category");
        String searchText = request.getParameter("searchText");
        String endDateParam = request.getParameter("endDate");
        String startDateParam = request.getParameter("startDate");
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(1);

        //TODO : Q. 파라미터 유효성 확인 위치 -> Controller vs Service?
        if (!StringUtils.isNullOrEmpty(categoryParam)) {
            category = Integer.parseInt(categoryParam);
        }

        if (!StringUtils.isNullOrEmpty(currentPageParam)) {
            currentPage = Integer.parseInt(currentPageParam);
        }

        if (!StringUtils.isNullOrEmpty(searchText)) {
            searchText = searchText;
        } else {
            searchText = "";
        }

        if (!StringUtils.isNullOrEmpty(startDateParam)) {
            startDate = LocalDate.parse(startDateParam);
        }

        if (!StringUtils.isNullOrEmpty(endDateParam)) {
            endDate = LocalDate.parse(endDateParam);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("category", category);
        params.put("searchText", searchText);
        params.put("currentPage", currentPage);
        params.put("pageSize", pageSize);
        params.put("offset", (currentPage - 1) * pageSize);

        Map<String, Object> searchBoardsResult = boardService.searchBoards(params);
        List<CategoryVO> categories = categoryService.getAllCategory();
        List<BoardVO> boards = (List<BoardVO>) searchBoardsResult.get("boards");
        int totalCount = (int) searchBoardsResult.get("totalCount");
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        model.addAttribute("searchBoards", boards);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("categories", categories);
        model.addAttribute("params", params);
        return "boardGetList";
    }

    /**
     * 게시글 등록 버튼을 눌렀을 때 카테고리 목록을 가져와서 뷰를 보여주는 함수
     * @param request
     * @param model 데이터베이스의 카테고리 목록을 가지고 있는 모델
     * @return 게시글 작성 페이지 boardWriteInfo.jsp 반환
     */
    @GetMapping("/write")
    public String BoardWriteButtonClick(HttpServletRequest request, Model model) {
        List<CategoryVO> categories = categoryService.getAllCategory();

        model.addAttribute("categories", categories);

        return "boardWriteInfo";
    }

    /**
     * 게시글 상세 내용을 가져오는 함수
     * @param request 게시글 ID를 포함하는 request
     * @param model  게시글 ID의 정보를 담고 있는 모델
     * @return  게시글 상세 내용을 보여주는 페이지 boardGetInfo.jsp 반환
     */

    @GetMapping("/view")
    public String BoardGetInfo(HttpServletRequest request, Model model) {
        int boardId = Integer.parseInt(request.getParameter("id"));

        Map<String, Object> resultMap = boardService.getBoardInfoByBoardId(boardId);

        model.addAttribute("board", (BoardVO) resultMap.get("board"));
        model.addAttribute("attachments", (List<AttachmentVO>) resultMap.get("attachments"));
        model.addAttribute("comments", (List<CommentVO>) resultMap.get("comments"));

        return "boardGetInfo";
    }

    /**
     * 게시글 내용을 저장하는 함수
     * @param categoryValue 카테고리번호
     * @param writer 게시글 작성자
     * @param password 게시글 비밀번호
     * @param confirmPassword 게시글 비밀번호 확인
     * @param title 제목
     * @param content 내용
     * @param files 첨부파일
     * @return 파일 저장 후 게시글 목록을 보여주는 boardGetList.jsp 반환
     * @throws Exception
     */
    @PostMapping("/save")
    public String BoardSave(@RequestParam("category_id") String categoryValue,
                            @RequestParam("writer") String writer,
                            @RequestParam("password") String password,
                            @RequestParam("confirmPassword") String confirmPassword,
                            @RequestParam("title") String title,
                            @RequestParam("content") String content,
                            @RequestParam("file1") MultipartFile file1,
                            @RequestParam("file2") MultipartFile file2,
                            @RequestParam("file3") MultipartFile file3
                            ) throws Exception {
        //TODO : Q. 스프링에서는 HttpServletRequest를 전달하지않꼬 @RequestParam을 전달? 다수의 인자가 있을때도 적용?
        //TODO : attachment Service를 Controller에서 사용하는지 boardService의 saveBoard 안에서 사용하는지?
        MultipartFile[] files = {file1, file2, file3};
        boardService.saveBoard(categoryValue, writer, password, confirmPassword, title, content, files);

        return "boardGetList";
    }

    /**
     * 게시판에 댓글을 추가하는 함수
     * @param request 게시판 ID와 댓글 내용을 포함하는 request
     * @param model 게시글 내용을 보여주기 위해 게시글 내용을 담고 있는 모델
     * @return 게시글 상세 정보를 나타내는 페이지 boardGetInfo.jsp 반환
     */
    @PostMapping("/add-comment")
    public String CommentAdd(HttpServletRequest request, Model model) {

        int boardId = Integer.parseInt(request.getParameter("id"));
        String commentContent = request.getParameter("content");

        Map<String, Object> params = new HashMap<>();
        params.put("boardId", boardId);
        params.put("content", commentContent);

        commentService.saveComment(params);
        Map<String, Object> resultMap = boardService.getBoardInfoByBoardId(boardId);
        model.addAttribute("board", (BoardVO) resultMap.get("board"));
        model.addAttribute("attachments", (List<AttachmentVO>) resultMap.get("attachments"));
        model.addAttribute("comments", (List<CommentVO>) resultMap.get("comments"));

        return "boardGetInfo";
    }


}
