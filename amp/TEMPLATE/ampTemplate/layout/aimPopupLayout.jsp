<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<html>
<digi:base />
<link href="css/global.css" rel="stylesheet" type="text/css">
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/EnterHitBinder.js'/>" >.</script>

<head>
			<%
				String title=(String)((org.apache.struts.tiles.ComponentContext) request.getAttribute("org.apache.struts.taglib.tiles.CompContext")).getAttribute("title");
				String key=(title.replaceAll(" ",""));
			%>
	<TITLE>
		
		<logic:present name="extraTitle" scope="request">
				<bean:define id="extTitle" name="extraTitle" scope="request" type="java.lang.String" />
				<c:set var="key">aim:pagetitle:<%=key%><%=extTitle%></c:set>
				<digi:trn key="aim:pagetitle:amp">AMP </digi:trn>   
				<digi:trn key="${key}">
					<%=title%> <%=extTitle%>
				</digi:trn>
		</logic:present>
	
		<logic:notPresent name="extraTitle" scope="request">
					
					<c:set var="key">aim:pagetitle:<%=key%></c:set>
					<digi:trn key="aim:pagetitle:amp">AMP </digi:trn> 
					<digi:trn key="${key}"><%=title%></digi:trn>
		</logic:notPresent>
		
	
	</TITLE>
	
</head>
<body bgcolor="#ffffff">
<digi:insert attribute="body" />
</body>
</html>
