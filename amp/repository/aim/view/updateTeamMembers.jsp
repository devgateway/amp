<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<c:set var="msg"><digi:trn>Please select the role</digi:trn></c:set>
<script language="javascript">
function validate(){
	if (document.aimTeamMemberForm.role.selectedIndex==0){
		alert('${msg}');
		document.aimTeamMemberForm.role.focus()
		return false;
	}
	return true;
	}
</script>


<digi:instance property="aimTeamMemberForm" />
<digi:form action="/updateTeamMember.do" method="post" onsubmit="return validate()">

<html:hidden property="teamId" />
<html:hidden property="teamMemberId" />
<html:hidden property="action" />
<html:hidden property="userId" />
<html:hidden property="name" />
<digi:errors/>
<jsp:include page="teamPagesHeader.jsp"  />
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td height=33><span class=crumb>
					<c:set var="clickToViewAdmin">
						<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
					</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${clickToViewAdmin}" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="clickToViewWorkspaceManager">
						<digi:trn key="aim:clickToViewWorkspaceManager">Click here to view Workspace Manager</digi:trn>
						</c:set>
						<digi:link href="/workspaceManager.do" styleClass="comment" title="${clickToViewWorkspaceManager}" >
						<digi:trn key="aim:workspaceManager">
						Workspace Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;

						<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlParams}" property="teamId">
						<bean:write name="aimTeamMemberForm" property="teamId" />
						</c:set>
						<c:set var="clickToViewTeamMembers">
						<digi:trn key="aim:clickToViewWorkspaceMembers">Click here to view Workspace Members</digi:trn>
						</c:set>
						<digi:link href="/teamMembers.do" name="urlParams" styleClass="comment" title="${clickToViewTeamMembers}" >
						<digi:trn key="aim:members">
						Members
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<logic:equal name="aimTeamMemberForm" property="action" value="edit">
							<digi:trn key="aim:editTeamMembers">Edit Members</digi:trn>	
						</logic:equal>
						<logic:equal name="aimTeamMemberForm" property="action" value="delete">
							<digi:trn key="aim:deleteTeamMembers">Delete Members</digi:trn>	
						</logic:equal>	
                        <div class="adminicon"><img src="/TEMPLATE/ampTemplate/img_2/adminicons/usermanager.jpg"/></div>
					
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
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">
							<tr bgColor=#f4f4f2>
								<td vAlign="top" width="100%">&nbsp;
									
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="95%" border="0">	
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellpadding="0" cellspacing="0" width="100%">
													<tr bgColor=#f4f4f2 height="20">
														<td bgColor=#c9c9c7 class=box-title width="10"0 align="center">
																<logic:equal name="aimTeamMemberForm" property="action" value="edit">
																	<digi:trn key="aim:editTeamMembers">Edit Members</digi:trn>	
																</logic:equal>
																<logic:equal name="aimTeamMemberForm" property="action" value="delete">
																	<digi:trn key="aim:deleteTeamMembers">Delete Members</digi:trn>	
																</logic:equal>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table border="0" cellPadding=5 cellspacing="1" class=box-border width="100%">
	<tr>
		<td align="right" width="50%">
			<digi:trn key="aim:memberName">Name</digi:trn>&nbsp;&nbsp; &nbsp;
		</td>
		<td align="left" width="50%">
			<bean:write name="aimTeamMemberForm" property="name" />
		</td>
	</tr>
	<tr>
		<td align="right" width="50%">
			<digi:trn key="aim:memberRole">Role</digi:trn>&nbsp;&nbsp; &nbsp;&nbsp;	
		</td>
		<td align="left" width="50%">
			<html:select property="role">
				<%@include file="teamMemberRolesDropDown.jsp" %>
			</html:select>
		</td>
	</tr>
	<tr>
		<td colspan="2" width="60%">
			<logic:equal name="aimTeamMemberForm" property="action" value="delete">
			<table width="100%" cellspacing="5">
				<tr>
					<td width="50%" align="right">
						<html:submit styleClass="dr-menu"	property="apply">
							<digi:trn key="btn:delete">Delete</digi:trn>
						</html:submit>						
					</td>
					<td width="50%" align="left">
						<html:reset  styleClass="dr-menu" property="submitButton" onclick="javascript:history.go(-1)">
							<digi:trn key="btn:cancel">Cancel</digi:trn>
						</html:reset>																					</td>
					</td>
				</tr>
			</table>
			</logic:equal>
			<logic:equal name="aimTeamMemberForm" property="action" value="edit">
			<table width="100%" cellspacing="5">
				<tr>
					<td width="50%" align="right">
						<html:submit styleClass="dr-menu" property="apply">
							<digi:trn key="btn:save">Save</digi:trn>
						</html:submit>						
					</td>
					<td width="50%" align="left">
						<html:reset  styleClass="dr-menu" property="submitButton" onclick="javascript:history.go(-1)">
							<digi:trn key="btn:cancel">Cancel</digi:trn>
						</html:reset>
					</td>
				</tr>
			</table>
			</logic:equal>
		</td>
	</tr>

												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>&nbsp;
								
							</td></tr>
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
								
											<td class="inside">
												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
												
												<c:set var="clickToViewWorkspaceManager">
												<digi:trn key="aim:clickToViewWorkspaceManager">Click here to view Workspace Manager</digi:trn>
												</c:set>
												<digi:link href="/workspaceManager.do" title="${clickToViewWorkspaceManager}" >
												<digi:trn>
													Workspace Manager
												</digi:trn>
												</digi:link>
											</td>
										</tr>

										<tr>
											<td class="inside">
												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="clickToAddTeams">
												<digi:trn key="aim:clickToAddTeams">Click here to Add Teams</digi:trn>
												</c:set>
												<digi:link href="/updateWorkspace.do" title="${clickToAddTeams}" >
												<digi:trn key="aim:addTeam">
												Add Teams
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										
										<tr>
											<td class="inside">
												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="clickToViewAdmin">
												<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</c:set>
												<digi:link href="/admin.do" title="${clickToViewAdmin}" >
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

