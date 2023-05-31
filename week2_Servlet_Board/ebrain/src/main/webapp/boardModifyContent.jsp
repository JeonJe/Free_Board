<%@ page import="board.Board" %>
<%@ page import="attachment.Attachment" %>
<%@ page import="java.util.List" %>
<%@ page import="category.CategoryDAO" %>
<%@ page import="utils.BoardUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    //Get Post information
    Board board = (Board) request.getAttribute("board");
    int boardId = (int) board.getBoardId();
    int categoryId = (int) board.getCategoryId();
    String createdAt = board.getCreatedAt();
    String modifiedAt = board.getModifiedAt();
    int visitCount = board.getVisitCount();
    String title = board.getTitle();
    String content = board.getContent();
    String writer = board.getWriter();
    String categoryName = CategoryDAO.getCategoryNameById(categoryId);
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


    const maxAttachments = 3;

    //Use attachment id to make it unique
    let attachmentCounter = 1;

    //Use to save the attachment deletion list.
    let deletedAttachmentIds = [];
    const deletedAttachmentIdsInput = document.getElementById("deletedAttachmentIdsInput");
    deletedAttachmentIdsInput.value = JSON.stringify(deletedAttachmentIds);

    // Delete Attachment Item and Draw new attachment upload Div
    function deleteAttachment(index, attachmentId) {

        const attachmentElement = document.getElementById("attachment" + index);
        attachmentElement.remove();
        deletedAttachmentIds.push(attachmentId);

        const attachmentsContainer = document.getElementById("attachmentsList");
        const numAttachments = attachmentsContainer.getElementsByClassName('file-block').length;

        // Add a new attachment input only if the number of attachments is less than maxAttachments
        if (numAttachments < maxAttachments) {
            drawNewAttachmentDiv();
        }
    }

    // Function to draw div to add new attachments
    function drawNewAttachmentDiv() {

        const attachmentsContainer = document.getElementById("attachmentsList");

        while (document.getElementById("attachment" + attachmentCounter) !== null) {
            attachmentCounter++;
        }

        const fileBlock = document.createElement("div");
        fileBlock.className = "file-block";

        const fileInput = document.createElement("input");
        fileInput.type = "file";

        const index = attachmentCounter++;
        fileInput.setAttribute("id", "attachment" + index);
        fileInput.setAttribute("name", "attachment" + index);
        fileBlock.appendChild(fileInput)

        attachmentsContainer.appendChild(fileBlock);
    }

    //  If you click the Save button, submit a list of attachments and deleted files with update information
    function submitForm() {

        const passwordField = document.getElementById('password');
        if (passwordField.value === '') {
            alert('비밀번호를 입력해주세요.');
            return false;
        }
        const deletedAttachmentIdsInput = document.getElementById("deletedAttachmentIdsInput");
        deletedAttachmentIdsInput.value = JSON.stringify(deletedAttachmentIds);

        document.forms[0].submit();
        return true;
    }

    // Click the Cancel button to cancel the update.
    function onClickCancel() {
        const params = new URLSearchParams({
            page: '<%= request.getParameter("page") %>',
            category: '<%= request.getParameter("category") %>',
            search: '<%= request.getParameter("search") %>',
            startDate: '<%= request.getParameter("startDate") %>',
            endDate: '<%= request.getParameter("endDate") %>',
        });

        const viewURL = `view?action=view&id=<%= board.getBoardId() %>&${params.toString()}`;
        location.href = viewURL;
    }

</script>


<%-- Show the board information --%>
<div class="container my-4">
    <h1 class="my-4">게시판 - 수정</h1>
    <div class="row justify-content-center">
        <div class="col-md-12 bg-light">

            <form action="update" method="post" class="p-3" enctype="multipart/form-data">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="<%= boardId %>">
                <div class="form-group row border-bottom p-3">
                    <label for="category" class="col-sm-2 col-form-label d-flex align-items-center">카테고리</label>
                    <div class="col-sm-8">
                        <p id="category"><%=categoryName %>
                        </p>
                    </div>
                </div>
                <div class="form-group row border-bottom p-3">
                    <label for="createdAt" class="col-sm-2 col-form-label d-flex align-items-center">등록일시</label>
                    <div class="col-sm-8">
                        <p id=createdAt><%=createdAt%>
                        </p>
                    </div>
                </div>
                <div class="form-group row border-bottom p-3">
                    <label for="modifiedAt" class="col-sm-2 col-form-label d-flex align-items-center">수정일시</label>
                    <div class="col-sm-8">
                        <p id=modifiedAt><%=modifiedAt%>
                        </p>
                    </div>
                </div>
                <div class="form-group row border-bottom p-3">
                    <label for="visitCount" class="col-sm-2 col-form-label d-flex align-items-center">조회수</label>
                    <div class="col-sm-8">
                        <p id=visitCount><%=visitCount %>
                        </p>
                    </div>
                </div>
                <div class="form-group row border-bottom p-3">
                    <label for="writer" class="col-sm-2 col-form-label d-flex align-items-center">작성자</label>
                    <div class="col-sm-8">
                        <input type="text" id="writer" name="writer" value="<%= writer %>"
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
                        <input type="text" id="title" name="title" value="<%= title%>" class="form-control">
                    </div>
                </div>

                <div class="form-group row border-bottom p-3">
                    <label for="content" class="col-sm-2 col-form-label d-flex align-items-center">내용</label>
                    <div class="col-sm-8">
                        <textarea id="content" name="content" class="form-control"
                                  rows="6"><%= content %></textarea>
                    </div>
                </div>

                <%-- List of attachments --%>
                <div class="form-group row border-bottom p-3" id="attachmentsContainer">
                    <label class="col-sm-2 col-form-label d-flex align-items-center">첨부파일</label>
                    <div class="col-sm-8" id="attachmentsList">
                        <% for (int i = 0; i < attachments.size(); i++) {
                            Attachment attachment = attachments.get(i);
                        %>
                        <div class="file-block d-flex justify-content-between" id="attachment<%= i+1 %>">
                            <input type="hidden" id="attachment<%= i+1 %>" name="<%= attachment.getAttachmentId() %>">
                            <p><%= attachment.getOriginName() %></p>
                            <div>
                                <a href="download?action=download&fileName=<%= attachment.getFileName() %>"
                                   class="btn btn-sm btn-primary">Download</a>
                                <button class="btn btn-sm btn-danger" onclick="deleteAttachment(<%= i+1 %>, <%= attachment.getAttachmentId() %>)">X</button>
                            </div>
                        </div>
                        <% } %>

                        <% for (int i = attachments.size(); i < 3; i++) { %>
                        <div class="file-block">
                            <input type="file" id="attachment<%= i+1 %>" name="attachment<%= i+1 %>">
                        </div>
                        <% } %>
                    </div>

                </div>
                <input type="hidden" name="deletedAttachmentIds" id="deletedAttachmentIdsInput">
                <div class="row mt-3 justify-content-center">
                    <div class="col-md-6">
                        <input type="button" value="취소" onclick="onClickCancel()" class="btn btn-secondary btn-block">
                    </div>
                    <div class="col-md-6">
                        <input type="submit" value="저장" onclick="submitForm()" class="btn btn-primary btn-block">
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>