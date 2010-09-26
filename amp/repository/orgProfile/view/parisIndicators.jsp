<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<DIV id="tabs">
    <UL>
        <div>
            <LI>
                <a name="node">
                    <div>
                        <digi:trn>Paris declaration indicators</digi:trn>
                    </div>
                </a>
            </LI>
        </div>
    </UL>
</DIV>
<div class="topBorder contentbox_border chartPlaceCss" style="border-width: 1px 1px 1px 1px;">

<digi:instance property="orgProfilePIForm"/>


<table border="0" class="tableElement" width="100%" cellspacing="0" cellpadding="4">

    <tr>
        <th colspan="2" rowspan="2" class="tableHeaderCls">
        <digi:trn> PARIS DECLARATION INDICATORS - DONORS</digi:trn></th>
        <th colspan="3" align="center" class="tableHeaderCls">
            <digi:trn key="parisIndicatorAllDonors">All donors</digi:trn>
       </th>
       <th colspan="2" align="center" class="tableHeaderCls">
           ${orgProfilePIForm.name}
       </th>
    </tr>
    <tr>
        <th class="tableHeaderCls">2005 <digi:trn>Baseline</digi:trn></th>
        <th class="tableHeaderCls">${orgProfilePIForm.fiscalYear} <digi:trn>Value</digi:trn></th>
        <th class="tableHeaderCls">2010 <digi:trn>Target</digi:trn></th>
        <th class="tableHeaderCls">2005 <digi:trn>Baseline</digi:trn></th>
        <th class="tableHeaderCls">${orgProfilePIForm.fiscalYear} <digi:trn>Value</digi:trn></th>

    </tr>
    <c:forEach var="indicator" items="${orgProfilePIForm.indicators}">
               <c:set var="percent">
             <c:if test="${indicator.prIndicator.indicatorCode!='5aii'&&indicator.prIndicator.indicatorCode!='5bii'&&indicator.prIndicator.indicatorCode!='6'}">
                %
            </c:if>
        </c:set>
        <tr>
            <td>${indicator.prIndicator.indicatorCode}</td>
            <td><digi:trn>${indicator.prIndicator.name}</digi:trn></td>
            <td align="center">${indicator.allDonorBaseLineValue}${percent}</td>
            <td align="center">${indicator.allCurrentValue}${percent}</td>
            <td align="center">${indicator.allTargetValue}${percent}</td>
            <td align="center">${indicator.orgBaseLineValue}${percent}</td>
            <td align="center">${indicator.orgValue}${percent}</td>

        </tr>
    </c:forEach>
</table>
</div>
