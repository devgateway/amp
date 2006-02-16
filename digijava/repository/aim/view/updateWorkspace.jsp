<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript">
<!--

function addChildWorkspaces() {
		if (document.aimUpdateWorkspaceForm.workspaceType.value != "Team") { 
			openNewWindow(650, 380);
			<digi:context name="addChild" property="context/module/moduleinstance/addChildWorkspaces.do" />
			document.aimUpdateWorkspaceForm.action = "<%=addChild%>?dest=admin";
			document.aimUpdateWorkspaceForm.target = popupPointer.name;
			document.aimUpdateWorkspaceForm.submit();
		} else {
			alert("Workspace type must be 'Management' to add teams");
			return false;
		}
}

function removeChildWorkspace(id) {
	<digi:context name="update" property="context/module/moduleinstance/removeChildWorkspace.do" />
	document.aimUpdateWorkspaceForm.action = "<%=update%>?dest=admin&tId="+id;
	document.aimUpdateWorkspaceForm.target = "_self";
	document.aimUpdateWorkspaceForm.submit();		  
}

function update(action) {
	<digi:context name="update" property="context/module/moduleinstance/updateWorkspace.do" />
	document.aimUpdateWorkspaceForm.action = "<%=update%>?dest=admin&event="+action;
	document.aimUpdateWorkspaceForm.target = "_self";
	document.aimUpdateWorkspaceForm.submit();
}

-->
</script>

<html:javascript formName="aimUpdateWorkspaceForm"/>

<digi:instance property="aimUpdateWorkspaceForm" />
<digi:context name="digiContext" property="context" />

<digi:form action="/updateWorkspace.do" method="post" name="aimUpdateWorkspaceForm" 
type="org.digijava.module.aim.form.UpdateWorkspaceForm"
onsubmit="return validateAimUpdateWorkspaceForm(this);">

<html:hidden property="teamId" />
<html:hidden property="actionEvent" />
<html:hidden property="id" />
<html:hidden property="mainAction" />

<input type="hidden" name="event">
<input type="hidden" name="dest">

<input type="hidden" name="currUrl">

<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
<tr><td vAlign="top" align="left">
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</td></tr>
<tr><td vAlign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772 vAlign="top" align="left">
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=33><span class=crumb>
						<digi:link href="/admin.do" styleClass="comment">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:link href="/workspaceManager.do" styleClass="comment">
						<digi:trn key="aim:workspaceManager">
						Workspace Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="add">
						<digi:trn key="aim:addTeam">Add Team</digi:trn>	
						</logic:equal>
						<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="edit">
						<digi:trn key="aim:editTeam">Edit Team</digi:trn>	
						</logic:equal>
						<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="delete">
						<digi:trn key="aim:deleteTeam">Delete Team</digi:trn>	
						</logic:equal>
						<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="view">
						<digi:trn key="aim:viewTeam">View Team</digi:trn>	
						</logic:equal></span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>Workspace Manager</span>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1>
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
							<tr bgColor=#f4f4f2>
								<td vAlign="top" width="100%">
									&nbsp;
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%" border=0>	
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<digi:errors />
											</td>
										</tr>
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellPadding=0 cellSpacing=0 width="100%">
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title height="20" align="center">
															<!-- Table title -->
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="add">
															<digi:trn key="aim:addTeam">Add Team</digi:trn>	
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="edit">
															<digi:trn key="aim:editTeam">Edit Team</digi:trn>	
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="delete">
															<digi:trn key="aim:deleteTeam">Delete Team</digi:trn>	
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="view">
															<digi:trn key="aim:viewTeam">View Team</digi:trn>	
															</logic:equal>
															<!-- end table title -->
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table border=0 cellPadding=3 cellSpacing=1 width="100%" bgcolor="#dddddd">
													<tr>
														<td width="150" align="right" bgcolor="#f4f4f2">
															<digi:trn key="aim:teamName">Team Name</digi:trn>			
														</td>
														<td align="left" bgcolor="#f4f4f2">
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="delete">
																<b><bean:write name="aimUpdateWorkspaceForm" property="teamName" /></b>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="view">
																<b><bean:write name="aimUpdateWorkspaceForm" property="teamName" /></b>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="edit">
																<html:text property="teamName" size="50" styleClass="inp-text" />
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="add">
																	<html:text property="teamName" size="50" styleClass="inp-text" />
															</logic:equal>
														</td>
													</tr>
													<tr>
														<td align="right" bgcolor="#f4f4f2">
															<digi:trn key="aim:teamCategory">Team Category</digi:trn>			
														</td>
														<td align="left" bgcolor="#f4f4f2">
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="delete">
																<b><bean:write name="aimUpdateWorkspaceForm" property="type" /></b>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="view">
																<b><bean:write name="aimUpdateWorkspaceForm" property="type" /></b>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="edit">
																<html:select property="type" styleClass="inp-text">
																	<html:option value="">-- Select Category --</html:option>
																	<html:option value="Bilateral">Bilateral</html:option>
																	<html:option value="Multilateral">Multilateral</html:option>
																</html:select>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="add">
																<html:select property="type" styleClass="inp-text">
																	<html:option value="">-- Select Category --</html:option>
																	<html:option value="Bilateral">Bilateral</html:option>
																	<html:option value="Multilateral">Multilateral</html:option>
																</html:select>
															</logic:equal>
														</td>
													</tr>	
													<tr>
														<td align="right" bgcolor="#f4f4f2">
															<digi:trn key="aim:description">Descriptions</digi:trn>		
														</td>
														<td align="left" bgcolor="#f4f4f2">
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="delete">
																<b><bean:write name="aimUpdateWorkspaceForm" property="description" /></b>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="view">
																<b><bean:write name="aimUpdateWorkspaceForm" property="description" /></b>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="edit">
																<html:textarea property="description" rows="3" cols="50" styleClass="inp-text"/>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="add">
																<html:textarea property="description" rows="3" cols="50" styleClass="inp-text"/>
															</logic:equal>
														</td>
													</tr>	
													<tr>
														<td align="right" bgcolor="#f4f4f2">
															<digi:trn key="aim:workspaceType">Workspace Type</digi:trn>		
														</td>
														<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'delete'}">
														<td align="left" bgcolor="#f4f4f2">
															<html:select property="workspaceType" styleClass="inp-text">
																<html:option value="">-- Select Type --</html:option>
																<html:option value="Management">Management</html:option>
																<html:option value="Team">Team</html:option>
															</html:select>
														</td>
														</c:if>
														<c:if test="${aimUpdateWorkspaceForm.actionEvent == 'delete'}">
														<td align="left" bgcolor="#f4f4f2"><b>
															<c:out value="${aimUpdateWorkspaceForm.workspaceType}" /></b>
														</td>
														</c:if>														
													</tr>
													<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'delete'}">
													<tr>
														<td align="right" width="150" bgcolor="#f4f4f2">
															<digi:trn key="aim:childWorkspacesOrTeams">Child Workspaces/Teams</digi:trn>		
														</td>
														<td align="left" bgcolor="#f4f4f2">
															<input type="button" value="Add" class="buton" onclick="addChildWorkspaces()">
														</td>																
													</tr>
													</c:if>
													<c:if test="${!empty aimUpdateWorkspaceForm.childWorkspaces}">
													<tr>
														<td colspan="2" align="center" bgcolor="#f4f4f2">
															<table width="98%" cellPadding=2 cellSpacing=0 valign="top" align="center" 
															class="box-border-nopadding">
															<c:forEach var="workspaces" items="${aimUpdateWorkspaceForm.childWorkspaces}">
																<tr>
																	<td align="left">&nbsp;
																		<c:out value="${workspaces.name}"/>
																	</td>
																	<td align="right" width="10">
																		<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'delete'}">
																		<a href="javascript:removeChildWorkspace(<c:out value="${workspaces.ampTeamId}"/>)">
																	 	<digi:img src="module/cms/images/deleteIcon.gif" 
																		border="0" alt="Remove this child workspace"/></a>&nbsp;
																		</c:if>
																	</td>																	
																</tr>															
															</c:forEach>
															</table>
														</td>
													</tr>
													</c:if>
													<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'delete'}">
													<tr>
														<td colspan="2" align="center" bgcolor="#f4f4f2">
															<table cellPadding=5>
																<tr>
																	<td>
																		<c:if test="${aimUpdateWorkspaceForm.actionEvent == 'add'}">
																			<input type="button" value="Save" class="dr-menu"
																			onclick="update('add')"/>
																		</c:if>
																		<c:if test="${aimUpdateWorkspaceForm.actionEvent == 'edit'}">
																			<input type="button" value="Save" class="dr-menu"
																			onclick="update('edit')"/>
																		</c:if>																		
																	</td>
																	<td>
																		<html:reset value="Clear" styleClass="dr-menu"/>
																	</td>
																</tr>
															</table>										
														</td>
													</tr>													
													</c:if>
												</table>
											</td>
										</tr>

									</table>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>
								&nbsp;
							</td></tr>
						</table>
					</td>
					<td noWrap width=100% vAlign="top">
						<table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>	
							<tr>
								<td>
									<table cellPadding=0 cellSpacing=0 width=100>
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<digi:trn key="aim:otherLinks">
												Other links
												</digi:trn>
											</td>
											<td background="module/aim/images/corner-r.gif" height="17" width=17>
												&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellSpacing=1 width="100%">
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<digi:link href="/workspaceManager.do">
												<digi:trn key="aim:teams">
												Teams
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<digi:link href="/roles.do">
												<digi:trn key="aim:roles">
												Roles
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<digi:link href="/updateRole.do">
												<digi:trn key="aim:addRole">
												Add Roles
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<digi:link href="/admin.do">
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
</td></tr>
</table>
</digi:form>
