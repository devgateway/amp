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

<html:hidden property="ampTeamId" />

<jsp:include page="teamPagesHeader.jsp"  />

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="translation">
							<digi:trn key="aim:clickToViewWorkspaceManager">Click here to view Workspace Manager</digi:trn>
						</c:set>
						<digi:link href="/workspaceManager.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:workspaceManager">
						Workspace Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;

						<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlParams}" property="teamId">
						<bean:write name="aimTeamMemberForm" property="ampTeamId" />
						</c:set>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewMember">Click here to view Member</digi:trn>
						</c:set>
						<digi:link href="/teamMembers.do" name="urlParams" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:members">
						Members
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:addWorkspaceMember">
							Add Workspace Member
						</digi:trn>	
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
														<td bgColor=#c9c9c7 class=box-title width="100%" align="center"><b>
															<digi:trn key="aim:addTeamMembersFor">Add Members for</digi:trn>
															<bean:write name="aimTeamMemberForm" property="teamName" />
															</b>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table border="0" cellPadding=2 cellSpacing=2 class=box-border width="100%">
													<tr>
														<td colspan=2 align="center">
															<digi:errors />
														</td>
													</tr>
													<tr>
														<td align="right" width="50%">
															<digi:trn key="aim:userId">User Id</digi:trn>	
														</td>
														<td align="left" width="50%">
															<html:text property="email" size="20" />
														</td>
													</tr>
													<tr>
														<td align="right" width="50%">
															<digi:trn key="aim:memberRole">Role</digi:trn>	
														</td>
														<td align="left" width="50%">
															<html:select property="role">
																<html:option value="">------ Select role ------</html:option>
																<html:optionsCollection name="aimTeamMemberForm" property="ampRoles"
																value="ampTeamMemRoleId" label="role" />
															</html:select>
														</td>
													</tr>
													<tr>
														<td align="center" colspan="2">
															<table cellspacing="5">
																<tr>
																	<td>
																		<html:radio property="permissions" value="default"
																		onclick="defaultPermsSelected()">
																		<digi:trn key="aim:defaultPermission">
																		Set default permissions based on the roles</digi:trn>
																		</html:radio>
																	</td>
																</tr>
																<tr>
																	<td>
																		<html:radio property="permissions" value="userSpecific"
																		onclick="userSpecificPermsSelected()">
																		<digi:trn key="aim:userSpecificPermission">
																		Manually set permissions for this user</digi:trn>
																		</html:radio>
																	</td>
																</tr>
																<tr>	
																	<td>
																		<html:checkbox property="readPerms" disabled="true">
																		<digi:trn key="aim:readPerms">Read</digi:trn>
																		</html:checkbox>
																	</td>
																</tr>
																<tr>
																	<td>
																		<html:checkbox property="writePerms" disabled="true">
																		<digi:trn key="aim:writePerms">Add / Update</digi:trn>
																		</html:checkbox>
																	</td>
																</tr>
																<tr>
																	<td>
																		<html:checkbox property="deletePerms" disabled="true">
																		<digi:trn key="aim:deletePerms">Delete</digi:trn>	
																		</html:checkbox>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td colspan="2" width="60%">
															<table width="100%" cellspacing="5">
																<tr>
																	<td width="50%" class="right-align">
																		<html:submit  styleClass="dr-menu" property="submitButton">
																			<digi:trn key="btn:save">Save</digi:trn> 
																		</html:submit>
																	</td>
																	<td width="50%" class="left-align">
																		<html:reset  styleClass="dr-menu" property="submitButton"  onclick="javascript:history.go(-1)">
																			<digi:trn key="btn:cancel">Cancel</digi:trn> 
																		</html:reset>
																		
																	</td>
																</tr>
															</table>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>&nbsp;
												
											</td>
										</tr>
									</table>
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
											<td background="module/aim/images/corner-r.gif" height="17" width=17>&nbsp;
												
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
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToViewWorkspaceManager">Click here to view Workspace Manager</digi:trn>
												</c:set>
												<digi:link href="/workspaceManager.do" title="${translation}" >
												<digi:trn key="aim:workspaceManager">
												Workspace Manager
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td class="inside">
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToAddTeams">Click here to Add Teams</digi:trn>
												</c:set>
												<digi:link href="/updateWorkspace.do" title="${translation}" >
												<digi:trn key="aim:addTeams">
												Add Teams
												</digi:trn>
												</digi:link>
											</td>
										</tr>
																				
										<tr>
											<td class="inside">
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</c:set>
												<digi:link href="/admin.do" title="${translation}" >
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



