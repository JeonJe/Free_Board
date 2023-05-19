<%@ page import="comment.CommentDAO" %>
<%@ page import="comment.Comment" %><%--
  Created by IntelliJ IDEA.
  User: premise
  Date: 2023/05/18
  Time: 11:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    request.setCharacterEncoding("UTF-8");
    String content = request.getParameter("content");
    int id =  Integer.parseInt(request.getParameter("id"));

    Comment comment = new Comment();
    comment.setContent(content);
    comment.setBoardId(id);
    comment.setWriter("-");

    CommentDAO commentDAO = new CommentDAO();
    commentDAO.save(comment);

    String url = "view.jsp?id=" + id;
    response.sendRedirect(url);

%>

<script>
    // 현재 페이지를 새로고침
    location.reload();
</script>
