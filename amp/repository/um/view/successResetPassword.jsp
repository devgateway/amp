<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
