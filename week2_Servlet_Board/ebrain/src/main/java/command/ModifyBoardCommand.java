package command;

import attachment.Attachment;
import attachment.AttachmentDAO;
import board.Board;
import board.BoardDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ModifyBoardCommand implements Command {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            BoardDAO boardDAO = new BoardDAO();
            Board board = boardDAO.getBoardInfoById(id);

            AttachmentDAO attachmentDAO = new AttachmentDAO();
            List<Attachment> attachments = attachmentDAO.getAttachmentsByBoardId(board.getBoardId());

            request.setAttribute("board", board);
            request.setAttribute("attachments", attachments);

            request.getRequestDispatcher("ModifyBoardContent.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Failed to retrieve board data for modification.");
            request.getRequestDispatcher("ShowError.jsp").forward(request, response);
        }
    }
}