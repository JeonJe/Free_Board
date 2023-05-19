<%@ page import="com.study.connection.ConnectionTest" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Goodbye World!" %>
</h1>
<br/>
<a href="hello-servlet">Hello Servlet</a>


<%
    ConnectionTest t = new ConnectionTest();
    out.println(t.getConnection());
    String redirectURL = "list.jsp";
    response.sendRedirect(redirectURL);
%>

</body>
</html>
