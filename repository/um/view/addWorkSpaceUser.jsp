<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">

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
  <table bgColor=#ffffff cellPadding=5 cellSpacing=1 width=705>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=752>
			<table cellPadding=5 cellSpacing=0 width="100%">
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
			      <td colspan="2">
			        <span class=subtitle-blue>
			          <digi:trn key="aim:viewEditUser:AddWorkspace">
			          Add Workspace
			          </digi:trn>
			        </span>
			      </td>
			    </tr>
				<tr>
				<td noWrap width=616 vAlign="top">
					<table width="100%" valign="top" align="left" cellpadding=0 cellSpacing=0 border=0>
					<tr><td width="100%" valign="top" align="left">
					<table bgColor=#ffffff border=0 cellPadding=0 cellSpacing=0 width=772>
						<tr>
							<td width=14>&nbsp;
							</td>
							<td align=left vAlign=top width=520><br>
								<table border=0 cellPadding=5 cellSpacing=0 width="100%">
									<tr>
										<td width="3%">&nbsp;</td>
										<td align=left class=title noWrap colspan="2">
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
										<td align=right class=f-names noWrap width="40%">
											<digi:trn key="um:firstName">First Name	</digi:trn>
										</td>
										<td align="left">
											<bean:write name="umAddUserForm" property="firstNames"/>
										</td>
									</tr>
									<tr>
										<td width="3%">&nbsp;</td>
										<td align=right class=f-names noWrap width="40%">
											<digi:trn key="um:lastName">Last Name </digi:trn>
										</td>
										<td align="left">
											<bean:write name="umAddUserForm" property="lastName"/>
										</td>
									</tr>
									<tr>
										<td width="3%">&nbsp;</td>
										<td align=right class=f-names noWrap width="40%">
											<digi:trn key="um:emailAddress">E-mail Address </digi:trn>
										</td>
										<td align="left">
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
											<td align="right" valign="top" width="30%">
												<digi:trn key="aim:workspaceId">Workspace Name</digi:trn>&nbsp;
											</td>
										<td align="left" width="70%">
											<html:select property="teamId" >
											<html:option value="-1">-- <digi:trn key="um:selectWorkspace">Select a workspace</digi:trn> --</html:option>
											<c:forEach items="${umAddUserForm.workspaces}" var="workspaces">
												<html:option value="${workspaces.ampTeamId}">
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
											<html:select property="role" >
											<html:option value="-1">-- <digi:trn key="um:selectRole">Select a role</digi:trn> --</html:option>
											<c:forEach items="${umAddUserForm.ampRoles}" var="roles">
												<html:option value="${roles.ampTeamMemRoleId}">
													<c:out value="${roles.description}"/>
												</html:option>
											</c:forEach>
											</html:select>
										</td>
									</tr>
						
									<tr>
										<td>&nbsp;</td>
										<td align="right">
					                        <c:set var="btnSubmit">
					                              <digi:trn key="btn:submit">Submit</digi:trn>
					                        </c:set>
											<html:submit value="${btnSubmit}" styleClass="dr-menu" onclick="return validate()" />
										</td>
										<td align="left">
					                        <c:set var="btnCancel">
					                              <digi:trn key="btn:cancel">Cancel</digi:trn>
					                        </c:set>
											<html:submit value="${btnCancel}" styleClass="dr-menu" onclick="return cancel()"/>
										</td>
										
									</tr>
									<tr>
										<td colspan=3>&nbsp;</td>
									</tr>
								</table>
							</td>
							<td vAlign=top>
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






