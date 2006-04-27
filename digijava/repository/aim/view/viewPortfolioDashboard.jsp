<%@ page pageEncoding="UTF-8" %>
<%@ page import = "org.digijava.module.aim.helper.ChartGenerator" %>
<%@ page import = "java.io.PrintWriter" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%
	Long actId = (Long) request.getAttribute("activityId");
	Long indId = (Long) request.getAttribute("indicatorId");
	Integer pg = (Integer) request.getAttribute("page");
	
	String actPerfChartFileName = ChartGenerator.getPortfolioPerformanceChartFileName(
						 actId,indId,pg,session,new PrintWriter(out));

	String actPerfChartUrl = null;
	if (actPerfChartFileName.equals("chart_no_data.png") ||
						 actPerfChartFileName.equals("chart_error.png")) {
		actPerfChartUrl = request.getContextPath() + "/TEMPLATE/ampTemplate/images/" + actPerfChartFileName;
	} else {
		actPerfChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actPerfChartFileName;
	}
	
	String actRiskChartFileName = ChartGenerator.getPortfolioRiskChartFileName(
						 session,new PrintWriter(out));

	String actRiskChartUrl = null;
	if (actRiskChartFileName.equals("chart_no_data.png") ||
						 actRiskChartFileName.equals("chart_error.png")) {
		actRiskChartUrl = request.getContextPath() + "/TEMPLATE/ampTemplate/images/" + actRiskChartFileName;
	} else {
		actRiskChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actRiskChartFileName;
	}

%>

<digi:instance property="aimPortfolioDashboardForm"/>
<TABLE width="100%" cellPadding=0 cellSpacing=0 valign="top" align="left">
<TR><TD width="100%" valign="top" align="left">
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</TD></TR>
<TR><TD width="100%" valign="top" align="left">

<digi:form action="/viewPortfolioDashboard.do">
<table width="100%" cellspacing="2" cellpadding="2" valign="top" align="left" border=0>
	<tr>
		<td width="100%" align="left">
			<logic:notEmpty name="aimPortfolioDashboardForm" property="activityList">
				<html:select property="actId" styleClass="inp-text">
					<html:option value="-1">--All Activities--</html:option>
					<html:optionsCollection name="aimPortfolioDashboardForm" property="activityList" 
						value="ampActivityId" label="name" />					
				</html:select>
			</logic:notEmpty>
			<logic:notEmpty name="aimPortfolioDashboardForm" property="indicatorList">
				<html:select property="indId" styleClass="inp-text">
					<html:option value="-1">--All Indicators--</html:option>
					<html:optionsCollection name="aimPortfolioDashboardForm" property="indicatorList" 
						value="ampMEIndId" label="name" />					
				</html:select>
			</logic:notEmpty>			
			<input type="submit" value="Go" class="dr-menu">
		</td>
	</tr>
	<tr>
		<td width="100%">
			<img src="<%= actPerfChartUrl %>" width=730 height=400 border=0 usemap="#<%= actPerfChartFileName %>">
		</td>
	</tr>
	<logic:notEmpty name="aimPortfolioDashboardForm" property="pageList">
	<jsp:useBean id="urlParam" type="java.util.Map" class="java.util.HashMap"/>
	<tr>
		<td width="100%">
			Page : 
			<logic:iterate name="aimPortfolioDashboardForm" property="pageList" id="pge">
				<c:set target="${urlParam}" property="pge">
					<%=pge%>
				</c:set>			
				<digi:link href="/viewPortfolioDashboard.do" name="urlParam">
				<%=pge%></digi:link> |
			</logic:iterate>
		</td>
	</tr>
	</logic:notEmpty>
	<tr>
		<td width="100%">
			<img src="<%= actRiskChartUrl %>" width=730 height=400 border=0 usemap="#<%= actRiskChartFileName %>">
		</td>
	</tr>	
</table>
</digi:form>
</TD></TR>
</TABLE>

