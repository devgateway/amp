<meta http-equiv="Content-Language" content="en-us">
<%@ page language="java" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>
<%@ page import="org.digijava.module.um.form.UserResetForm" %>

<digi:instance id="page_userResetForm" property="userResetForm" />
<digi:context name="userResetAction" property="context/module/moduleinstance/userResetPassword.do?action=Reset" />
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