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

<digi:form action="/login.do" method="post" name="aimLoginForm" type="org.digijava.ampModule.aim.form.LoginForm" onsubmit="return validateAimLoginForm(this);">

<table width="100%" valign="top" align="left" cellpadding="0" cellspacing="0" border="0">
<tr><td width="100%" valign="top" align="left">
<jsp:include page="header.jsp"  />			
</td>
</tr>
<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff border="0" cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;
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
						<digi:img src="ampModule/aim/images/arrow-th-BABAB9.gif" width="16"/>
							<digi:trn key="aim:registeredUserLogIn">
							Registered User Log In:
							</digi:trn>
					</td>
					<td align="left">
						<html:text property="userId" size="20" styleClass="inp-text"/> &nbsp;
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
						<digi:img src="ampModule/aim/images/arrow-th-BABAB9.gif" width="16"/>
						<digi:trn key="aim:password">
						Password:
						</digi:trn>
					</td>
					<td align="left">
						<html:password property="password" size="20" styleClass="inp-text"/>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>				
					<td>&nbsp;</td>
					<td align="left">
						<html:submit value="Sign In" property="submitButton" styleClass="dr-menu" onclick="submitForm()"/>
					</td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;&nbsp;
						<c:set var="translation">
							<digi:trn key="aim:clickToGoHome">Click here to go Home</digi:trn>
						</c:set>
						<digi:link href="/" title="${translation}" >
							<digi:trn key="aim:home">
							Home
							</digi:trn>
						</digi:link>
					</td>
				</tr>

				<tr>
					<td>
						<br>
					</td>
				</tr>				
			</table>
		</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>



