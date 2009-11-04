<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs"%>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<%@page import="java.util.*"%>
<%@page import="org.digijava.module.calendar.form.CalendarViewForm" %>
<%@page import="org.digijava.module.calendar.util.CalendarUtil"%>

<script type="text/javascript" charset="utf-8">
    var trn_today_button ='<digi:trn>Today</digi:trn>';
	var trn_day_tab ='<digi:trn>Day</digi:trn>';
	var trn_week_tab='<digi:trn>Week</digi:trn>';
	var trn_month_tab='<digi:trn>Month</digi:trn>';
	
//Translations for calendar locale_sv.js file 
//month full names
	
	var trn_month_january ='<digi:trn>January</digi:trn>';
	var trn_month_february ='<digi:trn>February</digi:trn>';
	var trn_month_march ='<digi:trn>March</digi:trn>';
	var trn_month_april ='<digi:trn>April</digi:trn>';
	var trn_month_may ='<digi:trn>May</digi:trn>';
	var trn_month_june ='<digi:trn>June</digi:trn>';
	var trn_month_july ='<digi:trn>July</digi:trn>';
	var trn_month_august ='<digi:trn>August</digi:trn>';
	var trn_month_september ='<digi:trn>September</digi:trn>';
	var trn_month_october ='<digi:trn>October</digi:trn>';
	var trn_month_november ='<digi:trn>November</digi:trn>';
	var trn_month_december ='<digi:trn>December</digi:trn>';
	
//month short names
	var trn_month_jan='<digi:trn>Jan</digi:trn>';
	var trn_month_feb='<digi:trn>Feb</digi:trn>';
	var trn_month_mar='<digi:trn>Mar</digi:trn>';
	var trn_month_apr='<digi:trn>Apr</digi:trn>';
	var trn_month_jun='<digi:trn>Jun</digi:trn>';
	var trn_month_jul='<digi:trn>Jul</digi:trn>';
	var trn_month_aug='<digi:trn>Aug</digi:trn>';
	var trn_month_sep='<digi:trn>Sep</digi:trn>';
	var trn_month_oct='<digi:trn>Oct</digi:trn>';
	var trn_month_nov='<digi:trn>Nov</digi:trn>';
	var trn_month_dec='<digi:trn>Dec</digi:trn>';

//day full names 
	
	var trn_day_sunday='<digi:trn>Sunday</digi:trn>';
	var trn_day_monday='<digi:trn>Monday</digi:trn>';
	var trn_day_tuesday='<digi:trn>Tuesday</digi:trn>';
	var trn_day_wednesday='<digi:trn>Wednesday</digi:trn>';
	var trn_day_thursday='<digi:trn>Thursday</digi:trn>';
	var trn_day_friday='<digi:trn>Friday</digi:trn>';
	var trn_day_saturday='<digi:trn>Saturday</digi:trn>';

<<<<<<< .working
//day shotr names 
	
=======
<table bgcolor="#f4f4f2"  width="100%" cellspacing="" cellpadding="1" height="100%">
	<tr>
    	<td bgcolor="#ffffff">
    		<jsp:include page="viewEventsButtons.jsp" flush="false" />
    	</td>
    </tr>
    <c:if test="${calendarViewForm.view != 'none'}">
    	<tr>
    	<td style="border-top: 1px solid #7B9EBD;border-left: 1px solid #7B9EBD; border-right: 1px solid #7B9EBD">
	      <table border="0" width="100%" height="40px" bgcolor="#f4f4f2" >
	        <tr>
	        	<td style="font-size:12px;font-weight:bold;font-family: Tahoma;">
	        		<table width="100%">
	        			<tr>
	        				<td align="right" nowrap="nowrap" >
	        					<c:if test="${calendarViewForm.view != 'custom'}">
					              <digi:img src="module/calendar/images/calenderLeftArrow1.jpg"/>
					              <a href="#"  onclick="submitFilterForm('${calendarViewForm.view}', '${calendarViewForm.dateNavigator.leftTimestamp}');return(false);"><digi:trn key="aim:last">Last</digi:trn></a>
				          		</c:if>
	        				</td>
	        				<feature:display name="Yearly View" module="Calendar">
	        					<c:if test="${calendarViewForm.view == 'yearly'}">
			        				<td align="center" width="20%" nowrap="nowrap" style="font-size:12px;font-weight:bold;font-family: Tahoma;">
							           ${calendarViewForm.baseDateBreakDown.year}
			        				</td>
		        				</c:if>
	        				</feature:display>
	        				<feature:display name="Monthly View" module="Calendar">
	        					<c:if test="${calendarViewForm.view == 'monthly'}">
		        					<td align="center" width="20%" nowrap="nowrap" style="font-size:12px;font-weight:bold;font-family: Tahoma;">
							        	${calendarViewForm.baseDateBreakDown.dateInLocaleMonth}
			        				</td>
		        				</c:if>
	        				</feature:display>
	        				<feature:display name="Weekly View" module="Calendar">
	        					<c:if test="${calendarViewForm.view == 'weekly'}">
		        					<td align="center" width="30%" nowrap="nowrap" style="font-size:12px;font-weight:bold;font-family: Tahoma;">
		        						${calendarViewForm.startDateBreakDown.dateInLocaleWeek}&nbsp;-&nbsp;
						                ${calendarViewForm.endDateBreakDown.dateInLocaleWeek}
		        					</td>
		        				</c:if>
	        				</feature:display>
	        				<feature:display name="Daily View" module="Calendar">
	        					<c:if test="${calendarViewForm.view == 'daily'}">
		        					<td align="center" width="20%" nowrap="nowrap" style="font-size:12px;font-weight:bold;font-family: Tahoma;">
		        						 ${calendarViewForm.baseDateBreakDown.dateInLocaleDay}
		        					</td>
		        				</c:if>
	        				</feature:display>
	        				<%-- 
	        					<c:if test="${calendarViewForm.view == 'custom'}">
	        					<td align="center" width="30%" nowrap="nowrap">
	        						<digi:trn key="aim:calendar:startmonthNameLong:${calendarViewForm.startDateBreakDown.monthNameLong}">${calendarViewForm.startDateBreakDown.monthNameLong}</digi:trn>
					                ${calendarViewForm.startDateBreakDown.dayOfMonth},&nbsp;
					                ${calendarViewForm.startDateBreakDown.year}&nbsp;-&nbsp;
					                <digi:trn key="aim:calendar:endmonthNameLong:${calendarViewForm.endDateBreakDown.monthNameLong}">${calendarViewForm.endDateBreakDown.monthNameLong}</digi:trn>
					                ${calendarViewForm.endDateBreakDown.dayOfMonth},&nbsp;
					                ${calendarViewForm.endDateBreakDown.year}
	        					</td>
	        				</c:if>
	        				--%>	        				
	        				<td align="left" nowrap="nowrap">
	        					<c:if test="${calendarViewForm.view != 'custom'}">
	              					<a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${calendarViewForm.dateNavigator.rightTimestamp}');return(false);"><digi:trn key="aim:next">Next</digi:trn></a>	<digi:img src="module/calendar/images/calenderRightArrow1.jpg"/>
	          					</c:if>
	        				</td>
	        			</tr>
	        		</table>
	        	</td>
	        </tr>
	      </table>
	    </td>
	  </tr>
	  <tr>
	  	<td width="95%" align="center" style="border-bottom: 1px solid #7B9EBD;border-left: 1px solid #7B9EBD; border-right: 1px solid #7B9EBD">
	  		<table width="100%">
	  			<tr>
				    <td align="center" vAlign="middle" width="100%">
				    	<table cellpadding="0" cellspacing="0" align="center" style="width:100%">
				        	<c:if test="${calendarViewForm.view != 'custom'}">
				        		<!-- Monthly view start -->
				        		<feature:display name="Monthly View" module="Calendar">
				        			<c:if test="${calendarViewForm.view == 'monthly'}">
				            		<tr align="center" vAlign="middle">
				                		<td width="100%">
				                  			<table width="99%" border="0" align="center" cellspacing="0">
							                    <tr>
							                    	<td align="left" valign="top" bgcolor="#376091" style="font-size:12px;color:White;font-family:Tahoma;"><digi:trn key="aim:mon">Mon</digi:trn></td>
							                    	<td width="1px" bgcolor="#7B9EBD"/>
							                      	<td align="left" valign="top" bgcolor="#376091" style="font-size:12px;color:White;font-family:Tahoma;"><digi:trn key="aim:tue">Tue</digi:trn></td>
							                      	<td width="1px" bgcolor="#7B9EBD"/>
							                      	<td align="left" valign="top" bgcolor="#376091" style="font-size:12px;color:White;font-family:Tahoma;"><digi:trn key="aim:wed">Wed</digi:trn></td>
							                      	<td width="1px" bgcolor="#7B9EBD"/>
							                      	<td align="left" valign="top" bgcolor="#376091" style="font-size:12px;color:White;font-family:Tahoma;"><digi:trn key="aim:thu">Thu</digi:trn></td>
							                      	<td width="1px" bgcolor="#7B9EBD"/>
							                      	<td align="left" valign="top" bgcolor="#376091" style="font-size:12px;color:White;font-family:Tahoma;"><digi:trn key="aim:fr">Fri</digi:trn></td>
							                      	<td width="1px" bgcolor="#7B9EBD"/>
							                      	<td align="left" valign="top" bgcolor="#376091" style="font-size:12px;color:White;font-family:Tahoma;"><digi:trn key="aim:sat">Sat</digi:trn></td>
							                      	<td width="1px" bgcolor="#7B9EBD"/>
							                      	<td align="left" valign="top" bgcolor="#376091" style="font-size:12px;color:White;font-family:Tahoma;"><digi:trn key="aim:sun">Sun</digi:trn></td>
							                      	<td width="1px" bgcolor="#7B9EBD"/>
							                    </tr>
							                    <tr height="4px" bgcolor="#e8eef7">
				                        				<td colspan="14" />
				                      			</tr>
				                   				<c:forEach var="row" items="${calendarViewForm.dateNavigator.items}">
				                   					<!-- In this row,if in monthly view other months' dates are shown,they should be of 'inactive' color-->
							                    	<tr vAlign="middle" bgcolor="#ffffff">
							                        	<c:forEach var="item" items="${row}" >
							                          		<td vAlign="top" style="padding-right: 0px;padding-left:0px; border-right: 0px;border-left: 0px;">
									                          	<c:if test="${!item.enabled}">
									                          		<span style="color:#cbcbcb">
									                          			${item.dayOfMonth}
									                          		</span>
									                          	</c:if>
									                          	<c:if test="${item.enabled}">
									                          		<span>${item.dayOfMonth}</span>
									                          	</c:if>
							                          		</td>
							                          		<td width="1px" bgcolor="#e8eef7"></td>
								                       </c:forEach>
								                    </tr>
				                      				<c:forEach var="ampCalendarGraph" items="${calendarViewForm.ampCalendarGraphs}">
				                        				<c:set var="startDay">${ampCalendarGraph.ampCalendar.calendarPK.startDay}</c:set>
				                        				<c:set var="endDay">${ampCalendarGraph.ampCalendar.calendarPK.endDay}</c:set>
				                        				<c:set var="endMonth">${ampCalendarGraph.ampCalendar.calendarPK.endMonth+1}</c:set>
				                 						<c:set var="startMonth">${ampCalendarGraph.ampCalendar.calendarPK.startMonth+1}</c:set>
				                        				<c:set var="currentMonth">${calendarViewForm.baseDateBreakDown.month}</c:set>
				                        				<c:set var="currentYear">${calendarViewForm.baseDateBreakDown.year}</c:set>
				                        				<c:set var="iterationBeginIndex">
												         <c:if test="${(fn:length(ampCalendarGraph.graphItems)-1) <0}"> 0</c:if>
												         <c:if test="${(fn:length(ampCalendarGraph.graphItems)-1) >=0}"> ${fn:length(ampCalendarGraph.graphItems)-1}</c:if>
												        </c:set>
												        <c:set var="eventName">
												        	<c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}" begin="${fn:length(ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem)-1}">
												        		${fn:replace(ampCalendarEventItem.title, "'", "\\'")}
												        	</c:forEach>
												        </c:set>
												        <!-- These fields are used in mouse Over function -->
				                        				<c:set var="startYear">${ampCalendarGraph.ampCalendar.calendarPK.startYear}</c:set>
				                        				<c:set var="endYear">${ampCalendarGraph.ampCalendar.calendarPK.endYear}</c:set>
				                        				<c:set var="startHours">${ampCalendarGraph.ampCalendar.calendarPK.startHour}</c:set>
				                                        <c:set var="endHours">${ampCalendarGraph.ampCalendar.calendarPK.endHour}</c:set>
				                                        <c:set var="startMinute">${ampCalendarGraph.ampCalendar.calendarPK.startMinute}</c:set>
				                                        <c:set var="endMinute">${ampCalendarGraph.ampCalendar.calendarPK.endMinute}</c:set>
				                        				<c:set var="eventStartDate">
										                   ${startDay}/${startMonth}/${startYear} ${startHours}:<c:if test="${startMinute<10}">0</c:if>${startMinute}
										                </c:set>
										                <c:set var="eventEndDate">
										                 ${endDay}/${endMonth}/${endYear} ${endHours}:<c:if test="${endMinute<10}">0</c:if>${endMinute}
										                </c:set>
										                <tr height="2px" bgcolor="#ffffff">
					                        				<td colspan="14" />
					                      				</tr>
				                        				<tr vAlign="middle" bgcolor="#ffffff">
				                          					<c:forEach var="item" items="${row}" varStatus="stat">
				                            					<td id="td1" valign="top" vAlign="top" width="14%" style="padding:0px; border-right: 0px;border-left: 0px;height: 100%">
				                              						<!-- Stars Month= Current Month -->
				                              						<script type="text/javascript">
				                              							var msg = stDate+":${eventStartDate}<br>"+endDate+":${eventEndDate}";				                              							
				                              						</script>
				                              						<c:if test="${startMonth==currentMonth && startYear== currentYear && startYear == endYear}">
				                              							<c:if test="${(endMonth==currentMonth && item.dayOfMonth >=startDay && item.dayOfMonth<=endDay && item.enabled) || (endMonth!=currentMonth && ((item.dayOfMonth>=startDay && item.enabled)||(item.dayOfMonth<endDay && !item.enabled)))}">
					                                						<div id="div1" style="border:1px solid ${ampCalendarGraph.ampCalendar.eventType.color};background-color:${ampCalendarGraph.ampCalendar.eventType.color};" onmouseover="stm(['${eventName}','stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])"  onmouseout="htm()" >
					                                                        	<c:if test="${item.dayOfMonth==startDay && item.enabled}">
					                                                        		<digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~method=preview~resetForm=true">
								                                                       	<digi:img src="module/calendar/images/lookglass2.gif" border="0" alt="" align="left"/>
					                                                        		</digi:link>
					                                                        		<c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
								                                                   		<span style="color: #ffffff"> ${ampCalendarEventItem.title}</span>
								                                                	</c:forEach>
					                                                        	</c:if>
					                                                        	&nbsp;
					                                                      	</div>
					                              						</c:if>
				                              						</c:if>
				                              						
				                              						<!-- Start Month != Current Month -->
				                              						<c:if test="${startMonth!=currentMonth && currentMonth==endMonth}">
				                                						<c:if test="${item.dayOfMonth>startDay && !item.enabled}">
											                                <div style="margin:0px;padding:0px;font-weight:Bold;text-align:center;color:Black;border:1px solid ${ampCalendarGraph.ampCalendar.eventType.color};background-color:${ampCalendarGraph.ampCalendar.eventType.color};" onmouseover="stm(['${eventName}','stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])"  onmouseout="htm()">
					                                                        	<!-- image with link should be only on mondays -->
												                                <c:if test="${stat.index%7==0}">
												                                	<digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~method=preview~resetForm=true">
										                                               	<digi:img src="module/calendar/images/lookglass2.gif" border="0" alt="" align="left"/>
							                                                       	</digi:link>
												                                </c:if>
					                                                        	&nbsp;
					                                                        </div>
				                                						</c:if>
				                                						<c:if test="${item.dayOfMonth<=endDay && item.enabled}">
											                                <div style="margin:0px;padding:0px;font-weight:Bold;text-align:center;color:Black;border:1px solid ${ampCalendarGraph.ampCalendar.eventType.color};background-color:${ampCalendarGraph.ampCalendar.eventType.color};" onmouseover="stm(['${eventName}','stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])"  onmouseout="htm()">
												                                <!-- image with link should be only on mondays -->
												                                <c:if test="${stat.index%7==0}">
												                                	<digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~method=preview~resetForm=true">
										                                               	<digi:img src="module/calendar/images/lookglass2.gif" border="0" alt="" align="left"/>
							                                                       	</digi:link>
												                                </c:if>
						                                                        <c:if test="${item.dayOfMonth==startDay && !item.enabled}">
						                                                        	<c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
									                                                   	<span style="color: #ffffff"> ${ampCalendarEventItem.title}</span>
									                                                </c:forEach>
						                                                        </c:if>
						                                                        &nbsp;
					                                                        </div>
				                                						</c:if>
				                              						</c:if>
				                              						<c:if test="${startMonth!=currentMonth && endMonth!=currentMonth}">
				                                						<div style="margin:0px;padding:0px;font-weight:Bold;text-align:center;color:Black;border:1px solid ${ampCalendarGraph.ampCalendar.eventType.color};background-color:${ampCalendarGraph.ampCalendar.eventType.color};" onmouseover="stm(['${eventName}','stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])"  onmouseout="htm()">
					                                                    	<!-- image with link should be only on mondays -->
												                        	<c:if test="${stat.index%7==0}">
												                               	<digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~method=preview~resetForm=true">
											                                      	<digi:img src="module/calendar/images/lookglass2.gif" border="0" alt="" align="left"/>
								                                                </digi:link>
												                            </c:if>
					                                                    	&nbsp;
					                                                    </div>
				                              						</c:if>
				                              					
				                              				<!-- when start Year < End year and start month and end month is equal -->		
				                              					<c:if test="${endYear!=currentYear && startMonth==currentMonth && endYear > startYear}">
				                              					
				                                						<c:if test="${(endMonth==currentMonth && item.dayOfMonth >=startDay  && item.enabled) || (endMonth==currentMonth && item.dayOfMonth <=startDay  && !item.enabled)}">
					                                						<div id="div1" style="border:1px solid ${ampCalendarGraph.ampCalendar.eventType.color};background-color:${ampCalendarGraph.ampCalendar.eventType.color};" onmouseover="stm(['${eventName}','stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])"  onmouseout="htm()" >
					                                                        	<c:if test="${item.dayOfMonth==startDay && item.enabled}">
					                                                        		<digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~method=preview~resetForm=true">
								                                                       	<digi:img src="module/calendar/images/lookglass2.gif" border="0" alt="" align="left"/>
					                                                        		</digi:link>
					                                                        		<c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
								                                                   		<span style="color: #ffffff"> ${ampCalendarEventItem.title}</span>
								                                                	</c:forEach>
					                                                        	</c:if>
					                                                        	&nbsp;
					                                                      	</div>
					                              						</c:if>
				                              						</c:if>	
				                              					<c:if test="${endYear==currentYear && endMonth==currentMonth && endYear > startYear}">
				                              							<c:if test="${(endMonth==currentMonth && item.dayOfMonth<=endDay && item.enabled) || (endMonth==currentMonth && item.dayOfMonth>=endDay && !item.enabled)  }">
					                                						<div id="div1" style="border:1px solid ${ampCalendarGraph.ampCalendar.eventType.color};background-color:${ampCalendarGraph.ampCalendar.eventType.color};" onmouseover="stm(['${eventName}','stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])"  onmouseout="htm()" >
					                                                        	<c:if test="${item.dayOfMonth==startDay && item.enabled}">
					                                                        		<digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~method=preview~resetForm=true">
								                                                       	<digi:img src="module/calendar/images/lookglass2.gif" border="0" alt="" align="left"/>
					                                                        		</digi:link>
					                                                        		<c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
								                                                   		<span style="color: #ffffff"> ${ampCalendarEventItem.title}</span>
								                                                	</c:forEach>
					                                                        	</c:if>
					                                                        	&nbsp;
					                                                      	</div>
					                              						</c:if>
				                              						</c:if>		
				                              						
				                              					</td>
				                              					<!-- define whether td's bgcolor should be white or not -->
				                              					<c:set var="backgrColor">
				                              						<c:choose>
				                              							<c:when test="${startMonth==currentMonth}">
				                              								<c:choose>
				                              									<c:when test="${endMonth==currentMonth}">
				                              										<c:choose>
				                              											<c:when test="${(item.dayOfMonth >=startDay && item.dayOfMonth<=endDay && item.enabled) || 
				                              											 (startDay==endDay && endYear!=startYear && startYear==currentYear && startDay<item.dayOfMonth && item.enabled) ||
				                              											  (startDay==endDay && endYear!=startYear && endYear==currentYear && startDay<item.dayOfMonth && !item.enabled) || 
				                              											  (startDay==endDay && endYear!=startYear && endYear==currentYear && endDay>=item.dayOfMonth && item.enabled)}">
				                              												${ampCalendarGraph.ampCalendar.eventType.color}
				                              											</c:when>
				                              											<c:otherwise>#e8eef7</c:otherwise>
				                              										</c:choose>
				                              									</c:when>
				                              									<c:when test="${endMonth!=currentMonth}">
				                              										<c:choose>
				                              											<c:when test="${(item.dayOfMonth>=startDay && item.enabled) ||(item.dayOfMonth<endDay && !item.enabled)}">
				                              												${ampCalendarGraph.ampCalendar.eventType.color}
				                              											</c:when>
				                              											<c:otherwise>#e8eef7</c:otherwise>
				                              										</c:choose>
				                              									</c:when>
				                              								</c:choose>
				                              							</c:when>
				                              							<c:when test="${startMonth!=currentMonth && currentMonth==endMonth}">
				                              								<c:choose>
				                              									<c:when test="${(item.dayOfMonth>startDay && !item.enabled) || (item.dayOfMonth<=endDay && item.enabled)}">
				                              										${ampCalendarGraph.ampCalendar.eventType.color}
				                              									</c:when>
				                              									<c:otherwise>#e8eef7</c:otherwise>
				                              								</c:choose>
				                              							</c:when>
				                              							<c:otherwise>${ampCalendarGraph.ampCalendar.eventType.color}</c:otherwise>
				                              						</c:choose>
				                              					</c:set>
				                              					<td width="1px" bgcolor="${backgrColor}"/>
								                          </c:forEach>
								                        </tr>
				                		        	</c:forEach>
				                      				<tr height="4px" bgcolor="#e8eef7">
				                        				<td colspan="14" />
				                      				</tr>
				                    			</c:forEach>
				                  			</table>
				    					</td>
				              		</tr>
								</c:if>
				        		</feature:display>				            	
				                <!-- Monthly view End -->
				                <!-- Daily View Start -->
				                <feature:display name="Daily View" module="Calendar">
				                	<c:if test="${calendarViewForm.view == 'daily'}">
				                	<tr>
				                    	<td style="padding:15px;text-align:center;">
				                        	<table align="center" style="min-width:700px;" width="100%">
				                            	<tr>
				                                	<td>
				                                    	<div style="overflow:auto;height:500px;border:2px solid #e8eef7;">
				                                        	<table width="100%">
				                                            	<c:forEach var="hour" begin="0" end="23">				                                            		
				                                              		<tr style="height:40px;">
				                                                		<td align="left" style="border-top:2px solid #e8eef7;color:White;background-color:#376091;vertical-align:top;width:70px;padding:6px;font-size:12px;font-family: Tahoma">
				                                                  			<c:if test="${hour < 12}">
				                                                    			<c:if test="${hour < 10}">
				                                                      				<c:set var="hoursToDisplay" value="0${hour}:00"/>
				                                                    			</c:if>
				                                                    			<c:if test="${hour > 9}">
				                                                      				<c:set var="hoursToDisplay" value="${hour}:00"/>
				                                                    			</c:if>
				                                                   	 			${hoursToDisplay} <digi:trn key="aim:am">AM</digi:trn>
				                                                  			</c:if>
				                                                  			<c:if test="${hour > 11}">
				                                                   				<c:if test="${hour < 13}">
				                                                      				${hour}:00 <digi:trn key="aim:mp">PM</digi:trn>
							                                                    </c:if>
							                                                    <c:if test="${hour > 12}">
				                                                      				<c:if test="${(hour - 12) < 10}">
				                                                        				<c:set var="hoursToDisplay" value="0${hour - 12}:00"/>
				                                                      				</c:if>
				                                                      				<c:if test="${(hour - 12) > 9}">
				                                                        				<c:set var="hoursToDisplay" value="${hour - 12}:00"/>
				                                                      				</c:if>
				                                                      				${hoursToDisplay} <digi:trn key="aim:pm">PM</digi:trn>
				                                                    			</c:if>
				                                                  			</c:if>
				                                                		</td>
				                                                		<td style="border-top:2px solid #e8eef7;background-color: #ffffff">
				                                                  			<c:forEach var="ampCalendarGraph" items="${calendarViewForm.ampCalendarGraphs}">
				                                                  				<bean:define id="startHours" value="${ampCalendarGraph.ampCalendar.calendarPK.startHour}"></bean:define>
				                                                  				<bean:define id="startMinute" value="${ampCalendarGraph.ampCalendar.calendarPK.startMinute}"></bean:define>
				                                                  				<bean:define id="startDay" value="${ampCalendarGraph.ampCalendar.calendarPK.startDay}"></bean:define>
				                                                  				<bean:define id="startMonth" value="${ampCalendarGraph.ampCalendar.calendarPK.startMonth+1}"></bean:define>
				                                                  				<bean:define id="startYear" value="${ampCalendarGraph.ampCalendar.calendarPK.startYear}"></bean:define>
				                                                  				<bean:define id="endHours" value="${ampCalendarGraph.ampCalendar.calendarPK.endHour}"></bean:define>
				                                                  				<bean:define id="endMinute" value="${ampCalendarGraph.ampCalendar.calendarPK.endMinute}"></bean:define>
				                                                  				<bean:define id="endDay" value="${ampCalendarGraph.ampCalendar.calendarPK.endDay}"></bean:define>
				                                                  				<bean:define id="endMonth" value="${ampCalendarGraph.ampCalendar.calendarPK.endMonth+1}"></bean:define>
				                                                  				<bean:define id="endYear" value="${ampCalendarGraph.ampCalendar.calendarPK.endYear}"></bean:define>
>>>>>>> .merge-right.r10759
<<<<<<< .working

	var trn_day_sun='<digi:trn>Sun</digi:trn>';
	var trn_day_mon='<digi:trn>Mon</digi:trn>';
	var trn_day_tue='<digi:trn>Tue</digi:trn>';
	var trn_day_wed='<digi:trn>Wed</digi:trn>';
	var trn_day_thu='<digi:trn>Thu</digi:trn>';
	var trn_day_fri='<digi:trn>Fri</digi:trn>';
	var trn_day_sat='<digi:trn>Sat</digi:trn>';
=======
																				<c:set var="reccperiod">${ampCalendarGraph.ampCalendar.calendarPK.calendar.recurrCalEvent}</c:set>
																		        <c:set var="eventName">
																		        	<c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}" begin="${fn:length(ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem)-1}">
																		        		${fn:replace(ampCalendarEventItem.title, "'", "\\'")}
																		        	</c:forEach>
																		        </c:set>
										                        				<c:set var="eventStartDate">
										                        					${startDay}/${startMonth}/${startYear} ${startHours}:<c:if test="${startMinute<10}">0</c:if>${startMinute}
										                        				</c:set>
										                        				<c:set var="eventEndDate">
										                        					${endDay}/${endMonth}/${endYear} ${endHours}:<c:if test="${endMinute<10}">0</c:if>${endMinute}
										                        				</c:set>
				                                                    			<bean:define id="currentMonth">${calendarViewForm.baseDateBreakDown.month}</bean:define>
				                                                    			<bean:define id="currentDay">${calendarViewForm.baseDateBreakDown.dayOfMonth}</bean:define>
				                                                    			<bean:define id="currentYear">${calendarViewForm.baseDateBreakDown.year}</bean:define>
				                                                    			<c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.recurrCalEvent}">
																						<script type="text/javascript">
									                              							var msg = stDate+":${eventStartDate}<br>"+endDate+":${eventEndDate}";									                              							
									                              						</script>
																						<c:if test='${(ampCalendarEventItem.typeofOccurrence == "Dailly" && calendarViewForm.baseDateBreakDown.dayOfMonth%ampCalendarEventItem.recurrPeriod == 0)||ampCalendarEventItem.typeofOccurrence != "Dailly"}'>
                                                                                        	<div style="font-weight:Bold;text-align:center;border:0px;">
                                                                                            	<table style="width: 100%;padding-bottom: 2px;padding-top: 2px;" cellspacing="0">
                                                                                                	<tr style="width: 100%">
                                                                                                    	<bean:define id="currentHour">${hour}</bean:define>
                                                                                                        <c:forEach var="min" begin="0" end="59">
                                                                                                          	<bean:define id="currentMinute" value="${min}"></bean:define>
                                                                                                            <% Calendar currentDate=CalendarUtil.buildCalendar(new Integer(currentDay).intValue(),new Integer(currentMonth).intValue(),new Integer(currentYear).intValue(),new Integer(currentHour).intValue(),new Integer(currentMinute).intValue());
                                                                                                               Calendar evntStartDate=CalendarUtil.buildCalendar(new Integer(startDay).intValue(),new Integer(startMonth).intValue(),new Integer(startYear).intValue(),new Integer(startHours).intValue(),new Integer(startMinute).intValue());
                                                                                                               Calendar evntEndDate=CalendarUtil.buildCalendar(new Integer(endDay).intValue(),new Integer(endMonth).intValue(),new Integer(endYear).intValue(),new Integer(endHours).intValue(),new Integer(endMinute).intValue());
>>>>>>> .merge-right.r10759

<<<<<<< .working
</script>

 <script src="<digi:file src="module/calendar/dhtmlxScheduler/dhtmlxcommon.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/scheduler.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/config.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/base.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/locale_sv.js"/>" language="JavaScript" type="text/javascript" ></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/event.js"/>"language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/load.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/lightbox.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/dhtmlxdataprocessor.js"/>" language="JavaScript" type="text/javascript"></script>
 <script src="<digi:file src="module/calendar/dhtmlxScheduler/property.js"/>" language="JavaScript" type="text/javascript"></script>
  <script src="<digi:file src="module/calendar/dhtmlxScheduler/recurring.js"/>" language="JavaScript" type="text/javascript"></script>

 <link rel="stylesheet" href="<digi:file src="module/calendar/css/layout.css"/>"> 
 <link rel="stylesheet" href="<digi:file src="module/calendar/css/note.css"/>"> 
 <link rel="stylesheet" href="<digi:file src="module/calendar/css/recurring.css"/>"> 
 <link rel="stylesheet" href="<digi:file src="module/calendar/css/lightbox.css"/>"> 
 
<div style="display: none"><jsp:include page="viewEventsFilter.jsp" flush="true"/></div>

<c:set var="printButon"><%=request.getSession().getAttribute("print")%></c:set>
 <c:if test="${printButon}">
 	<style>
		.dhx_cal_container {
		font-size: 10pt;
		}
		.dhx_cal_event_line {
		 height: auto;
		}
		.dhx_cal_event_clear{
		 height: auto;
=======
                                                                                                               if((currentDate.after(evntStartDate) || currentDate.equals(evntStartDate)) && (currentDate.before(evntEndDate) || currentDate.equals(evntEndDate))) {%>
                                                                                                                	<c:set var="backgrColor" value="${ampCalendarGraph.ampCalendar.eventType.color}"/>
                                                                                                                	<c:set var="takeEventsColor">1</c:set>
                                                                                                               <%} else {%>
                                                                                                            	   	<c:set var="backgrColor" value="#ffffff"></c:set>
                                                                                                            	   	<c:set var="takeEventsColor">0</c:set>
                                                                                                              <%}%>
                                                                                                              <c:if test="${ampCalendarEventItem.typeofOccurrence == 'Dailly'}">
                                                                                                              	<td style="padding:0px;background-color:${backgrColor};border-color:${backgrColor}"  <c:if test="${takeEventsColor==1}"> onmouseover="stm([evnt,'recurr:every ${ampCalendarEventItem.recurrPeriod} day <br>name:${eventName}<br>stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])" onmouseout="htm()"</c:if>>
                                                                                                              </c:if>
                                                                                                              <c:if test="${ampCalendarEventItem.typeofOccurrence != 'Dailly'}">
                                                                                                              	<td style="padding:0px;background-color:${backgrColor};border-color:${backgrColor}"  <c:if test="${takeEventsColor==1}"> onmouseover="stm(['${eventName}','stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])"  onmouseout="htm()"</c:if>>
                                                                                                              </c:if>
                                                                                                              	<c:if test="${backgrColor!='#ffffff' && (min==0 || (startYear==currentYear && startMonth==currentMonth && startDay==currentDay && startHours==hour && startMinute==min))}">
                                                                                                              		<digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~~method=preview~resetForm=true">
                                                                                                                        <digi:img src="module/calendar/images/lookglass2.gif" border="0" alt="" align="left"/>
                                                                                                                    </digi:link>
                                                                                                              	</c:if>
                                                                                                              	<c:choose>
                                                                                                                	<c:when test="${(startYear==currentYear && startMonth==currentMonth && startDay==currentDay && startHours==hour && startMinute==min) }">
                                                                                                                        <c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
                                                                                                                        	<span style="color: #ffffff"> ${ampCalendarEventItem.title}</span>
                                                                                                                        </c:forEach>
                                                                                                                     </c:when>
                                                                                                                   	 <c:otherwise>&nbsp;</c:otherwise>
                                                                                                                </c:choose>
                                                                                                              </td>
                                                                                                           </c:forEach>
                                                                                                         </tr>
                                                                                                         <tr height="2px">
                                                                                                             <td colspan="60"/>
                                                                                                         </tr>
                                                                                                      </table>
                                                                                                   </div>
                                                                                     		</c:if>
                                                                                    </c:forEach>
                                                                              </c:forEach>
						                                           			&nbsp;
				                                                		</td>
				                                              		</tr>
				                                            	</c:forEach>
				                                            	<tr height="2px">
				                                              		<td style="border-top:1px solid #e8eef7;color:White;">&nbsp; </td>
				                                              		<td style="border-top:1px solid #e8eef7;color:White;">&nbsp;</td>
				                                            	</tr>
				                                          	</table>
				                                        </div>
				                                     </td>
												</tr>
				                              </table>
				                           </td>
				                         </tr>
				                       </c:if>
				                </feature:display>			                
								<!-- Daily View End -->
				            	</c:if>
				            	<feature:display name="Yearly View" module="Calendar">
				            		<!-- yearly view Start -->
				            		<c:if test="${calendarViewForm.view == 'yearly'}">
									<tr style="width: 100%">
					                    	<td style="padding:10px;text-align:center;width: 100%">
					                        	<table  style="width: 100%">
					                            	<tr style="width: 100%">
					                                	<td style="width: 100%">
					                                    	<div style="border:2px solid #e8eef7;width: 100%">
					                                        	<table style="width: 100%">
						                                        		<c:forEach var="row" items="${calendarViewForm.dateNavigator.items}"  varStatus="stat">
						  													<c:forEach var="item" items="${row}">							  													
						  															<c:set var="monthIndex">
																					   	<c:if test="${item.month=='Jan'}">01</c:if>
																					    <c:if test="${item.month=='Feb'}">02</c:if>
																						<c:if test="${item.month=='Mar'}">03</c:if>
																						<c:if test="${item.month=='Apr'}">04</c:if>
																						<c:if test="${item.month=='May'}">05</c:if>
																						<c:if test="${item.month=='Jun'}">06</c:if>
																						<c:if test="${item.month=='Jul'}">07</c:if>
																						<c:if test="${item.month=='Aug'}">08</c:if>
																						<c:if test="${item.month=='Sep'}">09</c:if>
																						<c:if test="${item.month=='Oct'}">10</c:if>
																						<c:if test="${item.month=='Nov'}">11</c:if>
																						<c:if test="${item.month=='Dec'}">12</c:if>
																						
																						<c:if test="${item.month=='Mes'}">01</c:if>
																					    <c:if test="${item.month=='Tik'}">02</c:if>
																						<c:if test="${item.month=='Hid'}">03</c:if>
																						<c:if test="${item.month=='Tah'}">04</c:if>
																						<c:if test="${item.month=='Tir'}">05</c:if>
																						<c:if test="${item.month=='Yek'}">06</c:if>
																						<c:if test="${item.month=='Meg'}">07</c:if>
																						<c:if test="${item.month=='Miy'}">08</c:if>
																						<c:if test="${item.month=='Gin'}">09</c:if>
																						<c:if test="${item.month=='Sen'}">10</c:if>
																						<c:if test="${item.month=='Ham'}">11</c:if>
																						<c:if test="${item.month=='Neh'}">12</c:if>
																						<c:if test="${item.month=='Peg'}">13</c:if>
																						
																					</c:set>
																					<tr style="width: 100%;height: 25px">
																						<td style="width: 80px;background-color: #376091;text-align: center;" >
																					    	<a href="#" style="text-decoration:none;color:White;font-size:12px;font-family:Tahoma;text-align: center" onclick="submitFilterForm('${calendarViewForm.view}', '${item.timestamp}');return(false);">
																					        	<digi:trn key="aim:calendar${item.month}">${item.month}</digi:trn>
																					        </a>
																						</td>
																						<td style="border-top:2px solid #e8eef7;background-color: #ffffff">
																								<c:forEach var="ampCalendarGraph" items="${calendarViewForm.ampCalendarGraphs}">
																				           			<c:set var="startMonth">
																				           				<c:if test="${ampCalendarGraph.ampCalendar.calendarPK.startMonth+1 <10}">0${ampCalendarGraph.ampCalendar.calendarPK.startMonth+1}</c:if>
																				           				<c:if test="${ampCalendarGraph.ampCalendar.calendarPK.startMonth+1>=10}">${ampCalendarGraph.ampCalendar.calendarPK.startMonth+1}</c:if>
																				           			</c:set>
																				           			<c:set var="endMonth">
																				           				<c:if test="${ampCalendarGraph.ampCalendar.calendarPK.endMonth+1 <10}">0${ampCalendarGraph.ampCalendar.calendarPK.endMonth+1}</c:if>
																				           				<c:if test="${ampCalendarGraph.ampCalendar.calendarPK.endMonth+1>=10}">${ampCalendarGraph.ampCalendar.calendarPK.endMonth+1}</c:if>
																				           			</c:set>
																				           			<c:set var="reccperiod">${ampCalendarGraph.ampCalendar.calendarPK.calendar.recurrCalEvent}</c:set>
																				           			<c:set var="startDay">${ampCalendarGraph.ampCalendar.calendarPK.startDay}</c:set>
																				           			<c:set var="startYear">${ampCalendarGraph.ampCalendar.calendarPK.startYear}</c:set>
																				           			<c:set var="endYear">${ampCalendarGraph.ampCalendar.calendarPK.endYear}</c:set>
															                        				<c:set var="endDay">${ampCalendarGraph.ampCalendar.calendarPK.endDay}</c:set>
															                        				<c:set var="currentYear">${calendarViewForm.baseDateBreakDown.year}</c:set>
	                                                                                                <c:set var="currentYear">${calendarViewForm.baseDateBreakDown.year}</c:set>
	                                                                                               <c:set var="recStartMonth">${ampCalendarGraph.ampCalendar.calendarPK.recurrStartMonth+1}</c:set>
	                                                                                               <c:set var="recEndMonth">${ampCalendarGraph.ampCalendar.calendarPK.recurrEndMonth+1}</c:set>
	                                                                                               <c:set var="recStartYear">${ampCalendarGraph.ampCalendar.calendarPK.recurrStartYear}</c:set>
	                                                                                               <c:set var="recEndYear">${ampCalendarGraph.ampCalendar.calendarPK.recurrEndYear}</c:set>
>>>>>>> .merge-right.r10759
	
		}
		
	</style>
	<script type="text/javascript" charset="utf-8">
		  scheduler._click.dhx_cal_tab=function(){
		   var mode = this.getAttribute("name").split("_")[0];
		   scheduler.setCurrentView(scheduler._date,mode);
		    if(mode=="week"||mode=="day")
		      scheduler._els["dhx_cal_data"][0].style.height="1100px";
		 }
	
	</script>  
	  <table width="200px" height="40px">
	  	<tr>
	  		<td>	
	 	
		 	<a target="_blank" title="Printing" onclick="window.print();" style="cursor: pointer">
		 		<img width="20" vspace="2" hspace="2" height="30" border="0" alt="Printer Friendly" src="/TEMPLATE/ampTemplate/module/aim/images/printer.gif"/>
		 	</a>
	 		<input type="button" value="close" onClick="window.close()" width="20" height="30"/>
	 		</td>
	 	</tr>
	  </table>
 </c:if>

<style>
	<%=CalendarUtil.getEventTypesCss()%>
</style>
<script type="text/javascript" charset="utf-8">
<!--

	function init() {
		 
		var sections=[
		  			{key:1, label:"Section A"},
		  			{key:2, label:"Section B"},
		  			{key:3, label:"Section C"},
		  			{key:4, label:"Section D"},
		  			{key:5, label:"Section E"},
		  			{key:6, label:"Section F"},
		  			{key:7, label:"Section G"},
		  			{key:8, label:"Section H"},
		  			{key:9, label:"Section I"},
		  			{key:10, label:"Section G"},
		  			{key:11, label:"Section K"},
		  			{key:12, label:"Section L"}
		  			
		  		];		
		
		scheduler.config.xml_date="%Y-%m-%d %H:%i";
		scheduler.config.multi_day = true;
	
		var eth = getEthiopianCalendarDate(<%=request.getSession().getAttribute("year")%>,<%=request.getSession().getAttribute("month")%>,<%=request.getSession().getAttribute("day")%>);
		var myDate = new Date(eth);
		var ehtMonth = <%=request.getSession().getAttribute("month")%>;
		var type = calType(<%=request.getSession().getAttribute("type")%>);
		var printView = <%=request.getSession().getAttribute("view")%>;
		var printDate = <%=request.getSession().getAttribute("date")%>;


		if(printDate !=null){
		 var date = <%=request.getSession().getAttribute("date")%>;
		}else{
			date = myDate;
			}
		
		var defoultView = "month";
		if(printView!=null){
			if(printView == 1){
				defoultView = "day"

				}else if(printView == 2){
					defoultView = "week"

				}

			}
		
		
		scheduler.init('scheduler_here',date,defoultView,type, ehtMonth);
		scheduler.templates.event_text=function(start_date,end_date,ev){
			return "Text:<b> "+ev.text+"</b><br>"+"Descr."+ev.details;
		}
		scheduler.load("/calendar/showEvents.do");
		scheduler.templates.event_class=function(start_date,end_date,ev){
			if(ev.type != 0){
						return "event_"+ev.type;
				}
		  }
		
		 
		scheduler.attachEvent("onViewChange", function (mode , date){
			   document.getElementById("printView").value = mode;
			   document.getElementById("printDate").value = date;
			   //show corresponding view breadcrumb
			   var whichView=document.getElementById("viewSpan");
			   var monthly='<digi:trn>Monthly View</digi:trn>';
			   var weekly='<digi:trn>Weekly View</digi:trn>';
			   var daily='<digi:trn>Daily View</digi:trn>';
			   if(mode=='month'){
				   whichView.innerHTML=monthly;
			   }else if(mode=='week'){
				   whichView.innerHTML=weekly;
			   }else if(mode=='day'){
				   whichView.innerHTML=daily;
			   }			   
			});
		scheduler.attachEvent("onClick",function(id){
		    var ev = scheduler.getEvent(id);
			var eventId = ev.id;
			if(eventId.indexOf("#") != "-1"){
			var	eventId = eventId.slice(0,eventId.indexOf("#"));
			}
			<digi:context name="previewEvent" property="context/module/moduleinstance/showCalendarEvent.do" />
		        document.forms[0].action = "<%= previewEvent%>~ampCalendarId="+eventId+"~method=preview~resetForm=true";
		       document.forms[0].submit();
		    return true;
		});
		scheduler.createUnitsView("unit","section_id",sections);
		var printView = <%=request.getSession().getAttribute("print")%>
		
	if(!printView){
		 scheduler.templates.event_bar_text=function(start_date,end_date,ev){
		    var dotted= "..."; 
			var isDotted = ev.text.length >20 ? dotted : "";
		    var text = ev.text.substr(0,20)+isDotted;
	        var img = '<digi:img src="module/calendar/images/magnifier.png" height="12" width="12" align="left"/>';
	        return "<span title='"+"Title:"+text+" "+"StartDate:"+start_date+"EndDate:"+end_date+"'>"+img+""+text+"</span>";
		 }
	 }else{

		 scheduler.templates.event_bar_text=function(start_date,end_date,ev){
		        var text = ev.text.substr(0,20);
		        return "<span style='font-size: 10pt;'>"+text+"</span>";
			 }
		 

		 }
		scheduler.config.dblclick_create = false;
		scheduler.config.multi_days = true;
	/*
		scheduler.attachEvent("onViewChange",function(mode,date){

			if(mode == "month"){
						submitFilterForm('monthly',date.getTime()/1000); 
						
						return true;
					}
			});
		
	*/


	
	} 

function calType(type){
	switch (type){
	case 0:
		return "GRE";
		break;
	case 1:
		return "ETH";
		break;	

		}
}	
	
function getEthiopianCalendarDate(yyyy,mm,dd)
{
	
   var months = new Array(13);
   months[1]  = "jan";
   months[2]  = "feb";
   months[3]  = "mar";
   months[4]  = "apr";
   months[5]  = "may";
   months[6]  = "jun";
   months[7]  = "jul";
   months[8]  = "aug";
   months[9]  = "sep";
   months[10] = "oct";
   months[11] = "nov";
   months[12] = "dec";
   months[13] = "dec";
   var now = new Date(yyyy,mm,dd);

   if(now.getMonth()==0 || mm == 13){
   		var monthnumber = now.getMonth()+12;
   }else{
	   var monthnumber = now.getMonth();
	   }
   var monthname   = months[monthnumber];
   var monthday    = now.getDate();
 
   var dateString = monthname +
                    ' ' + monthday +
                	' ' + yyyy;

  return dateString;
}	
-->
</script>	
<style> 

</style>
<body>
<script type="text/javascript">
addLoadEvent(init);
</script>
<input type="hidden" value=""  id="printView"/>
<input type="hidden" value=""  id="printDate"/>
	<div id="scheduler_here" class="dhx_cal_container"  style='padding:1% 0% 1% 0%; width:100%; height:100%; position:relative'>
		<div class="dhx_cal_navline">
			<div class="dhx_cal_prev_button">&nbsp;</div>
			<div class="dhx_cal_next_button">&nbsp;</div>
			<div class="dhx_cal_today_button"></div>
			<div class="dhx_cal_date"></div>
	<!-- <div  class="dhx_cal_tab" name="unit_tab" style="right:270px;"></div>  -->	 
			<div class="dhx_cal_tab" name="month_tab" style="right:205px;"></div>
			<div class="dhx_cal_tab" name="week_tab" style="right:140px;"></div>
			<div class="dhx_cal_tab" name="day_tab" style="right:76px;"></div>
			
		</div>
		<div class="dhx_cal_header">
		</div>
		<div class="dhx_cal_data">
		</div>		
	</div>
</body>