<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@page import="java.util.*"%>
<%@page import="org.digijava.module.calendar.form.CalendarViewForm" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@page import="org.digijava.module.calendar.util.CalendarUtil"%>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"  src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer"  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>

<script langauage="JavaScript">
	var evnt="<digi:trn key='calendar:event'>Event Name</digi:trn>"
	var stDate="<digi:trn key='calendar:startDate'>Start Date</digi:trn>"
	var endDate="<digi:trn key='calendar:endDate'>End Date</digi:trn>"
</script>
<style type="text/css" media="screen">
<!--
#div1{
height: 100%;
margin:0px;
padding:0px;
font-weight:Bold;
text-align:center;
color:Black;
}
-->
</style>

<digi:instance property="calendarViewForm"/>

<table bgcolor="#f4f4f2"  width="100%" cellspacing="" cellpadding="1" height="100%">
	<tr>
    	<td bgcolor="#ffffff">
    		<jsp:include page="viewEventsButtons.jsp" flush="false" />
    	</td>
    <tr>
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
							        	<digi:trn key="aim:calendar:basemonthNameLong:${calendarViewForm.baseDateBreakDown.monthNameLong}">${calendarViewForm.baseDateBreakDown.monthNameLong}</digi:trn>,&nbsp;
							            ${calendarViewForm.baseDateBreakDown.year}
			        				</td>
		        				</c:if>
	        				</feature:display>
	        				<feature:display name="Weekly View" module="Calendar">
	        					<c:if test="${calendarViewForm.view == 'weekly'}">
		        					<td align="center" width="30%" nowrap="nowrap" style="font-size:12px;font-weight:bold;font-family: Tahoma;">
		        						<digi:trn key="aim:calendar:startmonthNameShort:${calendarViewForm.startDateBreakDown.monthNameShort}">${calendarViewForm.startDateBreakDown.monthNameShort}</digi:trn>
						                ${calendarViewForm.startDateBreakDown.dayOfMonth},&nbsp;
						                ${calendarViewForm.startDateBreakDown.year}&nbsp;-&nbsp;
						                <digi:trn key="aim:calendar:endmonthNameShort:${calendarViewForm.endDateBreakDown.monthNameShort}">${calendarViewForm.endDateBreakDown.monthNameShort}</digi:trn>
						                ${calendarViewForm.endDateBreakDown.dayOfMonth},&nbsp;
						                ${calendarViewForm.endDateBreakDown.year}
		        					</td>
		        				</c:if>
	        				</feature:display>
	        				<feature:display name="Daily View" module="Calendar">
	        					<c:if test="${calendarViewForm.view == 'daily'}">
		        					<td align="center" width="20%" nowrap="nowrap" style="font-size:12px;font-weight:bold;font-family: Tahoma;">
		        						<digi:trn key="aim:calendar:dailymonthNameLong:${calendarViewForm.baseDateBreakDown.monthNameLong}">${calendarViewForm.baseDateBreakDown.monthNameLong}</digi:trn>
						                ${calendarViewForm.baseDateBreakDown.dayOfMonth},&nbsp;
					                	${calendarViewForm.baseDateBreakDown.year}
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
												        		${ampCalendarEventItem.title}
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
				                              					
				                              						<c:if test="${startMonth==currentMonth && startYear== currentYear && endYear>!startYear}">
				                              							<c:if test="${(endMonth==currentMonth && item.dayOfMonth >=startDay && item.dayOfMonth<=endDay && item.enabled) || (endMonth!=currentMonth && ((item.dayOfMonth>=startDay && item.enabled)||(item.dayOfMonth<endDay && !item.enabled)))}">
					                                						<div id="div1" style="border:1px solid ${ampCalendarGraph.ampCalendar.eventType.color};background-color:${ampCalendarGraph.ampCalendar.eventType.color};" onmouseover="stm([evnt,'name:${eventName}<br>stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])" onmouseout="htm()" >
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
											                                <div style="margin:0px;padding:0px;font-weight:Bold;text-align:center;color:Black;border:1px solid ${ampCalendarGraph.ampCalendar.eventType.color};background-color:${ampCalendarGraph.ampCalendar.eventType.color};" onmouseover="stm([evnt,'name:${eventName}<br>stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])" onmouseout="htm()">
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
											                                <div style="margin:0px;padding:0px;font-weight:Bold;text-align:center;color:Black;border:1px solid ${ampCalendarGraph.ampCalendar.eventType.color};background-color:${ampCalendarGraph.ampCalendar.eventType.color};" onmouseover="stm([evnt,'name:${eventName}<br>stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])" onmouseout="htm()">
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
				                                						<div style="margin:0px;padding:0px;font-weight:Bold;text-align:center;color:Black;border:1px solid ${ampCalendarGraph.ampCalendar.eventType.color};background-color:${ampCalendarGraph.ampCalendar.eventType.color};" onmouseover="stm([evnt,'name:${eventName}<br>stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])" onmouseout="htm()">
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
				                              					<c:if test="${endYear!=currentYear && startMonth==currentMonth}">
				                                						<c:if test="${(endMonth==currentMonth && item.dayOfMonth >=startDay  && item.enabled)}">
					                                						<div id="div1" style="border:1px solid ${ampCalendarGraph.ampCalendar.eventType.color};background-color:${ampCalendarGraph.ampCalendar.eventType.color};" onmouseover="stm([evnt,'name:${eventName}<br>stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])" onmouseout="htm()" >
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
				                              						
				                              					<c:if test="${endYear==currentYear && endMonth==currentMonth}">
				                                						<c:if test="${(endMonth==currentMonth && item.dayOfMonth<=endDay  && item.enabled)}">
					                                						<div id="div1" style="border:1px solid ${ampCalendarGraph.ampCalendar.eventType.color};background-color:${ampCalendarGraph.ampCalendar.eventType.color};" onmouseover="stm([evnt,'name:${eventName}<br>stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])" onmouseout="htm()" >
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
				                              											<c:when test="${item.dayOfMonth >=startDay && item.dayOfMonth<=endDay && item.enabled}">
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

                                                                                <c:set var="reccperiod">${ampCalendarGraph.ampCalendar.calendarPK.calendar.recurrCalEvent}</c:set>
																		        <c:set var="eventName">
																		        	<c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}" begin="${fn:length(ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem)-1}">
																		        		${ampCalendarEventItem.title}
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
                                                                                                              	<td style="padding:0px;background-color:${backgrColor};border-color:${backgrColor}"  <c:if test="${takeEventsColor==1}"> onmouseover="stm([evnt,'name:${eventName}<br>stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])" onmouseout="htm()"</c:if>>
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
	
	                                                                                                <c:set var="eventName">
																							        	<c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}" begin="${fn:length(ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem)-1}">
																							        		${ampCalendarEventItem.title}
																							        	</c:forEach>
																							        </c:set>
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
																										<c:set var="drawTD">
																										<c:choose>
																											<c:when test="${startYear==endYear && startMonth<=monthIndex && monthIndex<=endMonth}">1</c:when>
																											<c:when test="${startYear!=endYear}">
																												<c:choose>
																													<c:when test="${currentYear==startYear && startMonth<=monthIndex}">1</c:when>
																													<c:when test="${currentYear==endYear && monthIndex<=endMonth}">1</c:when>
																													<c:when test="${currentYear>startYear && currentYear<endYear}">1</c:when>
																													
																													<c:otherwise>0</c:otherwise>
																												</c:choose>
																											</c:when>
																											<c:otherwise>0</c:otherwise>
																										</c:choose>
																					           	   	</c:set>
																				           	   			<c:if test="${drawTD==1}">
																					           	   			<c:if test="${startYear==currentYear || currentYear==endYear}">
	                                                                                                        <div style="margin:2px;padding:2px;height:15px;text-align: center;background-color:${ampCalendarGraph.ampCalendar.eventType.color};text-align:center;"  onmouseover="stm([evnt,'name:${eventName}<br>stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])" onmouseout="htm()">
																						                   		<digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~method=preview~resetForm=true">
																						                    		<digi:img src="module/calendar/images/lookglass2.gif" border="0" alt="" align="left" style="vertical-align:middle"/>
																						                   		</digi:link>	
																						                   			<c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
																							                    		<span style="color: #ffffff"> ${ampCalendarEventItem.title}</span>
																							                    	</c:forEach>	
																						                   	</div>
	                                                                                                        </c:if>
	                                                                                                        <c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.recurrCalEvent}">
																							                   	<c:if test='${ampCalendarEventItem.typeofOccurrence == "Dailly"}'>
																							                   	 <c:set var="Month">${ampCalendarEventItem.recurrPeriod}</c:set>
																							        	           <c:forEach begin="1" end="${30/Month-1}">
																														<div style="margin:2px;padding:2px;text-align: center;background-color:${ampCalendarGraph.ampCalendar.eventType.color};height:15px;text-align:center;"  onmouseover="stm([evnt,'recurr:every ${ampCalendarEventItem.recurrPeriod} day <br>name:${eventName}<br>stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])" onmouseout="htm()">
																									                   		<digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~method=preview~resetForm=true">
																									                       		<digi:img src="module/calendar/images/lookglass2.gif" border="0" alt="" align="left" style="vertical-align:middle"/>
																									                   		</digi:link>
																									                   		<c:if test="${startYear==currentYear && startMonth==monthIndex}">
																							                   					<c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
																								                    				<span style="color: #ffffff"> ${ampCalendarEventItem.title}</span>
																								                    			</c:forEach>
																							                   				</c:if>
																									                   	</div>
																													</c:forEach>
																								              	</c:if>
	
	                                                                                                             <c:if test="${ampCalendarEventItem.typeofOccurrence == 'Yearly'}">
	
	                                                                                                                 <c:set var="monthDay">${ampCalendarEventItem.selectedStartMonth}</c:set>
	                                                                                                                     <c:if test="${calendarViewForm.baseDateBreakDown.year == recStartYear || calendarViewForm.baseDateBreakDown.year <= recEndYear && calendarViewForm.baseDateBreakDown.year >= recStartYear}">
	                                                                                                                      <div style="margin:2px;padding:2px;text-align: center;background-color:${ampCalendarGraph.ampCalendar.eventType.color};height:15px;text-align:center;"  onmouseover="stm([evnt,'recurr:every ${ampCalendarEventItem.recurrPeriod} day of ${monthDay} Month <br>name:${eventName}<br>stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])" onmouseout="htm()">
	                                                                                                                            <digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~method=preview~resetForm=true">
	                                                                                                                                <digi:img src="module/calendar/images/lookglass2.gif" border="0" alt="" align="left" style="vertical-align:middle"/>
	                                                                                                                            </digi:link>
	                                                                                                                            <c:if test="${startYear==currentYear && startMonth==monthIndex}">
	                                                                                                                                <c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
	                                                                                                                                    <span style="color: #ffffff"> ${ampCalendarEventItem.title}</span>
	                                                                                                                                </c:forEach>
	                                                                                                                            </c:if>
	                                                                                                                        </div>
	
	                                                                                                                       <c:if test=""></c:if>
	
	                                                                                                                   </c:if>
	                                                                                                             </c:if>
	                                                                                                        </c:forEach>
																				           	   			</c:if>
																								</c:forEach>
																						</td>
																					</tr>
																					<tr height="1px" bgcolor="#e8eef7">
						  																<td bgcolor="#e8eef7" colspan="2"/>
						  															</tr>
						  													</c:forEach>
						  												</c:forEach>
					  											</table>
					  										</div>
					  									</td>
					  								</tr>
					  							</table>
					  						</td>
					  					</tr>	
									</c:if>
								<!-- yearly view End   -->
				            	</feature:display>								
								
								
								<feature:display name="Weekly View" module="Calendar">
									<!-- Weekly view Start -->
									<c:if test="${calendarViewForm.view == 'weekly'}">
									<tr>
				                    	<td style="padding:20px;text-align:center">
				                        	<table align="center" style="min-width:700px;" width="100%">
				                            	<tr>
				                                	<td width="100%">
				                                    	<div style="border:2px solid #e8eef7;width: 100%">
				                                        	<table width="100%">
					                                        		<c:forEach var="row" items="${calendarViewForm.dateNavigator.items}">
					  													<c:forEach var="item" items="${row}">
					  														<c:if test="${calendarViewForm.view == 'weekly' && item.selected}">
																				<tr style="width: 100%;height: 25px">
																					<td align="left" vAlign="top" style="font-size:12px;color:White;background-color: #376091;font-family:Tahoma;border-bottom:1px solid #ffffff;width: 80px;height: 25px">
																					   	<span id="calenderSubFont" style="width:25px">
																					       	<digi:trn key="aim:dayOfWeek${item.dayOfWeek}">${item.dayOfWeek}</digi:trn>, &nbsp;
																					   	</span>
																					   	<a href="#" style="font-size:10px;color:White;font-family:Tahoma;" onclick="submitFilterForm('${calendarViewForm.view}', '${item.timestamp}');return(false);">
																					  		${item.dayOfMonth}<c:if test="${item.dayOfMonth>3}"><digi:trn key="calendar:dayPrefix">th</digi:trn></c:if>
																					   	</a>
																					</td>
																					<td style="border-top:2px solid #e8eef7;background-color: #ffffff">
																							<c:forEach var="ampCalendarGraph" items="${calendarViewForm.ampCalendarGraphs}">
																								<c:set var="startDay">${ampCalendarGraph.ampCalendar.calendarPK.startDay}</c:set>
																						        <c:set var="endDay">${ampCalendarGraph.ampCalendar.calendarPK.endDay}</c:set>
														                        				<c:set var="endMonth">${ampCalendarGraph.ampCalendar.calendarPK.endMonth+1}</c:set>
														                        				<c:set var="startMonth">${ampCalendarGraph.ampCalendar.calendarPK.startMonth+1}</c:set>
														                        				<c:set var="currentMonth">${calendarViewForm.baseDateBreakDown.month}</c:set>
														                        				<c:set var="iterationBeginIndex">
														                        					<c:if test="${(fn:length(ampCalendarGraph.graphItems)-1) <0}"> 0</c:if>
														                        					<c:if test="${(fn:length(ampCalendarGraph.graphItems)-1) >=0}"> ${fn:length(ampCalendarGraph.graphItems)-1}</c:if>
														                        				</c:set>
																			           			<c:set var="startYear">${ampCalendarGraph.ampCalendar.calendarPK.startYear}</c:set>
																			           			<c:set var="endYear">${ampCalendarGraph.ampCalendar.calendarPK.endYear}</c:set>
																			           			<c:set var="currentDay">${calendarViewForm.baseDateBreakDown.dayOfMonth}</c:set>
				                                                    							<c:set var="currentYear">${calendarViewForm.baseDateBreakDown.year}</c:set>
														                        				<c:set var="eventName">
																						        	<c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}" begin="${fn:length(ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem)-1}">
																						        		${ampCalendarEventItem.title}
																						        	</c:forEach>
																						        </c:set>
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

														                        				<c:set var="drawTD">
														                        					<c:choose>
														                        						<c:when test="${startMonth==currentMonth}">
														                        							<c:choose>
														                        								<c:when test="${endMonth==currentMonth && item.dayOfMonth >=startDay && item.dayOfMonth<=endDay && item.selected}">1</c:when>
																                        						<c:when test="${endMonth!=currentMonth && (item.dayOfMonth>=startDay || (item.dayOfMonth<startDay && item.dayOfMonth<endDay)) && item.selected}">1</c:when>
																                        						<c:otherwise>0</c:otherwise>
														                        							</c:choose>
														                        						</c:when>
														                        						<c:when test="${(startMonth!=currentMonth && currentMonth==endMonth)&&(item.dayOfMonth>=startDay || item.dayOfMonth<=endDay) && item.selected}">1</c:when>
														                        						<c:when test="${startMonth!=currentMonth && endMonth!=currentMonth}">1</c:when>
														                        						<c:otherwise>0</c:otherwise>
														                        					</c:choose>
														                        				</c:set>
                                                                                                  <c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.recurrCalEvent}">
                                                                                                   <c:if test='${ampCalendarEventItem.typeofOccurrence != "Dailly"}'>
                                                                                                    <c:if test="${drawTD==1}">
                                                                                                        <div style="margin:2px;padding:2px;height:15px;font-weight:Bold;text-align:center;color:Black;background-color:${ampCalendarGraph.ampCalendar.eventType.color};"onmouseover="stm([evnt,'name:${eventName}<br>stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])" onmouseout="htm()">
                                                                                                            <digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~~method=preview~resetForm=true">
                                                                                                                <digi:img src="module/calendar/images/lookglass2.gif" border="0" alt="" align="left"/>
                                                                                                            </digi:link>
                                                                                                            <c:if test="${startYear==currentYear && startMonth==currentMonth && startDay==item.dayOfMonth}">
																												<c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
                                                                                                                	<span style="color: #ffffff"> ${ampCalendarEventItem.title}</span>
                                                                                                            	</c:forEach>
																											</c:if>
                                                                                                        </div>
                                                                                                    </c:if>
                                                                                                </c:if>
                                                                                                <c:if test='${ampCalendarEventItem.typeofOccurrence == "Dailly"}'>
                                                                                                	<c:if test="${item.dayOfMonth%ampCalendarEventItem.recurrPeriod == 0}">
                                                                                                    	<c:if test="${drawTD==1}">
                                                                                                        	<div style="margin:2px;padding:2px;height:15px;font-weight:Bold;text-align:center;color:Black;background-color:${ampCalendarGraph.ampCalendar.eventType.color};"onmouseover="stm([evnt,'recurr:every ${ampCalendarEventItem.recurrPeriod} day <br>name:${eventName}<br>stDate:${eventStartDate}<br>endDate:${eventEndDate}'],Style[14])" onmouseout="htm()">
                                                                                                            	<digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~~method=preview~resetForm=true">
                                                                                                                	<digi:img src="module/calendar/images/lookglass2.gif" border="0" alt="" align="left"/>
                                                                                                                </digi:link>
																												<c:if test="${startYear==currentYear && startMonth==currentMonth && startDay==item.dayOfMonth}">
																													<c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
                                                                                                                		<span style="color: #ffffff"> ${ampCalendarEventItem.title}</span>
                                                                                                            		</c:forEach>
																												</c:if>
                                                                                                             </div>
														                              			        </c:if>
                                                                                                   </c:if>
                                                                                                </c:if>
                                                                                             </c:forEach>
                                                                                         </c:forEach>
																					</td>
																				</tr>
																				<tr height="2px" bgcolor="#e8eef7">
					  																<td bgcolor="#e8eef7" colspan="2"/>
					  															</tr>
																			</c:if>
					  													</c:forEach>
					  												</c:forEach>
				  											</table>
				  										</div>
				  									</td>
				  								</tr>
				  							</table>
				  						</td>
				  					</tr>
				  				</c:if>
				  				<!-- Weekly view End -->
								</feature:display>								
							</tr>
				         </table>
					</td>
				</tr>
	  		</table>
	  	</td>
 	</tr>
    </c:if>
</table>