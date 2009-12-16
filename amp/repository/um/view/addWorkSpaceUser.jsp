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
        border: 1px solid black;
	border-width: 1px 1px 1px 1px; 
	background-color: #ffffff;
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

<digi:instance property="umAddUserForm" />

<digi:form action="/addWorkSpaceUser.do" method="post" onsubmit="return validateAimUserRegisterForm(this);">


<html:hidden property="orgType" />

<html:hidden property="orgGrp" />

<input type="hidden" name="actionFlag" value="">
<input type="hidden" name="teamMemberId" />
  <table bgColor=#ffffff cellPadding=5 cellSpacing=1 width=650>
	<tr>
		<td align=left vAlign=top>
			<table cellPadding=5 cellSpacing=0 width="100%">
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
				<td noWrap vAlign="top">
					<table width="100%" border="0">
					   <tr>
						  <td valign="top" align="center">
					
										<table border=0 cellPadding=5 cellSpacing=0 width="100%">
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=center colspan="2">
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
															<digi:trn key="${workspaces.name}"><c:out value="${workspaces.name}"/></digi:trn>
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
													<logic:iterate name="umAddUserForm" property="ampRoles" id="ampRole" type="org.digijava.module.aim.dbentity.AmpTeamMemberRoles">
														<html:option value="${ampRole.ampTeamMemRoleId}">
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
							                              <digi:trn key="btn:submit">Submit</digi:trn>
							                        </c:set>
<!--													<html:submit value="${btnSubmit}" styleClass="dr-menu" onclick="return validate()" />-->
													<input type="button" value="${btnSubmit}" onclick='return addWorkSpace()'/>
												</td>
												<td align="left">
							                        <c:set var="btnDone">
							                              <digi:trn key="btn:done">Done</digi:trn>
							                        </c:set>
<!--													<html:submit value="${btnDone}" styleClass="dr-menu" onclick="return cancel()"/>-->
													<input type="button" value="${btnDone}" onclick='return cancelAddWorkSpace()'/>
												</td>
												
											</tr>
											<tr>
												<td colspan=3>&nbsp;</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td align=center vAlign=top width=520><br>
		             					<table border=0 cellPadding=5 cellSpacing=0 width="80%">
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
										<c:if test="${empty umAddUserForm.assignedWorkspaces || umAddUserForm.assignedWorkspaces == null}">
										<tr><td colspan="3" align="center"><digi:trn>There are not workspaces assigned for this user yet</digi:trn></td></tr>
										</c:if>
										<c:if test="${!empty umAddUserForm.assignedWorkspaces && umAddUserForm.assignedWorkspaces != null}">										
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
										<a href="javascript:deleteWS(<c:out value="${assignedWS.ampTeamMemId}"/>)" title="<digi:trn key="aim:ClickDeleteUserFromWS">Click on this icon to remove user from the workspace</digi:trn>"><img  src="/repository/message/view/images/trash_12.gif" border=0 hspace="2"/></a>
										</td>
										</tr>
										</logic:iterate>
										</c:if>
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






