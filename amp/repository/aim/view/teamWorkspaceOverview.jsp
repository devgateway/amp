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
            var msg="<digi:trn jsFriendly="true">Workspace type must be 'Management' to add teams</digi:trn>";
			alert(escape(msg));
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
	if(validateMandatoryFields ()){
		var id = document.aimUpdateWorkspaceForm.teamId.value;
		<digi:context name="update" property="context/module/moduleinstance/updateWorkspaceForTeam.do" />
		document.aimUpdateWorkspaceForm.action = "<%=update%>?dest=teamLead&event="+action+"&tId="+id;
		document.aimUpdateWorkspaceForm.target = "_self";
		document.aimUpdateWorkspaceForm.submit();	
	}
	
}

function updateChild(action) {

		var id = document.aimUpdateWorkspaceForm.teamId.value;
		<digi:context name="update" property="context/module/moduleinstance/updateWorkspaceForTeam.do" />
		document.aimUpdateWorkspaceForm.action = "<%=update%>?dest=teamLead&event="+action+"&tId="+id + "&subtab=2";
		document.aimUpdateWorkspaceForm.target = "_self";
		document.aimUpdateWorkspaceForm.submit();
	
	
}

function validateMandatoryFields (){
	var workspaceName = document.getElementById("teamName").value;
	var grpIndex=document.aimUpdateWorkspaceForm.workspaceGroup.selectedIndex;
	var grpValue	= document.aimUpdateWorkspaceForm.workspaceGroup.options[grpIndex].value;
	if(trim(workspaceName) == ''){
		var msg = '<digi:trn jsFriendly="true">Please enter workspace name</digi:trn>';
		alert(msg);
		return false;
	}
	if(grpValue=='0'){
		var msg = '<digi:trn jsFriendly="true">Please select workspace group</digi:trn>';
		alert(msg);
		return false;
		
	}
	return true;
}

function trim(stringToTrim) {
	return stringToTrim.replace(/^\s+|\s+$/g,"");
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

<table cellspacing="0" cellpadding="0" vAlign="top" align="left" width="100%">
	<tr>
		<td width="100%">
<jsp:include page="teamPagesHeader.jsp"  />
		</td>
	</tr>
	<tr>
		<td>

			<c:set var="selectedTab" value="0" scope="request"/>
			<c:set var="selectedSubTab" scope="request">
				<%=request.getParameter("subtab") == null ? "0": request.getParameter("subtab") %>
			</c:set>
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
			
			<table width="1000px" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td height=33>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</c:set>
						<div class="breadcrump_cont">
							<span class="sec_name">
								<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
							</span>
							
							<span class="breadcrump_sep">|</span>
							<digi:link href="/viewMyDesktop.do" title="${translation}" styleClass="l_sm">
								<digi:trn key="aim:portfolio">Portfolio</digi:trn>
							</digi:link>
							<span class="breadcrump_sep"><b>�</b></span>
							<span class="bread_sel"><digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></span>
						</div>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
							<jsp:include page="teamSetupMenu.jsp" flush="true"/>

								<c:if test="${subtabId == 0 }">
									<table class="inside" width="970" cellpadding="0" cellspacing="0">
										<tr>
										    <td width=30% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside"><b class="ins_title"><digi:trn>Team name</digi:trn></b></td>
										    <td width=40% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside"><b class="ins_title"><digi:trn>Team Description</digi:trn></b></td>
										    <td width=30% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside"><b class="ins_title"><digi:trn>Workspace Type</digi:trn></b></td>
										</tr>
										<logic:equal name="aimUpdateWorkspaceForm" property="updateFlag" value="true">
										<tr>
											<td class="inside" colspan="3" align="center">
												<font color="blue"><b>
													<digi:trn key="aim:updateToAMPComplete">
														Update to AMP Complete
													</digi:trn></b>
												</font>
											</td>	
										</tr>
										</logic:equal>
										<tr>
										    <td class="inside" valign="top">
										    	<html:text property="teamName" size="50" styleClass="inputx insidex" styleId="teamName"/>
										    </td>
										    <td class="inside" valign="top">
										    	<html:textarea property="description" rows="3" cols="50" styleClass="inputx insidex"/>
										    </td>
										    <td class="inside" valign="top">
										    	<html:select property="workspaceType" styleClass="inputx insidex" disabled="true">
														<html:option value="">-- <digi:trn key="aim:selectType">Select Type</digi:trn> --</html:option>
														<html:option value="Donor"><digi:trn key="aim:donor">Donor</digi:trn></html:option>
														<html:option value="Management"><digi:trn key="aim:management">Management</digi:trn></html:option>
														<html:option value="Team"><digi:trn key="aim:team">Team</digi:trn></html:option>
													</html:select>
										    </td>
										</tr>
														<c:set var="translation">
															<digi:trn>Please select from below</digi:trn>
														</c:set>															
														<tr style="background:#f4f4f2;">
															<td class="inside" align="center" bgcolor="#f4f4f2" colspan="3">
															<div style="text-align:center;">
																<font color="red"><b>*</b></font>
																<digi:trn key="aim:workspaceGroup">Workspace Group</digi:trn>
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
																</logic:equal></div>
															</td>
														</tr>
										<logic:notEmpty name="aimUpdateWorkspaceForm" property="relatedTeamName" scope="session" >
											<tr>
												<td class="inside" colspan="3">
													<digi:trn key="aim:relatedTeam">
													Related Team
													</digi:trn>
													: <bean:write name="aimUpdateWorkspaceForm" property="relatedTeamName" scope="session" />
												</td>
											</tr>
										</logic:notEmpty>
										<tr>
											<td colspan="3">
												
											</td>	
										</tr>
									</table>
									
									<c:set var="translation">
										<digi:trn key="aim:btnSave">Save</digi:trn>
									</c:set>
									<br>
									<div align="center">
										<input type="button" class="buttonx_sm btn_save" value=" ${translation} " onclick="update('edit')"/>
									</div>
								</c:if>
								<c:if test="${subtabId == 2 }">
									<html:hidden name="aimUpdateWorkspaceForm" property="workspaceType"/>
									<table  width="100%" cellpadding="0" cellspacing="0">
											<tr>
											<td colspan="2">
												<digi:errors />
											</td>
										</tr>
										<logic:equal name="aimUpdateWorkspaceForm" property="updateFlag" value="true">
											<tr>
												<td class="inside" colspan="2" align="center">
													<font color="blue"><b>
														<digi:trn key="aim:updateToAMPComplete">Update to AMP Complete</digi:trn></b>
													</font>
												</td>	
										</tr>
										</logic:equal>
										<tr>
											<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
												<b class="ins_title">
													<digi:trn key="aim:childWorkspaces">Child Workspaces</digi:trn>
												</b>
											</td>
											<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside" width="50" align="center">
												<c:set var="translation">
													<digi:trn key="btn:teamWorkspaceAddChildWorkspace">Add</digi:trn>
												</c:set>
												<input type="button" value="${translation}" class="buttonx_sm btn_save" onclick="addChildWorkspaces()">
											</td>
										</tr>
										<c:if test="${!empty aimUpdateWorkspaceForm.childWorkspaces}">
										<tr>
											<td class="inside" colspan="2">
												<table width="100%" class="inside normal" cellpadding="2" cellspacing="0">
													<c:forEach var="workspaces" items="${aimUpdateWorkspaceForm.childWorkspaces}">
														<tr>
															<td class="inside" align="left">&nbsp;
																<c:out value="${workspaces.name}"/>
															</td>
															<td class="inside" align="right" width="10" valign="center">
																<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'delete'}">
																	<a href="javascript:removeChildWorkspace(<c:out value="${workspaces.ampTeamId}"/>)">
																		<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Remove this child workspace"/>
																	</a>
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
													<table width="98%" cellPadding=2 cellspacing="0" valign="top" align="center" class="box-border-nopadding">
														<tr>
															<td align="left">
																<digi:trn key="aim:noChildTeams">No child teams</digi:trn>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</c:if>
									</table>
									<c:set var="translation">
										<digi:trn key="btn:teamWorkspaceUpdate">Update</digi:trn>
									</c:set>
									<br>
									<div align="center">
										<input type="button" class="buttonx_sm btn_save" value=" ${translation} " onclick="updateChild('edit')"/>
									</div>
									</c:if>												
									
								</td>
							</tr>
						</table>
						</div>
						</div>											
						
					</td>
				</tr>
			</table>

		</td>
	</tr>
</table>
</digi:form>



