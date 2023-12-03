<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.digijava.ampModule.aim.util.FeaturesUtil" %>
<%@ page import="org.digijava.kernel.config.DigiConfig" %>
<%@ page import="org.digijava.kernel.util.DigiConfigManager" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script language="JavaScript">
<!--

	function submitForm() {
		document.aimLoginForm.submitButton.disabled=true;
		document.aimLoginForm.submit();
	}

-->
</script>

<%-- 
<table width="100%" height="100%" valign="top" align="left" cellpadding="0" cellspacing="0" border="0">
<tr><td width="100%" valign="top" align="left">
<form action="/j_spring_security_check" method="post"">
<table bgColor=#ffffff border="0" cellpadding="0" cellspacing="0" width=757>
	<tr>
		<td align=left valign="top" width=520><br>
			<table border="0" cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td width="3%">&nbsp;</td>
					<td colspan="2">

                    <%
					
					String logoutMessage = (String)session.getAttribute("showLogoutMessage");
					if(logoutMessage != null && !logoutMessage.equals("")){
					%>
<div align="center" class=f-names noWrap>
                    <digi:trn key="aim:ampLogout">
                   		You have been logged out
                    </digi:trn>
                   </div>
                    <%
						session.removeAttribute("showLogoutMessage");
					}
					%>
						<digi:errors/>
						<c:if test="${param['loginError'] != null}">
						  <bean:message key="errors.header" />
						  <bean:message key="errors.prefix" />

                                                  <c:if test="${param['loginError'] == 'invalidLogin'}">
                                                     <c:set var="errorDisplayed">true</c:set>
           					     <bean:message key="error.aim.invalidLogin" />
                                                  </c:if>

                                                  <c:if test="${param['loginError'] == 'userBanned'}">
                                                     <c:set var="errorDisplayed">true</c:set>
           					     <bean:message key="error.aim.userBanned" />
                                                  </c:if>


                                                  <c:if test="${param['loginError'] == 'noTeamMember'}">
                                                     <c:set var="errorDisplayed">true</c:set>
                                                     <digi:trn key="error.aim.userinvalidteammember">The user is not allowed to login</digi:trn>
           					     				</c:if>
                                                  
                                                  <c:if test="${param['loginError'] == 'invalidUser'}">
                                                     <c:set var="errorDisplayed">true</c:set>
           					     <bean:message key="error.aim.userInvalid" />
                                                  </c:if>

                                                  <c:if test="${errorDisplayed == null}">
           					     <bean:message key="error.aim.loginFailed" />
                                                  </c:if>

						  <bean:message key="errors.suffix" />
						  <bean:message key="errors.footer" />
						</c:if>
					</td>
				</tr>
				<tr>
					<td width="3%">&nbsp;</td>
					<td align=right valign="top" class=f-names noWrap width="31%">
							<digi:trn key="aim:registeredUserLogIn">
							Registered <u>U</u>ser Log In:
							</digi:trn>
					</td>
					<td align="left">
						<input type="text" name="j_username" size="20" class="inp-text" accesskey="u"/> &nbsp;
						<font color="red"><br>
						<digi:trn key="aim:userIdExample1">
						yourname@emailaddress.com
						</digi:trn>
						</font>
					</td>
				</tr>
				<tr>
					<td width="3%">&nbsp;</td>
					<td align=right class=f-names noWrap width="31%">
						<digi:trn key="aim:password">
						<u>P</u>assword:
						</digi:trn>
					</td>
					<td align="left">
					    <input type="password" name="j_password" size="20" class="inp-text" accesskey="p"/>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td align="left">
						<html:submit  styleClass="dr-menu" property="submitButton"><digi:trn key="btn:signIn">Sign In</digi:trn></html:submit>
					</td>
				</tr>

		          <td>&nbsp;</td>
		          <td>&nbsp;</td>
                  <td >
                    <c:set var="trn1">
                      <digi:trn key="aim:clickifyouForgotYourPassword">Click here if you have forgot your password</digi:trn>
                    </c:set>
                    <digi:link href="/showEmailForm.do" ampModule="aim" title="${trn1}">
                      <digi:trn key="aim:forgotPassword">
                      Forgot Password
                      </digi:trn>
                    </digi:link>
                    |
                    <c:set var="trn2">
                      <digi:trn key="aim:clickToChangeYourPassword">Click here if you want to change your password</digi:trn>
                    </c:set>
                    <digi:link href="/showChangePassword.do" ampModule="aim" title="${trn2}">
                      <digi:trn key="aim:changePassword">
                      Change Password
                      </digi:trn>
                    </digi:link>
                  </td>
    
	
		<td>
			<%if(DigiConfigManager.getConfig().isEnableAutoLogin()){%>
				<font color="red"><digi:trn key="aim:warningAutoLogin">Warning: Auto Login option is enabled, please disable it in production environments.</digi:trn></font>
			<%}%>
		</td>
	
				<tr>
					<td colspan=3>&nbsp;
					</td>
				</tr>
			</table>
		</td>
		<td bgcolor="#dbe5f1" valign="top">
	      <table align="center" border="0" cellPadding=3 cellspacing="0" width="90%">
      		 <tr>
		          <td valign="top"><br>
		          	&nbsp;
      	     	</td>
        		</tr>
        		<tr>
	          	<td valign="top" >
<strong>                        <digi:trn key="aim:loginWarning1">
                        AMP is used to report and manage aid flows from donors.<br> 
                        This site is accessible to registered users only. 
                        </digi:trn>
<br><br>
						 <digi:trn key="aim:loginWarning2">
						By attempting to log in you are confirming that you are registered and qualified to access one or more secure applications for official business.

						 </digi:trn>
</strong>						<BR><BR>
          		</td>
  				</tr>
        		<tr>
          		<td valign="top">&nbsp;</td>
  				</tr>
	      </table>
		</td>
	</tr>
</form>
</table>
</td></tr>
</table>
--%>
