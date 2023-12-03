
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script>function fnOnReject() {
      <digi:context name="confirmRejectEvent" property="context/ampModule/moduleinstance/confirmCalendarItems.do" />
      document.calendarItemForm.action = "<%= confirmRejectEvent%>";
      document.calendarItemForm.submit();
  }
 function fnOnAdmin() {
      <digi:context name="administerEvents" property="context/ampModule/moduleinstance/showCalendarItems.do?status=pe" />
      document.calendarItemForm.action = "<%= administerEvents%>";
      document.calendarItemForm.submit();
  }
 function fnOnSettings() {
      <digi:context name="showEventsSettings" property="context/ampModule/moduleinstance/showCalendarItemsSettings.do" />
      document.calendarItemForm.action = "<%= showEventsSettings%>";
      document.calendarItemForm.submit();
  }
</script>
<digi:errors/>
<digi:form action="/showEditCalendarItem.do" method="post"><link href="images/style.css" rel="stylesheet" type="text/css">
<p>
	<digi:link href="/viewEvents.do" styleClass="tlt">
	<digi:trn key="calendar:viewAll">View All</digi:trn></digi:link><br></p>
	<table border="0">
		<tr>
			<logic:present name="calendarItemForm" property="title">
			<td class="tlt"><strong><font size="5">
				<logic:present name="calendarItemForm" property="title">
				<bean:write name="calendarItemForm" property="title" filter="false" />
				</logic:present></font></strong>
			</td></logic:present>
		</tr>
		<tr>
			<td>
				<HR>
			</td>
		</tr>
		<tr>
			<td class="tlt"><b>
				<digi:trn key="calendar:dates">Dates:</digi:trn>
				<bean:write name="calendarItemForm" property="startDate"/>
				<logic:equal name="calendarItemForm" property="allStartDayEvent" value="true">
				<digi:trn key="calendar:allDayEvent">All day event</digi:trn></logic:equal>-
				<bean:write name="calendarItemForm" property="endDate"/>
				<logic:equal name="calendarItemForm" property="allEndDayEvent" value="true">
				<digi:trn key="calendar:allDayEvent">All day event</digi:trn></logic:equal></b>
			</td>
		</tr>
		<tr>
			<td>
				<logic:present name="calendarItemForm" property="description">
				<bean:write name="calendarItemForm" property="description" filter="false" />
				</logic:present>
			</td>
		</tr>
		<tr>
			<td class="tlt"><b>
				<digi:trn key="calendar:location">Location:</digi:trn></b>
				<bean:write name="calendarItemForm" property="location"/>
			</td>
		</tr>
		<tr>
			<td class="tlt"><b>
				<digi:trn key="calendar:country">Country:</digi:trn>
				<logic:present name="calendarItemForm" property="country">
				<bean:define id="countryKey" name="calendarItemForm" property="countryKey" type="java.lang.String"/>
				<digi:trn key="<%=countryKey%>"><%=countryKey%></digi:trn></logic:present>
			</td>
		</tr>
		<tr>
			<td class="tlt"><b>
				<digi:trn key="calendar:source">Source:</digi:trn></b>&nbsp;<a href='<bean:write name="calendarItemForm" property="sourceUrl" />'>
				<bean:write name="calendarItemForm" property="sourceName"/></a>
			</td>
		</tr>
		<tr>
			<td>
				<digi:secure authenticated="true">
				<logic:equal name="calendarItemForm" property="editable" value="true">
				<html:hidden property="activeCalendarItem"/>
				<html:hidden property="returnUrl"/>
				<html:submit value="Edit"/></logic:equal></digi:secure>
				<digi:secure actions="ADMIN">
				<html:hidden property="activeCalendarItem"/>
				<html:submit value="Reject" onclick="javascript:fnOnReject()"/>
				<html:hidden property="activeCalendarItem"/>
				<html:submit value="Admin" onclick="javascript:fnOnAdmin()"/>
				<html:submit value="Settings" onclick="javascript:fnOnSettings()"/></digi:secure>
			</td>
		</tr>
	</table>
</digi:form>