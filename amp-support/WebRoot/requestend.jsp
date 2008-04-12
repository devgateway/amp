<%@ taglib prefix="html" uri="/struts-tags"%>
<html>
<head>
<title>A<html:text name="%{getText('page.tittle')}"/></title>
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
<html:form action="RequestEnd.action" validate="true">
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
			<html:text name="%{getText('succes.mail.msg_1')}"/><br>
			<html:text name="%{getText('succes.mail.msg_2')}"/><br>
			<html:text name="%{getText('succes.mail.msg_3')}"/>	  
		</td>
	</tr>
	<tr>
		<td width="259" height="87">&nbsp;</td>
	    <td width="326" align="right" valign="bottom" class="Textk">
	    	<a href="<html:url action="login"/>" style="text-decoration: none">
	    	<html:text name="%{getText('label.logout')}"/>&nbsp;</a>
	  </td>
	    <td width="193" align="right" valign="bottom" class="Textk">
	    	<a href="<html:url action="showRequestForm"/>" style="text-decoration: none">
	    	<html:text name="%{getText('label.submit.another')}"/>&nbsp;</a>
	    	&nbsp;
      </td>
	</tr>
</table>
</html:form>
</body>
</html>
