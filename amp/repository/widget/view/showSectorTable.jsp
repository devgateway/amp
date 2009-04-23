<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>




<digi:instance property="showSectorTableForm"/>
<table  id="sectorTable${showSectorTableForm.widgetId}">
    <tr>
        <td style="color:#FFFFFF;font-weight:bold"><digi:trn>Sector</digi:trn></td>
        <c:forEach items="${showSectorTableForm.years}"    var="year">
            <td style="color:#FFFFFF;font-weight:bold;text-align:center">${year}</td>
        </c:forEach>
    </tr>
    <c:forEach items="${showSectorTableForm.sectorsInfo}"    var="sectorInfo">
        <tr>
            <td>${sectorInfo.sectorName}</td>
            <c:forEach items="${sectorInfo.totalYearsValue}"    var="totalYear">
                <td align="center">${totalYear}</td>
            </c:forEach>
            <c:forEach items="${sectorInfo.percentYearsValue}"    var="percentYear">
                <td align="center">${percentYear}%</td>
            </c:forEach>
        </tr>
    </c:forEach>


</table>
<script language="javascript">
    applyStyle(document.getElementById("sectorTable${showSectorTableForm.widgetId}"));
</script>




