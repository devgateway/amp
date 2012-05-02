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

	function popup_pdf() {
		openResisableWindow(800, 600);
		<digi:context name="pdf" property="context/module/moduleinstance/PlannedProjectPdf.do?docType=pdf" />
		document.aimCommitmentbyDonorForm.action = "<%= pdf %>";
		document.aimCommitmentbyDonorForm.target = popupPointer.name;
		document.aimCommitmentbyDonorForm.submit();
	}

	/* CSV function start  */

		function popup_csv() {
		openResisableWindow(800, 600);
		<digi:context name="csv" property="context/module/moduleinstance/PlannedProjectXls.do?docType=csv" />
		document.aimCommitmentbyDonorForm.action = "<%= csv %>";
		document.aimCommitmentbyDonorForm.target = popupPointer.name;
		document.aimCommitmentbyDonorForm.submit();
	}
	/* CSV function end  */

	function popup_xls() {
		openResisableWindow(800, 600);
		<digi:context name="xls" property="context/module/moduleinstance/PlannedProjectXls.do?docType=xls" />
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

<table width="772" border="0" cellpadding="0" cellspacing="5" bgcolor="#FFFFFF">
<tr>
    
    <td width="750" align="left" valign="top" >
	  <table width="100%"  border="0" cellpadding="5" cellspacing="0">
 
			<tr>
	          <td width="14" class="r-dotted-lg" align="center">&nbsp;</td>
              <td valign="bottom" class="crumb" >
						<c:set var="translation">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</c:set>
                <digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}">
               	<digi:trn key="aim:MyDesktop">My Desktop</digi:trn>
                </digi:link> &gt; 
				<digi:link href="/viewTeamReports.do" styleClass="comment">
				<digi:trn key="aim:AllReports">All Reports</digi:trn>
				</digi:link>&nbsp;&gt;&nbsp;
				<bean:write name="aimCommitmentbyDonorForm" property="perspective"/>&nbsp;
				<digi:trn key="aim:perspective">perspective</digi:trn></td>
	          <td width="14" class="r-dotted-lg" align="center">&nbsp;</td>

             </tr>
		<tr>
				<td colspan=3 class=subtitle-blue align="center">
				<digi:trn key="aim:ViewTitleforPlannedProjects">	
					<bean:write name="aimCommitmentbyDonorForm" property="reportName" />
				</digi:trn>
				</td>
			</tr>
			<tr>
				<td colspan=3 class=box-title align="center">
					<bean:write name="aimCommitmentbyDonorForm" property="workspaceType" />&nbsp; <bean:write name="aimCommitmentbyDonorForm" property="workspaceName" />&nbsp; 
				<!--	<digi:trn key="aim:team">Team</digi:trn>	-->
				</td>
			</tr>
			
			<logic:notEmpty name="aimCommitmentbyDonorForm" property="report">
				<tr>
				<td width="14" class="r-dotted-lg">&nbsp;</td>
				<td valign="bottom" class="crumb">
					&nbsp;&nbsp;<img src="../ampTemplate/images/print_icon.gif">
					<digi:link href="/htmlViewPlannedProjects.do" target="_blank">
						Print
					</digi:link>
				</td>
				</tr>
			</logic:notEmpty>	
			
<!--  PDF/XLS Links -->		
<logic:notEmpty name="aimCommitmentbyDonorForm" property="report">
			<tr>	
			<td width="14" class="r-dotted-lg">&nbsp;</td>
				<td valign="bottom" class="crumb">
					<logic:notEmpty name="aimCommitmentbyDonorForm" property="report">
					&nbsp;&nbsp;<img src="../ampTemplate/images/pdf_icon.gif" border="0">	
					<digi:link href="" onclick="popup_pdf(''); return false;"> 
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
							&nbsp;&nbsp;<img src="../ampTemplate/images/xls_icon.jpg" border="0">	<digi:link href="" onclick="popup_xls(''); return false;">
								 <digi:trn key="aim:createReportInXls">Create Report in Xls.</digi:trn>
								</digi:link>
		                	</logic:notEmpty>
		                </td>
	          <td valign="top" class="r-dotted-lg">&nbsp;</td>
            </tr>

			<!-- csv link -->
			<tr>
					<td width="14" class="r-dotted-lg" >&nbsp;&nbsp;</td>
			        <td valign="bottom" class="crumb" >
					<logic:notEmpty name="aimCommitmentbyDonorForm" property="report">
							&nbsp;&nbsp;
					<img src="../ampTemplate/images/icon_csv.gif" border="0">
					
						<!--<digi:trn key="aim:clickToCreateReportInCSV">Click here to Create Report in CSV </digi:trn>-->

					<digi:link href="" onclick="popup_csv(''); return false;" title="${translation}">
					 	<digi:trn key="aim:createReportInCsv">Create Report in CSV.</digi:trn>
					</digi:link>
					</logic:notEmpty>
			
            </td>
			<td valign="top" class="r-dotted-lg">&nbsp;</td>
            </tr>
			

</logic:notEmpty>
<!--  PDF/XLS Links -->
<digi:form action="/viewPlannedProjects.do" >			
			<tr>
     	<td width="14" class="r-dotted-lg">&nbsp;</td>
		<td>
  <bean:define id="fcount" name="aimCommitmentbyDonorForm" property="filterCnt" type="java.lang.Integer" /> 
<%
	int fcnt = fcount.intValue();
	System.out.println("FC = " +fcnt);
	
%>              
            <table border="0" cellspacing="0" cellpadding="2">
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
						<logic:notEmpty name="aimCommitmentbyDonorForm" property="sectorColl">
							<html:select property="ampSectorId" styleClass="dr-menu" >
								<option value="0">All Sectors</option>
								<html:optionsCollection name="aimCommitmentbyDonorForm" property="sectorColl" value="ampSectorId" label="name" /> 
							</html:select>
						</logic:notEmpty>
						</td>
						<logic:notEmpty name="aimCommitmentbyDonorForm" property="ampStartDays">
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
						</logic:notEmpty>
						<logic:notEmpty name="aimCommitmentbyDonorForm" property="ampCloseDays">
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
						</logic:notEmpty>
						<td><logic:equal name="aimCommitmentbyDonorForm" property="goFlag" value="true"><input type="submit" name="Submit" value=" GO " class="dr-menu"></logic:equal></td>
                </tr>
            </table>
          </td>
				<td width="14" class="r-dotted-lg">&nbsp;</td>
            </tr>
			
			<tr>
		<td width="14" class="r-dotted-lg">&nbsp;</td>
              <td>
			  <table width="100%"  border="0" cellpadding="0" cellspacing="0">
            <tr bgcolor="#FFFFFF">
              <td colspan="10"><table border="0" cellspacing="0" cellpadding="0">
                  <tr bgcolor="#C9C9C7">
                    <td bgcolor="#C9C9C7" class="box-title">
					<digi:trn key="aim:ViewPlannedProjects">
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
					<td rowspan="3" align="center" height="21" width="73" ><div align="center"><strong>
					<digi:trn key="aim:donor">Donor</digi:trn>
					</strong></div></td>
					<td rowspan="3" align="center" height="21" width="80"><div align="center"><strong>
					<digi:trn key="aim:title">Title </digi:trn>
					</strong></div></td>
					<td rowspan="3" align="center" height="21" width="80"><div align="center"><strong>
					<digi:trn key="aim:status">Status </digi:trn>
					</strong></div></td>
					<td rowspan="3" align="center" height="21" width="52"><div align="center"><strong>
					<digi:trn key="aim:typeOfAssistance">Type Of Assistance</digi:trn>
					</strong></div></td>
					<td rowspan="3" align="center" height="21" width="77"><div align="center"><strong>
					<digi:trn key="aim:instrumentofFunding">Instrument Of Funding</digi:trn>
					</strong></div></td>
					<td rowspan="3" align="center" height="21" width="67"><div align="center"><strong>
					<digi:trn key="aim:sector">Sector </digi:trn>
					</strong></div></td>
					<td rowspan="3" align="center" height="21" width="40"> <div align="center"><strong>
					<digi:trn key="aim:implementationLevel">Implementation Level</digi:trn>
					</strong></div></td>
					<td rowspan="3" align="center" height="21" width="69"><div align="center"><strong>
					<digi:trn key="aim:location">Location </digi:trn>
					</strong></div></td>
					<td rowspan="3" align="center" height="21" width="35"> <div align="center"><strong>
					<digi:trn key="aim:startDate">Start Date</digi:trn>
					</strong></td>
					<td rowspan="3" align="center" height="21" width="39"> <div align="center"><strong>
					<digi:trn key="aim:closeDate">Close Date</digi:trn>
					</strong></div></td>
					<td rowspan="3" align="center" height="21" width="39"> <div align="center"><strong>
					<digi:trn key="aim:commDate">Commitment Date</digi:trn>
					</strong></div></td>
					<td rowspan="3" align="center" height="21" width="69"><div align="center"><strong>
					<digi:trn key="aim:totalCommitments">Total Commitments </digi:trn>
					</strong></div></td>
					<logic:iterate name="aimCommitmentbyDonorForm"  property="fiscalYearRange" id="fiscalYearRange">
				<td height="21" colspan="12" align="center" >
				<div align="center"><strong><%=fiscalYearRange%></strong></div>
				</td>
</logic:iterate>	
<td rowspan="2" colspan="4" align="center" height="21" ><div align="center"><strong>
					<digi:trn key="aim:total">Total</digi:trn> 
					</strong></div></td>

<tr bgcolor="#F4F4F2">
<logic:iterate name="aimCommitmentbyDonorForm"  property="fiscalYearRange" id="fiscalYearRange">
					<td colspan="3" align="center" height="21">Q1</td>
					<td colspan="3" align="center" height="21">Q2</td>
					<td colspan="3" align="center" height="21">Q3</td>
					<td colspan="3" align="center" height="21">Q4</td>
				
	</logic:iterate>
				
</tr>
<tr bgcolor="#F4F4F2">
<logic:iterate name="aimCommitmentbyDonorForm"  property="fiscalYearRange" id="fiscalYearRange">
		<td align="center" height="21" width="7%"> Planned Disbursements</td>
		<td align="center" height="21" width="7%"> Disbursements</td>
		<td align="center" height="21" width="7%"> Expenditures</td>
		<td align="center" height="21" width="7%"> Planned Disbursements</td>
		<td align="center" height="21" width="7%"> Disbursements</td>
		<td align="center" height="21" width="7%"> Expenditures</td>
		<td align="center" height="21" width="7%"> Planned Disbursements</td>
		<td align="center" height="21" width="7%"> Disbursements</td>
		<td align="center" height="21" width="7%"> Expenditures</td>
		<td align="center" height="21" width="7%"> Planned Disbursements</td>
		<td align="center" height="21" width="7%"> Disbursements</td>
		<td align="center" height="21" width="7%"> Expenditures</td>
		
	</logic:iterate>
		<td align="center" height="21" width="7%"> Planned Disbursements</td>
		<td align="center" height="21" width="7%"> Disbursements</td>
		<td align="center" height="21" width="7%"> Expenditures</td>
		<td align="center" height="21" width="7%"> Undisbursed</td>
</tr>
			
	
		<logic:empty name="aimCommitmentbyDonorForm" property="report"> 
		<tr bgcolor="#F4F4F2">
				<!--<td align="center" height="21" width="73" ><b>Sector</b></td>-->
				<td colspan=<bean:write name="aimCommitmentbyDonorForm" property="totalColumns"/> align="center" height="21" ><b>
					<digi:trn key="aim:noRecords">No Records</digi:trn>
					</b>
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

					<logic:iterate name="report" property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
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
					<logic:iterate name="report" property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
							<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
							<bean:write name="ampFund" property="unDisbAmount" />
							</logic:notEqual>
					</logic:iterate>		
					</td>
					
		 </tr>
		 </logic:iterate>
		<tr><td colspan="11" align="left"><b>
			<digi:trn key="aim:total">Total</digi:trn>
			</b></td><td><bean:write name="aimCommitmentbyDonorForm" property="totComm" /></td>
			 <logic:iterate name="aimCommitmentbyDonorForm"  property="totFund" id="totFund" type="org.digijava.module.aim.helper.AmpFund">
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
 
          </table>
			  
            </td>
 				<td width="14" class="r-dotted-lg">&nbsp;</td>
            </tr>
     
            
			<tr>
				<td width="14" class="r-dotted-lg">&nbsp;</td>
                <td width="100%" valign="middle" >
				<table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2">
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
										<digi:trn key="aim:clickToViewPreviousPage">Click here to goto Previous page</digi:trn>
									</c:set>
									<digi:link href="/viewPlannedProjects.do" name="urlParams2" title="${translation}" >
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
									<digi:link href="/viewPlannedProjects.do" name="urlParams1" title="${translation}" >
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
									<digi:link href="/viewPlannedProjects.do" name="urlParams3" title="${translation}" >
										Next
									</digi:link>
									&nbsp;	
								<% } %>
<!-- ------------------------------------------------------------------------------  -->									
								
							</td>
							</tr>
									
					</logic:notEmpty>	

				<tr bgcolor="#F4F4F2"> 
					<td  bgcolor="#F4F4F2">&nbsp;</td>
	                <td align="right" bgcolor="#F4F4F2">&nbsp;</td>
    	        </tr>
            </table></td>
				<td width="14" class="r-dotted-lg">&nbsp;</td>
            </tr>
		  </td>
          <td valign="top" class="r-dotted-lg">&nbsp;</td>
		  <td>

                </td>
	          <td valign="top" class="r-dotted-lg">&nbsp;</td>
			</tr>
</table>
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
		



