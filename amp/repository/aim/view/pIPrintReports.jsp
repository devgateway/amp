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

<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
<!--
function load()
{
	window.print();
}
	


-->
</script>

<digi:errors/>
<digi:instance property="aimParisIndicatorReportForm" />


<table width="772" border="0" cellpadding="10" cellspacing="0" bgcolor="#FFFFFF" >
	<!--<tr>
		<td width="14" class="r-dotted-lg">
			&nbsp;
		</td>
		<td width="750" align="left" valign="top" BORDER="1">
			<table width="100%"  border="0" cellpadding="5" cellspacing="0">-->
		<%---------------------------------------------------------------------------------------------------------------------------------------%>		
				
				
				<tr>
					<td colspan=3 class=subtitle-blue align="center">
						<digi:trn key="aim:parisIndicator">Paris Indicator</digi:trn>&nbsp;
						<c:out value="${aimParisIndicatorReportForm.indicatorCode}" />&nbsp;
						<digi:trn key="aim:report">Report</digi:trn>
					</td>
				</tr>
				<tr>
					<td colspan=3 class=box-title align="center">
					</td>
				</tr>
				

	<digi:form action="/ParisIndicatorPrintReports.do" method="post" >
	<html:hidden property="filterFlag" />
	<html:hidden property="indicatorId" />
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
									 <digi:trn key="aim:ParisIn">
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
<%--*********************************************************************************************************************************************--%>
						<table cellspacing="0" cellpadding="0" border="1" width="100%" >
							<tr align="center"  bgcolor="#F4F4F2">

								<td width="15%" height="33">
									<div align="center">
										<strong>
											Donor(s)
										</strong>
									</div>
								  </td>
									 <td width="5%" height="33">
										<div align="center">
											<strong>
												Disbursement Year
											</strong>
										</div>
									</td>

								<!--Start of Indicator headers selection here-->
									<!-- Indicator 3 -->
									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '3'}">
									 <td width="27%" height="33">
											<div align="center">
											<strong>
											Aid flows to the government sector reported on the government's budget
											</strong>
											</div>
										</td>
										<td width="26%" height="33">
											<div align="center">
											<strong>
											Total Aid flows disbursed to the government sector
											</strong>
											</div>
										</td>
									</c:if>
										
									<!--Indicator 4 -->
									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '4'}">
										<td width="27%" height="33">
											<div align="center">
											<strong>
											Volume of technical co-operation for capacity development provided through co-ordinated programmes 
											</strong>
											</div>
										</td>
										<td width="26%" height="33">
											<div align="center">
											<strong>
											Total volume of technical co-operation provided 
											</strong>
											</div>
										</td>
										<td width="27%" height="33">
											<div align="center">
											<strong>
											% of TC for capacity development provided through coordinated programmes consistent with national development strategies 
											</strong>
											</div>
										</td>
									</c:if>
									<!--Indicator 5a -->
									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '5a'}">
										<td width="11%" height="33">
											<div align="center">
											<strong>
											Aid flows to the goverment sector that use national budget execution procedures
											</strong>
											</div>
										</td>
										<td width="11%" height="33">
											<div align="center">
											<strong>
											Aid flows to the goverment sector that use national financial reporting procedures
											</strong>
											</div>
										</td>
										<td width="11%" height="33">
											<div align="center">
											<strong>
											Aid flows to the goverment sector that use national financial auditing procedures
											</strong>
											</div>
										</td>
										<td width="11%" height="33">
											<div align="center">
											<strong>
											ODA that uses all 3 national PFM
											</strong>
											</div>
										</td>
										<td width="11%" height="33">
											<div align="center">
											<strong>
											Total aid flows disbursed to the government sector
											</strong>
											</div>
										</td>
										<td width="11%" height="33">
											<div align="center">
											<strong>
											Proportion aid flows to the government sector using one of the 3 country PFM systems
											</strong>
											</div>
										</td>
										<td width="11%" height="33">
											<div align="center">
											<strong>
											Proportion of aid flows to the government sector using all the 3 country PFM systems
											</strong>
											</div>
										</td>
									</c:if>
									<!--Indicator 5b -->
									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '5b'}">
										<td width="27%" height="33">
											<div align="center">
											<strong>
											Aid flows to the government sector that use national procurement procedures  
											</strong>
											</div>
										</td>
										<td width="26%" height="33">
											<div align="center">
											<strong>
											Total aid flows disbursed to the government sector
											</strong>
											</div>
										</td>
										<td width="27%" height="33">
											<div align="center">
											<strong>
											Proportion of aid flows to the government sector using national procurement procedures
											</strong>
											</div>
										</td>
									</c:if>
									<!--Indicator 9 -->
									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '9'}">
										<td width="20%" height="33">
											<div align="center">
											<strong>
											Budget support aid flows provided in the context of programme based approach 
											</strong>
											</div>
										</td>
										<td width="20%" height="33">
											<div align="center">
											<strong>
											Other aid flows provided in the context of programme based approach	
											</strong>
											</div>
										</td>
										<td width="20%" height="33">
											<div align="center">
											<strong>
											Total aid flows provided
											</strong>
											</div>
										</td>
										<td width="20%" height="33">
											<div align="center">
											<strong>
											Propotion of aid flows provided in the context of programme based approach
											</strong>
											</div>
										</td>
									</c:if>
									<!--Indicator 10a -->
									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '10a'}">
										<td width="27%" height="33">
											<div align="center">
											<strong>
											Number of missions to the field that are joint  
											</strong>
											</div>
										</td>
										<td width="26%" height="33">
											<div align="center">
											<strong>
											Total number of missions to the field
											</strong>
											</div>
										</td>
									</c:if>
									<!--Indicator 10b -->
									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '10b'}">
										<td width="27%" height="33">
											<div align="center">
											<strong>
											Number of country analytic reports that are joint  
											</strong>
											</div>
										</td>
										<td width="26%" height="33">
											<div align="center">
											<strong>
											Total number of country analytic reports
											</strong>
											</div>
										</td>
									</c:if>
									
									<!-- end of c:if for all the Indicators-->
									<c:if test = "${aimParisIndicatorReportForm.indicatorCode != '4'  &&
													aimParisIndicatorReportForm.indicatorCode != '5a' &&
													aimParisIndicatorReportForm.indicatorCode != '5b' &&
													aimParisIndicatorReportForm.indicatorCode != '9'}">
									    <td width="27%" height="33">
											<div align="center">
											<strong>
											<c:out value="${aimParisIndicatorReportForm.indicatorName}"/>
											</strong>
											</div>
									  	</td>
									</c:if>
							</tr>
							
							  <c:set var="numCols" value="${aimParisIndicatorReportForm.numColsCalculated}" />
							  <logic:empty name="aimParisIndicatorReportForm" property="donorsColl">
								<tr>
									<td width="100%" align="center" height="65" colspan='<c:out value="${numCols}" />' >
										<div align="center"><strong><font color="red">
											<digi:trn key="aim:noSurveyDataFound">No survey data found.</digi:trn>
										</font></strong></div>
									</td>
								</tr>
							  </logic:empty>
							  <logic:notEmpty name="aimParisIndicatorReportForm" property="donorsColl"> 
								<%-- <fmt:setLocale value="${en_US}" /> --%>
								<% boolean flag = false; %>	
								<nested:iterate name="aimParisIndicatorReportForm" property="donorsColl">
								<tr>
									<c:set var="yearRange" value="${aimParisIndicatorReportForm.closeYear - aimParisIndicatorReportForm.startYear + 1}" />		
									<%--<td width="15%" align="center" height="65" rowspan="3"> --%>
									<td width="15%" align="center" height="65" rowspan='<c:out value="${yearRange}" />' >
										<div align="center">
											<strong><digi:trn key="aim:parisIndicatorDonor">
											<nested:write property="donor" />
											</digi:trn>
											</strong>
										</div>
									</td>
									<nested:iterate property="answers">
									<% if (flag) { %>
									<tr> 
									<% } %>
										<c:set var="index" value="1" />
										<nested:iterate id="rowVal">
										<td>
											<div align="center">
												<c:if test="${index == 1}">
													<fmt:formatNumber type="number" value="${rowVal}" pattern="####" maxFractionDigits="0" />
												</c:if>
												<c:if test="${index != 1}">
													<c:choose>
														<c:when test="${index < (numCols-1)}">
															<fmt:formatNumber type="number" value="${rowVal}" maxFractionDigits="0" />
														</c:when>
														<c:when test="${index == (numCols-1)}">
															<c:choose>
																<c:when test="${aimParisIndicatorReportForm.indicatorCode == '5a'}">
																	<c:if test="${rowVal == -1}">n.a.</c:if>
																	<c:if test="${rowVal != -1}">
																		<fmt:formatNumber type="number" value="${rowVal}" maxFractionDigits="0" />%
																	</c:if>
																</c:when>
																<c:otherwise >
																	<fmt:formatNumber type="number" value="${rowVal}" maxFractionDigits="0" />
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:otherwise >
															<c:if test="${rowVal == -1}">n.a.</c:if>
															<c:if test="${rowVal != -1}">
																<fmt:formatNumber type="number" value="${rowVal}" maxFractionDigits="0" />%
															</c:if>
														</c:otherwise>
													</c:choose>
												</c:if>
												<c:set var="index" value="${index + 1}" />
											</div>
										</td>
										</nested:iterate>
										<% flag = true; %>
									</tr>
									</nested:iterate>
									<% flag = false; %>
								</tr>
								</nested:iterate>
							</logic:notEmpty>
						</table>
<%--*********************************************************************************************************************************************--%>
					</td>
					 
				</tr>
					<tr><td>
					<font color="black">
						<jsp:include page="utils/amountUnitsUnformatted.jsp">
							<jsp:param value="* " name="amount_prefix"/>
						</jsp:include>	
						<bean:write name="aimParisIndicatorReportForm" property="currency"/>
					</font>
					</td></tr>
<%-----------------------------------------------------------------------------------------------------------------------------------------------------%>
						<!--</table>
					</td>
					
				</tr>		-->
			</table>
		   </td>
		   
	</tr>
	<tr>
		
		<td>
			<logic:notEmpty name="aimParisIndicatorReportForm" property="donorsColl">
			<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<c:if test="${aimParisIndicatorReportForm.indicatorCode == '5a' || aimParisIndicatorReportForm.indicatorCode == '5b'}">
				<c:set var="range" value="${aimParisIndicatorReportForm.closeYear - aimParisIndicatorReportForm.startYear + 1}" />
				<c:set var="cntr" value="0" />
					<tr>
						<td align="center" colspan='<c:out value="${numCols + 1}" />' >
							<table border="1" width="50%" cellspacing="0" cellpadding="0">
								<c:set var="flag" value="true" />				
								<nested:iterate name="aimParisIndicatorReportForm" property="donorsCollIndc5">
									<c:if test="${flag == true}">
										<tr bgcolor="#F4F4F2">
											<nested:iterate id="rowVal">
												<c:if test="${cntr == 0}">
													<td align="center" rowspan="2">
														<strong><c:out value="${rowVal}"/></strong>
													</td>
													<td align="center" colspan='<c:out value="${range}" />' >
														<c:if test="${aimParisIndicatorReportForm.indicatorCode == '5a'}">
															<strong>Percent of donors that use national procurement systems</strong>
														</c:if>
														<c:if test="${aimParisIndicatorReportForm.indicatorCode == '5b'}">
															<strong>Percent of donors that use national procurement systems</strong>
														</c:if>
													</td>
												</c:if>
												<c:if test="${cntr != 0}">
													<c:if test="${cntr == 1}"><tr></c:if>
													<td align="center">
														<strong><c:out value="${rowVal}"/></strong>
													</td>
													<c:if test="${cntr == range}"></tr></c:if>
												</c:if>
												<c:set var="cntr" value="${cntr + 1}" />
											</nested:iterate>
										</tr>
									</c:if>
									<c:if test="${flag == false}">
										<tr>
											<nested:iterate id="rowVal">
												<c:if test="${flag == false}">
													<td align="center">
														<strong><c:out value="${rowVal}"/></strong>
													</td>
												</c:if>
												<c:if test="${flag == true}">
													<td align="center">
														<c:out value="${rowVal}"/>
														<%--
														<fmt:formatNumber type="number" value="${rowVal}" maxFractionDigits="0" />
														--%>
													</td>
												</c:if>
												<c:set var="flag" value="true" />
											</nested:iterate>
										</tr>
									</c:if>
									<c:set var="flag" value="false" />
								</nested:iterate>
							</table>
						</td>	
						
					</tr>
				</c:if>
			</table>
			</logic:notEmpty>
		</td>
		<td width="14" class="r-dotted-lg">
	</tr>
</table>