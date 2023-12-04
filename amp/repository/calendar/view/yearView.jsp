
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:form action="/viewYearEvents.do?go=true" method="post">
	<table border="0" cellpadding="0" cellspacing="5" width="100%">
		<tr>
			<digi:secure authenticated="true">
			<td width="50%" align="left" valign="top">
				<digi:link href="/viewYearEvents.do?status=mall">
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
				<digi:link href="/viewYearEvents.do?status=mpe">
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
							<td nowrap height="40" class="bgblue" align="center">
								<digi:link href="/viewMonthEvents.do">
								<span class="txtwhiteboldu">
								<digi:trn key="calendar:monthView">Month View</digi:trn></span></digi:link>
							</td>
						</tr>
					</table>
				</div>
			</td>
			<td width="25%" height="44">
				<table border="1" cellpadding="0" cellspacing="0" width="95%" height="40" bordercolor="steelblue">
					<tr>
						<td nowrap height="40" class="bgsteelblue" align="center">
							<span class="txtwhitebold">
							<digi:trn key="calendar:yearView">Year View</digi:trn></span>
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
	<table>
		<tr>
			<td class="bold">
				<bean:write name="calendarItemForm" property="infoText"/>
			</td>
		</tr>
	</table>
	<table border="3" cellpadding="0" cellspacing="0" bordercolor="steelblue" width="100%" height="227">
		<tr>
			<td>
				<table border="0" cellspacing="0" width="100%">
					<tr>
						<td width="30%" valign="top">
							<table>
								<tr>
									<td class="bggray">
										<table border="0" cellspacing="0" width="100%">
											<tr>
												<td align="center">
													<digi:link href="/viewYearEvents.do" paramName="calendarItemForm"  paramId="navYear" paramProperty="prevYear">
													<digi:img border="0" src="module/calendar/images/yarrowl.gif"/></digi:link>
													<span class="bold">
													<bean:write name="calendarItemForm" property="currentYear"/></span>
													<digi:link href="/viewYearEvents.do" paramName="calendarItemForm" paramId="navYear" paramProperty="nextYear">
													<digi:img border="0" src="module/calendar/images/yarrowr.gif"/></digi:link>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<table>
											<logic:iterate id="monthsList" name="calendarItemForm" property="monthsList" type="java.util.List">
											<tr>
												<logic:iterate id="rowMonths" name="monthsList" type="org.digijava.module.um.util.Calendar">
												<td align="left">
													<logic:equal name="rowMonths" property="today" value="true">
													<span class="bold">
													<bean:write name="rowMonths" property="calendarText"/></span></logic:equal>
													<logic:equal name="rowMonths" property="today" value="false">
													<digi:link href="/viewYearEvents.do" paramName="rowMonths" paramId="activeMonth" paramProperty="calendarId">
													<bean:write name="rowMonths" property="calendarText"/></digi:link></logic:equal>
												</td></logic:iterate>
											</tr></logic:iterate>
										</table>
									</td>
								</tr>
								<tr>
									<td noWrap align="center">
										<table>
											<tr>
												<td noWrap align="center">
													<html:text name="calendarItemForm" property="jumpToDate" size="15"/>
													<html:submit value="Go"/><br>(
													<digi:trn key="calendar:dateFormat">YYYY-MM-DD Format</digi:trn>)
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td width="100%" colspan="3" align="center" class="bggray2">
										<digi:link href="/viewYearEvents.do?td=td">
										<digi:trn key="calendar:todayIs">Today is</digi:trn></digi:link>
										<digi:date name="calendarItemForm" property="todayDate" formatKey="param:dateFormat" />
									</td>
								</tr>
							</table>
						</td>
						<td width="70%" valihn="top">
							<logic:present name="calendarItemForm" property="eventsList">
							<table border="0" cellpadding="0" cellspacing="0" width="100%">
								<logic:iterate id="eventsList" name="calendarItemForm" property="eventsList" type="org.digijava.module.calendar.form.CalendarItemForm.Events">
								<tr>
									<td width="100%">
										<span class="bold">
										<bean:write name="eventsList" property="day"/></span>
										<HR>
									</td>
								</tr>
								<logic:iterate id="events" name="eventsList" property="events" type="org.digijava.module.calendar.form.CalendarItemForm.EventInfo">
								<tr>
									<td width="100%" bgcolor="whitesmoke">
									<table width="100%">
									  <tr><td align="left" valign="bottom">
										<logic:present name="events" property="title">
										<logic:present name="events" property="description">
										<digi:link href="/showCalendarItemDetails.do" paramName="events" paramId="activeCalendarItem" paramProperty="id">
										<bean:define id="title" name="events" property="title" type="java.lang.String"/>
										<%=title%></digi:link></logic:present>
										<logic:notPresent name="events" property="description"><a href='<bean:write name="events" property="sourceUrl"/>' class="text">
										<bean:define id="title" name="events" property="title" type="java.lang.String"/>
										<%=title%></a></logic:notPresent></logic:present></td></tr>
									    <tr><td align="left" valign="top">
										<span class="italic">
										<bean:write name="events" property="startDate"/>-
										<bean:write name="events" property="endDate"/></span></td></tr>
									</table>
									</td>
								</tr>
								<tr>
									<td>	&nbsp;
									</td>
								</tr></logic:iterate></logic:iterate>
                        		<tr>
                        		<table width="100%">
                        		<tr>
                    			<td align="right" valign="top">
                     				<logic:present name="calendarItemForm" property="prev">
                     				<digi:link href="/viewYearEvents.do" paramName="calendarItemForm" paramId="nav" paramProperty="prev"><small><font size="1">&lt;&lt;</font></small><small>
                  				    <digi:trn key="news:prev">Prev</digi:trn></small></digi:link></logic:present>
                    			</td>
                    			<td align="left" valign="top">
                    				<logic:present name="calendarItemForm" property="next">
                    				<digi:link href="/viewYearEvents.do" paramName="calendarItemForm" paramId="nav" paramProperty="next">
                  				<small><digi:trn key="news:next">Next</digi:trn></small><small><font size="1">&gt;&gt;</font></small></digi:link></logic:present>
                   			   </td>
                   			   </tr>
                   			   </table>
                      		</tr>
							</table></logic:present>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</digi:form>
