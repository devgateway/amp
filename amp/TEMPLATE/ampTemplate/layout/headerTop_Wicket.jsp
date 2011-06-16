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

	<digi:ref href="/TEMPLATE/ampTemplate/css_2/amp.css" type="text/css" rel="stylesheet" />
	
		 

<script type="text/javascript">
	var YAHOOAmp 	= YAHOO;
</script>  
        
<%org.digijava.kernel.request.SiteDomain siteDomain = null;%>
<logic:present name="currentMember" scope="session">
	<script language=javascript>
	function showUserProfile(id){
		<digi:context name="information" property="/aim/userProfile.do" />
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
<center>
<!-- HEADER START -->
<div class="header">
	<div class="centering">
		<div class="logo">
			<img src="/TEMPLATE/ampTemplate/img_2/amp_logo.gif" align=left>
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
			
			<img src="/TEMPLATE/ampTemplate/img_2/top_sep.gif" class="top_sep">
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
</center>
<script type="text/javascript">
	function selectwkspace(id){
		var url = "/selectTeam.do?id="+id;
		document.location.href=url;
	}
</script>


