<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<%@ taglib uri="http://digijava.org/GlobalSettings" prefix="globalsettings" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<digi:instance property="aimChangePasswordForm" />

<html:javascript formName="aimChangePasswordForm"/>

<digi:form action="/changePassword.do" method="post" onsubmit="return validateAimChangePasswordForm(this);">

<table width="100%" valign="top" align="left" cellpadding="0" cellspacing="0" border="0">
<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff border="0" cellpadding="0" cellspacing="0" width=1000>
	<tr>
		<td align=left valign="top" width="100%"><br>
			<table border="0" cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td colspan="2">
						<digi:errors />					</td>				
				</tr>			
				<tr>
					<td>
						<digi:trn key="aim:changePassword">
						Change Password
						</digi:trn>					</td>				
				</tr>			
				<tr>
					<td>
						<b>
						<digi:trn key="aim:changePasswordSuccess">
						Successfully changed password
						</digi:trn>
						</b><br><br>
												<digi:img src="/WEB-INF/repository/aim/images/i-C2160E.gif" width="13" height="9"/>
						<digi:trn key="aim:loginWarning">
						 You are signing-in to one or more secure applications for        
        			    official business. You have been granted the right to access these        
          		 	 applications and the information contained in them to facilitate        
           			 your official business. Your accounts and passwords are your        
						 responsibility. Do not share them with anyone.        
						 </digi:trn>
						<BR><BR>
											</td>				
				</tr>

			</table>
		</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>



