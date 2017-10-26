<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<digi:base />
<link href="css/global.css" rel="stylesheet" type="text/css">
<digi:ref href="css/style.css" rel="stylesheet" type="text/css" />

<head>
<title>DEMOSITE TEMPLATE : <tiles:getAsString name="title"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body bgcolor="#ffffff" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">

<table border="0" width="100%" height="100%" cellpadding="0" cellspacing="0">
<tr>
   <td colspan="2" height="1" width="100%" >
     <digi:insert attribute="header" />
   </td>
</tr>
<%--
<tr>
	<td colspan="2" height="5">
		<digi:img src="images/ui/spacer.gif" height="5" border="1"/>
	</td>
</tr>
--%>
<tr>
   <td width="150" height="90%" valign="top" >
   <digi:insert attribute="left" />
   </td>
   <td width="90%" height="90%" valign="top">
   <digi:insert attribute="body" />
   </td>
</tr>
<tr>
   <td width="100%" colspan="2" height="10%" >
   		<digi:insert attribute="footer" />
   </td>
</tr>

</table>

</body>
</html>
