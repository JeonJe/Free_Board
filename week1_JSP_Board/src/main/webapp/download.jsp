<%--
  Created by IntelliJ IDEA.
  User: premise
  Date: 2023/05/19
  Time: 5:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.URLConnection" %>

<%
    String fileName = request.getParameter("fileName");
//    String sDownloadPath = "/Users/premise/Desktop/github/Java/ebrain/eb-study-templates-1week/build/libs/exploded/eb-study-template-1week-1.0-SNAPSHOT.war/upload/"; // 실제 파일이 위치한 경로
    String sDownloadPath = "/Users/premise/Desktop/github/Java/ebrain/upload"; // 실제 파일이 위치한 경로
    String sFilePath = sDownloadPath + fileName;

    try {
        byte b[] = new byte[4096];

        FileInputStream in = new FileInputStream(sFilePath);
        String sMimeType = URLConnection.guessContentTypeFromName(sFilePath);
        if (sMimeType == null) sMimeType = "application/octet-stream";
        response.setContentType(sMimeType);

        String sEncoding = new String(fileName.getBytes("utf-8"), "8859_1");
        response.setHeader("Content-Disposition", "attachment; filename= " + sEncoding);
        ServletOutputStream out2 = response.getOutputStream();
        int numRead;

        while ((numRead = in.read(b, 0, b.length)) != -1) {
            out2.write(b, 0, numRead);
//            System.out.println(numRead);
        }

        out2.flush();
        out2.close();
        in.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
%>

