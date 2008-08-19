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
            <digi:form action="/showCalendarEvent.do">
            <html:hidden styleId="hdnMethod" name="calendarEventForm" property="method"/>
            <html:hidden name="calendarEventForm" property="selectedCalendarTypeId" value="${calendarEventForm.selectedCalendarTypeId}"/>
            <html:hidden name="calendarEventForm" property="ampCalendarId" value="${calendarEventForm.ampCalendarId}"/>
            <html:hidden name="calendarEventForm" property="selectedEventTypeId" value="${calendarEventForm.selectedEventTypeId}"/>
            <html:hidden name="calendarEventForm" property="eventTitle" value="${calendarEventForm.eventTitle}"/>
            <table border="0" style="border:1px solid">
                <tr>
                    <td colspan="2" nowrap="nowrap"><digi:trn key="calendar:details">&nbsp;Details&nbsp;&nbsp;</digi:trn></td>
                </tr>
                <tr>
                    <td nowrap="nowrap"><digi:trn key="calendar:eventTitle">&nbsp;Event Title:&nbsp;&nbsp;</digi:trn></td>
                    <td>
                        <b>${calendarEventForm.eventTitle}</b>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap"><digi:trn key="calendar:eventType">&nbsp;Event Type:&nbsp;&nbsp;</digi:trn></td>
                    <td>
                        <b>${calendarEventForm.selectedEventTypeName}</b>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap" valign="top"><digi:trn key="calendar:orgs">&nbsp;Donors:&nbsp;&nbsp;</digi:trn></td>
                    <td>
                      <div style="width:300px;height:200px;border:1px solid;overflow:auto">

                        <c:if test="${empty calendarEventForm.selectedEventOrganisationsCol}">
                        &nbsp;
                        </c:if>
                        <c:if test="${!empty calendarEventForm.selectedEventOrganisationsCol}">
                          <table cellpadding="0" cellspacing="0">
                            <c:forEach var="org" items="${calendarEventForm.selectedEventOrganisationsCol}">
                              <tr>
                                <td nowrap="nowrap">
                                  <html:hidden name="calendarEventForm" property="selectedEventOrganisations" value="${org.value}"/>
                                  ${org.label};
                                </td>
                              </tr>
                            </c:forEach>
                          </table>
                        </c:if>
                      </div>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap">&nbsp;<digi:trn key="aim:calendar:from">From</digi:trn> (${calendarEventForm.selectedCalendarTypeName}):&nbsp;&nbsp;</td>
                    <td>
                        <html:hidden name="calendarEventForm" property="selectedStartDate" value="${calendarEventForm.selectedStartDate}"/>
                        <html:hidden name="calendarEventForm" property="selectedStartTime" value="${calendarEventForm.selectedStartTime}"/>
                        <b>${calendarEventForm.selectedStartDate}&nbsp;${calendarEventForm.selectedStartTime}</b>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap">&nbsp;<digi:trn key="calendar:to">To</digi:trn> (${calendarEventForm.selectedCalendarTypeName}):&nbsp;&nbsp;</td>
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
                                <td valign="top">
                                    <div style="width:300px;height:200px;border:1px solid;overflow:auto">
                                        <c:if test="${!empty calendarEventForm.selectedAttsCol}">
                                            <table cellpadding="0" cellspacing="0">
                                                <c:forEach var="att" items="${calendarEventForm.selectedAttsCol}">
                                                    <tr>
                                                        <td nowrap="nowrap">
                                                           <html:hidden name="calendarEventForm" property="selectedAtts" value="${att.value}"/>
                                                           &nbsp;${att.label}&nbsp;
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </table>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td nowrap="nowrap">&nbsp;<digi:trn key="calendar:privateEvent">Private event:</digi:trn>&nbsp;</td>
                    <td>
                        <html:hidden name="calendarEventForm" property="privateEvent" value="${calendarEventForm.privateEvent}"/>
                        <c:if test="${calendarEventForm.privateEvent}"><b><digi:trn key="calendar:yes">Yes</digi:trn></b></c:if>
                        <c:if test="${!calendarEventForm.privateEvent}"><b><digi:trn key="calendar:no">No</digi:trn></b></c:if>
                    </td>
                </tr>
                    <tr>
                    <td>&nbsp;</td>
                     <td>
                        <input type="submit" value="<digi:trn key="calendar:savebutton">Save</digi:trn>" onclick="document.getElementById('hdnMethod').value = 'save'">
                        &nbsp;
                        <input type="submit" value="<digi:trn key="calendar:editbutton">Edit</digi:trn>" onclick="document.getElementById('hdnMethod').value = ''">
                    </td>
                </tr>
            </table>
            </digi:form>
        </td>
    </tr>
</table>
