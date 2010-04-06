<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="umViewEditUserForm" />

<script language="javascript" type="text/javascript">
	function validate(){
		var workspaceId=document.getElementById('wId').value;
		if(workspaceId== -1 ){
			alert('Please Select Workspace');
			return false;
		}
		var roleId=document.getElementById('rId').value;
		if(roleId== -1){
			alert('Please Select Role');
			return false;
		}
		return true;
	}

	function confirmDelete(){
		return confirm("Are You Sure You Want To Remove This Workspace ?");
	}
</script>

<digi:form action="/viewEditUser.do?event=assignToWorkspace" method="post">
<digi:errors/>
<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="772">
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=33><span class=crumb>
						<c:set var="clickToViewAdmin">
							<digi:trn>Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" module="aim" styleClass="comment" title="${clickToViewAdmin}" >
							<digi:trn>Admin Home</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="clickToViewUserManager">
							<digi:trn>Click here to view User Manager</digi:trn>
						</c:set>
						<digi:link href="/viewAllUsers.do~reset=true" module="um" styleClass="comment" title="${clickToViewUserManager}" >
							<digi:trn>User Manager</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlParams}" property="id">
							<bean:write name="umViewEditUserForm" property="id" />
						</c:set>
						<c:set var="clickToViewUser">
							<digi:trn>Click here to view User</digi:trn>
						</c:set>
						<digi:link href="/viewEditUser.do" module="um" name="urlParams" styleClass="comment" title="${clickToViewUser}" >
							<digi:trn>Edit User</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn>Assign Workspace</digi:trn>							
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<digi:trn>Assign Workspace</digi:trn>
					</span></td>
				</tr>
				<tr>
					<td noWrap width="100%" vAlign="top">
						<table width="100%" cellspacing="1" cellSpacing="1">
							<tr>
								<td noWrap width="600" vAlign="top">
									<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" class="box-border-nopadding" width="100%">										
										<tr bgColor="#f4f4f2">
											<td bgColor="#dbe5f1" colspan="2">
												&nbsp;
											</td>
										</tr>
										<tr bgColor="#f4f4f2">
											<td valign="top">
												<table align="center" bgColor="#f4f4f2" cellPadding="0" cellSpacing="0" width="95%" border="0">
													<tr>
														<td width="10%">&nbsp;</td>
														<td bgColor="#f4f4f2">
															<table border="0" cellPadding="5" cellSpacing="1"width="100%">
																<logic:empty name="umViewEditUserForm" property="availableWorkspaces">
																	<tr>
																		<td colspan="2">
																			<digi:trn>No Workspaces exist</digi:trn>
																		</td>
																	</tr>
																</logic:empty>																
																<logic:notEmpty name="umViewEditUserForm" property="availableWorkspaces">
																	<tr>
																		<td align="right" width="50%">
																			<strong><digi:trn>First Name</digi:trn>:</strong>
																		</td>
																		<td align="left" width="50%">
																			<bean:write name="umViewEditUserForm" property="firstNames"/>
																		</td>
																	</tr>
																	<tr>
																		<td align="right" width="50%">
																			<strong><digi:trn>Last Name</digi:trn>:</strong>
																		</td>
																		<td align="left" width="50%">
																			<bean:write name="umViewEditUserForm" property="lastName"/>
																		</td>
																	</tr>
																	<tr>
																		<td align="right" width="50%">
																			<strong><digi:trn>Email</digi:trn>:</strong>
																		</td>
																		<td align="left" width="50%">
																			<bean:write name="umViewEditUserForm" property="email"/>
																		</td>
																	</tr>
																	<tr>
																		<td align="right" width="50%">
																			<strong><digi:trn>Workspace Name</digi:trn>:</strong>	
																		</td>
																		<td align="left" width="50%">
																			<html:select property="workspaceId" styleId="wId">
																				<html:option value="-1">------<digi:trn>Select Workspace</digi:trn>------</html:option>
																				<logic:iterate id="workspace" name="umViewEditUserForm" property="availableWorkspaces">
																					<html:option value="${workspace.ampTeamId}">${workspace.name}</html:option>
																				</logic:iterate>
																			</html:select>
																		</td>
																	</tr>
																	<tr>
																		<td align="right" width="50%">
																			<strong><digi:trn>Role</digi:trn>:</strong>	
																		</td>
																		<td align="left" width="50%">
																			<html:select property="role" styleId="rId">
																				<html:option value="-1">------<digi:trn>Select Role</digi:trn>------</html:option>
																				<logic:iterate name="umViewEditUserForm" property="roles" id="ampRole">
																					<html:option value="${ampRole.ampTeamMemRoleId}"><digi:trn>${ampRole.role}</digi:trn></html:option>
																				</logic:iterate>
																			</html:select>
																		</td>
																	</tr>
																	<tr>
																		<td colspan="2" width="60%">																			
																			<table width="100%" cellspacing="5">
																				<tr>
																					<td width="50%" align="right">
																						<html:submit styleClass="dr-menu" onclick="return validate()">
																							<digi:trn>Done</digi:trn>
																						</html:submit>						
																					</td>
																					<td width="50%" align="left">
																						<html:reset  styleClass="dr-menu" property="submitButton" onclick="javascript:history.go(-1)">
																							<digi:trn>Cancel</digi:trn>
																						</html:reset>
																					</td>
																				</tr>
																			</table>																						
																		</td>
																	</tr>
																</logic:notEmpty>
															</table>
														</td>
													</tr>
													<!-- show user's workspaces -->
													<logic:notEmpty name="umViewEditUserForm" property="userTeamsHolder.teamMembers">
														<tr height="5px"><td colspan="2" bgcolor="#f4f4f2">&nbsp;</td></tr>
														<tr>															
															<td width="10%">&nbsp;</td>
															<td>
																<table width="100%" height="30" cellpadding="2" cellspacing="0">
																	<tr style="background-color: #999999; color: #000000;" align="center">
																		<td align="left" width="50%">																			
																			<b><digi:trn>Workspace Name</digi:trn></b>
																		</td>
																		<td align="left" width="40%">
																			<b><digi:trn>Role</digi:trn></b>
																		</td>																	
																		<td align="left" width="10%">
																			<b><digi:trn>Action</digi:trn></b>																			
																		</td>																
																	</tr>
																	<c:forEach var="tm" items="${umViewEditUserForm.userTeamsHolder.teamMembers}" varStatus="stat">
																		<c:set var="trColor">
																			<c:if test="${stat.index%2==0}">#ffffff</c:if>
																			<c:if test="${stat.index%2!=0}">#dbe5f1</c:if>
																		</c:set>
																		<tr bgcolor="${trColor}">
																			<td align="left" width="50%">
																				${tm.ampTeam.name}
																			</td>
																			<td align="left" width="40%">
																				${tm.ampMemberRole.role}
																			</td>
																			<td align="left" width="10%">
																				<jsp:useBean id="urlParamsSort" type="java.util.Map" class="java.util.HashMap"/>
																				<c:set target="${urlParamsSort}" property="memberId" value="${tm.ampTeamMemId}"/>
																				<digi:link href="/viewEditUser.do?event=removeWorkspace" name="urlParamsSort" onclick="return confirmDelete()"><img  src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border=0 hspace="2"/></digi:link>																				
																			</td>
																		</tr>
																	</c:forEach>
																</table>
															</td>
														</tr>
													</logic:notEmpty>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#f4f4f2>&nbsp;</td>
										</tr>
									</table>
								</td>								
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</digi:form>

