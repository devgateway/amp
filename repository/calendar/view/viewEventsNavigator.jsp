<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="calendarViewForm"/>

<script type="text/javascript" src="<digi:file src="module/calendar/js/main.js"/>"></script>
<script type="text/javascript">
function selectCalendarType(view, type) {
    var form = document.getElementById('filterForm');
    if (form != null) {
        if (view == 'custom') {
            form.customViewStartDate.value = null;
            form.customViewEndDate.value = null;
        }
        form.view.value = view;
        form.submit();
    }
}
</script>

<table border="0" width="100%" style="border:1px solid; border-color: #484846;">
    <tr>
        <td nowrap="nowrap">
            <table border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td nowrap="nowrap"><digi:trn key="calendar:CalendarType">&nbsp;Calendar Type&nbsp;&nbsp;</digi:trn></td>
                    <td>
                        <html:select name="calendarViewForm" property="selectedCalendarType" onchange="selectCalendarType('${calendarViewForm.view}', '${calendarViewForm.selectedCalendarType}')">
                            <bean:define id="types" name="calendarViewForm" property="calendarTypes" type="java.util.List"/>
                            <html:options collection="types" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<c:if test="${calendarViewForm.view == 'custom'}">
    <table border="0" width="100%" style="border:1px solid; border-color: #484846;">
        <tr>
            <td nowrap="nowrap">
                <table border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td nowrap="nowrap"><digi:trn key="calendar:From">&nbsp;From&nbsp;&nbsp;</digi:trn></td>
                        <c:if test="${calendarViewForm.selectedCalendarType == 0}">
                            <script type="text/javascript" src="/thirdparty/CalendarPopup/PopupWindow.js"></script>
                            <script type="text/javascript" src="/thirdparty/CalendarPopup/AnchorPosition.js"></script>
                            <script type="text/javascript" src="/thirdparty/CalendarPopup/CalendarPopup.js"></script>
                            <script type="text/javascript" src="/thirdparty/CalendarPopup/date.js"></script>
                            <script type="text/javascript">
                                var startDateCalendar = new CalendarPopup();
                                startDateCalendar.setWeekStartDay(1);
                            </script>
                            <td nowrap="nowrap">
                                <table cellpadding="0" cellspacing="0">
                                    <td nowrap="nowrap"><html:text styleId="customViewStartDate" name="calendarViewForm" property="customViewStartDate" style="width:80px"/></td>
                                    <td nowrap="nowrap">&nbsp;<img id="startDateImg" name="startDateImg" src="/thirdparty/CalendarPopup/img/cal.gif" alt="STARTDATE" onclick="startDateCalendar.select(document.getElementById('customViewStartDate'), 'startDateImg', 'dd/MM/yyyy');return false;"></td>
                                </table>
                            </td>
                        </c:if>
                        <c:if test="${calendarViewForm.selectedCalendarType != 0}">
                            <html:hidden styleId="customViewStartDate" name="calendarViewForm" property="customViewStartDate"/>
                            <td nowrap="nowrap">
                                <select id="customViewStartYear" onchange="updateDate(document.getElementById('customViewStartDate'), 'year', this.value)"></select>
                                <script type="text/javascript">
                                createYearCombo(document.getElementById('customViewStartYear'), document.getElementById('customViewStartDate').value);
                                </script>
                                <select id="customViewStartMonth" onchange="updateDate(document.getElementById('customViewStartDate'), 'month', this.value)">
                                    <c:forEach var="i" begin="1" end="13">
                                        <bean:define id="index" value="${i - 1}"/>
                                        <bean:define id="type" value="${calendarViewForm.selectedCalendarType}"/>
                                        <c:if test="${i < 10}"><c:set var="i" value="0${i}"/></c:if>
                                        <option value="${i}"/>
                                            <%=org.digijava.module.calendar.entity.DateBreakDown.getMonthName(Integer.parseInt(index), Integer.parseInt(type), false)%>
                                        </option>
                                    </c:forEach>
                                </select>
                                <script type="text/javascript">
                                selectOptionByValue(document.getElementById('customViewStartMonth'), get('month', document.getElementById('customViewStartDate').value));
                                </script>
                                <select id="customViewStartDay" onchange="updateDate(document.getElementById('customViewStartDate'), 'day', this.value)">
                                    <c:forEach var="i" begin="1" end="30">
                                        <c:if test="${i < 10}"><c:set var="i" value="0${i}"/></c:if>
                                        <option value="${i}"/>${i}</option>
                                    </c:forEach>
                                </select>
                                <script type="text/javascript">
                                selectOptionByValue(document.getElementById('customViewStartDay'), get('day', document.getElementById('customViewStartDate').value));
                                </script>
                            </td>
                        </c:if>
                    </tr>
                    <tr>
                        <td nowrap="nowrap">&nbsp;<digi:trn key="aim:calendar:to">To</digi:trn>&nbsp;&nbsp;</td>
                        <c:if test="${calendarViewForm.selectedCalendarType == 0}">
                            <script type="text/javascript">
                                var endDateCalendar = new CalendarPopup();
                                startDateCalendar.setWeekStartDay(1);
                            </script>
                            <td nowrap="nowrap">
                                <table cellpadding="0" cellspacing="0">
                                    <td nowrap="nowrap"><html:text styleId="customViewEndDate" name="calendarViewForm" property="customViewEndDate" style="width:80px"/></td>
                                    <td nowrap="nowrap">&nbsp;<img id="endDateImg" name="endDateImg" src="/thirdparty/CalendarPopup/img/cal.gif" alt="ENDDATE" onclick="startDateCalendar.select(document.getElementById('customViewEndDate'), 'endDateImg', 'dd/MM/yyyy');return false;"></td>
                                </table>
                            </td>
                        </c:if>
                        <c:if test="${calendarViewForm.selectedCalendarType != 0}">
                            <html:hidden styleId="customViewEndDate" name="calendarViewForm" property="customViewEndDate"/>
                            <td nowrap="nowrap">
                                <select id="customViewEndYear" onchange="updateDate(document.getElementById('customViewEndDate'), 'year', this.value)"></select>
                                <script type="text/javascript">
                                createYearCombo(document.getElementById('customViewEndYear'), document.getElementById('customViewEndDate').value);
                                </script>
                                <select id="customViewEndMonth" onchange="updateDate(document.getElementById('customViewEndDate'), 'month', this.value)">
                                    <c:forEach var="i" begin="1" end="13">
                                        <bean:define id="index" value="${i - 1}"/>
                                        <bean:define id="type" value="${calendarViewForm.selectedCalendarType}"/>
                                        <c:if test="${i < 10}"><c:set var="i" value="0${i}"/></c:if>
                                        <option value="${i}"/>
                                            <%=org.digijava.module.calendar.entity.DateBreakDown.getMonthName(Integer.parseInt(index), Integer.parseInt(type), false)%>
                                        </option>
                                    </c:forEach>
                                </select>
                                <script type="text/javascript">
                                selectOptionByValue(document.getElementById('customViewEndMonth'), get('month', document.getElementById('customViewEndDate').value));
                                </script>
                                <select id="customViewEndDay" onchange="updateDate(document.getElementById('customViewEndDate'), 'day', this.value)">
                                    <c:forEach var="i" begin="1" end="30">
                                        <c:if test="${i < 10}"><c:set var="i" value="0${i}"/></c:if>
                                        <option value="${i}"/>${i}</option>
                                    </c:forEach>
                                </select>
                                <script type="text/javascript">
                                selectOptionByValue(document.getElementById('customViewEndDay'), get('day', document.getElementById('customViewEndDate').value));
                                </script>
                            </td>
                        </c:if>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</c:if>
<c:if test="${calendarViewForm.view != 'custom'}">
    <table border="0" width="100%" style="BORDER-LEFT: #484846 1px solid; BORDER-RIGHT: #484846 1px solid;">
        <tr>
            <td>
                <a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${calendarViewForm.dateNavigator.leftTimestamp}');return(false);">&lt;</a>
            </td>
            <td align="center">
                <c:if test="${calendarViewForm.view == 'yearly'}">
                    ${calendarViewForm.baseDateBreakDown.year - 1}
                    &nbsp;
                    <b>${calendarViewForm.baseDateBreakDown.year}</b>
                    &nbsp;
                    ${calendarViewForm.baseDateBreakDown.year + 1}
                </c:if>
                <c:if test="${calendarViewForm.view == 'monthly'}">
                    <b>

						<digi:trn key="aim:calendar:basemonthNameLong:${calendarViewForm.baseDateBreakDown.monthNameLong}">${calendarViewForm.baseDateBreakDown.monthNameLong}</digi:trn>
                        ${calendarViewForm.baseDateBreakDown.year}
                    </b>
                </c:if>
                <c:if test="${calendarViewForm.view == 'weekly'}">
                    <b>
                    <digi:trn key="aim:calendar:weeklyMonthNameLong:${calendarViewForm.baseDateBreakDown.monthNameLong}">${calendarViewForm.baseDateBreakDown.monthNameLong}</digi:trn>
                        ${calendarViewForm.baseDateBreakDown.year}
                    </b>
                </c:if>
                <c:if test="${calendarViewForm.view == 'daily'}">
                    <b>
                      <digi:trn key="aim:calendar:dayMonthNameLong:${calendarViewForm.baseDateBreakDown.monthNameLong}">${calendarViewForm.baseDateBreakDown.monthNameLong}</digi:trn>
                        ${calendarViewForm.baseDateBreakDown.dayOfMonth},&nbsp;
                        ${calendarViewForm.baseDateBreakDown.year}
                    </b>
                </c:if>
            </td>
            <td>
                <a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${calendarViewForm.dateNavigator.rightTimestamp}');return(false);">&gt;</a>
            </td>
        </tr>
    </table>
    <table border="0" width="100%" style="border:1px solid; border-color: #484846; border-bottom:none">
        <logic:iterate id="row" name="calendarViewForm" property="dateNavigator.items">
            <tr>
                <logic:iterate id="item" name="row">
                    <td align="center" <c:if test="${item.nolink}">bgcolor="#ffbebe"</c:if> <c:if test="${item.selected}">style="border:1px solid"</c:if>>
                        <c:if test="${!item.nolink}">
                            <a href="#" style="text-decoration:none" onclick="submitFilterForm('${calendarViewForm.view}', '${item.timestamp}'); return(false);">
                        </c:if>
                        <c:if test="${!item.enabled}"><span style="color:#cbcbcb"></c:if>
                        <c:choose>
                            <c:when test="${calendarViewForm.view == 'yearly'}"><digi:trn key="calendar:cal${item.month}">${item.month}</digi:trn></c:when>
                            <c:otherwise>${item.dayOfMonth}</c:otherwise>
                        </c:choose>
                        <c:if test="${!item.enabled}"></span></c:if>
                        <c:if test="${!item.nolink}">
                            </a>
                        </c:if>
                    </td>
                </logic:iterate>
            </tr>
        </logic:iterate>
    </table>
</c:if>
<table border="0" width="100%" style="border:1px solid; border-color: #484846;">
    <tr>
        <td>
           <digi:trn key="calendar:navToday"> &nbsp;Today&nbsp;is:&nbsp;</digi:trn>
            ${calendarViewForm.currentDateBreakDown.dayOfMonth}/${calendarViewForm.currentDateBreakDown.month}/${calendarViewForm.currentDateBreakDown.year}
        </td>
    <tr>
</table>
