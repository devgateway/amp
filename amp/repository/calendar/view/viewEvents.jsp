<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<table border="0"  width="100%" cellPadding=0 cellSpacing=0>
       <tr>
          <td colspan="2" valign="top"><jsp:include page="../../aim/view/teamPagesHeader.jsp" flush="true"/><td>
       </tr>
        <tr>
        <td valign="top" width="230" >
            <digi:form action="/showCalendarView.do" styleId="filterForm">
            <table border="0" width="100%">
                <tr>
                    <td valign="top"><jsp:include page="viewEventsNavigator.jsp" flush="true"/><td>
                </tr>
                <tr>
                    <td valign="top"><jsp:include page="viewEventsFilter.jsp" flush="true"/></td>
                </tr>
               	<tr>
                    <td valign="top"><jsp:include page="viewEventsCreateNewEvent.jsp" flush="true"/></td>
                </tr>
            </table>
            </digi:form>
        </td>
        <td valign="top" width="100%">
            <table border="0" width="100%">
                <tr>
                    <td valign="top"><jsp:include page="viewEventsButtons.jsp" flush="true"/></td>
                </tr>
                <tr>
                    <td valign="top"><jsp:include page="viewEventsBody.jsp" flush="true"/></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
