package command;


import comment.Comment;
import comment.CommentDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommentAddCommand implements Command {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            request.setCharacterEncoding("UTF-8");
            String content = request.getParameter("content");

            int id = Integer.parseInt(request.getParameter("id"));

            String currentPageParam = request.getParameter("page");
            String categoryParam = request.getParameter("category");
            String searchText = request.getParameter("searchText");
            String endDateString = request.getParameter("endDate");
            String startDateString = request.getParameter("startDate");

            Comment comment = new Comment();
            comment.setContent(content);
            comment.setBoardId(id);
            comment.setWriter("-");

            CommentDAO commentDAO = new CommentDAO();
            commentDAO.saveComment(comment);

            String url = "/view?action=view&id=" + id + "&page=" + currentPageParam+
                    "&category=" + categoryParam + "&searchText=" + searchText +
                    "&startDate=" + startDateString + "&endDate=" + endDateString;
            response.sendRedirect(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

