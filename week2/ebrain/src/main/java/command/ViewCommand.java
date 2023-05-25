package command;

import attachment.Attachment;
import attachment.AttachmentDAO;
import board.Board;
import board.BoardDAO;
import comment.Comment;
import comment.CommentDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ViewCommand implements Command {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            BoardDAO boardDAO = new BoardDAO();
            Board board = boardDAO.getBoardById(id);
            boardDAO.updateVisitCount(board.getBoardId(), board.getVisitCount());
            int updatedVisitCount = boardDAO.getBoardById(id).getVisitCount();

            CommentDAO commentDAO = new CommentDAO();
            List<Comment> comments = commentDAO.getCommentsByBoardId(board.getBoardId());

            AttachmentDAO attachmentDAO = new AttachmentDAO();
            List<Attachment> attachments = attachmentDAO.getAttachmentsByBoardId(board.getBoardId());

            request.setAttribute("board", board);
            request.setAttribute("updatedVisitCount", updatedVisitCount);
            request.setAttribute("comments", comments);
            request.setAttribute("attachments", attachments);

            request.getRequestDispatcher("view.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
