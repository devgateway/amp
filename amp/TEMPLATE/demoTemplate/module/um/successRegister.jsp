<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>

<digi:instance property="userAccountForm" />

<div align="left">

<table width="540" border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111">
<tr valign="top"><td><IMG height=20 alt="" src="images/trans.gif" width=1 border="0"></td></tr>
<tr>
	<td colspan=2 class="title">
		<bean:write name="userAccountForm" property="firstName" />&nbsp;<bean:write name="userAccountForm" property="lastName" />,&nbsp;<digi:trn key="um:welcomeToDefault">welcome to Default site</digi:trn>
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