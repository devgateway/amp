<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<digi:instance property="calendarViewForm"/>

<table border="0" width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <table border="0" width="100%">
                <tr>
                    <td colspan="2">&nbsp;<digi:link href="/showCalendarEvent.do~selectedCalendarTypeId=${calendarViewForm.selectedCalendarType}~method=new" style="text-decoration:none"><b><digi:trn key="calendar:CreateNewEvent">Create New Event</digi:trn></b></digi:link></td>
                </tr>
            </table>
        </td>
    <tr>
</table>
