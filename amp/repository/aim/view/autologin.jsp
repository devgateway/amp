//Agregar aca un control de si esta pagina esta activada.
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Autologin</title>
</head>
<body>
<form action="/j_acegi_security_check" method="post"">
	<input type="hidden" name="j_username" value="<%=request.getParameter("user")%>"/>
	<input type="hidden" name="j_password" value="<%=request.getParameter("password")%>" />
	<input type="hidden" name="j_autoLogin" value="true" />

	<input type="submit" />
</form>
</body>
</html>