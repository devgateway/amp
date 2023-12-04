<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<script language="JavaScript">
	function addActivity() {
		//levelObj=document.getElementById('activityLevel');
		//if(levelObj==null) 
		selectedLevelId=0; 
		//else
		//selectedLevelId=levelObj.options[levelObj.selectedIndex].value;
		window.location.href="/aim/addActivity.do~pageId=1~reset=true~action=create~activityLevelId="+selectedLevelId;	
	}
	
	function teamWorkspaceSetup(a) {
		window.location.href="/aim/workspaceOverview.do~tId="+a+"~dest=teamLead";	
	}
	function addMessage(fillTypesAndLevels) {
		window.location.href="/message/messageActions.do?editingMessage=false&actionType="+fillTypesAndLevels;
	}
</script>

<TABLE align="center" border="0" cellPadding=2 cellSpacing=3 width="100%" bgcolor="#f4f4f2">
	<TR>
		<TD class=r-dotted-lg-buttom valign="top">
			<TABLE border="0" cellpadding="0" cellspacing="0" width="100%" >
        		<TR><TD>
              	<TABLE border="0" cellpadding="0" cellspacing="0" >
              		<TR bgColor=#f4f4f2>
                 		<TD bgColor=#c9c9c7 class=box-title
							title='<digi:trn jsFriendly="true" key="aim:WorkspaceMemberListAssocWithTeam">List of Workspace Members associated with Workspace</digi:trn>'>
								<digi:trn key="aim:workspaceMembers">Workspace Members</digi:trn>
							</TD>
                    	<TD background="module/aim/images/corner-r.gif"
							height=17 width=17></TD>
						</TR>
					</TABLE>
				</TD></TR>
				<c:set var="trnclickToViewMemberDetails">
						<digi:trn key="aim:clickToAddNewActivit">Click here to Add New Activity</digi:trn>
				</c:set>
				<logic:notEmpty name="myTeamMembers" scope="session">
				<TR><TD bgColor=#ffffff class=box-border align=left>
					<TABLE border="0" cellpadding="1" cellspacing="1" width="100%" >
					<logic:iterate name="myTeamMembers" id="tm" scope="session" 
					type="org.digijava.module.aim.helper.TeamMember">
						<TR><TD title="${trnclickToViewMemberDetails}">
							<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width="10">
							<A href="javascript:showUserProfile(<c:out value="${tm.memberId}"/>)">
								<bean:write name="tm" property="memberName" />
							</A>
							<logic:equal name="tm" property="teamHead" value="true">*</logic:equal>
						</TD></TR>
					</logic:iterate>
					</TABLE>
				</TD></TR>
				</logic:notEmpty>
			</TABLE>	
					<br/><br/>
					<field:display name="Add Activity Button" feature="Edit Activity">
					<c:if test="${not empty sessionScope.currentMember}">
						<!-- 
						<c:if test="${sessionScope.currentMember.teamType != 'DONOR'}">
						<c:if test="${sessionScope.currentMember.teamAccessType != 'Management'}">
						 <c:if test="${sessionScope.currentMember.addActivity == 'true'}">
						 -->
							<c:set var="trnClickToAddNewActivit">
								<digi:trn key="aim:clickToAddNewActivit">Click here to Add New Activity</digi:trn>
							</c:set>
							<div title="${trnClickToAddNewActivit}" align="left">
								<input type="button" class="dr-menu" onclick="return addActivity()" 
								value="<digi:trn key="btn:addActivity">Add Activity</digi:trn>" name="addActivity"/>
								
							<c:set var="translation">
								<digi:trn key="aim:addActivitySelectLevel">Select Level</digi:trn>
							</c:set>


							<logic:present name="activity_level" scope="request">
							<category:showoptions firstLine="${translation}" name="xx" outerId="activityLevel"
							property="activityLevel"  styleClass="inp-text" keyName="<%=org.digijava.module.categorymanager.util.CategoryConstants.ACTIVITY_LEVEL_KEY%>"/>
							</logic:present>
							
							</div>
						</c:if>
						<!--
							</c:if>
							</c:if>
					</c:if>
						-->
					</field:display>
					<c:if test="${sessionScope.currentMember.teamHead == true}">
							<br/>
							<c:set var="trnclickToConfigureTeamPages">
								<digi:trn key="aim:clickToConfigureTeamPages1">Click here to Configure Team Pages</digi:trn>
							</c:set>
							<div title='${trnclickToConfigureTeamPages}' align="left">
            	         	<input type="button" class="dr-menu" onclick='return teamWorkspaceSetup("-1")' value="<digi:trn key="btn:teamWorkspaceSetup">Team Workspace Setup</digi:trn>" name="addActivity"/>
                	     	</div><br/><br/>
					</c:if>
			</TD>
	</TR>
</TABLE>
