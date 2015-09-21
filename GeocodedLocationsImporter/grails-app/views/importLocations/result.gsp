<%--
  Created by IntelliJ IDEA.
  User: Gabriel
  Date: 17/09/2015
  Time: 02:07 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>

    <style type="text/css">
    .sql {
        font-size: 0.85em;
    }

    .comment {
        color: green;
    }

    .error {
        color: red;
    }
    </style>
</head>

<body>
<h2>Result:</h2><br>
<g:each in="${generatedSQL}" var="sql">
    <span class="sql ${(sql.toString().startsWith("--") || sql.toString().startsWith("/")) ? 'comment' : ''}">${sql}</span>
    <br>
</g:each>
<hr>

<g:if test="${errors}">
    <h2>Errors:</h2><br>
    <g:each in="${errors}" var="error">
        <span class="sql error">${error}</span>
        <br>
    </g:each>
    <hr>
</g:if>
<g:link action="index">Back...</g:link>
</body>
</html>