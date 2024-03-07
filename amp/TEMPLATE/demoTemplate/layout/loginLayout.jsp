<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>
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
