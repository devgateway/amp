<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script language="JavaScript">
	function addActivity() {
		window.location.href="/aim/addActivity.do~pageId=1~reset=true~action=create";	
	}
	
	function teamWorkspaceSetup(a) {
		window.location.href="/aim/workspaceOverview.do~tId="+a+"~dest=teamLead";	
	}
</script>

<TABLE align=center border=0 cellPadding=2 cellSpacing=3 width="100%" bgcolor="#f4f4f2">
	<TR>
		<TD class=r-dotted-lg-buttom vAlign=top>
			<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" >
        		<TR><TD>
              	<TABLE border=0 cellPadding=0 cellSpacing=0 >
              		<TR bgColor=#f4f4f2>
                 		<TD bgColor=#c9c9c7 class=box-title
							title='<digi:trn key="aim:TeamMemberListAssocWithTeam">List of Team Members associated with Team</digi:trn>'>
								<digi:trn key="aim:teamMembers">Team Members</digi:trn>
							</TD>
                    	<TD background="module/aim/images/corner-r.gif" 
							height=17 width=17></TD>
						</TR>
					</TABLE>
				</TD></TR>
				<bean:define id="translation">
					<digi:trn key="aim:clickToViewMemberDetails">Click here to View Member Details</digi:trn>
				</bean:define>				
				<logic:notEmpty name="myTeamMembers" scope="session">
				<TR><TD bgColor=#ffffff class=box-border align=left>
					<TABLE border=0 cellPadding=1 cellSpacing=1 width="100%" >
					<logic:iterate name="myTeamMembers" id="tm" scope="session" 
					type="org.digijava.module.aim.helper.TeamMember"> 
						<TR><TD title='<%=translation%>'>
							<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>
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
					
					<div title='<%=translation%>' align="left">
					<input type="button" class="dr-menu" onclick="return addActivity()" value='<digi:trn key="btn:addActivity">Add Activity</digi:trn>' name="addActivity"/>
					</div>
					<logic:equal name="teamHead" scope="session" value="yes">
						<br/>
						<bean:define id="translation">
							<digi:trn key="aim:clickToConfigureTeamPages">Click here to Configure Team Workspace</digi:trn>
						</bean:define>
						<div title='<%=translation%>' align="left">
                     	<input type="button" class="dr-menu" onclick='return teamWorkspaceSetup("-1")' value='<digi:trn key="btn:teamWorkspaceSetup">Team Workspace Setup</digi:trn>' name="addActivity"/>
                     	</div><br/><br/>
					</logic:equal>
			</TD>
	</TR>
</TABLE>
