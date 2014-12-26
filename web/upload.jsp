<%--
  Created by IntelliJ IDEA.
  User: vzhang
  Date: 12/26/14
  Time: 11:39 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
  <h1>Upload a file extract text content and metadata</h1>
  <form id="file1" enctype="multipart/form-data" method="post" action="upload.do">
    <input type="file" name="file1"/>
    <input type="submit" value="上传"/>
  </form>
</body>
</html>
