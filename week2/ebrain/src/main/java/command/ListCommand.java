package command;

import board.Board;
import board.BoardDAO;
import utils.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ListCommand implements Command {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String categoryParam = request.getParameter("category");
        String search = request.getParameter("search");
        String endDateString = request.getParameter("endDate");
        String startDateString = request.getParameter("startDate");

        int currentPage = 1;
        int pageSize = 10;
        int category = 0;

        if (!StringUtils.isNullOrEmpty(categoryParam)) {
            category = Integer.parseInt(categoryParam);
        }

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(1);
        if (startDateString != null && !startDateString.isEmpty()) {
            startDate = LocalDate.parse(startDateString);
        }
        if (endDateString != null && !endDateString.isEmpty()) {
            endDate = LocalDate.parse(endDateString);
        }
        if (request.getParameter("page") != null) {
            currentPage = Integer.parseInt(request.getParameter("page"));
        }

        BoardDAO boardDAO = new BoardDAO();
        List<Board> boards = boardDAO.searchBoards(startDate, endDate, category, search, currentPage, pageSize);

        int totalCount = boardDAO.countBoards(startDate, endDate, category, search);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 필요한 데이터를 request 속성에 저장
        request.setAttribute("boards", boards);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("category", category);
        request.setAttribute("search", search);
        request.setAttribute("startDate", startDate.toString());
        request.setAttribute("endDate", endDate.toString());

        request.setAttribute("totalCount", totalCount);
        request.setAttribute("totalPages", totalPages);

        // JSP로 포워딩
        RequestDispatcher dispatcher = request.getRequestDispatcher("list.jsp");

        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();

            request.setAttribute("errorMessage", "게시글 목록 조회에 실패하였습니다.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}