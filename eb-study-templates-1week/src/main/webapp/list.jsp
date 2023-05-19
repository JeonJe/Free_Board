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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>자유 게시판</title>

</head>
<body>
<h1>자유 게시판 목록</h1>

<form action="list.jsp" method="get">

    <label for="startDate">등록일:</label>
    <input type="date" id="startDate" name="startDate" value="<%= (request.getParameter("startDate") != null) ? request.getParameter("startDate") : LocalDate.now().minusYears(1) %>">
    <label for="endDate"> ~ </label>
    <input type="date" id="endDate" name="endDate" value="<%= (request.getParameter("endDate") != null) ? request.getParameter("endDate") : LocalDate.now() %>">


    <select id="category" name="category">
        <option value="all">전체 카테고리</option>
        <option value="JAVA">JAVA</option>
        <option value="Database">Database</option>
        <option value="Javascript">Javascript</option>
    </select>

    <label for="search">검색어:</label>
    <input type="text" id="search" name="search">

    <input type="submit" value="검색">
</form>

<table>
    <tr>
        <th>카테고리</th>
        <th>제목</th>
        <th>작성자</th>
        <th>조회수</th>
        <th>등록일시</th>
        <th>수정일시</th>
    </tr>
    
    <%
        String search = request.getParameter("search");
        String category = request.getParameter("category");
        if (category == null && search == null){
            category = "all";
            search = "";
        }

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(1);

        String endDateString = request.getParameter("endDate");
        String startDateString = request.getParameter("startDate");

        if (startDateString != null && !startDateString.isEmpty()) {
            startDate = LocalDate.parse(startDateString);
        }

        if (endDateString != null && !endDateString.isEmpty()) {
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

    <div>
        <p> 총 <%= totalCount %>건 </p>
    </div>

    <% for (Board board : boards) { %>
    <tr>
        <td><%= board.getCategory() %>
        </td>
        <%--    attachment 추가 --%>
        <td><a href="view.jsp?id=<%= board.getBoardId() %>"><%= board.getTitle() %>
        </a></td>
        <td><%= board.getWriter() %>
        </td>
        <td><%= board.getVisitCount() %>
        </td>
        <td><%= board.getCreatedAt() %>
        </td>
        <td><%= board.getModifiedAt() %>
        </td>
    </tr>
    <% } %>
</table>

<%-- 페이징 버튼 --%>

<div>
    <% if (currentPage > 1) { %>
    <a href="list.jsp?page=<%= currentPage - 1 %>&category=<%= category %>&search=<%= search %>&startDate=<%= startDate %>&endDate=<%= endDate %>">이전</a>
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
    <a href="list.jsp?page=<%= currentPage + 1 %>&category=<%= category %>&search=<%= search %>&startDate=<%= startDate %>&endDate=<%= endDate %>">다음</a>
    <% } %>

</div>


<a href="write.jsp">등록</a>
</body>
</html>