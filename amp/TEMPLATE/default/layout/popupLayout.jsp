<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>
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
