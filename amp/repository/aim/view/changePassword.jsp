<%@ page import="org.digijava.module.aim.util.TeamUtil" %>
<%@ page import="org.digijava.module.aim.util.TeamMemberUtil" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<jsp:include page="/repository/aim/view/strongPassword.jsp"  />

<digi:instance property="aimChangePasswordForm" />
<script language="JavaScript">
function isVoid(name){
	if ( name == null ||name.length==0){
    	return true;
    }		
	return false;		
}

function validate(){
        var email = document.aimChangePasswordForm.userId.value;
        var oldpassword = document.aimChangePasswordForm.oldPassword.value;
        var password = document.aimChangePasswordForm.newPassword.value;
        var passwordConfirmation = document.aimChangePasswordForm.confirmNewPassword.value;
        var errors=new Array();
        if(isVoid(email)){
        	errors.push('<digi:trn jsFriendly="true">Email is required</digi:trn>');
        }
        else{
        	 var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
     		 if(!reg.test(email)){
     			errors.push('<digi:trn jsFriendly="true">Please enter a valid email address.</digi:trn>');
     		 }
        }
        if(isVoid(oldpassword)){
        	errors.push('<digi:trn jsFriendly="true">Old password is a required field</digi:trn>');
        }
        if(isVoid(password)){
        	errors.push('<digi:trn jsFriendly="true">New password is a required field</digi:trn>');
        }  
        if(password != passwordConfirmation){
        	errors.push('<digi:trn jsFriendly="true">Passwords in both fields must be the same</digi:trn>');
        }
        if(errors.length>0){
        	 alert(errors.join('\n'));
        	 return false;
        }
        return true;
	}

	</script>

<digi:form action="/changePassword.do" method="post" onsubmit="return validate();">
<div class="reg_form_container">
<div class="home_sec_title"><digi:trn key="aim:changePassword">
						Change Password
						</digi:trn></div>
	<table>
		<tr>
			<td bgcolor="#dbe5f1" style="padding: 10px 20px;">
				<span class="formnote">
						<digi:trn key="aim:loginWarning">
							<h4 class="login_warning_title">You are signing-in to one or more secure applications for
							official business.</h4>
							 You have been granted the right to access these
							applications and the information contained in them to facilitate
							your official business. Your accounts and passwords are your
							responsibility. Do not share them with anyone.
						</digi:trn>
						<BR><BR>
				</span>
			</td>


		</tr>
	</table>

<table width="100%" valign="top" align="left" cellpadding="0" cellspacing="0" border="0">
<tr>
<td width="100%" valign="top" align="left">
<table bgColor=#ffffff border="0" cellpadding="5" cellspacing="1" width="100%">
	<tr>
		<td width="1%">&nbsp;
		</td>
		<td align=left valign="top" width="60%">
			<digi:errors/>
			<jsp:include page="/repository/aim/view/strongPasswordRulesLegend.jsp"  />
		</td>
	</tr>
	<tr>
		<td width="5%">&nbsp;
		</td>
		<td align=left valign="top" width="100%"><br>
			<table border="0" cellPadding=5 cellspacing="0" width="100%">
				
				<tr class="input_row_container">
					<td width="3">&nbsp;</td>
					<td class=f-names style="padding-bottom:30px;" noWrap width="18%" valign=top>
<!--						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>-->
						<span style="color:#FF0000;">*</span>
						<digi:trn key="aim:email">
							UserId
						</digi:trn>
					</td>
					<td align="left" style="padding-bottom:10px;">
						<html:text property="userId" styleClass="pwd_username" readonly="${aimChangePasswordForm.userIdEnabled}" size="20"/><span>&nbsp;&nbsp;&nbsp;</span>

						<font color="red" style="font-size:11px;">
						<digi:trn key="aim:userIdExample1">
						e.g. yourname@emailaddress.com
						</digi:trn>
						</font>
					</td>
				</tr>
				<tr class="input_row_container">
					<td width="3">&nbsp;</td>
					<td class=f-names style="padding-bottom:10px;" noWrap>
<!--						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>-->
						<span style="color:#FF0000;">*</span> <digi:trn key="aim:oldPassword">Old Password</digi:trn>
					</td>
					<td align="left" style="padding-bottom:10px;">
						<html:password property="oldPassword" size="20" />
					</td>
				</tr>
				<tr class="input_row_container">
					<td width="3">&nbsp;</td>
					<td class=f-names style="padding-bottom:10px;" valign="top" noWrap>
<!--						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>-->
						<span style="color:#FF0000;">*</span> <digi:trn key="aim:newPassword">New Password</digi:trn>
					</td>
					<td align="left" style="padding-bottom:10px;">
							<html:password property="newPassword" size="20" />
						<div style="display: none" class="pwd_container" id="pwd_container">
							<span class="pwstrength_viewport_verdict">&nbsp;</span>
							<span class="pwstrength_viewport_progress"></span>
						</div>
					</td>
				</tr>
				<tr class="input_row_container">
					<td width="3">&nbsp;</td>
					<td class=f-names style="padding-bottom:10px;" noWrap>
<!--						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>-->
						<span style="color:#FF0000;">*</span> <digi:trn key="aim:confirmNewPassword">Confirm new
						Password</digi:trn>
					</td>
					<td align="left" style="padding-bottom:10px;">
						<html:password property="confirmNewPassword" maxlength="16" size="20" />
					</td>
				</tr>

				<tr class="reg_submit_btn_container">
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td align="right" >
						<html:submit styleClass="buttonx"><digi:trn key="btn:submit">Submit</digi:trn></html:submit>
					</td>

				</tr>

				<tr>
					<td colspan=3>&nbsp;</td>
				</tr>

			</table>
		</td>
		<td width="5%">&nbsp;
		</td>
<%--		<td bgcolor="#dbe5f1" valign="top" width="30%">--%>
<%--	      <table align="center" border="0" cellPadding=3 cellspacing="0" width="90%">--%>
<%--        		<tr>--%>
<%--		          <td valign="top">&nbsp;</td>--%>
<%--        		</tr>--%>
<%--        		<tr>--%>
<%--	          	<td valign="top">--%>
<%--                <span class="formnote">--%>
<%--						<digi:trn key="aim:loginWarning">--%>
<%--						 You are signing-in to one or more secure applications for--%>
<%--        			    official business. You have been granted the right to access these--%>
<%--          		 	 applications and the information contained in them to facilitate--%>
<%--           			 your official business. Your accounts and passwords are your--%>
<%--						 responsibility. Do not share them with anyone.--%>
<%--						 </digi:trn>--%>
<%--						<BR><BR>--%>
<%--				</span>--%>
<%--          		</td>--%>
<%--  				</tr>--%>
<%--        		<tr>--%>
<%--          		<td valign="top">&nbsp;</td>--%>
<%--  				</tr>--%>
<%--	      </table>--%>
<%--		</td>--%>
	</tr>
</table>
</td></tr>
</table>

<%--	<div class="reg_form_container">--%>
<%--		<div class="home_sec_title"><digi:trn key="aim:changePassword">--%>
<%--			Change Password--%>
<%--		</digi:trn></div>--%>

<%--		<span class="formnote">--%>
<%--						<digi:trn key="aim:loginWarning">--%>
<%--							You are signing-in to one or more secure applications for--%>
<%--							official business. You have been granted the right to access these--%>
<%--							applications and the information contained in them to facilitate--%>
<%--							your official business. Your accounts and passwords are your--%>
<%--							responsibility. Do not share them with anyone.--%>
<%--						</digi:trn>--%>
<%--						<BR><BR>--%>
<%--		</span>--%>

<%--		<div class="login_warning_container">--%>

<%--	    <digi:trn key="aim:loginWarning">--%>
<%--			<h5 class="formnote">You are signing-in to one or more secure applications for--%>
<%--				official business.--%>
<%--			</h5>--%>
<%--			<p>--%>
<%--				You have been granted the right to access these--%>
<%--				applications and the information contained in them to facilitate--%>
<%--				your official business. Your accounts and passwords are your--%>
<%--				responsibility. Do not share them with anyone.--%>
<%--			</p>--%>
<%--		</digi:trn>--%>

<%--		</div>--%>
<%--		--%>
<%--		<div>--%>
<%--			--%>
<%--		</div>--%>
<%--		--%>

<%--	</div>--%>
</div>
</digi:form>



