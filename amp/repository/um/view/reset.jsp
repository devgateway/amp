<meta http-equiv="Content-Language" content="en-us">
<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page import="org.digijava.ampModule.um.form.UserResetForm" %>

<digi:instance id="page_userResetForm" property="userResetForm" />
<digi:context name="userResetAction" property="context/ampModule/moduleinstance/userResetPassword.do?action=Reset" />
<digi:errors/>
      <TABLE width="100%">

      <form action='<%= userResetAction %>' method="post" >
        

<tr><td colspan="2"><b><font size="5"><digi:trn key="um:resetPassword">Reset Password</digi:trn></font></b></td></tr>
<tr><td><digi:trn key="um:newPasword">New Password</digi:trn></td><td><html:password name="page_userResetForm" property="newpassword"/></td></tr>
<tr><td><digi:trn key="um:confirmPassword">Confirm Password</digi:trn></td><td><html:password name="page_userResetForm" property="confirmpassword"/></td></tr>

<TR>
<TD colspan=2>
<html:submit property="submit" value="Submit"/>
</TD>
</TR>
</FORM>
</TABLE>