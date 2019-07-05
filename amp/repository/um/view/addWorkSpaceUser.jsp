<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<style  type="text/css">
<!--

.contentbox_border{
        border: 1px solid #CCCCCC;
	border-width: 1px 1px 1px 1px; 
	background-color: #f2f2f2;
}

#statesautocomplete ul {
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}

#statesautocomplete div {
	padding: 0px;
	margin: 0px; 
}



#statesautocomplete,
#statesautocomplete2 {
    width:15em; /* set width here */
    padding-bottom:2em;
}
#statesautocomplete {
    z-index:9000; /* z-index needed on top instance for ie & sf absolute inside relative issue */
}
#statesinput,
#statesinput2 {
    _position:absolute; /* abs pos needed for ie quirks */
}
.charcounter {
    display: block;
}

-->
</style>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>

<script language="JavaScript">
	function deleteWS(id){
		document.umAddUserForm.teamMemberId.value=id;
		document.umAddUserForm.actionFlag.value = "deleteWS";
		<digi:context name="selectType" property="context/module/moduleinstance/addWorkSpaceUser.do" />
		document.umAddUserForm.action = "<%= selectType %>";
		document.umAddUserForm.target = "_self";
		document.umAddUserForm.submit();
	}
	function cancel()
	{
		document.umAddUserForm.action = "/um/addUser.do";
		document.umAddUserForm.firstNames.value="";
		document.umAddUserForm.lastName.value="";
		document.umAddUserForm.email.value="";
		document.umAddUserForm.emailConfirmation.value="";
		document.umAddUserForm.password.value="";
		document.umAddUserForm.passwordConfirmation.value="";
		document.umAddUserForm.role.value="-1";
		document.umAddUserForm.teamId.value="-1"
		document.umAddUserForm.addWorkspace.value="false";
		document.umAddUserForm.selectedOrgType.value="-1";
		document.umAddUserForm.selectedOrgGroup.value="-1";
		document.umAddUserForm.selectedOrganizationId.value="-1";
		document.umAddUserForm.sendEmail.value="-1";

		document.umAddUserForm.target = "_self";
		document.umAddUserForm.submit();
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
	function validate()
	{
		if(document.umAddUserForm.teamId.value=="-1"){
			<c:set var="translation">
			<digi:trn key="aim:chooseTeam">Please choose workspace</digi:trn>
    		</c:set>
			
			alert("${translation}");
			document.umAddUserForm.teamId.focus();
			return false;
		}	
		if(document.umAddUserForm.role.value=="-1"){
			<c:set var="translation">
			<digi:trn key="aim:chooseRole">Please choose role</digi:trn>
    		</c:set>
			
			alert("${translation}");
			document.umAddUserForm.role.focus();
			return false;
		}	
		return true;
	}

</script>





<digi:instance property="umAddUserForm" />

<digi:form action="/addWorkSpaceUser.do" method="post" onsubmit="return validateAimUserRegisterForm(this);">


<html:hidden property="orgType" />

<html:hidden property="orgGrp" />

<input type="hidden" name="actionFlag" value="">
<input type="hidden" name="teamMemberId" />
  <table bgColor=#ffffff cellPadding=5 cellspacing="1" width=1000 align="center">
	<tr>
		<td class="left-align" valign="top" width=752>
			<table cellPadding=0 cellspacing="0" width="1000">
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>

						<c:set var="translation">
				            <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
				          </c:set>
				          <digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}" >
				            <digi:trn key="aim:AmpAdminHome">
				            Admin Home
				            </digi:trn>
				          </digi:link>&nbsp;&gt;&nbsp;
				
				          <c:set var="translation">
				            <digi:trn key="aim:clickToViewAllUsers">Click here to goto users manager</digi:trn>
				          </c:set>
				          <digi:link href="/viewAllUsers.do" styleClass="comment" title="${translation}" >
				            <digi:trn key="aim:viewAllUsers">
				            List of users
				            </digi:trn>
				          </digi:link>&nbsp;&gt;&nbsp;

				          <c:set var="translation">
				            <digi:trn key="aim:clickToViewAddUser">Click here to goto add new user</digi:trn>
				          </c:set>
				          <digi:link href="/addUser.do" styleClass="comment" title="${translation}" >
				            <digi:trn key="aim:addNewUser">
				            Add new user
				            </digi:trn>
				          </digi:link>&nbsp;&gt;&nbsp;
				
				          <digi:trn key="aim:viewEditUser:AddWorkspace">
				          Add Workspace
				          </digi:trn>
                      </span>
					</td>
					<!-- End navigation -->
				</tr>
			    <tr>
			      <td colspan="2" height=25 style="padding-bottom:10px;padding-top:10px;">
			        <span class=subtitle-blue>
			          <digi:trn key="aim:viewEditUser:AddWorkspace">
			          Add Workspace
			          </digi:trn>
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
							       <td style="background-color: #c7d4db; height: 25px;" align=center><b>New User</b>
							    </tr>
							 </table>
						  </td>
					   </tr>
					   <tr>
						  <td valign="top" bgcolor="#F2F2F2" align="center">
					
							<table border="0" cellpadding="0" cellspacing="0" width=772>
								<tr>
									<td class="left-align" valign="top" width=520>
									
										<table border="0" cellPadding=5 cellspacing="0" width="100%">
											<tr>
												<td width="3%">&nbsp;</td>
												<td class="left-align" noWrap colspan="2">
													<digi:errors/>
													<logic:notEmpty name="umAddUserForm" property="errors" >
							                        <font color="red">
														<ul>
							                            <logic:iterate id="element"	name="umAddUserForm" property="errors">
							                               <li><digi:trn key="${element.key}">
							                                   <bean:write name="element" property="value"/>
							                               </digi:trn></li>
							                           </logic:iterate>
							                           </ul>
							                        </font>
							                        </logic:notEmpty>
												</td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td class="f-names right-align padding-5" noWrap width="40%">
													<digi:trn key="um:firstName">First Name	</digi:trn>
												</td>
												<td class="left-align">
													<bean:write name="umAddUserForm" property="firstNames"/>
												</td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td class="f-names right-align padding-5" noWrap width="40%">
													<digi:trn key="um:lastName">Last Name </digi:trn>
												</td>
												<td class="left-align">
													<bean:write name="umAddUserForm" property="lastName"/>
												</td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td class="f-names right-align padding-5" noWrap width="40%">
													<digi:trn key="um:emailAddress">E-mail Address </digi:trn>
												</td>
												<td class="left-align">
													<bean:write name="umAddUserForm" property="email"/>
												</td>
											</tr>
							
											
											<tr>
												<td>
													<html:hidden property="firstNames"/>
													<html:hidden property="lastName"/>
													<html:hidden property="email"/>
													<html:hidden property="emailConfirmation"/>
													<html:hidden property="password"/>
													<html:hidden property="passwordConfirmation"/>
							                        <html:hidden property="selectedCountryResidence"/>
													<html:hidden property="selectedOrgGroup"/>
													<html:hidden property="organizationName"/>
													<html:hidden property="selectedOrgType"/>
													<html:hidden property="selectedOrganizationId"/>
							                        <html:hidden property="selectedLanguage"/>
							                        <html:hidden property="sendEmail"/>
							                        <html:hidden property="addWorkspace"/>
							                        
												</td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
													<td class="right-align padding-5" valign="top" width="30%">
														<digi:trn key="aim:workspaceId">Workspace Name</digi:trn>&nbsp;
													</td>
												<td class="left-align" width="70%">
													<html:select property="teamId"  onchange="hideUnhideApproverRole(this)">
													<html:option value="-1">-- <digi:trn key="um:selectWorkspace">Select a workspace</digi:trn> --</html:option>
													<c:forEach items="${umAddUserForm.workspaces}" var="workspaces">
														<html:option value="${workspaces.ampTeamId}"  styleClass='${workspaces.accessType}'>
															<c:out value="${workspaces.name}"/>
														</html:option>
													</c:forEach>
													</html:select>
												</td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td class="right-align padding-5" valign="top" width="30%">
													<digi:trn key="aim:RoleId">User Role</digi:trn>&nbsp;
												</td>
												<td class="left-align" width="70%">
													<html:select property="role" styleId="team_member_roles_drop_down">
													<html:option value="-1">-- <digi:trn key="um:selectRole">Select a role</digi:trn> --</html:option>
													
													<logic:iterate name="umAddUserForm" property="ampRoles" id="ampRole" type="org.digijava.module.aim.dbentity.AmpTeamMemberRoles">
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
												<td class="right-align padding-5">
							                        <c:set var="btnSubmit">
							                              <digi:trn key="btn:submit">Submit</digi:trn>
							                        </c:set>
													<html:submit value="${btnSubmit}" styleClass="dr-menu" onclick="return validate()" />
												</td>
												<td class="left-align">
							                        <c:set var="btnDone">
							                              <digi:trn key="btn:done">Done</digi:trn>
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
										<c:if test="${!empty umAddUserForm.assignedWorkspaces && umAddUserForm.assignedWorkspaces != null}">
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
		    							<logic:iterate name="umAddUserForm"  property="assignedWorkspaces" id="assignedWS" indexId="idx">
		                              	<tr bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" onmouseout="setPointer(this, <%=idx.intValue()%>, 'out', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" 
		                              	onmouseover="setPointer(this, <%=idx.intValue()%>, 'over', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" style="" >                           
		                              	<td bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" class="reportsBorderTD">
										<c:out value="${assignedWS.ampTeam.name}"/>
										</td>
										<td>
										<c:out value="${assignedWS.ampMemberRole.description}"/>
										</td>
										<td align="center">
										<a href="javascript:deleteWS(<c:out value="${assignedWS.ampTeamMemId}"/>)" title="<digi:trn key="aim:ClickDeleteUserFromWS">Click on this icon to remove user from the workspace</digi:trn>"><img  src="/repository/message/view/images/trash_12.gif" border="0" hspace="2"/></a>
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






