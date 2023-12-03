<%@page import="org.digijava.ampModule.aim.helper.GlobalSettingsConstants"%>
<%@page import="org.digijava.ampModule.aim.util.FeaturesUtil"%>
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
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ page language="java" import="org.digijava.ampModule.aim.helper.TeamMember" %>

<%if(FeaturesUtil.isVisibleModule("Public Reports")){ %> 

<!-- this is for the nice tooltip widgets -->

<DIV id="TipLayer" style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
	<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/dscript120.js"/>"></script>
	<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/dscript120_ar_style.js"/>"></script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/arFunctions.js"/>"></script>
<script type="text/javascript">
<!--
function popup(mylink, windowname)
{
	if (!window.focus)
		return true;

	var href;
	if (typeof(mylink) == 'string')
		href = mylink;
	else
		href = mylink.href;

	if(windowname == ""){
		windowname="popup"+new Date().getTime();
	}
	
	var openedWindow = window.open('', windowname, 'channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes');
	if(navigator.appName.indexOf('Microsoft Internet Explorer') > -1){ //Workaround to allow HTTP REFERER to be sent in IE (AMP-12638)
		var referLink = document.createElement('a');
		referLink.href = href;
		referLink.target = windowname;
		document.body.appendChild(referLink);
		referLink.click();
	}
	else
	{
		openedWindow.location = href;
	}
	return false;
}
function submitForm(action){
    document.aimTeamReportsForm.action.value=action;
    document.aimTeamReportsForm.submit();
    
}
//-->
</script>
<style>
.publicReportsTable THEAD TD {
	font-family: Arial;
	font-size:10px;
	background-color:#27415f;
	color:#FFFFFF;
	border-bottom:1px solid black;
	border-right:1px solid black;
}
.publicReportsTable TBODY TD {
	font-family: Arial;
	font-size:10px;
	color:black;
	border-right:1px solid #666666;
}
.publicReportsTable TBODY TD A {
	font-family: Arial;
	font-size:10px;
}



</style>
<digi:instance property="aimTeamReportsForm" />
<digi:form action="/viewTeamReports.do" method="post">
<html:hidden property="action"/>

<c:if test="${!aimTeamReportsForm.showTabs}">
  <c:set var="pageTitle">
    <digi:trn> List of Reports </digi:trn>
  </c:set>
  
  <c:set var="titleColumn">
    <digi:trn> Report Title </digi:trn>
  </c:set>
</c:if>

<hr />
<!-- MAIN CONTENT PART START -->
<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td>
								<div id="content" class="yui-skin-sam">
									<div id="demo" class="yui-navset" style="width: 1000px;">
										<ul class="yui-nav" id="MyTabs">
											<li class="selected"><a rel="Tab_Name" href="#"
												id="Tab-tab tertiary" onclick="return false;">
													<div>${pageTitle}</div> </a>
											</li>
										</ul>
										<div id="Tab_Name"
											class="ui-tabs-panel ui-widget-content ui-corner-bottom reports-tab-name-public-container content-direction">

											<table bgcolor="#FFFFFF"
												style="width: 970px; border-left: 1px solid #CCCCCC; border-top: 1px
												solid #CCCCCC; border-bottom: 1px solid #CCCCCC; border-right: 1px solid #CCCCCC;">
												<tr>
													<td>
													<div style="float:right; margin-right:15px; margin-top:8px;">
													<!--  Show XML, PDF and Printer Friendly icon removed with AMP-20044
													<div class="t_sm">
												            <b><digi:trn>Icons Reference</digi:trn></b>
														<br/>
														<img src="img_2/ico_exc.gif">&nbsp;&nbsp;<digi:trn>Click on this icon to get report in Excel format</digi:trn> &nbsp;&nbsp;|&nbsp;&nbsp;
														<img src="img_2/ico_pdf.gif">&nbsp;&nbsp;<digi:trn>Click on this icon to get report in PDF format</digi:trn>
													</div>
													-->
													</div>
														<table cellpadding="6" cellspacing="6">
															<tr>
																<td id="reportsearchform"><digi:trn>Report Title</digi:trn>:
																	<html:text property="keyword" />
																</td>
																<td id="reportsearchform1">
                                                                                                                                    <input type="button"  value="<digi:trn>Search</digi:trn>" onclick="submitForm('search')"/></td> 
                                                                                                                                
                                                                                                                                </td>
																<td id="reportsearchform2"><input type="button"  value="<digi:trn>clear</digi:trn>" onclick="submitForm('clear')"/>
															</tr>
														</table>
													</td>
												</tr>

												<c:set var="reportNumber"
													value="${fn:length(aimTeamReportsForm.reports)}"></c:set>
												<c:if test="${reportNumber != 0}">
													<tr style="font-size: 11px; font-family: Aria, sans-serif;">
														<td>
															<%
																ReportsForm aimTeamReportsForm = (ReportsForm) pageContext
																					.getAttribute("aimTeamReportsForm");
																			java.util.List pagelist = new java.util.ArrayList();
																			for (int i = 0; i < aimTeamReportsForm.getTotalPages(); i++)
																				pagelist.add(new Integer(i + 1));
																			pageContext.setAttribute("pagelist", pagelist);
																			pageContext.setAttribute("maxpages", new Integer(
																					aimTeamReportsForm.getTotalPages()));
																			pageContext.setAttribute("actualPage", new Integer(
																					aimTeamReportsForm.getPage()));
															%> <jsp:useBean
																id="urlParamsPagination" type="java.util.Map"
																class="java.util.HashMap" /> <c:set
																target="${urlParamsPagination}" property="action"
																value="getPage" /> <c:if
																test="${aimTeamReportsForm.currentPage >0}">
																<jsp:useBean id="urlParamsFirst" type="java.util.Map"
																	class="java.util.HashMap" />
																<c:set target="${urlParamsFirst}" property="page"
																	value="0" />
																<c:set target="${urlParamsFirst}" property="action"
																	value="getPage" />
																<c:if test="${aimTeamReportsForm.showTabs}">
																	<c:set target="${urlParamsFirst}" property="tabs"
																		value="true" />
																</c:if>
																<c:set var="translation">
																	<digi:trn key="aim:firstpage">First Page</digi:trn>
																</c:set>
																<digi:link href="/viewTeamReports.do"
																	style="text-decoration=none" name="urlParamsFirst"
																	title="${translation}">
																	<digi:trn key="aim:firstpage">First Page</digi:trn>
																</digi:link>
																<jsp:useBean id="urlParamsPrevious" type="java.util.Map"
																	class="java.util.HashMap" />
																<c:set target="${urlParamsPrevious}" property="page"
																	value="${aimTeamReportsForm.currentPage -1}" />
																<c:set target="${urlParamsPrevious}" property="action"
																	value="getPage" />
																<c:if test="${aimTeamReportsForm.showTabs}">
																	<c:set target="${urlParamsPrevious}" property="tabs"
																		value="true" />
																</c:if>
					                          								&nbsp;|&nbsp; 
					                          								<c:set var="translation">
																	<digi:trn key="aim:previous">Previous</digi:trn>
																</c:set>
																<!--<digi:link href="/viewTeamReports.do" name="urlParamsPrevious" style="text-decoration=none" title="${translation}" >-->
																<!--<digi:trn key="aim:previous">Previous</digi:trn>-->
																<!--</digi:link>-->
																<!--&nbsp;|&nbsp;-->
															</c:if> <c:set var="length"
																value="${aimTeamReportsForm.pagesToShow}"></c:set> <c:set
																var="start" value="${aimTeamReportsForm.offset}" /> <c:if
																test="${maxpages > 1}">
																<logic:iterate name="pagelist" id="pageidx"
																	type="java.lang.Integer" offset="${start}"
																	length="${length}">
																	<c:set target="${urlParamsPagination}" property="page"
																		value="${pageidx - 1}" />
																	<c:if test="${(pageidx - 1) eq actualPage}">
																		<span
																			style="background-color: #FF6000; margin-left: 2px; margin-right: 2px; color: white; padding: 1px 3px; font-weight: bold; border: 1px solid #CCCCCC;">
																			<bean:write name="pageidx" /> </span>
																	</c:if>
																	<c:if test="${(pageidx - 1) ne actualPage}">
																		<c:if test="${aimTeamReportsForm.showTabs}">
																			<c:set target="${urlParamsPagination}"
																				property="tabs" value="true" />
																		</c:if>
																		<digi:link href="/viewTeamReports.do"
																			name="urlParamsPagination">
																			<bean:write name="pageidx" />
																		</digi:link>
																	</c:if>
																	<c:if test="${pageidx < maxpages}">&nbsp;|&nbsp;</c:if>
																</logic:iterate>
															</c:if> <c:if
																test="${aimTeamReportsForm.currentPage+1 != aimTeamReportsForm.totalPages}">
																<jsp:useBean id="urlParamsNext" type="java.util.Map"
																	class="java.util.HashMap" />
																<c:if
																	test="${aimTeamReportsForm.currentPage+1 > aimTeamReportsForm.totalPages}">
																	<c:set target="${urlParamsNext}" property="page"
																		value="${aimTeamReportsForm.currentPage}" />
																</c:if>
																<c:if
																	test="${aimTeamReportsForm.currentPage+1 <= aimTeamReportsForm.totalPages}">
																	<c:set target="${urlParamsNext}" property="page"
																		value="${aimTeamReportsForm.currentPage+1}" />
																</c:if>
																<c:set target="${urlParamsNext}" property="action"
																	value="getPage" />
																<c:set var="translation">
																	<digi:trn key="aim:nextpage">Next Page</digi:trn>
																</c:set>
																<c:if test="${aimTeamReportsForm.showTabs}">
																	<c:set target="${urlParamsNext}" property="tabs"
																		value="true" />
																</c:if>
																<!--<digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsNext" title="${translation}"  >-->
																<!--<digi:trn key="aim:next">Next</digi:trn>-->
																<!--</digi:link>-->
					                          								&nbsp;|&nbsp;
					                          								<jsp:useBean id="urlParamsLast"
																	type="java.util.Map" class="java.util.HashMap" />
																<c:set target="${urlParamsLast}" property="page"
																	value="${aimTeamReportsForm.totalPages-1}" />
																<c:set target="${urlParamsLast}" property="action"
																	value="getPage" />
																<c:if test="${aimTeamReportsForm.showTabs}">
																	<c:set target="${urlParamsLast}" property="tabs"
																		value="true" />
																</c:if>
																<c:set var="translation">
																	<digi:trn key="aim:lastpage">Last Page</digi:trn>
																</c:set>
																<digi:link href="/viewTeamReports.do"
																	style="text-decoration=none" name="urlParamsLast"
																	title="${translation}">
																	<digi:trn key="aim:lastpage">Last Page</digi:trn>
																</digi:link>
					                          								&nbsp;&nbsp; 
					                        							</c:if> <!--<c:out value="${aimTeamReportsForm.currentPage+1}"></c:out>&nbsp;<digi:trn key="aim:of">of</digi:trn>&nbsp;<c:out value="${aimTeamReportsForm.totalPages}"></c:out>-->
												</c:if>
												
												<tr>
													<td>

														<table class="inside" style="font-size: 11px; font-family: Arial, sans-serif; background-color: white;" width="950px">
															<thead>
																<tr>
																	<td class="inside_header" width="450px">
																	<c:if test="${not empty aimTeamReportsForm.sortBy && aimTeamReportsForm.sortBy!=1}">
																			<digi:link href="/viewTeamReports.do?sortBy=1">
																				<b><digi:trn>${titleColumn}</digi:trn> </b>
																			</digi:link>
																			<c:if test="${aimTeamReportsForm.sortBy==2}">
																				<img src="/TEMPLATE/ampTemplate/images/arrow_down.gif" alt="down" />
																			</c:if>
																		</c:if> 
																		<c:if test="${empty aimTeamReportsForm.sortBy || aimTeamReportsForm.sortBy==1}">
																			<digi:link href="/viewTeamReports.do?sortBy=2">
																				<b class="ins_title">
																				<digi:trn key="aim:organizationName">${titleColumn}</digi:trn>
																				</b>
																			</digi:link>
																			<img src="/TEMPLATE/ampTemplate/images/arrow_up.gif" alt="up" />
																		</c:if>
																	</td>

																	<td class="inside_header" align="center"><b><digi:trn>Type</digi:trn></b></td>
																	<td class="inside_header" align="center"><b><digi:trn>Filtered</digi:trn></b></td>
																	<td class="inside_header" align="center"><b><digi:trn>Hierarchies</digi:trn></b></td>
																	<td class="inside_header" align="center"><b><digi:trn>Columns</digi:trn></b></td>
																	<td class="inside_header" align="center"><b><digi:trn>Measures</digi:trn></b></td>
																	<td class="inside_header" align="center"><b><digi:trn>Export Options</digi:trn></b></td>
																</tr>
															</thead>
															<tbody>
																<c:choose>
																	<c:when test="${reportNumber == 0}">
																		<tr>
																			<td colspan="7"><b><digi:trn
																						key="aim:noreportspresent"> No reports present </digi:trn>
																			</b></td>
																		</tr>
																	</c:when>
																	<c:otherwise>
																		<logic:iterate name="aimTeamReportsForm"
																			property="reportsList" id="report" indexId="idx"
																			type="org.digijava.ampModule.aim.dbentity.AmpReports">
																			<c:set var="color">
																				<c:choose>
																					<c:when test="${idx%2==1}">
																				#dbe5f1
																				</c:when>
																					<c:otherwise>
																				#ffffff
																				</c:otherwise>
																				</c:choose>
																			</c:set>


																			<tr
																				onmouseout="setPointer(this, '${idx}', 'out', '${color}', '#a5bcf2', '#FFFF00');"
																				onmouseover="setPointer(this, '${idx}', 'over', '${color}', '#a5bcf2', '#FFFF00');">
																				<c:set var="nameTooltip">
																					<digi:trn>Click here to view the Report</digi:trn>
																				</c:set>
																				<c:set var="reportLink" value="${fn:getReportUrl(report)}" />
																				<td class="report_inside" style="font-size: 11px;"
																					bgcolor="${color}">
																					<div class="t_sm" title="${report.name}">
																					
																						<a
																							href="${reportLink}"
																							onclick="return popup(this,'');"
																							title="${nameTooltip}">
																							<b> 
																								<c:choose>
																									<c:when test="${fn:length(report.name) > 120}">
																										<c:out value="${fn:substring(report.name, 0, 120)}" />... 
																									</c:when>
																									<c:otherwise>
																										<c:out value="${report.name}" />
																									</c:otherwise>
																								</c:choose>
																							</b>
																						</a>																						
																						<br>
																						<logic:present name="report" property="reportDescription">
																							<c:choose>
																								<c:when test="${fn:length(report.reportDescription) > 120}">
																									<c:out value="${fn:substring(report.reportDescription, 0, 120)}" />... 
																								</c:when>
																								<c:otherwise>
																									<c:out value="${report.reportDescription}" />
																								</c:otherwise>
																							</c:choose>
																						</logic:present>
																					</div></td>
																				<td class="report_inside" align="center"
																					style="font-size: 11px;" bgcolor="${color}">
																					<%-- Report Types --%> <%-- Donor:1 --%> <%-- Regional:2 --%>
																					<%-- Component:3 --%> <%-- Contribution:4 --%> <%-- Pledge:5 --%>
																					<c:choose>
																						<c:when test="${report.type == 1}">
																							<digi:trn>donor</digi:trn>
																							<br />
																						</c:when>
																						<c:when test="${report.type == 2}">
																							<digi:trn>component</digi:trn>
																							<br />
																						</c:when>
																						<c:when test="${report.type == 3}">
																							<digi:trn>regional</digi:trn>
																							<br />
																						</c:when>
																						<c:when test="${report.type == 4}">
																							<digi:trn>contribution</digi:trn>
																							<br />
																						</c:when>
																						<c:when test="${report.type == 5}">
																							<digi:trn>pledge</digi:trn>
																							<br />
																						</c:when>
																					</c:choose> <%-- Report Options --%> <%-- Monthly:M --%> <%-- Quarterly:Q --%>
																					<%-- Annual:A --%> <c:choose>
																						<c:when test="${report.options eq 'M'}">
																							<digi:trn key="aim:monthlyreport">Monthly</digi:trn>
																							<br />
																						</c:when>
																						<c:when test="${report.options eq 'Q'}">
																							<digi:trn key="aim:quarterlyreport">Quarterly</digi:trn>
																							<br />
																						</c:when>
																						<c:when test="${report.options eq 'A'}">
																							<digi:trn key="aim:annualreport">Annual</digi:trn>
																							<br />
																						</c:when>
																					</c:choose> <c:if test="${report.hideActivities}">
																						<digi:trn>Summary Report</digi:trn>
																					</c:if></td>
																				<td align="center" class="report_inside"
																					style="font-size: 11px;" bgcolor="${color}"><c:choose>
																						<c:when test="${empty report.filterDataSet}">
																							<img
																								src="/TEMPLATE/ampTemplate/images/bullet_grey_sq.gif"
																								vspace="2" border="0" align="absmiddle" />
																						</c:when>
																						<c:otherwise>
																							<img
																								src="/TEMPLATE/ampTemplate/images/bullet_green_sq.gif"
																								vspace="2" border="0" align="absmiddle" />
																						</c:otherwise>
																					</c:choose></td>
																				<td align="center" class="report_inside"
																					style="font-size: 11px;" bgcolor="${color}"><c:forEach
																						var="hierarchy" items="${report.hierarchies}">
																						<digi:colNameTrn>${hierarchy.column.columnName}</digi:colNameTrn>

																						<br />
																					</c:forEach>&nbsp;</td>
																				<td align="center" class="report_inside"
																					style="font-size: 11px;" bgcolor="${color}"><c:forEach
																						var="column" items="${report.columns}">
																						<digi:colNameTrn>${column.column.columnName}</digi:colNameTrn>
																						<br />
																					</c:forEach></td>
																				<td align="center" class="report_inside"
																					style="font-size: 11px;" bgcolor="${color}"><c:forEach
																						var="measure" items="${report.measures}">
																						<digi:trn
																							key="aim:reportBuilder:${measure.measure.aliasName}"> ${measure.measure.aliasName} </digi:trn>
																						<br />
																					</c:forEach></td>
																				<td align="center" class="report_inside"
																					style="font-size: 11px;" bgcolor="${color}">
																					<p style="white-space: nowrap">
																						<jsp:useBean id="urlParams" type="java.util.Map"
																							class="java.util.HashMap" />

																						<c:set target="${urlParams}" property="rid">
																							<bean:write name="report" property="ampReportId" />
																						</c:set>
																						<c:set target="${urlParams}" property="event" value="edit" />

																						<a href="${reportLink}"
						                                								onclick="return popup(this,'');" style="padding-right: 5px;" title="<digi:trn>Click here to view the report</digi:trn>">
						                                								<img src= "/TEMPLATE/ampTemplate/saikuui_reports/images/saiku.png" border="0" /></a>
																						
																						<c:set var="showExportExcelSetting" scope="page" value="true"/>
																						<%if(!FeaturesUtil.showEditableExportFormats()){ %>
																							<c:set var="showExportExcelSetting" scope="page" value="false"/>
																						<%}%>
																						
																						<c:if test="${(sessionScope.currentMember != null) || (showExportExcelSetting)}">
																							<c:set var="translation">
																								<digi:trn>Get report in Excel format</digi:trn>&nbsp;
																							</c:set>
																							<a style="cursor:pointer"
																								onclick="$.downloadReport(${report.ampReportId}, 'xls')" 
																								title="${translation}">
																								<digi:img hspace="0" vspace="0" height="16"	width="16" 
																								src="/TEMPLATE/ampTemplate/images/icons/xls.gif" border="0" />
																							</a>
																						</c:if>
																						&nbsp;
																						<!--
																						<c:set var="translation">
																							<digi:trn>Get report in PDF format</digi:trn>&nbsp;
																						</c:set>
																						<a style="cursor:pointer"
																							onclick="$.downloadReport(${report.ampReportId}, 'pdf')" 
																							title="${translation}">
																							<digi:img hspace="0" vspace="0" height="16"
																								width="16"
																								src="/TEMPLATE/ampTemplate/images/icons/pdf.gif"
																								border="0" />
																						</a>
																						-->
																						<!-- Show Printer Friendly icon removed with AMP-22055 -->
																						<!--  
																						<feature:display
																							name="Show Printer Friendly option"
																							ampModule="Public Reports">
                         																	&nbsp;
                         																	<c:set var="translation">
																								<digi:trn>Get report in printer friendly version</digi:trn>&nbsp;
																							</c:set>
																							<a
																								href="${reportLink}"
																								onclick="return popup(this,'');"
																								title="${translation}">
																								<digi:img hspace="0" vspace="0" height="16"
																									width="16"
																									src="/TEMPLATE/ampTemplate/img_2/ico-print.png"
																									border="0" />
																							</a>
																						</feature:display> -->
																					</p></td>
																			</tr>
																		</logic:iterate>
																	</c:otherwise>
																</c:choose>
															</tbody>


														</table></td>
												</tr>
												<tr>
													<td style="font-size: 11px; font-family: Aria, sans-serif;">
													<c:if test="${reportNumber != 0}">
														<c:set target="${urlParamsPagination}" property="action" value="getPage" />
														 <c:if test="${aimTeamReportsForm.currentPage >0}">
															<c:set target="${urlParamsFirst}" property="page" value="0" />
															<c:set target="${urlParamsFirst}" property="action" value="getPage" />
															<c:if test="${aimTeamReportsForm.showTabs}">
															<c:set target="${urlParamsFirst}" property="tabs" value="true" />
															</c:if>
															<c:set var="translation">
																<digi:trn key="aim:firstpage">First Page</digi:trn>
															</c:set>
															<digi:link href="/viewTeamReports.do" style="text-decoration=none" name="urlParamsFirst" title="${translation}">
																<digi:trn key="aim:firstpage">First Page</digi:trn>
															</digi:link>
															<c:set target="${urlParamsPrevious}" property="page" value="${aimTeamReportsForm.currentPage -1}" />
															<c:set target="${urlParamsPrevious}" property="action" value="getPage" />
															<c:if test="${aimTeamReportsForm.showTabs}">
															<c:set target="${urlParamsPrevious}" property="tabs" value="true" />
															</c:if>
					                          				&nbsp;|&nbsp; 
					                          				<c:set var="translation">
																<digi:trn key="aim:previous">Previous</digi:trn>
															</c:set>
															<!--					                          								<digi:link href="/viewTeamReports.do" name="urlParamsPrevious" style="text-decoration=none" title="${translation}" >-->
															<!--					                            								<digi:trn key="aim:previous">Previous</digi:trn>-->
															<!--					                          								</digi:link>-->
															<!--					                          								&nbsp;|&nbsp;-->
														</c:if> 
														<c:set var="length" value="${aimTeamReportsForm.pagesToShow}"></c:set>
														 <c:set var="start" value="${aimTeamReportsForm.offset}" /> 
														 <c:if test="${maxpages > 1}">
															<logic:iterate name="pagelist" id="pageidx"
																type="java.lang.Integer" offset="${start}"
																length="${length}">
																<c:set target="${urlParamsPagination}" property="page"
																	value="${pageidx - 1}" />
																<c:if test="${(pageidx - 1) eq actualPage}">
																	<span
																		style="background-color: #FF6000; margin-left: 2px; margin-right: 2px; color: white; padding: 1px 3px; font-weight: bold; border: 1px solid #CCCCCC;">
																		<bean:write name="pageidx" /> </span>
																</c:if>
																<c:if test="${(pageidx - 1) ne actualPage}">
																	<c:if test="${aimTeamReportsForm.showTabs}">
																		<c:set target="${urlParamsPagination}" property="tabs"
																			value="true" />
																	</c:if>
																	<digi:link href="/viewTeamReports.do"
																		name="urlParamsPagination">
																		<bean:write name="pageidx" />
																	</digi:link>
																</c:if>
																<c:if test="${pageidx < maxpages}">&nbsp;|&nbsp;</c:if>
															</logic:iterate>
														</c:if>
														<c:if test="${aimTeamReportsForm.currentPage+1 != aimTeamReportsForm.totalPages}">
															<c:if test="${aimTeamReportsForm.currentPage+1 > aimTeamReportsForm.totalPages}">
																<c:set target="${urlParamsNext}" property="page" value="${aimTeamReportsForm.currentPage}" />
															</c:if>
															<c:if test="${aimTeamReportsForm.currentPage+1 <= aimTeamReportsForm.totalPages}">
																<c:set target="${urlParamsNext}" property="page" value="${aimTeamReportsForm.currentPage+1}" />
															</c:if>
															<c:if test="${aimTeamReportsForm.showTabs}">
																<c:set target="${urlParamsNext}" property="tabs" value="true" />
															</c:if>
															<c:set target="${urlParamsNext}" property="action"
																value="getPage" />
															<c:set var="translation">
																<digi:trn key="aim:nextpage">Next Page</digi:trn>
															</c:set>
															<!--					                          								<digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsNext" title="${translation}"  >-->
															<!--					                            								<digi:trn key="aim:next">Next</digi:trn>-->
															<!--					                          								</digi:link>-->
					                          				&nbsp;|&nbsp;
													        <c:set target="${urlParamsLast}" property="page" value="${aimTeamReportsForm.totalPages-1}" />
															<c:set target="${urlParamsLast}" property="action" value="getPage" />
															<c:if test="${aimTeamReportsForm.showTabs}">
															<c:set target="${urlParamsLast}" property="tabs" value="true" />
															</c:if>
															<c:set var="translation">
																<digi:trn key="aim:lastpage">Last Page</digi:trn>
															</c:set>
															<digi:link href="/viewTeamReports.do"
																style="text-decoration=none" name="urlParamsLast"
																title="${translation}">
																<digi:trn key="aim:lastpage">Last Page</digi:trn>
															</digi:link>
					                          								&nbsp;&nbsp; 
					                        							</c:if> <!--<c:out value="${aimTeamReportsForm.currentPage+1}"></c:out>&nbsp;<digi:trn key="aim:of">of</digi:trn>&nbsp;<c:out value="${aimTeamReportsForm.totalPages}"></c:out>-->
					                        			</c:if>
													</td>
												</tr>
												<tr>
													<td>

													</td>
												</tr>
											</table>
										</div>
									</div>
								</div>
						
				</td>
			</tr>
		</table>
	</digi:form>
<%}%>


<script>

jQuery.downloadReport = function(reportId, type){
	//url and data options required
	if(reportId && type){ 
		var url = window.location.origin + '/rest/data/saikureport/export/' + type + '/' + reportId;
		var input ='<input type="hidden" name="reportId" value="'+ reportId +'" />';
		var inputPublic = '<input type="hidden" name="isPublic" value="true"/>';
		
		//send request
		jQuery('<form action="'+ url +'" method="POST' +'">' + input + inputPublic + '</form>').appendTo('body').submit().remove();
	};
};

</script>