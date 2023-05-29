<%@ page import="board.Board" %>
<%@ page import="attachment.Attachment" %>
<%@ page import="java.util.List" %>
<%@ page import="category.CategoryDAO" %>
<%@ page import="utils.BoardUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    //Get Post information
    Board board = (Board) request.getAttribute("board");
    List<Attachment> attachments = (List<Attachment>) request.getAttribute("attachments");
%>

<html>
<head>
    <title>게시판 - 수정</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">

    <!-- Bootstrap CSS -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap JS, Popper.js -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</head>
<body>

<script>
    //show error message
    <% String errorMessage = (String) request.getAttribute(BoardUtils.ERROR_MESSAGE_ATTRIBUTE);
             if (errorMessage != null && !errorMessage.isEmpty()) { %>
    alert("<%= errorMessage %>");
    <% } %>
    //TODO : 폼으로 전달해보기
    function onClickCancel() {
        const page = '<%= request.getParameter("page") %>';
        const category = '<%= request.getParameter("category") %>';
        const search = '<%= request.getParameter("search") %>';
        const startDate = '<%= request.getParameter("startDate") %>';
        const endDate = '<%= request.getParameter("endDate") %>';

        const viewURL = 'view?id=<%= board.getBoardId() %>' + '&page=' + page +
            '&category=' + category + '&search=' + search +
            '&startDate=' + startDate + '&endDate=' + endDate;
        location.href = viewURL;
    }

    // 첨부파일 삭제 함수
    function deleteAttachment(index) {
        // 첨부파일 요소를 삭제합니다.
        const attachmentElement = document.getElementById("attachment" + index);
        attachmentElement.remove();

        const fileBlock = document.createElement("div");
        fileBlock.className = "file-block";
        fileBlock.innerHTML = `<input type="file" id="attachment${index}" name="attachment${index}>`;

        const attachmentsContainer = document.getElementById("attachmentsList");
        attachmentsContainer.appendChild(fileBlock);
    }

</script>

<% if (board != null) { %>

<%-- Show the post information --%>
<div class="container my-4">
    <h1 class="my-4">게시판 - 수정</h1>
    <div class="row justify-content-center">
        <div class="col-md-12 bg-light">
            <form action="update" method="post" class="p-3" enctype="multipart/form-data">
                <input type="hidden" name="id" value="<%= board.getBoardId() %>">
                <div class="form-group row border-bottom p-3">
                    <label for="category" class="col-sm-2 col-form-label d-flex align-items-center">카테고리</label>
                    <div class="col-sm-8">
                        <p id="category"><%= CategoryDAO.getCategoryNameById(board.getCategoryId())%>
                        </p>
                    </div>
                </div>
                <div class="form-group row border-bottom p-3">
                    <label for="createdAt" class="col-sm-2 col-form-label d-flex align-items-center">등록일시</label>
                    <div class="col-sm-8">
                        <p id=createdAt><%= board.getCreatedAt()%>
                        </p>
                    </div>
                </div>
                <div class="form-group row border-bottom p-3">
                    <label for="modifiedAt" class="col-sm-2 col-form-label d-flex align-items-center">수정일시</label>
                    <div class="col-sm-8">
                        <p id=modifiedAt><%= board.getModifiedAt()%>
                        </p>
                    </div>
                </div>
                <div class="form-group row border-bottom p-3">
                    <label for="visitCount" class="col-sm-2 col-form-label d-flex align-items-center">조회수</label>
                    <div class="col-sm-8">
                        <p id=visitCount><%= board.getVisitCount()%>
                        </p>
                    </div>
                </div>
                <div class="form-group row border-bottom p-3">
                    <label for="writer" class="col-sm-2 col-form-label d-flex align-items-center">작성자</label>
                    <div class="col-sm-8">
                        <input type="text" id="writer" name="writer" value="<%= board.getWriter() %>"
                               class="form-control">
                    </div>
                </div>

                <div class="form-group row border-bottom p-3">
                    <label for="password" class="col-sm-2 col-form-label d-flex align-items-center">비밀번호</label>
                    <div class="col-sm-8">
                        <input type="password" id="password" name="password" placeholder="비밀번호" class="form-control"
                               required>
                    </div>
                </div>

                <div class="form-group row border-bottom p-3">
                    <label for="title" class="col-sm-2 col-form-label d-flex align-items-center">제목</label>
                    <div class="col-sm-8">
                        <input type="text" id="title" name="title" value="<%= board.getTitle() %>" class="form-control">
                    </div>
                </div>

                <div class="form-group row border-bottom p-3">
                    <label for="content" class="col-sm-2 col-form-label d-flex align-items-center">내용</label>
                    <div class="col-sm-8">
                        <textarea id="content" name="content" class="form-control"
                                  rows="6"><%= board.getContent() %></textarea>
                    </div>
                </div>

                <%-- List of attachments --%>
                <div class="form-group row border-bottom p-3" id="attachmentsContainer">
                    <label class="col-sm-2 col-form-label d-flex align-items-center">첨부파일</label>
                    <div class="col-sm-8" id="attachmentsList">
                        <% for (int i = 0; i < attachments.size(); i++) {
                            Attachment attachment = attachments.get(i);
                        %>
                        <div class="file-block d-flex justify-content-between" id="attachment<%= (i+1) %>" %>"
                            <hidden><input type="file" id="attachment<%= (i+1) %>" name="attachment<%= (i+1) %>" ></hidden>
                            <p><%= attachment.getOriginName() %></p>
                            <div>
                                <a href="download?fileName=<%= attachment.getFileName() %>"
                                   class="btn btn-sm btn-primary">Download</a>
                                <a href="#" class="btn btn-sm btn-danger" onclick="deleteAttachment(<%= i %>)">X</a>
                            </div>
                        </div>
                        <% } %>

                        <% for (int i = attachments.size(); i < 3; i++) { %>
                        <div class="file-block">
                            <input type="file" id="attachment<%= (i+1) %>" name="attachment<%= (i+1) %>" >
                        </div>
                        <% } %>
                    </div>
                </div>

                <div class="row mt-3 justify-content-center">
                    <div class="col-md-6">
                        <input type="button" value="취소" onclick="onClickCancel()" class="btn btn-secondary btn-block">
                    </div>
                    <div class="col-md-6">
                        <input type="submit" value="저장" class="btn btn-primary btn-block">
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<% } %>

</body>
</html>