<%@ page pageEncoding="UTF-8" %>
<%@ page import = "org.digijava.module.aim.helper.ChartGenerator" %>
<%@ page import = "java.io.PrintWriter" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="Javascript">
	function showPrinterFriendly(actId,indId,chartType) {
		<digi:context name="ptUrl" property="context/module/moduleinstance/viewPortfolioDashboard.do" />
		var url = "<%=ptUrl%>?actId="+actId+"&indId="+indId+"&cType="+chartType;
	 	openURLinWindow(url,650,450);
	}

</script>

<%
	Long actId = (Long) request.getAttribute("activityId");
	Long indId = (Long) request.getAttribute("indicatorId");
	Integer pg = (Integer) request.getAttribute("page");
	
	String actPerfChartFileName = ChartGenerator.getPortfolioPerformanceChartFileName(
						 actId,indId,pg,session,new PrintWriter(out),730,400);

	String actPerfChartUrl = null;
	
	if (actPerfChartFileName != null) {
		actPerfChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actPerfChartFileName;
	}
	
	String actRiskChartFileName = ChartGenerator.getPortfolioRiskChartFileName(
						 session,new PrintWriter(out),730,400);

	String actRiskChartUrl = null;
	if (actRiskChartFileName != null) {
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
			<% if (actPerfChartUrl != null) { %>
				<img src="<%= actPerfChartUrl %>" width=730 height=400 border=0 usemap="#<%= actPerfChartFileName %>">
				<br><br>
				<div align="left">
				<input type="button" class="buton" value="Printer Friendly Version" 
				onclick="javascript:showPrinterFriendly('<%=actId%>','<%=indId%>','P')">
				</div>						  
			<% } else { %>
				<br><span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
						  <digi:trn key="aim:portfolioPerformanceChart">Portfolio-Performance chart</digi:trn>
						  </span><br><br>
			<% } %>		

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
			<% if (actRiskChartUrl != null) { %>
				<img src="<%= actRiskChartUrl %>" width=730 height=400 border=0 usemap="#<%= actRiskChartFileName %>">
				<br><br>
				<div align="left">
				<input type="button" class="buton" value="Printer Friendly Version" 
				onclick="javascript:showPrinterFriendly('<%=actId%>','<%=indId%>','R')">
				</div>						  
			<% } else { %>
				<br><span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
						  <digi:trn key="aim:portfolioRiskChart">Portfolio-Risk chart</digi:trn>
						  </span><br><br>
			<% } %>		
		</td>
	</tr>	
</table>
</digi:form>
</TD></TR>
</TABLE>

