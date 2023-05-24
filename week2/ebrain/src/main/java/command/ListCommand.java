package command;//package command;


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
        int category = 0;
        String categoryParam = request.getParameter("category");
        if (!StringUtils.isNullOrEmpty(categoryParam)) {
            category = Integer.parseInt(categoryParam);
        }

        String search = request.getParameter("search");

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(1);

        String endDateString = request.getParameter("endDate");
        String startDateString = request.getParameter("startDate");

        if (startDateString != null && !startDateString.isEmpty()) {
            startDate = LocalDate.parse(startDateString);
        }

        if (endDateString != null && !endDateString.isEmpty()) {
            endDate = LocalDate.parse(endDateString);
        }

        BoardDAO boardDAO = new BoardDAO();

        int currentPage = 1;
        int pageSize = 10;
        if (request.getParameter("page") != null) {
            currentPage = Integer.parseInt(request.getParameter("page"));
        }

        List<Board> boards = boardDAO.searchBoards(startDate, endDate, category, search, currentPage, pageSize);

        int totalCount = boardDAO.countBoards(startDate, endDate, category, search);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 필요한 데이터를 request 속성에 저장
        request.setAttribute("boards", boards);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("totalPages", totalPages);

        // JSP로 포워딩
        RequestDispatcher dispatcher = request.getRequestDispatcher("list.jsp");


        try {
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            // 예외 처리
            // 필요한 경우 에러 메시지 등을 설정하여 에러 페이지로 포워드
            request.setAttribute("errorMessage", "게시글 목록 조회에 실패하였습니다.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}