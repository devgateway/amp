<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<% // ----------- AFTER USER LOGIN %>
<c:if test="${!empty org.digijava.kernel.user}">
<table border="0" >
<tr><td nowrap ><digi:link href="/showUserAccount.do" styleClass="title_topic"><c:out value="${org.digijava.kernel.user.name}" /></digi:link></td></tr>
<tr><td><digi:link site="dglogin" contextPath="/ampModule/moduleinstance" href="/logoutAction.do" styleClass="text" ><digi:trn key="um:logOut">Log out</digi:trn></digi:link></td></tr>
</table>
</c:if>
<% // ---------------------------------  %>

<% // ----------- BEFOR USER LOGIN %>
<html:javascript formName="logonForm" />
<c:if test="${ empty org.digijava.kernel.user}">
<div align="center">
<digi:form site="dglogin" contextPath="/ampModule/moduleinstance" action="/logonAction.do" onsubmit="return validateLogonForm(this);">
<table border="0" class="border">
<tr><td nowrap bgcolor="#C0C0C0">
<B>DEMOSITE LOGON</B>
</td></tr>
<tr><td>
<digi:errors/>
</td></tr>
<tr><td>
<TABLE border="0" width="100%" >
<TR>
<TD colspan=2></TD>
</TR>
<TR>
<TH align="right">E<digi:trn key="um:email">mail:</digi:trn></TH>
<TD align="left"><html:text property="username"/></TD>
</TR>
<TR>
<TH align="right"><digi:trn key="um:password">Password:</digi:trn></TH>
<TD align="left"><html:password property="password"/></TD>
</TR>
<TR><TD colspan="2"><digi:link href="/showEmailForm.do"><digi:trn key="um:forgotYourPassword">Forgot your password?</digi:trn></digi:link></TD></TR>
<TR><TD colspan="2" align="center">e.g.: admin/admin  read/read  translator/translator</TD></TR>
<TR><TD colspan="2" align="center"><HR></TD></TR>
<TR>
<TD align="left" ><html:submit value="Enter"/></TD>
<TD align="left" ><html:checkbox property="saveLogin"><digi:trn key="um:saveLogin">Save Login?</digi:trn></html:checkbox></TD>
</TR>
<TR><TD colspan="2" align="center"><HR></TD></TR>
<TR><TD colspan="2"><digi:link href="/showUserRegister.do" styleClass="title"><digi:trn key="um:registerHere">Register Here</digi:trn></digi:link></TD></TR>
</TABLE>
</digi:form>
</td></tr>
</table>
</div>
</c:if>
<% // ---------------------------------  %>