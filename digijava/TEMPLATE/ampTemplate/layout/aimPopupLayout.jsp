<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<html>
<digi:base />
<link href="css/global.css" rel="stylesheet" type="text/css">
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<head>
<title><tiles:getAsString name="title"/></title>
</head>
<body bgcolor="#ffffff" onload="load()" onunload="unload()">
<digi:insert attribute="body" />
</body>
</html>
