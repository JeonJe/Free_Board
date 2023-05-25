package command;


import attachment.Attachment;
import attachment.AttachmentDAO;
import board.Board;
import board.BoardDAO;
import utils.BoardUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class UpdateCommand implements Command {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            request.setCharacterEncoding("UTF-8");
            int id = Integer.parseInt(request.getParameter("id"));
            String writer = request.getParameter("writer");
            String password = request.getParameter("password");
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String hashedPassword = BoardUtils.hashPassword(password);

            BoardDAO boardDAO = new BoardDAO();
            Board board = boardDAO.getBoardById(id);
            //TODO : 첨부파일 업데이트
            if (board != null && board.getPassword().equals(hashedPassword)) {
                board.setBoardId(id);
                board.setPassword(hashedPassword);
                board.setWriter(writer);
                board.setTitle(title);
                board.setContent(content);
                boardDAO.update(board);
                response.sendRedirect("list");
            }else{
                String errorMessage = "비밀번호가 틀렸습니다.";
                BoardUtils.setErrorAlert(request, errorMessage);
                AttachmentDAO attachmentDAO = new AttachmentDAO();
                List<Attachment> attachments = attachmentDAO.getAttachmentsByBoardId(board.getBoardId());

                request.setAttribute("id", id);
                request.setAttribute("board", board);
                request.setAttribute("attachments", attachments);

                request.getRequestDispatcher("modify.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}