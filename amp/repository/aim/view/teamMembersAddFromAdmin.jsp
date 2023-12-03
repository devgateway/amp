<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">

	function defaultPermsSelected() {
		document.aimTeamMemberForm.readPerms.disabled=true;
		document.aimTeamMemberForm.writePerms.disabled=true;
		document.aimTeamMemberForm.deletePerms.disabled=true;
	}

	function userSpecificPermsSelected() {
		document.aimTeamMemberForm.readPerms.disabled=false;
		document.aimTeamMemberForm.writePerms.disabled=false;
		document.aimTeamMemberForm.deletePerms.disabled=false;
	}
	
</script>

<digi:instance property="aimTeamMemberForm" />

<digi:form action="/addTeamMember.do" method="post">

<jsp:include page="teamPagesHeader.jsp"  />

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td height=33><span class=crumb>
						<digi:link href="/admin.do" styleClass="comment" title="Click here to goto Admin Home" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:link href="/workspaceManager.do" styleClass="comment" title="Click here to view Workspace Manager" >
						<digi:trn key="aim:workspaceManager">
						Workspace Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;

						<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlParams}" property="teamId">
						<bean:write name="aimTeamMemberForm" property="teamId" />
						</c:set>
						<digi:link href="/teamMembers.do" name="urlParams" styleClass="comment" title="Click here to view Member" >
						<digi:trn key="aim:members">
						Members
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:addWorkspaceMember">
							Add Workspace Member
						</digi:trn>	
						</span>
					</td>
				</tr>
				<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue>
						<bean:write name="aimTeamMemberForm" property="teamName" />
					</span></td>
				</tr>
				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellspacing="1" cellspacing="1">
					<tr>
					<td noWrap width=600 vAlign="top">
						<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="100%">
							<tr>
								<td valign="top">
									<jsp:include page="addTeamMember.jsp" />
								</td>
							</tr>
						</table>
					</td>
					<td noWrap width="100%" vAlign="top">
						<table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">	
							<tr>
								<td>
									<table cellpadding="0" cellspacing="0" width="10"0>
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<digi:trn key="aim:otherLinks">
												Other links
												</digi:trn>
											</td>
											<td background="ampModule/aim/images/corner-r.gif" height="17" width=17>&nbsp;
												
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellspacing="1" width="100%">
										<tr>
											<td class="inside">
												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<digi:link href="/workspaceManager.do" title="Click here to view Workspace Manager" >
												<digi:trn key="aim:workspaceManager">
												Workspace Manager
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td class="inside">
												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<digi:link href="/updateWorkspace.do" title="Click here to Add Teams" >
												<digi:trn key="aim:addTeams">
												Add Teams
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										
										<tr>
											<td class="inside">
												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<digi:link href="/admin.do" title="Click here to goto Admin Home" >
												<digi:trn key="aim:AmpAdminHome">
												Admin Home
												</digi:trn>
												</digi:link>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td></tr>
					</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</digi:form>
