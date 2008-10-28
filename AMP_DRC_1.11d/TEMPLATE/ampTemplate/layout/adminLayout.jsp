<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<digi:base />
<%--<link href="css/global.css" rel="stylesheet" type="text/css">--%>
<link rel="stylesheet" href="<digi:file src="module/admin/css/admin.css"/>">
<head>
<title>DEMOSITE TEMPLATE : <tiles:getAsString name="title"/></title>
</head>
<body bgcolor="#ffffff" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
<table border="1" width="100%" height="100%" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#9A9A9A">
<tr>
	<td height="1%" width="100%">
		<digi:insert attribute="header"/>
	</td>
</tr>
<tr>
   <td valign="top" height="99%">
   	<table border="1" width="100%" height="100%" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#9A9A9A">
		<tr>
			<td align="top" height="99%">
				<digi:insert attribute="left"/>
			</td>
			<td valign="top" height="99%" width="90%">
				<digi:insert attribute="body"/>
			</td>
		</tr>
	</table>
   </td>
</tr>
</table>
</body>
</html>
