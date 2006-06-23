<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="calendarViewForm"/>

<table border="0" width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <table border="0" width="100%" style="border:2px solid">
                <tr>
                    <td colspan="2">&nbsp;<digi:link href="/showCalendarEvent.do~selectedCalendarTypeId=${calendarViewForm.selectedCalendarType}" style="text-decoration:none"><b>Create New Event</b></digi:link></td>
                </tr>
            </table>
        </td>
    <tr>
</table>
