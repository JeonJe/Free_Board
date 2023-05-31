package command;

import board.BoardDAO;
import utils.BoardUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BoardDeleteCommand implements Command {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            request.setCharacterEncoding("UTF-8");
            String id = request.getParameter("id");
            String password = request.getParameter("password");
            String hashedPassword = BoardUtils.hashPassword(password);

            if (id != null && password != null) {
                int parseIntId = Integer.parseInt(id);

                BoardDAO boardDAO = new BoardDAO();
                boolean isValid = boardDAO.validatePassword(parseIntId, hashedPassword);

                if (isValid) {
                    boardDAO.deleteBoard(parseIntId);
                    response.getWriter().write("삭제가 완료 되었습니다");
                } else {
                    response.getWriter().write("비밀번호가 틀립니다");
                }
            } else {
                response.getWriter().write("게시글 ID와 비밀번호를 입력해주세요.");
            }

        } catch (NumberFormatException e) {
            response.getWriter().write("게시글 ID가 올바르지 않습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "게시글 삭제에 실패하였습니다.");
        }
    }
}