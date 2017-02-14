<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/aim" prefix="aim"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

    <c:if test="${not empty aimEditActivityForm.indicators}">
        <table width="100%">
            <c:forEach var="indicator" items="${aimEditActivityForm.indicators}">
                <tr bgcolor="#f0f0f0">
                    <td width="40%">
                        <field:display name="Indicator Name" feature="Activity">
                            <digi:trn key="me:name">Name</digi:trn>
                        </field:display>
                    </td>
                    <td width="10%">
                        <field:display name="Indicator Name" feature="Activity">
                            <digi:trn key="me:code">Code</digi:trn>
                        </field:display>
                    </td>
                    <td width="10%">
                        <field:display name="Logframe Category" feature="Activity">
                            <digi:trn key="me:logFrame">LogFrame</digi:trn>
                        </field:display>
                    </td>
                    <td width="40%">
                        <field:display name="Sectors" feature="Admin">
                            <digi:trn key="me:sectors">Sectors</digi:trn>
                        </field:display>
                    </td>
                </tr>
                <tr>
                    <td width="40%" valign="top">
                        <field:display name="Indicator Name" feature="Activity">
                            <span class="word_break bold">${indicator.indicator.name}</span>
                        </field:display>
                    </td>
                    <td width="10%" valign="top">
                        <field:display name="Indicator Name" feature="Activity">
                            <span class="word_break bold">${indicator.indicator.code}</span>
                        </field:display>
                    </td>
                    <td width="10%" valign="top">
                        <field:display name="Logframe Category" feature="Activity">
                            <c:if test="${not empty indicator.values}">
                                <span class="word_break bold">${indicator.logFrame}</span>
                            </c:if>
                        </field:display>
                    </td>
                    <td width="40%" valign="top">
                        <field:display name="Sectors" feature="Admin">
                            <c:forEach var="sector" items="${indicator.indicator.sectors}">
                                <ul>
                                    <li><span class="word_break bold">${sector.name}</span></li>
                                </ul>
                            </c:forEach>
                        </field:display>
                    </td>
                </tr>
                <tr>
                    <td align="right" width="100%" colspan="4">
                        <table width="100%">
                            <tr bgcolor="#f0f0f0">
                                <td width="20%">
                                    <digi:trn key="me:type">Type</digi:trn>
                                </td>
                                <td width="10%">
                                    <digi:trn key="me:value">Value</digi:trn>
                                </td>
                                <td width="50%">
                                    <digi:trn key="me:comment">Comment</digi:trn>
                                </td>
                                <td width="20%">
                                    <digi:trn key="me:date">Date</digi:trn>
                                </td>
                            </tr>
                            <c:forEach var="value" items="${indicator.valuesSorted}">

                                <tr>
                                    <td width="20%"><span class="word_break bold">
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
                                                        <c:set var="fieldName">Revised Target</c:set>
                                                        <c:set var="fieldNameLabel">Revised Target Value</c:set>
                                                    </c:when>
                                                </c:choose>
												<digi:trn key="me:${fieldNameLabel}">${fieldNameLabel}</digi:trn>
												</span></td>
                                    <td width="10%">
                                        <field:display name="Indicator ${fieldName} Value" feature="Activity">
                                            <span class="word_break"><aim:formatNumber value="${value.value}"/></span>
                                        </field:display>
                                    </td>
                                    <td width="50%">
                                        <field:display name="Comments ${fieldName} Value" feature="Activity">
                                            <span class="word_break">${fn:escapeXml(value.comment)}</span>
                                        </field:display>
                                    </td>
                                    <td width="20%">
                                        <field:display name="Date ${fieldName} Value" feature="Activity">
                                            <span class="word_break"><aim:formatDate value="${value.valueDate}"> </aim:formatDate></span>
                                        </field:display>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="5" width="100%">
                                        <hr/>
                                    </td>
                                </tr>

                            </c:forEach>
                        </table>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <hr/>
    </c:if>
