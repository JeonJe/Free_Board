<%--
  Created by IntelliJ IDEA.
  User: premise
  Date: 2023/05/17
  Time: 1:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>
<%@ page import="board.Board" %>
<%@ page import="board.BoardDAO" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="utils.StringUtils" %>
<%@ page import="category.CategoryDAO" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>자유 게시판</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container">
    <h1 class="my-4">자유 게시판 목록</h1>
    <form action="list.jsp" method="get" class="form-inline mb-4">
        <label for="startDate" class="mr-2">등록일:</label>
        <input type="date" id="startDate" name="startDate" class="form-control mr-2"
               value="<%= (request.getParameter("startDate") != null) ? request.getParameter("startDate") : LocalDate.now().minusYears(1) %>">
        <label for="endDate" class="mr-2"> ~ </label>
        <input type="date" id="endDate" name="endDate" class="form-control mr-2"
               value="<%= (request.getParameter("endDate") != null) ? request.getParameter("endDate") : LocalDate.now() %>">


        <label for="search" class="mr-2">검색어:</label>
        <input type="text" id="search" name="search" class="form-control mr-2">

        <input type="submit" value="검색" class="btn btn-primary">
    </form>


    <table class="table table-striped text-center" style="table-layout: auto; width: 100%;"
    <%
        String search = request.getParameter("search");
        String category = request.getParameter("category");
        if (category == null && search == null) {
            category = "all";
            search = "";
        }

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(1);

        String endDateString = request.getParameter("endDate");
        String startDateString = request.getParameter("startDate");

        if (!StringUtils.isNullOrEmpty(startDateString)) {
            startDate = LocalDate.parse(startDateString);
        }

        if (!StringUtils.isNullOrEmpty(endDateString)) {
            endDate = LocalDate.parse(endDateString);
        }

        BoardDAO boardDAO = new BoardDAO();

        int currentPage = 1;
        int pageSize = 10;
        if (request.getParameter("page") != null) {
            currentPage = Integer.parseInt(request.getParameter("page"));
        }

        List<Board> boards = boardDAO.searchBoards(startDate, endDate, category, search, currentPage, pageSize);

        int totalCount = boardDAO.countBoards(startDate, endDate, category, search);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
    %>
    <thead class="text-center">
    <tr>
        <th>카테고리</th>
        <th>제목</th>
        <th>작성자</th>
        <th>조회수</th>
        <th>등록일시</th>
        <th>수정일시</th>
    </tr>
    </thead>

    <div>
        <p> 총 <%= totalCount %>건 </p>
    </div>

    <% for (Board board : boards) { %>
    <tr>
        <td><%= CategoryDAO.getCategoryNameById(board.getCategoryId()) %>
        </td>

        <td>
            <%
                String title = board.getTitle();
                if (title.length() > 80) {
                    title = title.substring(0, 80) + "...";
                }
            %>
            <a href="view.jsp?id=<%= board.getBoardId() %>"><%=title%>
            </a>

        </td>
        <td><%= board.getWriter() %>
        </td>
        <td><%= board.getVisitCount() %>
        </td>
        <td><%= board.getCreatedAt() %>
        </td>
        <%
            String modifiedAt = board.getModifiedAt();
            if (board.getModifiedAt().equals(board.getCreatedAt())) {
                modifiedAt = "-";
            }
        %>
        <td><%= modifiedAt %>
        </td>
    </tr>
    <% } %>
    </table>

    <%-- 페이징 버튼 --%>
    <div class="d-flex justify-content-between">
        <div class="text-center mx-auto">

            <% if (currentPage > 1) { %>
            <a href="list.jsp?page=<%= currentPage - 1 %>&category=<%= category %>&search=<%= search %>&startDate=<%= startDate %>&endDate=<%= endDate %>">&lt;&nbsp;</a>
            <a href="list.jsp?page=1&category=<%= category %>&search=<%= search %>&startDate=<%= startDate %>&endDate=<%= endDate %>"><<&nbsp;</a>
            <% } %>

            <% for (int i = 1; i <= totalPages; i++) { %>
            <% if (i == currentPage) { %>
            <strong><%= i %>
            </strong>
            <% } else { %>
            <a href="list.jsp?page=<%= i %>&category=<%= category %>&search=<%= search %>&startDate=<%= startDate %>&endDate=<%= endDate %>"><%= i %>
            </a>
            <% } %>
            <% } %>

            <% if (currentPage < totalPages) { %>
            <a href="list.jsp?page=<%= currentPage + 1 %>&category=<%= category %>&search=<%= search %>&startDate=<%= startDate %>&endDate=<%= endDate %>">&nbsp;&gt;</a>
            <% } %>
            <a href="list.jsp?page=<%= totalPages %>&category=<%= category %>&search=<%= search %>&startDate=<%= startDate %>&endDate=<%= endDate %>">&nbsp;>></a>
        </div>
        <a href="write.jsp" class="btn btn-primary">등록</a>
    </div>
</div>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>