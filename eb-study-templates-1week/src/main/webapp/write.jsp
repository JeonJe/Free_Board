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
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

</head>
<body>
<form id="post-form" action="save.jsp" method="post"enctype="multipart/form-data" >
    <label for="category">카테고리:</label><br>
    <select id="category" name="category" required>
        <option value="">카테고리 선택</option>
        <option value="Java">Java</option>
        <option value="Database">Database</option>
        <option value="Javascript">Javascript</option>
    </select><br>
    <label for="writer">작성자:</label><br>
    <input autofocus type="text" id="writer" name="writer" required><br>
    <label for="password">비밀번호:</label><br>
    <input type="password" id="password" name="password" required><br>
    <label for="password">비밀번호 확인:</label><br>
    <input type="password" id="password-confirm" name="password-confirm" required><br>
    <label for="title">제목:</label><br>
    <input type="text" id="title" name="title" required><br>
    <label for="content">내용:</label><br>
    <textarea id="content" name="content" required></textarea><br>

    <label for="attachment1">첨부파일:</label><br>
    <input type="file" id="attachment1" name="attachment1"><br>
    <label for="attachment2">첨부파일:</label><br>
    <input type="file" id="attachment2" name="attachment2"><br>
    <label for="attachment3">첨부파일:</label><br>
    <input type="file" id="attachment3" name="attachment3"><br>

    <input type="submit" value="저장">
    <input type="button" value="취소" onclick="location.href='list.jsp'">
</form>
<script>
    document.getElementById('post-form').addEventListener('submit', function(event) {
        var writer = document.getElementById('writer').value;
        var password = document.getElementById('password').value;
        var passwordConfirm = document.getElementById('password-confirm').value;
        var title = document.getElementById('title').value;
        var content = document.getElementById('content').value;

        if (writer.length < 3 || writer.length >= 5) {
            alert('작성자는 3글자 이상 5글자 미만이어야 합니다.');
            event.preventDefault();
            return;
        }

        if (password.length < 4 || password.length >= 16 || !/[A-Za-z0-9_$@#%&*]/.test(password) || password !== passwordConfirm) {
            alert('비밀번호는 4글자 이상 16글자 미만이어야 하며, 영문, 숫자, 특수문자를 포함해야 하며, 비밀번호 확인과 일치해야 합니다.');
            event.preventDefault();
            return;
        }

        if (title.length < 4 || title.length >= 100) {
            alert('제목은 4글자 이상 100글자 미만이어야 합니다.');
            event.preventDefault();
            return;
        }

        if (content.length < 4 || content.length >= 2000) {
            alert('내용은 4글자 이상 2000글자 미만이어야 합니다.');
            event.preventDefault();
            return;
        }
    });
</script>
</body>
</html>