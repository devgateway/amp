<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="userAccountForm" />

<div align="left">

<table width="540" border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111">
<tr valign="top"><td><IMG height=20 alt="" src="images/trans.gif" width=1 border="0"></td></tr>
<tr>
	<td colspan=2 class="title">
		<c:out value="${userAccountForm.firstName}" />&nbsp;<c:out value="${userAccountForm.lastName}" />,&nbsp;<digi:trn key="um:welcomeToDefault">welcome to Default site</digi:trn>
	</td>
</tr>
<tr><td><img height="10" alt="" src="images/trans.gif" width="1" border="0"></td></tr>
<tr> 
    <td>
        <digi:context name="digiContext" property="context" />
        <a href="<%= digiContext %>/showLayout.do"><digi:trn key="um:returnToHomePage">Return to the home page</digi:trn></a>
    </td>
</tr>
</table>