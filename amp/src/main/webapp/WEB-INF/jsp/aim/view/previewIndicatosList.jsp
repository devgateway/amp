<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://digijava.org/CategoryManager" prefix="category"%>
<%@ taglib uri="http://digijava.org/digi" prefix="digi"%>
<%@ taglib uri="http://digijava.org/fields" prefix="field"%>
<%@ taglib uri="http://digijava.org/features" prefix="feature"%>
<%@ taglib uri="http://digijava.org/modules" prefix="module"%>
<%@ taglib uri="http://digijava.org/aim" prefix="aim"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:if test="${not empty aimEditActivityForm.indicators}">

    <%-- Detect whether any indicator is linked to a specific country (multicountry activity) --%>
    <c:set var="isMulticountry" value="false" />
    <c:forEach var="_chk" items="${aimEditActivityForm.indicators}">
        <c:if test="${not empty _chk.activityLocation}">
            <c:set var="isMulticountry" value="true" />
        </c:if>
    </c:forEach>

    <c:choose>

        <%-- ================================================================
             MULTICOUNTRY: group indicators by country.
             Indicators without a country are shown first as "Common".
             ================================================================ --%>
        <c:when test="${isMulticountry}">

            <%-- Common (no country) indicators --%>
            <c:set var="hasCommon" value="false" />
            <c:forEach var="_chk2" items="${aimEditActivityForm.indicators}">
                <c:if test="${empty _chk2.activityLocation}">
                    <c:set var="hasCommon" value="true" />
                </c:if>
            </c:forEach>

            <c:if test="${hasCommon}">
                <h4 style="margin:8px 0 4px;"><digi:trn key="me:commonIndicators">Common Indicators</digi:trn></h4>
                <table width="100%">
                    <c:forEach var="indicator" items="${aimEditActivityForm.indicators}">
                        <c:if test="${empty indicator.activityLocation}">
                            <c:set var="currentIndicator" value="${indicator}" scope="request" />
                            <jsp:include page="previewIndicatorItem.jsp" />
                        </c:if>
                    </c:forEach>
                </table>
                <hr/>
            </c:if>

            <%-- Per-country groups: iterate outer loop to collect unique locations --%>
            <c:set var="seenLocations" value="|" />
            <c:forEach var="_outer" items="${aimEditActivityForm.indicators}">
                <c:if test="${not empty _outer.activityLocation}">
                    <c:set var="_locName" value="${_outer.activityLocation.location.name}" />
                    <c:set var="_locKey" value="|${_locName}|" />
                    <c:if test="${not fn:contains(seenLocations, _locKey)}">
                        <c:set var="seenLocations" value="${seenLocations}${_locName}|" />

                        <%-- Country section header --%>
                        <h4 style="margin:10px 0 4px; padding:4px 6px; background:#e8e8e8;">
                            ${_locName}
                        </h4>
                        <table width="100%">
                            <c:forEach var="indicator" items="${aimEditActivityForm.indicators}">
                                <c:if test="${not empty indicator.activityLocation
                                             and indicator.activityLocation.location.name == _locName}">
                                    <c:set var="currentIndicator" value="${indicator}" scope="request" />
                                    <jsp:include page="previewIndicatorItem.jsp" />
                                </c:if>
                            </c:forEach>
                        </table>
                        <hr/>
                    </c:if>
                </c:if>
            </c:forEach>

        </c:when>

        <%-- ================================================================
             SINGLE-COUNTRY: flat list (original behaviour)
             ================================================================ --%>
        <c:otherwise>
            <table width="100%">
                <c:forEach var="indicator" items="${aimEditActivityForm.indicators}">
                    <c:set var="currentIndicator" value="${indicator}" scope="request" />
                    <jsp:include page="previewIndicatorItem.jsp" />
                </c:forEach>
            </table>
            <hr/>
        </c:otherwise>

    </c:choose>

</c:if>

