<%--
  Created by IntelliJ IDEA.
  User: brianbrix
  Date: 03/04/2024
  Time: 12:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Data Importer Form</title>
</head>
<body>
<h2>Data Importer Form</h2>
<form action="${pageContext.request.contextPath}/aim/dataImporter.do" method="post">
  <label for="file">Select File:</label>
  <input type="file" id="file" name="file" accept=".csv, .xls, .xlsx" required>
  <br><br>
  <input type="submit" value="Submit">
</form>
</body>
</html>
