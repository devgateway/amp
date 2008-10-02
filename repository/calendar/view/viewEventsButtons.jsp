<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="calendarViewForm"/>

<style type="text/css">
    #tbl td {
        border:1px solid;
        border-color: #484846;
        width:100px;
    }
    #tbl td:hover {
        background-color:#ffbebe;
    }
    #tbl td a {
        text-decoration:none;
    }
    #clicked {
        background-color:#ffbebe;
    }
</style>

<table id="tbl" border="0" width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td <c:if test="${calendarViewForm.view == 'yearly'}">id="clicked"</c:if> align="center"><a href="#" onclick="submitFilterForm('yearly', '${calendarViewForm.timestamp}'); return(false);"><digi:trn key="calendar:Yearly">Yearly</digi:trn></a></td>
        <td style="border:none;width:5px">&nbsp;</td>
        <td <c:if test="${calendarViewForm.view == 'monthly'}">id="clicked"</c:if> align="center"><a href="#" onclick="submitFilterForm('monthly', '${calendarViewForm.timestamp}'); return(false);"><digi:trn key="calendar:Monthly">Monthly</digi:trn></a></td>
        <td style="border:none;width:5px">&nbsp;</td>
        <td <c:if test="${calendarViewForm.view == 'weekly'}">id="clicked"</c:if> align="center"><a href="#" onclick="submitFilterForm('weekly', '${calendarViewForm.timestamp}'); return(false);"><digi:trn key="calendar:Weekly">Weekly</digi:trn></a></td>
        <td style="border:none;width:5px">&nbsp;</td>
        <td <c:if test="${calendarViewForm.view == 'daily'}">id="clicked"</c:if> align="center"><a href="#" onclick="submitFilterForm('daily', '${calendarViewForm.timestamp}'); return(false);"><digi:trn key="calendar:1Day">1 Day</digi:trn></a></td>
        <td style="border:none;width:5px">&nbsp;</td>
        <%--<td <c:if test="${calendarViewForm.view == 'custom'}">id="clicked"</c:if> align="center"><a href="#" onclick="submitFilterForm('custom', '${calendarViewForm.timestamp}'); return(false);"><digi:trn key="calendar:CustomView">Custom View</digi:trn></a></td>--%>
    <tr>
</table>
