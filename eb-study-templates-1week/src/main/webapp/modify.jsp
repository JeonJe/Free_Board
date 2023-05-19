
<%@ page import="board.Board" %>
<%@ page import="board.BoardDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    int id = Integer.parseInt(request.getParameter("id"));  // 파라미터로 전달된 id 값 가져오기
    BoardDAO boardDAO = new BoardDAO();
    Board board = boardDAO.getBoardById(id);  // id에 해당하는 게시글 객체 가져오기
%>

<html>
<head>
    <title>게시판 - 수정</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<h1>게시판 - 수정</h1>


<% if (board != null) {  // 게시글이 존재하는 경우에만 표시 %>
<form action="update.jsp" method="post">
    <p>카테고리: <%= board.getCategory() %></p>
    <p>등록일시: <%= board.getCreatedAt() %></p>
    <p>수정일시: <%= board.getModifiedAt() %></p>
    <p>조회수: <%= board.getVisitCount() %></p>
    <input type="hidden" name="id" value="<%= board.getBoardId() %>">

    <label for="writer">작성자:</label><br>
    <input type="text" id="writer" name="writer" value="<%= board.getWriter() %>"><br>

    <label for="password">비밀번호:</label><br>
    <input type="password" id="password" name="password"><br>

    <label for="title">제목:</label><br>
    <input type="text" id="title" name="title" value="<%= board.getTitle() %>"><br>

    <label for="content">내용:</label><br>
    <textarea id="content" name="content"><%= board.getContent() %></textarea><br>

    <input type="submit" value="저장">
    <input type="button" value="취소" onclick="location.href='view.jsp?id= <%= board.getBoardId() %>">
</form>
<% } %>

</body>
</html>