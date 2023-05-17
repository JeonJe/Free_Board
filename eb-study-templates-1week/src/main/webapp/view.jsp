<%--
  Created by IntelliJ IDEA.
  User: premise
  Date: 2023/05/16
  Time: 11:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="board.Board" %>
<%@ page import="board.BoardDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    int id = Integer.parseInt(request.getParameter("id"));  // 파라미터로 전달된 id 값 가져오기
    BoardDAO boardDAO = new BoardDAO();
    Board board = boardDAO.getBoardById(id);  // id에 해당하는 게시글 객체 가져오기
%>
<%
    boolean isEdit = false;
    if (request.getParameter("isEdit") != null && request.getParameter("isEdit").equals("true")) {
        isEdit = true;
    }
%>
<html>
<head>
    <title>게시판 - 보기</title>
    <style>
        /* 모달 스타일 */
        .modal {
            display: none;
            position: fixed;
            z-index: 9999;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0, 0, 0, 0.8);
        }

        .modal-content {
            background-color: #fefefe;
            margin: 10% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 60%;
            max-width: 600px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
        }

        .modal-content h2 {
            margin-top: 0;
        }

        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
        }

        .close:hover,
        .close:focus {
            color: #000;
            text-decoration: none;
            cursor: pointer;
        }

        .error-message {
            color: red;
        }
    </style>
    <script>
        function hidePasswordModal() {
            var modal = document.getElementById('passwordModal');
            modal.style.display = 'none';
        }

        function showPasswordModal() {
            var modal = document.getElementById('passwordModal');
            modal.style.display = 'block';
        }

        function validatePassword(form, isEdit) {
            var password = '<%= board.getPassword() %>';
            var enteredPassword = form.password.value;

            if (enteredPassword === password) {
                hidePasswordModal();
                if (isEdit) {
                    openEditPage(<%= board.getBoardId() %>);
                } else {
                    openDeletePage(<%= board.getBoardId() %>);
                }
            } else {
                var errorDiv = document.getElementById('passwordError');
                errorDiv.textContent = '비밀번호가 일치하지 않습니다.';
            }
        }

        function openEditPage(boardId) {
            var url = 'editBoard.jsp?id=' + boardId;
            window.location.href = url;
        }

        function openDeletePage(boardId) {
            var url = 'deleteBoard.jsp?id=' + boardId;
            window.location.href = url;
        }
    </script>
</head>
<body>
<h1>게시판 - 보기</h1>


<% if (board != null) {  // 게시글이 존재하는 경우에만 표시 %>
<table>
    <tr>
        <td colspan="2">작성자: <%= board.getWriter() %></td>
        <td>등록일시: <%= board.getCreatedAt() %></td>
        <td>수정일시: <%= board.getModifiedAt() %></td>
    </tr>
    <tr>
        <td colspan="2">[<%= board.getCategory() %>]</td>
        <td colspan="2">제목: <%= board.getTitle() %></td>
    </tr>
    <tr>
        <td>조회수: <%= board.getVisitCount() %></td>
        <td colspan="3"></td>
    </tr>
    <tr>
        <td colspan="4"><%= board.getContent() %></td>
    </tr>
    <tr>
        <td colspan="4">첨부파일 명: <%-- 첨부파일이 있을 경우 파일 이름을 출력 --%></td>
    </tr>
</table>

<h2>댓글 작성</h2>
<form action="addComment.jsp" method="post">
    <input type="hidden" name="boardId" value="<%= board.getBoardId() %>">
    <textarea name="comment" rows="4" cols="50"></textarea>
    <br>
    <input type="submit" value="댓글 등록">
</form>


<!-- 비밀번호 확인 모달 -->
<div id="passwordModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="hidePasswordModal()">&times;</span>
        <h2>비밀번호 확인</h2>
        <form id="passwordForm" onsubmit="validatePassword(this, <%= isEdit %>); return false;">
            <input type="password" id="passwordInput" name="password" required>
            <br>
            <div id="passwordError" class="error-message"></div>
            <br>
            <input type="submit" value="확인">
        </form>
    </div>
</div>

<div class="buttons">
    <a href="list.jsp">목록으로</a>
    <button onclick="showPasswordModal()">수정</button>
    <button onclick="showPasswordModal()">삭제</button>
</div>
<% } else {  // 게시글이 존재하지 않는 경우에 대한 처리 %>
<p>해당 게시글을 찾을 수 없습니다.</p>
<a href="list.jsp">목록으로</a>
<% } %>




</body>
</html>