<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<html>
<digi:base />
<link href="css/global.css" rel="stylesheet" type="text/css">
<head>
<title><tiles:getAsString name="title"/></title>
</head>
<body bgcolor="#ffffff">
<digi:insert attribute="body" />
</body>
</html>
