<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>

<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>

<%@ taglib uri="/taglib/struts-html" prefix="html"%>

<%@ taglib uri="/taglib/digijava" prefix="digi"%>

<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

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

<digi:form action="/registerUser.do" method="post" onsubmit="return validateAimUserRegisterForm(this);">

	<html:hidden property="orgType" />

	<html:hidden property="orgGrp" />

	<input type="hidden" name="actionFlag" value="">

	<table bgColor=#ffffff cellPadding=5 cellSpacing=1 width=650>
		<tr>
			<td align=left vAlign=top>
			<table cellPadding=5 cellSpacing=0 width="100%">
			<tr>
			<td>
			<table border=0 cellPadding=5 cellSpacing=0 width="100%">
				<logic:notEmpty name="umAddUserForm" property="errors">
				<tr>
					<td width="3%">&nbsp;</td>
					<td align=left colspan="4">
					<!-- digi:errors /-->
						<font color="red">
						<ul>
							<logic:iterate id="element" name="umAddUserForm"
								property="errors">
								<li>
								<digi:trn key="${element.key}">
									<bean:write name="element" property="value" />
								</digi:trn>
								</li>
							</logic:iterate>
						</ul>
						</font>
					</td>
				</tr>
				</logic:notEmpty>
				<tr>
				    <td width="3%">&nbsp;</td>
					<td align=left noWrap colspan="4">
						<digi:trn key="um:allMarkedRequiredField">
							All fields marked with an 
							<FONT color=red><B><BIG>*</BIG> </B></FONT> are required.
							</digi:trn> 
							<digi:trn key="um:userValidEmail"> Please use a valid e-mail address.</digi:trn>
					</td>
				</tr>
			</table>
			
			</td>
			</tr>

					   		<tr>
							  <td valign="top" align="center">
								  <table border=0 cellPadding=0 cellSpacing=0>
	
									<tr>
										<td align=left vAlign=top width=520>
											<fieldset>
											<legend><digi:trn>User Data</digi:trn></legend>
											<table border=0 cellPadding=5 cellSpacing=0 width="100%">
											<tr>
												<td align=right noWrap width="40%">
													<FONT color=red>*</FONT> 
													<digi:trn key="um:firstName">First Name</digi:trn>
												</td>
												<td align="left">
													<html:text property="firstNames" size="20" styleClass="inp-text" />
												</td>
												<td align=right noWrap width="40%">
													<FONT color=red>*</FONT> 
													<digi:trn key="um:lastName">Last Name</digi:trn>
												</td>
												<td align="left">
													<html:text property="lastName"	size="20" styleClass="inp-text" />
												</td>
											</tr>
											<tr>
												<td align=right noWrap>
													<FONT color=red>*</FONT>
													<digi:trn key="um:emailAddress">E-mail Address</digi:trn>
												</td>
												<td align="left">
													<html:text  styleId="userEmail" property="email" size="20" styleClass="inp-text" />
												</td>
												<td align=right noWrap>
													<FONT color=red>*</FONT>
													<digi:trn key="um:repEmailAddress">Repeat Email Address</digi:trn>
												</td>
												<td align="left">
													<html:text styleId="userEmailConfirmation" property="emailConfirmation"	size="20" styleClass="inp-text" />
												</td>
											</tr>
											<tr>
												<td align=right noWrap>
													<FONT color=red>*</FONT>
													<digi:trn key="um:password">Password</digi:trn>
												</td>
												<td align="left">
													<html:password styleId="userPassword" property="password"size="20" />
												</td>
												<td align=right noWrap>
													<FONT color=red>*</FONT>
													<digi:trn key="um:repPassword">Repeat Password</digi:trn></td>
												<td align="left">
													<html:password styleId="userPasswordConfirmation" property="passwordConfirmation" size="20" />
												</td>
											</tr>
											<tr>
												<td align=right noWrap>
													<FONT color=red>*</FONT>
													<digi:trn key="um:countryOfResidence">Country of Residence</digi:trn></td>
												<td align="left" colspan="3">
													<html:select property="selectedCountryResidence" styleClass="inp-text">
														<c:forEach var="cn"	items="${umAddUserForm.countryResidence}">
															<html:option value="${cn.iso}">${cn.name}</html:option>
														</c:forEach>
													</html:select>
												</td>
											</tr>
											<tr>
												<td align=right noWrap>
													<digi:trn key="um:mailingAddress">Mailing Address</digi:trn></td>
												<td align="left" colspan="3">
													<html:text property="mailingAddress" size="20" styleClass="inp-text" /></td>
											</tr>
											</table>
											</fieldset>
											<fieldset>
											<legend><digi:trn>Organization Data</digi:trn></legend>
											<table border=0 cellPadding=5 cellSpacing=0 width="100%">
											<tr>
												<td align=right noWrap>
													<FONT color=red>*</FONT>
													<digi:trn key="um:organizationType">Organization Type</digi:trn>
												</td>
												<td align="left">
													<html:select property="selectedOrgType"	styleClass="inp-text" onchange="optionChanged('otype')">
														<html:option value="-1">-- <digi:trn key="um:selectType">Select a type</digi:trn> --</html:option>
														<html:optionsCollection name="umAddUserForm" property="orgTypeColl" value="ampOrgTypeId" label="orgType" />
													</html:select></td>
												<td colspan="2">
												<c:if test="${umAddUserForm.registrationByEmail}">
													<html:checkbox property="sendEmail"	styleClass="inp-text" >
														<digi:trn key="um:sendNotificationEmail">Send notification email</digi:trn>
													</html:checkbox></td>
												</c:if>
												<c:if test="${!umAddUserForm.registrationByEmail}">
													<html:checkbox property="sendEmail"	styleClass="inp-text" disabled="true" >
														<digi:trn key="um:sendNotificationEmail">Send notification email</digi:trn>
													</html:checkbox></td>
												</c:if>
													
											</tr>
											<tr>
												<td align=right noWrap>
													<FONT color=red>*</FONT>
													<digi:trn key="um:organizationGroup">Organization Group</digi:trn>
												</td>
												<td align="left">
													<html:select property="selectedOrgGroup" styleClass="inp-text" onchange="optionChanged('ogroup')">
														<html:option value="-1">-- <digi:trn key="um:selectGroup">Select a group</digi:trn> --</html:option>
														<logic:notEmpty name="umAddUserForm" property="orgGroupColl">
															<html:optionsCollection name="umAddUserForm" property="orgGroupColl" value="ampOrgGrpId" label="orgGrpName" />
														</logic:notEmpty>
													</html:select></td>
												<td colspan="2">
													<html:checkbox property="addWorkspace" styleClass="inp-text">
														<digi:trn key="um:assignToWorkspace">Assign to Workspace</digi:trn>
													</html:checkbox></td>
													
											</tr>
											<tr>
												<td align=right noWrap>
													<FONT color=red>*</FONT>
													<digi:trn key="um:organizationName">Organization Name</digi:trn></td>
												<td align="left" colspan="3">
													<html:hidden property="organizationName" value="-1" /> 
													<html:select property="selectedOrganizationId" styleClass="inp-text">
														<html:option value="-1">-- <digi:trn key="um:selectOrganization">Select an organization</digi:trn> --</html:option>
														<logic:notEmpty name="umAddUserForm" property="orgColl">
															<html:optionsCollection name="umAddUserForm" property="orgColl" value="ampOrgId" label="name" />
														</logic:notEmpty>
													</html:select></td>
	
											</tr>
											<tr>
												<td align=right noWrap>
													<digi:trn key="um:yourLangSettings">Your language settings</digi:trn>
												</td>
												<td align="left" colspan="3">
													<html:select property="selectedLanguage" styleClass="inp-text">
														<bean:define id="languages" name="umAddUserForm"
															property="navigationLanguages" type="java.util.Collection" />
														<html:options collection="languages" property="code"
															labelProperty="name" />
													</html:select></td>
	
											</tr>
											</table>
											</fieldset>
										<table border=0 cellPadding=5 cellSpacing=0 width="100%">
											<tr>
												<td>&nbsp;</td>
												<td align="right">
													<c:set var="btnSubmit">
														<digi:trn key="btn:submit">Submit</digi:trn>
													</c:set> 
<!--													<html:submit value="${btnSubmit}" styleClass="dr-menu" onclick="return validate()" /></td>-->
													<input type="button" value="${btnSubmit}" onclick='return registerNewUser()'/>
												</td>
												<td align="left">
													<c:set var="btnCancel">
														<digi:trn key="btn:cancel">Cancel</digi:trn>
													</c:set> 
<!--													<html:submit value="${btnCancel}" styleClass="dr-menu" onclick="cancel()" /></td>-->
													<input type="button" value="${btnCancel}" onclick='closeWindow()'/>													
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
		</td>
		</tr>
	</table>
</digi:form>