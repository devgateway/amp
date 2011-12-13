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

<script type="text/javascript" src="<digi:file src="script/yui/yahoo-dom-event.js"/>"></script>
<script type="text/javascript">
	function setStepNumber( status ) {
		var loadingStepEl	= document.getElementById("loading_step");
		if (loadingStepEl != null)
			loadingStepEl.innerHTML=status;
	}
	function removeLoadingMsg() {
		document.getElementById('loading_div').style.display="none";
	}
	var myYahoo 	= null;
	try {
		myYahoo = YAHOO;
	}
	catch (e) {
		myYahoo = YAHOOAmp;
	}
	myYahoo.util.Event.on(window, "load", removeLoadingMsg );
</script>

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
				<digi:trn><%=title%></digi:trn> ${extTitle}
		</logic:present>
	
		<logic:notPresent name="extraTitle" scope="request">
					
					<c:set var="key">aim:pagetitle:<%=key%></c:set>
					<digi:trn key="aim:pagetitle:amp">AMP </digi:trn> 
					<digi:trn key="${key}"><%=title%></digi:trn>
		</logic:notPresent>
		
	
	</TITLE>
	
</head>
<body bgcolor="#ffffff">
	<c:if test="${param.widget=='false' || param.widget==null}">
	<div style='height: 20px; left: 45%; position: absolute; text-align: center; top: 0%;width: 230px;padding: 5px;background-color:#27415F;font-family: arial; font-size: 14px;text-align: center;font-weight:bold;color: white;' id="loading_div">
		<digi:trn>Loading step</digi:trn> <span id="loading_step">2/3</span>. <digi:trn>Please wait</digi:trn> ...
	</div>
	</c:if>
<digi:insert attribute="body" />
</body>
</html>
