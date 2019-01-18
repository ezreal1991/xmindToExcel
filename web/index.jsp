<%-- Created by IntelliJ IDEA. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<form action="${pageContext.request.contextPath}/upload.do" enctype="multipart/form-data"
      method="post">
    上传文件1：<input type="file" name="file1"><br/>
    <input type="submit" value="提交">

</form>
</body>
</html>