<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>자유 게시판</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <h1 class="my-4">자유 게시판 목록</h1>

    <%-- Form of the Searching Condition --%>
    <form action="list" method="get" class="form-inline mb-4">
        <input type="hidden" name="action" value="list">
        <input type="date" id="startDate" name="startDate" class="form-control mr-2"
               value="${searchCondition.startDate != null ? searchCondition.startDate : LocalDate.now().minusYears(1)}">

        <input type="date" id="endDate" name="endDate" class="form-control mr-2"
               value="${searchCondition.endDate != null ? searchCondition.endDate : LocalDate.now()}">

        <select id="category" name="category" class="form-control mr-2">
            <option value="0" ${searchCondition.category != null && searchCondition.category == 0 ? 'selected' : ''}>
                전체 카테고리
            </option>
            <c:if test="${not empty categories}">
                <c:forEach items="${categories}" var="categoryItem">
                    <option value="${categoryItem.categoryId}" ${categoryItem.categoryId == searchCondition['category'] ? 'selected' : ''}>
                            ${categoryItem.categoryName}
                    </option>
                </c:forEach>
            </c:if>
        </select>

        <input type="text" id="searchText" name="searchText" class="form-control mr-2" placeholder="카테고리 + 제목 + 내용"
               value="${searchCondition.searchText != null ? searchCondition.searchText : ''}">

        <input type="submit" value="검색" class="btn btn-primary">
    </form>

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
        <tbody>
        <c:if test="${not empty searchBoards}">
            <c:forEach items="${searchBoards}" var="boardItem">
                <tr>
                    <td>${boardItem.categoryId}</td>
                    <td><a href="view?id=${boardItem.boardId}">${boardItem.title}</a></td>
                    <td>${boardItem.writer}</td>
                    <td>${boardItem.visitCount}</td>
                    <td>${boardItem.createdAt}</td>
                    <td>${boardItem.modifiedAt}</td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>


    <!-- pagination -->
    <div class="d-flex justify-content-between">
        <div class="text-center mx-auto">
            <!-- Set currentPage to 1 if it's null -->
            <c:set var="currentPage" value="${empty searchCondition.currentPage ? 1 : searchCondition.currentPage}"/>
            <c:if test="${searchCondition.currentPage > 1}">
                <a href="list?action=list&page=${searchCondition.currentPage - 1}&category=${searchCondition.category}&searchText=${searchCondition.searchText}&startDate=${searchCondition.startDate}&endDate=${searchCondition.endDate}">&lt;&nbsp;</a>
                <a href="list?action=list&page=1&category=${searchCondition.category}&searchText=${searchCondition.searchText}&startDate=${searchCondition.startDate}&endDate=${searchCondition.endDate}"><<&nbsp;</a>
            </c:if>

            <c:forEach var="i" begin="1" end="${totalPages}">
                <c:choose>
                    <c:when test="${i == searchCondition.currentPage}">
                        <strong>${i}</strong>
                    </c:when>
                    <c:otherwise>
                        <a href="list?action=list&page=${i}&category=${searchCondition.category}&searchText=${searchCondition.searchText}&startDate=${searchCondition.startDate}&endDate=${searchCondition.endDate}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${searchCondition.currentPage < totalPages}">
                <a href="list?action=list&page=${searchCondition.currentPage + 1}&category=${searchCondition.category}&searchText=${searchCondition.searchText}&startDate=${searchCondition.startDate}&endDate=${searchCondition.endDate}">&nbsp;&gt;</a>
                <a href="list?action=list&page=${totalPages}&category=${searchCondition.category}&searchText=${searchCondition.searchText}&startDate=${searchCondition.startDate}&endDate=${searchCondition.endDate}">&nbsp;>></a>
            </c:if>
        </div>
        <a href="write?action=write&&page=${searchCondition.page}&category=${searchCondition.category}&searchText=${searchCondition.searchText}&startDate=${searchCondition.startDate}&endDate=${searchCondition.endDate}" class="btn btn-primary">등록</a>
    </div>
</div>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>