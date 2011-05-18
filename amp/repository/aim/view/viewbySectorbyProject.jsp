<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
<!--

	function clearFilter()
	{
		<digi:context name="clearVal" property="context/module/moduleinstance/viewbySectorbyProject.do" />
		document.aimMulitlateralbyDonorForm.action = "<%= clearVal %>";
		document.aimMulitlateralbyDonorForm.target = "_self";
		document.aimMulitlateralbyDonorForm.submit();
	}

	function popup_pdf() {
		openResisableWindow(800, 600);
		<digi:context name="pdf" property="context/module/moduleinstance/sectorByProjectPdf.do" />
		document.aimMulitlateralbyDonorForm.action = "<%= pdf %>";
		document.aimMulitlateralbyDonorForm.target = popupPointer.name;
		document.aimMulitlateralbyDonorForm.submit();
	}

	/* CSV function start  */

		function popup_csv() {
		openResisableWindow(800, 600);
		<digi:context name="csv" property="context/module/moduleinstance/SectorByProjectXls.do?docType=csv" />
		document.aimMulitlateralbyDonorForm.action = "<%= csv %>";
		document.aimMulitlateralbyDonorForm.target = popupPointer.name;
		document.aimMulitlateralbyDonorForm.submit();
	}
	/* CSV function end  */

	function popup_xls() {
		openResisableWindow(800, 600);
		<digi:context name="xls" property="context/module/moduleinstance/SectorByProjectXls.do?docType=xls" />
		document.aimMulitlateralbyDonorForm.action = "<%= xls %>";
		document.aimMulitlateralbyDonorForm.target = popupPointer.name;
		document.aimMulitlateralbyDonorForm.submit();
	}

	function popup_warn() {
		alert("Year Range selected should NOT be Greater than 3 Years.");
	}

-->
</script>

<digi:errors/>
<digi:instance property="aimMulitlateralbyDonorForm" />


<jsp:include page="teamPagesHeader.jsp"  />
<table width="772" border="0" cellpadding="10" cellspacing="0" bgcolor="#FFFFFF" class="r-dotted">
<tr>
	
	<td width="14" class="r-dotted-lg">&nbsp;</td>
	<td width="750" align="left" valign="top" >
	<table width="100%"  border="0" cellpadding="5" cellspacing="0" >
           <tr>
              <td valign="bottom" class="crumb" >
<c:set var="translation">
	<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
</c:set>
                <digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}">
                	<digi:trn key="aim:MyDesktop">My Desktop</digi:trn></digi:link> &gt; 
				    <c:set var="translation">
						<digi:trn key="aim:clickToViewAllReports">Click here to view list of all Reports </digi:trn>
					</c:set>
					<digi:link href="/viewTeamReports.do" styleClass="comment" title="${translation}">
					<digi:trn key="aim:AllReports">All Reports</digi:trn>
					</digi:link> &nbsp;&gt;&nbsp;					
					<bean:write name="aimMulitlateralbyDonorForm" property="perspective"/>&nbsp;
				<digi:trn key="aim:perspective">perspective</digi:trn></td>
              <td width="2">&nbsp;</td>
            </tr>
			<tr>
				<td colspan=3 class=subtitle-blue align="center">
					<digi:trn key="aim:SectorbyProjectTitle">
					<bean:write name="aimMulitlateralbyDonorForm" property="reportName" />
					</digi:trn>
				</td>
			</tr>
			<tr>
				<td colspan=3 class=box-title align="center">
					<bean:write name="aimMulitlateralbyDonorForm" property="workspaceType" />&nbsp; <bean:write name="aimMulitlateralbyDonorForm" property="workspaceName" />&nbsp; 
				<!--	<digi:trn key="aim:team">Team</digi:trn>	-->
				</td>
			</tr>

			<logic:notEmpty name="aimMulitlateralbyDonorForm" property="multiReport">
				<tr>
					<td valign="bottom" class="crumb">
					&nbsp;&nbsp;<img src="../ampTemplate/images/print_icon.gif">
					<digi:link href="/htmlSectorbyProject.do" target="_blank">
						Print
					</digi:link>
				</td>
				</tr>
			</logic:notEmpty>
<!--  PDF/XLS Links -->		
	<logic:notEmpty name="aimMulitlateralbyDonorForm" property="multiReport">
		<tr>	
			<logic:greaterThan name="aimMulitlateralbyDonorForm" property="fiscalYrRange" value="3">
				<td valign="bottom" class="crumb">
					&nbsp;&nbsp;<img src="../ampTemplate/images/pdf_icon.gif" border=0>				
					<c:set var="translation">
						<digi:trn key="aim:clickToCreateReportInPDF">Click here to Create Report in Pdf </digi:trn>
					</c:set>
					<digi:link href="" onclick="popup_warn(''); return false;" title="<%translation%>"> 
						<digi:trn key="aim:createReportInPdf">Create Report in Pdf.</digi:trn> 	
					</digi:link>
                </td>
            </logic:greaterThan>    
			<logic:lessEqual name="aimMulitlateralbyDonorForm" property="fiscalYrRange" value="3">
				<td valign="bottom" class="crumb">
					&nbsp;&nbsp;<img src="../ampTemplate/images/pdf_icon.gif" border=0>				
					<c:set var="translation">
						<digi:trn key="aim:clickToCreateReportInPDF">Click here to Create Report in Pdf </digi:trn>
					</c:set>
					<digi:link href="" onclick="popup_pdf(''); return false;" title="${translation}"> 
						<digi:trn key="aim:createReportInPdf">Create Report in Pdf.</digi:trn> 	
					</digi:link>
                </td>
			</logic:lessEqual>
           </tr>
				<tr>
						<td valign="bottom" class="crumb">
							<logic:notEmpty name="aimMulitlateralbyDonorForm" property="multiReport">
							&nbsp;&nbsp;<img src="../ampTemplate/images/xls_icon.jpg" border=0>	
								<c:set var="translation">
									<digi:trn key="aim:clickToCreateReportInExcel">Click here to Create Report in Excel </digi:trn>
								</c:set>
								<digi:link href="" onclick="popup_xls(''); return false;" title="${translation}">
									<digi:trn key="aim:createReportInXls">Create Report in Xls.</digi:trn>	
								</digi:link>
		                	</logic:notEmpty>
		                </td>

            </tr>

			<!-- csv link -->
			<tr>

			        <td valign="bottom" class="crumb" >
					<logic:notEmpty name="aimMulitlateralbyDonorForm" property="multiReport">
					&nbsp;	
					<img src="../ampTemplate/images/icon_csv.gif" border=0>
					<c:set var="translation">
						<digi:trn key="aim:clickToCreateReportInCSV">Click here to Create Report in CSV </digi:trn>
					</c:set>
					<digi:link href="" onclick="popup_csv(''); return false;" title="${translation}">
					 	<digi:trn key="aim:createReportInCsv">Create Report in CSV.</digi:trn>
					</digi:link>
					</logic:notEmpty>
			
            </td>
            </tr>

</logic:notEmpty>  
<!--  PDF/XLS Links -->	

<digi:form action="/viewbySectorbyProject.do" >
			<tr><td width="9" ></td>
			
<bean:define id="fcount" name="aimMulitlateralbyDonorForm" property="filterCnt" type="java.lang.Integer" /> 
<%
	int fcnt = fcount.intValue();
	System.out.println("FC = " +fcnt);
	
%>              
            <table border="0" cellspacing="0" cellpadding="2">
			<TR>
			<logic:greaterThan name="aimMulitlateralbyDonorForm" property="filterCnt" value="0" >
			<TD bgColor=black colspan=<%= fcnt+1%> ></TD>
			</logic:greaterThan>
			</TR>

			<tr bgcolor="#c0c0c0" height=30>

              		<logic:equal name="aimMulitlateralbyDonorForm" property="filterFlag" value="true">
						<td> 
							<html:select styleClass="dr-menu" property="perspectiveFilter">
								<html:option value="DN">Donor View</html:option>
								<html:option value="MA"><digi:trn key="aim:MOFED">Mofed</digi:trn></html:option>
							</html:select>
						</td> 
						</logic:equal>

					<!--	<logic:equal name="aimMulitlateralbyDonorForm" property="adjustmentFlag" value="true">
						<td>
							<html:select styleClass="dr-menu" property="ampAdjustmentId">
								<html:option value="1">Actual</html:option>
								<html:option value="0">Planned</html:option>
							</html:select>
						</td>
						</logic:equal>-->

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="currencyColl">
						<td>						
							<html:select property="ampCurrencyCode" styleClass="dr-menu" >
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="currencyColl" value="currencyCode" label="currencyName"/> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="fiscalYears">
						<td>
							<html:select property="fiscalCalId" styleClass="dr-menu">
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="fiscalYears" value="ampFiscalCalId" label="name"/> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="ampFromYears">
						<td>	 					
							<html:select property="ampFromYear" styleClass="dr-menu">
									<html:options name="aimMulitlateralbyDonorForm" property="ampFromYears" /> 
							</html:select> 
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="ampToYears">
						<td>
							<html:select property="ampToYear" styleClass="dr-menu">
								<html:options name="aimMulitlateralbyDonorForm" property="ampToYears" /> 
							</html:select>
						</td>							
	 					</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="regionColl">
						<td>						
							<html:select property="ampLocationId" styleClass="dr-menu" >
								<option value="All">All Regions</option>
									<html:optionsCollection name="aimMulitlateralbyDonorForm" property="regionColl" value="name" 
									label="name"  /> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="modalityColl">
						<td>
							<html:select property="ampModalityId" styleClass="dr-menu" >
								<option value="0">All Financing Instruments</option>
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="modalityColl" value="ampModalityId" label="name" /> 
							</html:select>
						</td>
		 				</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="donorColl">
						<td>
							<html:select property="ampOrgId" styleClass="dr-menu" >
								<option value="0">All Donors</option>
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="donorColl" value="ampOrgId" label="acronym"/> 
							</html:select>
						</td>
						</logic:notEmpty> 
						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="statusColl">
						<td>						
							<html:select property="ampStatusId" styleClass="dr-menu" >
								<option value="0">All Status</option>
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="statusColl" value="ampStatusId" label="name"  /> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="sectorColl">
						<td>						
							<html:select property="ampSectorId" styleClass="dr-menu" >
								<option value="0">All Sectors</option>
								<html:optionsCollection name="aimMulitlateralbyDonorForm" property="sectorColl" value="ampSectorId" label="name" /> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="ampStartDays">
						<td>
							
	 						<html:select property="startDay" styleClass="dr-menu">
								<option value="0">DD</option>
								<html:options name="aimMulitlateralbyDonorForm" 	property="ampStartDays" /> 
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
							<html:options name="aimMulitlateralbyDonorForm" property="ampStartYears" /> 
							</html:select> 
		 				</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimMulitlateralbyDonorForm" property="ampCloseDays">
						<td>
	 						<html:select property="closeDay" styleClass="dr-menu">
							<option value="0">DD</option>
							<html:options name="aimMulitlateralbyDonorForm" property="ampCloseDays" /> 
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
							<html:options name="aimMulitlateralbyDonorForm" property="ampCloseYears" /> 
							</html:select> 
						</td>
						</logic:notEmpty>
						
						<logic:equal name="aimMulitlateralbyDonorForm" property="goFlag" value="true">
						<td>
						<input type="button" name="GoButton" value=" GO " class="dr-menu" onclick="clearFilter()">
						</td>						
						</logic:equal>
                </tr>

              </table></td>
             
              <td width="2" valign="middle">&nbsp;</td>
            </tr>
			
  <!-- 		<tr>
              <td valign="top" class="r-dotted-lg">&nbsp;</td>
              <td width="9">&nbsp;&nbsp;</td>

		</tr>-->
   		<tr>
              <td valign="top" class="r-dotted-lg">&nbsp;</td>
              <td >

			  <table width="100%"  border="0" cellpadding="0" cellspacing="0" >
            <tr bgcolor="#FFFFFF">
              <td colspan=<bean:write name="aimMulitlateralbyDonorForm" property="totalColumns"/>><table border="0" cellspacing="0" cellpadding="0">
                  <tr bgcolor="#C9C9C7">
                    <td bgcolor="#C9C9C7" class="box-title">
					<digi:trn key="aim:SectorbyProject">
					Commitment / Disbursement / Actual Exp by Sector / Donor / Project
					</digi:trn>
					</td>
                <td bgcolor="#FFFFFF"><img src="../ampTemplate/images/corner-r.gif" width="17" height="17"></td>
                  </tr>
      </table>
     
</tr>
 <tr bgcolor="#FFFFFF"><td valign="top" colspan="30">
 
	 <logic:empty name="aimMulitlateralbyDonorForm" property="multiReport"> 
	 <table cellspacing="1" cellPading=0 width="100%" border=0  bgcolor="#8B8B83" >

			    <tr bgcolor="#F4F4F2">
				<td rowspan=3 align="center" height="21" width="73" ><strong>
				<a title="<digi:trn key="aim:SerialNumber">Serial Number</digi:trn>">S.No</a></strong>
				</td>
				<td rowspan=3 align="center" height="21" width="52"><strong> 
				<a title="<digi:trn key="aim:DonorOrProjectName">The country or agency that financed the project Or Title used in donors or MoFED internal systems</digi:trn>">
				Donor / Project
				</a></strong>
				</td>
				<td colspan=<bean:write name="aimMulitlateralbyDonorForm" property="yearCount"/> align="center" height="21" ><strong>
				<digi:trn key="aim:year">Year</digi:trn>
				</strong> </td>
				<td rowspan="2" colspan="3" align="center" height="21" width="20%">
				<digi:trn key="aim:total">Total</digi:trn>
				</td>
			</tr>
			<tr bgcolor="#f4f4f2">
				<logic:iterate name="aimMulitlateralbyDonorForm"  property="fiscalYearRange" id="fiscalYearRange">
					<td colspan=4 align="center" height="21" width="77">
						<strong><%=fiscalYearRange%></strong> 
					</td>
				</logic:iterate>
			</tr>
			<tr bgcolor="#F4F4F2">
			<logic:iterate name="aimMulitlateralbyDonorForm"  property="fiscalYearRange" id="fiscalYearRange">
				<td align="center" height="21" width="77"> <digi:trn key="aim:comm">Comm</digi:trn></td>
				<td align="center" height="21" width="77"> <digi:trn key="aim:disb">Disb</digi:trn></td>
				<td align="center" height="21" width="77"> <digi:trn key="aim:exp">Exp</digi:trn></td>
				</logic:iterate>
				<td align="center" height="21" width="77"> <digi:trn key="aim:comm">Comm</digi:trn></td>
				<td align="center" height="21" width="77"> <digi:trn key="aim:disb">Disb</digi:trn></td>
				<td align="center" height="21" width="77"> <digi:trn key="aim:exp">Exp</digi:trn></td>
				</tr>
		<tr><td bgcolor="ffffff" align="center" colspan=<bean:write name="aimMulitlateralbyDonorForm" property="totalColumns"/>>
		<b>
		<digi:trn key="aim:noRecords">No Records</digi:trn>
		</b></td></tr></table>
		</logic:empty>


	<logic:notEmpty name="aimMulitlateralbyDonorForm"  property="multiReport">	
	<logic:iterate name="aimMulitlateralbyDonorForm"  property="multiReport" id="multiReport" type="org.digijava.module.aim.helper.multiReport">
		<table cellspacing="1" cellPadding=0 width="100%" border=0  bgcolor="#8B8B83" >
			<tr bgcolor="#F4F4F2">
				<!--<td align="center" height="21" width="73" ><b>Sector</b></td>-->
				<td colspan=100 align="left" height="21" >
					<a title="<digi:trn key="aim:SectorSpecific">Specific area the the project is intended to foster</digi:trn>">
					<digi:trn key="aim:sector">SECTOR :</digi:trn> 
					</a>
					<b><u><bean:write name="multiReport" property="sector" /></u></b>
				</td>
			</tr>
			<tr bgcolor="#F4F4F2">
				<td rowspan=3 align="center" height="21" width="73" ><strong>
				<a title="<digi:trn key="aim:SerialNumber">Serial Number</digi:trn>">
				<digi:trn key="aim:SerialNo">S.No  </digi:trn>
				</a></strong>
				</td>
				<td rowspan=3 align="center" height="21" width="52"><strong>
				<a title="<digi:trn key="aim:DonorOrProjectName">The country or agency that financed the project Or Title used in donors or MoFED internal systems</digi:trn>">
				<digi:trn key="aim:donor">Donor / Project</digi:trn>
				</a></strong>
				</td>
				<td colspan=<bean:write name="aimMulitlateralbyDonorForm" property="yearCount" /> align="center" height="21" ><strong>
				<digi:trn key="aim:year">YEAR</digi:trn>
				</strong> </td>
				<td rowspan="2" colspan="5" align="center" height="21" width="20%">
				<digi:trn key="aim:total">Total</digi:trn>
				</td>
			</tr>
			<tr bgcolor="#f4f4f2">
				<logic:iterate name="aimMulitlateralbyDonorForm"  property="fiscalYearRange" id="fiscalYearRange">
					<td colspan=4 align="center" height="21" width="77">
						<strong><%=fiscalYearRange%></strong>
					</td>
				</logic:iterate>
			</tr>
			<tr bgcolor="#F4F4F2">
			<logic:iterate name="aimMulitlateralbyDonorForm"  property="fiscalYearRange" id="fiscalYearRange">
				<td align="center" height="21" width="7%"> 
				<a title="<digi:trn key="aim:PlannedDisbursementofFund">Actual Commitment</digi:trn>">
				<digi:trn key="aim:commitments">Commitment</digi:trn>
				</a>
				</td>
				<td align="center" height="21" width="77"> 
				<a title="<digi:trn key="aim:PlannedDisbursementofFund">Expected disbursements</digi:trn>">
				<digi:trn key="aim:plannedDisbursements">Planned Disbursement</digi:trn>
				</a>
				</td>
				<td align="center" height="21" width="77"> 
				<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
				<digi:trn key="aim:disbursements">Disbursements</digi:trn>
				</a>
				</td>
				<td align="center" height="21" width="77"> 
				<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
				<digi:trn key="aim:expenditures">Expenditures</digi:trn>
				</a>
				</td>
				
				</logic:iterate>
				<td align="center" height="21" width="7%"> 
				<a title="<digi:trn key="aim:PlannedDisbursementofFund">Actual Commitment</digi:trn>">
				<digi:trn key="aim:commitments">Commitment</digi:trn>
				</a>
				</td>
				<td align="center" height="21" width="77">
				<a title="<digi:trn key="aim:PlannedDisbursementofFund">Expected disbursements</digi:trn>">
				<digi:trn key="aim:plannedDisbursements">Planned Disbursements</digi:trn>
				</a>
				</td>
				<td align="center" height="21" width="77">
				<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
				<digi:trn key="aim:disbursements">Disbursements</digi:trn>
				</a>
				</td>
				<td align="center" height="21" width="77"> 
				<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
				<digi:trn key="aim:expenditures">Expenditures</digi:trn>
				</a>
				</td>
				<td align="center" height="21" width="77"> 
				<a title="<digi:trn key="aim:undisbursedAmount">Undisbursed Amount</digi:trn>">
				<digi:trn key="aim:undisbursed">Undisbursed</digi:trn>
				</a>
				</td>
				</tr>
			
		
			<logic:iterate name="multiReport"  property="donors" id="donors" type="org.digijava.module.aim.helper.AmpTeamDonors">				
				<tr bgcolor="#FFFFFF">
				<td align="center"><bean:write name="donors" property="donorCount"/></td>
					<td colspan=<bean:write name="aimMulitlateralbyDonorForm" property="totalColumns" /> height="21">
					<!--<td align="center" height="21" width="77">1</td>-->
						<strong><bean:write name="donors" property="donorAgency"/></strong>
					</td>
				</tr>
				<logic:iterate name="donors"  property="project" id="project" type="org.digijava.module.aim.helper.Project">
					<tr bgcolor="#FFFFFF">
						<!--<td align="center" height="21" width="77">1.1</td>-->
						<td align="center" rowspan=<bean:write name="project" property="rowspan"/>><bean:write name="donors" property="donorCount"/>.<bean:write name="project" property="count"/></td>
						<td align="left" height="21" width="77"> 
								<bean:write name="project" property="name"/>
							</td>
							<logic:iterate name="project"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
							<td align="right" height="21" > 
								<logic:notEqual name="ampFund" property="commAmount" value="0">
									<bean:write name="ampFund" property="commAmount" />
								</logic:notEqual>
							</td>
							<td align="right" height="21" > 
								<logic:notEqual name="ampFund" property="plannedDisbAmount" value="0">
									<bean:write name="ampFund" property="plannedDisbAmount" />
								</logic:notEqual>
							</td>
							<td align="right" height="21" > 
								<logic:notEqual name="ampFund" property="disbAmount"  value="0">
									<bean:write name="ampFund" property="disbAmount" />
								</logic:notEqual>
							</td>
							<td align="right" height="21" > 
							<logic:notEqual name="ampFund" property="expAmount" value="0">
								<bean:write name="ampFund" property="expAmount" />
							</logic:notEqual>
							</td>
						</logic:iterate>
						<td align="right" height="21"> 
							<logic:notEqual value="0" name="project" property="projCommAmount">
								<bean:write name="project" property="projCommAmount"/>
							</logic:notEqual>
						</td>
						<td align="right" height="21"> 
							<logic:notEqual value="0" name="project" property="projPlannedDisbAmount">
								<bean:write name="project" property="projPlannedDisbAmount"/>
							</logic:notEqual>
						</td>
						<td align="right" height="21"> 
							<logic:notEqual name="project" property="projDisbAmount" value="0">
								<bean:write name="project" property="projDisbAmount"/>
							</logic:notEqual>
						</td>
						<td align="right" height="21"> 
							<logic:notEqual name="project" property="projExpAmount" value="0">
								<bean:write name="project" property="projExpAmount"/>
							</logic:notEqual>
						</td>
						<td align="right" height="21"> 
							<logic:notEqual name="project" property="projUnDisbAmount" value="0">
								<bean:write name="project" property="projUnDisbAmount"/>
							</logic:notEqual>
						</td>
					</tr>
				
				<logic:notEmpty name="project"  property="termAssist">
				<logic:iterate name="project"  property="termAssist" id="termAssist" type="org.digijava.module.aim.helper.ProjectTermAssist">
					<tr bgcolor="white">
						<td align="left" height="21" > 
							<bean:write name="termAssist" property="termAssistName"/>
						</td>
						<logic:iterate name="termAssist"  property="termAssistFund" id="termAssistFund" type="org.digijava.module.aim.helper.AmpFund">
						<td align="right" height="21" >
							<logic:notEqual name="termAssistFund" property="commAmount" value="0">
							<bean:write name="termAssistFund" property="commAmount" />
							</logic:notEqual>
						</td>
						<td align="right" height="21" >
							<logic:notEqual name="termAssistFund" property="plannedDisbAmount" value="0">
							<bean:write name="termAssistFund" property="plannedDisbAmount" />
							</logic:notEqual>
						</td>
						<td align="right" height="21"> 
							<logic:notEqual name="termAssistFund" property="disbAmount" value="0">
							<bean:write name="termAssistFund" property="disbAmount" />
							</logic:notEqual>
						</td>
						<td align="right" height="21" > 
							<logic:notEqual name="termAssistFund" property="expAmount" value="0">
							<bean:write name="termAssistFund" property="expAmount" />
							</logic:notEqual>
						</td>
				</logic:iterate>
				<td align="right" height="21"> 
					<logic:notEqual name="termAssist" property="termCommAmount" value="0">
					<bean:write name="termAssist" property="termCommAmount"/>
					</logic:notEqual>
				</td>
				<td align="right" height="21"> 
					<logic:notEqual name="termAssist" property="termPlannedDisbAmount" value="0">
					<bean:write name="termAssist" property="termPlannedDisbAmount"/>
					</logic:notEqual>
				</td>
				<td align="right" height="21"> 
					<logic:notEqual name="termAssist" property="termDisbAmount" value="0">
					<bean:write name="termAssist" property="termDisbAmount"/>
					</logic:notEqual>
				</td>
				<td align="right" height="21"> 
					<logic:notEqual name="termAssist" property="termExpAmount" value="0">
					<bean:write name="termAssist" property="termExpAmount"/>
					</logic:notEqual>
				</td>
				<td align="right" height="21"> 
					<logic:notEqual name="termAssist" property="termUnDisbAmount" value="0">
					<bean:write name="termAssist" property="termUnDisbAmount"/>
					</logic:notEqual>
				</td>
			</tr>
		</logic:iterate>
		</logic:notEmpty>
		</logic:iterate>
		</logic:iterate>
		
		<logic:notEmpty name="multiReport"  property="totalSectorTermAssistFund">
		<logic:iterate name="multiReport"  property="totalSectorTermAssistFund" id="totalSectorTermAssistFund" type="org.digijava.module.aim.helper.TermFund">
		<tr bgcolor="#C9C9C7"><td>&nbsp;</td>
			<td align="left" height="21">Total <bean:write name="totalSectorTermAssistFund" property="termAssistName" /></td>
			<logic:iterate name="totalSectorTermAssistFund"  property="termFundTotal" id="termFundTotal" type="org.digijava.module.aim.helper.TermFundTotal">
				<td align="right" height="21"> 
					<logic:notEqual name="termFundTotal" property="totCommAmount" value="0">
					<br><bean:write name="termFundTotal" property="totCommAmount" />
					</logic:notEqual>
				</td>
				<td align="right" height="21"> 
					<logic:notEqual name="termFundTotal" property="totPlannedDisbAmount" value="0">
					<br><bean:write name="termFundTotal" property="totPlannedDisbAmount" />
					</logic:notEqual>
				</td>
				<td align="right" height="21">
					<logic:notEqual name="termFundTotal" property="totDisbAmount" value="0">
					<br><bean:write name="termFundTotal" property="totDisbAmount" />
					</logic:notEqual>
				</td>
				<td align="right" height="21"> 
					<logic:notEqual name="termFundTotal" property="totExpAmount" value="0">
					<br><bean:write name="termFundTotal" property="totExpAmount" />
					</logic:notEqual>
				</td>
			</logic:iterate>
			<td align="right" height="21"> 
				<br>
				<logic:notEqual name="totalSectorTermAssistFund" property="totDonorCommAmount" value="0">
				<bean:write name="totalSectorTermAssistFund" property="totDonorCommAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"> 
				<br>
				<logic:notEqual name="totalSectorTermAssistFund" property="totDonorPlannedDisbAmount" value="0">
				<bean:write name="totalSectorTermAssistFund" property="totDonorPlannedDisbAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"><br> 
				<logic:notEqual name="totalSectorTermAssistFund" property="totDonorDisbAmount" value="0">
				<bean:write name="totalSectorTermAssistFund" property="totDonorDisbAmount"/>
				</logic:notEqual>
				</td>
			<td align="right" height="21"><br> 
				<logic:notEqual name="totalSectorTermAssistFund" property="totDonorExpAmount" value="0">
				<bean:write name="totalSectorTermAssistFund" property="totDonorExpAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"><br> 
				<logic:notEqual name="totalSectorTermAssistFund" property="totDonorUnDisbAmount" value="0">
				<bean:write name="totalSectorTermAssistFund" property="totDonorUnDisbAmount"/>
				</logic:notEqual>
				</td>
		</tr>
		</logic:iterate>
		</logic:notEmpty>

		<tr bgcolor="#C9C9C7"><td>&nbsp;</td>
			<td align="left" height="21" width="77"><strong>
			<digi:trn key="aim:totalFor">Total for </digi:trn>
			<bean:write name="multiReport" property="sector" /></strong></td>
			<logic:iterate name="multiReport"  property="totalSectorFund" id="totalSectorFund" type="org.digijava.module.aim.helper.FundTotal">
			<td align="right" height="21">
					<logic:notEqual name="totalSectorFund" property="totCommAmount" value="0">
					<br><bean:write name="totalSectorFund" property="totCommAmount" /></td>
					</logic:notEqual>
				<td align="right" height="21">
					<logic:notEqual name="totalSectorFund" property="totPlannedDisbAmount" value="0">
					<br><bean:write name="totalSectorFund" property="totPlannedDisbAmount" /></td>
					</logic:notEqual>
				<td align="right" height="21">
					<logic:notEqual name="totalSectorFund" property="totDisbAmount" value="0">
					<br><bean:write name="totalSectorFund" property="totDisbAmount" />
					</logic:notEqual>
				</td>
				<td align="right" height="21" > 
					<logic:notEqual name="totalSectorFund" property="totExpAmount" value="0">
					<br><bean:write name="totalSectorFund" property="totExpAmount" />
					</logic:notEqual>
				</td>
			</logic:iterate>
			<td align="right" height="21"> 
				<logic:notEqual name="multiReport" property="sectorCommAmount" value="0">			
				<br><bean:write name="multiReport" property="sectorCommAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"> 
				<logic:notEqual name="multiReport" property="sectorPlannedDisbAmount" value="0">			
				<br><bean:write name="multiReport" property="sectorPlannedDisbAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"><br> 
				<logic:notEqual name="multiReport" property="sectorDisbAmount" value="0">			
				<bean:write name="multiReport" property="sectorDisbAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"> <br>
				<logic:notEqual name="multiReport" property="sectorExpAmount" value="0">			
				<bean:write name="multiReport" property="sectorExpAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"><br> 
				<logic:notEqual name="multiReport" property="sectorUnDisbAmount" value="0">			
				<bean:write name="multiReport" property="sectorUnDisbAmount"/>
				</logic:notEqual>
			</td>
		</tr>

		<logic:notEmpty name="multiReport"  property="totalTeamTermAssistFund">
		<logic:iterate name="multiReport"  property="totalTeamTermAssistFund" id="totalTeamTermAssistFund" type="org.digijava.module.aim.helper.TermFund">
		<tr bgcolor="#F4F4F2">
			<td align="left" height="21" colspan="2">Total <bean:write name="totalTeamTermAssistFund" property="termAssistName" /></td>
			<logic:iterate name="totalTeamTermAssistFund"  property="termFundTotal" id="termFundTotal" type="org.digijava.module.aim.helper.TermFundTotal">
				<td align="right" height="21"> 
					<logic:notEqual name="termFundTotal" property="totCommAmount" value="0">
					<br><bean:write name="termFundTotal" property="totCommAmount" />
					</logic:notEqual>
				</td>
				<td align="right" height="21"> 
					<logic:notEqual name="termFundTotal" property="totPlannedDisbAmount" value="0">
					<br><bean:write name="termFundTotal" property="totPlannedDisbAmount" />
					</logic:notEqual>
				</td>
				<td align="right" height="21">
					<logic:notEqual name="termFundTotal" property="totDisbAmount" value="0">
					<br><bean:write name="termFundTotal" property="totDisbAmount" />
					</logic:notEqual>
				</td>
				<td align="right" height="21"> 
					<logic:notEqual name="termFundTotal" property="totExpAmount" value="0">
					<br><bean:write name="termFundTotal" property="totExpAmount" />
					</logic:notEqual>
				</td>
			</logic:iterate>
			<td align="right" height="21"> 
				<br>
				<logic:notEqual name="totalTeamTermAssistFund" property="totDonorCommAmount" value="0">
				<bean:write name="totalTeamTermAssistFund" property="totDonorCommAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"> 
				<br>
				<logic:notEqual name="totalTeamTermAssistFund" property="totDonorPlannedDisbAmount" value="0">
				<bean:write name="totalTeamTermAssistFund" property="totDonorPlannedDisbAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"><br> 
				<logic:notEqual name="totalTeamTermAssistFund" property="totDonorDisbAmount" value="0">
				<bean:write name="totalTeamTermAssistFund" property="totDonorDisbAmount"/>
				</logic:notEqual>
				</td>
			<td align="right" height="21"><br> 
				<logic:notEqual name="totalTeamTermAssistFund" property="totDonorExpAmount" value="0">
				<bean:write name="totalTeamTermAssistFund" property="totDonorExpAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"><br> 
				<logic:notEqual name="totalTeamTermAssistFund" property="totDonorUnDisbAmount" value="0">
				<bean:write name="totalTeamTermAssistFund" property="totDonorUnDisbAmount"/>
				</logic:notEqual>
				</td>
		</tr>
		</logic:iterate>
		</logic:notEmpty>

		<logic:notEmpty name="multiReport"  property="totalTeamFund">
		<tr bgcolor="#F4F4F2"><td align="left" height="21" colspan="2"><strong>
			<digi:trn key="aim:grandTotal">Grand Total</digi:trn></strong></td>
			<logic:iterate name="multiReport"  property="totalTeamFund" id="totalTeamFund" type="org.digijava.module.aim.helper.FundTotal">
				<td align="right" height="21">
					<logic:notEqual name="totalTeamFund" property="totCommAmount" value="0">
					<br><bean:write name="totalTeamFund" property="totCommAmount" /></td>
					</logic:notEqual>
				<td align="right" height="21">
					<logic:notEqual name="totalTeamFund" property="totPlannedDisbAmount" value="0">
					<br><bean:write name="totalTeamFund" property="totPlannedDisbAmount" /></td>
					</logic:notEqual>
				<td align="right" height="21">
					<logic:notEqual name="totalTeamFund" property="totDisbAmount" value="0">
					<br><bean:write name="totalTeamFund" property="totDisbAmount" />
					</logic:notEqual>
				</td>
				<td align="right" height="21" > 
					<logic:notEqual name="totalTeamFund" property="totExpAmount" value="0">
					<br><bean:write name="totalTeamFund" property="totExpAmount" />
					</logic:notEqual>
				</td>
			</logic:iterate>
			<td align="right" height="21"> 
				<logic:notEqual name="multiReport" property="teamCommAmount" value="0">			
				<br><bean:write name="multiReport" property="teamCommAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"> 
				<logic:notEqual name="multiReport" property="teamPlannedDisbAmount" value="0">			
				<br><bean:write name="multiReport" property="teamPlannedDisbAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"><br> 
				<logic:notEqual name="multiReport" property="teamDisbAmount" value="0">			
				<bean:write name="multiReport" property="teamDisbAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"> <br>
				<logic:notEqual name="multiReport" property="teamExpAmount" value="0">			
				<bean:write name="multiReport" property="teamExpAmount"/>
				</logic:notEqual>
			</td>
			<td align="right" height="21"><br> 
				<logic:notEqual name="multiReport" property="teamUnDisbAmount" value="0">			
				<bean:write name="multiReport" property="teamUnDisbAmount"/>
				</logic:notEqual>
			</td>
		</tr>
		</logic:notEmpty>

		</table>
		</logic:iterate>
		</logic:notEmpty>
		
	
	
	</td>
</tr>
</table>
            </td>
              <td width="2">&nbsp;</td>
            </tr>
      
			         
		  </td>
          <td valign="top" class="r-dotted-lg">&nbsp;</td>
		</tr>

</table>

</digi:form>




