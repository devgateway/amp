<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimRolesForm" />
<digi:context name="digiContext" property="context" />

<jsp:include page="teamPagesHeader.jsp"  />
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td height=33><span class=crumb>
					<c:set var="clickToViewAdmin">
					<digi:trn key="aim:clickToViewAdmin">Click here to go to Admin Home</digi:trn>
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
						<digi:trn>Workspace Manager</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:roles">
						Roles
						</digi:trn>
					</td>
				</tr>
				<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue><digi:trn>Workspace Manager</digi:trn></span>
					</td>
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
									<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="90%" border="0">	
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellpadding="0" cellspacing="0" width=117>
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title width="10"0>
															<digi:trn key="aim:roles">
															Roles
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
												<table border="0" cellPadding=3 cellspacing="1" class=box-border width="100%">
												
													<tr bgColor=#dddddb>
														<td bgColor=#dddddb align="left" colspan="3">
															<digi:trn key="aim:roleName">Role Name</digi:trn>
														</td>
													</tr>
													<logic:empty name="aimRolesForm" property="roles">
													<tr>
														<td colspan="3"><b>
															<digi:trn key="aim:noRoles">
															No roles present
															</digi:trn>
														</b></td>
													</tr>
													</logic:empty>		
													<logic:notEmpty name="aimRolesForm" property="roles">
													<logic:iterate name="aimRolesForm" property="roles" id="roles" 
													type="org.digijava.ampModule.aim.dbentity.AmpTeamMemberRoles">
													<tr>
														<td width="90%" bgcolor="#eeeeee">
															<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams}" property="id">
																<bean:write name="roles" property="ampTeamMemRoleId" />
															</c:set>
															<c:set target="${urlParams}" property="action" value="view" />
															<c:set var="translation">
																<digi:trn key="aim:clickToViewRole">Click here to view Role</digi:trn>
															</c:set>
															<digi:link href="/getRole.do" name="urlParams" title="${translation}" >
																<bean:write name="roles" property="role"/>
															</digi:link>															
														</td>
														<td bgcolor="#eeeeee">
															<c:set target="${urlParams}" property="action" value="edit" />
															
															<c:set var="clickToEditRole">
															<digi:trn key="aim:clickToEditRole">Click here to Edit Role</digi:trn>
															</c:set>
															[ <digi:link href="/getRole.do" name="urlParams" title="${clickToEditRole}" >
																<digi:trn key="aim:rolesManagerEdit">Edit</digi:trn>
															</digi:link> ]
														</td>
														<td bgcolor="#eeeeee">
															<c:set target="${urlParams}" property="action" value="delete" />
															<c:set var="translation">
															<digi:trn key="aim:clickToDeleteRole">Click here to Delete Role</digi:trn>
															</c:set>
															[ <digi:link href="/getRole.do" name="urlParams" title="${translation}" >
																<digi:trn key="aim:rolesManagerDelete">Delete</digi:trn>
															</digi:link> ]														
														</td>
													</tr>
													</logic:iterate>
													</logic:notEmpty>
												</table>
											</td>
										</tr>

										<logic:notEmpty name="aimRolesForm" property="pages">
										<tr>
											<td colspan="4">
												<digi:trn key="aim:rolesManagerPages">
													Pages:
												</digi:trn>
												<logic:iterate name="aimRolesForm" property="pages" id="pages" type="java.lang.Integer">
													<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParams1}" property="page">
														<%=pages%>
													</c:set>
													<c:set var="translation">
														<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
													</c:set>
													<digi:link href="/roles.do" name="urlParams1" title="${translation}" >
														<%=pages%>
													</digi:link> |&nbsp; 												
												</logic:iterate>
											</td>
										</tr>
										</logic:notEmpty>
										
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
										<tr>
											<td class="inside">
												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToViewTeams">Click here to view Teams</digi:trn>
												</c:set>
												<digi:link href="/workspaceManager.do" title="${translation}" >
												<digi:trn key="aim:teams">
												Teams
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td class="inside">
												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToAddTeams">Click here to Add Teams</digi:trn>
												</c:set>
												<digi:link href="/updateWorkspace.do" title="${translation}" >
												<digi:trn key="aim:addTeam">
												Add Teams
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										
										<tr>
											<td class="inside">
												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
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

