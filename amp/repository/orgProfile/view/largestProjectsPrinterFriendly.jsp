<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<style type="text/css">
    .tableHeaderCls {
        font-size: 12px;
        font-family: Arial;
        text-align:center;
        font-weight: bold;
        background-color:#FFFFFF;
    }

    td {
        font-size:10px;
        font-family: Arial;
        background-color:#FFFFFF;
    }
</style>
<style type="text/css" media="print">
    #printWin {
        display: none
}
</style>

<digi:instance property="orgProfLargestProjectsForm"/>

<digi:link styleId="printWin" href="#" onclick="window.print(); return false;" style="media">
    <img alt="print preview" src="/TEMPLATE/ampTemplate/images/print_icon.gif" border="0" >
</digi:link>
<table border="1"  bgcolor="#dddddd" width="100%"  class="tableElement" cellspacing="0" cellpadding="0">
    <tr>
        <th colspan="4" class="tableHeaderCls">
          ${sessionScope.orgProfileFilter.largestProjectNumb} <digi:trn>LARGEST PROJECTS</digi:trn>(${sessionScope.orgProfileFilter.year})
        </th>
    </tr>
    <tr>
        <td class="tableHeaderCls"><digi:trn>Project title</digi:trn></td>
        <td class="tableHeaderCls"><digi:trn>Commitment</digi:trn><br/>(${sessionScope.orgProfileFilter.currName})</td>
        <c:if test="${sessionScope.orgProfileFilter.transactionType==2}">
        <td class="tableHeaderCls"><digi:trn>Disbursement</digi:trn><br/>(${sessionScope.orgProfileFilter.currName})</td>
        </c:if>
        <td class="tableHeaderCls"><digi:trn>Sector</digi:trn></td>
    </tr>

    <c:forEach items="${orgProfLargestProjectsForm.projects}" var="project"  varStatus="status">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${empty project.fullTitle}">
                        ${project.title}
                    </c:when>
                    <c:otherwise>
                        ${project.fullTitle}
                    </c:otherwise>
                </c:choose>

            </td>
            <td align="center">${project.amount}</td>
            <c:if test="${sessionScope.orgProfileFilter.transactionType==2}">
            <td align="center">${project.disbAmount}</td>
            </c:if>
            <td>
                <ul>
                    <c:forEach items="${project.sectorNames}" var="sectorName">
                        <li>
                            ${sectorName}
                        </li>
                    </c:forEach>
                </ul>
            </td>
        </tr>
    </c:forEach>

</table>
