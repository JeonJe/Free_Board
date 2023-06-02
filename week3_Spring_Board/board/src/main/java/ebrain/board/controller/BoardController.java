package ebrain.board.controller;

import ebrain.board.service.CategoryService;
import ebrain.board.utils.StringUtils;
import ebrain.board.vo.CategoryVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import ebrain.board.service.BoardService;
import ebrain.board.vo.BoardVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private CategoryService categoryService;

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

        //Q. 파라미터 유효성 확인 위치 -> Controller vs Service?
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

        List<BoardVO> searchBoards = boardService.searchBoards(params);
        List<CategoryVO> categories = categoryService.getAllCategory();

        model.addAttribute("searchBoards", searchBoards);
        model.addAttribute("categories", categories);
        model.addAttribute("params", params);
        return "boardGetList";
    }

}
