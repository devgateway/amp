<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="calendarViewForm"/>

<script type="text/javascript">
function submitFilterForm(view, timestamp) {
    var form = document.getElementById('filterForm');
    if (form != null) {
        form.view.value = view;
        form.timestamp.value = timestamp;
        form.submit();
    }
}
</script>

<table border="0" width="100%" cellpadding="0" cellspacing="0">
    <tr><td>Filter</td></tr>
    <tr>
        <td>
            <html:hidden name="calendarViewForm" property="view" value="${calendarViewForm.view}"/>
            <html:hidden name="calendarViewForm" property="timestamp" value="${calendarViewForm.timestamp}"/>
            <html:hidden name="calendarViewForm" property="filterInUse" value="true"/>
            <table border="0" width="100%" style="border:2px solid; border-color: #484846;">
                <tr>
                    <td colspan="2">&nbsp;Event Type</td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div style="width:200px;height:80px;border:1px solid;overflow:auto; border-color: #484846;">
                            <logic:notEmpty name="calendarViewForm" property="filter.eventTypes">
                                <table border="0">
                                <logic:iterate id="eventType" name="calendarViewForm" property="filter.eventTypes">
                                    <tr>
                                        <td valign="middle"><html:multibox name="calendarViewForm" property="filter.selectedEventTypes" value="${eventType.id}"/></td>
                                        <td valign="middle"><div style="width:10px;height:10px;background-color:${eventType.color};border:1x solid"></div></td>
                                        <td valign="middle" nowrap="nowrap">${eventType.name}</td>
                                    </tr>
                                </logic:iterate>
                                </table>
                            </logic:notEmpty>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;Donors</td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div style="width:200px;height:80px;border:1px solid;overflow:auto; border-color: #484846;">
                            <logic:notEmpty name="calendarViewForm" property="filter.donors">
                                <table border="0">
                                <logic:iterate id="donor" name="calendarViewForm" property="filter.donors">
                                    <tr>
                                        <td valign="middle"><html:multibox name="calendarViewForm" property="filter.selectedDonors" value="${donor.value}"/></td>
                                        <td valign="middle" nowrap="nowrap">${donor.label}</td>
                                    </tr>
                                </logic:iterate>
                                </table>
                            </logic:notEmpty>
                        </div>
                    </td>
                </tr>
                <digi:secure authenticated="true">
                    <tr>
                        <td colspan="2">
                            <html:checkbox name="calendarViewForm" property="filter.showPublicEvents"/>&nbsp;Show public events
                        </td>
                    </tr>
                </digi:secure>
                <tr>
                    <td colspan="2">
                        <html:submit value="Show"/>&nbsp;<html:reset value="Reset"/>
                    </td>
                </tr>
            </table>
        </td>
    <tr>
</table>
