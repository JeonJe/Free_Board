<%--
  Created by IntelliJ IDEA.
  User: premise
  Date: 2023/05/16
  Time: 11:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="board.Board" %>
<%@ page import="board.BoardDAO" %>
<%@ page import="comment.CommentDAO" %>
<%@ page import="comment.Comment" %>
<%@ page import="java.util.List" %>
<%@ page import="attachment.Attachment" %>
<%@ page import="attachment.AttachmentDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    int id = Integer.parseInt(request.getParameter("id"));  // 파라미터로 전달된 id 값 가져오기
    BoardDAO boardDAO = new BoardDAO();
    Board board = boardDAO.getBoardById(id);  // id에 해당하는 게시글 객체 가져오기
    boardDAO.updateVisitCount(board.getBoardId(), board.getVisitCount());
    int updatedVisitCount = boardDAO.getBoardById(id).getVisitCount();  // id에 해당하는 게시글 객체 가져오기
    AttachmentDAO attachmentDAO = new AttachmentDAO();
    List<Attachment> attachments = attachmentDAO.getAttachmentsByBoardId(board.getBoardId());

%>

<html>
<head>
    <title>게시판 - 보기</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.0.0/crypto-js.min.js"></script>

</head>
<body>
<h1>게시판 - 보기</h1>
<script>
    var isEdit = false;

    function hidePasswordModal() {
        var modal = document.getElementById('passwordModal');
        modal.style.display = 'none';
    }

    function showPasswordModal(action) {
        var modal = document.getElementById('passwordModal');
        modal.style.display = 'block';
        if (action === 'edit') {
            isEdit = true;
        } else {
            isEdit = false;
        }
    }

    async function validatePassword(form) {
        var password = '<%= board.getPassword() %>';
        var enteredPassword = form.password.value;
        var hashedPassword = CryptoJS.SHA256(enteredPassword).toString();

        if (hashedPassword === password) {
            hidePasswordModal();
            if (isEdit) {
                window.location.replace("modify.jsp?id=<%= board.getBoardId() %>")
            } else {
                try {
                    const response = await fetch('delete.jsp', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: 'id=' + <%= board.getBoardId() %> +'&password=' + enteredPassword
                    });
                    if (!response.ok) {
                        alert("삭제에 실패하였습니다");
                    }
                    window.location.replace("list.jsp")

                } catch (error) {
                    alert(error);
                }
            }
        } else {
            var errorDiv = document.getElementById('passwordError');
            errorDiv.textContent = '비밀번호가 일치하지 않습니다.';
        }
    }

    async function downloadFile(attachmentId, fileName) {
        console.log(attachmentId, fileName);
    }

</script>

<!-- 비밀번호 확인 모달 -->
<div id="passwordModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="hidePasswordModal()">&times;</span>
        <h2>비밀번호 확인</h2>
        <form id="passwordForm" onsubmit="validatePassword(this); return false;">
            <input type="password" id="passwordInput" name="password" required>
            <br>
            <div id="passwordError" class="error-message"></div>
            <br>
            <input type="submit" value="확인">
        </form>
    </div>
</div>


<% if (board != null) { %>
<table>
    <tr>
        <td colspan="2">작성자: <%= board.getWriter() %>
        </td>
        <td>등록일시: <%= board.getCreatedAt() %>
        </td>
        <td>수정일시: <%= board.getModifiedAt() %>
        </td>
    </tr>
    <tr>
        <td colspan="2">[<%= board.getCategory() %>]</td>
        <td colspan="2">제목: <%= board.getTitle() %>
        </td>
    </tr>
    <tr>
        <td>조회수: <%= updatedVisitCount %>
        </td>
        <td colspan="3"></td>
    </tr>
    <tr>
        <td colspan="4"><%= board.getContent() %>
        </td>
    </tr>
    <% if (attachments != null && attachments.size() > 0) { %>
    <tr>
        <td colspan="4">첨부파일 명:
            <%
                for (Attachment attachment : attachments) {
            %>
            <p><%= attachment.getFileName() %></p>
            <a href="download.jsp?fileName=<%=attachment.getFileName()%>">다운로드</a>
            <%
                }
            %>
        </td>
    </tr>
    <% } %>
</table>

<%
    CommentDAO commentDAO = new CommentDAO();
    List<Comment> comments = commentDAO.getCommentsByBoardId(board.getBoardId());

    if (comments != null && comments.size() > 0) {
%>
<h2>댓글 목록</h2>
<table>
    <% for (Comment comment : comments) { %>
    <tr>
        <td><%= comment.getCreatedAt() %>
        </td>
        <td>댓글 내용: <%= comment.getContent() %>
        </td>
    </tr>
    <% } %>
</table>
<% } else { %>
<p>댓글이 없습니다.</p>
<% } %>


<% } else { %>
<p>해당 게시글을 찾을 수 없습니다.</p>
<a href="list.jsp">목록으로</a>
<% } %>

<h2>댓글 작성</h2>
<form action="addComment.jsp" method="post">
    <input type="hidden" name="id" value="<%= board.getBoardId() %>">
    <textarea name="content" rows="4" cols="50"></textarea>
    <br>
    <input type="submit" value="댓글 등록">
</form>


<div class="buttons">
    <a href="list.jsp">
        <button>목록으로</button>

    </a>

    <button onclick="showPasswordModal('edit')">수정</button>
    <button onclick="showPasswordModal('delete')">삭제</button>
</div>
</body>
</html>