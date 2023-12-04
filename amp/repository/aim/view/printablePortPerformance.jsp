<%@ page pageEncoding="UTF-8" %>
<%@ page import = "org.digijava.module.aim.helper.ChartGenerator" %>
<%@ page import = "java.io.PrintWriter, java.util.*" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="javascript">
	
	function load() {}
	function unload() {}

</script>

<%
	Long actId = (Long) session.getAttribute("activityId");
	Long indId = (Long) session.getAttribute("indicatorId");
	Integer pg = (Integer) session.getAttribute("page");
	
	
	String actPerfChartFileName = ChartGenerator.getPortfolioPerformanceChartFileName(
			 actId,indId,pg,session,new PrintWriter(out),600,450,"",true,request);

	String actPerfChartUrl = actPerfChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actPerfChartFileName;

%>

<table width="100%" cellspacing="2" cellpadding="2" valign="top" align="left" border="0">
	<tr>
		<td width="100%" colspan="2">
			<img src="<%= actPerfChartUrl %>" width=600 height=450 border="0" usemap="#<%= actPerfChartFileName %>">
		</td>
	</tr>
	<logic:notEmpty name="aimPortfolioDashboardForm" property="pageList">
	<jsp:useBean id="urlParam" type="java.util.Map" class="java.util.HashMap"/>
	<tr>
		<td width="100%" colspan="2">
			Page : 
			<logic:iterate name="aimPortfolioDashboardForm" property="pageList" id="pge">
				<c:set target="${urlParam}" property="pge">
					<%=pge%>
				</c:set>			
				<c:set target="${urlParam}" property="cType" value="P" />
				<digi:link href="/viewPortfolioDashboard.do" name="urlParam">
				<%=pge%></digi:link> |
			</logic:iterate>
		</td>
	</tr>
	</logic:notEmpty>

	<tr>
		<td width="50%" align="right">
			<a href="javascript:window.close()">
			<digi:trn key="um:closeThisWindow">Close this window</digi:trn></a> |
		</td>
		<td width="50%" align="left">
			<a href="javascript:window.print()">
			<digi:trn key="aim:print">Print</digi:trn></a>
		</td>
	</tr>		
</table>
