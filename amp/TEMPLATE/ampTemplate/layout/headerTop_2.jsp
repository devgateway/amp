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
<script language="JavaScript" type="text/javascript" src="<digi:file src="js_2/amp/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/relatedLinks.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/ajaxtabs/ajaxtabs.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/ajax.js"/>"></script>

<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicContent.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-modalMessage.js"/>"></script>

	<!-- Stylesheet of AMP -->
	<!--[if IE 6]><link href='css/amp_ie_hacks_6.css' rel='stylesheet' type='text/css'><![endif]-->
	<!--[if IE 7]><link href='css/amp_ie_hacks_7.css' rel='stylesheet' type='text/css'><![endif]-->
	<!--[if IE 8]><link href='css/amp_ie_hacks_8.css' rel='stylesheet' type='text/css'><![endif]-->
	<digi:ref href="/TEMPLATE/ampTemplate/css_2/amp.css" type="text/css" rel="stylesheet" />
	<digi:ref href="/TEMPLATE/ampTemplate/css_2/yui_tabs.css" type="text/css" rel="stylesheet" />
	<digi:ref href="/TEMPLATE/ampTemplate/css_2/yui_popins.css" type="text/css" rel="stylesheet" />
	<digi:ref href="/TEMPLATE/ampTemplate/css_2/yui_datatable.css" type="text/css" rel="stylesheet" />
	<digi:ref href="/TEMPLATE/ampTemplate/css_2/desktop_yui_tabs.css" type="text/css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/tabview-core.css"> 
	
	
		 
    <!-- Individual YUI CSS files --> 
	<link rel="stylesheet" type="text/css" href="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css"/>"> 
	
	 
	<!-- Individual YUI JS files --> 
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"/>"></script> 
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/dragdrop/dragdrop-min.js"/>"></script> 
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"/>"></script> 
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"/>"></script> 
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"/>"></script> 
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/menu/menu-min.js"/>"></script>
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/dom/dom-min.js"/>"></script>
	
	<!-- Jquery Base Library -->
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>
	
<script type="text/javascript">
	var YAHOOAmp 	= YAHOO;
</script>  
        
<%org.digijava.kernel.request.SiteDomain siteDomain = null;%>
<logic:present name="currentMember" scope="session">
	<script language=javascript>
	function showUserProfile(id){
		<digi:context name="information" property="context/module/moduleinstance/userProfile.do" />
		openURLinWindow("<%= information %>~edit=true~id="+id,480, 350);
	}
	function help(){
		 <digi:context name="rev" property="/help/help.do~blankPage=true" />
			openURLinWindow("<%=rev%>",1024,768);
		}
	function adminHelp(){
			 <digi:context name="admin" property="/help/admin/help.do~blankPage=true" />
			openURLinWindow("<%=admin%>",1024,768);
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

<!-- HEADER START -->
<div class="header">
	<div class="centering">
		<div class="logo">
			<img src="img_2/amp_logo.gif" align=left>
			<div class="amp_label">&nbsp;<digi:trn key="aim:aidManagementPlatform">Aid Management Platform (AMP)</digi:trn></div>
		</div>
		
		<!-- <logic:notEmpty name="currentMember" scope="session">
		<feature:display name="Change Workspace" module="My Desktop">
			<div class="workspace_info">
				<digi:trn key="aim:changeworkspace">Workspace</digi:trn>:
		 		 <select onchange="selectwkspace(this.value)" class="dropdwn_sm_wksp">
		 			<logic:iterate id="item"  name="USER_WORKSPACES" scope="session" type="org.digijava.module.aim.dbentity.AmpTeamMember">
						<bean:define id="team" name="item" property="ampTeam" type="org.digijava.module.aim.dbentity.AmpTeam"></bean:define>
						<logic:equal name="currentMember" property="teamId" scope="session" value="${team.ampTeamId}">
								<option selected="selected" value='<bean:write name="item" property="ampTeamMemId"/>'><bean:write name="team" property="name"/></option>
						</logic:equal>
						<logic:notEqual name="currentMember" property="teamId" scope="session" value="${team.ampTeamId}">
								<option value="<bean:write name="item" property="ampTeamMemId"/>">
									<bean:write name="team" property="name"/>
								</option>
						</logic:notEqual>
					</logic:iterate>
				</select>
		 	</div>			
		</feature:display>
		</logic:notEmpty> -->
		
		<div id="usr_menu_logged">
			<a href="javascript:showUserProfile(${teamMember.memberId})">${teamMember.memberName}</a>	
			
			<img src="img_2/top_sep.gif" class="top_sep">
			<logic:present name="ampAdmin" scope="session">
				<logic:equal name="ampAdmin" value="no">
					<logic:notEmpty name="currentMember" scope="session">
						<feature:display name="Change Workspace" module="My Desktop">
							<select onChange="selectwkspace(this.value)" class="dropdwn_sm_wksp">
								<logic:iterate id="item"  name="USER_WORKSPACES" scope="session" type="org.digijava.module.aim.dbentity.AmpTeamMember">
									<bean:define id="team" name="item" property="ampTeam" type="org.digijava.module.aim.dbentity.AmpTeam"></bean:define>
									<logic:equal name="currentMember" property="teamId" scope="session" value="${team.ampTeamId}">
										<option selected="selected" value='<bean:write name="item" property="ampTeamMemId"/>'><bean:write name="team" property="name"/></option>
									</logic:equal>
									<logic:notEqual name="currentMember" property="teamId" scope="session" value="${team.ampTeamId}">
												<option value="<bean:write name="item" property="ampTeamMemId"/>">
											<bean:write name="team" property="name"/>
										</option>
									</logic:notEqual>
								</logic:iterate>
							</select>
						</feature:display>
					</logic:notEmpty>
				</logic:equal>
			</logic:present>
			
						
			
		</div>
	</div>
</div>
<!-- HEADER END -->

<script type="text/javascript">
	function selectwkspace(id){
		var url = "/selectTeam.do?id="+id;
		document.location.href=url;
	}
</script>


