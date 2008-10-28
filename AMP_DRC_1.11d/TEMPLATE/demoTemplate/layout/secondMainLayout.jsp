<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<html>
<link href="/digijava/TEMPLATE/demoSite/css/global.css" rel="stylesheet" type="text/css">
<head>
<title>DemoSite secondary layout: <tiles:getAsString name="title"/></title>
</head>
<body bgcolor="#ffffff">

<table border="0" width="100%" height="100%" cellpadding="0" cellspacing="0">
<tr>
   <td height="20%" width="100%">
     <digi:insert attribute="header" />
   </td>
</tr>
<tr>
   <td width="85%" align="center">
   <digi:insert attribute="body" />
   </td>
</tr>
<tr>
   <td height="10%">
   <digi:insert attribute="footer" />
   </td>
</tr>

</table>

</body>
</html>
