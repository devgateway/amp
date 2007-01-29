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
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>

	<logic:present name="extraTitle" scope="request">
		<bean:define id="extTitle" name="extraTitle" scope="request" type="java.lang.String" />
		<TITLE>AMP<tiles:getAsString name="title"/> <%=extTitle%></TITLE>
	</logic:present>
	<logic:notPresent name="extraTitle" scope="request">
		<title><tiles:getAsString name="title"/></title>
	</logic:notPresent>

		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">		
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
		<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
     	<META HTTP-EQUIV="EXPIRES" CONTENT="0">	
	
</head>
<body bgcolor="#ffffff" body onLoad="init()" onunload="unload()">
<script>
 var ld=(document.all);
 var ns4=document.layers;
 var ns6=document.getElementById&&!document.all;
 var ie4=document.all;
 if (ns4)
 ld=document.loading;
 else if (ns6)
 ld=document.getElementById("loading").style; 
 else if (ie4)
 ld=document.all.loading.style;
 
 function init()
 {
 		if(ns4){	ld.visibility="hidden";		load();	}
	 	else if (ns6||ie4) {	ld.display="none";	load();	}
 }
 </script>
<digi:insert attribute="body" />
</body>
</html>
