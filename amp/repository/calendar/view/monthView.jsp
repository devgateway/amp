
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:form action="/viewMonthEvents.do" method="post">
	<table border="0" cellpadding="0" cellspacing="5" width="100%">
		<tr>
			<digi:secure authenticated="true">
			<td width="50%" align="left" valign="top">
				<digi:link href="/viewMonthEvents.do?status=mall">
				<digi:trn key="calendar:viewMyEvents">View my events</digi:trn></digi:link>
			</td></digi:secure>
			<digi:secure actions="ADMIN">
			<td width="50%" align="left" valign="top">
				<digi:link href="/showCalendarItems.do?status=pe">
				<digi:trn key="calendar:administer">Administer</digi:trn></digi:link>
			</td></digi:secure>
		</tr>
		<tr>
			<digi:secure authenticated="true">
			<td width="50%" align="left" valign="top">
				<digi:link href="/viewMonthEvents.do?status=mpe">
				<digi:trn key="calendar:viewMyPendingEvents">View my pending events</digi:trn></digi:link>
			</td></digi:secure>
			<digi:secure actions="ADMIN">
			<td width="50%">
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
							<td nowrap height="40" class="bgsteelblue" align="center">
								<span class="txtwhitebold">
								<digi:trn key="calendar:monthView">Month View</digi:trn></span>
							</td>
						</tr>
					</table>
				</div>
			</td>
			<td width="25%" height="44">
				<table border="1" cellpadding="0" cellspacing="0" width="95%" height="40" bordercolor="steelblue">
					<tr>
						<td nowrap height="40" class="bgblue" align="center">
							<digi:link href="/viewYearEvents.do">
							<span class="txtwhiteboldu">
							<digi:trn key="calendar:yearView">Year View</digi:trn></span></digi:link>
						</td>
					</tr>
				</table>
			</td>
			<td width="25%" height="44">
				<div align="left">
					<table border="1" cellpadding="0" cellspacing="0" width="95%" height="40" bordercolor="steelblue">
						<tr>
							<td nowrap height="40" class="bgblue" align="center">
								<digi:link href="/viewListEvents.do">
								<span class="txtwhiteboldu">
								<digi:trn key="calendar:listView">List View</digi:trn></span></digi:link>
							</td>
						</tr>
					</table>
				</div>
			</td>
			<td width="25%" height="44">
				<div align="left">
					<table border="1" cellpadding="0" cellspacing="0" width="95%" height="43" bordercolor="steelblue">
						<tr>
							<td nowrap height="40" class="bgblue" align="center">
								<digi:link href="/showCreateCalendarItem.do">
								<span class="txtwhiteboldu">
								<digi:trn key="calendar:addEvent">Add Event</digi:trn></span></digi:link>
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
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td width="34%" height="39">
							<table border="0" cellpadding="0" cellspacing="0" width="80%">
								<tr>
									<td width="10%" height="38">
										<digi:link href="/viewMonthEvents.do" paramName="calendarItemForm" paramId="nav" paramProperty="prev">
										<digi:img border="0" src="ampModule/calendar/images/arrowl.gif"/></digi:link>
									</td>
									<td width="80%" height="38" class="dgtitle" align="center">
										<bean:write name="calendarItemForm" property="infoText"/>
									</td>
									<td width="10%" height="38" align="right">
										<digi:link href="/viewMonthEvents.do" paramName="calendarItemForm" paramId="nav" paramProperty="next">
										<digi:img border="0" src="ampModule/calendar/images/arrowr.gif"/></digi:link>
									</td>
								</tr>
							</table>
						</td>
						<td width="80%" height="39" align="right">
							<table border="0" cellpadding="0" cellspacing="0" width="80%">
								<tr>
									<td nowrap width="20%" align="center">
										<digi:trn key="calendar:jumpTo">Jump to</digi:trn>
									</td>
									<td nowrap width="43%" align="left">
										<digi:trn key="calendar:month">Month:</digi:trn>
										<html:select property="jumpToMonth">
										<bean:define id="mid" name="calendarItemForm" property="months" type="java.util.Collection"/>
										<html:options collection="mid" property="calendarId" labelProperty="calendarText"/></html:select>
									</td>
									<td nowrap width="27%" align="left">
										<digi:trn key="calendar:year">Year:</digi:trn>
										<html:select property="jumpToYear">
										<bean:define id="yid" name="calendarItemForm" property="years" type="java.util.Collection"/>
										<html:options collection="yid" property="calendarId" labelProperty="calendarText"/></html:select>
									</td>
									<td width="10%" align="left">
										<html:submit value="Go"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td width="100%" height="266" align="center" valign="top">
				<div align="left">
					<table border="0" cellspacing="1" width="100%">
						<tr>
							<td width="14%" class="bgsteelblue" height="20">
								<span class="txtwhite">Sun</span>
							</td>
							<td width="14%" class="bgsteelblue" height="20">
								<span class="txtwhite">Mon</span>
							</td>
							<td width="14%" class="bgsteelblue" height="20">
								<span class="txtwhite">The</span>
							</td>
							<td width="14%" class="bgsteelblue" height="20">
								<span class="txtwhite">Wed</span>
							</td>
							<td width="14%" class="bgsteelblue" height="20">
								<span class="txtwhite">Thu</span>
							</td>
							<td width="15%" class="bgsteelblue" height="20">
								<span class="txtwhite">Fri</span>
							</td>
							<td width="15%" class="bgsteelblue" height="20">
								<span class="txtwhite">Sat</span>
							</td>
						</tr>
						<logic:present name="calendarItemForm" property="eventsList">
						<logic:iterate id="eventsList" name="calendarItemForm" property="eventsList" type="java.util.List">
						<tr>
							<logic:iterate id="weekEvents" name="eventsList" type="org.digijava.ampModule.calendar.form.CalendarItemForm.Events">
							<logic:equal name="weekEvents" property="today" value="false">
							<logic:equal name="weekEvents" property="monthDay" value="false">
							<td width="14%" nowrap height="50" valign="top"></logic:equal>
								<logic:equal name="weekEvents" property="monthDay" value="true">
								<td width="14%" nowrap height="50" class="bgpurple3" valign="top"></logic:equal></logic:equal>
									<logic:equal name="weekEvents" property="today" value="true">
									<td width="14%" nowrap height="50" class="bglightyellow" valign="top"></logic:equal>
										<span class="underline">
										<bean:write name="weekEvents" property="day"/></span>
										<logic:present name="weekEvents" property="events">
										<table width="100%">
											<logic:iterate id="events" name="weekEvents" property="events" type="org.digijava.ampModule.calendar.form.CalendarItemForm.EventInfo">
											<tr>
												<td colspan="2" nowrap>
													<logic:present name="events" property="title">
													<logic:present name="events" property="description">
													<digi:link href="/showCalendarItemDetails.do" paramName="events" paramId="activeCalendarItem" paramProperty="id">
													<bean:define id="title" name="events" property="title" type="java.lang.String"/>
													<%=title%></digi:link></logic:present>
													<logic:notPresent name="events" property="description"><a href='<bean:write name="events" property="sourceUrl"/>'>
													<bean:define id="title" name="events" property="title" type="java.lang.String"/>
													<%=title%></a></logic:notPresent></logic:present>
												</td>
											</tr></logic:iterate>
											<logic:equal name="weekEvents" property="more" value="true">
											<tr>
												<td nowrap align="left">
													<span class="bold">...</span>
												</td>
												<td nowrap align="right">
												</td>
											</tr></logic:equal>
										</table></logic:present>
									</td></logic:iterate>
						</tr></logic:iterate></logic:present>
						<tr>
							<td width="14%" class="bgsteelblue" height="1">
								<span class="txtwhite">Sun</span>
							</td>
							<td width="14%" class="bgsteelblue" height="1">
								<span class="txtwhite">Mon</span>
							</td>
							<td width="14%" class="bgsteelblue" height="1">
								<span class="txtwhite">The</span>
							</td>
							<td width="14%" class="bgsteelblue" height="1">
								<span class="txtwhite">Wed</span>
							</td>
							<td width="14%" class="bgsteelblue" height="1">
								<span class="txtwhite">Thu</span>
							</td>
							<td width="15%" class="bgsteelblue" height="1">
								<span class="txtwhite">Fri</span>
							</td>
							<td width="15%" class="bgsteelblue" height="1">
								<span class="txtwhite">Sat</span>
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>
</td>
</tr>
</table>
</digi:form>