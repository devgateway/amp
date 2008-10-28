<html>
<head>

<script type="text/javascript">
function delayer(){
    document.myAutoForm.submit();
}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><digi:trn key="aim:autologinamp"><b>AMP Auto Login</b></digi:trn></title>
</head>
<body onLoad="setTimeout('delayer()', 500)">
<form action="/j_acegi_security_check" method="post" name="myAutoForm">
	<input type="hidden" name="j_username" value="<%=request.getParameter("user")%>"/>
	<input type="hidden" name="j_password" value="<%=request.getParameter("password")%>" />
	<input type="hidden" name="j_autoLogin" value="true" />
</form>
</body>
</html> 