<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

    
<digi:instance property="orgProfileNameValueYearForm"/>
<div style="height:200px">
    <table border="0" class="tableElement" bgcolor="#dddddd" width="100%" cellspacing="0" cellpadding="0">
                <tr>
                    <th colspan="6" class="tableHeaderCls">
                        <c:choose>
                            <c:when test="${orgProfileNameValueYearForm.type==2}">
                                <digi:trn>TYPE OF AID</digi:trn>
                            </c:when>
                            <c:otherwise>
                                <digi:trn>ODA PROFILE</digi:trn>
                            </c:otherwise>
                        </c:choose> (
                        <c:choose>
                            <c:when test="${sessionScope.orgProfileFilter.transactionType=='1'}">
                                <digi:trn>DISBURSEMENTS</digi:trn>
                            </c:when>
                            <c:otherwise>
                                <digi:trn>COMMITMENTS</digi:trn>
                            </c:otherwise>
                        </c:choose>|${sessionScope.orgProfileFilter.currName}<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true">
    , <digi:trn>Amounts in Thousands</digi:trn>
</gs:test>)</th>
                </tr>
                <tr>
                    <td class="tableHeaderCls">
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
                                <td class="tableHeaderCls"> <c:out value="${sessionScope.orgProfileFilter.year-year}" /></td>
                            </c:forEach>
                         

                        </tr>

                        <c:forEach items="${orgProfileNameValueYearForm.values}" var="value"  varStatus="status">
                            <tr>
                                <td nowrap>
                                    <digi:trn>${value.name}</digi:trn>
                                </td>
                                <c:forEach var="amount" items="${value.yearValues}">
                                    <td align="center">${amount.value}</td>
                                </c:forEach>
                            </tr>
                        </c:forEach>

                    </table>
        </div>
         
      

