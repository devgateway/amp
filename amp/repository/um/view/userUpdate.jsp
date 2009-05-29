<meta http-equiv="Content-Language" content="en-us">
<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:errors/>
<table width="100%">
<digi:form action="/userChangePassword.do?action=update" method="post" >
<tr><td colspan="2"><font size="5"><digi:trn key="um:updatePassword">Update Password</digi:trn></font></td></tr>
<tr><td><digi:trn key="um:currentPassword">Current Password</digi:trn></td><td><html:password property="currentpassword"/></td></tr>
<tr><td><digi:trn key="um:newPassword">New Password</digi:trn></td><td><html:password property="newpassword"/></td></tr>
<tr><td><digi:trn key="um:confirmPassword">Confirm Password</digi:trn></td><td><html:password property="confirmpassword"/></td></tr>
<tr>
<td colspan=2>
<html:submit property="submit" value="Submit"/>
</td>
</tr>
</digi:form>
</table>