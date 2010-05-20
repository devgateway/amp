<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<c:set var="colSpan">
    <c:choose>
        <c:when test="${sessionScope.orgProfileFilter.transactionType=='2'}">
            2
        </c:when>
        <c:otherwise>1</c:otherwise>

    </c:choose>
</c:set>
<digi:instance property="orgProfileNameValueYearForm"/>
<div style="height:200px">
    <table border="0" class="tableElement" bgcolor="#dddddd" width="100%" cellspacing="0" cellpadding="0">
        <tr>
            <th colspan="${6*colSpan}" class="tableHeaderCls">
                <c:choose>
                    <c:when test="${orgProfileNameValueYearForm.type==2}">
                        <digi:trn>TYPE OF AID</digi:trn>
                    </c:when>
                    <c:otherwise>
                        <digi:trn>ODA PROFILE</digi:trn>
                    </c:otherwise>
                </c:choose> (${sessionScope.orgProfileFilter.currName}
                <gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS%>" compareWith="true">
                    , <digi:trn>Amounts in Thousands</digi:trn>
                </gs:test>)</th>
        </tr>
        <tr>
            <td class="tableHeaderCls" rowspan="2">
                <c:choose>
                    <c:when test="${orgProfileNameValueYearForm.type==2}">
                        <digi:trn>TYPE OF AID</digi:trn>
                    </c:when>
                    <c:otherwise>
                        <digi:trn>ODA</digi:trn>
                    </c:otherwise>
                </c:choose>
            </td>
            <c:forEach var="year" items="4,3,2,1,0">

                <td class="tableHeaderCls" colspan="${colSpan}"> <c:out value="${sessionScope.orgProfileFilter.year-year}" /></td>
            </c:forEach>

        </tr>
        <tr>
            <c:forEach var="year" items="4,3,2,1,0">
                <c:choose>
                    <c:when test="${sessionScope.orgProfileFilter.transactionType=='1'}">
                        <td class="tableHeaderCls"><digi:trn>DISBURSEMENTS</digi:trn></td>
                    </c:when>
                    <c:when test="${sessionScope.orgProfileFilter.transactionType=='0'}">
                        <td class="tableHeaderCls"><digi:trn>COMMITMENTS</digi:trn></td>
                    </c:when>
                    <c:otherwise>
                        <td class="tableHeaderCls"><digi:trn>COMMITMENTS</digi:trn></td>
                        <td class="tableHeaderCls"><digi:trn>DISBURSEMENTS</digi:trn></td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tr>

        <c:forEach items="${orgProfileNameValueYearForm.values}" var="value" >
            <tr>
                <td nowrap>
                    <digi:trn>${value.name}</digi:trn>
                </td>
                <c:forEach var="amount" items="${value.values}">
                    <td align="center">${amount}</td>
                </c:forEach>
            </tr>
        </c:forEach>

    </table>
</div>



