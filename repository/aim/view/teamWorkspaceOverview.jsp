<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<style>
.contentbox_border{
	border: 	1px solid #666666;
	width: 		750px;
	background-color: #f4f4f2;
}
</style>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript">
<!--

function addChildWorkspaces() {
		if (document.aimUpdateWorkspaceForm.workspaceType.value != "Team") {
			openNewWindow(650, 380);
			<digi:context name="addChild" property="context/module/moduleinstance/addChildWorkspaces.do" />
			document.aimUpdateWorkspaceForm.action = "<%=addChild%>?dest=teamLead";
			document.aimUpdateWorkspaceForm.target = popupPointer.name;
			document.aimUpdateWorkspaceForm.submit();
		} else {
                  <c:set var="message">
                  <digi:trn key="aim:teamWorkspaceSetup:addChildWorkspace">
                  Workspace type must be 'Management' to add teams
                  </digi:trn>
                  </c:set>
			alert("${message}");
			return false;
		}
}

function removeChildWorkspace(id) {
	<digi:context name="update" property="context/module/moduleinstance/removeChildWorkspace.do" />
	document.aimUpdateWorkspaceForm.action = "<%=update%>?dest=teamLead&tId="+id;
	document.aimUpdateWorkspaceForm.target = "_self";
	document.aimUpdateWorkspaceForm.submit();
}

function update(action) {
	var id = document.aimUpdateWorkspaceForm.teamId.value;
	<digi:context name="update" property="context/module/moduleinstance/updateWorkspaceForTeam.do" />
	document.aimUpdateWorkspaceForm.action = "<%=update%>?dest=teamLead&event="+action+"&tId="+id;
	document.aimUpdateWorkspaceForm.target = "_self";
	document.aimUpdateWorkspaceForm.submit();
}

function updateChild(action) {

		var id = document.aimUpdateWorkspaceForm.teamId.value;
		<digi:context name="update" property="context/module/moduleinstance/updateWorkspaceForTeam.do" />
		document.aimUpdateWorkspaceForm.action = "<%=update%>?dest=teamLead&event="+action+"&tId="+id;
		document.aimUpdateWorkspaceForm.target = "_self";
		document.aimUpdateWorkspaceForm.submit();
	
	
}

-->
</script>

<digi:instance property="aimUpdateWorkspaceForm" />
<digi:form action="/updateWorkspaceForTeam.do" method="post">

<html:hidden property="teamId" />
<html:hidden property="actionEvent" />
<html:hidden property="id" />
<html:hidden property="mainAction" />

<logic:notPresent parameter="subtab"> 
	<bean:define id="subtabId" value="0"/>
</logic:notPresent>
<logic:present parameter="subtab">
	<bean:parameter id="subtabId" name="subtab" />
</logic:present>

<table cellSpacing=0 cellPadding=0 vAlign="top" align="left" width="100%">
<tr><td width="100%">
<jsp:include page="teamPagesHeader.jsp"  />
</td></tr>
<tr><td>
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=780 border="0">
	<tr>
		<td width=14>&nbsp;</td>
		<td align=left vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</c:set>
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:portfolio">
						Portfolio
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:teamWorkspaceSetup">
						Team Workspace Setup
						</digi:trn>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class=subtitle-blue>
							<digi:trn key="aim:teamWorkspaceSetup">
								Team Workspace Setup
							</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td noWrap vAlign="top">
						<table cellPadding=0 cellSpacing=0 width="100%"	valign="top" align="left">
							<tr>
								<td vAlign="top" width="100%">
									<c:set var="selectedTab" value="0" scope="request"/>
									<c:set var="selectedSubTab" value="<%=request.getParameter("subtab") == null ? "0": request.getParameter("subtab") %>" scope="request"/>
									<c:choose>
										<c:when test="${aimUpdateWorkspaceForm.workspaceType != 'Management' }">
											<c:set var="childWorkspaces" value="disabled" scope="request" />
										</c:when>
										<c:otherwise>
											<c:set var="childWorkspaces" value="enabled" scope="request" />
										</c:otherwise>
									</c:choose>
									<c:set var="teamAccessTypeLocal" scope="session"><bean:write name="aimUpdateWorkspaceForm" property="teamAccessType"/>
									</c:set>
									<jsp:include page="teamSetupMenu.jsp" />
								</td>
							</tr>
							<tr>
								<td valign="top">
                                <div class="contentbox_border" style="border-top:0px;padding: 20px 0px 20px 0px;">
                                	<table align=center cellPadding=0 cellSpacing=0 width="90%">
										<tr>
											<td style="padding-left:10px">
												<table border=0 cellPadding=3 cellSpacing=0 width="500">
													<logic:equal name="aimUpdateWorkspaceForm" property="updateFlag" value="true">
													<tr>
														<td colspan="2" align="center">
															<font color="blue"><b>
															<digi:trn key="aim:updateToAMPComplete">
																Update to AMP Complete
															</digi:trn></b></font>
														</td>
													</tr>
													</logic:equal>
													<tr>
														<td colspan="2" align="center">
															<digi:errors/>
														</td>
													</tr>

													<c:if test="${subtabId == 0 }">
													
														<tr>
															<td align="right" width="50%">
																<digi:trn key="aim:teamName">
																Team Name
																</digi:trn>
															</td>
															<td align="left">
																<html:text property="teamName" size="50" styleClass="inp-text" />
															</td>
														</tr>
														<tr>
															<td align="right" width="50%" valign="top">
																<digi:trn key="aim:teamDescription">
																Team Description
																</digi:trn>
															</td>
															<td align="left" >
																<html:textarea property="description" rows="3" cols="50" styleClass="inp-text"/>
															</td>
														</tr>
														<c:set var="translation">
															<digi:trn>Please select from below</digi:trn>
														</c:set>															
														<tr>
															<td width="150" align="right" bgcolor="#f4f4f2">
																<font color="red"><b>*</b></font>
																<digi:trn key="aim:workspaceGroup">Workspace Group</digi:trn>
															</td>
															<td align="left" bgcolor="#f4f4f2">
																<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="delete">
																	<b><bean:write name="aimUpdateWorkspaceForm" property="workspaceGroup" /></b>
																</logic:equal>
																<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="view">
																	<b><bean:write name="aimUpdateWorkspaceForm" property="workspaceGroup" /></b>
																</logic:equal>
																<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="edit">
																	<category:showoptions firstLine="${translation}" name="aimUpdateWorkspaceForm" property="workspaceGroup" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.WORKSPACE_GROUP_KEY %>" styleClass="inp-text" />
																</logic:equal>
																<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="add">
																	<category:showoptions firstLine="${translation}" name="aimUpdateWorkspaceForm" property="workspaceGroup" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.WORKSPACE_GROUP_KEY %>" styleClass="inp-text" />
																</logic:equal>
															</td>
														</tr>
														<logic:notEmpty name="aimUpdateWorkspaceForm" property="relatedTeamName" scope="session" >
														<tr>
															<td align="right" width="50%">
																<digi:trn key="aim:relatedTeam">
																Related Team
																</digi:trn>
															</td>
															<td align="left">
																<bean:write name="aimUpdateWorkspaceForm" property="relatedTeamName" scope="session" />
															</td>
														</tr>
														</logic:notEmpty>
	
														
															<td align="right">
																<digi:trn key="aim:workspaceType">Workspace Type</digi:trn>
															</td>
															<td align="left">
																<html:select property="workspaceType" styleClass="inp-text" disabled="true">
																	<html:option value="">-- <digi:trn key="aim:selectType">Select Type</digi:trn> --</html:option>
																	<html:option value="Donor"><digi:trn key="aim:donor">Donor</digi:trn></html:option>
																	<html:option value="Management"><digi:trn key="aim:management">Management</digi:trn></html:option>
																	<html:option value="Team"><digi:trn key="aim:team">Team</digi:trn></html:option>
																</html:select>
															</td>
														</tr>
														<tr>
															<c:set var="translation">
																<digi:trn key="aim:btnSave">Save</digi:trn>
															</c:set>
															<td colspan="2" align="center">
																<input type="button" class="dr-menu" value=" ${translation} " onclick="update('edit')"/>
															</td>																	
														</tr>													
													</c:if>
													<c:if test="${subtabId == 2 }">

														<html:hidden name="aimUpdateWorkspaceForm" property="workspaceType"/>
														<html:hidden name="aimUpdateWorkspaceForm" property="category"/>
														<tr>
															<td align="right" width="150">
																<digi:trn key="aim:childWorkspaces">Child Workspaces</digi:trn>
															</td>
															<td align="left">
																<c:set var="translation">
																	<digi:trn key="btn:teamWorkspaceAddChildWorkspace">Add</digi:trn>
																</c:set>
																<input type="button" value="${translation}" class="dr-menu" onclick="addChildWorkspaces()">
															</td>
														</tr>
														<c:if test="${!empty aimUpdateWorkspaceForm.childWorkspaces}">
														<tr>
															<td colspan="2" align="center">
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
																		 	<digi:img src="../ampTemplate/images/deleteIcon.gif"
																			border="0" alt="Remove this child workspace"/></a>&nbsp;
																			</c:if>
																		</td>
																	</tr>
																</c:forEach>
																</table>
															</td>
														</tr>
														</c:if>
														<c:if test="${empty aimUpdateWorkspaceForm.childWorkspaces}">
														<tr>
															<td colspan="2" align="center">
																<table width="98%" cellPadding=2 cellSpacing=0 valign="top" align="center"
																class="box-border-nopadding">
																<tr>
																	<td align="left">
																		<digi:trn key="aim:noChildTeams">No child teams</digi:trn>
																	</td>
																</tr>
																</table>
															</td>
														</tr>
														</c:if>
														<tr><td>&nbsp;</td></tr>
														<tr>
															<c:set var="translation">
																<digi:trn key="btn:teamWorkspaceUpdate">Update</digi:trn>
															</c:set>
															<td colspan="2" align="center">
																<input type="button" class="dr-menu" value=" ${translation} " onclick="updateChild('edit')"/>
															</td>																	
														</tr>
													</c:if>
												</table>
											</td>
										</tr>
									</table>
                                </div>
                                </td>
							</tr>
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



