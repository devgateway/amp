<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>


<digi:instance property="emailTemplatesForm" />
<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
	<tr class="yellow">
		<td><digi:img src="module/admin/images/yellowLeftTile.gif" border="0" width="20"/></td>
		<td width="100%">
			<font class="sectionTitle">
				<digi:trn key="admin:emailTemplates">Email Templates</digi:trn>
			</font>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="yellow" valign="top" align="left">
<TABLE border="0" >
        <TR>
			<td align="left" valign="top" colspan=2>
				<fieldset>
				<legend><digi:trn key="admin:alertsMailer">Admin Alerts E-mail</digi:trn></legend>
				 <table>
				    <tr>
						<td width="5%" align="right" ><b>From:</b></td>
					    <td><digi:msg default="noreply@digijava.org" linkAlwaysVisible="true">alerts:newcontent:emailAdmin</digi:msg></td>
					<tr>
				    <tr>
				      <td width="5%" align="right"><b>Subject:</b></td>
				      <td><digi:msg default="New content awaiting review {moduleName}" linkAlwaysVisible="true">alerts:newcontent:subject</digi:msg></td>
				    <tr>
				    <tr>
				      <td width="5%" align="right" valign="top"><b>Message:</b></td>
				      <td><digi:msg default="There is new content awaiting review on {siteName} page: Title: {title} Source: {sourceName} URL: {sourceUrl} Description:{description} Submitted to: {siteName} Use the link below to review this content: {link}" linkAlwaysVisible="true">alerts:newcontent:body</digi:msg></td>
				    <tr>
			    </table>			   
				</fieldset> 
			</td>
		</TR>
        <TR>
			<td align="left" valign="top" colspan=2>
				<fieldset>
				<legend><digi:trn key="exception:alertsMailer">Exception Alerts E-mail</digi:trn></legend>
				 <table>
				    <tr>
				      <td width="5%" align="right"><b>Subject:</b></td>
				      <td><digi:msg default="Error {statusCode}, site {siteName}" linkAlwaysVisible="true">exception:subject</digi:msg></td>
				    <tr>
				    <tr>
				      <td width="5%" align="right" valign="top"><b>Message:</b></td>
				      <td><digi:msg default="Some error occured while server was processing page {exceptionMessage},{timeStamp},{siteName},{statusCode},{url},{comment},{stackTrace}" linkAlwaysVisible="true">exception:body</digi:msg></td>
				    <tr>
			    </table>			   
				</fieldset> 
			</td>
		</TR>
        <TR>
			<td align="left" valign="top" colspan=2>
				<fieldset>
				<legend><digi:trn key="admin:cmsAlertsMailer">CMS Admin Alerts E-mail</digi:trn></legend>
				 <table>
				    <tr>
						<td width="5%" align="right" ><b>From:</b></td>
					    <td><digi:msg default="noreply@digijava.org" linkAlwaysVisible="true">alerts:cmsnewcontent:emailAdmin</digi:msg></td>
					<tr>
				    <tr>
				      <td width="5%" align="right"><b>Subject:</b></td>
				      <td><digi:msg default="New content awaiting review {moduleName}" linkAlwaysVisible="true">alerts:cmsnewcontent:subject</digi:msg></td>
				    <tr>
				    <tr>
				      <td width="5%" align="right" valign="top"><b>Message:</b></td>
				      <td><digi:msg default="There is new content awaiting review on {siteName} page: Title: {title} URL: {url} File Name: {fileName} Description:{description} Category: {categoryNames} Submitted to: {siteName} Use the link below to review this content: {link}" linkAlwaysVisible="true">alerts:cmsnewcontent:body</digi:msg></td>
				    <tr>
			    </table>			   
				</fieldset> 
			</td>
		</TR>
         <TR>
		 	<td align="left" valign="top" colspan=2>
				<fieldset>
				<legend><digi:trn key="admin:accountMailer">Account information Email</digi:trn></legend>
				 <table>
				    <tr>
						<td width="5%" align="right" valign="top"><b><digi:trn key="admin:fromMail">From</digi:trn>:</b></td>
						<td><digi:msg default="account-info@digijava.org" linkAlwaysVisible="true">param:email:support</digi:msg></td>
					</tr>
					<!-- Reset Password Alert Email -->
					<tr>
					<td colspan="2" class="yellow" valign="top" align="left">
						<fieldset>
						<legend><digi:trn key="admin:resetPasswordMailer">Reset Password Alert Email</digi:trn></legend>
							 <table>
							    <tr>
							      <td width="5%" align="right"><b>Subject:</b></td>
							      <td><digi:msg default="New password - {siteName}" linkAlwaysVisible="true">alerts:newpassword:subject</digi:msg></td>
							    <tr>
							    <tr>
							      <td width="5%" align="right" valign="top"><b>Message:</b></td>
							      <td><digi:msg default="PASSWORD Someone using the e-mail {email} has asked the {siteName} to reset the password for this account.If the request came from you, click on the link below and create a new password.If you did not send the request, please disregard this e-mail. {link}" linkAlwaysVisible="true">alerts:resetpassword:body</digi:msg></td>
							    <tr>
						    </table>
						</fieldset>			   
					</td>
					</tr>
					<!-- CMS Send Mail -->
					<tr>
					<td colspan="2" class="yellow" valign="top" align="left">
						<fieldset>
						<legend><digi:trn key="admin:cmsSendMail">CMS send mail</digi:trn></legend>
							 <table>
							    <tr>
							      <td width="5%" align="right"><b>Subject:</b></td>
							      <td><digi:msg default="Interesting resource" linkAlwaysVisible="true">alerts:cmsinterestingresource:subject</digi:msg></td>
							    <tr>
							    <tr>
							      <td width="5%" align="right" valign="top"><b>Message:</b></td>
							      <td><digi:msg default="Hi,I found this resource on the {siteName} {link} {name}" linkAlwaysVisible="true">alerts:cmsinterestingresource:message</digi:msg></td>
							    <tr>
						    </table>
						</fieldset>			   
					</td>
					</tr>

				 </table>
				</fieldset> 
			</td>
		</TR>

</TABLE >
		</td>
	</tr>
</table>