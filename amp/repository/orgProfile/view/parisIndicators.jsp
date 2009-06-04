<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<digi:instance property="orgProfilePIForm"/>


<table border="1" id="pIndicator">

    <tr>
        <td colspan="2" rowspan="2" class="tableHeaderCls">
        <digi:trn> PARIS DECLARATION INDICATORS - DONORS</digi:trn></td>
        <td colspan="3" align="center" class="tableHeaderCls">
            <digi:trn key="parisIndicatorAllDonors">All donors</digi:trn>
       </td>
       <td colspan="2" align="center" class="tableHeaderCls">
           ${orgProfilePIForm.organization.name}
       </td>
    </tr>
    <tr>
        <td class="tableHeaderCls">2005 <digi:trn>Baseline</digi:trn></td>
        <td class="tableHeaderCls">${orgProfilePIForm.fiscalYear-1} Value</td>
        <td class="tableHeaderCls">2010 <digi:trn>Target</digi:trn></td>
        <td class="tableHeaderCls">2005 <digi:trn>Baseline</digi:trn></td>
        <td class="tableHeaderCls">${orgProfilePIForm.fiscalYear-1} Value</td>

    </tr>
    <c:forEach var="indicator" items="${orgProfilePIForm.indicators}">
               <c:set var="percent">
             <c:if test="${indicator.prIndicator.indicatorCode!='5aii'&&indicator.prIndicator.indicatorCode!='6'}">
                %
            </c:if>
        </c:set>
        <tr>
            <td>${indicator.prIndicator.indicatorCode}</td>
            <td>${indicator.prIndicator.name}</td>
            <td>${indicator.allDonorBaseLineValue}${percent}</td>
            <td>${indicator.allCurrentValue}${percent}</td>
            <td>${indicator.allTargetValue}${percent}</td>
            <td>${indicator.orgBaseLineValue}${percent}</td>
            <td>${indicator.orgValue}${percent}</td>

        </tr>
    </c:forEach>


</table>

<script language="javascript">
setStripsTable("pIndicator", "tableEven", "tableOdd");
setHoveredTable("pIndicator");
</script>