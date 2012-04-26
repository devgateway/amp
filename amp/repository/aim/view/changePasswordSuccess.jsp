<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/globalsettings" prefix="globalsettings" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimChangePasswordForm" />

<html:javascript formName="aimChangePasswordForm"/>

<digi:form action="/changePassword.do" method="post" onsubmit="return validateAimChangePasswordForm(this);">

<table width="100%" valign="top" align="left" cellpadding="0" cellspacing="0" border="0">
<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff border="0" cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td  width="5%">&nbsp;
		</td>
		<td align=left valign="top" width="60%"><br>
			<table border="0" cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td width="3%">&nbsp;</td>				
					<td vAlign=left>
						<digi:errors />
					</td>
					<td>&nbsp;
					</td>
				</tr>			
				<tr>
					<td width="3%">&nbsp;</td>				
					<td class=subtitle-blue vAlign=left>
						<digi:trn key="aim:changePassword">
						Change Password
						</digi:trn>
					</td>
				</tr>			
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align="center" class=f-names noWrap width="40%">
						<b>
						<digi:trn key="aim:changePasswordSuccess">
						Successfully changed password
						</digi:trn>
						</b>
					</td>
				</tr>

			</table>
		</td>
		<td  width="5%">&nbsp;
		</td>
		<td bgColor=#f7f7f4 class=r-dotted-lg valign="top" width="5%">
	      <table align="center" border="0" cellPadding=3 cellspacing="0" width="90%">
      		 <tr>
		        <td class=r-dotted-lg-buttom valign="top"><br/>
      	     	</td>
        		</tr>
        		<tr>
		          <td valign="top">&nbsp;</td>
        		</tr>
        		<tr>
	          	<td class=r-dotted-lg-buttom valign="top">
						<digi:img src="module/aim/images/i-C2160E.gif" width="13" height="9"/>
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
        		<tr>
          		<td valign="top">&nbsp;</td>
  				</tr>
	      </table>
		</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>



