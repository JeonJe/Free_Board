<%--
  Created by IntelliJ IDEA.
  User: premise
  Date: 2023/05/18
  Time: 1:41 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="board.BoardDAO" %>
<%@ page import="utils.PasswordUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%

    String id = request.getParameter("id");
    String password = request.getParameter("password");
    String hashedPassword = PasswordUtils.hashPassword(password);

    if (id != null && password != null) {
        int parseIntId =  Integer.parseInt(id);
        try {
            BoardDAO boardDAO = new BoardDAO();
            boolean isValid = boardDAO.validatePassword(parseIntId, hashedPassword);

            if (isValid) {
                boardDAO.delete(parseIntId);
                response.getWriter().write("삭제가 완료 되었습니다");
            } else {
                response.getWriter().write("비밀번호가 틀립니다");
            }
        } catch (NumberFormatException e) {
            response.getWriter().write("유효한 게시판 번호가 아닙니다.");
        } catch (Exception e) {
            response.getWriter().write("Error: " + e.getMessage());
        }
    } else {
        response.getWriter().write("게시물 ID 또는 비밀번호가 올바르지 않습니다.");
    }
%>