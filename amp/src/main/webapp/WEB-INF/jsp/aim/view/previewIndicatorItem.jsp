<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi"%>
<%@ taglib uri="http://digijava.org/fields" prefix="field"%>
<%@ taglib uri="http://digijava.org/features" prefix="feature"%>
<%@ taglib uri="http://digijava.org/modules" prefix="module"%>
<%@ taglib uri="http://digijava.org/aim" prefix="aim"%>
<%--
    Renders a single IndicatorActivity row.
    Expects: requestScope.currentIndicator = the IndicatorActivity instance.
--%>
<c:set var="indicator" value="${currentIndicator}" />

<tr bgcolor="#f0f0f0">
    <td width="40%">
        <module:display name="/Activity Form/M&E/Name" parentModule="/Activity Form/M&E">
            <digi:trn key="me:name">Name</digi:trn>
        </module:display>
    </td>
    <td width="10%">
        <module:display name="/Activity Form/M&E/Code" parentModule="/Activity Form/M&E">
            <digi:trn key="me:code">Code</digi:trn>
        </module:display>
    </td>
    <td width="10%">
        <module:display name="/Activity Form/M&E/ME Item/Logframe Category"
                        parentModule="/Activity Form/M&E/ME Item">
            <digi:trn key="me:logFrame">LogFrame</digi:trn>
        </module:display>
    </td>
    <td width="40%">
        <digi:trn key="me:sectors">Sectors</digi:trn>
    </td>
</tr>
<tr>
    <td width="40%" valign="top">
        <module:display name="/Activity Form/M&E/Name" parentModule="/Activity Form/M&E">
            <span class="word_break bold">${indicator.indicator.name}</span>
        </module:display>
    </td>
    <td width="10%" valign="top">
        <module:display name="/Activity Form/M&E/Code" parentModule="/Activity Form/M&E">
            <span class="word_break bold">${indicator.indicator.code}</span>
        </module:display>
    </td>
    <td width="10%" valign="top">
        <module:display name="/Activity Form/M&E/ME Item/Logframe Category"
                        parentModule="/Activity Form/M&E/ME Item">
            <c:if test="${not empty indicator.values}">
                <span class="word_break bold">${indicator.logFrame}</span>
            </c:if>
        </module:display>
    </td>
    <td width="40%" valign="top">
        <c:forEach var="sector" items="${indicator.indicator.sectors}">
            <ul>
                <li><span class="word_break bold">${sector.name}</span></li>
            </ul>
        </c:forEach>
    </td>
</tr>

<%-- Indicator values (Base / Current / Target / Revised) --%>
<tr>
    <td align="right" width="100%" colspan="4">
        <table width="100%">
            <tr bgcolor="#f0f0f0">
                <td width="20%"><digi:trn key="me:type">Type</digi:trn></td>
                <td width="10%"><digi:trn key="me:value">Value</digi:trn></td>
                <td width="50%"><digi:trn key="me:comment">Comment</digi:trn></td>
                <td width="20%"><digi:trn key="me:date">Date</digi:trn></td>
            </tr>
            <c:forEach var="value" items="${indicator.valuesSorted}">
                <tr>
                    <td width="20%">
                        <span class="word_break bold">
                            <c:set var="fieldName"></c:set>
                            <c:choose>
                                <c:when test="${value.valueType == 1}">
                                    <c:set var="fieldName">Current</c:set>
                                    <c:set var="fieldNameLabel">Current Value</c:set>
                                </c:when>
                                <c:when test="${value.valueType == 2}">
                                    <c:set var="fieldName">Base</c:set>
                                    <c:set var="fieldNameLabel">Base Value</c:set>
                                </c:when>
                                <c:when test="${value.valueType == 0}">
                                    <c:set var="fieldName">Target</c:set>
                                    <c:set var="fieldNameLabel">Target Value</c:set>
                                </c:when>
                                <c:when test="${value.valueType == 3}">
                                    <c:set var="fieldName">Revised</c:set>
                                    <c:set var="fieldNameLabel">Revised Target Value</c:set>
                                </c:when>
                            </c:choose>
                            <digi:trn key="me:${fieldNameLabel}">${fieldNameLabel}</digi:trn>
                        </span>
                    </td>
                    <td width="10%">
                        <module:display name="/Activity Form/M&E/ME Item/${fieldName} Value/${fieldName} Value"
                                        parentModule="/Activity Form/M&E/ME Item/${fieldName} Value">
                            <span class="word_break"><aim:formatNumber value="${value.value}"/></span>
                        </module:display>
                    </td>
                    <td width="50%">
                        <module:display name="/Activity Form/M&E/ME Item/${fieldName} Value/${fieldName} Comments"
                                        parentModule="/Activity Form/M&E/ME Item/${fieldName} Value">
                            <span class="word_break">${fn:escapeXml(value.comment)}</span>
                        </module:display>
                    </td>
                    <td width="20%">
                        <module:display name="/Activity Form/M&E/ME Item/${fieldName} Value/${fieldName} Date"
                                        parentModule="/Activity Form/M&E/ME Item/${fieldName} Value">
                            <span class="word_break"><aim:formatDate value="${value.valueDate}"> </aim:formatDate></span>
                        </module:display>
                    </td>
                </tr>
                <tr>
                    <td colspan="5" width="100%"><hr/></td>
                </tr>
            </c:forEach>
        </table>
    </td>
</tr>

<%-- Disaggregation values --%>
<c:if test="${not empty indicator.indicator.disaggregationValues}">
    <tr>
        <td colspan="4">
            <table width="100%" style="margin-top:6px; border-top: 1px solid #ccc;">
                <tr bgcolor="#e8e8e8">
                    <td colspan="7" style="padding:4px 0;">
                        <b><digi:trn key="me:disaggregationValues">Disaggregation Values</digi:trn></b>
                    </td>
                </tr>
                <tr bgcolor="#f0f0f0">
                    <td width="15%"><digi:trn key="me:parentCategory">Category</digi:trn></td>
                    <td width="15%"><digi:trn key="me:childCategory">Sub-Category</digi:trn></td>
                    <td width="10%"><digi:trn key="me:baseValue">Base Value</digi:trn></td>
                    <td width="10%"><digi:trn key="me:baseValueDate">Base Date</digi:trn></td>
                    <td width="10%"><digi:trn key="me:targetValue">Target Value</digi:trn></td>
                    <td width="10%"><digi:trn key="me:targetValueDate">Target Date</digi:trn></td>
                    <td width="30%"><digi:trn key="me:actualValues">Actual Values</digi:trn></td>
                </tr>
                <c:forEach var="dv" items="${indicator.indicator.disaggregationValues}">
                    <tr>
                        <td>${dv.parentCategory.value}</td>
                        <td>${dv.childCategory.value}</td>
                        <td>
                            <c:if test="${not empty dv.baseValue}">
                                <span class="word_break"><aim:formatNumber value="${dv.baseValue.originalValue}"/></span>
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${not empty dv.baseValue}">
                                <span class="word_break"><aim:formatDate value="${dv.baseValue.originalValueDate}"> </aim:formatDate></span>
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${not empty dv.targetValue}">
                                <span class="word_break"><aim:formatNumber value="${dv.targetValue.originalValue}"/></span>
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${not empty dv.targetValue}">
                                <span class="word_break"><aim:formatDate value="${dv.targetValue.originalValueDate}"> </aim:formatDate></span>
                            </c:if>
                        </td>
                        <td>
                            <c:forEach var="av" items="${dv.actualValues}">
                                <span class="word_break">
                                    <aim:formatNumber value="${av.originalValue}"/>
                                    <c:if test="${not empty av.originalValueDate}">
                                        (<aim:formatDate value="${av.originalValueDate}"> </aim:formatDate>)
                                    </c:if>
                                </span><br/>
                            </c:forEach>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </td>
    </tr>
</c:if>
