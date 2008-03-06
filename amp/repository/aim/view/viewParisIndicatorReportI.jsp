<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<%@ taglib uri="/taglib/fmt" prefix="fmt" %>

<%@ taglib uri="/taglib/category" prefix="category" %>




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

	function popup_pdf(val) {



		openResisableWindow(1000,1000);

		<digi:context name="pdf" property="context/module/moduleinstance/parisIndicatorReportPDFXLSCSV.do?docType=pdf" />

		document.aimParisIndicatorReportForm.action = "<%= pdf %>&pid="+val;

		document.aimParisIndicatorReportForm.target = popupPointer.name;

		document.aimParisIndicatorReportForm.submit();

	}



	/* CSV function start  */



		function popup_csv() {

		openResisableWindow(800, 600);

		<digi:context name="csv" property="context/module/moduleinstance/parisIndicatorReportPDFXLSCSV.do?docType=csv" />

		document.aimParisIndicatorReportForm.action = "<%= csv %>&pid="+val;

		document.aimParisIndicatorReportForm.target = popupPointer.name;

		document.aimParisIndicatorReportForm.submit();

	}

	/* CSV function end  */



	function popup_xls(val) {



		openResisableWindow(800, 600);

		<digi:context name="xls" property="context/module/moduleinstance/parisIndicatorReportPDFXLSCSV.do?docType=xls" />

		document.aimParisIndicatorReportForm.action = "<%= xls %>&pid="+val;

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

					<c:set var="translation">

						<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>

					</c:set>

					<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >

						<digi:trn key="aim:MyDesktop">Portfolio</digi:trn>

					</digi:link> &gt;

					 <c:set var="translation">

						<digi:trn key="aim:clickToViewAllAidParisReports">Click here to view list of all paris indicator Reports </digi:trn>

					 </c:set>

					 <digi:link href="/parisIndicatorReport.do" styleClass="comment" title="${translation}">

						<digi:trn key="aim:allParisIndcReportsList">Paris Indicator Reports List</digi:trn>

					 </digi:link>

				  </td>

				  <td width="2">&nbsp;</td>



			    </tr>

				<tr>
                  <td>
                    <table>

                        <tr>
                          <td class="subtitle-blue" style="width:70%;text-align:right;">
                            <digi:trn key="aim:parisIndicator">Paris Indicator</digi:trn>&nbsp;
                            ${aimParisIndicatorReportForm.indicatorCode}&nbsp;
                            <digi:trn key="aim:report">Report</digi:trn>
                          </td>
                          <feature:display  name="Target Value" module="PI Reports">
                            <td class="subtitle-blue" style="width:250px;text-align:right;">
                              <c:if test="${!empty aimParisIndicatorReportForm.targetValue && !empty aimParisIndicatorReportForm.calcResult}">
                              ${aimParisIndicatorReportForm.targetValue}&nbsp;
                              <digi:trn key="aim:piTargerValue">Target</digi:trn>:
                              ${aimParisIndicatorReportForm.calcResult}%
                              </c:if>
                            </td>
                          </feature:display>
                        </tr>
                    </table>
                  </td>
				</tr>

				<tr>

					<td colspan=3 class=box-title align=center>

					</td>

				</tr>



<!--  PDF/XLS Links -->

			<tr>



			<logic:greaterThan name="aimParisIndicatorReportForm" property="yearColl" value="3">

				<td valign="bottom" class="crumb">

					<%--<logic:notEmpty name="aimMulitlateralbyDonorForm" property="multiReport">--%>

						<img src="../ampTemplate/images/pdf_icon.gif" border=0>

						<c:set var="translation">

							<digi:trn key="aim:clickToCreateReportInPDF">Click here to Create Report in Pdf </digi:trn>

						</c:set>

						 <a href="javascript:popup_pdf('<bean:write name="aimParisIndicatorReportForm" property="indicatorCode" />')"> <digi:trn key="aim:createReportInPdf">Create Report in Pdf.</digi:trn> </a> &nbsp;&nbsp;

		        	<%--</logic:notEmpty>--%>

                </td>

            </logic:greaterThan>







           </tr>

			<tr>

						<td valign="bottom" class="crumb">

							<%--<logic:notEmpty name="aimParisIndicatorReportForm" property="multiReport">--%>

							<img src="../ampTemplate/images/xls_icon.jpg" border=0>

								<c:set var="translation">

									<digi:trn key="aim:clickToCreateReportInExcel">Click here to Create Report in Excel </digi:trn>

								</c:set>

							 <a href="javascript:popup_xls('<bean:write name="aimParisIndicatorReportForm" property="indicatorCode" />')"> <digi:trn key="aim:createReportInXls">Create Report in Xls.</digi:trn> </a> &nbsp;&nbsp;

		                	<%--</logic:notEmpty>--%>

		                </td>



            </tr>



				<%--	<logic:notEmpty name="aimMulitlateralbyDonorForm" property="multiReport">--%>

				<tr>

					<td colspan=4 align="left">

						<img src="../ampTemplate/images/print_icon.gif">

							<digi:link href="/ParisIndicatorPrintReports.do" target="_blank">

								<digi:trn key="aim:print">Print</digi:trn>

							</digi:link>

					</td>

				</tr>

			<%-- </logic:notEmpty>--%>



			<!-- CSV link

			<tr>



			        <td valign="bottom" class="crumb" >



					<img src="../ampTemplate/images/icon_csv.gif" border=0>

					<c:set var="translation">

						<digi:trn key="aim:clickToCreateReportInCVS">Click here to Create Report in CSV </digi:trn>

					</c:set>

					<digi:link href="" onclick="popup_csv(''); return false;" title="${translation}">

					 	<digi:trn key="aim:createReportInCsv">Create Report in CSV.</digi:trn>

					</digi:link>





            </td>

            </tr>





<!--  PDF/XLS Links -->





	<digi:form action="/parisIndicatorReport.do" >

	<html:hidden property="filterFlag" />

	<html:hidden property="indicatorId" />

				<tr>

					<td>

<%--===========================================================================================================================================--%>

						<table border="0" cellspacing="0" cellpadding="0" width="750">



							<tr bgcolor="#c0c0c0" height=20>

								<td>

									<table>

										<tr>

											<td>


                                                <c:set var="syear">
                                                ${aimParisIndicatorReportForm.startYear}
                                                </c:set>

												<select name="startYear" value="${syear}" class="dr-menu" onchange="chkYear('start')">

													<logic:notEmpty name="aimParisIndicatorReportForm" property="yearColl">

                                                      <c:forEach var="year" items="${aimParisIndicatorReportForm.yearColl}">
                                                        <c:if test="${syear == year}">
                                                          <option value="${year}" selected>${year}</option>
                                                        </c:if>

                                                        <c:if test="${syear != year}">
                                                          <option value="${year}">${year}</option>
                                                        </c:if>
                                                      </c:forEach>


													</logic:notEmpty>

												</select>

											</td>

											<td>
                                                <c:set var="cyear">
                                                ${aimParisIndicatorReportForm.closeYear}
                                                </c:set>

												<select name="closeYear" value="${cyear}" class="dr-menu" onchange="chkYear('close')">

													<logic:notEmpty name="aimParisIndicatorReportForm" property="yearColl">
                                                      <c:forEach var="year" items="${aimParisIndicatorReportForm.yearColl}">
                                                        <c:if test="${cyear == year}">
                                                          <option value="${year}" selected>${year}</option>
                                                        </c:if>

                                                        <c:if test="${cyear != year}">
                                                          <option value="${year}">${year}</option>
                                                        </c:if>
                                                      </c:forEach>
													</logic:notEmpty>

												</select>

											</td>

										<c:if test = "${aimParisIndicatorReportForm.indicatorCode != '10a'}">

											<td>

												<html:select property="calendar" name="aimParisIndicatorReportForm" styleClass="dr-menu" >

													<logic:notEmpty name="aimParisIndicatorReportForm" property="calendarColl">

														<html:optionsCollection name="aimParisIndicatorReportForm" property="calendarColl"

																				value="ampFiscalCalId" label="name"/>

													</logic:notEmpty>

												</html:select>

											</td>

											<td>

												<html:select property="perspective" name="aimParisIndicatorReportForm" styleClass="dr-menu" >

													<html:option value="MA"><digi:trn key="aim:MOFED">Mofed</digi:trn></html:option>

													<html:option value="DN"><digi:trn key="aim:DONOR">DONOR</digi:trn></html:option>

												</html:select>

											</td>

											<td>

												<html:select property="currency" name="aimParisIndicatorReportForm" styleClass="dr-menu" >

													<logic:notEmpty name="aimParisIndicatorReportForm" property="currencyColl">

														<html:optionsCollection name="aimParisIndicatorReportForm" property="currencyColl"

																				value="currencyCode" label="currencyName"/>

													</logic:notEmpty>

												</html:select>

											</td>

										</c:if>

											<td>

												<html:select property="donor" name="aimParisIndicatorReportForm" styleClass="dr-menu" >

													<html:option value="all"><digi:trn key="aim:allDonors">All Donors</digi:trn></html:option>

													<logic:notEmpty name="aimParisIndicatorReportForm" property="donorColl">

														<html:optionsCollection name="aimParisIndicatorReportForm" property="donorColl"

																				value="ampOrgId" label="acronym"/>

													</logic:notEmpty>

												</html:select>

											</td>

										<c:if test = "${aimParisIndicatorReportForm.indicatorCode != '10a'}">

											<td>
												<c:set var="translation">
														<digi:trn key="aim:PIFilterStatusFirstLine">All Statuses</digi:trn>
												</c:set>
												<category:showoptions firstLine="${translation}" styleClass="dr-menu" name="aimParisIndicatorReportForm" property="status" keyName="<%= org.digijava.module.aim.helper.CategoryConstants.ACTIVITY_STATUS_KEY %>" />

												<%--<html:select property="status" name="aimParisIndicatorReportForm" styleClass="dr-menu" >

													<html:option value="all"><digi:trn key="aim:allStatus">All Status</digi:trn></html:option>

													<logic:notEmpty name="aimParisIndicatorReportForm" property="statusColl">

														<html:optionsCollection name="aimParisIndicatorReportForm" property="statusColl"

																				value="name" label="name"  />

													</logic:notEmpty>

												</html:select> --%>

											</td>

										</c:if>


										<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '10a'}">

											<td>

												<html:select property="orgGroup" name="aimParisIndicatorReportForm" styleClass="dr-menu" >

													<html:option value="all"><digi:trn key="aim:allGroups">All Groups</digi:trn></html:option>

													<logic:notEmpty name="aimParisIndicatorReportForm" property="orgGroupColl">

														<html:optionsCollection name="aimParisIndicatorReportForm" property="orgGroupColl"

																				value="orgGrpCode" label="orgGrpName"/>

													</logic:notEmpty>

												</html:select>

											</td>

											<td>

												<input type="button" value="<digi:trn key='aim:go'>GO</digi:trn>" class="dr-menu" onclick="clearFilter()">

											</td>

										</c:if>

										<%--

											<td>

												<html:select property="termAssist" name="aimParisIndicatorReportForm" styleClass="dr-menu" >

													<html:option value="all">All Term Assist</html:option>

													<logic:notEmpty name="aimParisIndicatorReportForm" property="termAssistColl">

														<html:optionsCollection name="aimParisIndicatorReportForm" property="termAssistColl"

																				value="termsAssistName" label="termsAssistName"  />

													</logic:notEmpty>

												</html:select>

											</td>

										--%>

										</tr>

									</table>

								</td>

							</tr>

						<c:if test = "${aimParisIndicatorReportForm.indicatorCode != '10a'}">

							<tr bgcolor="#c0c0c0" height=20>

								<td>

									<table>

										<tr>

											<td>

												<html:select property="orgGroup" name="aimParisIndicatorReportForm" styleClass="dr-menu" >

													<html:option value="all"><digi:trn key="aim:allGroups">All Groups</digi:trn></html:option>

													<logic:notEmpty name="aimParisIndicatorReportForm" property="orgGroupColl">

														<html:optionsCollection name="aimParisIndicatorReportForm" property="orgGroupColl"

																				value="orgGrpCode" label="orgGrpName"/>

													</logic:notEmpty>

												</html:select>

											</td>

											<td>
												<c:set var="translation">
														<digi:trn key="aim:PIFilterFinInstrFirstLine">All Financing Instruments</digi:trn>
												</c:set>
												<category:showoptions firstLine="${translation}" styleClass="dr-menu" name="aimParisIndicatorReportForm" property="financingInstrument" keyName="<%= org.digijava.module.aim.helper.CategoryConstants.FINANCING_INSTRUMENT_KEY %>" />
												<%--<html:select property="financingInstrument" name="aimParisIndicatorReportForm" styleClass="dr-menu" >

													<html:option value="all"><digi:trn key="aim:allFinancingInstruments">All Financing Instruments</digi:trn></html:option>

													<logic:notEmpty name="aimParisIndicatorReportForm" property="financingInstrumentColl">

														<html:optionsCollection name="aimParisIndicatorReportForm" property="financingInstrumentColl"

																				value="name" label="name"  />

													</logic:notEmpty>

												</html:select> --%>

											</td>

											<td>

												<html:select property="sector" name="aimParisIndicatorReportForm" styleClass="dr-menu" >

													<html:option value="all"><digi:trn key="aim:allSectors">All Sectors</digi:trn></html:option>

													<logic:notEmpty name="aimParisIndicatorReportForm" property="sectorColl">

														<html:optionsCollection name="aimParisIndicatorReportForm" property="sectorColl" value="ampSectorId" label="name"  />

													</logic:notEmpty>

												</html:select>

											</td>



											<td>

												<input type="button" value="<digi:trn key='aim:go'>GO</digi:trn>" class="dr-menu" onclick="clearFilter()">

											</td>

										</tr>

									</table>

								</td>

							</tr>

						</c:if>

						</table>

<%--============================================================================================================================================--%>

					</td>



				</tr>



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

											<digi:trn key="aim:donors">Donor(s)</digi:trn>

										</strong>

									</div>

								  </td>

									 <td width="5%" height="33">

										<div align="center">

											<strong>

												<digi:trn key="aim:disbursmentYear">Disbursement Year</digi:trn>

											</strong>

										</div>

									</td>



								<!--Start of Indicator headers selection here-->

									<!-- Indicator 3 -->

									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '3'}">

									 <td width="27%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:aidFlowsGovernmentSectorReported">
											Aid flows to the government sector reported on the government's budget
											</digi:trn>
											</strong>

											</div>

										</td>

										<td width="26%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:totalAidFlowsDisbursed">
											Total Aid flows disbursed to the government sector
											</digi:trn>
											</strong>

											</div>

										</td>

									</c:if>



									<!--Indicator 4 -->

									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '4'}">

										<td width="27%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:VolumeOfTehnicalCoOperation">
											Volume of technical co-operation for capacity development provided through co-ordinated programmes
											</digi:trn>
											</strong>

											</div>

										</td>

										<td width="26%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:totalVolumeOfTehnicalCoOperation">
											Total volume of technical co-operation provided
											</digi:trn>
											</strong>

											</div>

										</td>

										<td width="27%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:noOfTC">
											% of TC for capacity development provided through coordinated programmes consistent with national development strategies
											</digi:trn>
											</strong>

											</div>

										</td>

									</c:if>

									<!--Indicator 5a -->

									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '5a'}">

										<td width="11%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:aidFlowsToGovernmentSectorBudget">
											Aid flows to the goverment sector that use national budget execution procedures
											</digi:trn>
											</strong>

											</div>

										</td>

										<td width="11%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:aidFlowsToGovernmentSectorFinancialReporting">
											Aid flows to the goverment sector that use national financial reporting procedures
											</digi:trn>
											</strong>

											</div>

										</td>

										<td width="11%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:aidFlowsToGovernmentSectorFinancialAuditing">
											Aid flows to the goverment sector that use national financial auditing procedures
											</digi:trn>
											</strong>

											</div>

										</td>

										<td width="11%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:odaThatUses">
											ODA that uses all 3 national PFM
											</digi:trn>
											</strong>

											</div>

										</td>

										<td width="11%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:totalAidFlowsDisbursed">
											Total aid flows disbursed to the government sector
											</digi:trn>
											</strong>

											</div>

										</td>

										<td width="11%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:proportionAidFlowsUsingOne">
											Proportion aid flows to the government sector using one of the 3 country PFM systems
											</digi:trn>
											</strong>

											</div>

										</td>

										<td width="11%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:proportionOfAidFlowsUsingAll">
											Proportion of aid flows to the government sector using all the 3 country PFM systems
											</digi:trn>
											</strong>

											</div>

										</td>

									</c:if>

									<!--Indicator 5b -->

									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '5b'}">

										<td width="27%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:aidFlowsToTheGovernmentSector">
											Aid flows to the government sector that use national procurement procedures
											</digi:trn>
											</strong>

											</div>

										</td>

										<td width="26%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:totalAidFlowsDisbured">
											Total aid flows disbursed to the government sector
											</digi:trn>
											</strong>

											</div>

										</td>

										<td width="27%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:proportionOfAidFlowsToTheGovernmentSector">
											Proportion of aid flows to the government sector using national procurement procedures
											</digi:trn>
											</strong>

											</div>

										</td>

									</c:if>

									<!--Indicator 9 -->

									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '9'}">

										<td width="20%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:budgetSupportAidFlowsInTheContextOfProgammeBasedApproach">
											Budget support aid flows provided in the context of programme based approach
											</digi:trn>
											</strong>

											</div>

										</td>

										<td width="20%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:otherAidFlowsProvidedInTheContextOfProgrammeBasedApproach">
											Other aid flows provided in the context of programme based approach
											</digi:trn>
											</strong>

											</div>

										</td>

										<td width="20%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:totalAidFlowsProvided">
											Total aid flows provided
											</digi:trn>
											</strong>

											</div>

										</td>

										<td width="20%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:proportionOfAidFlowsInTheContextOfProgrammeBasedApproach">
											Proportion of aid flows provided in the context of programme based approach
											</digi:trn>
											</strong>

											</div>

										</td>

									</c:if>

									<!--Indicator 10a -->

									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '10a'}">

										<td width="27%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:numberOfMissions">
											Number of missions to the field that are joint
											</digi:trn>

											</strong>

											</div>

										</td>

										<td width="26%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:totalNumberOfMissions">
											Total number of missions to the field
											</digi:trn>
											</strong>

											</div>

										</td>

									</c:if>

									<!--Indicator 10b -->

									<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '10b'}">

										<td width="27%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:numberOfCountryAnalytic">
											Number of country analytic reports that are joint
											</digi:trn>
											</strong>

											</div>

										</td>

										<td width="26%" height="33">

											<div align="center">

											<strong>
											<digi:trn key="aim:totalNumberOfCountryAnalytic">
											Total number of country analytic reports
											</digi:trn>
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
											<digi:trn key="aim:${aimParisIndicatorReportForm.indicatorNameTrn}">
												<c:out value="${aimParisIndicatorReportForm.indicatorName}"/>
											</digi:trn>
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

									<td width="15%" align="center" height="65" rowspan='${yearRange}' >

										<div align="center">
											<c:set var="key">
												<nested:write property="donor"/>
											</c:set>
                                            <c:set var="key">
												${fn:replace(key, " ", "")}
											</c:set>
                                            <c:set var="key">
												${fn:replace(key, "%", "")}
											</c:set>
                                            <c:set var="key">
												${fn:toLowerCase(key)}
											</c:set>
											<strong>
											<digi:trn key="aim:pi:${key}">
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
															<fmt:formatNumber type="number" value="${rowVal}" pattern="###" maxFractionDigits="0" />

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

																	<fmt:formatNumber type="number" value="${rowVal}" pattern="###" maxFractionDigits="0" />

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

				<c:if test = "${aimParisIndicatorReportForm.indicatorCode != '10a' &&

									aimParisIndicatorReportForm.indicatorCode != '10b'}">

					<tr><td><font color="blue">* <digi:trn key="aim:allTheAmounts">All the amounts are in thousands (000) </digi:trn>
                                                                    <c:set var="selCurrency">
                                                                      ${aimParisIndicatorReportForm.currency}
                                                                    </c:set>
													               <c:set var="selCurrency">
                                                                      ${fn:replace(selCurrency," ","")}
                                                                    </c:set>
                                                                    <c:set var="selCurrency">
                                                                      ${fn:replace(selCurrency,"%","")}
                                                                    </c:set>
                                                                    <c:set var="selCurrency">
                                                                      ${fn:toLowerCase(selCurrency)}
                                                                    </c:set>
                                                                    <digi:trn key="aim:currency:${selCurrency}">
                                                                      ${selCurrency}
                                                                    </digi:trn>

							</font>
					</td></tr>

				</c:if>

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

														<c:if test="${aimParisIndicatorReportForm.indicatorCode == '5a'}">
														<strong><digi:trn key="aim:percentOfODAUsingAll">Percent of ODA using all three partner's PFM procedures</digi:trn></strong>
														</c:if>
														<c:if test="${aimParisIndicatorReportForm.indicatorCode == '5b'}">
														<strong><digi:trn key="aim:percentOdODAUsingNational">Percent of ODA using national procurement systems</digi:trn></strong>
														</c:if>
													</td>

													<td align="center" colspan='<c:out value="${range}" />' >

														<c:if test="${aimParisIndicatorReportForm.indicatorCode == '5a'}">

															<strong><digi:trn key="aim:percentOfDonorsThatUseAllThree">Percent of donors that use all three partner's PFM procedures</digi:trn></strong>

														</c:if>

														<c:if test="${aimParisIndicatorReportForm.indicatorCode == '5b'}">

															<strong><digi:trn key="aim:percentOfDonorsThatUseNational">Percent of donors that use national procurement systems</digi:trn></strong>

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
                                                      <c:set var="rowValTrn">
                                                      ${fn:replace(rowVal, " ", "_")}
                                                      </c:set>
                                                      <c:set var="rowValTrn">
                                                      ${fn:replace(rowValTrn, "%", "")}
                                                      </c:set>
                                                      <strong>
                                                      <digi:trn key="aim:${rowValTrn}">
                                                      ${rowVal}
                                                      </digi:trn>
                                                      </strong>
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

						<td align="right" width="14" class="r-dotted-lg">&nbsp;</td>

					</tr>

				</c:if>

			</table>

			</logic:notEmpty>

		</td>

		<td width="14" class="r-dotted-lg">

	</tr>

</digi:form>

</table>




