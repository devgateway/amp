<html>
<head>

<script type="text/javascript">
function delayer(){
    document.myAutoForm.submit();
}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>AMP Auto Login</title>
</head>
<body onLoad="setTimeout('delayer()', 500)">
<form action="/j_spring_security_check" method="post" name="myAutoForm">
	<input type="hidden" name="j_username" value="<%=request.getParameter("user")%>"/>
	<input type="hidden" name="j_password" value="<%=request.getParameter("password")%>" />
	<input type="hidden" name="j_autoLogin" value="true" />
	<input type="hidden" name="j_autoWorkspaceId" value="<%=request.getParameter("workspaceId")%>" />
</form>
</body>
</html> 