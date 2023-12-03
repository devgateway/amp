<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/arFunctions.js"/>"></script>

<script language="JavaScript">
	function deleteWS(id){
		document.umViewEditUserForm.teamMemberId.value=id;
		document.umViewEditUserForm.action = "/um/viewEditUser.do?event=deleteWS&wId="+id;
		document.umViewEditUserForm.target = "_self";
		document.umViewEditUserForm.submit();
	}
	function cancel() {
		document.umViewEditUserForm.action = "/um/viewEditUser.do?addWorkspace=false";
		document.umViewEditUserForm.target = "_self";
		document.umViewEditUserForm.submit();
		return false;
	}
	
	var removedApproverRole;
	
	function hideUnhideApproverRole(teamSelect){
		var index=teamSelect.selectedIndex;
		var type=teamSelect.options[index].className;
		var approverRole=document.getElementById('role_approver_true');
		var rolesDropDown=document.getElementById('team_member_roles_drop_down');
		if(type=='Team'){
			if(approverRole==null&&removedApproverRole!=null){
				rolesDropDown.appendChild(removedApproverRole);
			}
		}
		else{
			if(approverRole!=null){
				removedApproverRole=rolesDropDown.removeChild(approverRole);
			}
		}
	}

	function validate() {
		if(document.umViewEditUserForm.teamId.value=="-1"){
			<c:set var="translation">
				<digi:trn>Please choose workspace</digi:trn>
    		</c:set>
			
			alert("${translation}");
			document.umViewEditUserForm.teamId.focus();
			return false;
		}	
		if(document.umViewEditUserForm.role.value=="-1"){
			<c:set var="translation">
				<digi:trn>Please choose role</digi:trn>
    		</c:set>
			
			alert("${translation}");
			document.umViewEditUserForm.role.focus();
			return false;
		}	
		return true;
	}

</script>

<digi:instance property="umViewEditUserForm" />
<digi:form name="umViewEditUserForm" action="/viewEditUser.do?event=assignWorkspaceToUser" method="post">

<input type="hidden" name="teamMemberId" />
  <table bgColor=#ffffff cellPadding=5 cellspacing="1" width=1000 align="center">
	<tr>
		<td align=left valign="top" width=752>
			<table cellPadding=0 cellspacing="0" width="1000">
			    <tr>
			      <td colspan="2" height=25 style="padding-bottom:10px;padding-top:10px;">
			        <span class=subtitle-blue>
			          <digi:trn>Add Workspace</digi:trn>
			        </span>
			      </td>
			    </tr>
				<tr>
				<td noWrap width=1000 vAlign="top">
					<table class="contentbox_border" width="1000" border="0" bgcolor="#f4f4f2">
					   <tr>			
					      <td align="center">
						     <table width="100%">
							    <tr>
							       <td style="background-color: #c7d4db; height: 25px;" align=center><b><digi:trn>New User</digi:trn></b>
							    </tr>
							 </table>
						  </td>
					   </tr>
					   <tr>
						  <td valign="top" bgcolor="#F2F2F2" align="center">
					
							<table border="0" cellpadding="0" cellspacing="0" width=772>
								<tr>
									<td align=left valign="top" width=520>
									
										<table border="0" cellPadding=5 cellspacing="0" width="100%">
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=left noWrap colspan="2">											
													<digi:errors/>
												</td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=right class=f-names noWrap width="40%">
													<digi:trn key="um:firstName">First Name	</digi:trn>
												</td>
												<td align="left">
													<bean:write name="umViewEditUserForm" property="firstNames"/>
												</td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=right class=f-names noWrap width="40%">
													<digi:trn key="um:lastName">Last Name </digi:trn>
												</td>
												<td align="left">
													<bean:write name="umViewEditUserForm" property="lastName"/>
												</td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=right class=f-names noWrap width="40%">
													<digi:trn key="um:emailAddress">E-mail Address </digi:trn>
												</td>
												<td align="left" id="mailTD">
													<bean:write name="umViewEditUserForm" property="email"/>
												</td>
											</tr>
							
											
											<tr>
												<td>
													<html:hidden name="umViewEditUserForm" property="firstNames"/>
													<html:hidden name="umViewEditUserForm" property="lastName"/>
													<html:hidden name="umViewEditUserForm" property="email"/>							                        
							                        <html:hidden name="umViewEditUserForm" property="addWorkspace"/>
							                        
												</td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
													<td align="right" valign="top" width="30%">
														<digi:trn key="aim:workspaceId">Workspace Name</digi:trn>&nbsp;
													</td>
												<td align="left" width="70%">
													<html:select property="teamId"  onchange="hideUnhideApproverRole(this)">
													<html:option value="-1">-- <digi:trn key="um:selectWorkspace">Select a workspace</digi:trn> --</html:option>
													<c:forEach items="${umViewEditUserForm.workspaces}" var="workspaces">
														<html:option value="${workspaces.ampTeamId}"  styleClass='${workspaces.accessType}'>
															<c:out value="${workspaces.name}"/>
														</html:option>
													</c:forEach>
													</html:select>
												</td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align="right" valign="top" width="30%">
													<digi:trn key="aim:RoleId">User Role</digi:trn>&nbsp;
												</td>
												<td align="left" width="70%">
													<html:select property="role" styleId="team_member_roles_drop_down">
													<html:option value="-1">-- <digi:trn key="um:selectRole">Select a role</digi:trn> --</html:option>
													
													<logic:iterate name="umViewEditUserForm" property="ampRoles" id="ampRole" type="org.digijava.ampModule.aim.dbentity.AmpTeamMemberRoles">
														<html:option value="${ampRole.ampTeamMemRoleId}" styleId="role_approver_${ampRole.approver}">
															<digi:trn key="<%=ampRole.getAmpTeamMemberKey() %>">
																<bean:write name="ampRole" property="role" />
															</digi:trn>
														</html:option>
													</logic:iterate>
													</html:select>
												</td>
											</tr>
								
											<tr>
												<td>&nbsp;</td>
												<td align="right">
							                        <c:set var="btnSubmit">
							                              <digi:trn>Submit</digi:trn>
							                        </c:set>
													<html:submit value="${btnSubmit}" styleClass="dr-menu" onclick="return validate()" />
												</td>
												<td align="left">
							                        <c:set var="btnDone">
							                              <digi:trn>Done</digi:trn>
							                        </c:set>
													<html:submit value="${btnDone}" styleClass="dr-menu" onclick="return cancel()"/>
												</td>
												
											</tr>
											<tr>
												<td colspan=3>&nbsp;</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td align="center" valign="top" width=520><br>
		             					<table border="0" cellPadding=5 cellspacing="0" width="80%">
										<c:if test="${!empty umViewEditUserForm.assignedWorkspaces && umViewEditUserForm.assignedWorkspaces != null}">
										<tr bgColor=#999999>
										<td bgColor=#999999 align="center" height="20">
										<b><digi:trn key="um:addWorkspaces:wsname">Workspace</digi:trn></b>
										</td>
										<td bgColor=#999999 align="center" height="20">
										<b><digi:trn key="um:addWorkspaces:wsrole">Role</digi:trn></b>
										</td>
										<td bgColor=#999999 align="center" height="20">
										<b><digi:trn key="um:addWorkspaces:wsaction">Action</digi:trn></b>
										</td>
										</tr>
		    							<logic:iterate name="umViewEditUserForm"  property="assignedWorkspaces" id="assignedWS" indexId="idx">
			                              	<tr bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" onmouseout="setPointer(this, <%=idx.intValue()%>, 'out', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" 
			                              	onmouseover="setPointer(this, <%=idx.intValue()%>, 'over', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" style="" >                           
				                              	<td bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" class="reportsBorderTD">
												<c:out value="${assignedWS.ampTeam.name}"/>
												</td>
												<td>
												<digi:trn><c:out value="${assignedWS.ampMemberRole.description}"/></digi:trn>
												</td>
												<td align="center">
													<a href="javascript:deleteWS(<c:out value="${assignedWS.ampTeamMemId}"/>)" title="<digi:trn>Click on this icon to remove user from the workspace</digi:trn>">
														<img  src="/repository/message/view/images/trash_12.gif" border="0" hspace="2"/>
													</a>
												</td>
											</tr>
										</logic:iterate>
										</c:if>
										</table>
									</td>
								</tr>
							</table>
							<br /><br />
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
