<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/c.tld" prefix="c" %>

<digi:instance property="userEmailForm" />

<table cellpadding="10" cellspacing="0" border="0">
<tr>
    <td align="left" valign="top" class="PageTitle">
	Reset password (Repository)
    </td>
</tr>
<tr>
    <td align="left" valign="top" class="text">
        An e-mail has been sent to<br><b><c:out value="${userEmailForm.email}" /><b>
    </td>
</tr>
<tr>	
    <td align="left" valign="top" class="text">
        Click on the link included in the e-mail to create a new password.
    </td>
</tr>
</table>
