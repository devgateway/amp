<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>	

<script language="JavaScript">
<!--
	
	function submitForm() {
		document.aimLoginForm.submitButton.disabled=true;
		document.aimLoginForm.submit();
	}

-->
</script>


<html:javascript formName="aimLoginForm"/>

<table width="100%" valign="top" align="left" cellpadding="0" cellspacing="0" border="0">
<tr><td width="100%" valign="top" align="left">
<jsp:include page="header.jsp"  />
</td>
</tr>
<tr><td width="100%" valign="top" align="left">
<digi:form action="/login.do" method="post" onsubmit="return validateAimLoginForm(this);">
<table bgColor=#ffffff border="0" cellpadding="0" cellspacing="0" width=757>
	<tr>
		<td class=r-dotted-lg width=5>&nbsp;
		</td>
		<td align=left class=r-dotted-lg valign="top" width=520><br>
			<table border="0" cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td width="3%">&nbsp;</td>				
					<td colspan="2">
						<digi:errors/>
					</td>
				</tr>
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align=right class=f-names noWrap width="31%">
						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
							<digi:trn key="aim:registeredUserLogIn">
							Registered <u>U</u>ser Log In:
							</digi:trn>
					</td>
					<td align="left">
						<html:text property="userId" size="20" styleClass="inp-text" accesskey="u"/> &nbsp;
						<font color="red"><br>
						<digi:trn key="aim:userIdExample1">
						e.g. yourname@emailaddress.com
						</digi:trn>						
						</font>
					</td>
				</tr>
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align=right class=f-names noWrap width="31%">
						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
						<digi:trn key="aim:password">
						<u>P</u>assword:
						</digi:trn>
					</td>
					<td align="left">
						<html:password property="password" size="20" styleClass="inp-text" accesskey="p"/>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>				
					<td>&nbsp;</td>
					<td align="left">
						<html:submit value="Sign In" styleClass="dr-menu" property="submitButton" onclick="return submitForm()"/>
					</td>
				</tr>

				<tr>
				</tr>
		          <td>&nbsp;</td>
					 <td colSpan=2>
						<c:set var="translation">
							<digi:trn key="aim:clickifyouForgotYourPassword">Click here if you have forgot your password</digi:trn>
						</c:set>
						<digi:link href="/showEmailForm.do" title="${translation}">
						<digi:trn key="aim:forgotPassword">
						Forgot Password
						</digi:trn>
						</digi:link>
						 | 
						<c:set var="translation">
							<digi:trn key="aim:clickToChangeYourPassword">Click here if you want to change your password</digi:trn>
						</c:set>
						<digi:link href="/showChangePassword.do" title="${translation}">
						<digi:trn key="aim:changePassword">
						Change Password
						</digi:trn>
						</digi:link>
						<br><br>
						<c:set var="translation">
							<digi:trn key="aim:clickToRegister">Click here to register</digi:trn>
						</c:set>
						<digi:link href="/showRegisterUser.do" title="${translation}">
						<digi:trn key="aim:newUserRegister">
						New user register
						</digi:trn>
						</digi:link>
					</td>
				</tr>
				<tr>
					<td colspan=3>&nbsp;
					</td>
				</tr>
			</table>
		</td>
		<td bgColor=#f7f7f4 class=r-dotted-lg valign="top">
	      <table align="center" border="0" cellPadding=3 cellspacing="0" width="90%">
      		 <tr>
		          <td class=r-dotted-lg-buttom valign="top"><br>
		          	&nbsp;
      	     	</td>
        		</tr>
        		<tr>
		          <td valign="top">&nbsp;</td>
        		</tr>
        		<tr>
	          	<td class=r-dotted-lg-buttom valign="top">
						<digi:img src="module/aim/images/i-C2160E.gif" width="13" height="9"/>
						 <digi:trn key="aim:loginWarning">
						 You are signing-in to one or more secure applications for        
	        		     official business. You have been granted the right to access these        
    	      		 	 applications and the information contained in them to facilitate        
        	   			 your official business. Your accounts and passwords are your        
						 responsibility. Do not share them with anyone.        
						 </digi:trn>
						<BR><BR>
          		</td>
  				</tr> 
        		<tr>
          		<td valign="top">&nbsp;</td>
  				</tr>
	      </table>
		</td>
	</tr>
</digi:form>
</table>
</td></tr>
</table>



