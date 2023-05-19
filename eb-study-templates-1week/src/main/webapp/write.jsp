<%--
  Created by IntelliJ IDEA.
  User: premise
  Date: 2023/05/16
  Time: 11:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>게시글 작성</title>
</head>
<body>
<form action="save.jsp" method="post">
    <label for="category">카테고리:</label><br>
    <input type="text" id="category" name="category" required><br>
    <label for="writer">작성자:</label><br>
    <input type="text" id="writer" name="writer" required><br>
    <label for="password">비밀번호:</label><br>
    <input type="password" id="password" name="password" required><br>
    <label for="password">비밀번호 확인:</label><br>
    <input type="password" id="password-confirm" name="password-confirm" required><br>
    <label for="title">제목:</label><br>
    <input type="text" id="title" name="title" required><br>
    <label for="content">내용:</label><br>
    <textarea id="content" name="content" required></textarea><br>
    <input type="submit" value="저장">
    <input type="button" value="취소" onclick="location.href='list.jsp'">
</form>
</body>
</html>