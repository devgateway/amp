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



		openResisableWindow(800, 600);

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

				<c:if test="${aimParisIndicatorReportForm.indicatorCode == 6}">

					<tr>

						<td colspan=3 class=subtitle-blue-2 align=center>

							[<digi:trn key="aim:numParallelPIU">Number Of Parallel PIUs</digi:trn>]&nbsp;

						</td>

					</tr>

				</c:if>

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



			<tr>

					<td colspan=4 align="left">

						<img src="../ampTemplate/images/print_icon.gif">

							<digi:link href="/ParisIndicatorPrintReports.do" target="_blank">

								<digi:trn key="aim:print">Print</digi:trn>

							</digi:link>

					</td>

				</tr>



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

					<c:if test="${aimParisIndicatorReportForm.indicatorCode != '6'}">

						<table border="0" cellspacing="0" cellpadding="0" width="750">

					</c:if>

					<c:if test="${aimParisIndicatorReportForm.indicatorCode == '6'}">

						<table border="0" cellspacing="0" cellpadding="0" width="350">

					</c:if>



							<tr bgcolor="#c0c0c0" height=20>

								<td>

									<table>

										<tr>

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

											<c:if test="${aimParisIndicatorReportForm.indicatorCode != '6'}">

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

											<td>

													<c:set var="translation">
														<digi:trn key="aim:PIFilterStatusFirstLine">All Statuses</digi:trn>
													</c:set>
													<category:showoptions firstLine="${translation}" styleClass="dr-menu" name="aimParisIndicatorReportForm" property="status" keyName="<%= org.digijava.module.aim.helper.CategoryConstants.ACTIVITY_STATUS_KEY %>" />
												<%-- <html:select property="status" name="aimParisIndicatorReportForm" styleClass="dr-menu" >

													<html:option value="all"><digi:trn key="aim:allStatus">All Status</digi:trn></html:option>

													<logic:notEmpty name="aimParisIndicatorReportForm" property="statusColl">

														<html:optionsCollection name="aimParisIndicatorReportForm" property="statusColl"

																				value="name" label="name"  />

													</logic:notEmpty>

												</html:select> --%>

											</td>

										</tr>

									</table>

								</td>

							</tr>

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

										<c:if test="${aimParisIndicatorReportForm.indicatorCode != '6'}">

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

										</c:if>

											<td>

												<html:select property="sector" name="aimParisIndicatorReportForm" styleClass="dr-menu" >

													<html:option value="all"><digi:trn key="aim:allSectors">All Sectors</digi:trn></html:option>

													<logic:notEmpty name="aimParisIndicatorReportForm" property="sectorColl">

														<html:optionsCollection name="aimParisIndicatorReportForm" property="sectorColl"

																				value="ampSectorId" label="name"  />

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

										<div align="center"><strong><digi:trn key="aim:donors">Donor(s)</digi:trn></strong></div>

								    </td>

								</c:if>

								<c:if test="${aimParisIndicatorReportForm.indicatorCode == '7'}">

									<td width="15%" height="25" rowspan="2">

										<div align="center"><strong><digi:trn key="aim:donors">Donor(s)</digi:trn></strong></div>

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
													<digi:trn key="aim:aidFlowsToTheGovernmentSectorScheduled">
													Aid flows to the government sector scheduled for fiscal year
													</digi:trn>
												</strong>

											</div>

										 </td>

										 <td width="15%" height="25" >

											<div align="center">

												<strong>
													<digi:trn key="aim:totalAidFlowsDisbursedToTheGovernmentSector">
													Total Aid flows disbursed to the government sector
													</digi:trn>
												</strong>

											</div>

										 </td>

										 <td width="15%" height="25" >

											<div align="center">

												<strong>
													<digi:trn key="aim:proportionOfAidToTheGovernmentSectorDisbursed">
													Proportion of aid to the government sector disbursed within the fiscal year it was scheduled
													</digi:trn>
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

												<strong>

												<nested:write property="donor" />

												</strong>

											</div>

										</td>

										<nested:iterate property="answers">

											<c:set var="index2" value="0" />

											<nested:iterate id="rowVal">

												<c:if test="${aimParisIndicatorReportForm.indicatorCode == '6'}">

													<td>

														<div align="center">
                                                            <fmt:formatNumber type="number" value="${rowVal}" pattern="###" maxFractionDigits="0" />

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
																		<fmt:formatNumber type="number" value="${rowVal}" pattern="###" maxFractionDigits="0" />%

																	</c:if>

																</c:if>

																<c:if test="${index1 != index2}">
                                                                    <fmt:formatNumber type="number" value="${rowVal}" pattern="###" maxFractionDigits="0" />

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

					<tr><td><font color="blue">* <digi:trn key="aim:allTheAmounts">All the amounts are in thousands (000)</digi:trn>
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
							</font></td></tr>

				</c:if>

<%----------------------------------------------------------------------------------------------------------------%>

						</table>

					</td>



				</tr>

			</table>

		 </td>

		 <td width="14" class="r-dotted-lg">&nbsp;</td>

	</tr>

</table>

