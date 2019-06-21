<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<jsp:include page="/repository/aim/view/strongPassword.jsp"  />

<script language="JavaScript">



	function optionChanged(flag) {
		if (flag == 'otype') {
			var index1  = document.aimUserRegisterForm.selectedOrgType.selectedIndex;
			var val1    = document.aimUserRegisterForm.selectedOrgType.options[index1].value;
			var orgType = document.aimUserRegisterForm.orgType.value;
			if ( val1 != "-1") {
				if (val1 != orgType) {
					document.aimUserRegisterForm.orgType.value = val1;
					document.aimUserRegisterForm.actionFlag.value = "typeSelected";
					<digi:context name="selectType" property="context/module/moduleinstance/showRegisterUser.do" />
		   			document.aimUserRegisterForm.action = "<%= selectType %>";
					document.aimUserRegisterForm.target = "_self";
					document.aimUserRegisterForm.submit();
				}
				return false;
			}
			else
				return false;
		}
		if (flag == 'ogroup') {
			var index2  = document.aimUserRegisterForm.selectedOrgGroup.selectedIndex;
			var val2    = document.aimUserRegisterForm.selectedOrgGroup.options[index2].value;
			var orgGrp = document.aimUserRegisterForm.orgGrp.value;
			if ( val2 != "-1") {
				if (val2 != orgGrp) {
					document.aimUserRegisterForm.orgGrp.value = val2;
					document.aimUserRegisterForm.actionFlag.value = "groupSelected";
					<digi:context name="selectGrp" property="context/module/moduleinstance/showRegisterUser.do" />
		   			document.aimUserRegisterForm.action = "<%= selectGrp %>";
					document.aimUserRegisterForm.target = "_self";
					document.aimUserRegisterForm.submit();
				}
				return false;
			}
			else
				return false;
		}
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
	
	function validate(){
        name = document.aimUserRegisterForm.firstNames.value;
        lastname = document.aimUserRegisterForm.lastName.value;
        password = document.aimUserRegisterForm.password.value;
        passwordConfirmation = document.aimUserRegisterForm.passwordConfirmation.value;
        selectedOrgType = document.aimUserRegisterForm.selectedOrgType.value;
        selectedOrgGroup = document.aimUserRegisterForm.selectedOrgGroup.value;
        selectedOrganizationId = document.aimUserRegisterForm.selectedOrganizationId.value;
        
        if (isVoid(name)==1)
        {
			<c:set var="translation">
			<digi:trn key="erroruregistration.FirstNameBlank">First Name is Blank</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if (isVoid(name)==2)
        {
			<c:set var="translation">
			<digi:trn key="erroruregistration.FirstNameNumeric">First Name can't be only numeric</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if (isVoid(lastname)==1)
        {
			<c:set var="translation">
			<digi:trn key="error.registration.LastNameBlank">Last Name is Blank</digi:trn>
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
        if(validateEmail()==false)
            return false
        if (isVoid(password)||isVoid(passwordConfirmation))
        {
			<c:set var="translation">
			<digi:trn key="error.registration.passwordOneChar">Please use at least one letter in the password field.</digi:trn>
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
    
	function validateEmail() {
	    var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
		var address = document.aimUserRegisterForm.email.value;
		var address2 = document.aimUserRegisterForm.emailConfirmation.value;
		if(reg.test(address) == false||reg.test(address2) == false) {
			<c:set var="translation">
			<digi:trn key="error.registration.noemail">Please enter a valid email address.</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
		}
		if(address != address2){
			<c:set var="translation">
			<digi:trn key="error.registration.noemailmatch">Emails in both fields must be the same</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
		}
		
		var notificationEmail = $('#notificationEmail').val();
        if ($('#notificationEmailEnabled').is(":checked")) {
	        	if(reg.test(notificationEmail) == false) {
		        	<c:set var="translation"><digi:trn>Please enter a valid Notification Email</digi:trn></c:set>
		            alert("${translation}");
		            return false;
		        }
	        	
	        	if(address == notificationEmail) {
                    <c:set var="translation"><digi:trn>Email address and notification email address must be different</digi:trn></c:set>
                    alert("${translation}");
                    return false;
                }
        }
        
		return true;
	}
	
	function resetFields () {
		var list = document.getElementsByTagName('input');
		for(var i = 0; i < list.length; i++) {
		  if(list[i].type == 'text' || list[i].type == 'password')
		    {
			  list[i].value = '';
			}
		}
		document.getElementsByName("selectedOrgType")[0].selectedIndex = 0;
		document.getElementsByName("selectedOrgGroup")[0].selectedIndex = 0;
		document.getElementsByName("selectedOrganizationId")[0].selectedIndex = 0;
		
		$('#notificationEmailEnabled').prop('checked', false);
		$('#notificationEmailRow').hide();
	}
	
	function init() {
		$('#notificationEmailEnabled').bind("click", function() {
            $('#notificationEmailRow') [this.checked ? "show" : "hide"]();
        });
		
		$('#notificationEmailRow')[$('#notificationEmailEnabled').is(":checked") ? "show" : "hide"]();
	}


	var enterBinder	= new EnterHitBinder('registerUserBtn');
	
	YAHOOAmp.util.Event.addListener(window, "load", init);

</script>

<c:set var="notificationHelp"><digi:trn>
Please select this checkbox in case you want to specify an email address to receive system notifications.&#013;
This email address will potentially be used by the system to send AMP related information.&#013;
If you are unsure now, an AMP admin can add your Notification Email later on.&#013;
Please do not attempt to use the Notification Email as user login.
</digi:trn></c:set>

<center>
<div class="reg_form_container">
<div class="home_sec_title"><digi:trn key="um:accountInfoAboutYou">Account information / about you </digi:trn></div>
<digi:instance property="aimUserRegisterForm" />
<digi:form action="/registerUser.do" method="post" onsubmit="return validateAimUserRegisterForm(this);">
  <html:hidden property="orgType" />
  <html:hidden property="orgGrp" />
  <input type="hidden" name="actionFlag" value="">
  <table cellpadding="0" cellspacing="0" border="0">
	<tr>
    <td width="100%" valign="top" align="left">
    <table bgColor=#ffffff border="0" cellpadding="0" cellspacing="0" width="100%">
      <tr>
        <td class="left-align" valign="top" width="65%">
          <table border="0" cellPadding=0 cellspacing="0" width="650">
            <logic:notEmpty name="aimUserRegisterForm" property="errors" >
              <tr>
                <td class="left-align" class=title noWrap colspan="33"><!-- digi:errors /-->
                  <font color="red">
                  <ul>
                    <logic:iterate id="element"	name="aimUserRegisterForm" property="errors">
                      <li>
                        <digi:trn key="${element.key}">
                          <bean:write name="element" property="value"/>
                        </digi:trn>
                      </li>
                    </logic:iterate>
                  </ul>
                  </font>
                  <jsp:include page="/repository/aim/view/strongPasswordRulesLegend.jsp"  />
                </td>
              </tr>
            </logic:notEmpty>

            <tr>
              <td class="left-align title" noWrap colspan="5"><digi:trn>All fields marked with an</digi:trn><FONT color=red><B> * </B></FONT><digi:trn>are required.</digi:trn>
                <digi:trn key="um:userValidEmail"> Please use a valid e-mail address.</digi:trn>              </td>
            </tr>
			<tr>
			<td colspan="8" height=15>&nbsp;</td>
			</tr>
            <tr>
              <td class=f-names noWrap width="175" style="padding-bottom:10px;"><FONT color=red>*</FONT>
                <digi:trn key="um:firstName">First Name</digi:trn>              </td>
              <td class="right-align" width="145" style="padding-bottom:10px;"><html:text property="firstNames" size="20" styleClass="inp-text"/>              </td>
              <td width="10">&nbsp;</td>
              <td class=f-names noWrap width="175" style="padding-bottom:10px;"><FONT color=red>*</FONT>
                <digi:trn key="um:lastName">Last Name</digi:trn>              </td>
              <td class="right-align" width="145" style="padding-bottom:10px;"><html:text property="lastName" size="20" styleClass="inp-text"/>              </td>
            </tr>
            <tr>
              <td class=f-names noWrap style="padding-bottom:10px;"><FONT color=red>*</FONT>
                <digi:trn key="um:emailAddress">E-mail Address </digi:trn>              </td>
              <td class="right-align" style="padding-bottom:10px;"><html:text property="email" size="20" styleClass="inp-text pwd_username"/>
              </td>
              <td>&nbsp;</td>
              <td class=f-names noWrap style="padding-bottom:10px;"><FONT color=red>*</FONT>
                <digi:trn key="um:repEmailAddress">Repeat Email Address </digi:trn>              </td>
              <td class="right-align" style="padding-bottom:10px;"><html:text property="emailConfirmation" size="20" styleClass="inp-text"/>              </td>
            </tr>
            <tr>
              <td valign="top" class=f-names noWrap style="padding-bottom:10px;"><FONT color=red>*</FONT>
                <digi:trn key="um:password">Password </digi:trn>              </td>
              <td style="padding-bottom:10px;"><html:password property="password" size="20"
                                                              styleClass="inp-text"/>
                <div style="display: none" class="pwd_container" id="pwd_container">
                  <span class="pwstrength_viewport_verdict">&nbsp;</span>
                  <span class="pwstrength_viewport_progress"></span>
                </div>
              </td>
              <td>&nbsp;</td>
              <td class=f-names valign="top" noWrap style="padding-bottom:10px;"><FONT color=red>*</FONT>
                <digi:trn key="um:repPassword">Repeat Password </digi:trn>              </td>
              <td class="right-align" valign="top" style="padding-bottom:10px;"><html:password property="passwordConfirmation" size="20" styleClass="inp-text" />              </td>
            </tr>
            <tr>
              <td class=f-names noWrap style="padding-bottom:10px;">
                <img src= "../ampTemplate/images/help.gif" border="0" title="${notificationHelp}">
                <digi:trn>Use different email for email notifications</digi:trn></td>
              <td align="center" style="padding-bottom:10px;">
                <html:checkbox property="notificationEmailEnabled" style="width: auto" styleId="notificationEmailEnabled"/>              
              </td>
              <td>&nbsp;</td>
              <td colspan=2>
                <table width="100%">
                    <tr id="notificationEmailRow">
                        <td class=f-names noWrap style="padding-bottom:10px;">
                            <FONT color=red>*</FONT>
                            <digi:trn>Notification Email</digi:trn>
                        </td>
                        <td class="right-align" style="padding-bottom:10px;">
                            <html:text property="notificationEmail" size="20" styleClass="inp-text" styleId="notificationEmail"/>
                        </td>
                    </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td class=f-names noWrap style="padding-bottom:10px;"><FONT color=red>*</FONT>
                <digi:trn key="um:countryOfResidence">Country of Residence </digi:trn>              </td>
              <td class="orgtype right-align" style="padding-bottom:10px;">
              	<html:select  property="selectedCountryResidence" styleClass="inp-text" >
                  <html:optionsCollection name="aimUserRegisterForm" property="countryResidence" value="iso" label="name" />
                </html:select>              
              </td>
              <td>&nbsp;</td>
              <td class=f-names noWrap style="padding-bottom:10px;"><digi:trn key="um:mailingAddress">Mailing Address </digi:trn>              </td>
              <td class="right-align" style="padding-bottom:10px;"><html:text property="mailingAddress" size="20" styleClass="inp-text"/>              </td>
            </tr>
            <tr>
              <td class=f-names noWrap style="padding-bottom:10px;"><FONT color=red>*</FONT>
                <digi:trn key="um:organizationType">Organization Type </digi:trn>              </td>
              <td class="orgtype right-align" style="padding-bottom:10px;"><html:select property="selectedOrgType"  styleClass="inp-text"    onchange="optionChanged('otype')">
                  <html:option value="-1">--
                    <digi:trn key="um:selectType">Select a type</digi:trn>
                    --</html:option>
                  <html:optionsCollection name="aimUserRegisterForm" property="orgTypeColl"

													value="ampOrgTypeId" label="orgType" />
                </html:select>              </td>
				<td></td>
				<td></td>
				<td></td>
				
            </tr>
            <tr>
              <td class="f-names orgtype" noWrap style="padding-bottom:10px;"><FONT color=red>*</FONT>
                <digi:trn key="um:organizationGroup">Organization Group</digi:trn>              </td>
              <td class="orgtype right-align" style="padding-bottom:10px;"><html:select property="selectedOrgGroup" styleClass="inp-text"  onchange="optionChanged('ogroup')">
                  <html:option value="-1">--
                    <digi:trn key="um:selectGroup">Select a group</digi:trn>
                    --</html:option>
                  <logic:notEmpty name="aimUserRegisterForm" property="orgGroupColl" >
                    <html:optionsCollection name="aimUserRegisterForm" property="orgGroupColl"

									         			value="ampOrgGrpId" label="orgGrpName" />
                  </logic:notEmpty>
                </html:select>              </td>
				<td></td>
				<td></td>
				<td></td>
            </tr>
            <tr>
              <td class="f-names orgtype" noWrap style="padding-bottom:10px;"><FONT color=red>*</FONT>
                <digi:trn key="um:organizationName">Organization Name </digi:trn>              </td>
              <td class="orgtype right-align" style="padding-bottom:10px;"><html:hidden property="organizationName" value="-1"/>
                <html:select property="selectedOrganizationId" styleClass="inp-text" >
                  <html:option value="-1">--
                    <digi:trn key="um:selectOrganization">Select an organization</digi:trn>
                    --</html:option>
                  <logic:notEmpty name="aimUserRegisterForm" property="orgColl" >
                    <html:optionsCollection name="aimUserRegisterForm" property="orgColl" value="ampOrgId" label="name" />
                  </logic:notEmpty>
                </html:select>              </td>
				<td></td>
				<td></td>
				<td></td>
            </tr>
            <!-- <tr>

					<td width="3%">&nbsp;</td>

					<td align=right class=f-names noWrap>

						<digi:trn key="um:website">Website

						</digi:trn>

					</td>

					<td align="left">

						<html:text property="webSite" size="20" styleClass="inp-text"/>

					</td>

				</tr> -->
            <tr>
              <td class=f-names noWrap style="padding-bottom:10px;"><digi:trn key="um:yourLangSettings">Your language settings </digi:trn>              </td>
              <td class="orgtype right-align" style="padding-bottom:10px;"><html:select  property="selectedLanguage" styleClass="inp-text">
                  <bean:define id="languages" name="aimUserRegisterForm" property="navigationLanguages"

					 type="java.util.Collection" />
                  <html:options  collection="languages" property="code" labelProperty="name" />
                </html:select>              </td>
				<td></td>
				<td></td>
				<td></td>
            </tr>
            <tr>
            	<td>&nbsp;</td>
            	<td>&nbsp;</td>
              <td class="right-align" style="padding-top:20px;padding-right:10px;" class="reg_butt"><c:set var="btnSubmit">
                <digi:trn key="btn:submit">Submit</digi:trn>
              </c:set>
                <html:submit value="${btnSubmit}" styleClass="buttonx" onclick="return validate();" styleId="registerUserBtn"/>
             </td>
              <td class="left-align" style="padding-top:20px;padding-left:10px;" class="reg_butt">
              <c:set var="btnReset">
                <digi:trn key="btn:reset">Reset</digi:trn>
              </c:set>
             	<input type="button" class="buttonx" value="${btnReset}" onclick="return resetFields();"/>
             </td>
			 <td></td>
              </tr>
            <tr>
              <td colspan=6>&nbsp;</td>
            </tr>
          </table>
        </td>
        <td width="5%">&nbsp;</td>
        <td bgcolor="#dbe5f1" vAlign="top" width="30%">
            <table align="center" border="0" cellPadding=3 cellspacing="0" width="90%">
                <tr>
                  <td valign="top">&nbsp;</td>
                </tr>
                <tr>
                  <td valign="top">
                  	<span class="formnote">
                    <digi:trn key="aim:loginWarning"> You are signing-in to one or more secure applications for
                      
                      official business. You have been granted the right to access these
                      
                      applications and the information contained in them to facilitate
                      
                      your official business. Your accounts and passwords are your
                      
                      responsibility. Do not share them with anyone. </digi:trn>
                    </span>
                    <BR />
                    <BR />
                  </td>
                </tr>
                <tr>
                  <td valign="top">&nbsp;</td>
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
