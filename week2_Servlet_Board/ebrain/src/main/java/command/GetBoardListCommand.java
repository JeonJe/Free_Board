package command;

import board.Board;
import board.BoardDAO;
import utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

public class GetBoardListCommand implements Command {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String currentPageParam = request.getParameter("page");
            String categoryParam = request.getParameter("category");
            String search = request.getParameter("search");
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
            if (!StringUtils.isNullOrEmpty(startDateString)) {
                startDate = LocalDate.parse(startDateString);
            }
            if (!StringUtils.isNullOrEmpty(endDateString)) {
                endDate = LocalDate.parse(endDateString);
            }
            if (!StringUtils.isNullOrEmpty(currentPageParam)) {
                currentPage = Integer.parseInt(currentPageParam);
            }

            BoardDAO boardDAO = new BoardDAO();
            List<Board> boards = boardDAO.searchBoards(startDate, endDate, category, search, currentPage, pageSize);

            int totalCount = boardDAO.countBoards(startDate, endDate, category, search);
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);

            // 필요한 데이터를 request 속성에 저장
            //TODO : VO로 하나로 넘기기  + 클래스명 변경 동사 X
            //jsp 소문자, 명사 시작
            request.setAttribute("boards", boards);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("category", category);
            request.setAttribute("search", search);
            request.setAttribute("startDate", startDate.toString());
            request.setAttribute("endDate", endDate.toString());
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("totalPages", totalPages);



            request.getRequestDispatcher("GetListBoard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "게시글 목록 조회에 실패하였습니다.");
            request.getRequestDispatcher("ShowError.jsp").forward(request, response);
        }
    }
}