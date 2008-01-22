<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="calendarEventForm"/>

<table border="0" align="center" width="80%">
    <tr>
        <td valign="top">
            <digi:form action="/previewCalendarEventDone.do">
            <html:hidden styleId="method" name="calendarEventForm" property="method" value="save"/>
            <html:hidden name="calendarEventForm" property="selectedCalendarTypeId" value="${calendarEventForm.selectedCalendarTypeId}"/>
            <html:hidden name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>
            <table border="0" style="border:1px solid">
                <tr>
                    <td colspan="2" nowrap="nowrap"><digi:trn key="calendar:details">&nbsp;Details&nbsp;&nbsp;</digi:trn></td>
                </tr>
                <tr>
                    <td nowrap="nowrap"><digi:trn key="calendar:eventTitle">&nbsp;Event Title:&nbsp;&nbsp;</digi:trn></td>
                    <td>
                        <html:hidden name="calendarEventForm" property="eventTitle" value="${calendarEventForm.eventTitle}"/>
                        <b>${calendarEventForm.eventTitle}</b>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap"><digi:trn key="calendar:eventType">&nbsp;Event Type:&nbsp;&nbsp;</digi:trn></td>
                    <td>
                        <html:hidden name="calendarEventForm" property="selectedEventTypeId" value="${calendarEventForm.selectedEventTypeId}"/>
                        <b>${calendarEventForm.selectedEventTypeName}</b>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" valign="top"><digi:trn key="calendar:dolors">&nbsp;Donors:&nbsp;&nbsp;</digi:trn></td>
                    <td>
                        <logic:empty name="calendarEventForm" property="donors">
                            &nbsp;
                        </logic:empty>
                        <logic:notEmpty name="calendarEventForm" property="donors">
                            <table cellpadding="0" cellspacing="0">
                                <logic:iterate id="donor" name="calendarEventForm" property="donors">
                                    <tr>
                                        <td nowrap="nowrap">
                                            <html:hidden name="calendarEventForm" property="selectedDonors" value="${donor.value}"/>
                                            ${donor.label};
                                        </td>
                                    </tr>
                                </logic:iterate>
                            </table>
                        </logic:notEmpty>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap">&nbsp;From (${calendarEventForm.selectedCalendarTypeName}):&nbsp;&nbsp;</td>
                    <td>
                        <html:hidden name="calendarEventForm" property="selectedStartDate" value="${calendarEventForm.selectedStartDate}"/>
                        <html:hidden name="calendarEventForm" property="selectedStartTime" value="${calendarEventForm.selectedStartTime}"/>
                        <b>${calendarEventForm.selectedStartDate}&nbsp;${calendarEventForm.selectedStartTime}</b>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap">&nbsp;To (${calendarEventForm.selectedCalendarTypeName}):&nbsp;&nbsp;</td>
                    <td>
                        <html:hidden name="calendarEventForm" property="selectedEndDate" value="${calendarEventForm.selectedEndDate}"/>
                        <html:hidden name="calendarEventForm" property="selectedEndTime" value="${calendarEventForm.selectedEndTime}"/>
                        <b>${calendarEventForm.selectedEndDate}&nbsp;${calendarEventForm.selectedEndTime}</b>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" valign="top"><digi:trn key="calendar:attendees">&nbsp;Attendees:&nbsp;&nbsp;</digi:trn></td>
                    <td>
                        <table border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td valign="top"><digi:trn key="calendar:users">&nbsp;Users</digi:trn></td>
                                <td rowspan="2">&nbsp;</td>
                                <td valign="top"><digi:trn key="calendar:guests">&nbsp;Guests</digi:trn></td>
                            </tr>
                            <tr>
                                <td valign="top">
                                    <div style="width:200px;height:100px;border:1px solid;overflow:auto">
                                        <logic:notEmpty name="calendarEventForm" property="attendeeUsers">
                                            <table cellpadding="0" cellspacing="0">
                                                <logic:iterate id="user" name="calendarEventForm" property="attendeeUsers">
                                                    <tr>
                                                        <td nowrap="nowrap">
                                                            <html:hidden name="calendarEventForm" property="selectedAttendeeUsers" value="${user.value}"/>
                                                            &nbsp;${user.label}&nbsp;
                                                        </td>
                                                    </tr>
                                                </logic:iterate>
                                            </table>
                                        </logic:notEmpty>
                                    </div>
                                </td>
                                <td valign="top">
                                    <div style="width:200px;height:100px;border:1px solid;overflow:auto">
                                        <logic:notEmpty name="calendarEventForm" property="attendeeGuests">
                                            <table cellpadding="0" cellspacing="0">
                                                <logic:iterate id="guest" name="calendarEventForm" property="attendeeGuests">
                                                    <tr>
                                                        <td nowrap="nowrap">
                                                            <html:hidden name="calendarEventForm" property="selectedAttendeeGuests" value="${guest.value}"/>
                                                            &nbsp;${guest.label}&nbsp;
                                                        </td>
                                                    </tr>
                                                </logic:iterate>
                                            </table>
                                        </logic:notEmpty>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap"><digi:trn key="calendar:eventIsPrivate">&nbsp;Event is private</digi:trn></td>
                    <td>
                        <html:hidden name="calendarEventForm" property="privateEvent" value="${calendarEventForm.privateEvent}"/>
                        <c:if test="${calendarEventForm.privateEvent}"><b>Yes</b></c:if>
                        <c:if test="${!calendarEventForm.privateEvent}"><b>No</b></c:if>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                     <td>
                        <input type="submit" value="<digi:trn key="calendar:savebutton">Save</digi:trn>">&nbsp;<input type="submit" value="<digi:trn key="calendar:editbutton">Edit</digi:trn>" onclick="document.getElementById('method').value = 'edit'">
                    </td>
                </tr>
            </table>
            </digi:form>
        </td>
    </tr>
</table>
