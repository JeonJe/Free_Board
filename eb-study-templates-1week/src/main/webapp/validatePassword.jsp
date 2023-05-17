<%@ page import="board.BoardDAO" %>
<%@ page import="board.Board" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int boardId = Integer.parseInt(request.getParameter("boardId"));
    String enteredPassword = request.getParameter("password");

    // 게시글 정보를 데이터베이스에서 가져옴
    BoardDAO boardDAO = new BoardDAO();
    Board board = boardDAO.getBoardById(boardId);

    boolean valid = false;  // 비밀번호 유효성 검사 결과

    if (board != null && board.getPassword().equals(enteredPassword)) {
        valid = true;  // 비밀번호가 일치하는 경우
    }

    // JSON 형식의 응답 데이터 생성
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write("{\"valid\": " + valid + "}");
%>