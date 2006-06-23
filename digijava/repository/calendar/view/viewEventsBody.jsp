<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="calendarViewForm"/>

<table border="0">
    <tr>
        <c:if test="${calendarViewForm.view != 'custom'}">
            <td>
                <a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${calendarViewForm.dateNavigator.leftTimestamp}');return(false);">&lt;</a>
            </td>
        </c:if>
        <td align="center">
        <logic:equal name="calendarViewForm" property="view" value="yearly">
            <b>${calendarViewForm.baseDateBreakDown.year}</b>
        </logic:equal>
        <logic:equal name="calendarViewForm" property="view" value="monthly">
            <b>
                ${calendarViewForm.baseDateBreakDown.monthNameLong},&nbsp;
                ${calendarViewForm.baseDateBreakDown.year}
            </b>
        </logic:equal>
        <logic:equal name="calendarViewForm" property="view" value="weekly">
            <b>
                ${calendarViewForm.startDateBreakDown.monthNameShort}&nbsp;
                ${calendarViewForm.startDateBreakDown.dayOfMonth},&nbsp;
                ${calendarViewForm.startDateBreakDown.year}&nbsp;-&nbsp;
                ${calendarViewForm.endDateBreakDown.monthNameShort}&nbsp;
                ${calendarViewForm.endDateBreakDown.dayOfMonth},&nbsp;
                ${calendarViewForm.endDateBreakDown.year}
            </b>
        </logic:equal>
        <logic:equal name="calendarViewForm" property="view" value="daily">
            <b>
                ${calendarViewForm.baseDateBreakDown.monthNameLong}&nbsp;
                ${calendarViewForm.baseDateBreakDown.dayOfMonth},&nbsp;
                ${calendarViewForm.baseDateBreakDown.year}
            </b>
        </logic:equal>
        <logic:equal name="calendarViewForm" property="view" value="custom">
            <b>
                ${calendarViewForm.startDateBreakDown.monthNameLong}&nbsp;
                ${calendarViewForm.startDateBreakDown.dayOfMonth},&nbsp;
                ${calendarViewForm.startDateBreakDown.year}&nbsp;-&nbsp;
                ${calendarViewForm.endDateBreakDown.monthNameLong}&nbsp;
                ${calendarViewForm.endDateBreakDown.dayOfMonth},&nbsp;
                ${calendarViewForm.endDateBreakDown.year}
            </b>
        </logic:equal>
        </td>
        <c:if test="${calendarViewForm.view != 'custom'}">
            <td>
                <a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${calendarViewForm.dateNavigator.rightTimestamp}');return(false);">&gt;</a>
            </td>
        </c:if>
    </tr>
</table>
<table border="0" width="100%" cellpadding="0" cellspacing="0" style="border:2px solid">
    <tr>
        <td align="center" width="140" <c:if test="${calendarViewForm.view == 'monthly'}">rowspan="2"</c:if>>Event Name</td>
        <c:if test="${calendarViewForm.view != 'custom'}">
            <c:if test="${calendarViewForm.view == 'monthly'}">
                <logic:iterate id="row" name="calendarViewForm" property="dateNavigator.items">
                    <c:set var="firstItem" value="true"/>
                    <logic:iterate id="item" name="row">
                        <c:if test="${firstItem}">
                            <td colspan="7" style="border-left:1px solid;border-bottom:1px solid">&nbsp;${item.month}&nbsp;${item.dayOfMonth}</td>
                        </c:if>
                        <c:set var="firstItem" value="false"/>
                    </logic:iterate>
                </logic:iterate>
                </tr><tr>
            </c:if>
            <logic:iterate id="row" name="calendarViewForm" property="dateNavigator.items">
                <logic:iterate id="item" name="row">
                    <c:choose>
                        <c:when test="${calendarViewForm.view == 'yearly'}">
                            <td align="center" style="border-left:2px solid" <c:if test="${item.nolink}">bgcolor="#ffbebe"</c:if>>
                                <a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${item.timestamp}');return(false);">${item.month}</a>
                            </td>
                        </c:when>
                        <c:when test="${calendarViewForm.view == 'monthly'}">
                            <td align="center" style="border-left:1px solid" <c:if test="${item.nolink}">bgcolor="#ffbebe"</c:if>>
                                <a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${item.timestamp}');return(false);">${item.dayOfWeek}</a>
                            </td>
                        </c:when>
                        <c:when test="${calendarViewForm.view == 'weekly' && item.selected}">
                            <td align="center" style="border-left:1px solid" <c:if test="${item.nolink}">bgcolor="#ffbebe"</c:if>>
                                <a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${item.timestamp}');return(false);">${item.dayOfMonth}</a>
                            </td>
                        </c:when>
                    </c:choose>
                </logic:iterate>
            </logic:iterate>
            <c:if test="${calendarViewForm.view == 'daily'}">
                <c:forEach var="hour" begin="0" end="23">
                    <td align="center" style="border-left:1px solid">
                        <c:if test="${hour < 10}">
                            <c:set var="hour" value="0${hour}"/>
                        </c:if>
                        ${hour}
                    </td>
                </c:forEach>
            </c:if>
        </c:if>
        <c:if test="${calendarViewForm.view == 'custom'}">
            <td align="center" width="80">Event Type</td>
            <td align="center" width="80">Donors</td>
            <td align="center" width="80">Attendees</td>
            <td>
                <table width="100%" cellspacing="0" cellpadding="0">
                    <td width="25%" align="center">Q1</td>
                    <td width="25%" align="center">Q2</td>
                    <td width="25%" align="center">Q3</td>
                    <td width="25%" align="center">Q4</td>
                </table>
            </td>
        </c:if>
    </tr>
    <logic:iterate id="ampCalendarGraph" name="calendarViewForm" property="ampCalendarGraphs">
        <tr>
            <td align="center" style="border-top:2px solid;" width="80">
                <div style="width:140;overflow:hidden">
                    <digi:link href="/showCalendarEvent.do~ampCalendarId=${ampCalendarGraph.ampCalendar.calendarPK.calendar.id}">
                        <logic:iterate id="ampCalendarEventItem" name="ampCalendarGraph" property="ampCalendar.calendarPK.calendar.calendarItem">
                            ${ampCalendarEventItem.title}
                        </logic:iterate>
                    </digi:link>
                </div>
            </td>
            <c:if test="${calendarViewForm.view == 'custom'}">
                <td align="center" style="border-top:1px solid" width="80">
                    ${ampCalendarGraph.ampCalendar.eventType.name}
                </td>
                <td align="center" style="border-top:1px solid">
                    <select style="width:80">
                        <logic:iterate id="donor" name="ampCalendarGraph" property="ampCalendar.donors">
                            <option>${donor.name}</option>
                        </logic:iterate>
                    </select>
                </td>
                <td align="center" style="border-top:1px solid" width="80">
                    <select style="width:80">
                        <logic:iterate id="attendee" name="ampCalendarGraph" property="ampCalendar.attendees">
                            <logic:notEmpty name="attendee" property="guest">
                                <option>${attendee.guest}</option>
                            </logic:notEmpty>
                            <logic:notEmpty name="attendee" property="user">
                                <option>${attendee.user.firstNames}&nbsp;${attendee.user.lastName}</option>
                            </logic:notEmpty>
                        </logic:iterate>
                    </select>
                </td>
                <td align="center" style="border-top:1px solid">
                    <table width="100%" cellspacing="0" cellpadding="0" border="0">
                        <tr>
            </c:if>
            <logic:iterate id="ampCalendarGraphItem" name="ampCalendarGraph" property="graphItems">
                <td align="center" valign="middle" <c:if test="${calendarViewForm.view == 'custom'}">width="25%"</c:if><c:if test="${calendarViewForm.view != 'custom'}">style="border-top:2px solid;"</c:if>>
                    <table cellpadding="0" cellspacing="0" width="100%" border="0">
                        <tr>
                            <c:if test="${ampCalendarGraphItem.left > 0}">
                                <td width="${ampCalendarGraphItem.left}%"><digi:img src="module/calendar/images/spacer.gif"/></td>
                            </c:if>
                            <c:if test="${ampCalendarGraphItem.center > 0}">
                                <td width="${ampCalendarGraphItem.center}%"><digi:img src="module/calendar/images/spacer.gif" style="width:100%;height:5px;background-color:#${ampCalendarGraphItem.color}"/></td>
                            </c:if>
                            <c:if test="${ampCalendarGraphItem.right > 0}">
                                <td width="${ampCalendarGraphItem.right}%"><digi:img src="module/calendar/images/spacer.gif"/></td>
                            </c:if>
                        </tr>
                    </table>
                </td>
            </logic:iterate>
            <c:if test="${calendarViewForm.view == 'custom'}">
                </tr></table></td>
            </c:if>
        </tr>
    </logic:iterate>
</table>
