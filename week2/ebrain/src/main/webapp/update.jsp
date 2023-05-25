<%--
  Created by IntelliJ IDEA.
  User: premise
  Date: 2023/05/18
  Time: 7:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="board.Board" %>
<%@ page import="board.BoardDAO" %>
<%@ page import="utils.BoardUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
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
    }
%>

