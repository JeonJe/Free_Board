
<%@ page import="board.Board" %>
<%@ page import="board.BoardDAO" %>
<%@ page import="attachment.AttachmentDAO" %>
<%@ page import="attachment.Attachment" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    int id = Integer.parseInt(request.getParameter("id"));  // 파라미터로 전달된 id 값 가져오기
    BoardDAO boardDAO = new BoardDAO();
    Board board = boardDAO.getBoardById(id);  // id에 해당하는 게시글 객체 가져오기

    AttachmentDAO attachmentDAO = new AttachmentDAO();
    List<Attachment> attachments = attachmentDAO.getAttachmentsByBoardId(board.getBoardId());

%>

<html>
<head>
    <title>게시판 - 수정</title>
    <link rel="stylesheet" type="text/css" href="styles.css">

    <!-- Bootstrap CSS -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap JS, Popper.js -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</head>
<body>

<% if (board != null) {  // 게시글이 존재하는 경우에만 표시 %>
<div class="container my-4">
    <h1 class="my-4">게시판 - 수정</h1>
    <div class="row justify-content-center">
        <div class="col-md-12 bg-light">
            <form action="update.jsp" method="post" class="p-3">
                <input type="hidden" name="id" value="<%= board.getBoardId() %>">
                <div class="form-group row border-bottom p-3">
                    <label for="category" class="col-sm-2 col-form-label d-flex align-items-center">카테고리</label>
                    <div class="col-sm-8">
                        <p id="category"><%= board.getCategory()%></p>
                    </div>
                </div>
                <div class="form-group row border-bottom p-3">
                    <label for="createdAt" class="col-sm-2 col-form-label d-flex align-items-center">등록일시</label>
                    <div class="col-sm-8">
                        <p id=createdAt><%= board.getCreatedAt()%></p>
                    </div>
                </div>
                <div class="form-group row border-bottom p-3">
                    <label for="modifiedAt" class="col-sm-2 col-form-label d-flex align-items-center">수정일시</label>
                    <div class="col-sm-8">
                        <p id=modifiedAt><%= board.getModifiedAt()%></p>
                    </div>
                </div>
                <div class="form-group row border-bottom p-3">
                    <label for="visitCount" class="col-sm-2 col-form-label d-flex align-items-center">조회수</label>
                    <div class="col-sm-8">
                        <p id=visitCount><%= board.getVisitCount()%></p>
                    </div>
                </div>
                <div class="form-group row border-bottom p-3">
                    <label for="writer" class="col-sm-2 col-form-label d-flex align-items-center">작성자</label>
                    <div class="col-sm-8">
                        <input type="text" id="writer" name="writer" value="<%= board.getWriter() %>" class="form-control">
                    </div>
                </div>

                <div class="form-group row border-bottom p-3">
                    <label for="password" class="col-sm-2 col-form-label d-flex align-items-center">비밀번호</label>
                    <div class="col-sm-8">
                        <input type="password" id="password" name="password" placeholder="비밀번호" class="form-control" required>
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
                        <textarea id="content" name="content" class="form-control" rows="6"><%= board.getContent() %></textarea>
                    </div>
                </div>

                <%-- 첨부파일 목록 --%>
                <%-- 첨부파일 목록 --%>
                <div class="form-group row border-bottom p-3">
                    <label class="col-sm-2 col-form-label d-flex align-items-center">첨부파일</label>
                    <div class="col-sm-8">
                        <%
                            for(int i=0; i<attachments.size(); i++) {
                                Attachment attachment = attachments.get(i);
                        %>
                        <div class="file-block d-flex justify-content-between">
                            <p><%= attachment.getFileName() %></p>
                            <div>
                                <a href="download.jsp?file=<%= attachment.getFileName() %>" class="btn btn-sm btn-primary">Download</a>
                                <a href="delete.jsp?file=<%= attachment.getFileName() %>" class="btn btn-sm btn-danger">X</a>
                            </div>
                        </div>
                        <% } %>

                        <% for(int i=attachments.size(); i<3; i++) { %>
                        <div class="file-block">
                            <input type="file" id="file<%= (i+1) %>" name="file<%= (i+1) %>">
                        </div>
                        <% } %>
                    </div>
                </div>
                <div class="row mt-3 justify-content-center">
                    <div class="col-md-6">
                        <input type="button" value="취소" onclick="location.href='view.jsp?id= <%= board.getBoardId() %>'" class="btn btn-secondary btn-block">
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