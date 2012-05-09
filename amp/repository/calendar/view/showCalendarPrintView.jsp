<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<digi:instance  property="calendarViewForm"/>



<table border="0"  width="1000px" height="500px">
    <tr>
        <td width="800px" height="500px" >
            <jsp:include page="viewEventsBody.jsp" />
        </td>
    </tr>
</table>

