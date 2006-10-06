<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<html>
<digi:base />
<link href="css/global.css" rel="stylesheet" type="text/css">
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<head>
	<logic:present name="extraTitle" scope="request">
		<bean:define id="extTitle" name="extraTitle" scope="request" type="java.lang.String" />
		<TITLE>AMP : <%=extTitle%></TITLE>
	</logic:present>
	<logic:notPresent name="extraTitle" scope="request">
		<title><tiles:getAsString name="title"/></title>
	</logic:notPresent>
</head>
<body bgcolor="#ffffff" onload="load()" onunload="unload()">
<digi:insert attribute="body" />
</body>
</html>
