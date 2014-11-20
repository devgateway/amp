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
	<%@include file="title.jsp"%>	
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
