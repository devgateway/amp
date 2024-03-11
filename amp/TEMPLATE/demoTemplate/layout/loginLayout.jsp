<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<html>
<head>
<link href="/digijava/TEMPLATE/demoSite/css/global.css" rel="stylesheet" type="text/css">
<title><tiles:getAsString name="title"/></title>
</head>
<body bgcolor="#ffffff">

<table border="1" width="100%" height="100%" cellpadding="5">
<tr>
   <td height="20%" width="100%" >
     <digi:insert attribute="header" />
   </td>
</tr>
<tr>
   <td width="100%" >
   <digi:insert attribute="body" />
   </td>
</tr>
<tr>
   <td height="10%" >
   <digi:insert attribute="footer" />
   </td>
</tr>

</table>

</body>
</html>
