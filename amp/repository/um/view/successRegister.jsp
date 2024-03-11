<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/c.tld" prefix="c" %>

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