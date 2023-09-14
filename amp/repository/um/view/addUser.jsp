<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>

<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>

<%@ taglib uri="/taglib/struts-html" prefix="html"%>

<%@ taglib uri="/taglib/digijava" prefix="digi"%>

<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<jsp:include page="/repository/aim/view/strongPassword.jsp"  />

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

<%@include file="userValidation.jsp" %>

<script type="text/javascript">
	function optionChanged(flag) {
		if (flag == 'otype') {
			var index1  = document.umAddUserForm.selectedOrgType.selectedIndex;
			var val1    = document.umAddUserForm.selectedOrgType.options[index1].value;
			var orgType = document.umAddUserForm.orgType.value;
			if ( val1 != "-1") {
				if (val1 != orgType) {
					document.umAddUserForm.orgType.value = val1;
					document.umAddUserForm.actionFlag.value = "typeSelected";
					<digi:context name="selectType" property="context/module/moduleinstance/addUser.do" />
		   			document.umAddUserForm.action = "<%= selectType %>";
					document.umAddUserForm.target = "_self";
					document.umAddUserForm.submit();
				}
				return false;
			}
			else
				return false;
		}
		if (flag == 'ogroup') {
			var index2  = document.umAddUserForm.selectedOrgGroup.selectedIndex;
			var val2    = document.umAddUserForm.selectedOrgGroup.options[index2].value;
			var orgGrp = document.umAddUserForm.orgGrp.value;
			if ( val2 != "-1") {
				if (val2 != orgGrp) {
					document.umAddUserForm.orgGrp.value = val2;
					document.umAddUserForm.actionFlag.value = "groupSelected";
					<digi:context name="selectGrp" property="context/module/moduleinstance/addUser.do" />
		   			document.umAddUserForm.action = "<%= selectGrp %>";
					document.umAddUserForm.target = "_self";
					document.umAddUserForm.submit();
				}
				return false;
			}
			else
				return false;
		}
	}

	function cancel()
	{
		document.umAddUserForm.action = "/um/viewAllUsers.do";
		document.umAddUserForm.target = "_self";
		document.umAddUserForm.submit();
		return false;
	}
	function isVoid(name){
		if (name == "" || name == null || name.charAt(0) == ' '){
        	return 1;
        }		
		if (!isNaN(name)){
        	return 2;
        }		
		return 0;		
	}

	function isPassVoid(name){
        return (name==null || name.length<1);
	}
	
	
	function validate(){
        name = document.umAddUserForm.firstNames.value;
        lastname = document.umAddUserForm.lastName.value;
        password = document.umAddUserForm.password.value;
        passwordConfirmation = document.umAddUserForm.passwordConfirmation.value;
        truBudgetPassword = document.umAddUserForm.truBudgetPassword.value;
		truBudgetPasswordConfirmation = document.umAddUserForm.truBudgetPasswordConfirmation.value;
        selectedOrgType = document.umAddUserForm.selectedOrgType.value;
        selectedOrgGroup = document.umAddUserForm.selectedOrgGroup.value;
        selectedOrganizationId = document.umAddUserForm.selectedOrganizationId.value;
        
        if (isVoid(name)==1)
        {
			<c:set var="translation">
			<digi:trn key="erroruregistration.FirstNameBlank">First Name is Blank or starts with an space</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if (isVoid(name)==2)
        {
			<c:set var="translation">
			<digi:trn key="erroruregistration.FirstNameNumeric">First can't be only numeric</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if (isVoid(lastname)==1)
        {
			<c:set var="translation">
			<digi:trn key="error.registration.LastNameBlank">LastName is Blank or starts with an space</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if (isVoid(lastname)==2)
        {
			<c:set var="translation">
			<digi:trn key="erroruregistration.LastNameNumeric">LastName can't be only numeric</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        var address = document.umAddUserForm.email.value;
		var address2 = document.umAddUserForm.emailConfirmation.value;
        if(! validateEmails(address, address2))
            return false;
        
        var notificationEmail = $('#notificationEmail').val();
        if ($('#notificationEmailEnabled').is(":checked")) {
        	if (!validateNotificationEmail(notificationEmail) || !validateMailWithNotificationMail(address, notificationEmail)) {
        	    return false;
        	}
        }
        
        if (isPassVoid(password)||isPassVoid(passwordConfirmation))
        {
			<c:set var="translation">
			<digi:trn key="error.registration.passwordBlank">Password field is Blank</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if(password != passwordConfirmation){
			<c:set var="translation">
			<digi:trn key="error.registration.NoPasswordMatch">Passwords in both fields must be the same</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
		if(truBudgetPassword !== truBudgetPasswordConfirmation){
			<c:set var="translation">
			<digi:trn key="error.registration.NoPasswordMatch">Both Trubudget Passwords must be the same</digi:trn>
			</c:set>
			alert("${translation}");
			return false;
		}
        if(selectedOrgType=="-1"){
			<c:set var="translation">
			<digi:trn key="error.registration.enterorganizationother">Please enter Organization Type</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if(selectedOrgGroup=="-1"){
			<c:set var="translation">
			<digi:trn key="error.registration.NoOrgGroup">Please Select Organization Group</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if(selectedOrganizationId=="-1"){
			<c:set var="translation">
			<digi:trn key="error.registration.NoOrganization">Please Select Organization</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }

        return true;
	}

	function init() {

		$('#notificationEmailEnabled').bind("click", function() {
	        $('#notificationEmailRow') [this.checked ? "show" : "hide"]();
	      });
		
		$('#notificationEmailRow')[$('#notificationEmailEnabled').is(":checked") ? "show" : "hide"]();
	}
	
	YAHOOAmp.util.Event.addListener(window, "load", init) ;
</script>
<digi:instance property="umAddUserForm" />
<center>
<div class="ampFormContainer">
<digi:form action="/registerUser.do" method="post">

	<html:hidden property="orgType" />

	<html:hidden property="orgGrp" />

	<input type="hidden" name="actionFlag" value="">

	<table bgColor=#ffffff cellPadding=5 cellspacing="1" width=1000>
		<tr>
			<td align=left valign="top" width=752>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb> 
						<c:set	var="translation">
						<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set> 
						<digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}">
							<digi:trn key="aim:AmpAdminHome">
				            	Admin Home
				        	</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp; 
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAllUsers">Click here to goto users manager</digi:trn>
						</c:set> 
						<digi:link href="/viewAllUsers.do" styleClass="comment"	title="${translation}">
							<digi:trn key="aim:viewAllUsers">
				            	List of users
				        	</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp; 
							<digi:trn key="aim:viewEditUser:EditUser">
				          		Edit user
				          	</digi:trn> 
				        </span>
				    </td>
					<!-- End navigation -->
				</tr>
			
				<tr>
					<td vAlign="top" colspan="2">
						<table style="border:1px solid #dddddd;" border="0" cellpadding=0 cellspacing=0>
                        	<tr>
								<td colspan="2"  height="25" bgcolor="#C7D4DB" align="center" style="font-size:12px; font-weight:bold;border-bottom:1px solid 	 		 								#dddddd;"><span class=subtitle-blue> 
								<digi:trn key="aim:viewEditUser:AddEditUserHeader">Add user</digi:trn> </span>
			        </td>
				</tr>
                        
					   		<tr>
							  <td valign="top"  align="center">
								  <table border="0" cellpadding="0" cellspacing="0" width=1000 id="addUserContainer">
	
									<tr>
										
										<td align=left valign="top" width=520><br>
										<table border="0" cellPadding=5 cellspacing="0" width="100%">
											<tr>
												
												<td align=left class=title noWrap colspan="4">
												<!-- digi:errors /-->
												<logic:notEmpty name="umAddUserForm" property="errors">
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
												</logic:notEmpty>
													<jsp:include page="/repository/aim/view/strongPasswordRulesLegend.jsp"  />
												</td>
											</tr>
                                            <tr>
                                            	<td colspan="3"bgcolor="#C7D4DB" align="center" style="background-color: #F0F0F0;">
                                                <digi:trn key="um:userAccountInformation">User Account information</digi:trn>
                                                <digi:trn>All fields marked with an</digi:trn><span style='color: red; font-weight: bold; font-size: larger;'> * </span> <digi:trn>are required.</digi:trn> 
	 												<digi:trn key="um:userValidEmail"> Please use a valid e-mail address.</digi:trn><br/><br/>
                                                </td>
                                            </tr>
												<tr>
                                                	<td><br/></td>
                                                </tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=right class=f-names noWrap width="40%">
                                                
													<FONT color=red>*</FONT> 
													<digi:trn key="um:firstName">First Name</digi:trn>
												</td>
												<td align="left">
													<html:text property="firstNames" size="20" styleClass="inp-text" /></td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=right class=f-names noWrap width="40%">
													<FONT color=red>*</FONT> 
													<digi:trn key="um:lastName">Last Name</digi:trn>
												</td>
												<td align="left">
													<html:text property="lastName"	size="20" styleClass="inp-text" />
												</td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=right class=f-names noWrap>
													<FONT color=red>*</FONT>
													<digi:trn key="um:emailAddress">E-mail Address</digi:trn></td>
												<td align="left">
													<html:text  styleId="userEmail" property="email" size="20"
																styleClass="inp-text pwd_username" autocomplete="off"/>
												</td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=right class=f-names noWrap>
													<FONT color=red>*</FONT>
													<digi:trn key="um:repEmailAddress">Repeat Email Address</digi:trn></td>
												<td align="left">
													<html:text styleId="userEmailConfirmation" property="emailConfirmation"
															   size="20" styleClass="inp-text" autocomplete="off" />
												</td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td valign="top" align=right class=f-names noWrap>
													<div style="margin-top: 10px;">
													<FONT color=red>*</FONT>
													<digi:trn key="um:password">Password</digi:trn>
													</div>
												</td>
												<td align="left">
													<html:password styleId="userPassword"
																   property="password" size="20" autocomplete="new-password" />
													<div style="padding-left: 2px; margin: 5px">
														<div style="display: none" class="pwd_container" id="pwd_container">
															<span class="pwstrength_viewport_verdict">&nbsp;</span>
															<span class="pwstrength_viewport_progress"></span>
														</div>
													</div>
												</td>
											</tr>
										
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=right class=f-names noWrap>
													<FONT color=red>*</FONT>
													<digi:trn key="um:repPassword">Repeat Password</digi:trn></td>
												<td align="left">
													<html:password styleId="userPasswordConfirmation" property="passwordConfirmation"
																   size="20" autocomplete="new-password" />
												</td>
											</tr>
											<tr>
                                                <td  width="3%">&nbsp;</td>
                                                <td align="right" class="formCheckContainer">
                                                    <digi:trn>Use different email for email notifications</digi:trn>
                                                </td>
                                                <td class=f-names align="left">
                                                    <html:checkbox property="notificationEmailEnabled" styleClass="inp-text" styleId="notificationEmailEnabled"/>
                                                </td>
                                            </tr>
											<tr id="notificationEmailRow">
                                                <td width="3%">&nbsp;</td>
                                                <td align=right class=f-names noWrap>
                                                    <font color=red>*</font>
                                                    <digi:trn>Notification Email</digi:trn></td>
                                                <td align="left">
                                                    <html:text property="notificationEmail" size="20" styleClass="inp-text" styleId="notificationEmail"/></td>
                                            </tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=right class=f-names noWrap>
													<FONT color=red>*</FONT>
													<digi:trn key="um:countryOfResidence">Country of Residence</digi:trn></td>
												<td align="left">
													<html:select property="selectedCountryResidence" styleClass="inp-text">
														<c:forEach var="cn"	items="${umAddUserForm.countryResidence}">
															<html:option value="${cn.iso}"><c:out value="${cn.name}"></c:out></html:option>
														</c:forEach>
													</html:select>
												</td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=right class=f-names noWrap>
													<digi:trn key="um:mailingAddress">Mailing Address</digi:trn></td>
												<td align="left">
													<html:text property="mailingAddress" size="20" styleClass="inp-text" /></td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=right class=f-names noWrap>
													<FONT color=red>*</FONT>
													<digi:trn key="um:organizationType">Organization Type</digi:trn>
												</td>
												<td align="left">
													<html:select property="selectedOrgType"	styleClass="inp-text" onchange="optionChanged('otype')">
														<html:option value="-1">-- <digi:trn key="um:selectType">Select a type</digi:trn> --</html:option>
														<html:optionsCollection name="umAddUserForm" property="orgTypeColl" value="ampOrgTypeId" label="orgType" />
													</html:select></td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=right class=f-names noWrap>
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
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=right class=f-names noWrap>
													<FONT color=red>*</FONT>
													<digi:trn key="um:organizationName">Organization Name</digi:trn></td>
												<td align="left">
													<html:hidden property="organizationName" value="-1" /> 
													<html:select property="selectedOrganizationId" styleClass="inp-text">
														<html:option value="-1">-- <digi:trn key="um:selectOrganization">Select an organization</digi:trn> --</html:option>
														<logic:notEmpty name="umAddUserForm" property="orgColl">
															<html:optionsCollection name="umAddUserForm" property="orgColl" value="ampOrgId" label="name" />
														</logic:notEmpty>
													</html:select></td>
	
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=right class=f-names noWrap>
													<digi:trn key="um:yourLangSettings">Your language settings</digi:trn>
												</td>
												<td align="left">
													<html:select property="selectedLanguage" styleClass="inp-text">
														<bean:define id="languages" name="umAddUserForm"
															property="navigationLanguages" type="java.util.Collection" />
														<html:options collection="languages" property="code"
															labelProperty="name" />
													</html:select></td>
	
											</tr>

											<tr>
												<td width="3%">&nbsp;</td>
												<td align=right class=f-names noWrap>
<%--													<FONT color=red>*</FONT>--%>
													<digi:trn key="um:truBudgetIntents">TruBudget Permissions </digi:trn></td>
												<td align="left">
													<html:select property="selectedTruBudgetIntents" styleClass="inp-text" multiple="true">
														<c:forEach var="cn"	items="${umAddUserForm.truBudgetIntents}">
															<html:option value="${cn.truBudgetIntentName}"><c:out value="${cn.truBudgetIntentDisplayName}"></c:out></html:option>
														</c:forEach>
													</html:select>
												</td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=right class=f-names noWrap>
													<FONT color=red>*</FONT>
													<digi:trn key="um:repPassword">Enter TruBudget Password</digi:trn></td>
												<td align="left">
													<html:password styleId="userPassword" property="truBudgetPassword"
																   size="20" autocomplete="new-password" />
												</td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align=right class=f-names noWrap>
													<FONT color=red>*</FONT>
													<digi:trn key="um:repPassword">Repeat TruBudget Password</digi:trn></td>
												<td align="left">
													<html:password styleId="userPasswordConfirmation" property="truBudgetPasswordConfirmation"
																   size="20" autocomplete="new-password" />
												</td>
											</tr>
 											<module:display name="Pledges" parentModule="PROJECT MANAGEMENT">
											<tr>
												<td  width="3%">&nbsp;</td>
												<td align="right" class="formCheckContainer"><digi:trn key="um:pledgesUser">Pledges User</digi:trn></td>
												<td class=f-names align="left">
													<html:checkbox property="pledger" styleClass="inp-text">
														
													</html:checkbox></td>
											</tr>
											</module:display>
											<tr>
												<td  width="3%">&nbsp;</td>
												<td align="right"  class="formCheckContainer"><digi:trn key="um:sendNotificationEmail">Send notification email</digi:trn></td>
												<td class=f-names align="left">

												<c:if test="${umAddUserForm.registrationByEmail}">
													<html:checkbox property="sendEmail"	styleClass="inp-text" >
													</html:checkbox></td>
												</c:if>
												<c:if test="${!umAddUserForm.registrationByEmail}">
													<html:checkbox property="sendEmail"	styleClass="inp-text" disabled="true" >
													</html:checkbox></td>
												</c:if>													
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align="right"  class="formCheckContainer"><digi:trn key="um:nationalCoordinator">National Coordinator</digi:trn></td>
												<td class=f-names align="left">
													<html:checkbox property="nationalCoordinator" styleClass="inp-text">
													</html:checkbox></td>
											</tr>
											<tr>
												<td width="3%">&nbsp;</td>
												<td align="right"  class="formCheckContainer"><digi:trn key="um:assignToWorkspace">Assign to Workspace</digi:trn></td>
												<td class=f-names align="left">
													<html:checkbox property="addWorkspace" styleClass="inp-text">
													</html:checkbox></td>
											</tr>
											 <tr>
												<td width="3%">&nbsp;</td>
												<td align="right"  class="formCheckContainer"><digi:trn key="um:exemptFromDataFreezing">Exempt from activity freezing</digi:trn></td>
												<td class=f-names align="left">
													<html:checkbox property="exemptFromDataFreezing" styleClass="inp-text">
													</html:checkbox></td>
											</tr>
											<tr>
												<td>&nbsp;</td>
												<td align="right" class="addUserButContainer">
													<c:set var="btnSubmit">
														<digi:trn key="btn:submit">Submit</digi:trn>
													</c:set> 
													<html:submit value="${btnSubmit}" styleClass="dr-menu" onclick="return validate()" /></td>
												<td align="left" class="addUserButContainer">
													<c:set var="btnCancel">
														<digi:trn key="btn:cancel">Cancel</digi:trn>
													</c:set> 
													<html:submit value="${btnCancel}" styleClass="dr-menu" onclick="return cancel()" /></td>
											</tr>

											<tr>
												<td colspan=3>&nbsp;</td>
											</tr>
										</table>
										</td>
									</tr>
								  </table>
						          <br />
						          <br />
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
</div>
</center>