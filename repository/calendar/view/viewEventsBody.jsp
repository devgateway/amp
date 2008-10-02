<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@page import="java.util.*"%>

<digi:instance property="calendarViewForm"/>
<table border="0" border-color="red" width="100%" cellspacing="" cellpadding="1" >
  <tr>
    <td >
      <table border="0" width="100%" height="40px">
        <tr >
          <c:if test="${calendarViewForm.view != 'custom'}">

            <td align="right" width="40%" vAlign="center">
              <digi:img src="module/calendar/images/calenderLeftArrow1.jpg"/>
              <a href="#"  onclick="submitFilterForm('${calendarViewForm.view}', '${calendarViewForm.dateNavigator.leftTimestamp}');return(false);"><digi:trn key="aim:last">Last</digi:trn></a>
            </td>
          </c:if>
          <td align="center" >
            <span id="calendarFont" style="font-size:14px;font-weight:bold;">
              <c:if test="${calendarViewForm.view == 'yearly'}">
              ${calendarViewForm.baseDateBreakDown.year}
              </c:if>
              <c:if test="${calendarViewForm.view == 'monthly'}">

                <digi:trn key="aim:calendar:basemonthNameLong:${calendarViewForm.baseDateBreakDown.monthNameLong}">${calendarViewForm.baseDateBreakDown.monthNameLong}</digi:trn>,&nbsp;
                ${calendarViewForm.baseDateBreakDown.year}

              </c:if>
              <c:if test="${calendarViewForm.view == 'weekly'}">

                <digi:trn key="aim:calendar:startmonthNameShort:${calendarViewForm.startDateBreakDown.monthNameShort}">${calendarViewForm.startDateBreakDown.monthNameShort}</digi:trn>
                ${calendarViewForm.startDateBreakDown.dayOfMonth},&nbsp;
                ${calendarViewForm.startDateBreakDown.year}&nbsp;-&nbsp;
                <digi:trn key="aim:calendar:endmonthNameShort:${calendarViewForm.endDateBreakDown.monthNameShort}">${calendarViewForm.endDateBreakDown.monthNameShort}</digi:trn>
                ${calendarViewForm.endDateBreakDown.dayOfMonth},&nbsp;
                ${calendarViewForm.endDateBreakDown.year}
              </c:if>
              <c:if test="${calendarViewForm.view == 'daily'}">
                <digi:trn key="aim:calendar:dailymonthNameLong:${calendarViewForm.baseDateBreakDown.monthNameLong}">${calendarViewForm.baseDateBreakDown.monthNameLong}</digi:trn>
                ${calendarViewForm.baseDateBreakDown.dayOfMonth},&nbsp;
                ${calendarViewForm.baseDateBreakDown.year}
              </c:if>
              <c:if test="${calendarViewForm.view == 'custom'}">
                <digi:trn key="aim:calendar:startmonthNameLong:${calendarViewForm.startDateBreakDown.monthNameLong}">${calendarViewForm.startDateBreakDown.monthNameLong}</digi:trn>
                ${calendarViewForm.startDateBreakDown.dayOfMonth},&nbsp;
                ${calendarViewForm.startDateBreakDown.year}&nbsp;-&nbsp;
                <digi:trn key="aim:calendar:endmonthNameLong:${calendarViewForm.endDateBreakDown.monthNameLong}">${calendarViewForm.endDateBreakDown.monthNameLong}</digi:trn>
                ${calendarViewForm.endDateBreakDown.dayOfMonth},&nbsp;
                ${calendarViewForm.endDateBreakDown.year}
              </c:if>
            </span>
          </td>
          <c:if test="${calendarViewForm.view != 'custom'}">
            <td align="left" width="40%">
              <a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${calendarViewForm.dateNavigator.rightTimestamp}');return(false);"><digi:trn key="aim:next">Next</digi:trn></a>	<digi:img src="module/calendar/images/calenderRightArrow1.jpg"/>
            </td>
          </c:if>
        </tr>
      </table>
    </td>
  </tr>
  <tr width="100%" height="3px" bgcolor=#ffffff>

    <td  >
    </td>
  </tr>
  <tr width="98%">
    <td align="center" vAlign="center">


      <table border="0" width="100%" cellpadding="1" cellspacing="1" ailgn="center">
          <c:if test="${calendarViewForm.view != 'custom'}">
            <c:if test="${calendarViewForm.view == 'monthly'}">
              <tr width="99%" align="center" vAlign="center">
                <td>
                  <table width="99%" border="0" align="center" >
                    <tr width="100%">
                      <td valign="left"><digi:trn key="aim:m">M</digi:trn></td>
                      <td valign="left"><digi:trn key="aim:t">T</digi:trn></td>
                      <td valign="left"><digi:trn key="aim:w">W</digi:trn></td>
                      <td valign="left"><digi:trn key="aim:t">T</digi:trn></td>
                      <td valign="left"><digi:trn key="aim:f">F</digi:trn></td>
                      <td valign="left"><digi:trn key="aim:s">S</digi:trn></td>
                      <td valign="left"><digi:trn key="aim:s">S</digi:trn></td>
                    </tr>
                    <c:forEach var="row" items="${calendarViewForm.dateNavigator.items}">
                      <tr vAlign="center" bgcolor="#ffffff">
                        <c:forEach var="item" items="${row}" >
                          <td align="top" vAlign="top">
                            <c:if test="${!item.enabled}"><span style="color:#cbcbcb"></c:if>
                            ${item.dayOfMonth}
                            <c:if test="${!item.enabled}"></span></c:if>
                          </td>
                        </c:forEach>
                      </tr>
                      <c:forEach var="ampCalendarGraph" items="${calendarViewForm.ampCalendarGraphs}">
                        <c:set var="startDay">
                        ${ampCalendarGraph.ampCalendar.calendarPK.startDay}
                        </c:set>
                        <c:set var="endDay">
                        ${ampCalendarGraph.ampCalendar.calendarPK.endDay}
                        </c:set>
                        <c:set var="endMonth">
                        ${ampCalendarGraph.ampCalendar.calendarPK.endMonth+1}
                        </c:set>
                        <c:set var="startMonth">
                        ${ampCalendarGraph.ampCalendar.calendarPK.startMonth+1}
                        </c:set>
                        <c:set var="currentMonth">
                        ${calendarViewForm.baseDateBreakDown.month}
                        </c:set>
                        <tr vAlign="center" bgcolor="#ffffff">
                          <c:forEach var="item" items="${row}" >
                            <td align="top" vAlign="top" width="14%" style="padding:6px;" >

                              <c:if test="${startMonth==currentMonth}">
                                <c:if test="${item.dayOfMonth==startDay&&item.enabled}">
                                  <digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~method=preview~resetForm=true">
                                    <c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
                                    ${ampCalendarEventItem.title}-VVV1
                                    </c:forEach>
                                  </digi:link>
                                </c:if>
                              </c:if>

                              <c:if test="${startMonth!=currentMonth&&currentMonth==endMonth}">
                                <c:if test="${item.dayOfMonth==startDay&&!item.enabled}">
                                  <digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~method=preview~resetForm=true">
                                    <c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
                                    ${ampCalendarEventItem.title}-VVV2
                                    </c:forEach>
                                  </digi:link>
                                </c:if>
                              </c:if>



                              <c:if test="${startMonth!=currentMonth&&endMonth!=currentMonth}">
                                <c:forEach var="ampCalendarGraphItem" items="${ampCalendarGraph.graphItems}">
                                  <digi:img src="module/calendar/images/spacer.gif" style="width:100%;height:7px;background-color:${ampCalendarGraphItem.color}"/>
                                </c:forEach>
                              </c:if>

                              <c:if test="${startMonth==currentMonth&&endMonth==currentMonth}">
                                <c:if test="${item.dayOfMonth>startDay && item.dayOfMonth<=endDay && item.enabled}">
                                  <c:forEach var="ampCalendarGraphItem" items="${ampCalendarGraph.graphItems}">
                                    <digi:img src="module/calendar/images/spacer.gif"  style="width:100%;height:7px;background-color:${ampCalendarGraphItem.color}"/>
                                  </c:forEach>
                                </c:if>
                              </c:if>

                              <c:if test="${startMonth==currentMonth&&endMonth!=currentMonth}">
                                <c:if test="${item.dayOfMonth>startDay&&item.enabled}">
                                  <c:forEach var="ampCalendarGraphItem" items="${ampCalendarGraph.graphItems}">
                                    <digi:img src="module/calendar/images/spacer.gif"  style="width:100%;height:7px;background-color:${ampCalendarGraphItem.color}"/>
                                  </c:forEach>
                                </c:if>
                                <c:if test="${item.dayOfMonth<endDay && !item.enabled}">
                                  <c:forEach var="ampCalendarGraphItem" items="${ampCalendarGraph.graphItems}">
                                    <digi:img src="module/calendar/images/spacer.gif"  style="width:100%;height:7px;background-color:${ampCalendarGraphItem.color}"/>
                                  </c:forEach>
                                </c:if>
                              </c:if>

                              <c:if test="${startMonth!=currentMonth && endMonth==currentMonth}">
                                <c:if test="${item.dayOfMonth>startDay&&!item.enabled}">
                                  <c:forEach var="ampCalendarGraphItem" items="${ampCalendarGraph.graphItems}">
                                    <digi:img src="module/calendar/images/spacer.gif"  style="width:100%;height:7px;background-color:${ampCalendarGraphItem.color}"/>
                                  </c:forEach>
                                </c:if>
                                <c:if test="${item.dayOfMonth<=endDay&&item.enabled}">
                                  <c:forEach var="ampCalendarGraphItem" items="${ampCalendarGraph.graphItems}">
                                    <digi:img src="module/calendar/images/spacer.gif"  style="width:100%;height:7px;background-color:${ampCalendarGraphItem.color}"/>
                                  </c:forEach>
                                </c:if>
                              </c:if>



                            </td>

                          </c:forEach>

                        </tr>


                      </c:forEach>





                      <tr height="1px">
                        <td colspan="7" >
                        </td>
                      </tr>
                    </c:forEach>
                  </table>
    </td>
              </tr>
                            </c:if>
                            <c:if test="${calendarViewForm.view == 'daily'}">
                              <tr>
                                <td style="padding:30px;text-align:center;">
                                  <table align="center" style="min-width:700px;">
                                    <tr>
                                      <td>
                                        <div style="overflow:auto;height:500px;border:2px solid #CCECFF;">
                                          <table width="100%">
                                            <c:forEach var="hour" begin="0" end="24">
                                              <tr style="height:40px;">
                                                <td align="left" style="border-top:1px solid #CCECFF;color:White;background-color:#7B9EBD;vertical-align:top;width:70px;padding:6px;font-size:12px;font-weight:bold;">
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
                                                      ${hoursToDisplay} <digi:trn key="aim:am">PM</digi:trn>
                                                    </c:if>
                                                  </c:if>
                                                </td>
                                                <td style="border-top:1px solid #CCECFF;">
                                                  <c:forEach var="ampCalendarGraph" items="${calendarViewForm.ampCalendarGraphs}">
                                                    <c:set var="startHours">
                                                    ${ampCalendarGraph.ampCalendar.calendarPK.startHour}
                                                    </c:set>
                                                    <c:if test="${hour==startHours}">
                                                      <div style="margin:2px;padding:2px;font-weight:Bold;text-align:center;color:Black;border:1px solid Black;background-color:${ampCalendarGraph.ampCalendar.eventType.color};">
                                                        <c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
                                                        ${ampCalendarEventItem.title}
                                                        </c:forEach>
                                                        <%--
                                                         <digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~~method=preview~resetForm=true">

                                                        </digi:link>--%>
                                                      </div>
                                                    </c:if>
                                                  </c:forEach>
                                                  &nbsp;
                                                </td>
                                              </tr>
                                            </c:forEach>
                                            <tr>
                                              <td style="border-top:1px solid #CCECFF;color:White;">
                                              &nbsp;
                                              </td>
                                              <td style="border-top:1px solid #CCECFF;color:White;">
                                              &nbsp;
                                              </td>
                                            </tr>
                                          </table>
                                        </div>
                                      </td>
                                    </tr>
                                  </table>
                                </td>
                              </tr>
                            </c:if>
  </tr>
            </c:if>


              <td style="font-size:14px;padding:4px;font-weight:Bold;background-color:#7B9EBD;color:White;" align="center" width="140" <c:if test="${calendarViewForm.view == 'monthly'}">rowspan="2"</c:if>>
              <c:if test="${calendarViewForm.view != 'daily' && calendarViewForm.view != 'monthly'}">
                <digi:trn key="calendar:EventName">Event Name</digi:trn>
              </c:if>
</td>
<c:forEach var="row" items="${calendarViewForm.dateNavigator.items}">

  <c:forEach var="item" items="${row}">


    <c:choose>
      <c:when test="${calendarViewForm.view == 'yearly'}">
        <td align="center" style="padding:4px;font-weight:Bold;background-color:#7B9EBD;color:White;width:7%;border-left:1px solid #CCECFF;" <c:if test="${item.nolink}">bgcolor="#ffbebe"</c:if>>
        <a href="#" style="text-decoration:none;color:White;font-size:14px;" onclick="submitFilterForm('${calendarViewForm.view}', '${item.timestamp}');return(false);"><digi:trn key="aim:calendar${item.month}">${item.month}</digi:trn></a>

</td>
      </c:when>
      <%-- <c:when test="${calendarViewForm.view == 'monthly'}">
        <td align="center" style="border-left:1px solid" <c:if test="${item.nolink}">bgcolor="#ffbebe"</c:if>>
        <a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${item.timestamp}');return(false);"><digi:trn key="aim:calendar:day:${item.dayOfWeek}">${item.dayOfWeek}</digi:trn></a>
</td>
</c:when>--%>

<c:when test="${calendarViewForm.view == 'weekly' && item.selected}">
  <td>
    <table  width="98%" cellspacing="1" cellpadding="2" border="0" bgcolor="#ffffff">
      <tr>
        <td align="left" vAlign="top">
          <span id="calenderSubFont">

            <digi:trn key="aim:dayOfWeek${item.dayOfWeek}">${item.dayOfWeek}</digi:trn>

          </span>
        </td>
        <td align="center" <c:if test="${item.nolink}">bgcolor="#ffbebe"</c:if>>
        <a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${item.timestamp}');return(false);">${item.dayOfMonth}/<digi:trn key="calendar:${item.month}">${item.month}</digi:trn>/${calendarViewForm.baseDateBreakDown.year}</a>
  </td>
      </tr>
    </table>
</td>
</c:when>
    </c:choose>
  </c:forEach>
</c:forEach>

<c:if test="${calendarViewForm.view == 'custom'}">
  <td align="center" width="80"><digi:trn key="calendar:BodyEventTypes">Event Types</digi:trn></td>
  <td align="center" width="80"><digi:trn key="calendar:BodyDonors">Donors</digi:trn></td>
  <td align="center" width="80"><digi:trn key="calendar:BodyAttendees">Attendees</digi:trn></td>
  <td>
    <table width="100%" cellspacing="0" cellpadding="0">
      <td width="25%" align="center"><digi:trn key="aim:calendar:Q1">Q1</digi:trn></td>
      <td width="25%" align="center"><digi:trn key="aim:calendar:Q2">Q2</digi:trn></td>
      <td width="25%" align="center"><digi:trn key="aim:calendar:Q3">Q3</digi:trn></td>
      <td width="25%" align="center"><digi:trn key="aim:calendar:Q4">Q4</digi:trn></td>
    </table>
  </td>
</c:if>
            </tr>
            <c:if test="${calendarViewForm.view != 'daily'&&calendarViewForm.view != 'monthly'}">
              <c:forEach var="ampCalendarGraph" items="${calendarViewForm.ampCalendarGraphs}">
                <tr>
                  <td align="center" style="border-top:solid 1px #CCECFF;padding:6px;" width="80">
                    <div style="width:140;overflow:hidden;">
                      <digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}~method=preview~resetForm=true">
                        <c:forEach var="ampCalendarEventItem" items="${ampCalendarGraph.ampCalendar.calendarPK.calendar.calendarItem}">
                        ${ampCalendarEventItem.title}
                        </c:forEach>
                      </digi:link>
                    </div>
                  </td>
                  <c:if test="${calendarViewForm.view == 'custom'}">
                    <td align="center" style="border-top:solid 1px #CCECFF;" width="80">
                    ${ampCalendarGraph.ampCalendar.eventType.name}
                    </td>
                    <td align="center" style="border-top:1px solid">
                      <select style="width:80">
                        <c:if test="${calendarViewForm.view == 'custom'}">
                          <c:forEach var="organization" items="${ampCalendarGraph.ampCalendar.organisations}">
                            <option>${organization.name}</option>
                          </c:forEach>
                        </c:if>
                      </select>
                    </td>
                    <td align="center" style="border-top:solid 1px #CCECFF;" width="80">
                      <select style="width:80">
                        <c:forEach var="attendee" items="${ampCalendarGraph.ampCalendar.attendees}">
                          <c:if test="${!empty attendee.member}">
                            <option>${attendee.member.user.firstNames} ${attendee.member.user.lastName}</option>
                          </c:if>
                          <c:if test="${!empty attendee.team}">
                            <option>${attendee.team.name}</option>
                          </c:if>
                          <c:if test="${!empty attendee.guest}">
                            <option>${attendee.guest}</option>
                          </c:if>
                        </c:forEach>
                      </select>
                    </td>
                    <td align="center" style="border-top:solid 1px #CCECFF;">
                      <table width="100%" cellspacing="0" cellpadding="0" border="0">
                        <tr>
                  </c:if>

                  <c:forEach var="ampCalendarGraphItem" items="${ampCalendarGraph.graphItems}">
                    <td align="center" valign="middle" <c:if test="${calendarViewForm.view == 'custom'}">width="25%"</c:if><c:if test="${calendarViewForm.view != 'custom'}">style="border-top:1px solid #CCECFF;"</c:if>>
                    <table cellpadding="0" cellspacing="0" width="100%" border="0">
                      <tr>
                        <c:if test="${ampCalendarGraphItem.left > 0}">
                          <td width="${ampCalendarGraphItem.left}%"><digi:img src="module/calendar/images/spacer.gif"/></td>
                        </c:if>
                        <c:if test="${ampCalendarGraphItem.center > 0}">
                          <td width="${ampCalendarGraphItem.center}%"><digi:img src="module/calendar/images/spacer.gif" style="width:100%;height:7px;background-color:${ampCalendarGraphItem.color};"/></td>
                        </c:if>
                        <c:if test="${ampCalendarGraphItem.right > 0}">
                          <td width="${ampCalendarGraphItem.right}%"><digi:img src="module/calendar/images/spacer.gif"/></td>
                        </c:if>
                      </tr>
                    </table>
</td>
                  </c:forEach>

                  <c:if test="${calendarViewForm.view == 'custom'}">
                        </tr></table></td>
                  </c:if>
                </tr>
              </c:forEach>
</c:if>
                      </table>
</td>
</tr>
      </table>
