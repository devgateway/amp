<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

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
