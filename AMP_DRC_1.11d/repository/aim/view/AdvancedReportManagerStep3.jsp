<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/advanceReportManager.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
<!--
	function saveReport()
	{
		alert('<digi:trn key="aim:reportBuilder:ReportSavingValidation">Your report is being saved</digi:trn>');
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=SaveReport" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}
	function popup_chart() {
		openResisableWindow(800, 600);
		<digi:context name="pchart" property="context/module/moduleinstance/advancedReportPieChart.do" />
		document.aimAdvancedReportForm.action = "<%= pchart %>";
		document.aimAdvancedReportForm.target = popupPointer.name;
		document.aimAdvancedReportForm.submit();
	}

	function popup_barChart() {
		openResisableWindow(800, 600);
		<digi:context name="barchart" property="context/module/moduleinstance/advancedReportBarChart.do" />
		document.aimAdvancedReportForm.action = "<%= barchart %>";
		document.aimAdvancedReportForm.target = popupPointer.name;
		document.aimAdvancedReportForm.submit();

	}

-->
</script>

<digi:instance property="aimAdvancedReportForm" />
<digi:form action="/advancedReportManager.do" method="post">
<input type="hidden" name="isAdd" >

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
<tr>
	<td>
		<jsp:include page="teamPagesHeader.jsp" flush="true" />
	</td>
</tr>

<tr>

<td width="100%" vAlign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="770" vAlign="top" align="left" border=0>
	<tr>

	<td class=r-dotted-lg align=left vAlign=top >	&nbsp;</td>
	<td>
		<table>
			<tr>
				<td>
					<table cellPadding=5 cellSpacing=0 width="100%">
						<tr>
							<td height=33><span class=crumb>
								<c:set var="translation">
									<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
								</c:set>
								<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
								<digi:trn key="aim:portfolio">
									Portfolio
								</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp;

								<digi:link href="/advancedReportManager.do?check=forward" styleClass="comment" title="${translation}" >
								<digi:trn key="aim:reportBuilder:selectcolumn">
									Report Builder : Select Column
								</digi:trn>
								&gt;&gt;
								</digi:link>&nbsp;&nbsp;

								<digi:link href="/advancedReportManager.do?check=charts" styleClass="comment" title="${translation}" >
								<digi:trn key="aim:reportBuilder:selectrows">
									Report Builder : Select Rows
								</digi:trn>
								&gt;&gt;
								</digi:link>&nbsp;&nbsp;

								<digi:link href="/advancedReportManager.do?check=SelectMeasures" styleClass="comment" title="${translation}" >
								<digi:trn key="aim:reportBuilder:selectmeasures">
									Report Builder : Select Measure
								</digi:trn>
								&gt;&gt;
								</digi:link>&nbsp;&nbsp;

								<digi:link href="/advancedReportManager.do?check=4" styleClass="comment" title="${translation}" >
								<digi:trn key="aim:reportBuilder:reportDetails">
									Report Builder : Report Details
								</digi:trn>
								&gt;&gt;
								</digi:link>&nbsp;&nbsp;

								<digi:link href="/advancedReportManager.do?check=5" styleClass="comment" title="${translation}" >
								<digi:trn key="aim:reportBuilder:htmlReport">
									Report Builder : HTML Report
								</digi:trn>
								&gt;&gt;
								</digi:link>&nbsp;&nbsp;

								<digi:trn key="aim:reportBuilder:chartReport">
									Report Builder : Chart Report
								</digi:trn>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		 	<tr>

				<td height=16 vAlign=right align=center>
					<span class=subtitle-blue>
					Report Builder : CHART Creation
					</span>
				</td>
			</tr>
			<TR>
			<TD vAlign="top" align="center">
				<TABLE width="100%" cellSpacing=0 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4" class="box-border-nopadding">
					<TR>
						<TD bgcolor="#f4f4f4">
							<TABLE width="100%" cellSpacing=1 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4">
								<jsp:include page="AdvancedReportManagerMenu.jsp" flush="true"/>
								<TR bgColor=#f4f4f2>
									<TD vAlign="top" align="left" width="100%">
									</TD>
								</TR>
								<TR bgColor=#f4f4f2>
									<TD vAlign="top" align="center" width="100%" bgColor=#f4f4f2>
										<TABLE width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2 >
											<TR>
												<TD width="100%" bgcolor="pink" align="center"  valign=top>
													<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" align="center" bgColor=#f4f4f2 class="box-border-nopadding" border=0>
													<tr>
														<td bgColor=#f4f4f2>
															<TABLE width="100%" cellPadding=2 cellSpacing=0 vAlign="top" align="top" bgColor=#f4f4f2 border=1
															style="border-collapse: collapse">
																<tr>
																	<td class=box-title>
																		Note :
																	</td>
																</tr>
																<tr>
																	<td class=box-title>
																	<FONT color=red size="4">* </font>	Please select<u> "Title"  to generate a Chart.</u><br>
																	</td>
																</tr>
															    <tr bgcolor="#cccccc">
																<TD bgcolor="#f4f4f4" height=200 align="center">
<!--																<br>
																<digi:link href="" onclick="popup_chart(); return false;"></digi:link>
																<b>::: Pie CHART Image :::</b><br>
																<br>
																<bean:define id="fName">
																	<c:out value="${aimAdvancedReportForm.pieImageUrl}"/>
																</bean:define>
																<%
																String piegraphURL = request.getContextPath() + "/aim/DisplayChart.img?filename="+fName;
																%>

																<br>
																<img src=<%=piegraphURL%> border="1">
-->
																<br><br><br>
																<b>::: Bar CHART Image :::</b><br>
																<br>
																<bean:define id="brName">
																	<c:out value="${aimAdvancedReportForm.barImageUrl}"/>
																</bean:define>
																<%
																String bargraphURL = request.getContextPath() + "/aim/DisplayChart.img?filename="+brName;
																%>

																<br>
																<a href="<%=bargraphURL%>" target="_blank"><img src=<%=bargraphURL%> border="1" width="600" height="600"></a>

																<br><br>
																<html:button  styleClass="dr-menu" property="submitButton"  onclick="javascript:history.back()">
																	<< <digi:trn key="btn:previous">Previous</digi:trn>
																</html:button>
																<c:set var="message">
																	<digi:trn key="aim:reports:DataNotSaved">Do you really want to quit Report Generator? \nWarning: All your Current Data Will be Lost... press OK to QUIT Report Generator.</digi:trn>
																</c:set>
																<html:button  styleClass="dr-menu" property="submitButton"  onclick="return quitAdvRptMngr('${message}')">
																	<digi:trn key="btn:cancel">Cancel</digi:trn>
																</html:button>
																<html:button  styleClass="dr-menu" property="submitButton"  onclick="popup_barChart()">
																	<digi:trn key="btn:createBarChartPDF">Create Bar-Chart PDF</digi:trn>
																</html:button>
																<html:button  styleClass="dr-menu" property="submitButton"  onclick="saveReport()">
																	<digi:trn key="btn:saveReport">Save Report</digi:trn>
																</html:button>


																</TD>

															    </tr>
															</TABLE>
														</td>
													</tr>
													</TABLE>
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</TD>
			</TR>
		</table>
	</td>
	<td class=r-dotted-lg align=left vAlign=top >	&nbsp;</td>
</tr>
</table>
</td>
</TR>
</TABLE>
</digi:form>
