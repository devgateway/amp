
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:form action="/updateConfirmCalendarItems.do" method="post"><link href="images/style.css" rel="stylesheet" type="text/css">
<p>
	<digi:trn key="calendar:theFollowingItemsWillBe">The following item(s) will be</digi:trn>&quot;<strong>
	<bean:write name="calendarItemForm" property="statusTitle"/>ed</strong>&quot;</p>
	<logic:present name="calendarItemForm" property="eventsList">
	<table width="278" border="0" cellpadding="5" cellspacing="1">
		<tr bgcolor="#F0F3F7" styleClass="tlt">
			<td width="140"><strong>
				<digi:trn key="calendar:title1">Title</digi:trn></strong>
			</td>
			<td width="219"><strong>
				<digi:trn key="calendar:author1">Author</digi:trn></strong>
			</td>
			<td width="154" nowrap><strong>
				<digi:trn key="calendar:releaseDate1">Release Date</digi:trn></strong>
			</td>
		</tr>
		<logic:iterate id="eventsList" name="calendarItemForm" property="eventsList" type="org.digijava.module.calendar.form.CalendarItemForm.EventInfo">
		<logic:equal name="eventsList" property="selected" value="true">
		<tr valign="top" bgcolor="#F0F3F7">
			<td nowrap width="140">
				<bean:write name="eventsList" property="title"/>&nbsp;
				<html:hidden property="returnUrl"/>
				<digi:link href="/showEditCalendarItem.do" paramName="eventsList" paramId="activeCalendarItem" paramProperty="id" styleClass="tlt">[
				<digi:trn key="calendar:edit">Edit</digi:trn>]</digi:link>
			</td>
			<td nowrap width="219">
				<bean:write name="eventsList" property="authorFirstNames"/>
				<bean:write name="eventsList" property="authorLastName"/>
			</td>
			<td nowrap width="154">
				<bean:write name="eventsList" property="startDate"/>
			</td>
		</tr>
		<tr>
			<td colspan="3" width="535">
				<html:hidden name="calendarItem" property="selected" value="true" indexed="true"/>
			</td>
		</tr></logic:equal></logic:iterate>
		<tr valign="top" bgcolor="#F0F3F7">
			<td nowrap styleClass="tlt" width="140"><b>
				<span class="tlt">
				<bean:write name="calendarItemForm" property="statusTitle"/>
				<digi:trn key="calendar:message">message:</digi:trn></span></b>
			</td>
			<td colspan="2" nowrap width="384">
			<bean:define id="moduleName" name="calendarItemForm" property="moduleName" type="java.lang.String" />
			<bean:define id="instanceName" name="calendarItemForm" property="instanceName" type="java.lang.String" />
			<bean:define id="selectedStatus" name="calendarItemForm" property="selectedStatus" type="java.lang.String" />
			<fieldset title="Alert Emails">
			<legend><digi:trn key="calendar:alertsMailer">User Alerts E-mail</digi:trn></legend>
			 <table bgcolor="#ffffff">
			    <tr><td width="5%" align="right" valign="top"><b>From:</b></td>
			       <td><digi:msg default="noreply@digijava.org" linkAlwaysVisible="true">
			          alerts:newcontent:emailUser
 		          </digi:msg></td><tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Subject:</b></td>
			      <td>
			       <digi:msg default="Your {siteName} content" linkAlwaysVisible="true">
			          <%=moduleName%>:<%=instanceName%>:alert:<%=selectedStatus%>:subject
			       </digi:msg>
			      </td>
			   <tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Message:</b></td>
			      <td>
			       <digi:msg default="{siteName} Title: {title}  URL: {sourceUrl}" linkAlwaysVisible="true">
			           <%=moduleName%>:<%=instanceName%>:alert:<%=selectedStatus%>:body
		           </digi:msg>
			      </td>
			   <tr>
			   
			 </table>
			</fieldset> 
			</td>
		</tr>
		<tr valign="top" bgcolor="#F0F3F7">
			<td nowrap styleClass="tlt" width="140"><b>
				<span class="tlt">
				<digi:trn key="calendar:sendMessage">Send message?</digi:trn></span></b>
			</td>
			<td colspan="2" nowrap width="384">
				<table border="0" cellpadding="2" cellspacing="1" width="112">
					<tr>
						<td width="51">
							<html:radio name="calendarItemForm" property="sendMessage" value="true"><strong style="font-weight: 400">
							<digi:trn key="calendar:yes">Yes</digi:trn></strong></html:radio>
						</td>
						<td width="51">
							<html:radio name="calendarItemForm" property="sendMessage" value="false"><strong style="font-weight: 400">
							<digi:trn key="calendar:no">No</digi:trn></strong></html:radio>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td width="140">
				<html:submit value="Confirm" onclick="this.disabled = true; form.submit()"/>
			</td>
		</tr>
	</table>
	</logic:present>
</digi:form>