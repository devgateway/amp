<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/util.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="/repository/aim/view/scripts/common.js"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/ajaxtabs/ajaxtabs.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/ajax.js"/>"></script>

<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicContent.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-modalMessage.js"/>"></script>

	<!-- Stylesheet of AMP -->
	<digi:ref href="/TEMPLATE/ampTemplate/css_2/amp.css" type="text/css" rel="stylesheet" />
	<digi:ref href="/TEMPLATE/ampTemplate/css_2/yui_tabs.css" type="text/css" rel="stylesheet" />
	<digi:ref href="/TEMPLATE/ampTemplate/css_2/yui_datatable.css" type="text/css" rel="stylesheet" />
	<digi:ref href="/TEMPLATE/ampTemplate/css_2/desktop_yui_tabs.css" type="text/css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/tabview-core.css"> 
	
	
		 
    <!-- Individual YUI CSS files --> 
	<link rel="stylesheet" type="text/css" href="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css"/>"> 
	<digi:ref href="/TEMPLATE/ampTemplate/css_2/yui_popins.css" type="text/css" rel="stylesheet" />
	
	 
	<!-- Individual YUI JS files --> 
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"/>"></script> 
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/dragdrop/dragdrop-min.js"/>"></script> 
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"/>"></script> 
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"/>"></script> 
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"/>"></script> 
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/menu/menu-min.js"/>"></script>
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/dom/dom-min.js"/>"></script>
	
	<!-- Jquery Base Library -->
<script type="text/javascript">
(function(){
	 var newscript = document.createElement('script');
     newscript.type = 'text/javascript';
     newscript.async = true;
     newscript.src = '<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>';
     if(jQuery === undefined && $ === undefined){
  		(document.getElementsByTagName('head')[0]||document.getElementsByTagName('body')[0]).appendChild(newscript);
     }
})();
</script>  
<script type="text/javascript">
	var YAHOOAmp 	= YAHOO;
</script>  
        
<%org.digijava.kernel.request.SiteDomain siteDomain = null;%>
<logic:present name="currentMember" scope="session">
	<script language=javascript>
	function showUserProfile(id){
		<digi:context name="information" property="/aim/userProfile.do" />
		//openURLinWindow("<%= information %>~edit=true~id="+id,480, 350);
		var param = "~edit=true~id="+id;
		previewWorkspaceframe('/aim/default/userProfile.do',param);
	}
	function canExit(){
	    if(typeof quitRnot1 == 'function') {
	        return quitRnot1('${msg}');
	    }
	    else{
	        return true;
	    }
	
	}	
</script>
</logic:present>

<!-- Prevent Skype Highlighter -->
<META name="SKYPE_TOOLBAR" content="SKYPE_TOOLBAR_PARSER_COMPATIBLE" />
<meta http-equiv="X-UA-Compatible" content="chrome=1; IE=8">
<style>
#components_dots table tr td {font-size:11px;}
table tr td {font-size:11px;}
</style>
<digi:context name="displayFlag" property="context/aim/default/displayFlag.do" />
<c:set var="message">
<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
	${fn:replace(message,quote,escapedQuote)}
</c:set>

 <logic:notEmpty name="currentMember" scope="session">
 	<bean:define id="teamMember" name="currentMember" scope="session" type="org.digijava.module.aim.helper.TeamMember" />
 </logic:notEmpty>
 <logic:empty name="currentMember" scope="session">
	<logic:notEmpty name="currentUser" scope="session">
 		<bean:define id="userLogged" name="currentUser" scope="session" type="org.digijava.kernel.user.User" />
	 </logic:notEmpty>
 </logic:empty>

<%-- <jsp:include page="/TEMPLATE/ampTemplate/layout/header.jsp"/> --%>


<script type="text/javascript">
	function selectwkspace(id){
		var a;
		var url = "/selectTeam.do?id="+id;
		document.location.href=url;
	}
</script>


