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
	Long actId = (Long) request.getAttribute("actId");
	
	String actPerfChartFileName = ChartGenerator.generateActivityPerformanceChart(
						 actId,session,new PrintWriter(out));

	String actPerfChartUrl = null;
	if (actPerfChartFileName.equals("chart_no_data.png") ||
						 actPerfChartFileName.equals("chart_error.png")) {
		actPerfChartUrl = request.getContextPath() + "/TEMPLATE/ampTemplate/images/" + actPerfChartFileName;
	} else {
		actPerfChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actPerfChartFileName;
	}
	
	String actRiskChartFileName = ChartGenerator.generateActivityRiskChart(
						 actId,session,new PrintWriter(out));

	String actRiskChartUrl = null;
	if (actRiskChartFileName.equals("chart_no_data.png") ||
						 actRiskChartFileName.equals("chart_error.png")) {
		actRiskChartUrl = request.getContextPath() + "/TEMPLATE/ampTemplate/images/" + actRiskChartFileName;
	} else {
		actRiskChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actRiskChartFileName;
	}

%>

<table width="100%" cellspacing="2" cellpadding="2" valign="top" align="left" border=0>
	<tr>
		<td width="50%">
			<img src="<%= actPerfChartUrl %>" width=400 height=350 border=0 usemap="#<%= actPerfChartFileName %>">
		</td>
		<td width="50%">
			<img src="<%= actRiskChartUrl %>" width=400 height=350 border=0 usemap="#<%= actRiskChartFileName %>">
		</td>
	</tr>
	</tr>
</table>

