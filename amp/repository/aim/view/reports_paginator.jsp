<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.digijava.ampModule.aim.form.ReportsForm"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule" %>
<%@ page import="org.digijava.ampModule.categorymanager.util.CategoryConstants"%>
<%@ taglib uri="/taglib/category" prefix="category" %>

<%@ page language="java" import="org.digijava.ampModule.aim.helper.TeamMember" %>

<%
ReportsForm aimTeamReportsForm = (ReportsForm) pageContext.getAttribute("aimTeamReportsForm");
java.util.List pagelist = new java.util.ArrayList();
for(int i = 0; i < aimTeamReportsForm.getTotalPages(); i++)
pagelist.add(new Integer(i + 1));
pageContext.setAttribute("pagelist",pagelist);
pageContext.setAttribute("maxpages", new Integer(aimTeamReportsForm.getTotalPages()));
pageContext.setAttribute("actualPage", new Integer(aimTeamReportsForm.getPage()));
%>
<c:set target="${urlParamsPagination}" property="action" value="getPage"/>
<c:if test="${aimTeamReportsForm.currentPage >0}">
    <c:set target="${urlParamsFirst}" property="page" value="0"/>
    <c:set target="${urlParamsFirst}" property="action" value="getPage"/>
    <c:if test="${aimTeamReportsForm.showTabs}">
        <c:set target="${urlParamsFirst}" property="tabs" value="true"/>
    </c:if>
    <c:set var="translation">
        <digi:trn key="aim:firstpage">First Page</digi:trn>
    </c:set>
    <digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsFirst" title="${translation}" ><digi:trn key="aim:firstpage">&lt;&lt;</digi:trn></digi:link>
    <c:set target="${urlParamsPrevious}" property="page" value="${aimTeamReportsForm.currentPage -1}"/>
    <c:set target="${urlParamsPrevious}" property="action" value="getPage"/>
    <c:if test="${aimTeamReportsForm.showTabs}">
        <c:set target="${urlParamsPrevious}" property="tabs" value="true"/>
    </c:if>
    &nbsp;|&nbsp;
    <c:set var="translation">
        <digi:trn key="aim:previous">Previous</digi:trn>
    </c:set>
    <digi:link href="/viewTeamReports.do" name="urlParamsPrevious" style="text-decoration=none" title="${translation}" >
        <digi:trn key="aim:previous">Previous</digi:trn>
    </digi:link>
    &nbsp;|&nbsp;
</c:if>

<c:set var="length" value="${aimTeamReportsForm.pagesToShow}"></c:set>
<c:set var="start" value="${aimTeamReportsForm.offset}"/>

<c:if test="${maxpages > 1}">
    <logic:iterate name="pagelist" id="pageidx" type="java.lang.Integer" offset="${start}" length="${length}">

        <c:set target="${urlParamsPagination}" property="page" value="${pageidx - 1}"/>
        <c:if test="${(pageidx - 1) eq actualPage}">
						                                							<span style="background-color: #FF6000; margin-left:2px; margin-right:2px; color: white; padding: 1px 3px; font-weight: bold; border: 1px solid #CCCCCC;">
						                                								<bean:write name="pageidx"/>
						                                							</span>
        </c:if>
        <c:if test="${(pageidx - 1) ne actualPage}">
            <c:if test="${aimTeamReportsForm.showTabs}">
                <c:set target="${urlParamsPagination}" property="tabs" value="true"/>
            </c:if>
            <digi:link href="/viewTeamReports.do"  name="urlParamsPagination" >
                <bean:write name="pageidx"/>
            </digi:link>
        </c:if>
        <c:if test="${(pageidx - 1) ne actualPage or actualPage != maxpages - 1}">&nbsp;|&nbsp;</c:if>
    </logic:iterate>
</c:if>
<c:if test="${aimTeamReportsForm.currentPage+1 != aimTeamReportsForm.totalPages}">
    <c:if test="${aimTeamReportsForm.currentPage+1 > aimTeamReportsForm.totalPages}">
        <c:set target="${urlParamsNext}" property="page" value="${aimTeamReportsForm.currentPage}"/>
    </c:if>
    <c:if test="${aimTeamReportsForm.currentPage+1 <= aimTeamReportsForm.totalPages}">
        <c:set target="${urlParamsNext}" property="page" value="${aimTeamReportsForm.currentPage+1}"/>
    </c:if>
    <c:set target="${urlParamsNext}" property="action" value="getPage"/>
    <c:set var="translation">
        <digi:trn key="aim:nextpage">Next Page</digi:trn>
    </c:set>
    <c:if test="${aimTeamReportsForm.showTabs}">
        <c:set target="${urlParamsNext}" property="tabs" value="true"/>
    </c:if>
    <!--&nbsp;|&nbsp;-->
    <digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsNext" title="${translation}">
        <digi:trn key="aim:next">Next</digi:trn>
    </digi:link>
    &nbsp;|&nbsp;
    <c:set target="${urlParamsLast}" property="page" value="${aimTeamReportsForm.totalPages-1}"/>
    <c:set target="${urlParamsLast}" property="action" value="getPage"/>
    <c:if test="${aimTeamReportsForm.showTabs}">
        <c:set target="${urlParamsLast}" property="tabs" value="true"/>
    </c:if>
    <c:set var="translation">
        <digi:trn key="aim:lastpage">Last Page</digi:trn>
    </c:set>
    <digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsLast" title="${translation}"><digi:trn key="aim:lastpage">&gt;&gt;</digi:trn></digi:link>
    &nbsp;&nbsp;
</c:if>