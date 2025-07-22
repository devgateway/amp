<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
<!--

	function load()
{
	window.print();
}


-->
</script>
</script>

<digi:errors/>
<digi:instance property="aimParisIndicatorReportForm" />


<table width="772" border="0" cellpadding="10" cellspacing="0" bgcolor="#FFFFFF" >
	<tr>
		
			&nbsp;
		</td>
		<td width="750" align="left" valign="top" BORDER="1">
			
<!--  PDF/XLS Links -->	
	<digi:form action="/parisIndicatorReport.do" >
	<html:hidden property="filterFlag" />
	<html:hidden property="indicatorId" />
				
<%--===========================================================================================================================================--%>					
					<c:if test="${aimParisIndicatorReportForm.indicatorCode != '6'}">
						<table border="0" cellspacing="0" cellpadding="2" width="750">
					</c:if>
					<c:if test="${aimParisIndicatorReportForm.indicatorCode == '6'}">
						<table border="0" cellspacing="0" cellpadding="2" width="350">
					</c:if>
							
							

						</table>
<%--============================================================================================================================================--%>
					</td>
					
				</tr>
				<tr>
					<td colspan=3 class=subtitle-blue align="center">
						<digi:trn key="aim:parisIndicator">Paris Indicator</digi:trn>&nbsp;
						<c:out value="${aimParisIndicatorReportForm.indicatorCode}" />&nbsp;
						<digi:trn key="aim:report">Report</digi:trn>
					</td>
				</tr>
				<c:if test="${aimParisIndicatorReportForm.indicatorCode == 6}">
					<tr>
						<td colspan=3 class=subtitle-blue-2 align="center">
							[<digi:trn key="aim:numParallelPIU">Number Of Parallel PIUs</digi:trn>]&nbsp;
						</td>
					</tr>
				</c:if>
				<tr>
	</digi:form>
				<tr>
				
					<td width="100%">
						<table border="0" cellpadding="0" cellspacing="0" width="100%" height="169">
						<tr>
							<td colspan="2" >
						<!--angle image-->
						<table border="0" cellspacing="0" cellpadding="0" >
							  <tr bgcolor="#C9C9C7">
								<td bgcolor="#C9C9C7" class="box-title">
									 <digi:trn key="aim:parisIndcReports">
										 Paris Indicator Reports
										</digi:trn>
								 </td>
								 <td bgcolor="#FFFFFF"><img src="../ampTemplate/images/corner-r.gif" width="17" height="17">
								 </td>
							  </tr>
			              </table>
					</td>
				</tr>
				<tr align="top">

					<td>
						<!--for headers and data-->
<%--********************************************************************************************************************************************--%>
						<table cellspacing="0" cellpadding="0" border="1" width="100%" >
							<tr align="center"  bgcolor="#F4F4F2">
								
								<c:if test="${aimParisIndicatorReportForm.indicatorCode == '6'}">
									<td width="15%" height="25">
										<div align="center"><strong>Donor(s)</strong></div>
								    </td>
								</c:if>
								<c:if test="${aimParisIndicatorReportForm.indicatorCode == '7'}">
									<td width="15%" height="25" rowspan="2">
										<div align="center"><strong>Donor(s)</strong></div>
								  	</td>
								</c:if>
								 
								  <%-- Loop-1[Years] starts here --%>
								  <c:set var="stYear" value="${aimParisIndicatorReportForm.startYear}" />
								  <c:set var="clYear" value="${aimParisIndicatorReportForm.closeYear}" />
								  <c:set var="yrRange" value="${clYear - stYear + 1}" />
								  
								  <%--<bean:define id="syear"><c:out value="${aimParisIndicatorReportForm.startYear}" /></bean:define>
								  <bean:define id="cyear"><c:out value="${aimParisIndicatorReportForm.closeYear}" /></bean:define>
								  <% for(int i = Integer.parseInt(syear); i <= Integer.parseInt(cyear); i++) {  %>--%>
								  
								  <logic:iterate id="year" name="aimParisIndicatorReportForm" property="yearColl">
								  	<c:if test="${year == stYear}">
								  		<c:if test="${aimParisIndicatorReportForm.indicatorCode == '6'}">
								  			<td width="75" height="25">
								  		</c:if>
								  		<c:if test="${aimParisIndicatorReportForm.indicatorCode == '7'}">
								  			<td width="15%" height="25" colspan="3">
								  		</c:if>
								  			<div align="center">
												<%--<%=i%>--%>
												<strong><c:out value="${stYear}" /></strong>
											</div>
								 		</td>
								 	<%--<% } %>--%>
								 		<c:if test="${stYear != clYear}">
								 			<c:set var="stYear" value="${stYear + 1}" />
								 		</c:if>
								 	</c:if>
								 </logic:iterate>
								 <%-- Loop-1[Years] ends here --%>
								 
							</tr>
							<c:if test="${aimParisIndicatorReportForm.indicatorCode == '7'}">
								<tr align="center"  bgcolor="#F4F4F2">
									<%-- Loop-2 starts here --%>
									<%--<c:set var="stYear" value="${aimParisIndicatorReportForm.startYear}" />
									<bean:define id="cntr"><c:out value="${clYear - stYear + 1}" /></bean:define>
									<% for(int i = 0, j = Integer.parseInt(cntr); i < j; i++) {  %> --%>
									<c:forEach begin="1" end="${yrRange}" step="1">
										<td width="15%" height="25">
											<div align="center">
												<strong>
													Aid flows to the government sector scheduled for fiscal year
												</strong>
											</div>
										 </td>
										 <td width="15%" height="25" >
											<div align="center">
												<strong>
													Total Aid flows disbursed to the government sector
												</strong>
											</div>
										 </td>
										 <td width="15%" height="25" >
											<div align="center">
												<strong>
													Proportion of aid to the government sector disbursed within the fiscal year it was scheduled
												</strong>
											</div>
										 </td>
									</c:forEach>
									 <%-- <% } %> --%>
									 <%-- Loop-2 ends here --%>
								</tr>
							</c:if>
							
							<tr>
								<%-- Loop-3[ANSWERS] starts here --%>
								<td align="center">
									<%--// Answers here--%>
									<c:set var="numCols" value="${aimParisIndicatorReportForm.numColsCalculated}" />
							  		<logic:empty name="aimParisIndicatorReportForm" property="donorsColl">
										<tr>
											<td width="100%" align="center" height="65" colspan='<c:out value="${numCols}" />'>
												<div align="center"><strong><font color="red">
													<digi:trn key="aim:noSurveyDataFound">No survey data found.</digi:trn>
												</font></strong></div>
											</td>
										</tr>
							  		</logic:empty>
							  		<logic:notEmpty name="aimParisIndicatorReportForm" property="donorsColl"> 
							  		<c:set var="index1" value="${numCols - 1}" />
							  		<nested:iterate name="aimParisIndicatorReportForm" property="donorsColl">
									<tr>
										<td width="15%" align="center" height="65">
											<div align="center">
											<strong><digi:trn key="aim:parisIndicatorDonor">
											<nested:write property="donor" />
											</digi:trn>
											</strong>
											</div>
										</td>
										<nested:iterate property="answers">
											<c:set var="index2" value="0" />
											<nested:iterate id="rowVal">
												<c:if test="${aimParisIndicatorReportForm.indicatorCode == '6'}">
													<td>
														<div align="center">
															<fmt:formatNumber value="${rowVal}" type="number" maxFractionDigits="0"/>
														</div>
													</td>
												</c:if>
												<c:if test="${aimParisIndicatorReportForm.indicatorCode == '7'}">
													<c:if test="${index2 != 0}">
														<td>
															<div align="center">
																<c:if test="${index1 == index2}">
																	<c:if test="${rowVal == -1}">n.a.</c:if>
																	<c:if test="${rowVal != -1}">
																		<fmt:formatNumber type="number" value="${rowVal}" maxFractionDigits="0" />%
																	</c:if>
																</c:if>
																<c:if test="${index1 != index2}">
																	<fmt:formatNumber type="number" value="${rowVal}" maxFractionDigits="0" />
																</c:if>
															</div>
														</td>
													</c:if>
													<c:set var="index2" value="${index2 + 1}" />	
												</c:if>
											</nested:iterate>
										</nested:iterate>
									</tr>
									</nested:iterate>
									</logic:notEmpty>
								</td>
								<%-- Loop-3[ANSWERS] ends here --%>
							</tr>
						</table>
<%--***********************************************************************************************************--%>
					</td>
					 
				</tr>
				<c:if test="${aimParisIndicatorReportForm.indicatorCode != '6'}">
					<tr><td>
					<font color="black">
						<jsp:include page="utils/amountUnitsUnformatted.jsp">
							<jsp:param value="* " name="amount_prefix"/>
						</jsp:include>	
					<bean:write name="aimParisIndicatorReportForm" property="currency"/></font></td></tr>
				</c:if>
					</td>
					
				</tr>			
			</table>
		 </td>
		 
	</tr>
</table>
