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

<%@ page import="category.CategoryDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    int id = Integer.parseInt(request.getParameter("id"));  // 파라미터로 전달된 id 값 가져오기

    BoardDAO boardDAO = new BoardDAO();
    Board board = boardDAO.getBoardById(id);  // id에 해당하는 게시글 객체 가져오기
    boardDAO.updateVisitCount(board.getBoardId(), board.getVisitCount());
    int updatedVisitCount = boardDAO.getBoardById(id).getVisitCount();  // id에 해당하는 게시글 객체 가져오기

    CommentDAO commentDAO = new CommentDAO();
    List<Comment> comments = commentDAO.getCommentsByBoardId(board.getBoardId());

    AttachmentDAO attachmentDAO = new AttachmentDAO();
    List<Attachment> attachments = attachmentDAO.getAttachmentsByBoardId(board.getBoardId());

%>

<html>
<head>
    <title>게시판 - 보기</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/3.1.9-1/crypto-js.min.js"></script>
    <!-- Bootstrap CSS -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap JS, Popper.js -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

</head>
<body>
<div class="container my-5">
    <h1 class="my-4">게시판 - 보기</h1>
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
                        window.location.replace("list")

                    } catch (error) {
                        alert(error);
                    }
                }
            } else {
                var errorDiv = document.getElementById('passwordError');
                errorDiv.textContent = '비밀번호가 일치하지 않습니다.';
            }
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

    <div class="card mb-3">
        <div class="card-header bg-transparent pb-0">
            <div class="d-flex justify-content-between">
                <p class="mb-0">작성자: <%= board.getWriter() %>
                </p>
                <div class="d-flex">
                    <p class="mb-0 me-4 mr-2">등록일시: <%= board.getCreatedAt()%>
                    </p>
                    <p class="mb-0">수정일시: <%= board.getModifiedAt() %>
                    </p>
                </div>
            </div>
            <div class="d-flex justify-content-between pt-2">
                <h5 class="card-title mb-4">[<%= CategoryDAO.getCategoryNameById(board.getCategoryId()) %>
                    ]: <%= board.getTitle() %>
                </h5>
                <p class="card-text mb-4">조회수: <%= updatedVisitCount %>
                </p>
            </div>
        </div>
    </div>
    <div class="card-body border">
        <div class="card-text">dd
            <%= board.getContent() %>
        </div>
    </div>

    <br>
    <div>
        <% for (Attachment attachment : attachments) { %>
        <a href="download.jsp?fileName=<%=attachment.getFileName()%>" class="mb-2 text-decoration-underline d-block">
            <%=attachment.getOriginName()%>
        </a>
        <% } %>
    </div>
    <br>

    <div>
        <% if (comments != null && comments.size() > 0) { %>

        <div class="list-group comment-item bg-light">
            <% for (Comment comment : comments) { %>
            <div class="list-group-item comment-item">
                <div class="d-flex justify-content-between">
                    <small class="mb-1"><%= comment.getCreatedAt() %>
                    </small>
                </div>
                <p class="mb-1"><%= comment.getContent() %>
                </p>
            </div>
            <% } %>
        </div>

        <% } else { %>
        <p>댓글이 없습니다.</p>
        <% } %>
    </div>

    <!-- 댓글 작성 -->
    <div class="d-flex justify-content-center my-3">
        <form action="addComment.jsp" method="post" class="w-75">
            <input type="hidden" name="id" value="<%= board.getBoardId() %>">
            <div class="row">
                <div class="col-8">
                    <div class="form-group">
                        <textarea class="form-control" name="content" rows="4" placeholder="댓글을 입력해주세요."></textarea>
                    </div>
                </div>
                <div class="col-4 d-flex align-items-center">
                    <button class="btn btn-primary" type="submit">댓글 등록</button>
                </div>
            </div>
        </form>
    </div>


    <!-- 버튼 그룹 -->
    <div class="d-flex justify-content-center mt-3">
        <div class="buttons">
            <%
                String category = request.getParameter("category");
                String search = request.getParameter("search");
                String startDate = request.getParameter("startDate");
                String endDate = request.getParameter("endDate");
            %>
            <a href="list?page=<%= request.getParameter("page") %>&category=<%= category %>&search=<%= search %>&startDate=<%= startDate %>&endDate=<%= endDate %>"
               class="btn btn-secondary">목록으로 돌아가기</a>

            <button class="btn btn-primary" onclick="showPasswordModal('edit')">수정</button>
            <button class="btn btn-primary" onclick="showPasswordModal('delete')">삭제</button>
        </div>
    </div>
</div>

</body>
</html>