<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<jsp:include page="/repository/aim/view/strongPassword.jsp"  />

<digi:instance property="umViewEditUserForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<!-- jsp:include page="teamPagesHeader.jsp"  /-->
<!-- End of Logo -->

<%@include file="userValidation.jsp" %>

<script type="text/javascript">

$(document).ready(function() {
	initialize();
});

function initialize() {
	$('#notificationEmailEnabled').bind("click", function() {
        $('#notificationEmailRow') [this.checked ? "show" : "hide"]();
      });
    
    $('#notificationEmailRow')[$('#notificationEmailEnabled').is(":checked") ? "show" : "hide"]();
}

function cancel()
{
	document.umViewEditUserForm.action = "/um/viewAllUsers.do~reset=true";
	document.umViewEditUserForm.target = "_self";
	document.umViewEditUserForm.submit();
	return false;
}

function goAction(value){
	var submitForm=true;	
	
  	if(value!=null){
	  	if(value=='save'){
	  		submitForm=validateUserInfo();		  
	  	}
	  	
	  	if (value == 'delOrgs'){
	  		var msg='<digi:trn jsFriendly="true">Please select at least one organization ! </digi:trn>';
	  		var assignedOrgs = $("input[id^='verOrg_']");
	  		var orgsSelected = 0;
	  		if(assignedOrgs == null || assignedOrgs.length ==0){ //
	  			alert(msg);
	  			return false;
	  		}else{
	  			for(var i=0;i<assignedOrgs.length;i++){
	  	            if(assignedOrgs[i].checked){
	  	            	orgsSelected++;
	  	            	break;
	  	            }
	  	        }
	  			if(orgsSelected == 0){
	  				alert(msg);
	  				return false;
	  			}
	  		}
			if (!confirm("Are you sure you want to perform this action?. Confirming the deletion the assignment will be deleted")) {
				return false
			}
	  	}
	  	if(submitForm==true){
	  		document.getElementById("event").value=value;
	    	document.umViewEditUserForm.submit();
	  	}    	
  	}
}

function resetPasswordFields(){
  document.umViewEditUserForm.newPassword.value='';
  document.umViewEditUserForm.confirmNewPassword.value=''
}
function validate(str,value){
  var newPassword=document.umViewEditUserForm.newPassword.value;
  var confirmNewPassword=document.umViewEditUserForm.confirmNewPassword.value;
  if(newPassword.length==0||confirmNewPassword.length==0||newPassword!=confirmNewPassword){
    alert(str);
  }
  else{
    goAction(value);
  }
}
function checkPledgeSuperUser(){
    if(!$('#pledger').is(':checked')){
        $('#pledgeSuperUser').prop('checked', false);
        $('#pledgeSuperUser').attr('disabled', true);

    }
	else{
        $('#pledgeSuperUser').removeAttr('disabled');
	}
}

function validateUserInfo(){
	var userMail=document.getElementById("userMail").value;
	var notificationEmail = $("#notificationEmail").val();
	var firstName=document.getElementById("firstName").value;
	var lastName=document.getElementById("lastName").value;
	var country=document.getElementById("country").value;
	var language = $('select[name="selectedLanguageCode"]').val();
	var errorMsg='';
	
	if(!validateEmail(userMail)) {
		return false;
	}
	
	if(isInvalid(firstName)==1){
		<c:set var="translation">
		<digi:trn key="erroruregistration.FirstNameBlank">First Name is Blank or starts with an space</digi:trn>
		</c:set>
		alert("${translation}");
    	return false;
	}
	if(isInvalid(firstName)==2){
		<c:set var="translation">
		<digi:trn key="erroruregistration.FirstNameNumeric">First can't be only numeric</digi:trn>
		</c:set>
		alert("${translation}");
    	return false;
    }
	if(isInvalid(lastName)==1){
		<c:set var="translation">
		<digi:trn key="error.registration.LastNameBlank">LastName is Blank or starts with an space</digi:trn>
		</c:set>
		alert("${translation}");
    	return false;
	}
	if(isInvalid(lastName)==2){
		<c:set var="translation">
		<digi:trn key="erroruregistration.LastNameNumeric">LastName can't be only numeric</digi:trn>
		</c:set>
		alert("${translation}");
    	return false;
    }
	
	if ($('#notificationEmailEnabled').is(":checked")) {
        if (!validateNotificationEmail(notificationEmail) || !validateMailWithNotificationMail(userMail, notificationEmail)) {
            return false;
        }
    }
	
	if(country=='-1'){
		errorMsg='<digi:trn jsFriendly="true">Please Select Country</digi:trn>';
		alert(errorMsg);
		return false;
	}
	if (language == '-1') {
		errorMsg='<digi:trn jsFriendly="true" >Please Select a Language Settings value</digi:trn>';
		alert(errorMsg);
		return false;
	}
	return true;
}

</script>
<center>
<div class="ampFormContainer">
<digi:form action="/viewEditUser.do" method="post">
  <html:hidden name="umViewEditUserForm" property="event" styleId="event"/>
  <html:hidden name="umViewEditUserForm" property="id" styleId="id"/>
  
  <table bgColor="#ffffff" cellPadding="5" cellSpacing="1" width="705" >
	<tr>
		<td class="r-dotted-lg" width="14">&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=752>
        
			<table cellPadding="5" cellSpacing="0" width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height="33"><span class=crumb>

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
							 <span style="font-size: 11px;font-weight: bold; color:#000;">
				          <digi:trn key="aim:viewEditUser:EditUser" >
                         
				          Edit user
				          </digi:trn>
                      </span>
					</td>
					<!-- End navigation -->
				</tr>
			   
    			
   				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="740" cellspacing="1" cellSpacing="1">
                    
					<tr>
						<td noWrap width="616" vAlign="top">
							<table bgColor="#ffffff" cellPadding="0" cellSpacing="0"  width="100%" style="border: 1px solid #DDDDDD;">
								 <tr>
			      <td colspan="2" height="25" bgcolor="#C7D4DB" align="center" style="font-size:12px; font-weight:bold;border-bottom:1px solid 	 		 								#dddddd;">
			        <span class=subtitle-blue>
			          <digi:trn key="aim:viewEditUser:EditUserHeader">
			          Edit user
			          </digi:trn>
			        </span>
                    <digi:errors/>
					<jsp:include page="/repository/aim/view/strongPasswordRulesLegend.jsp"  />
					        &nbsp;
					        <br/>
					        <logic:equal name="umViewEditUserForm" property="displaySuccessMessage" value="true" >
					          <b>
					            <digi:trn key="aim:viewEditUser:changePasswordSuccessMessage">
					            The password has been changed successfully
					            </digi:trn>
					          </b>
					        </logic:equal>
					        <logic:equal name="umViewEditUserForm" property="emailerror" value="true" >
					          <b style="color: red;">
					            <digi:trn>
					            	The	email already exists in the database.
					            </digi:trn>
					          </b>
					        </logic:equal>
			      </td>
			    </tr><tr >
															<td bgColor="#dddddb" height="25" align="center" colspan="5" style="font-size:12px; font-weight:normal;">
																 <b><digi:trn key="aim:viewEditUser:edit">Edit</digi:trn> ${umViewEditUserForm.name}</b>
															</td>
														</tr>
								<tr>
									<td valign="top" colspan="3">
										<table align="center"  cellPadding="0" cellSpacing="0" width="562" border="0" >
                                        
											<tr>
												<td bgColor="#ffffff" class="box-border" width="560">
													<table border="0" cellPadding="1" cellSpacing="1" class="box-border" width="100%">
														
														<!-- Page Logic -->
														<tr>
															<td width="100%" id="viewUserEditContainer">
																<table width="563" border="0"  height="363" >
																	<tr>
																		<td width="169" align="right" height="30" style="font-size: 11px;
    font-weight: bold; color:#000;">
																			<digi:trn key="aim:viewEditUser:firstName">First name</digi:trn>
																			<font color="red">*</font>
																		</td>
																	    <td width="380" height="30" colspan="2" >
																	          <html:text name="umViewEditUserForm" property="firstNames" style="background-color: #FFFFFF;border: 1px solid #D0D0D0;color: #767676;font-size: 11px;margin: 5px;padding: 2px; width:180px;" styleId="firstName"/>																	          
																	    </td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30"style="font-size: 11px;
    font-weight: bold; color:#000;">
																			<digi:trn key="aim:viewEditUser:lastName">Last name</digi:trn>
																			<font color="red">*</font>
																		</td>
																	    <td width="380" height="30" colspan="2" >
																	          <html:text name="umViewEditUserForm" property="lastName" styleId="lastName" style="background-color: #FFFFFF;border: 1px solid #D0D0D0;color: #767676;font-size: 11px;margin: 5px;padding: 2px; width:180px;"/>
																	    </td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30"style="font-size: 11px;
    font-weight: bold; color:#000;">
																	        <digi:trn key="aim:viewEditUser:email">Email </digi:trn>
																	        <font color="red">*</font>
																		</td>
																		<td width="190" height="30">
																           <html:text name="umViewEditUserForm"
																					  property="email" styleClass="pwd_username" style="background-color: #FFFFFF;border: 1px solid #D0D0D0;color: #767676;font-size: 11px;margin: 5px;padding: 2px; width:180px;" styleId="userMail"/>
																		</td>
																	</tr>
																	<tr>
                                                                        <td width="169" align="right" height="30"style="font-size: 11px; font-weight: bold; color:#000;">
                                                                             <digi:trn>Use different email for email notifications</digi:trn>
                                                                        </td>
                                                                        <td width="380" height="30" colspan="2">
                                                                            <html:checkbox property="notificationEmailEnabled" styleClass="inp-text" style="margin: 5px" styleId="notificationEmailEnabled"/>
                                                                        </td>
                                                                    </tr>
	                                                                    <tr id="notificationEmailRow">
	                                                                        <td width="169" align="right" height="30"style="font-size: 11px; font-weight: bold; color:#000;">
	                                                                            <digi:trn>Notification Email</digi:trn>
	                                                                            <font color="red">*</font>
	                                                                        </td>
	                                                                        <td width="190" height="30">
	                                                                           <html:text name="umViewEditUserForm" property="notificationEmail" style="background-color: #FFFFFF;border: 1px solid #D0D0D0;color: #767676;font-size: 11px;margin: 5px;padding: 2px; width:180px;" styleId="notificationEmail"/>
	                                                                        </td>
	                                                                    </tr>
																	<tr>
																		<td width="169" height="30" align="right"style="font-size: 11px;
    font-weight: bold; color:#000;">
																			<digi:trn key="aim:viewEditUser:country">Administrative Level 0</digi:trn>
																			<font color="red">*</font>
																	    </td>
																	    <td width="190" height="30">
																           <html:select name="umViewEditUserForm" property="selectedCountryIso"  style="background-color: #FFFFFF;border: 1px solid #D0D0D0;color: #767676;font-size: 11px;margin: 5px;padding: 2px; width:180px;" styleId="country">
																                <c:set var="translation">
																                  <digi:trn key="aim:viewEditUser:selectCountry">
																                  --Select country--
																                  </digi:trn>
																                </c:set>
																                <html:option value="-1">${translation}</html:option>
																                <c:if test="${!empty umViewEditUserForm.countries}">
																                  <c:forEach var="cn" items="${umViewEditUserForm.countries}">
																                    <html:option value="${cn.iso}">${cn.name}</html:option>
																                  </c:forEach>
																                </c:if>
																             </html:select>
																		</td>
																	</tr>


																	<tr>
																		<td width="169" align="right" height="30"style="font-size: 11px;
    font-weight: bold; color:#000;">
                                                                    		<digi:trn key="aim:viewEditUser:mailingAddress">Mailing address</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                    		<html:text name="umViewEditUserForm" property="mailingAddress"  style="background-color: #FFFFFF;border: 1px solid #D0D0D0;color: #767676;font-size: 11px;margin: 5px;padding: 2px; width:180px;"/>
																		</td>
                                                          			</tr>
																	<c:if test="${umViewEditUserForm.truBudgetEnabled=='true'}">

																	<tr>
																		<td align=right style="font-size: 11px;">
																				<%--																			<FONT color=red>*</FONT>--%>
																			<digi:trn key="um:truBudgetIntents">TruBudget Permissions </digi:trn></td>
																		<td align="left">
																			<html:select property="selectedTruBudgetIntents" styleClass="inp-text" multiple="true">
																				<c:forEach var="cn"	items="${umViewEditUserForm.truBudgetIntents}">
																					<option value="${cn.truBudgetIntentName}" <c:if test="${cn.userHas}"> selected </c:if> ><c:out value="${cn.truBudgetIntentDisplayName}"/></option>
																				</c:forEach>
																			</html:select>
																		</td>
																	</tr>

																	<tr>
																		<td width="169" align="right" height="30" style="font-size: 11px;
    font-weight: bold; color:#000;">
																			<digi:trn key="aim:viewEditUser:confirmPassword">New TruBudget Password:</digi:trn>
																		</td>
																		<td width="380" height="30" colspan="2"  class="inputcontainer">
																			<html:password name="umViewEditUserForm" property="truBudgetPassword" redisplay="false"/>
																		</td>
																	</tr>
																		<%--																	<tr>--%>
																		<%--																		<td width="169" align="right" height="30"style="font-size: 11px;--%>
																		<%--    font-weight: bold; color:#000;">--%>
																		<%--																			<digi:trn key="aim:viewEditUser:confirmPassword">Confirm:</digi:trn>--%>
																		<%--																		</td>--%>
																		<%--																		<td width="380" height="30" colspan="2"  class="inputcontainer">--%>
																		<%--																			<html:password name="umViewEditUserForm" property="confirmNewPassword" redisplay="false"/>--%>
																		<%--																		</td>--%>
																		<%--																	</tr>--%>
																	</c:if>
                                                          			<tr>
																		<td width="169" align="right" height="2">&nbsp;
                                                                            
																		</td>																		
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30"style="font-size: 11px;
    font-weight: bold; color:#000;">
																	       <digi:trn key="aim:viewEditUser:organizationType">Organization type</digi:trn>
																		</td>
																		<td width="380" height="30" colspan="2">
																			<html:select name="umViewEditUserForm" property="selectedOrgTypeId" onchange="goAction('typeSelected');"  style="background-color: #FFFFFF;border: 1px solid #D0D0D0;color: #767676;font-size: 11px;margin: 5px;padding: 2px; width:180px;">
																                <c:set var="translation">
																                  <digi:trn key="aim:viewEditUser:selectOrganisationType">
																                  --Select organisation type--
																                  </digi:trn>
																                </c:set>
																                <html:option value="-1">${translation}</html:option>
																                <c:if test="${!empty umViewEditUserForm.orgTypes}">
																                  <html:optionsCollection name="umViewEditUserForm" property="orgTypes" value="ampOrgTypeId" label="orgType"/>
																                </c:if>
																            </html:select>
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30"style="font-size: 11px;
    font-weight: bold; color:#000;">
																	        <digi:trn key="aim:viewEditUser:organizationGroup">Organization group</digi:trn>
																		</td>
																		<td width="380" height="30" colspan="2">
																	         <html:select name="umViewEditUserForm" property="selectedOrgGroupId" onchange="goAction('groupSelected');"  style="background-color: #FFFFFF;border: 1px solid #D0D0D0;color: #767676;font-size: 11px;margin: 5px;padding: 2px; width:180px;">
																                <c:set var="translation">
																                  <digi:trn key="aim:viewEditUser:selectOrganisationGroup">
																                  --Select organisation group--
																                  </digi:trn>
																                </c:set>
																                <html:option value="-1">${translation}</html:option>
																                <c:if test="${!empty umViewEditUserForm.orgGroups}">
																                  <html:optionsCollection name="umViewEditUserForm" property="orgGroups" value="ampOrgGrpId" label="orgGrpName"/>
																                </c:if>
																              </html:select>
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30"style="font-size: 11px;
    font-weight: bold; color:#000;">
                                                                   			<digi:trn key="aim:viewEditUser:organisationName">Organisation name</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                    		<html:select name="umViewEditUserForm" property="selectedOrgId"  style="background-color: #FFFFFF;border: 1px solid #D0D0D0;color: #767676;font-size: 11px;margin: 5px;padding: 2px; width:180px;">
																                <c:set var="translation">
																                  <digi:trn key="aim:viewEditUser:selectOrganisation">
																                  --Select organisation--
																                  </digi:trn>
																                </c:set>
																                <html:option value="-1">${translation}</html:option>
																                <c:if test="${!empty umViewEditUserForm.orgs}">
																                  <html:optionsCollection name="umViewEditUserForm" property="orgs" value="ampOrgId" label="name"/>
																                </c:if>
																             </html:select>
                                                               			</td>
																	</tr>

																	<tr>
																		<td width="169" align="right" height="30"style="font-size: 11px;
    font-weight: bold; color:#000;">
                                                                 			<digi:trn key="aim:viewEditUser:selectOrgVeified">Select Organization To Add as Verified</digi:trn> 
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                    		<html:select name="umViewEditUserForm" property="assignedOrgId"  style="background-color: #FFFFFF;border: 1px solid #D0D0D0;color: #767676;font-size: 11px;margin: 5px;padding: 2px; width:180px;">
																                <c:set var="translation">
																                  <digi:trn key="aim:viewEditUser:selectOrganisation">
																                  --Select organisation--
																                  </digi:trn>
																                </c:set>
																                <html:option value="-1">${translation}</html:option>
																                <c:if test="${!empty umViewEditUserForm.orgs}">
																                  <html:optionsCollection name="umViewEditUserForm" property="orgs" value="ampOrgId" label="name"/>
																                </c:if>
																            </html:select>
																		</td>
																	</tr>
																	
																	<tr>
																		<td width="169" align="right" height="30">&nbsp;</td>
																		<td class="addUserButContainer">
																		  <bean:define id="addOrganization"><digi:trn>Add Organization</digi:trn></bean:define>
		 	 	 	 		                                                <input type="button" value="${addOrganization}" onclick="goAction('addOrg');" style="font-family:verdana; font-size:11px; min-width:60px; "/>                                                                                                                                             
	 	                                                                </td>
																	</tr>
																	
																	<tr>
																		<td width="169" align="right" height="30"style="font-size: 14px;
    font-weight: bold; color:#000;"><digi:trn key="aim:viewEditUser:verifiedAssignedOrgs">Verified Assigned Organisations</digi:trn></td>
                                                                        <td>
	                                                                        <table width="80%" cellSpacing="1" cellPadding="1" vAlign="top" align="left" bgcolor="#ffffff">
    	                                                                        <logic:iterate id="organisation" name="umViewEditUserForm" property="assignedOrgs" indexId="ind">
        		                                                                    <tr>
                	                                                                    <td width="2%">
                                                                                             <html:multibox name="umViewEditUserForm" property="selAssignedOrgs" styleId="verOrg_${ind}">
                                                                                                     <bean:write name="organisation" property="ampOrgId" scope="page"/>
                                                                                             </html:multibox>
                    	                                                                 </td>
                        	                                                             <td align="left" width="49%">
                                                                                             <bean:write name="organisation" property="name" scope="page"/>
                            	                                                         </td>                                                                                                   
                                		                                             </tr>
                                        	                                     </logic:iterate>
                                            		                         </table>
                                                                        </td>
                                                                    </tr>
                                                                    
                                                                    <tr>
																		<td width="169" align="right" height="30">&nbsp;</td>
 	 	 	 															<td class="addUserButContainer">
																			<logic:notEmpty name="umViewEditUserForm" property="assignedOrgs">
																			    <bean:define id="removeOrganization"><digi:trn>Remove Organizations</digi:trn></bean:define>
																				<input type="button" value="${removeOrganization}" onclick="goAction('delOrgs');" style="font-family:verdana; font-size:11px; min-width:60px; "/>
																			</logic:notEmpty>                                                                                                                                                       
 	 	 	 															</td>
																	</tr>
                                                          			<tr>
																		<td width="169" height="30" align="right"style="font-size: 11px; font-weight: bold; color:#000;">
																			<digi:trn>Verified Region</digi:trn>
																	    </td>
																	    <td width="190" height="30">
																           <html:select name="umViewEditUserForm" property="selectedRegionId"  style="background-color: #FFFFFF;border: 1px solid #D0D0D0;color: #767676;font-size: 11px;margin: 5px;padding: 2px; width:180px;" styleId="region">
																                <c:set var="translation">
																                  <digi:trn>
																                  --Select region--
																                  </digi:trn>
																                </c:set>
																                <html:option value="-1">${translation}</html:option>
																                <c:if test="${!empty umViewEditUserForm.regions}">
																                  <c:forEach var="cn" items="${umViewEditUserForm.regions}">
																                    <html:option value="${cn.id}">${cn.name}</html:option>
																                  </c:forEach>
																                </c:if>
																             </html:select>
																		</td>
																	</tr>
                                                                    
																	<tr>
																		<td width="169" align="right" height="30"style="font-size: 11px;
    font-weight: bold; color:#000;">
                                                                     		 <digi:trn key="aim:viewEditUser:languageSettings">Language settings</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
          																	<html:select name="umViewEditUserForm" property="selectedLanguageCode"  style="background-color: #FFFFFF;border: 1px solid #D0D0D0;color: #767676;font-size: 11px;margin: 5px;padding: 2px; width:180px;">
																                <c:set var="translation">
																                  <digi:trn key="aim:viewEditUser:selectLanguage">
																                  --Select language--
																                  </digi:trn>
																                </c:set>
																                <html:option value="-1">${translation}</html:option>
																                <c:if test="${!empty umViewEditUserForm.languages}">
																                  <html:optionsCollection name="umViewEditUserForm" property="languages" value="code" label="name"/>
																                </c:if>
																             </html:select>
																		</td>
																	</tr>
																	<module:display name="Pledges" parentModule="PROJECT MANAGEMENT">
																	<tr>
																		<td width="169" align="right" height="30"style="font-size: 11px;
    font-weight: bold; color:#000;">
                                                                     		 <digi:trn key="um:pledgesUser">Pledges User</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
          																	<html:checkbox property="pledger"
																						   style="margin: 5px"
																						   styleClass="inp-text"

																			styleId="pledger"/>
																		</td>
																	</tr>
																		<tr>
																			<td width="169" align="right" height="30"style="font-size: 11px;
    font-weight: bold; color:#000;">
																				<digi:trn key="um:pledgesSuperUser">Pledges Super User</digi:trn>
																			</td>
																			<td width="380" height="30" colspan="2">
																				<c:if
																						test="${umViewEditUserForm.pledger}">
																				<html:checkbox
																						property="pledgeSuperUser"
																							   style="margin: 5px"
																						styleClass="inp-text"
																						styleId="pledgeSuperUser"/>
																				</c:if>
																				<c:if
																						test="${!umViewEditUserForm.pledger}">
																					<html:checkbox
																							property="pledgeSuperUser"
																							style="margin: 5px"
																							styleClass="inp-text"
																							styleId="pledgeSuperUser"
																							disabled="true"/>
																				</c:if>
																			</td>
																		</tr>
																	</module:display>																	
																		<tr>
																		<td width="169" align="right" height="30"style="font-size: 11px;font-weight: bold; color:#000;">
                                                                     		 <digi:trn key="um:nationalCoordinator">National Coordinator</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
          																	<html:checkbox property="nationalCoordinator" style="margin: 5px" styleClass="inp-text"/>
																		</td>
																	</tr>																	
																		<tr>
																			<td align="right"  class="formCheckContainer">
																				<digi:trn>Assign to Workspace</digi:trn>
																			</td>
																			<td class="f-names" align="left">
																				<c:if test="${!umViewEditUserForm.banReadOnly}">
																					<html:checkbox property="addWorkspace" style="margin: 5px" styleClass="inp-text"></html:checkbox>
																				</c:if>
																				<c:if test="${umViewEditUserForm.banReadOnly}">
																					<html:checkbox property="addWorkspace" style="margin: 5px" styleClass="inp-text" disabled="true"></html:checkbox>
																				</c:if>
																			</td>
																		</tr>
																		<tr>
																		<td width="169" align="right" height="30"style="font-size: 11px;font-weight: bold; color:#000;">
                                                                     		 <digi:trn key="um:exemptFromDataFreezing">Exempt from activity freezing</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
          																	<html:checkbox property="exemptFromDataFreezing" style="margin: 5px" styleClass="inp-text"/>
																		</td>
																	 </tr>
																	<tr>
															            <td>&nbsp;</td>
															          </tr>
																	<tr>
																	    <td  class="addUserButContainer" width="380" height="30" colspan="2" align="center"style="font-size: 11px; font-weight: bold; color:#000;">
                                                              				<c:set var="translation">
																                <digi:trn jsFriendly="true">Save</digi:trn>
																              </c:set>
																              <input type="button" value="${translation}" onclick="goAction('save');" style="font-family:verdana; font-size:11px; min-width:60px; "/>
																
																              <c:set var="translation">
																                <digi:trn jsFriendly="true">
																                Cancel
																                </digi:trn>
																              </c:set>
																              <input type="button" value="${translation}" onclick="cancel();" style="font-family:verdana; font-size:11px; min-width:60px; "/>
																		</td>
																	</tr>																	
																	<tr>
															            <td>&nbsp;</td>
															          </tr>
															          <tr>
															            <td>&nbsp;</td>
															          </tr>
																	<tr>
																		<c:set var="translation">
															              <digi:trn key="aim:viewEditUser:changePasswordButton">Change password</digi:trn>
															            </c:set>
																		<td width="169" valign="top" align="right"
																			height="30" style="font-size: 11px;
    font-weight: bold; color:#000;">
																			<div style="margin-top: 10px;">
                                                                   			<digi:trn key="aim:viewEditUser:password">Password:</digi:trn>
																			</div>
																		</td>
																	    <td width="380" valign="top" height="30" colspan="2" class="inputcontainer">
                                                                    		<html:password name="umViewEditUserForm"
																						   property="newPassword"
																						   redisplay="false"/>
																			<div style="padding-left: 2px; margin: 5px">
																				<div style="display: none" class="pwd_container" id="pwd_container">
																					<span class="pwstrength_viewport_verdict">&nbsp;</span>
																					<span class="pwstrength_viewport_progress"></span>
																				</div>
																			</div>
                                                               			</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30"style="font-size: 11px;
    font-weight: bold; color:#000;">
                                                                   			<digi:trn key="aim:viewEditUser:confirmPassword">Confirm:</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2"  class="inputcontainer">
                                                                    		<html:password name="umViewEditUserForm" property="confirmNewPassword" redisplay="false"/>
                                                               			</td>
																	</tr>
																	<tr>
																		<c:set var="errMsg">
																           <digi:trn key="aim:viewEditUser:errorMessage" jsFriendly="true">
																              Either fields are blank or their values do not match
																            </digi:trn>
																        </c:set>
																	    <td  class="addUserButContainer" width="270" height="30" coslpan="2" align="right">
                                                              				<input type="button" value="${translation}" onclick="validate('${errMsg}','changePassword');" style="font-family:verdana;font-size:11px;"/></td>
																		</td>
																		<td   class="addUserButContainer" width="169" height="30" colspan="2" align="left">
																			<c:set var="translation">
																                <digi:trn key="aim:viewEditUser:resetPasswordButton" jsFriendly="true">Reset </digi:trn>
																              </c:set>
                                                              				<input type="button" value="${translation}" onclick="resetPasswordFields()" style="font-family:verdana;font-size:11px;"/></td>
																		</td>
																	</tr>
																			
																</table>
                                                                <br/>
															</td>
														</tr>
													<!-- end page logic -->
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
        </td>
    </tr>
  </table>
        <br />
</digi:form>
</div>
</center>










