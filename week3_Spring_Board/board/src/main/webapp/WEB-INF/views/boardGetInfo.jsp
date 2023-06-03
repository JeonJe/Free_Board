<%--
  Created by IntelliJ IDEA.
  User: premise
  Date: 2023/05/16
  Time: 11:37 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>게시판 - 보기</title>
    <%-- Use for hashing password     --%>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/3.1.9-1/crypto-js.min.js"></script>
    <!-- Bootstrap CSS -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap JS, Popper.js -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</head>

<body>
<script>
    // True when clicking the Modify button, False when clicking the Delete button
    let isEdit = false;

    function hidePasswordModal() {
        const modal = document.getElementById('passwordModal');
        modal.style.display = 'none';
    }

    function showPasswordModal(action) {
        const modal = document.getElementById('passwordModal');
        modal.style.display = 'block';
        if (action === 'edit') {
            isEdit = true;
        } else {
            isEdit = false;
        }
    }

    //After checking the password, call the modification and deletion function.
    async function validatePassword(form) {
        const password = '${board.password}';
        const enteredPassword = document.getElementById('passwordInput').value;
        const hashedPassword = CryptoJS.SHA256(enteredPassword).toString();

        if (hashedPassword === password) {
            hidePasswordModal();
            if (isEdit) {
                const editPage = '${param.page}';
                const editCategory = '${param.category}';
                const editSearch = '${param.searchText}';
                const editStartDate = '${param.startDate}';
                const editEndDate = '${param.endDate}';

                const redirectURL = `modify?action=modify&id=${board.boardId}&page=${editPage}&
                category=${editCategory}&search=${editSearch}&startDate=${editStartDate}&endDate=${editEndDate}`;

                window.location.replace(redirectURL);
            } else {
                try {
                    const params = new URLSearchParams();
                    params.append('action', 'delete');
                    params.append('id', '${board.boardId}');
                    params.append('password', enteredPassword);

                    const response = await fetch('/delete', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: params,
                    });
                    if (!response.ok) {
                        alert("게시글 삭제에 실패하였습니다");
                    } else {
                        window.location.replace("/list?action=list");
                    }
                } catch (error) {
                    alert(error);
                }
            }
        } else {
            const errorDiv = document.getElementById('passwordError');
            errorDiv.textContent = '비밀번호가 일치하지 않습니다.';
        }
    }
</script>

<div class="container my-5">
    <h1 class="my-4">게시판 - 보기</h1>


    <%-- Password checking modal   --%>
    <div id="passwordModal" class="modal">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">비밀번호 확인</h5>
                    <button type="button" class="close" data-dismiss="modal" onclick="hidePasswordModal()">&times;
                    </button>
                </div>
                <div class="modal-body">
                    <form id="passwordForm" onsubmit="validatePassword(this); return false;">
                        <input type="password" id="passwordInput" name="password" required>
                        <br>
                        <div id="passwordError" class="error-message"></div>
                        <br>
                        <input type="submit" value="확인" class="btn btn-primary">
                    </form>
                </div>
            </div>
        </div>
    </div>

    <%-- Show the post information    --%>
    <div class="card mb-3">
        <div class="card-header bg-transparent pb-0">
            <div class="d-flex justify-content-between">
                <p class="mb-0">작성자: ${board.writer}</p>
                <div class="d-flex">
                    <p class="mb-0 me-4 mr-2">등록일시: ${board.createdAt}</p>
                    <p class="mb-0">수정일시: ${board.modifiedAt}</p>
                </div>
            </div>
            <div class="d-flex justify-content-between pt-2">
                <h5 class="card-title mb-4">[${categoryName}
                    ]: ${board.title}</h5>
                <p class="card-text mb-4">조회수: ${board.visitCount}</p>
            </div>
        </div>
    </div>
    <div class="card-body border">
        <div class="card-text">${board.content}</div>
    </div>
    <br>
    <%-- list of attachments --%>
    <div>
        <c:forEach var="attachment" items="${attachments}">
            <a href="download?action=download&fileName=${attachment.fileName}"
               class="mb-2 text-decoration-underline d-block">${attachment.originName}</a>
        </c:forEach>
    </div>
    <br>
    <%-- list of comments --%>
    <div>
        <c:if test="${not empty comments}">
            <div class="list-group comment-item bg-light">
                <c:forEach var="comment" items="${comments}">
                    <div class="list-group-item comment-item">
                        <div class="d-flex justify-content-between">
                            <small class="mb-1">${comment.createdAt}</small>
                        </div>
                        <p class="mb-1">${comment.content}</p>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <c:if test="${empty comments}">
            <p>댓글이 없습니다.</p>
        </c:if>
    </div>

    <!-- write comment and post -->
    <div class="d-flex justify-content-center my-3">
        <form action="add-comment" method="post" class="w-75">
            <input type="hidden" name="action" value="addComment">
            <input type="hidden" name="id" value="${board.boardId}">
            <input type="hidden" name="page" value="${param.page}">
            <input type="hidden" name="category" value="${param.category}">
            <input type="hidden" name="searchText" value="${param.searchText}">
            <input type="hidden" name="startDate" value="${param.startDate}">
            <input type="hidden" name="endDate" value="${param.endDate}">
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


    <!-- button group -->
    <div class="d-flex justify-content-center mt-3">
        <div class="buttons">
            <a href="list?action=list&page=${param.page}&category=${param.category}&searchText=${param.searchText}&startDate=${param.startDate}&endDate=${param.endDate}"
               class="btn btn-secondary">목록으로 돌아가기</a>
            <button class="btn btn-primary" onclick="showPasswordModal('edit')">수정</button>
            <button class="btn btn-primary" onclick="showPasswordModal('delete')">삭제</button>
        </div>
    </div>
</div>

</body>
</html>