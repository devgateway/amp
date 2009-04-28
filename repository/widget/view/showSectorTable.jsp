<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>




<digi:instance property="showSectorTableForm"/>
<table  id="sectorTable${showSectorTableForm.widgetId}" width="100%">
    <tr>
        <td style="color:#FFFFFF;font-weight:bold"><digi:trn>Sector</digi:trn></td>
        <c:forEach items="${showSectorTableForm.years}"    var="year">
            <td style="color:#FFFFFF;font-weight:bold;text-align:center">${year}</td>
        </c:forEach>
    </tr>
    <c:forEach items="${showSectorTableForm.sectorsInfo}"    var="sectorInfo">
        <tr>
            <c:choose>
                <c:when test="${sectorInfo.applyStyle}">
                    <td style="font-weight:bold">${sectorInfo.sectorName}</td>
                </c:when>
                <c:when test="${sectorInfo.emptyRow}">
                    <c:forEach items="${showSectorTableForm.years}">
                        <td>&nbsp;</td>
                    </c:forEach>
                    <td>&nbsp;</td>
                </c:when>
                <c:otherwise>
                    <td>${sectorInfo.sectorName}</td>
                </c:otherwise>
            </c:choose>
            <c:forEach items="${sectorInfo.values}"    var="value">
                <c:choose>
                    <c:when test="${sectorInfo.applyStyle}">
                        <td style="font-weight:bold;text-align:center">${value}</td>
                    </c:when>
                    <c:otherwise>
                        <td align="center">${value}</td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tr>
    </c:forEach>


</table>
<script language="javascript">
    applyStyle(document.getElementById("sectorTable${showSectorTableForm.widgetId}"));
</script>




