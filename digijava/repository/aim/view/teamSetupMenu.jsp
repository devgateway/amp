<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<table width="100%" cellPadding=0 cellSpacing=0 align="left" valign="top">
	<tr><td height="20">
		<table bgColor=#ffffff align="left" valign="bottom" cellPadding=0 cellspacing=1 height="20">
			<tr height="20">
				<td noWrap>
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewWorkspaceOverview">Click here to view Workspace Overview</digi:trn>
					</bean:define>
					<digi:link href="/workspaceOverview.do"  styleClass="sub-nav" title="<%=translation%>" >
						:: <digi:trn key="aim:workspaceOverview">Workspace Overview</digi:trn>
					</digi:link>
				</td>											
				<td noWrap>
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewActivityList">Click here to view Activity List</digi:trn>
					</bean:define>
					<digi:link href="/teamActivityList.do" styleClass="sub-nav" title="<%=translation%>" >
						:: <digi:trn key="aim:activityList">Activity List</digi:trn>
					</digi:link>
				</td>										
				<td noWrap>
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewReportList">Click here to view Report List</digi:trn>
					</bean:define>
					<digi:link href="/teamReportList.do" styleClass="sub-nav" title="<%=translation%>" > 
						:: <digi:trn key="aim:reportList">Report List</digi:trn>
					</digi:link>
				</td>											
				<td noWrap>
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewRelatedLinksList">Click here to view Related Links List</digi:trn>
					</bean:define>
					<digi:link href="/relatedLinksList.do" styleClass="sub-nav" title="<%=translation%>" >
						:: <digi:trn key="aim:relatedLinksList">Related Links List</digi:trn>
					</digi:link>
				</td>
				<td noWrap>
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewMembers">Click here to view Members</digi:trn>
					</bean:define>
					<digi:link href="/teamMemberList.do" styleClass="sub-nav" title="<%=translation%>" >
						:: <digi:trn key="aim:members">Members</digi:trn>
					</digi:link>
				</td>
				<td width="40" bgColor=#3754a1 height="19">
				</td>
			</tr>
		</table>	
	</td></tr>
	<tr><td>
		<table bgcolor="#f4f4f2" align="left" valign="top" cellPadding=0 cellspacing=1>
			<tr>
				<td noWrap>
					<bean:define id="translation">
						<digi:trn key="aim:clickToCustomizeTeamSettings">Click here to Customize Team Settings</digi:trn>
					</bean:define>
					<digi:link href="/defaultSettings.do" styleClass="sub-nav" title="<%=translation%>" >
						:: <digi:trn key="aim:customizedTeamSettings">Customized Team Settings</digi:trn>
					</digi:link>
				</td>	
				<td noWrap>
					<bean:define id="translation">
						<digi:trn key="aim:clickToConfigureTeamPages">Click here to Configure Team Pages</digi:trn>
					</bean:define>
					<digi:link href="/configureTeamPage.do" styleClass="sub-nav" title="<%=translation%>" >
						:: <digi:trn key="aim:configureTeamPages">Configure Team Pages</digi:trn>
					</digi:link>
				</td>
				<td noWrap width="329" bgColor=#3754a1 height="19">
				</td>
			</tr>
		</table>													
	</td></tr>
</table>
