<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript">
<!--
	function openPage()
	{
	 	openResisableWindow(1000, 768);
		<digi:context name="clearVal" property="context/module/moduleinstance/htmlCommitmentbyModality.do" />
		document.aimCommitmentbyDonorForm.action = "<%= clearVal %>";
		document.aimCommitmentbyDonorForm.target = popupPointer.name;
		document.aimCommitmentbyDonorForm.submit();
	}

	function clearFilter()
	{
		<digi:context name="clear" property="context/module/moduleinstance/viewCommitmentbyModality.do" />
		document.aimCommitmentbyDonorForm.action = "<%= clear %>";
		document.aimCommitmentbyDonorForm.target = "_self";
		document.aimCommitmentbyDonorForm.submit();
	}

	function popup_pdf() {
		openResisableWindow(800, 600);
		<digi:context name="pdf" property="context/module/moduleinstance/commitmentByModalityPdf.do" />
		document.aimCommitmentbyDonorForm.action = "<%= pdf %>";
		document.aimCommitmentbyDonorForm.target = popupPointer.name;
		document.aimCommitmentbyDonorForm.submit();
	}

	/* CSV function start  */

		function popup_csv() {
		openResisableWindow(800, 600);
		<digi:context name="csv" property="context/module/moduleinstance/commitmentByModalityXls.do?docType=csv" />
		document.aimCommitmentbyDonorForm.action = "<%= csv %>";
		document.aimCommitmentbyDonorForm.target = popupPointer.name;
		document.aimCommitmentbyDonorForm.submit();
	}
	/* CSV function end  */

	function popup_xls() {
		openResisableWindow(800, 600);
		<digi:context name="xls" property="context/module/moduleinstance/commitmentByModalityXls.do?docType=xls" />
		document.aimCommitmentbyDonorForm.action = "<%= xls %>";
		document.aimCommitmentbyDonorForm.target = popupPointer.name;
		document.aimCommitmentbyDonorForm.submit();
	}

   function popup_warn() {
	alert(" Year Range Selected should NOT be more than 3 Years.");
  }

-->  
</script>

<digi:errors/>
<digi:instance property="aimCommitmentbyDonorForm" />

<jsp:include page="teamPagesHeader.jsp"  />
<table width="772" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
	<tr>

		<td width="750" align="left" valign="top" >
		
		<table width="100%"  border="0" cellpadding="5" cellspacing="0">			<tr>
			<td width="9" height="33"></td>
			<td valign="bottom" class="crumb" >
				<c:set var="translation">
					<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
				</c:set>
				<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}">
				<digi:trn key="aim:MyDesktop">My Desktop</digi:trn>
				</digi:link> &gt; 
				<c:set var="translation">
					<digi:trn key="aim:clickToViewAllReports">Click here to view list of all Reports </digi:trn>
				</c:set>
				<digi:link href="/viewTeamReports.do" styleClass="comment" title="${translation}">
				<digi:trn key="aim:AllReports">All Reports</digi:trn>
				</digi:link>&nbsp;&gt;&nbsp;
				<bean:write name="aimCommitmentbyDonorForm" property="perspective"/>&nbsp;
				<digi:trn key="aim:perspective">perspective</digi:trn></td>
				<td width="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan=3 class=subtitle-blue align=center>
					<digi:trn key="aim:TrendAnalysisTitle">
					<bean:write name="aimCommitmentbyDonorForm" property="reportName" />
					</digi:trn>
				</td>
			</tr>
			<tr>			<td width="14" class="r-dotted-lg">&nbsp;</td>
				<td colspan=3 class=box-title align=center>
					<bean:write name="aimCommitmentbyDonorForm" property="workspaceType" />&nbsp; <bean:write name="aimCommitmentbyDonorForm" property="workspaceName" />&nbsp; 
				<!--	<digi:trn key="aim:team">Team</digi:trn>	-->
				</td>
			</tr>
			
<logic:notEmpty name="aimCommitmentbyDonorForm" property="report">
<!--  PDF/XLS Links -->		
<!--			<tr>	
			<td width="14" class="r-dotted-lg">&nbsp;</td>
			<logic:greaterThan name="aimCommitmentbyDonorForm" property="yrCount" value="3">
				<td valign="bottom" class="crumb">
					<logic:notEmpty name="aimCommitmentbyDonorForm" property="report">
					&nbsp;&nbsp;<img src="../ampTemplate/images/pdf_icon.gif" border=0>
					<c:set var="translation">
						<digi:trn key="aim:clickToCreateReportInPDF">Click here to Create Report in Pdf </digi:trn>
					</c:set>
					<digi:link href="" onclick="popup_warn(''); return false;" title="${translation}" > 
						<digi:trn key="aim:Pdf">Create Report in Pdf.</digi:trn>
					</digi:link> 
                	</logic:notEmpty>
                </td>
            </logic:greaterThan>    

			<logic:lessEqual name="aimCommitmentbyDonorForm" property="yrCount" value="3">
				<td valign="bottom" class="crumb" >
					<logic:notEmpty name="aimCommitmentbyDonorForm" property="report">&nbsp;&nbsp;
					<img src="../ampTemplate/images/pdf_icon.gif" border=0>				
						<a href="javascript:popup_pdf()">
						<digi:trn key="aim:Pdf">Create Report in Pdf.</digi:trn>
						</a>
		
					<c:set var="translation">
						<digi:trn key="aim:clickToCreateReportInPDF">Click here to Create Report in Pdf </digi:trn>
					</c:set>
					<digi:link href="" onclick="popup_warn(''); return false;" title="${translation}" > 
						<digi:trn key="aim:Pdf">Create Report in Pdf.</digi:trn> 

					</digi:link>
                	</logic:notEmpty>
                </td>
            </logic:lessEqual>    
           <td valign="top" class="r-dotted-lg">&nbsp;</td>
           </tr>
		   -->

		   <logic:notEmpty name="aimCommitmentbyDonorForm" property="report">
				<tr>
					<td width="14" class="r-dotted-lg">&nbsp;</td>
					<td align="left">
					&nbsp;&nbsp;<img src="../ampTemplate/images/print_icon.gif">
				<digi:link href="/htmlCommitmentbyModality.do" target="_blank">
					Print
				</digi:link>
					</td>
				</tr>
			</logic:notEmpty>

			<tr>
				<td width="14" class="r-dotted-lg">&nbsp;</td>
						<td valign="bottom" class="crumb">
							<logic:notEmpty name="aimCommitmentbyDonorForm" property="report">
							&nbsp;&nbsp;<img src="../ampTemplate/images/pdf_icon.gif" border=0>	
							<c:set var="translation">
								<digi:trn key="aim:clickToCreateReportInPDF">Click here to Create Report in Pdf </digi:trn>
							</c:set>
							<digi:link href="" onclick="popup_pdf(''); return false;" title="${translation}">
								 <digi:trn key="aim:createReportInPdf">Create Report in Pdf.</digi:trn>
							</digi:link>
		                	</logic:notEmpty>
		                </td>
	          <td valign="top" class="r-dotted-lg">&nbsp;</td>
            </tr>
			<tr>
				<td width="14" class="r-dotted-lg">&nbsp;</td>
						<td valign="bottom" class="crumb">
							<logic:notEmpty name="aimCommitmentbyDonorForm" property="report">
							&nbsp;&nbsp;<img src="../ampTemplate/images/xls_icon.jpg" border=0>	
							<c:set var="translation">
								<digi:trn key="aim:clickToCreateReportInExcel">Click here to Create Report in Excel </digi:trn>
							</c:set>
							<digi:link href="" onclick="popup_xls(''); return false;" title="${translation}">
								 <digi:trn key="aim:createReportInXls">Create Report in Xls.</digi:trn>
							</digi:link>
		                	</logic:notEmpty>
		                </td>
	          <td valign="top" class="r-dotted-lg">&nbsp;</td>
            </tr>


			<!-- csv link -->
			<tr>
					<td width="14" class="r-dotted-lg" >&nbsp;</td>
			        <td valign="bottom" class="crumb" >
					<logic:notEmpty name="aimCommitmentbyDonorForm" property="report">
							&nbsp;
					<img src="../ampTemplate/images/icon_csv.gif" border=0>
					<c:set var="translation">
						<digi:trn key="aim:clickToCreateReportInCVS">Click here to Create Report in CSV </digi:trn>
					</c:set>
					<digi:link href="" onclick="popup_csv(''); return false;" title="${translation}">
					 	<digi:trn key="aim:createReportInCsv">Create Report in CSV.</digi:trn>
					</digi:link>
					</logic:notEmpty>
			
            </td>
			<td valign="top" class="r-dotted-lg">&nbsp;</td>
            </tr>

<!--  PDF/XLS Links -->	
</logic:notEmpty>
<digi:form action="/viewCommitmentbyModality.do" >
			
			<tr><td width="9"></td>
              
			<td width="100%" valign="middle"> 
				<div align="center"></div>
				<table border="0" cellspacing="0" cellpadding="2">
					<tr>
						<td> 
						<logic:equal name="aimCommitmentbyDonorForm" property="filterFlag" value="true">
							<html:select styleClass="dr-menu" property="perspectiveFilter">
								<html:option value="DN">Donor View</html:option>
								<html:option value="MA"><digi:trn key="aim:MOFED">Mofed</digi:trn></html:option>
							</html:select>
						</logic:equal>
						</td>

						<!--<logic:equal name="aimCommitmentbyDonorForm" property="adjustmentFlag" value="true">
						<td>
							<html:select styleClass="dr-menu" property="ampAdjustmentId">
								<html:option value="1">Actual</html:option>
								<html:option value="0">Planned</html:option>
							</html:select>
						</td>
						</logic:equal>-->

						<td><logic:notEmpty name="aimCommitmentbyDonorForm" property="currencyColl">
							<html:select property="ampCurrencyCode" styleClass="dr-menu" >
								<html:optionsCollection name="aimCommitmentbyDonorForm" property="currencyColl" value="currencyCode" label="currencyName"/> 
							</html:select>
						</logic:notEmpty>
						</td>
						<td>
						<logic:notEmpty name="aimCommitmentbyDonorForm" property="fiscalYears">
	 						<html:select property="fiscalCalId" styleClass="dr-menu">
								<html:optionsCollection name="aimCommitmentbyDonorForm" property="fiscalYears" value="ampFiscalCalId" label="name"/> 
							</html:select>
						</logic:notEmpty>
	 					</td>
	 					<td>
						<logic:notEmpty name="aimCommitmentbyDonorForm" property="ampFromYears">
	 						<html:select property="ampFromYear" styleClass="dr-menu">
								<html:options name="aimCommitmentbyDonorForm" property="ampFromYears" /> 
							</html:select> 
	 					</logic:notEmpty></td>
	 					<td>
						<logic:notEmpty name="aimCommitmentbyDonorForm" property="ampToYears">
	 						<html:select property="ampToYear" styleClass="dr-menu">
								<html:options name="aimCommitmentbyDonorForm" property="ampToYears" /> 
							</html:select> 
	 					</logic:notEmpty></td>
						<td>
						<logic:notEmpty name="aimCommitmentbyDonorForm" property="regionColl">
						    <html:select property="ampLocationId" styleClass="dr-menu" >
								<option value="All">All Regions</option>
									<html:optionsCollection name="aimCommitmentbyDonorForm" property="regionColl" value="name" 
									label="name"  /> 
							</html:select>
						</logic:notEmpty></td>
						<td>
						<logic:notEmpty name="aimCommitmentbyDonorForm" property="modalityColl">
						    <html:select property="ampModalityId" styleClass="dr-menu" >
								<option value="0">All Financing Instruments</option>
								<html:optionsCollection name="aimCommitmentbyDonorForm" property="modalityColl" value="ampModalityId" label="name" /> 
							</html:select>
		 				</logic:notEmpty></td>
						<td> 
						<logic:notEmpty name="aimCommitmentbyDonorForm" property="donorColl">
							<html:select property="ampOrgId" styleClass="dr-menu" >
								<option value="0">All Donors</option>
								<html:optionsCollection name="aimCommitmentbyDonorForm" property="donorColl" value="ampOrgId" label="acronym"/> 
							</html:select>
						</logic:notEmpty></td>
						<td> 
						<logic:notEmpty name="aimCommitmentbyDonorForm" property="statusColl">
							<html:select property="ampStatusId" styleClass="dr-menu" >
								<option value="0">All Status</option>
								<html:optionsCollection name="aimCommitmentbyDonorForm" property="statusColl" value="ampStatusId" label="name"  /> 
							</html:select>
						</logic:notEmpty>
						</td>
						<td> 
						<logic:notEmpty name="aimCommitmentbyDonorForm" property="sectorColl">
							<html:select property="ampSectorId" styleClass="dr-menu" >
								<option value="0">All Sectors</option>
								<html:optionsCollection name="aimCommitmentbyDonorForm" property="sectorColl" value="ampSectorId" label="name" /> 
							</html:select>
						</logic:notEmpty>
						</td>
						<logic:notEmpty name="aimCommitmentbyDonorForm" property="ampStartDays">
						<a title="<digi:trn key="aim:DateofStart">Date (dd/mm/yy) when the project commenced (effective start date)</digi:trn>">
						<td>
						
	 						<html:select property="startDay" styleClass="dr-menu">
								<option value="0">DD</option>
								<html:options name="aimCommitmentbyDonorForm" 	property="ampStartDays" /> 
							</html:select>
							
		 				</td>
						<td>
	 						<html:select property="startMonth" styleClass="dr-menu">
								<option value="0">MON</option>
	 							<html:option value="1">Jan</html:option>
								<html:option value="2">Feb</html:option>
								<html:option value="3">March</html:option>
								<html:option value="4">April</html:option>
								<html:option value="5">May</html:option>
								<html:option value="6">June</html:option>
								<html:option value="7">July</html:option>
								<html:option value="8">Aug</html:option>
								<html:option value="9">Sept</html:option>
								<html:option value="10">Oct</html:option>
								<html:option value="11">Nov</html:option>
								<html:option value="12">Dec</html:option>
							</html:select>
	 					</td>
						<td>
	 						<html:select property="startYear" styleClass="dr-menu">
							<option value="0">YYYY</option>
							<html:options name="aimCommitmentbyDonorForm" property="ampStartYears" /> 
							</html:select> 
		 				</td>
						</a>
						</logic:notEmpty>
						<logic:notEmpty name="aimCommitmentbyDonorForm" property="ampCloseDays">
						<a title="<digi:trn key="aim:DateofClose">Date (dd/mm/yy) in which the project ended or is expected to end</digi:trn>">
						<td>
	 						<html:select property="closeDay" styleClass="dr-menu">
							<option value="0">DD</option>
							<html:options name="aimCommitmentbyDonorForm" property="ampCloseDays" /> 
							</html:select> 
	 					</td>
						<td>
	 						<html:select property="closeMonth" styleClass="dr-menu">
	 						<option value="0">MON</option>
	 						<html:option value="1">Jan</html:option>
							<html:option value="2">Feb</html:option>
							<html:option value="3">March</html:option>
							<html:option value="4">April</html:option>
							<html:option value="5">May</html:option>
							<html:option value="6">June</html:option>
							<html:option value="7">July</html:option>
							<html:option value="8">Aug</html:option>
							<html:option value="9">Sept</html:option>
							<html:option value="10">Oct</html:option>
							<html:option value="11">Nov</html:option>
							<html:option value="12">Dec</html:option>
							</html:select>
	 					</td>
						<td>
							<html:select property="closeYear" styleClass="dr-menu">
							<option value="0">YYYY</option>
							<html:options name="aimCommitmentbyDonorForm" property="ampCloseYears" /> 
							</html:select> 
						</td>
						</a>
						</logic:notEmpty>
						<td><logic:equal name="aimCommitmentbyDonorForm" property="goFlag" value="true"><input type="submit" name="Submit" value=" GO " class="dr-menu"></logic:equal></td>
				    </tr>
	            </table>
			</td>
            <td width="2" valign="middle">&nbsp;</td>
		</tr>
		
		<tr>
			<td width="9"></td>
            <td >
				<table width="100%"  border="0" cellpadding="0" cellspacing="0">
				<tr bgcolor="#FFFFFF">
              <td colspan="10"><table border="0" cellspacing="0" cellpadding="0">
                  <tr bgcolor="#C9C9C7">
                    <td bgcolor="#C9C9C7" class="box-title">
					 <digi:trn key="aim:TrendAnalysis">
					 Trend Analysis
					 </digi:trn>
					 </td>
                    <td bgcolor="#FFFFFF"><img src="../ampTemplate/images/corner-r.gif" width="17" height="17"></td>
                  </tr>
              </table></td>
            </tr>
            <tr bgcolor="#FFFFFF">
              <td valign="top" colspan="10"><table width="100%"  border="0" cellpadding="1" cellspacing="1" bgcolor="#C6C7C4">
                 <tr bgcolor="#F4F4F2">
					<td rowspan="3" align="center" height="21" width="73" >
					<div align="center"><strong>
					<a title="<digi:trn key="aim:DonorName">The country or agency that financed the project </digi:trn>">
					<digi:trn key="aim:donor">Donor</digi:trn>
					</a></strong>
					</div>
					</td>
					<td rowspan="3" align="center" height="21" width="80"><div align="center"><strong>
					<a title="<digi:trn key="aim:ProjectTitleinReports">Title used in donors or MoFED internal systems</digi:trn>">
					<digi:trn key="aim:title">Title</digi:trn>
					</a></strong></div>
					</td>
					<td rowspan="3" align="center" height="21" width="80"><div align="center"><strong>
					<a title="<digi:trn key="aim:StatusoftheProject">Current stage of project</digi:trn>">
					<digi:trn key="aim:status">Status </digi:trn>
					</a></strong></div>
					</td>
					<td rowspan="3" align="center" height="21" width="52"><div align="center"><strong>
					<a title="<digi:trn key="aim:TypeoftheAssist">Type of flow used to finance the project: grant, loan or in kind</digi:trn>">
					<digi:trn key="aim:typeOftheAssistance">Type Of Assistance</digi:trn>
					</a></strong></div>
					</td>
					<td rowspan="3" align="center" height="21" width="77"><div align="center"><strong>
					<a title="<digi:trn key="aim:FundingInstrumental">Method by which aid is delivered to an activity</digi:trn>">
					<digi:trn key="aim:instrumentofFunding">Instrument Of Funding</digi:trn>
					</a></strong></div>
					</td>
					<td rowspan="3" align="center" height="21" width="67"><div align="center"><strong>
					<a title="<digi:trn key="aim:SectorSpecific">Specific area the the project is intended to foster</digi:trn>">
					<digi:trn key="aim:sector">Sector </digi:trn>
					</a>
					</strong></div></td>
					<td rowspan="3" align="center" height="21" width="40"> <div align="center"><strong>
					<a title="<digi:trn key="aim:LevelImplemented">Federal and regional programs are the scope of a project. They are a classification of the sponsorship of the project or program. This works in conjunction with location</digi:trn>">
					<digi:trn key="aim:implementationLevel">Implementation Level</digi:trn>
					</a></strong></div>
					</td>
					<td rowspan="3" align="center" height="21" width="69"><div align="center"><strong>
					<a title="<digi:trn key="aim:ImplementedLocation">The regions, zones and woredas in which the project is implemented</digi:trn>">
					<digi:trn key="aim:Implelocation">Location</digi:trn>
					</a></strong></div>
					</td>
					<td rowspan="3" align="center" height="21" width="35"> <div align="center"><strong>
					<a title="<digi:trn key="aim:DateofStart">Date (dd/mm/yy) when the project commenced (effective start date)</digi:trn>">
					<digi:trn key="aim:startDate">Start Date</digi:trn>
					</a></strong>
					</td>
					<td rowspan="3" align="center" height="21" width="39"> <div align="center"><strong>
					<a title="<digi:trn key="aim:DateofClose">Date (dd/mm/yy) in which the project ended or is expected to end</digi:trn>">
					<digi:trn key="aim:closeDate">Close Date</digi:trn>
					</a></strong></div>
					</td>
					<td rowspan="3" align="center" height="21" width="39"> <div align="center"><strong>
					<a title="<digi:trn key="aim:DateofCommitmentDone">The date (day, month, year) when funding commitment was signed</digi:trn>">
					<digi:trn key="aim:commDate">Commitment Date</digi:trn>
					</a></strong></div>
					</td>
					<td rowspan="3" align="center" height="21" width="69"><div align="center"><strong>
					<a title="<digi:trn key="aim:CommitmentinTotal">Total Commitment Amount of Activity</digi:trn>">
					<digi:trn key="aim:totalCommitments">Total Commitment</digi:trn>
					</a></strong></div>
					</td>
	
<logic:iterate name="aimCommitmentbyDonorForm"  property="report" id="report" length="1" type="org.digijava.module.aim.helper.Report">
	<td colspan = <bean:write name="aimCommitmentbyDonorForm" property="totalColumns" /> align="center" height="21"><div align="center"><strong>
	<digi:trn key="aim:year">Year</digi:trn>
	</strong></div></td>
</logic:iterate>	
					<td rowspan="2" colspan="5" align="center" height="21" ><div align="center"><strong>
					<digi:trn key="aim:total">Total</digi:trn> 
					</strong></div></td>
					
				</tr>
			<tr bgcolor="#F4F4F2">
<logic:iterate name="aimCommitmentbyDonorForm"  property="fiscalYearRange" id="fiscalYearRange">
				<td height="21" width="69" colspan="4" align="center" >
				<div align="center"><strong><%=fiscalYearRange%></strong></div>
				</td>
</logic:iterate>	

			</tr>
			<tr bgcolor="#FFFFFF">
			
			<logic:iterate name="aimCommitmentbyDonorForm"  property="fiscalYearRange" id="fiscalYearRange">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
					<digi:trn key="aim:commitment">Commitment</digi:trn>
					</a>
					</td>
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:PlannedDisbursementofFund">Expected disbursements</digi:trn>">
					<digi:trn key="aim:plannedDisbursements">Planned Disbursement</digi:trn>
					</a>
					</td>
    				<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
					<digi:trn key="aim:disbursements">Disbursement</digi:trn>
					</a>
					</td>
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
					<digi:trn key="aim:expenditure">Expenditure</digi:trn>
					</a>
					</td>
		   	
			</logic:iterate>
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
					<digi:trn key="aim:commitment">Commitment</digi:trn>
					</a>
					</td>
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:PlannedDisbursementofFund">Expected disbursements</digi:trn>">
					<digi:trn key="aim:plannedDisbursements">Planned Disbursement</digi:trn>
					</a>
					</td>
    				<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
					<digi:trn key="aim:disbursements">Disbursement</digi:trn>
					</a>
					</td>
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
					<digi:trn key="aim:expenditure">Expenditure</digi:trn>
					</a>
					</td>
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:undisbursedAmount">Undisbursed</digi:trn>">
					<digi:trn key="aim:undisbursed">Undisbursed</digi:trn>
					</a>
					</td>
  			</tr>	
  				<tr bgcolor="#FFFFFF">
  				<td colspan=100 align="center" height="21" width="73">
					
					</td>

  				</tr>
  			<logic:empty name="aimCommitmentbyDonorForm" property="report"> 
		<tr bgcolor="#F4F4F2">
				<!--<td align="center" height="21" width="73" ><b>Sector</b></td>-->
				<td colspan=<bean:write name="aimCommitmentbyDonorForm" property="totalColumns"/> align="center" height="21">
				<b><digi:trn key="aim:noRecords">No Records</digi:trn></b>
				</td>
			</tr>
		</logic:empty>
		<logic:notEmpty name="aimCommitmentbyDonorForm" property="report"> 
			<logic:iterate name="aimCommitmentbyDonorForm"  property="report" id="report" type="org.digijava.module.aim.helper.Report">
 			 <tr bgcolor="#F4F4F2">
    				<td align="left" height="21" width="73" >
					<logic:empty name="report" property="donors">&nbsp;
					</logic:empty>
					<logic:notEmpty name="report" property="donors">
					<logic:iterate name="report" id="donors" property="donors"> <%=donors%>	
					<br>
					</logic:iterate>
					</logic:notEmpty>
					</td>
					<td align="left" height="21" width="80"> 
						<bean:write name="report" property="title" />
					</td>					
					<td align="left" height="21" width="80"> 
						<bean:write name="report" property="status" />
					</td>					
				   	<td align="left" height="21" width="52">
					<logic:empty name="report" property="assistance">&nbsp;
					</logic:empty>
					<logic:notEmpty name="report" property="assistance">
					<logic:iterate name="report" id="assistance" property="assistance"> <%=assistance%>	
					<br>
					</logic:iterate>
					</logic:notEmpty>
					</td>
					<td align="left">
   					<logic:empty name="report" property="modality">&nbsp;
					</logic:empty>
					<logic:notEmpty name="report" property="modality">
					<logic:iterate name="report" id="modality" property="modality"> <%=modality%>	
					<br>
					</logic:iterate>
					</logic:notEmpty>
					</td>
					
					<td align="left" height="21" width="67">
						<logic:empty name="report" property="sectors">&nbsp;
					</logic:empty>
					<logic:notEmpty name="report" property="sectors">
					<logic:iterate name="report" id="sectors" property="sectors"> <%=sectors%>	
					<br>
					</logic:iterate>
					</logic:notEmpty>
					</td>
					<td align="left" height="21" width="40"> 
					<bean:write name="report" property="level" />
					</td>
					<td align="left" height="21" width="69">
					<logic:empty name="report" property="regions">&nbsp;
					</logic:empty>
					<logic:notEmpty name="report" property="regions">
					<logic:iterate name="report" id="regions" property="regions"> <%=regions%>	
					<br>
					</logic:iterate>
					</logic:notEmpty>
					</td>
					<td align="right" height="21" width="35">
					<bean:write name="report" property="startDate" />
					</td>
					<td align="right" height="21" width="39">
					<bean:write name="report" property="closeDate" />
					</td>
					<td align="right" height="21" width="69">
					<logic:empty name="report" property="commitmentDate">&nbsp;
					</logic:empty>
					<logic:notEmpty name="report" property="commitmentDate">
					<logic:iterate name="report" id="commitmentDate" property="commitmentDate"> <%=commitmentDate%>	
					<br>
					</logic:iterate>
					</logic:notEmpty>
					</td>
					<td align="right" height="21" width="69">
						<logic:notEqual name="report" property="acCommitment" value="0">
						<bean:write name="report" property="acCommitment" />
						</logic:notEqual>
					</td>


					<logic:iterate name="report"  property="ampFund" id="ampFund" 	type="org.digijava.module.aim.helper.AmpFund">
						<td align="right" height="21" width="69">
							<logic:notEqual name="ampFund" property="commAmount" value="0">
							<bean:write name="ampFund" property="commAmount" />
							</logic:notEqual>
						</td>
						<td align="right" height="21" width="69">
							<logic:notEqual name="ampFund" property="plannedDisbAmount" value="0">
							<bean:write name="ampFund" property="plannedDisbAmount" />
							</logic:notEqual>
						</td>
						<td align="right" height="21" width="69">
							<logic:notEqual name="ampFund" property="disbAmount" value="0">
							<bean:write name="ampFund" property="disbAmount" />
							</logic:notEqual>
						</td>
						<td align="right" height="21" width="69">
							<logic:notEqual name="ampFund" property="expAmount" value="0">
							<bean:write name="ampFund" property="expAmount" />
							</logic:notEqual>
						</td>
					</logic:iterate>
				<td align="right" height="21" width="69">
				<logic:iterate name="report"  property="ampFund" id="ampFund" 	type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
						<bean:write name="ampFund" property="unDisbAmount" />
						</logic:notEqual>
				</logic:iterate>		
				</td>	
					
		 </tr>
		 </logic:iterate>
		<tr><td colspan="11" align="left"><b>
		<digi:trn key="aim:total">Total</digi:trn>
		</b></td><td>
			<logic:notEqual name="aimCommitmentbyDonorForm" property="totComm" value="0">
			<bean:write name="aimCommitmentbyDonorForm" property="totComm" />
			</logic:notEqual>
		</td>
		 <logic:iterate name="aimCommitmentbyDonorForm"  property="totFund" id="totFund" type="org.digijava.module.aim.helper.AmpFund">
				<td align="right" height="21" width="69">
		 			<logic:notEqual name="totFund" property="commAmount" value="0">
					<bean:write name="totFund" property="commAmount" />
					</logic:notEqual>
				</td>
		 <td align="right" height="21" width="69">
		 			<logic:notEqual name="totFund" property="plannedDisbAmount" value="0">
					<bean:write name="totFund" property="plannedDisbAmount" />
					</logic:notEqual>
				</td>
				<td align="right" height="21" width="69">
					<logic:notEqual name="totFund" property="disbAmount" value="0">
					<bean:write name="totFund" property="disbAmount" />
					</logic:notEqual>
				</td>

				<td align="right" height="21" width="69">
					<logic:notEqual name="totFund" property="expAmount" value="0">
					<bean:write name="totFund" property="expAmount" />
					</logic:notEqual>
				</td>
			</logic:iterate>
			<td align="right" height="21" width="69">
			<logic:iterate name="aimCommitmentbyDonorForm"  property="totFund" id="totFund" type="org.digijava.module.aim.helper.AmpFund">
					<logic:notEqual name="totFund" property="unDisbAmount" value="0">
					<bean:write name="totFund" property="unDisbAmount" />
					</logic:notEqual>
			</logic:iterate>		
			</td>
			</tr>
</logic:notEmpty>		 
		 
        
		 
                  </table></td>

              </tr>

<logic:notEmpty name="aimCommitmentbyDonorForm" property="pages">
          </table>
			  
            </td>
              <td width="2">&nbsp;</td>
            </tr>
      
			<tr>
                <td>&nbsp;</td>
            
              <td width="100%" valign="middle" >
              <table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2">
              <tr bgcolor="#F4F4F2"> 
			  
			  
			<tr>
			<td>
<!--			
			Page <bean:write name="aimCommitmentbyDonorForm" property="page" />
			<digi:trn key="aim:ofPages">
			of Pages :
			</digi:trn>
-->			
<!-- -------------------------------  Prevoius Links     ---------------------------       -->
			<bean:define id="currPage" name="aimCommitmentbyDonorForm" property="page" type="java.lang.Integer" /> 
				<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
				<c:set target="${urlParams2}" property="page">
					<%=(currPage.intValue()-1)%>
				</c:set>
				<logic:notEqual name="aimCommitmentbyDonorForm" property="page" value="1">
					<c:set var="translation">
						<digi:trn key="aim:clickToViewPreviousPage">Click here to go to Previous page</digi:trn>
					</c:set>
					<digi:link href="/viewCommitmentbyModality.do" name="urlParams2" title="${translation}" >
						Previous 
					</digi:link>
					&nbsp;
				</logic:notEqual>
					
				<logic:equal name="aimCommitmentbyDonorForm" property="page" value="1">
					<digi:trn key="aim:prev">Previous</digi:trn> &nbsp;
				</logic:equal>
<!----------------------------------END   -----------------------------------------------     -->									

			
			<logic:iterate name="aimCommitmentbyDonorForm" property="pages" id="pages" type="java.lang.Integer">
			<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
			<c:set target="${urlParams1}" property="page">
				<%=pages%>
			</c:set>
			
			<%  int curr = currPage.intValue();
				int cnt = pages.intValue();
				System.out.println(curr + " Comparison : " + cnt);
			%>
			<% if( curr != cnt ) { %>
				<c:set var="translation">
					<digi:trn key="aim:clickToViewNextPage">Click here to go to Next page</digi:trn>
				</c:set>
				<digi:link href="/viewCommitmentbyModality.do" name="urlParams1" title="${translation}" >
					<%=pages%>
				</digi:link>
			<% } else { %>
				<%=pages%>
			<% } %>			
			|&nbsp; 
			</logic:iterate>

<!-- -------------------------------  Next Links -------------------------------       -->									
			<bean:define id="currPage" name="aimCommitmentbyDonorForm" property="page" type="java.lang.Integer" /> 
			<jsp:useBean id="urlParams3" type="java.util.Map" class="java.util.HashMap"/>
			<c:set target="${urlParams3}" property="page">
				<%=(currPage.intValue()+1)%>
			</c:set>
									
			<bean:define name="aimCommitmentbyDonorForm" id="allPages" property="pages" 
			type="java.util.Collection" />
			<% if(allPages.size() == currPage.intValue()) { %>	
				&nbsp; <digi:trn key="aim:next">Next</digi:trn> 
			<% } else { %>
				<c:set var="translation">
					<digi:trn key="aim:clickToViewNextPage">Click here to go to Next page</digi:trn>
				</c:set>
				<digi:link href="/viewCommitmentbyModality.do" name="urlParams3" title="${translation}" >
					 Next
				</digi:link>
				&nbsp;
			<% } %>
<!-- ------------------------------------------------------------------------------  -->									
			
			</td>
			</tr>
			  
              </tr>  
              <tr bgcolor="#F4F4F2"> 
                <td  bgcolor="#F4F4F2">&nbsp;</td>
                <td align="right" bgcolor="#F4F4F2">&nbsp;</td>
              </tr>
            </table></td>

                      <td width="2" nowrap >&nbsp;</td>
             
            </tr>
			
          </table>
		  
		  </td>
		</tr>
</logic:notEmpty>  

</table>
<!--
<table   border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr bgcolor="#484846">
    <td bgcolor="#484846"><img src="../ampTemplate/images/feedback.gif" width="144" height="28"></td>
    
  </tr>
</table>
-->
</digi:form>



