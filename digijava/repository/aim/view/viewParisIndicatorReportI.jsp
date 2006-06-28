<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
<!--

	function clearFilter()
	{
		<digi:context name="clearVal" property="context/module/moduleinstance/parisIndicatorReport.do" />
		var param = document.aimParisIndicatorReportForm.indicatorId.value;
		document.aimParisIndicatorReportForm.filterFlag.value = "true";
		document.aimParisIndicatorReportForm.action = "<%= clearVal %>?indcId=" + param;
		document.aimParisIndicatorReportForm.target = "_self";
		document.aimParisIndicatorReportForm.submit();
	}

	function popup_pdf() {
		openResisableWindow(800, 600);
		<digi:context name="pdf" property="context/module/moduleinstance/projectByDonorPdf.do" />
		document.aimParisIndicatorReportForm.action = "<%= pdf %>";
		document.aimParisIndicatorReportForm.target = popupPointer.name;
		document.aimParisIndicatorReportForm.submit();
	}

	/* CSV function start  */

		function popup_csv() {
		openResisableWindow(800, 600);
		<digi:context name="csv" property="context/module/moduleinstance/projectByDonorXls.do?docType=csv" />
		document.aimParisIndicatorReportForm.action = "<%= csv %>";
		document.aimParisIndicatorReportForm.target = popupPointer.name;
		document.aimParisIndicatorReportForm.submit();
	}
	/* CSV function end  */

	function popup_xls() {
		openResisableWindow(800, 600);
		<digi:context name="xls" property="context/module/moduleinstance/projectByDonorXls.do?docType=xls" />
		document.aimParisIndicatorReportForm.action = "<%= xls %>";
		document.aimParisIndicatorReportForm.target = popupPointer.name;
		document.aimParisIndicatorReportForm.submit();
	}

	function popup_warn() {
		alert("Year Range selected should NOT be Greater than 3 Years.");
	}
	
	function chkYear(val) {
		var stYr = document.aimParisIndicatorReportForm.startYear.value;
		var clYr = document.aimParisIndicatorReportForm.closeYear.value;
		if (clYr < stYr) {
			if (val == 'start')
				alert("Start-year can not be greater than Close-year.");
			else
				alert("Close-year can not be less than Start-year.");
			return false;
		}
	}

-->
</script>

<digi:errors/>
<digi:instance property="aimParisIndicatorReportForm" />

<jsp:include page="teamPagesHeader.jsp" flush="true" />
<table width="772" border="0" cellpadding="10" cellspacing="0" bgcolor="#FFFFFF" >
	<tr>
		<td width="14" class="r-dotted-lg">
			&nbsp;
		</td>
		<td width="750" align="left" valign="top" BORDER="1">
			<table width="100%"  border="0" cellpadding="5" cellspacing="0">
		<%---------------------------------------------------------------------------------------------------------------------------------------%>		
				
				<tr>
				  <td valign="bottom" class="crumb" >
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
					</bean:define>
					<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:MyDesktop">Portfolio</digi:trn>
					</digi:link> &gt; 
					 <bean:define id="translation">
						<digi:trn key="aim:clickToViewAllAidParisReports">Click here to view list of all paris indicator Reports </digi:trn>
					 </bean:define>
					 <digi:link href="/parisIndicatorReport.do" styleClass="comment" title="<%=translation%>">
						<digi:trn key="aim:allParisIndcReportsList">Paris Indicator Reports List</digi:trn>
					 </digi:link>
				  </td>
				  <td width="2">&nbsp;</td>
			
			    </tr>
				<tr>
					<td colspan=3 class=subtitle-blue align=center>
						<digi:trn key="aim:parisIndicator">Paris Indicator</digi:trn>&nbsp;
						<c:out value="${aimParisIndicatorReportForm.indicatorCode}" />&nbsp;
						<digi:trn key="aim:report">Report</digi:trn>
					</td>
				</tr>
				<tr>
					<td colspan=3 class=box-title align=center>
					</td>
				</tr>

	<digi:form action="/parisIndicatorReport.do" >
	<html:hidden property="filterFlag" />
	<html:hidden property="indicatorId" />
				<tr>
					<td>
<%--===========================================================================================================================================--%>					
						<table border="0" cellspacing="0" cellpadding="2" width="750">
							


							<tr bgcolor="#c0c0c0" height=30>
								<td>
									<bean:define id="syear" property="startYear" name="aimParisIndicatorReportForm" />
									<select name="startYear" value="<%=syear%>" class="dr-menu" onchange="chkYear('start')">
										<logic:notEmpty name="aimParisIndicatorReportForm" property="yearColl">
											<logic:iterate id="year" name="aimParisIndicatorReportForm" property="yearColl">
												<c:if test="${syear == year}">
													<option value="<%=year%>" selected><%=year%></option>
												</c:if>
												<c:if test="${syear != year}">
													<option value="<%=year%>"><%=year%></option>
												</c:if>
											</logic:iterate>
										</logic:notEmpty>
									</select>
								</td>
								<td>
									<bean:define id="cyear" property="closeYear" name="aimParisIndicatorReportForm"/>
									<select name="closeYear" value="<%=cyear%>" class="dr-menu" onchange="chkYear('close')">
										<logic:notEmpty name="aimParisIndicatorReportForm" property="yearColl">
											<logic:iterate id="year" name="aimParisIndicatorReportForm" property="yearColl">
												<c:if test="${cyear == year}">
													<option value="<%=year%>" selected><%=year%></option>
												</c:if>
												<c:if test="${cyear != year}">
													<option value="<%=year%>"><%=year%></option>
												</c:if>
											</logic:iterate>
										</logic:notEmpty>
									</select>
								</td>
								<td>
									<html:select property="currency" name="aimParisIndicatorReportForm" styleClass="dr-menu" >
										<logic:notEmpty name="aimParisIndicatorReportForm" property="currencyColl">
											<html:optionsCollection name="aimParisIndicatorReportForm" property="currencyColl" 
																	value="currencyCode" label="currencyName"/> 
										</logic:notEmpty>
									</html:select>
								</td>
								<td>
									<html:select property="orgGroup" name="aimParisIndicatorReportForm" styleClass="dr-menu" >
										<html:option value="all">All Groups</html:option>
										<logic:notEmpty name="aimParisIndicatorReportForm" property="orgGroupColl">
											<html:optionsCollection name="aimParisIndicatorReportForm" property="orgGroupColl" 
																	value="orgGrpCode" label="orgGrpName"/>
										</logic:notEmpty>							
									</html:select>
								<td>
								<td>
									<html:select property="termAssist" name="aimParisIndicatorReportForm" styleClass="dr-menu" >
										<html:option value="all">All Term Assist</html:option>
										<logic:notEmpty name="aimParisIndicatorReportForm" property="termAssistColl">
											<html:optionsCollection name="aimParisIndicatorReportForm" property="termAssistColl" 
																	value="termsAssistName" label="termsAssistName"  /> 
										</logic:notEmpty>
									</html:select>
								</td>
								<td>
									<html:select property="financingInstrument" name="aimParisIndicatorReportForm" styleClass="dr-menu" >
										<html:option value="all">All Financing Instruments</html:option>
										<logic:notEmpty name="aimParisIndicatorReportForm" property="financingInstrumentColl">
											<html:optionsCollection name="aimParisIndicatorReportForm" property="financingInstrumentColl" 
																	value="name" label="name"  /> 
										</logic:notEmpty>
									</html:select>
								</td>
								<td>
									<html:select property="status" name="aimParisIndicatorReportForm" styleClass="dr-menu" >
										<html:option value="all">All Status</html:option>
										<logic:notEmpty name="aimParisIndicatorReportForm" property="statusColl">
											<html:optionsCollection name="aimParisIndicatorReportForm" property="statusColl" 
																	value="name" label="name"  /> 
										</logic:notEmpty>
									</html:select>
								</td>
								<td>
									<input type="button" value="GO" class="dr-menu" onclick="clearFilter()">
								</td>
							</tr>

						</table>
<%--============================================================================================================================================--%>
					</td>
					
				</tr>
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
									 <td width="35%" height="33">
											<div align="center">
											<strong>
											Aid flows to the government sector reported on the government's budget
											</strong>
											</div>
										</td>
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Total Aid flows disbursed to the government sector
											</strong>
											</div>
										</td>
									</c:if>
										
									<!--Indicator 4 -->
									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '4'}">
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Volume of technical co-operation for capacity development provided through co-ordinated programmes 
											</strong>
											</div>
										</td>
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Total volume of technical co-operation for capacity development provided 
											</strong>
											</div>
										</td>
									</c:if>
									<!--Indicator 5a -->
									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '5a'}">
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Aid flows to the goverment sector that use national budget execution procedures
											</strong>
											</div>
										</td>
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Aid flows to the goverment sector that use national financial reporting procedures
											</strong>
											</div>
										</td>
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Aid flows to the goverment sector that use national financial auditing procedures
											</strong>
											</div>
										</td>
										<td width="35%" height="33">
											<div align="center">
											<strong>
											ODA that uses all 3 national PFM
											</strong>
											</div>
										</td>
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Aid flows that use national PFMS
											</strong>
											</div>
										</td>
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Proportion aid flows to the government sector using one of the 3 country PFM systems
											</strong>
											</div>
										</td>
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Proportion of aid flows to the government sector using all the 3 country PFM systems
											</strong>
											</div>
										</td>
									</c:if>
									<!--Indicator 5b -->
									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '5b'}">
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Aid flows to the government sector that use national procurement procedures  
											</strong>
											</div>
										</td>
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Total aid flows disbursed to the government sector
											</strong>
											</div>
										</td>
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Proportion of aid flows to the government sector using national procurement procedures
											</strong>
											</div>
										</td>
									</c:if>
									<!--Indicator 9 -->
									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '9'}">
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Budget support aid flows provided in the context of programme based approach 
											</strong>
											</div>
										</td>
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Other aid flows provided in the context of programme based approach	
											</strong>
											</div>
										</td>
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Total aid flows provided
											</strong>
											</div>
										</td>
									</c:if>
									<!--Indicator 10a -->
									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '10a'}">
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Number of missions to the field that are joint  
											</strong>
											</div>
										</td>
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Total number of missions to the field
											</strong>
											</div>
										</td>
									</c:if>
									<!--Indicator 10a -->
									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '10b'}">
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Number of country analytic reports that are joint  
											</strong>
											</div>
										</td>
										<td width="35%" height="33">
											<div align="center">
											<strong>
											Total number of country analytic reports
											</strong>
											</div>
										</td>
									</c:if>
									<!-- end of c:if for all the Indicators-->
									<c:if test = "${aimParisIndicatorReportForm.indicatorCode != '5a' && 
													aimParisIndicatorReportForm.indicatorCode != '5b'}">	
									    <td width="10%" height="33">
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
											<strong>
											<nested:write property="donor" />
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
																	<c:out value="${rowVal}"/>%
																</c:when>
																<c:otherwise >
																	<c:out value="${rowVal}"/>
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:otherwise >
															<c:out value="${rowVal}"/>%
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
<%-----------------------------------------------------------------------------------------------------------------------------------------------------%>
						</table>
					</td>
					
				</tr>			
			</table>
		   </td>
		   <td width="14" class="r-dotted-lg">&nbsp;</td>
	</tr>
	<tr>
		<td width="24" class="r-dotted-lg">&nbsp;</td>
		<td>
			<logic:notEmpty name="aimParisIndicatorReportForm" property="donorsColl">
			<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<c:if test="${aimParisIndicatorReportForm.indicatorCode == '5a' || aimParisIndicatorReportForm.indicatorCode == '5b'}">
					<tr>
						<%--<td width="24" class="r-dotted-lg">&nbsp;</td>--%>
						<td align="center" colspan='<c:out value="${numCols + 1}" />' >
							<table border="1" width="50%" cellspacing="0" cellpadding="0">					
								<c:set var="flag" value="true" />
								<nested:iterate name="aimParisIndicatorReportForm" property="donorsCollIndc5">
									<c:if test="${flag == true}">
										<tr bgcolor="#F4F4F2">
											<nested:iterate id="rowVal">
												<td align="center">
													<strong><c:out value="${rowVal}"/></strong>
												</td>
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
						<td align="right" width="14" class="r-dotted-lg">&nbsp;</td>
					</tr>
				</c:if>
			</table>
			</logic:notEmpty>
		</td>
		<td width="14" class="r-dotted-lg">
	</tr>
</table>
