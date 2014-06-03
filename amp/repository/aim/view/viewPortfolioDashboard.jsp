<%@ page pageEncoding="UTF-8" %>

<%@ page import = "org.digijava.module.aim.helper.ChartGenerator" %>

<%@ page import = "java.io.PrintWriter, java.util.*" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />

<digi:context name="digiContext" property="context" />


<div style='position:relative;display:none;'>
<digi:trn key="aim:risk:medium">Medium</digi:trn>
<digi:trn key="aim:risk:high">High</digi:trn>
<digi:trn key="aim:risk:low">Low</digi:trn>
<digi:trn key="aim:risk:veryhigh">Very High</digi:trn>
<digi:trn key="aim:risk:verylow">Very Low</digi:trn>
<digi:trn key="aim:performance:actual">Actual</digi:trn>
<digi:trn key="aim:performance:target">Target</digi:trn>
	<feature:display name="Activity" module="M & E"></feature:display>
	<feature:display name="Portfolio Dashboard" module="M & E"></feature:display>
	<feature:display name="Admin" module="M & E"></feature:display>

</div>



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

						 actId,indId,pg,session,new PrintWriter(out),400,500,"",true, request);



	String actPerfChartUrl = null;



	if (actPerfChartFileName != null) {

		actPerfChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actPerfChartFileName;

	}



	//String url = "/filterDesktopActivities.do";
	String url = "/";

	String actRiskChartFileName = ChartGenerator.getPortfolioRiskChartFileName(

						 session,new PrintWriter(out),370,400,url);



	String actRiskChartUrl = null;

	if (actRiskChartFileName != null) {

		actRiskChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actRiskChartFileName;

	}



%>

<digi:errors/>



<digi:form action="/viewPortfolioDashboard.do">

<div style="margin-left:auto; margin-right:auto; width:1000px;">
    <div id="content" class="yui-skin-sam" style="width:100%;">
        <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
            <ul class="yui-nav">
                <module:display name="National Planning Dashboard" parentModule="NATIONAL PLAN DASHBOARD">
                <li>
                    <digi:link href="/nationalPlaningDashboard.do">
                        <div>
	                        <digi:trn key="aim:nplDashboard">National Planning Dashboard</digi:trn>
                        </div>
                    </digi:link>
                </li>
                </module:display>
                <feature:display name="Portfolio Dashboard" module="M & E">
                <li class="selected">
                    <a style="cursor:pointer">
                        <div>
                            <digi:trn key="aim:portfolioDashboard">Dashboard</digi:trn>
                        </div>
                    </a>
                </li>
                </feature:display>
            </ul>
        </div>
	</div>
<div class="yui-content" style="background-color:#ffffff;border:1px solid #CCCCCC;height:auto;">
        <feature:display name="Portfolio Dashboard" module="M & E">
		<TABLE border="0" cellpadding="0" cellspacing="0" width="100%" >
			<TR><TD align=left>
				<TABLE cellspacing="0" cellpadding="0" align="center" vAlign="top" border="0" width="100%">

					<TR>

						<TD vAlign="top" align="center">

							<TABLE width="99%" cellspacing="0" cellpadding="0" vAlign="top" align="center">
                                 <TR>
                                       <TD>&nbsp;
                                     
                                     </TD>
                                </TR>
                                  <TR>
                                       <TD BGCOLOR="#C7D4DB" style="font-weight : bold; padding:6px;">
                                      	<field:display name="Printer Friendly Button Performance" feature="Portfolio Dashboard">
                                   
                                        <a style="cursor:pointer; color:#376091; font-size:12px;" onclick="javascript:showPrinterFriendlyPortPerf()"><digi:trn >Printer Friendly Version</digi:trn></a>

                                    </field:display>
                                     </TD>
                                </TR>
                                 <TR>
                                       <TD>&nbsp;
                                     
                                     </TD>
                                </TR>

								<TR><TD class="highlight" align="center">

									<digi:trn key="aim:overallPortfolioRisk">Overall Risk</digi:trn>:

									<font color="<bean:write name="aimPortfolioDashboardForm" property="fontColor" />">


									<bean:define id="riskName" name="aimPortfolioDashboardForm" property="overallRisk" toScope="page"
									type="java.lang.String"/>
									<logic:notEmpty name="riskName">

										<digi:trn key='<%="aim:" + riskName%>'><%=riskName%></digi:trn>
									</logic:notEmpty>
                                    </font>

								</TD></TR>

								<TR><TD>

									<TABLE cellspacing="2" cellpadding="2" valign="top" align="center" border="0">

										<TR>

											<TD align="center" style="font-weight : bold;font-size: large;" height="20">

												<digi:trn key="aim:mePortfolioPerformance">

												 Performance</digi:trn>

											</TD>

											<TD align="center" style="font-weight : bold;font-size: large;" height="20">

												<digi:trn key="aim:mePortfolioRisk">

												Risk</digi:trn>

											</TD>

										</TR>

										<TR>

											<TD>

												<% if (actPerfChartUrl != null) { %>

													<img src="<%= actPerfChartUrl %>" width=400 height=500 border="0" usemap="#<%= actPerfChartFileName %>">

													<br><br>

													<div align="center">
													<field:display name="Without Baseline Button Performance" feature="Portfolio Dashboard">
													<input type="button" class="buttonx" value="<digi:trn key='btn:withoutBaseline'>Without Baseline</digi:trn>"
													onclick="javascript:portPerfWithoutBaseline()">
													</field:display>													
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

													<img src="<%= actRiskChartUrl %>" width=370 height=400 border="0" usemap="#<%= actRiskChartFileName %>">

													<br><br>

													<div align="center">
													<field:display name="Printer Friendly Button Risk" feature="Portfolio Dashboard">
													<input type="button" class="buttonx" value="<digi:trn key='btn:printerfriendly'>Printer Friendly</digi:trn>"

													onclick="javascript:showPrinterFriendlyPortRisk()">
													</field:display>

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
		</feature:display>
</div>
</div>

</digi:form>

