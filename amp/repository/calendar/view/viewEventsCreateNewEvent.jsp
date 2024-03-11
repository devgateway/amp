<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/c.tld" prefix="c" %>

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
