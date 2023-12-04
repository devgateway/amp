
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:instance property="calendarItemForm"/>
	<table border="0" cellpadding="0" cellspacing="5" width="100%" height="48">
		<tr>
			<digi:secure authenticated="true">
			<td width="50%" align="left" valign="top" height="19">
				<digi:link href="/viewListEvents.do?status=mall">
				<digi:trn key="calendar:viewMyEvents">View my events</digi:trn></digi:link>
			</td></digi:secure>
			<digi:secure actions="ADMIN">
			<td width="50%" align="left" valign="top" height="19">
				<digi:link href="/showCalendarItems.do?status=pe">
				<digi:trn key="calendar:administer">Administer</digi:trn></digi:link>
			</td></digi:secure>
		</tr>
		<tr>
			<digi:secure authenticated="true">
			<td width="50%" align="left" valign="top" height="19">
				<digi:link href="/viewListEvents.do?status=mpe">
				<digi:trn key="calendar:viewMyPendingEvents">View my pending events</digi:trn></digi:link>
			</td></digi:secure>
			<digi:secure actions="ADMIN">
			<td width="50%" height="19">
				<digi:link href="/showCalendarItemsSettings.do">
				<digi:trn key="calendar:settings">Settings</digi:trn></digi:link>
			</td></digi:secure>
		</tr>
	</table>
	<table border="0" cellpadding="0" cellspacing="0" width="80%" height="44">
		<tr>
			<td nowrap width="25%" height="44">
				<div align="left">
					<table border="1" cellpadding="0" cellspacing="0" width="95%" height="40" bordercolor="steelblue">
						<tr>
							<td nowrap height="40" bgcolor="#006699">
								<p align="center">
								<digi:link href="/viewMonthEvents.do"><b><font color="#FFFFFF">
								<digi:trn key="calendar:monthView">Month View</digi:trn></font></b></digi:link>
							</td>
						</tr>
					</table>
				</div>
			</td>
			<td width="25%" height="44">
				<table border="1" cellpadding="0" cellspacing="0" width="95%" height="40" bordercolor="steelblue">
					<tr>
						<td nowrap height="40" bgcolor="#006699">
							<p align="center">
							<digi:link href="/viewYearEvents.do"><b><font color="#FFFFFF">
							<digi:trn key="calendar:yearView">Year View</digi:trn></font></b></digi:link>
						</td>
					</tr>
				</table>
			</td>
			<td width="25%" height="44">
				<div align="left">
					<table border="1" cellpadding="0" cellspacing="0" width="95%" height="40" bordercolor="steelblue">
						<tr>
							<td nowrap height="40" bgcolor="steelblue">
								<p align="center"><b><font color="#FFFFFF">
								<digi:trn key="calendar:listView">List View</digi:trn></font></b>
							</td>
						</tr>
					</table>
				</div>
			</td>
			<td width="25%" height="44">
				<div align="left">
					<table border="1" cellpadding="0" cellspacing="0" width="95%" height="43" bordercolor="steelblue">
						<tr>
							<td nowrap height="40" bgcolor="#006699">
								<p align="center">
								<digi:link href="/showCreateCalendarItem.do"><font color="#FFFFFF"><b>
								<digi:trn key="calendar:addEvent">Add Event</digi:trn></b></font></digi:link>
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>
	<table border="3" cellpadding="0" cellspacing="0" bordercolor="steelblue" width="100%">
		<tr>
			<td width="100%" height="39">
				<table border="0" cellpadding="0" cellspacing="0" width="28%" height="35">
					<tr>
						<td width="9%" height="35">
							<digi:img src="module/calendar/images/arrowl.gif"/>
						</td>
						<td width="83%" height="35" class="tlt">
							<p align="center"><b><font size="4">
							<bean:write name="calendarItemForm" property="infoText"/></font></b>
						</td>
						<td width="8%" height="35">
							<digi:img src="module/calendar/images/arrowr.gif"/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td width="100%" height="226" align="center" valign="top">	&nbsp;
				<table border="0" cellpadding="0" cellspacing="0" width="97%" height="209">
					<tr>
						<td width="100%" height="150" valign="top">
							<logic:present name="calendarItemForm" property="eventsList">
							<table border="0" cellpadding="0" cellspacing="0" width="100%">
								<logic:iterate id="eventsList" name="calendarItemForm" property="eventsList" type="org.digijava.module.calendar.form.CalendarItemForm.Events">
								<tr>
									<td width="100%" bgcolor="#CACACA">
										<bean:write name="eventsList" property="day"/>
									</td>
								</tr>
								<logic:iterate id="events" name="eventsList" property="events" type="org.digijava.module.calendar.form.CalendarItemForm.EventInfo">
								<tr>
									<td width="100%" bgcolor="whitesmoke" height="1">
									 <table width="100%">
									  <tr><td valign="bottom" align="left">
										<logic:present name="events" property="title">
										<logic:present name="events" property="description">
										<digi:link href="/showCalendarItemDetails.do" paramName="events" paramId="activeCalendarItem" paramProperty="id">
										<bean:define id="title" name="events" property="title" type="java.lang.String"/>
										<%=title%></digi:link></logic:present>
										<logic:notPresent name="events" property="description"><a href='<bean:write name="events" property="sourceUrl"/>' class="text">
										<bean:define id="title" name="events" property="title" type="java.lang.String"/>
										<%=title%></a></logic:notPresent></logic:present></td></tr>
										<tr><td valign="top" align="left">
										<span class="dta"><i>
										<bean:write name="events" property="startDate"/>-
										<bean:write name="events" property="endDate"/></i></span></td></tr>
									  </table>	
									</td>
								</tr></logic:iterate>
								<tr>
									<td>	&nbsp;
									</td>
								</tr></logic:iterate>
							</table></logic:present>
							<logic:empty name="calendarItemForm" property="eventsList">
							<digi:trn key="calendar:thereAreNoEventsToView">There are no items to be displayed on this page. Please use prev/next links for navigation</digi:trn>
							</logic:empty>
						</td>
					</tr>
					<tr>
						<td width="100%" height="59" align="center" valign="center">
							<table border="0" cellpadding="4" cellspacing="0" width="834">
								<tr>
									<td align="right" width="5%">
										<logic:present name="calendarItemForm" property="prev">
										<digi:link href="/viewListEvents.do" paramName="calendarItemForm" paramId="nav" paramProperty="prev"><small><font size="1">&lt;&lt;</font></small><small>
										<digi:trn key="calendar:prev">Prev</digi:trn></small></digi:link></logic:present>
									</td>
									<td width="5%">
										<logic:present name="calendarItemForm" property="next">
										<digi:link href="/viewListEvents.do" paramName="calendarItemForm" paramId="nav" paramProperty="next"><small>
										<digi:trn key="calendar:next">Next</digi:trn></small><small><font size="1">&gt;&gt;</font></small></digi:link></logic:present>
									</td>
									<td width="731">	&nbsp;
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>