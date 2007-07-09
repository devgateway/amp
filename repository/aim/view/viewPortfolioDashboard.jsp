<%@ page pageEncoding="UTF-8" %>
<%@ page import = "org.digijava.module.aim.helper.ChartGenerator" %>
<%@ page import = "java.io.PrintWriter, java.util.*" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/relatedLinks.js"/>"></script>
<digi:context name="digiContext" property="context" />

<script language="Javascript">
	function showPrinterFriendlyPortPerf() {
		<digi:context name="ptUrl" property="context/module/moduleinstance/viewPrintablePortPerf.do" />
		var url = "<%=ptUrl%>";
	 	openURLinWindow(url,650,500);
	}

	function showPrinterFriendlyPortRisk() {
		<digi:context name="ptUrl" property="context/module/moduleinstance/viewPrintablePortRisk.do" />
		var url = "<%=ptUrl%>";
	 	openURLinWindow(url,650,450);
	}
	
	function portPerfWithoutBaseline() {
		<digi:context name="ptUrl" property="context/module/moduleinstance/viewPortPerfWithoutBase.do" />
		var url = "<%=ptUrl%>";
	 	openURLinWindow(url,650,500);			  
	}

</script>

<%
	Long actId = (Long) session.getAttribute("activityId");
	Long indId = (Long) session.getAttribute("indicatorId");
	Integer pg = (Integer) session.getAttribute("page");
	
	String actPerfChartFileName = ChartGenerator.getPortfolioPerformanceChartFileName(
						 actId,indId,pg,session,new PrintWriter(out),400,500,"",true);

	String actPerfChartUrl = null;
	
	if (actPerfChartFileName != null) {
		actPerfChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actPerfChartFileName;
	}
	
	//String url = "/filterDesktopActivities.do";
	String url = "/aim/showDesktop.do";
	String actRiskChartFileName = ChartGenerator.getPortfolioRiskChartFileName(
						 session,new PrintWriter(out),370,400,url);

	String actRiskChartUrl = null;
	if (actRiskChartFileName != null) {
		actRiskChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actRiskChartFileName;
	}

%>
<digi:errors/>

<digi:form action="/viewPortfolioDashboard.do">

<TABLE width="810" cellspacing="1" cellpadding="4" valign="top" align="left">

	<TR><TD>R
		<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" >
      	<TR><TD>
           	<TABLE border=0 cellPadding=0 cellSpacing=0 >
           		<TR>
              		<TD bgColor=#c9c9c7 class=box-title width=80>
							&nbsp;<digi:trn key="aim:portfolioDashboard">Dashboard</digi:trn>
						</TD>
                 	<TD background="module/aim/images/corner-r.gif" 
						height=17 width=17></TD>
              		<TD bgColor=#c9c9c7 class=box-title width=220 nowrap="nowrap">
							&nbsp;<digi:link href="/nationalPlaningDashboard.do"><digi:trn key="aim:npDashboard">National Planing Dashboard</digi:trn></digi:link>
						</TD>
                 	<TD background="module/aim/images/corner-r.gif" 
						height=17 width=17></TD>
					</TR>
		</TABLE>
			</TD></TR>
			<TR><TD bgColor=#ffffff class=box-border align=left>

				<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
					<TR>
						<TD vAlign="top" align="center">
							<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top" align="center">
								<TR><TD class="highlight">
									<digi:trn key="aim:overallPortfolioRisk">Overall Risk</digi:trn>: 
									<font color="<bean:write name="aimPortfolioDashboardForm" property="fontColor" />">

									<bean:define id="riskName" name="aimPortfolioDashboardForm" property="overallRisk" toScope="page"
									type="java.lang.String"/>
									<logic:notEmpty name="riskName">
										<digi:trn key='<%="aim:" + riskName%>'><%=riskName%></digi:trn>
									</logic:notEmpty>
								</TD></TR>								
								<TR><TD>
									<TABLE cellspacing="2" cellpadding="2" valign="top" align="left" border=0>
										<TR>
											<TD align="center" class="textalb" height="20" bgcolor="#336699">
												<digi:trn key="aim:mePortfolioPerformance">
												Portfolio - Performance</digi:trn>
											</TD>
											<TD align="center" class="textalb" height="20" bgcolor="#336699">
												<digi:trn key="aim:mePortfolioRisk">
												Portfolio - Risk</digi:trn>
											</TD>
										</TR>
										<TR>
											<TD>
												<% if (actPerfChartUrl != null) { %>
													<img src="<%= actPerfChartUrl %>" width=400 height=500 border=0 usemap="#<%= actPerfChartFileName %>">
													<br><br>
													<div align="center">
													<input type="button" class="buton" value="Printer Friendly"
													onclick="javascript:showPrinterFriendlyPortPerf()">
													<input type="button" class="buton" value="Without Baseline"
													onclick="javascript:portPerfWithoutBaseline()">													
													</div>
												<% } else { %>
													<br><span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
													<digi:trn key="aim:portfolioPerformanceChart">Portfolio-Performance chart</digi:trn>
												 	</span><br><br>
												<% } %>
												
												<logic:notEmpty name="aimPortfolioDashboardForm" property="pageList">
													<jsp:useBean id="urlParam" type="java.util.Map" class="java.util.HashMap"/>
													Page : 
													<logic:iterate name="aimPortfolioDashboardForm" property="pageList" id="pge">
														<c:set target="${urlParam}" property="pge">
															<%=pge%>
														</c:set>			
														<digi:link href="/viewPortfolioDashboard.do" name="urlParam">
														<%=pge%></digi:link> |
													</logic:iterate>
												</logic:notEmpty>							
											</TD>
											<TD valign="top">
												<% if (actRiskChartUrl != null) { %>
													<img src="<%= actRiskChartUrl %>" width=370 height=400 border=0 usemap="#<%= actRiskChartFileName %>">
													<br><br>
													<div align="center">
													<input type="button" class="buton" value="Printer Friendly" 
													onclick="javascript:showPrinterFriendlyPortRisk()">
													</div>						  
												<% } else { %>
													<br><span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>
											  		<digi:trn key="aim:portfolioRiskChart">Portfolio-Risk chart</digi:trn>
												  	</span><br><br>
												<% } %>								
											</TD>
										</TR>
									</TABLE>
								</TD></TR>
							</TABLE>
						</TD>
					</TR>
					<TR><TD>&nbsp;</TD></TR>
				</TABLE>
			</TD></TR>
		</TABLE>
	</TD></TR>
</TABLE>
</digi:form>
