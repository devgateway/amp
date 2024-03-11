<meta http-equiv="Content-Language" content="en-us">
<%@ page language="java" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>


<html:javascript formName="userChangePasswordForm" />
<digi:errors/>
<table width="70%">
<digi:form action="/userChangePassword.do?action=update" method="post" onsubmit="return validateUserChangePasswordForm(this);" >
	<tr>
		<td colspan="2" align="left" class="dgTitle"> &nbsp;<digi:trn key="um:changePassword">Change Password</digi:trn></td>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td nowrap width="20%" align="right"><digi:trn key="um:currentPassword">Current Password:</digi:trn></td>
		<td width="50%" align="left"><html:password property="currentpassword"/></td>
	</tr>
	<tr>
	   <td nowrap width="20%" align="right"><digi:trn key="um:newPassword">New Password:</digi:trn></td>
	   <td width="50%" align="left"><html:password property="newpassword"/></td>
	</tr>
	<tr>
	   <td nowrap width="20%" align="right"><digi:trn key="um:confirmPassword">Confirm Password:</digi:trn></td>
	   <td width="50%" align="left"><html:password property="confirmpassword"/></td>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td colspan=2><html:submit value="Update"/><html:reset/></td>
	</tr>
</digi:form>
</table>