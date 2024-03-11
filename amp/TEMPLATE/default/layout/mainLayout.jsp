<%@ page contentType="text/html; charset=UTF-8" %> 

<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org" prefix="digi" %>

<html>
<digi:base />
<head>

<title>SITE : <tiles:getAsString name="title"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/global.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#ffffcc">

<table border="1" width="100%" height="100%" cellpadding="5" bordercolor="#000000" style="border-collapse: collapse;">
<tr>
   <td width="150" valign="top" >
   <digi:insert attribute="left" />
   </td>
   <td width="85%" valign="top">
   <digi:insert attribute="body" />
   </td>
</tr>
<tr>
   <td colspan="2" height="10%" >
   		<digi:insert attribute="footer" />
   </td>
</tr>

</table>

</body>
</html>