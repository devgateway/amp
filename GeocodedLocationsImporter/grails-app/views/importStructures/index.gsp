<%--
  Created by IntelliJ IDEA.
  User: Gabriel
  Date: 17/09/2015
  Time: 01:25 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Import DRC Structures</title>
</head>

<body>
<h2>Import Structures</h2>
<g:uploadForm name="myUpload" action="importFileStructures">
    <input type="file" name="myFile"/>
    <g:submitButton name="submit"/>
</g:uploadForm>
<hr>
</body>
</html>