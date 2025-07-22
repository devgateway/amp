<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<html:javascript formName="aimUserRegisterForm"/>
<digi:form action="/registerUser.do" method="post" onsubmit="return validateAimUserRegisterForm(this);">

<table width="100%" valign="top" align="left" cellpadding="0" cellspacing="0" border="0">
<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width=14>&nbsp;
		</td>
		<td align=left valign="top" width=1000><br>
			<table border="0" cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td align=left class=title noWrap colspan="2">
						<b>
						<digi:trn key="aim:newUserRegistrationSuccess">Congratulations, your AMP registration was successful. The next step is to contact your AMP Software Administrator so that you can be assigned to a workspace.</digi:trn>
						</b>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>




