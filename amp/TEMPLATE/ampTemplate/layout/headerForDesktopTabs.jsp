<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/fn.tld" prefix="fn" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/fieldVisibility.tld" prefix="field" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/featureVisibility.tld" prefix="feature" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/moduleVisibility.tld" prefix="module" %>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html" %>

<!-- Stylesheet of AMP -->
<!--[if IE 6]><link href='/TEMPLATE/ampTemplate/css_2/amp_ie_hacks_6.css' rel='stylesheet' type='text/css'><![endif]-->
<!--[if IE 7]><link href='/TEMPLATE/ampTemplate/css_2/amp_ie_hacks_7.css' rel='stylesheet' type='text/css'><![endif]-->
<!--[if IE 8]><link href='/TEMPLATE/ampTemplate/css_2/amp_ie_hacks_8.css' rel='stylesheet' type='text/css'><![endif]-->
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
	
<script type="text/javascript">
	var YAHOOAmp 	= YAHOO;
</script>  
        
<%org.digijava.kernel.request.SiteDomain siteDomain = null;%>
<logic:present name="currentMember" scope="session">
	<script language=javascript>
	function showUserProfile(id){
		<digi:context name="information" property="/aim/userProfile.do" />
		//openURLinWindow("<%=information%>~edit=true~id="+id,480, 350);
		var param = "~edit=true~id="+id;
		previewWorkspaceframe('/aim/default/userProfile.do',param);
	}
	function canExit() {
			if (typeof quitRnot1 == 'function') {
				return quitRnot1('${msg}');
			} else {
				return true;
			}

		}
	</script>
</logic:present>

<!-- Prevent Skype Highlighter -->
<META name="SKYPE_TOOLBAR" content="SKYPE_TOOLBAR_PARSER_COMPATIBLE" />
<meta http-equiv="X-UA-Compatible" content="chrome=1; IE=8">
<style>
#components_dots table tr td {
	font-size: 11px;
}

table tr td {
	font-size: 11px;
}
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


<script type="text/javascript">
	function selectwkspace(id){
		var url = "/selectTeam.do?id="+id;
		document.location.href=url;
	}
</script>