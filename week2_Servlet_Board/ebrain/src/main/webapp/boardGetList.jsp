<%--
  Created by IntelliJ IDEA.
  User: premise
  Date: 2023/05/17
  Time: 1:18 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="java.time.LocalDate" %>
<%@ page import="board.Board" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="category.CategoryDAO" %>
<%@ page import="category.Category" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>자유 게시판</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<%
    //Get Searching Condition
    Map<String, Object> attributes = (Map<String, Object>) request.getAttribute("attributes");
    List<Board> boards = (List<Board>) attributes.get("boards");
    int currentPage = (int) attributes.get("currentPage");
    int category = (int) attributes.get("category");
    String searchText = (String) attributes.get("searchText");
    String startDate = (String) attributes.get("startDate");
    String endDate = (String) attributes.get("endDate");
    int totalPages = (int) attributes.get("totalPages");
    List<Category> categories = (List<Category>) attributes.get("categories");
%>

<div class="container">
    <h1 class="my-4">자유 게시판 목록</h1>€
    <%-- Form of the Searching Condition --%>
    <form action="list" method="get" class="form-inline mb-4">
        <input type="hidden" name="action" value="list">
        <label for="startDate" class="mr-2">등록일:</label>
        <input type="date" id="startDate" name="startDate" class="form-control mr-2"
               value="<%= (request.getParameter("startDate") != null) ? request.getParameter("startDate") : LocalDate.now().minusYears(1) %>">
        <label for="endDate" class="mr-2"> ~ </label>
        <input type="date" id="endDate" name="endDate" class="form-control mr-2"
               value="<%= (request.getParameter("endDate") != null) ? request.getParameter("endDate") : LocalDate.now() %>">

        <select id="category" name="category" class="form-control mr-2">
            <option value="0" <%= (request.getParameter("category") != null && request.getParameter("category").equals("0")) ? "selected" : "" %>>
                전체 카테고리
            </option>

            <% for (Category categoryItem : categories) { %>
            <option value="<%= categoryItem.getCategoryId() %>" <%=
            String.valueOf(categoryItem.getCategoryId()).equals(request.getParameter("category")) ? "selected" : "" %>><%= categoryItem.getCategoryName() %>
            </option>
            <% } %>

            <label for="searchText" class="mr-2">검색어:</label>

            <input type="text" id="searchText" name="searchText" class="form-control mr-2" placeholder="카테고리 + 제목 + 내용"
                   value="<%= (searchText != null ) ? searchText : "" %>">
        </select>

        <input type="submit" value="검색" class="btn btn-primary">
    </form>

    <%-- List of the posts   --%>
    <table class="table table-striped text-center">
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
            <p> 총 <%= boards.size() %>건 </p>
        </div>

        <tbody>

        <% if (boards != null) { %>
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
                <a href="view?action=view&id=<%= board.getBoardId() %>&page=<%= currentPage %>&category=<%= category %>&searchText=<%= searchText %>&startDate=<%= startDate %>&endDate=<%= endDate %>"><%= title %>
                </a>
            </td>
            <td><%= board.getWriter() %>
            </td>
            <td><%= board.getVisitCount() %>
            </td>
            <td><%= board.getCreatedAt() %>
            </td>
            <% String modifiedAt = board.getModifiedAt();
                if (board.getModifiedAt().equals(board.getCreatedAt())) {
                    modifiedAt = "-";
                }
            %>
            <td><%= modifiedAt %>
            </td>
        </tr>
        <% } %>
        <% } %>
        </tbody>
    </table>

    <%-- pagination --%>
    <div class="d-flex justify-content-between">
        <div class="text-center mx-auto">
            <% if (currentPage > 1) { %>
            <a href="list?action=list&page=<%= currentPage - 1 %>&category=<%= category %>&searchText=<%= searchText %>&startDate=<%= startDate %>&endDate=<%= endDate %>">&lt;&nbsp;</a>
            <a href="list?action=list&page=1&category=<%= category %>&searchText=<%= searchText %>&startDate=<%= startDate %>&endDate=<%= endDate %>"><<&nbsp;</a>
            <% } %>

            <% for (int i = 1; i <= totalPages; i++) { %>
            <% if (i == currentPage) { %>
            <strong><%= i %>
            </strong>
            <% } else { %>
            <a href="list?action=list&page=<%= i %>&category=<%= category %>&searchText=<%= searchText %>&startDate=<%= startDate %>&endDate=<%= endDate %>"><%= i %>
            </a>
            <% } %>
            <% } %>

            <% if (currentPage < totalPages) { %>
            <a href="list?action=list&page=<%= currentPage + 1 %>&category=<%= category %>&searchText=<%= searchText %>&startDate=<%= startDate %>&endDate=<%= endDate %>">&nbsp;&gt;</a>
            <% } %>
            <a href="list?action=list&page=<%= totalPages %>&category=<%= category %>&searchText=<%= searchText %>&startDate=<%= startDate %>&endDate=<%= endDate %>">&nbsp;>></a>
        </div>
        <a href="write" class="btn btn-primary">등록</a>
    </div>
</div>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>