<meta http-equiv="Content-Language" content="en-us">
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>

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