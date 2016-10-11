
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:form action="/updateCalendarItemsSettings.do" method="post"><h6>
 <digi:trn key="calendar:moduleAdminSettings">Module Admin Settings</digi:trn></h6>
	<table border="0" cellpadding="5" cellspacing="1">
		<tr bgcolor="#F0F3F7">
			<td valign="top" class="tlt"><strong>
				<digi:trn key="calendar:options">Options:</digi:trn></strong>
			</td>
			<td align="left" valign="top">	&nbsp;
			</td>
			<td valign="top">	&nbsp;
			</td>
		</tr>
		<tr bgcolor="#F0F3F7">
			<td valign="top" class="tlt">
				<digi:trn key="calendar:moderated">Moderated?</digi:trn>
			</td>
			<td align="left" valign="top">
				<table border="0" cellpadding="2" cellspacing="1">
					<tr>
						<td width="51">
							<html:radio name="calendarAdminForm" property="setting.moderated" value="true"/>
							<digi:trn key="calendar:yes">Yes</digi:trn>
						</td>
						<td width="44">
							<html:radio name="calendarAdminForm" property="setting.moderated" value="false"/>
							<digi:trn key="calendar:no">No</digi:trn>
						</td>
					</tr>
				</table>
			</td>
			<td valign="top">	&nbsp;
			</td>
		</tr>
		<tr bgcolor="#F0F3F7">
			<td valign="top" class="tlt">
				<digi:trn key="calendar:private">Private?</digi:trn>
			</td>
			<td align="left" valign="top">
				<table border="0" cellpadding="2" cellspacing="1">
					<tr>
						<td width="51">
							<html:radio name="calendarAdminForm" property="setting.privateItem" value="true"/>
							<digi:trn key="calendar:yes">Yes</digi:trn>
						</td>
						<td width="44">
							<html:radio name="calendarAdminForm" property="setting.privateItem" value="false"/>
							<digi:trn key="calendar:no">No</digi:trn>
						</td>
					</tr>
				</table>
			</td>
			<td valign="top">	&nbsp;
			</td>
		</tr>
			<bean:define id="moduleName" name="calendarAdminForm" property="moduleName" type="java.lang.String" />
			<bean:define id="instanceName" name="calendarAdminForm" property="instanceName" type="java.lang.String" />
			<bean:define id="approve" name="calendarAdminForm" property="approve" type="java.lang.String" />
			<bean:define id="reject" name="calendarAdminForm" property="reject" type="java.lang.String" />
			<bean:define id="revoke" name="calendarAdminForm" property="revoke" type="java.lang.String" />
		
		<tr bgcolor="#F0F3F7">
			<td valign="top" class="tlt">
				<digi:trn key="calendar:approveMessage">Approve Message:</digi:trn>
			</td>
			<td align="left" valign="top">
			<fieldset title="Alert Emails">
			<legend><digi:trn key="calendar:alertsMailer">User Alerts E-mail</digi:trn></legend>
			 <table bgcolor="#ffffff">
			    <tr><td width="5%" align="right" valign="top"><b>From:</b></td><td><digi:msg default="noreply@digijava.org" linkAlwaysVisible="true">alerts:newcontent:emailUser</digi:msg></td><tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Subject:</b></td>
			      <td>
			       <digi:msg default="Your {siteName} content" linkAlwaysVisible="true">
			          <%=moduleName%>:<%=instanceName%>:alert:<%=approve%>:subject
			       </digi:msg>
			      </td>
			   <tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Message:</b></td>
			      <td>
			       <digi:msg default="Your event item was approved  {siteName} Title: {title} URL: {sourceUrl}" linkAlwaysVisible="true">
			         <%=moduleName%>:<%=instanceName%>:alert:<%=approve%>:body
  		          </digi:msg>
			      </td>
			   <tr>
			 </table>
			</fieldset> 
			</td>
			<td align="left" valign="top"><b>
				<span class="tlt">
				<digi:trn key="calendar:sendApproveMessage">Send approve message?</digi:trn></span></b>
				<table border="0" cellpadding="2" cellspacing="1">
					<tr>
						<td width="51">
							<html:radio name="calendarAdminForm" property="setting.sendApproveMessage" value="true"/>
							<digi:trn key="calendar:yes">Yes</digi:trn>
						</td>
						<td width="44">
							<html:radio name="calendarAdminForm" property="setting.sendApproveMessage" value="false"/>
							<digi:trn key="calendar:no">No</digi:trn><b> </b>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr bgcolor="#F0F3F7">
			<td valign="top" class="tlt">
				<digi:trn key="calendar:rejectMessage">Reject message:</digi:trn>
			</td>
			<td align="left" valign="top">
			<fieldset title="Alert Emails">
			<legend><digi:trn key="calendar:alertsMailer">User Alerts E-mail</digi:trn></legend>
			 <table bgcolor="#ffffff">
			    <tr><td width="5%" align="right" valign="top"><b>From:</b></td>
			    <td>
			       <digi:msg default="noreply@digijava.org" linkAlwaysVisible="true">
			          alerts:newcontent:emailUser
		          </digi:msg></td><tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Subject:</b></td>
			      <td>
			       <digi:msg default="Your {siteName} content" linkAlwaysVisible="true">
			          <%=moduleName%>:<%=instanceName%>:alert:<%=reject%>:subject
		          </digi:msg>
			      </td>
			   <tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Message:</b></td>
			      <td>
			       <digi:msg default="Your event item was rejected  {siteName}  Title: {title}  URL: {sourceUrl}" linkAlwaysVisible="true">
			        <%=moduleName%>:<%=instanceName%>:alert:<%=reject%>:body
 		           </digi:msg>
			      </td>
			   <tr>
			 </table>
			</fieldset> 
			</td>
			<td align="left" valign="top"><b>
				<span class="tlt">
				<digi:trn key="calendar:sendRejectMessage">Send reject message?</digi:trn></span></b>
				<table border="0" cellpadding="2" cellspacing="1">
					<tr>
						<td width="51">
							<html:radio name="calendarAdminForm" property="setting.sendRejectMessage" value="true"/>
							<digi:trn key="calendar:yes">Yes</digi:trn>
						</td>
						<td width="44">
							<html:radio name="calendarAdminForm" property="setting.sendRejectMessage" value="false"/>
							<digi:trn key="calendar:no">No</digi:trn>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr bgcolor="#F0F3F7">
			<td valign="top" bgcolor="#F0F3F7" class="tlt">
				<digi:trn key="calendar:revokeMessage">Revoke message:</digi:trn>
			</td>
			<td align="left" valign="top">
			<fieldset title="Alert Emails">
			<legend><digi:trn key="calendar:alertsMailer">User Alerts E-mail</digi:trn></legend>
			 <table bgcolor="#ffffff">
			    <tr><td width="5%" align="right" valign="top"><b>From:</b></td>
			      <td>
			        <digi:msg default="noreply@digijava.org" linkAlwaysVisible="true">
			          alerts:newcontent:emailUser</digi:msg></td><tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Subject:</b></td>
			      <td>
			       <digi:msg default="Your {siteName} content" linkAlwaysVisible="true">
			          <%=moduleName%>:<%=instanceName%>:alert:<%=revoke%>:subject
			       </digi:msg>
			      </td>
			   <tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Message:</b></td>
			      <td>
			       <digi:msg default="Your event item was revoked {siteName} Title: {title} \n URL: {sourceUrl}" linkAlwaysVisible="true">
			         <%=moduleName%>:<%=instanceName%>:alert:<%=revoke%>:body
 		          </digi:msg>
			      </td>
			   <tr>
			 </table>
			</fieldset> 
			</td>
			<td align="left" valign="top"><b>
				<span class="tlt">
				<digi:trn key="calendar:sendRevokeMessage">Send revoke message?</digi:trn></span></b>
				<table border="0" cellpadding="2" cellspacing="1">
					<tr>
						<td width="51">
							<html:radio name="calendarAdminForm" property="setting.sendRevokeMessage" value="true"/>
							<digi:trn key="calendar:yes">Yes</digi:trn>
						</td>
						<td width="44">
							<html:radio name="calendarAdminForm" property="setting.sendRevokeMessage" value="false"/>
							<digi:trn key="calendar:no">No</digi:trn>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr bgcolor="#F0F3F7">
			<td colspan="3">
				<table>
					<tr bgcolor="#F0F3F7">
						<td align="left" valign="top"><b>
							<span class="tlt">
							<digi:trn key="calendar:defaultView">Default View:</digi:trn></span></b>
						</td>
						<td>
							<html:select name="calendarAdminForm" property="selectedDefaultView">
							<bean:define id="defaultView" name="calendarAdminForm" property="defaultView" type="java.util.Collection"/>
							<html:options collection="defaultView" property="code" labelProperty="name"/></html:select>
						</td>
					</tr>
					<tr bgcolor="#F0F3F7">
						<td align="left" valign="top"><b>
							<span class="tlt">
							<digi:trn key="calendar:numOfNewItemsInTeaser">Number of new items in teaser:</digi:trn></span></b>
						</td>
						<td>
							<html:select name="calendarAdminForm" property="selectedNumOfItemsInTeaser">
							<bean:define id="numberItems" name="calendarAdminForm" property="numberOfTeasers" type="java.util.Collection"/>
							<html:options collection="numberItems" property="value" labelProperty="value"/></html:select>
						</td>
					</tr>
					<tr bgcolor="#F0F3F7">
						<td align="left" valign="top"><b>
							<span class="tlt">
							<digi:trn key="calendar:numOfCharsInItemTitle">Number of characters in item title:</digi:trn></span></b>
						</td>
						<td>
							<html:text name="calendarAdminForm" property="numOfCharsInTitle" size="7"/>
						</td>
					</tr>
					<tr bgcolor="#F0F3F7">
						<td align="left" valign="top"><b>
							<span class="tlt">
							<digi:trn key="calendar:numOfItemsPerPage">Number of items per page:</digi:trn></span></b>
						</td>
						<td>
							<html:text name="calendarAdminForm" property="numOfItemsPerPage" size="7"/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr bgcolor="#F0F3F7">
			<td colspan="3" valign="top" align="left">
				<html:submit value="Save Options"/>
			</td>
		</tr>
	</table>
</digi:form>