<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ page import="org.digijava.module.aim.util.FeaturesUtil" %>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/util.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
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
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-1.4.2.min.js"/>"></script>


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
		<logic:notEmpty name="currentMember" scope="session">
		<feature:display name="Change Workspace" module="My Desktop">
			<div class="workspace_info">
				<digi:trn key="aim:changeworkspace">Workspace</digi:trn>:
		 		<a id="menutoggle" style="color:#FFFFFF;cursor: pointer;">
		 			<strong>
				 		<bean:define id="teamMember" name="currentMember" scope="session" type="org.digijava.module.aim.helper.TeamMember" />
						${teamMember.teamName}
					</strong>
					<img border="0" src="img_2/arr_wrk.gif">
				</a>
				<div id="workspacemenu" class="yuimenu">
    				<div class="bd">
	        			<ul class="first-of-type">
	           				<logic:iterate id="item"  name="USER_WORKSPACES" scope="session" type="org.digijava.module.aim.dbentity.AmpTeamMember">
								<bean:define id="team" name="item" property="ampTeam" type="org.digijava.module.aim.dbentity.AmpTeam"></bean:define>
								<logic:equal name="currentMember" property="teamId" scope="session" value="${team.ampTeamId}">
									<li class="yuimenuitem" style="height:18px;"><a href="#"><bean:write name="team" property="name"/></a></li>
								</logic:equal>
								<logic:notEqual name="currentMember" property="teamId" scope="session" value="${team.ampTeamId}">
									<li class="yuimenuitem" style="height:18px;"><a href='/selectTeam.do?id=<bean:write name="item" property="ampTeamMemId"/>' onclick="return canExit()"><bean:write name="team" property="name"/></a></li>
								</logic:notEqual>
							</logic:iterate>
	        			</ul>            
    				</div>
				</div>
			</div>
		</feature:display>
		</logic:notEmpty>
		<div>
			<table class="top_nav">
				<tr>
					<td>
						<logic:notEmpty name="currentMember" scope="session">
							<a href="javascript:showUserProfile(${teamMember.memberId})">${teamMember.memberName}</a>
						</logic:notEmpty>
						<img src="img_2/top_sep.gif" class="top_sep">
					</td>
					<td>
						<feature:display name="Language Option" module="Tools">
	               			<div id="reports2" class="yuiampmenu" style="margin-top: 10px">
	                          <div class="bd">                    
	                              <ul>
	                              	<digi:insert flush="false" attribute="dropdownLangSwitch" />
	                              </ul>
	                          </div>
	                      	</div>                              
	                   	</feature:display>
			  		</td>
					<td style="text-transform: uppercase;">
						<img src="img_2/top_sep.gif" class="top_sep">
						<digi:link styleClass="loginWidget" href="/j_acegi_logout" module="aim">
							<digi:trn key="aim:logout">LOGOUT</digi:trn>
						</digi:link>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>
<!-- HEADER END -->

<script language="javascript">
var oMenu = new YAHOO.widget.Menu("workspacemenu", {  });
oMenu.render();
YAHOO.util.Event.addListener("menutoggle", "click", oMenu.show, null, oMenu); 
var lMenu = new YAHOO.widget.Menu("languageemenu", {  });
lMenu.render();


</script>




