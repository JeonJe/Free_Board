<%--
  Created by IntelliJ IDEA.
  User: premise
  Date: 2023/05/16
  Time: 11:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="board.Board" %>
<%@ page import="board.BoardDAO" %>
<%@ page import="utils.PasswordUtils" %>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String category = request.getParameter("category");
    String writer = request.getParameter("writer");
    String password = request.getParameter("password");
    String title = request.getParameter("title");
    String content = request.getParameter("content");

    String hashedPassword = PasswordUtils.hashPassword(password);

    Board board = new Board();
    board.setCategory(category);
    board.setWriter(writer);
    board.setPassword(hashedPassword);
    board.setTitle(title);
    board.setContent(content);
    board.setVisitCount(0);

    BoardDAO boardDAO = new BoardDAO();
    boardDAO.save(board);

    response.sendRedirect("list.jsp");
%>