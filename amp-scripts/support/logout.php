<?php 
session_start();
if (isset($_SESSION['bla']) && isset($_SESSION['bla2'])) {
session_unset();
session_destroy();
}
else {
    header("location:index.php");
}
?>

<html>
<head>
<title>AMP Support Site </title>
<link href="Styles/common.css" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
</head>
<body>
<form namespace="" id="RequestEnd" action="RequestEnd.action" method="post">
<br>
<br>
<br>
<br>

<table width="780" border="0" align="center" cellpadding="0" cellspacing="0" class="table">
	<tr>
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="3" align="center" class="Text">
			You have been logged out.  
		</td>
	</tr>
</table>
</form>

</body>
</html>
