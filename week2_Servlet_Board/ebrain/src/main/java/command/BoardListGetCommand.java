package command;

import board.Board;
import board.BoardDAO;
import category.Category;
import category.CategoryDAO;
import utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardListGetCommand implements Command {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try {
            String currentPageParam = request.getParameter("page");
            String categoryParam = request.getParameter("category");
            String searchText = request.getParameter("searchText");
            String endDateString = request.getParameter("endDate");
            String startDateString = request.getParameter("startDate");
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusYears(1);

            int currentPage = 1;
            int pageSize = 10;
            int category = 0;

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

            if (!StringUtils.isNullOrEmpty(startDateString)) {
                startDate = LocalDate.parse(startDateString);
            }
            if (!StringUtils.isNullOrEmpty(endDateString)) {
                endDate = LocalDate.parse(endDateString);
            }

            BoardDAO boardDAO = new BoardDAO();
            Map<String, Object> resultMap = boardDAO.searchBoards(startDate, endDate, category, searchText, currentPage, pageSize);

            CategoryDAO categoryDAO = new CategoryDAO();
            List<Category> resultList = categoryDAO.getAllCategory();

            List<Board> boards = (List<Board>) resultMap.get("boards");
            int totalCount = (int) resultMap.get("totalCount");
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("boards", boards);
            attributes.put("categories", resultList);
            attributes.put("currentPage", currentPage);
            attributes.put("category", category);
            attributes.put("searchText", searchText);
            attributes.put("startDate", startDate.toString());
            attributes.put("endDate", endDate.toString());
            attributes.put("totalCount", totalCount);
            attributes.put("totalPages", totalPages);
            request.setAttribute("attributes", attributes);
            request.getRequestDispatcher("boardGetList.jsp").forward(request, response);


        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "게시글 목록 조회에 실패하였습니다.");
            request.getRequestDispatcher("errorPage.jsp").forward(request, response);
        }
    }
}