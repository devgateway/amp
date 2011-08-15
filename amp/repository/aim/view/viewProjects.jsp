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

	function clearFilter()
	{
		<digi:context name="clearVal" property="context/module/moduleinstance/viewProjects.do" />
		document.aimCommitmentbyDonorForm.action = "<%= clearVal %>";
		document.aimCommitmentbyDonorForm.target = "_self";
		document.aimCommitmentbyDonorForm.submit();
	}

	function popup_pdf() {
		openResisableWindow(800, 600);
		<digi:context name="pdf" property="context/module/moduleinstance/viewProjectPdf.do" />
		document.aimCommitmentbyDonorForm.action = "<%= pdf %>";
		document.aimCommitmentbyDonorForm.target = popupPointer.name;
		document.aimCommitmentbyDonorForm.submit();
	}

	/* CSV function start  */

		function popup_csv() {
		openResisableWindow(800, 600);
		<digi:context name="csv" property="context/module/moduleinstance/viewProjectXls.do?docType=csv" />
		document.aimCommitmentbyDonorForm.action = "<%= csv %>";
		document.aimCommitmentbyDonorForm.target = popupPointer.name;
		document.aimCommitmentbyDonorForm.submit();
	}
	/* CSV function end  */
	/* this is a comment added*/

	function popup_xls() {
		openResisableWindow(800, 600);
		<digi:context name="xls" property="context/module/moduleinstance/viewProjectXls.do?docType=xls" />
		document.aimCommitmentbyDonorForm.action = "<%= xls %>";
		document.aimCommitmentbyDonorForm.target = popupPointer.name;
		document.aimCommitmentbyDonorForm.submit();
	}

	function popup_warn() {
		alert("Year Range selected should NOT be Greater than 4 Years.");
	}

-->
</script>

<digi:errors/>
<digi:instance property="aimCommitmentbyDonorForm" />

<jsp:include page="teamPagesHeader.jsp"  />

<table width="100%" border="0" cellpadding="10" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
          <td width="750" align="left" valign="top" >
		
		  <table width="100%"  border="0" cellpadding="5" cellspacing="0">
            
			<tr>
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
				<digi:trn key="aim:AllReport">All Reports</digi:trn>
				</digi:link>&nbsp;&gt;&nbsp;<bean:write name="aimCommitmentbyDonorForm" property="perspective"/> &nbsp;
				<digi:trn key="aim:perspective">perspective</digi:trn></td>
                <td width="2">&nbsp;</td>
             </tr>
	<tr>
				<td colspan=3 class=subtitle-blue align="center">
					<digi:trn key="aim:ViewProjectsTitle">
					<bean:write name="aimCommitmentbyDonorForm" property="reportName" />
					</digi:trn>
				</td>
			</tr>
			<tr>
				<td colspan=3 class=box-title align="center">
					<bean:write name="aimCommitmentbyDonorForm" property="workspaceType" />&nbsp; <bean:write name="aimCommitmentbyDonorForm" property="workspaceName" />&nbsp; 
			<!--		<digi:trn key="aim:team">Team</digi:trn>		-->
				</td>
			</tr>

			<logic:notEmpty name="aimCommitmentbyDonorForm"  property="report">
				<tr>
					<td valign="bottom" class="crumb">
					&nbsp;&nbsp;<img src="../ampTemplate/images/print_icon.gif">
					<digi:link href="/htmlViewProjects.do" target="_blank">
						Print
					</digi:link>
				</td>
				</tr>
			</logic:notEmpty>			


<!--  PDF/XLS Links -->	
<logic:notEmpty name="aimCommitmentbyDonorForm"  property="report">
		<tr>	
				<td valign="bottom" class="crumb">
					&nbsp;&nbsp;<img src="../ampTemplate/images/pdf_icon.gif" border="0">
					<c:set var="translation">
						<digi:trn key="aim:clickToCreateReportInPDF">Click here to Create Report in Pdf </digi:trn>
					</c:set>
					<digi:link href="" onclick="popup_pdf(''); return false;" title="${translation}"> 
						<digi:trn key="aim:createReportInPdf">Create Report in Pdf.</digi:trn>
					</digi:link>
                </td>
           </tr>
			<tr>
				<td valign="bottom" class="crumb" >
				&nbsp;&nbsp;<img src="../ampTemplate/images/xls_icon.jpg" border="0">
				<c:set var="translation">
					<digi:trn key="aim:clickToCreateReportInExcel">Click here to Create Report in Excel </digi:trn>
				</c:set>
				<digi:link href="" onclick="popup_xls(''); return false;" title="${translation}">
					 <digi:trn key="aim:createReportInXls">Create Report in Xls.</digi:trn>
				</digi:link>
                </td>
       </tr>

			<!-- csv link -->
			<tr>
				
			        <td valign="bottom" class="crumb" >
					&nbsp;&nbsp;<img src="../ampTemplate/images/icon_csv.gif" border="0">
					<c:set var="translation">
						<digi:trn key="aim:clickToCreateReportInCVS">Click here to Create Report in CSV </digi:trn>
					</c:set>
					<digi:link href="" onclick="popup_csv(''); return false;" title="${translation}">
					 	<digi:trn key="aim:createReportInCsv">Create Report in CSV.</digi:trn>
					</digi:link>

			
					</td>
            </tr>
</logic:notEmpty>
<!--  PDF/XLS Links -->	

			
<digi:form action="/viewProjects.do" >			
			<tr><td width="9">
			</td>
        

  <bean:define id="fcount" name="aimCommitmentbyDonorForm" property="filterCnt" type="java.lang.Integer" /> 
<%
	int fcnt = fcount.intValue();
	System.out.println("FC = " +fcnt);
	
%>              
            <table  border="0" cellspacing="0" cellpadding="2">
			<TR>
			<logic:greaterThan name="aimCommitmentbyDonorForm" property="filterCnt" value="0" >
			<TD bgColor="black" colspan=<%= fcnt+1%> ></TD>
			</logic:greaterThan>
			</TR>

			<tr bgcolor="#c0c0c0" height=30>	
			
			<logic:equal name="aimCommitmentbyDonorForm" property="filterFlag" value="true">
						<td>			
						<html:select styleClass="dr-menu" property="perspectiveFilter">
								<html:option value="DN">Donor View</html:option>
								<html:option value="MA"><digi:trn key="aim:MOFED">Mofed</digi:trn></html:option>
						</html:select>
						</td>
						</logic:equal>

						<logic:notEmpty name="aimCommitmentbyDonorForm" property="currencyColl">
						<td>						
							<html:select property="ampCurrencyCode" styleClass="dr-menu" >
								<html:optionsCollection name="aimCommitmentbyDonorForm" property="currencyColl" value="currencyCode" label="currencyName"/> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimCommitmentbyDonorForm" property="fiscalYears">
						<td>	 					
							<html:select property="fiscalCalId" styleClass="dr-menu">
								<html:optionsCollection name="aimCommitmentbyDonorForm" property="fiscalYears" value="ampFiscalCalId" label="name"/> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimCommitmentbyDonorForm" property="ampFromYears">
						<td>	 					
							<html:select property="ampFromYear" styleClass="dr-menu">
								<html:options name="aimCommitmentbyDonorForm" property="ampFromYears" /> 
							</html:select> 
						</td>
	 					</logic:notEmpty>

						<logic:notEmpty name="aimCommitmentbyDonorForm" property="ampToYears">
						<td>	 					
							<html:select property="ampToYear" styleClass="dr-menu">
								<html:options name="aimCommitmentbyDonorForm" property="ampToYears" /> 
							</html:select> 
						</td>
	 					</logic:notEmpty>

						<logic:notEmpty name="aimCommitmentbyDonorForm" property="regionColl">
						<td>						
							<html:select property="ampLocationId" styleClass="dr-menu" >
								<option value="All">All Regions</option>
									<html:optionsCollection name="aimCommitmentbyDonorForm" property="regionColl" value="name" 
									label="name"  /> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimCommitmentbyDonorForm" property="modalityColl">
						<td>						
							<html:select property="ampModalityId" styleClass="dr-menu" >
								<option value="0">All Financing Instruments</option>
								<html:optionsCollection name="aimCommitmentbyDonorForm" property="modalityColl" value="ampModalityId" label="name" /> 
							</html:select>
						</td>
		 				</logic:notEmpty>

						<logic:notEmpty name="aimCommitmentbyDonorForm" property="donorColl">
						<td>						
							<html:select property="ampOrgId" styleClass="dr-menu" >
								<option value="0">All Donors</option>
								<html:optionsCollection name="aimCommitmentbyDonorForm" property="donorColl" value="ampOrgId" label="acronym"/> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimCommitmentbyDonorForm" property="statusColl">
						<td>						
							<html:select property="ampStatusId" styleClass="dr-menu" >
								<option value="0">All Status</option>
								<html:optionsCollection name="aimCommitmentbyDonorForm" property="statusColl" value="ampStatusId" label="name"  /> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimCommitmentbyDonorForm" property="sectorColl">
						<td>						
							<html:select property="ampSectorId" styleClass="dr-menu" >
								<option value="0">All Sectors</option>
								<html:optionsCollection name="aimCommitmentbyDonorForm" property="sectorColl" value="ampSectorId" label="name" /> 
							</html:select>
						<td>
						</logic:notEmpty>

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

						<logic:equal name="aimCommitmentbyDonorForm" property="goFlag" value="true">
						<td>						
						<input type="button" name="GoButton" value=" GO " class="dr-menu" onclick="clearFilter()">
						</td>						
						</logic:equal>
                </tr>
            </table>
          </td>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
            </tr>
			
	<!--		<tr>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
			<td width="2" valign="middle">&nbsp;</td>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
            </tr>					-->
			
			<tr>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
              <td >
			  <table width="100%"  border="0" cellpadding="0" cellspacing="0">
            <tr bgcolor="#FFFFFF">
              <td colspan="10"><table border="0" cellspacing="0" cellpadding="0">
                  <tr bgcolor="#C9C9C7">
                    <td bgcolor="#C9C9C7" class="box-title">
					<digi:trn key="aim:ViewProjects">
					Commitments, Disbursements, & Pipeline Projects
					</digi:trn>
					</td>
                    <td bgcolor="#FFFFFF"><img src="../ampTemplate/images/corner-r.gif" width="17" height="17"></td>
                  </tr>
              </table></td>
            </tr>
            <tr bgcolor="#FFFFFF">
              <td valign="top" colspan="10"><table width="100%"  border="0" cellpadding="1" cellspacing="1" bgcolor="#C6C7C4">
                    <tr bgcolor="#F4F4F2">
					<td align="center" height="21" width="42" ><div align="center"><strong>
					<a title="<digi:trn key="aim:DonorName">The country or agency that financed the project</digi:trn>">
					<digi:trn key="aim:donor">Donor</digi:trn>
					</a></strong></div>
					</td>
					<td align="center" height="21" width="28"><div align="center"><strong> 
					<a title="<digi:trn key="aim:NameofProject">Title used in donors or MoFED internal systems</digi:trn>">
					<digi:trn key="aim:projectName">Project Name</digi:trn>
					</a></strong></div>
					</td>
					<td align="center" height="21" width="43"><div align="center"> <strong>
					<a title="<digi:trn key="aim:SectorSpecific">Specific area the the project is intended to foster</digi:trn>">
					<digi:trn key="aim:sector">Sector</digi:trn>
					</a></strong></div>
					</td>
					<td align="center" height="21" width="35"> <div align="center"><strong>
					<a title="<digi:trn key="aim:DateofStart">Date (dd/mm/yy) when the project commenced (effective start date)</digi:trn>">
					<digi:trn key="aim:startDate">Start Date</digi:trn>
					</a></strong>
					</td>
					<td align="center" height="21" width="39"> <div align="center"><strong>
					<a title="<digi:trn key="aim:DateofClose">Date (dd/mm/yy) in which the project ended or is expected to end</digi:trn>">
					<digi:trn key="aim:closeDate">Close Date</digi:trn>
					</a></strong></div>
					</td>
					<td align="center" height="21" width="40"> <div align="center"><strong>
					<a title="<digi:trn key="aim:LevelImplemented">Federal and regional programs are the scope of a project. They are a classification of the sponsorship of the project or program. This works in conjunction with location</digi:trn>">
					<digi:trn key="aim:implementationLevel">Implementation Level </digi:trn>
					</a></strong></div>
					</td>
					<td align="center" height="21" width="45"><div align="center"> <strong>
					<a title="<digi:trn key="aim:RegionofImplementation">Region where project is implemented</digi:trn>">
					<digi:trn key="aim:region">Region</digi:trn>
					</a></strong></div>
					</td>
					<td align="center" height="21" width="40"> <div align="center"><strong>
					<a title="<digi:trn key="aim:StatusoftheProject">Current stage of project</digi:trn>">
					<digi:trn key="aim:status">Status </digi:trn>
					</a></strong></div>
					</td>
					<td align="center" height="21" width="200"><div align="center"> <strong>
					<a title="<digi:trn key="aim:TypeoftheAssist">Type of flow used to finance the project: grant, loan or in kind</digi:trn>">
					<digi:trn key="aim:thetypeOfAssistance"> Type of Assistance </digi:trn>
					</a></strong></div>
					</td>
					<td align="center" height="21" width="200"> <div align="center"><strong>
					<a title="<digi:trn key="aim:CommitmentinTotal">Total Commitment Amount of Activity</digi:trn>">
					<digi:trn key="aim:totalCommitments">Total Commitment</digi:trn>
					</a></strong></div>
					</td>
					<td align="center" height="21" width="200" ><div align="center"><strong>
					<a title="<digi:trn key="aim:TotalAmtDisbursedToDate">Total Amount Disbursed as of today's date</digi:trn>">
					<digi:trn key="aim:totalDisbursed">Total Disbursed To Date</digi:trn>
					</a></strong></div>
					</td>
					<td align="center" height="21" width="200" ><div align="center"><strong>
					<a title="<digi:trn key="aim:UndisbursedFundshere">Remaining balance of funds to be disbursed</digi:trn>">
					<digi:trn key="aim:unDisbursedhere">Undisbursed </digi:trn>
					</a></strong></div>
					</td>
					<td colspan="4" align="center" height="21" width="240" ><div align="center"><strong>
					<a title="<digi:trn key="aim:PlannedDisbursementofFund">Expected disbursements</digi:trn>">
					<digi:trn key="aim:plannedDisbursements">Planned Disbursed </digi:trn>
					</a></strong></div>
					</td>

				</tr>
	
				<logic:empty name="aimCommitmentbyDonorForm" property="report"> 
					<tr bgcolor="#F4F4F2">
						<td colspan=<bean:write name="aimCommitmentbyDonorForm" property="totalColumns"/> align="center" height="21" >
							<b><digi:trn key="aim:noRecords">No Records</digi:trn></b>
						</td>
					</tr>
				</logic:empty>
		
		<logic:notEmpty name="aimCommitmentbyDonorForm"  property="report">
				<tr bgcolor="#FFFFFF">
				<td align="center" height="21" width="42"></td>
				<td align="center" height="21" width="28"></td>
				<td align="center" height="21" width="43"></td>
				<td align="center" height="21" width="60"></td>
				<td align="center" height="21" width="40"></td>
				<td align="center" height="21" width="45"></td>
				<td align="center" height="21" width="35"></td>
				<td align="center" height="21" width="39"></td>
				<td align="center" height="21" width="200"></td>
				<td align="center" height="21" width="200"></td>
				<td align="center" height="21" width="200"></td>
				<td align="center" height="21" width="200"></td>
				<logic:iterate name="aimCommitmentbyDonorForm"  property="forecastYear" id="forecastYear">		
				<td align="center" height="21" width="60">
				<div align="center"><strong><%=forecastYear%></strong></div>
				</td>
				</logic:iterate>
			</tr>
		
		<logic:iterate name="aimCommitmentbyDonorForm"  property="report" id="report" type="org.digijava.module.aim.helper.Report">
			<tr bgcolor="#F4F4F2">
				<td align="left" height="21" width="42" rowspan="4">
					<logic:empty name="report" property="donors">&nbsp;
					</logic:empty>
					<logic:notEmpty name="report" property="donors">
					<logic:iterate name="report" id="donors" property="donors"> <%=donors%>	
					<br>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="left" height="21" width="28" rowspan="4">
					<bean:write name="report" property="title" /> 
				</td>
				<td align="left" height="21" width="43" rowspan="4">
					<logic:empty name="report" property="sectors">&nbsp;
					</logic:empty>
					<logic:notEmpty name="report" property="sectors">
					<logic:iterate name="report" id="sectors" property="sectors"> <%=sectors%>	
					<br>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="left" height="21" width="35" rowspan="4">
					<bean:write name="report" property="startDate" />
				</td>
				<td align="left" height="21" width="39" rowspan="4">
					<bean:write name="report" property="closeDate" />
				</td>
				<td align="left" height="21" width="40" rowspan="4">
					<bean:write name="report" property="level" />
				</td>
				<td align="left" height="21" width="45" rowspan="4">
					<logic:empty name="report" property="regions">&nbsp;
					</logic:empty>
					<logic:notEmpty name="report" property="regions">
					<logic:iterate name="report" id="regions" property="regions"> <%=regions%>	
					<br>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="left" height="21" width="40" rowspan="4">
					<bean:write name="report" property="status" />
				</td>
				<td><b>Grant</b></td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTypeComm">
						<logic:iterate name="ampFund" property="byTypeComm" id="terms">
							<c:if test='${terms.fundingTerms == "Grant"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTypeDisb">
						<logic:iterate name="ampFund" property="byTypeDisb" id="terms">
							<c:if test='${terms.fundingTerms == "Grant"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTypeUnDisb">
						<logic:iterate name="ampFund" property="byTypeUnDisb" id="terms">
							<c:if test='${terms.fundingTerms == "Grant"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTermsPlDisbForecast1">
						<logic:iterate name="ampFund" property="byTermsPlDisbForecast1" id="terms">
							<c:if test='${terms.fundingTerms == "Grant"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTermsPlDisbForecast2">
						<logic:iterate name="ampFund" property="byTermsPlDisbForecast2" id="terms">
							<c:if test='${terms.fundingTerms == "Grant"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTermsPlDisbForecast3">
						<logic:iterate name="ampFund" property="byTermsPlDisbForecast3" id="terms">
							<c:if test='${terms.fundingTerms == "Grant"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTermsPlDisbForecast4">
						<logic:iterate name="ampFund" property="byTermsPlDisbForecast4" id="terms">
							<c:if test='${terms.fundingTerms == "Grant"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
			</tr>
			<tr bgcolor="#F4F4F2">
				<td><b>Loan</b></td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTypeComm">
						<logic:iterate name="ampFund" property="byTypeComm" id="terms">
							<c:if test='${terms.fundingTerms == "Loan"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTypeDisb">
						<logic:iterate name="ampFund" property="byTypeDisb" id="terms">
							<c:if test='${terms.fundingTerms == "Loan"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTypeUnDisb">
						<logic:iterate name="ampFund" property="byTypeUnDisb" id="terms">
							<c:if test='${terms.fundingTerms == "Loan"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTermsPlDisbForecast1">
						<logic:iterate name="ampFund" property="byTermsPlDisbForecast1" id="terms">
							<c:if test='${terms.fundingTerms == "Loan"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTermsPlDisbForecast2">
						<logic:iterate name="ampFund" property="byTermsPlDisbForecast2" id="terms">
							<c:if test='${terms.fundingTerms == "Loan"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTermsPlDisbForecast3">
						<logic:iterate name="ampFund" property="byTermsPlDisbForecast3" id="terms">
							<c:if test='${terms.fundingTerms == "Loan"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTermsPlDisbForecast4">
						<logic:iterate name="ampFund" property="byTermsPlDisbForecast4" id="terms">
							<c:if test='${terms.fundingTerms == "Loan"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
			</tr>
			<tr bgcolor="#F4F4F2">
				<td><b>In Kind</b></td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTypeComm">
						<logic:iterate name="ampFund" property="byTypeComm" id="terms">
							<c:if test='${terms.fundingTerms == "In Kind"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTypeDisb">
						<logic:iterate name="ampFund" property="byTypeDisb" id="terms">
							<c:if test='${terms.fundingTerms == "In Kind"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTypeUnDisb">
						<logic:iterate name="ampFund" property="byTypeUnDisb" id="terms">
							<c:if test='${terms.fundingTerms == "In Kind"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTermsPlDisbForecast1">
						<logic:iterate name="ampFund" property="byTermsPlDisbForecast1" id="terms">
							<c:if test='${terms.fundingTerms == "In Kind"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTermsPlDisbForecast2">
						<logic:iterate name="ampFund" property="byTermsPlDisbForecast2" id="terms">
							<c:if test='${terms.fundingTerms == "In Kind"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTermsPlDisbForecast3">
						<logic:iterate name="ampFund" property="byTermsPlDisbForecast3" id="terms">
							<c:if test='${terms.fundingTerms == "In Kind"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<td align="right">
					<logic:notEmpty name="report" property="ampFund">
					<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
						<logic:notEmpty name="ampFund" property="byTermsPlDisbForecast4">
						<logic:iterate name="ampFund" property="byTermsPlDisbForecast4" id="terms">
							<c:if test='${terms.fundingTerms == "In Kind"}'>
								<bean:write name="terms"/>
							</c:if>
						</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					</logic:notEmpty>
				</td>
			</tr>
			<tr bgcolor="#F4F4F2">
				<td align="left" height="21" width="40"><b>
					Total</b>
				</td>
				<td align="right" height="21">
					<logic:notEqual name="report" property="acCommitment" value="0">
						<bean:write name="report" property="acCommitment" />
					</logic:notEqual>
				</td>
				<td align="right" height="21">
					<logic:notEqual name="report" property="acDisbursement" value="0">
						<bean:write name="report" property="acDisbursement" />
					</logic:notEqual>
				</td>
				<td align="right" height="21">
					<logic:notEqual name="report" property="acUnDisbursement" value="0">
						<bean:write name="report" property="acUnDisbursement" />
					</logic:notEqual>
				</td>
				<% int i = 1; %>
				<logic:iterate name="report"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
				<%
					if(i<5)
			  		{		
				%>
					<td align="right" height="21">
						<logic:notEqual name="ampFund" property="disbAmount" value="0">
							<bean:write name="ampFund" property="disbAmount" />
						</logic:notEqual>
					</td>
				<% 
					}
					i++;
				%>
				</logic:iterate>
			</tr>
		</logic:iterate>

		<tr><td><b>
		<digi:trn key="aim:grandtotal">Grand Total</digi:trn>
		</b></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
		<td align="right">
			<logic:notEqual name="aimCommitmentbyDonorForm" property="totComm" value="0">
			<bean:write name="aimCommitmentbyDonorForm" property="totComm" />
			</logic:notEqual>
		</td>
		<td align="right">
			<logic:notEqual name="aimCommitmentbyDonorForm" property="totDisb" value="0">
			<bean:write name="aimCommitmentbyDonorForm" property="totDisb" />
			</logic:notEqual>
		</td>
		<td align="right">
			<logic:notEqual name="aimCommitmentbyDonorForm" property="totUnDisb" value="0">
			<bean:write name="aimCommitmentbyDonorForm" property="totUnDisb" />
			</logic:notEqual>
		</td>
		<logic:iterate name="aimCommitmentbyDonorForm"  property="totDisbFund" id="totDisbFund" type="org.digijava.module.aim.helper.AmpFund">
				<td align="right" height="21">
					<logic:notEqual name="totDisbFund" property="disbAmount" value="0">
					<bean:write name="totDisbFund" property="disbAmount" />
					</logic:notEqual>
				</td>
			</logic:iterate></tr>
		</logic:notEmpty>
                     </table></td>
              </tr>

          </table>
			  
            </td>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
            </tr>
			
            
			<tr>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
                <td width="100%" valign="middle" ><table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2">
              		<tr bgcolor="#F4F4F2"> 
		                <logic:notEmpty name="aimCommitmentbyDonorForm" property="pages">
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
								<logic:notEqual name="aimCommitmentbyDonorForm" property="page"
								value="1">
								  <c:set var="translation">
										<digi:trn key="aim:clickToViewPreviousPage">Click here to view Previous page</digi:trn>
									</c:set>
									<digi:link href="/viewProjects.do" name="urlParams2" title="${translation}" >
										Previous
									</digi:link>
									&nbsp;
								</logic:notEqual>
								
								<logic:equal name="aimCommitmentbyDonorForm" property="page"
								value="1">
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
										<digi:trn key="aim:clickToViewAllPages">Click here to view All pages</digi:trn>
									</c:set>
									<digi:link href="/viewProjects.do" name="urlParams1" title="${translation}" >
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
									<digi:link href="/viewProjects.do" name="urlParams3" title="${translation}" >
										Next
									</digi:link>
									&nbsp;	
								<% } %>
<!-- ------------------------------------------------------------------------------  -->									
								
							</td>
							</tr>
						</logic:notEmpty>					
	            	</tr>
				<tr bgcolor="#F4F4F2"> 
					<td  bgcolor="#F4F4F2">&nbsp;</td>
	                <td align="right" bgcolor="#F4F4F2">&nbsp;</td>
    	        </tr>
            </table>
			</td>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
            </tr>

		  </td>
		</tr>

</table>
<!--
<table   border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr bgcolor="#484846">
    <td bgcolor="#484846"><img src="../ampTemplate/images/feedback.gif" width="144" height="28"></td>
    
  </tr>
</table>
-->
</digi:form>




