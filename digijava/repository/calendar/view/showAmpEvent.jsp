<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="calendarEventForm"/>

<link rel="stylesheet" href="<digi:file src="module/calendar/css/main.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/calendar/js/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/calendar/js/main.js"/>"></script>
<script language="JavaScript" type="text/javascript">

function addOrganisation(orgId, orgName){
    var list = document.getElementById('organizationList');
    if (list == null || orgId == "" || orgName == "") {
        return;
    }
    var option = document.createElement("OPTION");
    option.value = orgId;
    option.text = orgName;
    list.options.add(option);
}

function addGuest(guest) {
    var list = document.getElementById('selectedAttendeeGuests');
    if (list == null || guest == null || guest.value == null || guest.value == "") {
        return;
    }
    var option = document.createElement("OPTION");
    option.value = guest.value;
    option.text = guest.value;
    list.options.add(option);
    guest.value = "";
}

function removeGuest() {
    var list = document.getElementById('selectedAttendeeGuests');
    if (list == null) {
        return;
    }
    var index = list.selectedIndex;
    if (index != -1) {
        for(var i = list.length - 1; i >= 0; i--) {
            if (list.options[i].selected) {
                list.options[i] = null;
            }
        }
        if (list.length > 0) {
            list.selectedIndex = index == 0 ? 0 : index - 1;
        }
    }
}

function selectGuests() {
    var list = document.getElementById('selectedAttendeeGuests');
    if (list == null) {
        return;
    }
    for(var i = 0; i < list.length; i++) {
        list.options[i].selected = true;
    }
}

function edit(key) {
	document.calendarEditActivityForm.step.value = "1.1";
	document.calendarEditActivityForm.editKey.value = key;
	document.calendarEditActivityForm.target = "_self";
	document.calendarEditActivityForm.submit();
}

function removeSelOrganisations() {
    var list = document.getElementById('organizationList');
    if (list == null) {
        return false;
    }
    var index = list.selectedIndex;
    if (index != -1) {
        for(var i = list.length - 1; i >= 0; i--) {
            if (list.options[i].selected) {
                list.options[i] = null;
            }
        }
        if (list.length > 0) {
            list.selectedIndex = index == 0 ? 0 : index - 1;
        }
    }
    return false;
}

function selectAllOrgs() {
    var lsItem;
    var orglist = document.getElementById('organizationList');
    for(var ind=0; ind<orglist.options.length; ind++){
      lsItem=orglist.options[ind];
      lsItem.selected=true;
    }
    //document.getElementById('organizationList').option.selectAll;
    return true;
}
</script>

<table border="0" width="100%" cellPadding=0 cellSpacing=0>
        <tr>
          <td colspan="3"><jsp:include page="teamPagesHeader.jsp" flush="true"/><td>
       </tr>

       <table class="r-dotted-lg">
       <tr>
       <td class="r-dotted-lg">&nbsp;</td>
         <td valign="top" width="230">
            <table border="0" style="border:1px solid">
                <tr>
                    <td nowrap="nowrap">
                        <table border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td nowrap="nowrap">&nbsp;Calendar Type&nbsp;&nbsp;</td>
                                <digi:form action="/showCalendarEvent.do">
                                    <td>
                                        <html:hidden name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>
                                        <html:select name="calendarEventForm" property="selectedCalendarTypeId" onchange="this.form.submit()">
                                            <logic:notEmpty name="calendarEventForm" property="calendarTypes">
                                                <bean:define id="types" name="calendarEventForm" property="calendarTypes" type="java.util.List"/>
                                                <html:options collection="types" property="value" labelProperty="label"/>
                                            </logic:notEmpty>
                                        </html:select>
                                    </td>
                                </digi:form>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

        </td>
        <td align="right">
             <c:if test="${calendarEventForm.ampCalendarId != null && calendarEventForm.ampCalendarId != 0}">
                <digi:form action="/DeleteEvent.do~ampCalendarId=${calendarEventForm.ampCalendarId}">
                    <html:submit value="Delete" style="width:80px" onclick="if(confirm('Are You sure?')){return true}else{return false};"/>
                </digi:form>
             </c:if>
        </td>
    </tr>
    <tr>
      <td class="r-dotted-lg">&nbsp;</td>
        <td colspan="3">
            <html:errors/>
            <digi:form action="/previewCalendarEvent.do" onsubmit="selectGuests()">
            <html:hidden name="calendarEventForm" property="selectedCalendarTypeId" value="${calendarEventForm.selectedCalendarTypeId}"/>
            <html:hidden name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>
            <table border="0" style="border:1px solid" width="756px">
                <tr>
                    <td colspan="3" nowrap="nowrap">&nbsp;Details&nbsp;&nbsp;</td>
                </tr>
                <tr>
                    <td nowrap="nowrap">&nbsp;<span class="redbold">*</span>Event Title&nbsp;&nbsp;</td>
                    <td>
                        <html:text name="calendarEventForm" property="eventTitle"/>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap">&nbsp;<span class="redbold">*</span>Event Type&nbsp;&nbsp;</td>
                    <td>
                        <html:select name="calendarEventForm" property="selectedEventTypeId">
                            <logic:notEmpty name="calendarEventForm" property="eventTypesList">
                                <bean:define id="types" name="calendarEventForm" property="eventTypesList" type="java.util.List"/>
                                <html:options collection="types" property="id" labelProperty="name"/>
                            </logic:notEmpty>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" valign="top"><br />&nbsp;Organisations&nbsp;&nbsp;</td>
                    <td>

                      <a title="Facilitates tracking activities in donors' internal databases">
                       <br />
                       <digi:link href="/selectOrganization.do?orgSelReset=true&edit=true" onclick="window.open(this.href, 'users', 'HEIGHT=500,resizable=yes,scrollbars=yes,WIDTH=500');return false;">
                        Add Organisation
                       </digi:link>
                       &nbsp
                      <a href="#" onclick="return removeSelOrganisations();">
                        Delete Organisation
                      </a>
                        </a>
                        <br /><br />
                        <html:select name="calendarEventForm" property="selectedDonors" multiple="multiple" size="5" styleId="organizationList" style="width: 61%;">
                            <logic:notEmpty name="calendarEventForm" property="donors">
                                <bean:define id="donors" name="calendarEventForm" property="donors" type="java.util.List"/>
                                <html:options collection="donors" property="value" labelProperty="label"/>
                            </logic:notEmpty>
                        </html:select>

                    </td>

                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap">&nbsp;<span class="redbold">*</span>From&nbsp;&nbsp;</td>
                    <html:hidden styleId="selectedStartTime" name="calendarEventForm" property="selectedStartTime"/>
                    <html:hidden styleId="selectedEndTime" name="calendarEventForm" property="selectedEndTime"/>
                    <c:if test="${calendarEventForm.selectedCalendarTypeId == 0}">
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
                                <td nowrap="nowrap">
                                    <html:text styleId="selectedStartDate" name="calendarEventForm" property="selectedStartDate" style="width:80px"/>
                                </td>
                                <td>
                                    <a href="javascript:calendar('selectedStartDate');">
                                        <img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
                                    </a>
                                </td>
                                <td nowrap="nowrap">&nbsp;&nbsp;<img id="startDateImg" name="startDateImg" src="/thirdparty/CalendarPopup/img/cal.gif" alt="STARTDATE" onclick="startDateCalendar.select(document.getElementById('selectedStartDate'), 'startDateImg', 'dd/MM/yyyy');return false;">&nbsp;</td>
                                <td>
                                    <select id="selectedStartHour" onchange="updateTime(document.getElementById('selectedStartTime'), 'hour', this.value)">
                                        <c:forEach var="hour" begin="0" end="23">
                                            <c:if test="${hour < 10}"><c:set var="hour" value="0${hour}"/></c:if>
                                            <option value="${hour}">${hour}</option>
                                        </c:forEach>
                                    </select>
                                    <script type="text/javascript">
                                    selectOptionByValue(document.getElementById('selectedStartHour'), get('hour', document.getElementById('selectedStartTime').value));
                                    </script>
                                </td>
                                <td nowrap="nowrap">&nbsp;<b>:</b>&nbsp;</td>
                                <td>
                                    <select id="selectedStartMinute" onchange="updateTime(document.getElementById('selectedStartTime'), 'minute', this.value)">
                                        <c:forEach var="minute" begin="0" end="59">
                                            <c:if test="${minute < 10}"><c:set var="minute" value="0${minute}"/></c:if>
                                            <option value="${minute}">${minute}</option>
                                        </c:forEach>
                                    </select>
                                    <script type="text/javascript">
                                    selectOptionByValue(document.getElementById('selectedStartMinute'), get('minute', document.getElementById('selectedStartTime').value));
                                    </script>
                                </td>
                            </table>
                        </td>
                    </c:if>
                    <c:if test="${calendarEventForm.selectedCalendarTypeId != 0}">
                        <html:hidden styleId="selectedStartDate" name="calendarEventForm" property="selectedStartDate"/>
                        <td nowrap="nowrap">
                            <table cellpadding="0" cellspacing="0">
                                <tr>
                                    <td>
                                        <select id="selectedStartYear" onchange="updateDate(document.getElementById('selectedStartDate'), 'year', this.value)"></select>
                                        <script type="text/javascript">
                                        createYearCombo(document.getElementById('selectedStartYear'), document.getElementById('selectedStartDate').value);
                                        </script>
                                    </td>
                                    <td>
                                        <select id="selectedStartMonth" onchange="updateDate(document.getElementById('selectedStartDate'), 'month', this.value)">
                                            <c:forEach var="i" begin="1" end="13">
                                                <bean:define id="index" value="${i - 1}"/>
                                                <bean:define id="type" value="${calendarEventForm.selectedCalendarTypeId}"/>
                                                <c:if test="${i < 10}"><c:set var="i" value="0${i}"/></c:if>
                                                <option value="${i}"/>
                                                    <%=org.digijava.module.calendar.entity.DateBreakDown.getMonthName(Integer.parseInt(index), Integer.parseInt(type), false)%>
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <script type="text/javascript">
                                        selectOptionByValue(document.getElementById('selectedStartMonth'), get('month', document.getElementById('selectedStartDate').value));
                                        </script>
                                    </td>
                                    <td>
                                        <select id="selectedStartDay" onchange="updateDate(document.getElementById('selectedStartDate'), 'day', this.value)">
                                            <c:forEach var="i" begin="1" end="30">
                                                <c:if test="${i < 10}"><c:set var="i" value="0${i}"/></c:if>
                                                <option value="${i}"/>${i}</option>
                                            </c:forEach>
                                        </select>
                                        <script type="text/javascript">
                                        selectOptionByValue(document.getElementById('selectedStartDay'), get('day', document.getElementById('selectedStartDate').value));
                                        </script>
                                    </td>
                                    <td nowrap="nowrap">&nbsp;&nbsp;</td>
                                    <td>
                                        <select id="selectedStartHour" onchange="updateTime(document.getElementById('selectedStartTime'), 'hour', this.value)">
                                            <c:forEach var="hour" begin="0" end="23">
                                                <c:if test="${hour < 10}"><c:set var="hour" value="0${hour}"/></c:if>
                                                <option value="${hour}">${hour}</option>
                                            </c:forEach>
                                        </select>
                                        <script type="text/javascript">
                                        selectOptionByValue(document.getElementById('selectedStartHour'), get('hour', document.getElementById('selectedStartTime').value));
                                        </script>
                                    </td>
                                    <td nowrap="nowrap">&nbsp;<b>:</b>&nbsp;</td>
                                    <td>
                                        <select id="selectedStartMinute" onchange="updateTime(document.getElementById('selectedStartTime'), 'minute', this.value)">
                                            <c:forEach var="minute" begin="0" end="59">
                                                <c:if test="${minute < 10}"><c:set var="minute" value="0${minute}"/></c:if>
                                                <option value="${minute}">${minute}</option>
                                            </c:forEach>
                                        </select>
                                        <script type="text/javascript">
                                        selectOptionByValue(document.getElementById('selectedStartMinute'), get('minute', document.getElementById('selectedStartTime').value));
                                        </script>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </c:if>
                </tr>
                <tr>
                    <td nowrap="nowrap">&nbsp;<span class="redbold">*</span>To&nbsp;&nbsp;</td>
                    <c:if test="${calendarEventForm.selectedCalendarTypeId == 0}">
                        <script type="text/javascript">
                            var endDateCalendar = new CalendarPopup();
                            endDateCalendar.setWeekStartDay(1);
                        </script>
                        <td nowrap="nowrap">
                            <table cellpadding="0" cellspacing="0">
                                <td nowrap="nowrap">
                                    <html:text styleId="selectedEndDate" name="calendarEventForm" property="selectedEndDate" style="width:80px"/>
                                </td>
                                <td>
                                    <a href="javascript:calendar('selectedEndDate')">
                                        <img src=" ../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
                                    </a>
                                </td>
                                <td nowrap="nowrap">&nbsp;&nbsp;<img id="endDateImg" name="endDateImg" src="/thirdparty/CalendarPopup/img/cal.gif" alt="ENDDATE" onclick="endDateCalendar.select(document.getElementById('selectedEndDate'), 'endDateImg', 'dd/MM/yyyy');return false;">&nbsp;</td>
                                <td>
                                    <select id="selectedEndHour" onchange="updateTime(document.getElementById('selectedEndTime'), 'hour', this.value)">
                                        <c:forEach var="hour" begin="0" end="23">
                                            <c:if test="${hour < 10}"><c:set var="hour" value="0${hour}"/></c:if>
                                            <option value="${hour}">${hour}</option>
                                        </c:forEach>
                                    </select>
                                    <script type="text/javascript">
                                    selectOptionByValue(document.getElementById('selectedEndHour'), get('hour', document.getElementById('selectedEndTime').value));
                                    </script>
                                </td>
                                <td nowrap="nowrap">&nbsp;<b>:</b>&nbsp;</td>
                                <td>
                                    <select id="selectedEndMinute" onchange="updateTime(document.getElementById('selectedEndTime'), 'minute', this.value)">
                                        <c:forEach var="minute" begin="0" end="59">
                                            <c:if test="${minute < 10}"><c:set var="minute" value="0${minute}"/></c:if>
                                            <option value="${minute}">${minute}</option>
                                        </c:forEach>
                                    </select>
                                    <script type="text/javascript">
                                    selectOptionByValue(document.getElementById('selectedEndMinute'), get('minute', document.getElementById('selectedEndTime').value));
                                    </script>
                                </td>
                            </table>
                        </td>
                    </c:if>
                    <c:if test="${calendarEventForm.selectedCalendarTypeId != 0}">
                        <html:hidden styleId="selectedEndDate" name="calendarEventForm" property="selectedEndDate"/>
                        <td nowrap="nowrap">
                            <table cellpadding="0" cellspacing="0">
                                <tr>
                                    <td>
                                        <select id="selectedEndYear" onchange="updateDate(document.getElementById('selectedEndDate'), 'year', this.value)"></select>
                                        <script type="text/javascript">
                                        createYearCombo(document.getElementById('selectedEndYear'), document.getElementById('selectedEndDate').value);
                                        </script>
                                    </td>
                                    <td>
                                        <select id="selectedEndMonth" onchange="updateDate(document.getElementById('selectedEndDate'), 'month', this.value)">
                                            <c:forEach var="i" begin="1" end="13">
                                                <bean:define id="index" value="${i - 1}"/>
                                                <bean:define id="type" value="${calendarEventForm.selectedCalendarTypeId}"/>
                                                <c:if test="${i < 10}"><c:set var="i" value="0${i}"/></c:if>
                                                <option value="${i}"/>
                                                    <%=org.digijava.module.calendar.entity.DateBreakDown.getMonthName(Integer.parseInt(index), Integer.parseInt(type), false)%>
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <script type="text/javascript">
                                        selectOptionByValue(document.getElementById('selectedEndMonth'), get('month', document.getElementById('selectedEndDate').value));
                                        </script>
                                    </td>
                                    <td>
                                        <select id="selectedEndDay" onchange="updateDate(document.getElementById('selectedEndDate'), 'day', this.value)">
                                            <c:forEach var="i" begin="1" end="30">
                                                <c:if test="${i < 10}"><c:set var="i" value="0${i}"/></c:if>
                                                <option value="${i}"/>${i}</option>
                                            </c:forEach>
                                        </select>
                                        <script type="text/javascript">
                                        selectOptionByValue(document.getElementById('selectedEndDay'), get('day', document.getElementById('selectedEndDate').value));
                                        </script>
                                    </td>
                                    <td nowrap="nowrap">&nbsp;&nbsp;</td>
                                    <td>
                                        <select id="selectedEndHour" onchange="updateTime(document.getElementById('selectedEndTime'), 'hour', this.value)">
                                            <c:forEach var="hour" begin="0" end="23">
                                                <c:if test="${hour < 10}"><c:set var="hour" value="0${hour}"/></c:if>
                                                <option value="${hour}">${hour}</option>
                                            </c:forEach>
                                        </select>
                                        <script type="text/javascript">
                                        selectOptionByValue(document.getElementById('selectedEndHour'), get('hour', document.getElementById('selectedEndTime').value));
                                        </script>
                                    </td>
                                    <td nowrap="nowrap">&nbsp;<b>:</b>&nbsp;</td>
                                    <td>
                                        <select id="selectedEndMinute" onchange="updateTime(document.getElementById('selectedEndTime'), 'minute', this.value)">
                                            <c:forEach var="minute" begin="0" end="59">
                                                <c:if test="${minute < 10}"><c:set var="minute" value="0${minute}"/></c:if>
                                                <option value="${minute}">${minute}</option>
                                            </c:forEach>
                                        </select>
                                        <script type="text/javascript">
                                        selectOptionByValue(document.getElementById('selectedEndMinute'), get('minute', document.getElementById('selectedEndTime').value));
                                        </script>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </c:if>
                </tr>
                <tr>
                    <td nowrap="nowrap" valign="top">&nbsp;Attendees&nbsp;&nbsp;</td>
                    <td>
                        <table border="0" style="border:1px solid">
                            <tr>
                                <td valign="top">
                                    <table border="0" width="100%">
                                        <tr>
                                            <td nowrap="nowrap">&nbsp;Users</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <html:select name="calendarEventForm" property="selectedAttendeeUsers" multiple="multiple" size="7">
                                                    <logic:notEmpty name="calendarEventForm" property="attendeeUsers">
                                                        <bean:define id="users" name="calendarEventForm" property="attendeeUsers" type="java.util.List"/>
                                                        <html:options collection="users" property="value" labelProperty="label"/>
                                                    </logic:notEmpty>
                                                </html:select>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td valign="top">
                                    <table border="0" width="100%" cellpadding="0">
                                        <tr>
                                            <td nowrap="nowrap">&nbsp;Guests</td>
                                        </tr>
                                        <tr>
                                            <td nowrap="nowrap" valign="top">
                                                <table border="0" width="100%">
                                                    <tr>
                                                        <td valign="top">
                                                            <input id="guest" type="text" style="width:100%">
                                                        </td>
                                                        <td valign="top">
                                                            <input type="button" value="Add" style="width:80px" onclick="addGuest(document.getElementById('guest'))">
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="top">
                                                            <html:select styleId="selectedAttendeeGuests" name="calendarEventForm" property="selectedAttendeeGuests" multiple="multiple" size="5" style="width:200px">
                                                                <logic:notEmpty name="calendarEventForm" property="attendeeGuests">
                                                                    <bean:define id="guests" name="calendarEventForm" property="attendeeGuests" type="java.util.List"/>
                                                                    <html:options collection="guests" property="value" labelProperty="label"/>
                                                                </logic:notEmpty>
                                                            </html:select>
                                                        </td>
                                                        <td valign="top">
                                                            <input type="button" value="Remove" style="width:80px" onclick="removeGuest()">
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" valign="top">&nbsp;</td>
                    <td>
                        <html:checkbox name="calendarEventForm" property="privateEvent"/>&nbsp;Event is private
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>
                        <input type="submit" value="Preview" onclick="selectAllOrgs()">
                    </td>
                </tr>
            </table>
            </digi:form>
       </td>
    </tr>
    </table>
</table>
