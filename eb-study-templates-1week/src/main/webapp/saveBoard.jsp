<%--
  Created by IntelliJ IDEA.
  User: premise
  Date: 2023/05/16
  Time: 11:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="board.Board" %>
<%@ page import="board.BoardDAO" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%

    String category = request.getParameter("category");
    String writer = request.getParameter("writer");
    String password = request.getParameter("password");
    String title = request.getParameter("title");
    String content = request.getParameter("content");


    Board board = new Board();
    board.setCategory(category);
    board.setWriter(writer);
    board.setPassword(password);
    board.setTitle(title);
    board.setContent(content);
    board.setCreatedAt(((Timestamp) new java.sql.Timestamp(System.currentTimeMillis())).toLocalDateTime());
    board.setModifiedAt(((Timestamp) new java.sql.Timestamp(System.currentTimeMillis())).toLocalDateTime());

    BoardDAO boardDAO = new BoardDAO();
    boardDAO.save(board);

    response.sendRedirect("index.jsp");
%>