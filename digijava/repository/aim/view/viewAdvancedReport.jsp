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
	 	//openResisableWindow(1000, 768);
		<digi:context name="clearVal" property="context/module/moduleinstance/htmlViewProjects.do" />
		document.aimAdvancedReportForm.action = "<%= clearVal %>";
		document.aimAdvancedReportForm.target = "_blank";
		document.aimAdvancedReportForm.submit();
	}

	function clearFilter()
	{
		<digi:context name="clearVal" property="context/module/moduleinstance/viewAdvancedReport.do" />
		url = "<%=clearVal %>?ampReportId=" + document.aimAdvancedReportForm.createdReportId.value;
		document.aimAdvancedReportForm.action = url;
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}

	function popup_pdf() {
		openResisableWindow(800, 600);
		<digi:context name="pdf" property="context/module/moduleinstance/advancedReportPdf.do?docType=pdf" />
		document.aimAdvancedReportForm.action = "<%= pdf %>";
		document.aimAdvancedReportForm.target = popupPointer.name;
		document.aimAdvancedReportForm.submit();
	}

	function popup_xls() {
		openResisableWindow(800, 600);
		<digi:context name="xls" property="context/module/moduleinstance/advancedReportPdf.do?docType=excel" />
		document.aimAdvancedReportForm.action = "<%= xls %>";
		document.aimAdvancedReportForm.target = popupPointer.name;
		document.aimAdvancedReportForm.submit();
	}

	function popup_csv() {
		openResisableWindow(800, 600);
		<digi:context name="csv" property="context/module/moduleinstance/advancedReportPdf.do?docType=csv" />
		document.aimAdvancedReportForm.action = "<%= csv %>";
		document.aimAdvancedReportForm.target = popupPointer.name;
		document.aimAdvancedReportForm.submit();
	}

	function popup_warn() {
		alert("Year Range selected should NOT be Greater than 4 Years.");
	}

-->
</script>

<digi:errors/>
<digi:instance property="aimAdvancedReportForm" />
<digi:form action="/viewAdvancedReport.do" >
<jsp:include page="teamPagesHeader.jsp" flush="true" />

<html:hidden property="createdReportId"/>


<table width="772" border="0" cellpadding="10" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
          <td width="750" align="left" valign="top" >
		
		  <table width="100%"  border="0" cellpadding="5" cellspacing="0">
            
			<tr>
              <td valign="bottom" class="crumb" >
				  <bean:define id="translation">
						<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
					</bean:define>
                <digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>">
				<digi:trn key="aim:MyDesktop">My Desktop</digi:trn>
                </digi:link> &gt; 
				<bean:define id="translation">
					<digi:trn key="aim:clickToViewAllReports">Click here to view list of all Reports </digi:trn>
				</bean:define>
				<digi:link href="/viewTeamReports.do" styleClass="comment" title="<%=translation%>">
				<digi:trn key="aim:AllReport">All Reports</digi:trn>
				</digi:link>&nbsp;&gt;&nbsp;<bean:write name="aimAdvancedReportForm" property="perspective"/> &nbsp;
				<digi:trn key="aim:perspective">perspective</digi:trn></td>
                <td width="2">&nbsp;</td>
             </tr>
	<tr>
				<td colspan=3 class=subtitle-blue align=center>
					
					<bean:write name="aimAdvancedReportForm" property="reportName" />
					
				</td>
			</tr>
			<tr>
				<td colspan=3 class=box-title align=center>
					<bean:write name="aimAdvancedReportForm" property="workspaceType" />&nbsp; <bean:write name="aimAdvancedReportForm" property="workspaceName" />&nbsp; 
			<!--		<digi:trn key="aim:team">Team</digi:trn>		-->
				</td>
			</tr>

			<logic:notEmpty name="aimAdvancedReportForm"  property="report">
				<tr>
					<td>
						<input type="button" value="Print Preview" class="dr-menu">
					</td>
				</tr>
			</logic:notEmpty>			


<!--  PDF/XLS Links -->	
<logic:notEmpty name="aimAdvancedReportForm"  property="report">
		<tr>	
				<td valign="bottom" class="crumb">
					&nbsp;&nbsp;<img src="../ampTemplate/images/pdf_icon.gif" border=0>
					<bean:define id="translation">
						<digi:trn key="aim:clickToCreateReportInPDF">Click here to Create Report in Pdf </digi:trn>
					</bean:define>
					<digi:link href="" onclick="popup_pdf(''); return false;"  title="<%=translation%>"> 
						<digi:trn key="aim:createReportInPdf">Create Report in Pdf.</digi:trn>
					</digi:link>
                </td>
           </tr>
			<tr>
				<td valign="bottom" class="crumb" >
				&nbsp;&nbsp;<img src="../ampTemplate/images/xls_icon.jpg" border=0>
				<bean:define id="translation">
					<digi:trn key="aim:clickToCreateReportInExcel">Click here to Create Report in Excel </digi:trn>
				</bean:define>
				<digi:link href="" onclick="popup_xls(''); return false;" title="<%=translation%>">
					 <digi:trn key="aim:createReportInXls">Create Report in Xls.</digi:trn>
				</digi:link>
                </td>
       </tr>
	
			<tr>
				<td valign="bottom" class="crumb" >
				&nbsp;&nbsp;<img src="../ampTemplate/images/icon_csv.gif" border=0>
				<bean:define id="translation">
					<digi:trn key="aim:clickToCreateReportInExcel">Click here to Create Report in Excel </digi:trn>
				</bean:define>
				<digi:link href="" onclick="popup_csv(''); return false;" title="<%=translation%>">
					 <digi:trn key="aim:createReportInCsv">Create Report in CSV.</digi:trn>
				</digi:link>
                </td>
	       </tr>

</logic:notEmpty>
<!--  PDF/XLS Links -->	

			
			
			<tr><td width="9">
			</td>
        

  <bean:define id="fcount" name="aimAdvancedReportForm" property="filterCnt" type="java.lang.Integer" /> 
<%
	int fcnt = fcount.intValue();
	System.out.println("FC = " +fcnt);
	
%>              
            <table border="0" cellspacing="0" cellpadding="2">
			<TR>
			<logic:greaterThan name="aimAdvancedReportForm" property="filterCnt" value="0" >
			<TD bgColor="black" colspan=<%= fcnt+1%> ></TD>
			</logic:greaterThan>
			</TR>

			<tr bgcolor="#c0c0c0" height=30>	
			
			<logic:equal name="aimAdvancedReportForm" property="filterFlag" value="true">
						<td>			
						<html:select styleClass="dr-menu" property="perspectiveFilter">
								<html:option value="DN">Donor View</html:option>
								<html:option value="MA">MOFED</html:option>
						</html:select>
						</td>
						</logic:equal>

						<logic:notEmpty name="aimAdvancedReportForm" property="currencyColl">
						<td>						
							<html:select property="ampCurrencyCode" styleClass="dr-menu" >
								<html:optionsCollection name="aimAdvancedReportForm" property="currencyColl" value="currencyCode" label="currencyName"/> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimAdvancedReportForm" property="fiscalYears">
						<td>	 					
							<html:select property="fiscalCalId" styleClass="dr-menu">
								<html:optionsCollection name="aimAdvancedReportForm" property="fiscalYears" value="ampFiscalCalId" label="name"/> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimAdvancedReportForm" property="ampFromYears">
						<td>	 					
							<html:select property="ampFromYear" styleClass="dr-menu">
								<html:options name="aimAdvancedReportForm" property="ampFromYears" /> 
							</html:select> 
						</td>
	 					</logic:notEmpty>

						<logic:notEmpty name="aimAdvancedReportForm" property="ampToYears">
						<td>	 					
							<html:select property="ampToYear" styleClass="dr-menu">
								<html:options name="aimAdvancedReportForm" property="ampToYears" /> 
							</html:select> 
						</td>
	 					</logic:notEmpty>

						<logic:notEmpty name="aimAdvancedReportForm" property="regionColl">
						<td>						
							<html:select property="ampLocationId" styleClass="dr-menu" >
								<option value="All">All Regions</option>
									<html:optionsCollection name="aimAdvancedReportForm" property="regionColl" value="name" 
									label="name"  /> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimAdvancedReportForm" property="modalityColl">
						<td>						
							<html:select property="ampModalityId" styleClass="dr-menu" >
								<option value="0">All Financing Instruments</option>
								<html:optionsCollection name="aimAdvancedReportForm" property="modalityColl" value="ampModalityId" label="name" /> 
							</html:select>
						</td>
		 				</logic:notEmpty>

						<logic:notEmpty name="aimAdvancedReportForm" property="donorColl">
						<td>						
							<html:select property="ampOrgId" styleClass="dr-menu" >
								<option value="0">All Donors</option>
								<html:optionsCollection name="aimAdvancedReportForm" property="donorColl" value="ampOrgId" label="acronym"/> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimAdvancedReportForm" property="statusColl">
						<td>						
							<html:select property="ampStatusId" styleClass="dr-menu" >
								<option value="0">All Status</option>
								<html:optionsCollection name="aimAdvancedReportForm" property="statusColl" value="ampStatusId" label="name"  /> 
							</html:select>
						</td>
						</logic:notEmpty>

						<logic:notEmpty name="aimAdvancedReportForm" property="sectorColl">
						<td>						
							<html:select property="ampSectorId" styleClass="dr-menu" >
								<option value="0">All Sectors</option>
								<html:optionsCollection name="aimAdvancedReportForm" property="sectorColl" value="ampSectorId" label="name" /> 
							</html:select>
						<td>
						</logic:notEmpty>

						<logic:notEmpty name="aimAdvancedReportForm" property="ampStartDays">
						<a title="<digi:trn key="aim:DateofStart">Date (dd/mm/yy) when the project commenced (effective start date)</digi:trn>">
						<td>
						
	 						<html:select property="startDay" styleClass="dr-menu">
								<option value="0">DD</option>
								<html:options name="aimAdvancedReportForm" 	property="ampStartDays" /> 
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
							<html:options name="aimAdvancedReportForm" property="ampStartYears" /> 
							</html:select> 
		 				</td>
						</a>
						</logic:notEmpty>
						<logic:notEmpty name="aimAdvancedReportForm" property="ampCloseDays">
						<a title="<digi:trn key="aim:DateofClose">Date (dd/mm/yy) in which the project ended or is expected to end</digi:trn>">
						<td>
	 						<html:select property="closeDay" styleClass="dr-menu">
							<option value="0">DD</option>
							<html:options name="aimAdvancedReportForm" property="ampCloseDays" /> 
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
							<html:options name="aimAdvancedReportForm" property="ampCloseYears" /> 
							</html:select> 
						</td>
						</a>
						</logic:notEmpty>

						<logic:equal name="aimAdvancedReportForm" property="goFlag" value="true">
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
              <td valign="top" colspan="10">
			<!-- begin no hierarchy -->
			<logic:equal name="aimAdvancedReportForm"  property="hierarchyFlag" value="false"> 

            
			    <table width="100%"  border="0" cellpadding="1" cellspacing="1" bgcolor="#C6C7C4">
                    <tr bgcolor="#F4F4F2">
					<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
					<td align="center" height="21" width="42" ><div align="center"><strong>
					<bean:write name="titles" property="columnName" /></strong></div>
					</td></logic:iterate>
				<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
				<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center" >
				<div align="center"><strong><%=fiscalYearRange%></strong></div>
				</td>
			</logic:iterate>
				<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="fundColumns" /> align="center" >
					<b>Total 	</b>
				</td>
			
				</tr>
				<tr bgcolor="#FFFFFF">
				<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
			<td>&nbsp;</td></logic:iterate>
			<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
				<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
					<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>
					</a>
					</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
					<digi:trn key="aim:actualDisbursement">Actual Disbursement</digi:trn>
					</a>
					</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
					<digi:trn key="aim:actualExpenditure">Actual Expenditure</digi:trn>
					</a>
					</td>
				</logic:equal>


				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>">
					<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>
					</a>
					</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
					<digi:trn key="aim:plannedDisbursement">Planned Disbursement</digi:trn>
					</a>
					</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
					<digi:trn key="aim:plannedExpenditure">Planned Expenditure</digi:trn>
					</a>
					</td>
				</logic:equal>

			</logic:iterate>
			
					<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
					<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>
					</a>
					</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
					<digi:trn key="aim:actualDisbursement">Actual Disbursement</digi:trn>
					</a>
					</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
					<digi:trn key="aim:actualExpenditure">Actual Expenditure</digi:trn>
					</a>
					</td>
				</logic:equal>


				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>">
					<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>
					</a>
					</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
					<digi:trn key="aim:plannedDisbursement">Planned Disbursement</digi:trn>
					</a>
					</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
					<digi:trn key="aim:plannedExpenditure">Planned Expenditure</digi:trn>
					</a>
					</td>
				</logic:equal>

				<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
					<td height="21" width="23" align="center" >
					<digi:trn key="aim:undisbursed">Undisbursed</digi:trn>
					</td>
				</logic:equal>

			
				<logic:empty name="aimAdvancedReportForm" property="report"> 
		<tr bgcolor="#F4F4F2">
				<!--<td align="center" height="21" width="73" ><b>Sector</b></td>-->
				<td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/> align="center" height="21" >
				<b>
					<digi:trn key="aim:noRecords">No Records</digi:trn>
				</b>
				</td>
			</tr>
		</logic:empty>
	
		<bean:define id="measureCnt" >
			<bean:write property="measureCount" name="aimAdvancedReportForm"/>
		</bean:define>
	
		<% 
			int i = 0;
		%>
		<logic:notEmpty name="aimAdvancedReportForm" property="report"> 
		<logic:iterate name="aimAdvancedReportForm"  property="report" id="report" type="org.digijava.module.aim.helper.Report">
		<tr bgcolor="#F4F4F2">
		<logic:iterate name="report"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
		<logic:notEmpty name="records" property="title">
				<td align="center" height="21" >
				<bean:write name="records" property="title" /></td></logic:notEmpty>
				<logic:notEmpty name="records" property="actualStartDate">
						<td align="center" height="21" >
				<bean:write name="records" property="actualStartDate" />
				</td></logic:notEmpty>
				<logic:notEmpty name="records" property="actualCompletionDate">
						<td align="center" height="21" >
				<bean:write name="records" property="actualCompletionDate" /></td></logic:notEmpty>
				<logic:notEmpty name="records" property="status">
						<td align="center" height="21" >
				<bean:write name="records" property="status" /></td></logic:notEmpty>
				<logic:notEmpty name="records" property="level">
						<td align="center" height="21" >
				<bean:write name="records" property="level" /></td></logic:notEmpty>
				<logic:notEmpty name="records" property="objective">
						<td align="center" height="21" >
				<bean:write name="records" property="objective" /></td></logic:notEmpty>
				<logic:notEmpty name="records" property="description">
				<td align="center" height="21" width="800">
					<bean:define id="descriptionKey">
						<bean:write name="records" property="description" />
					</bean:define>						
				<digi:edit key="<%=descriptionKey%>" />
				</td></logic:notEmpty>

				<logic:notEmpty name="records" property="assistance">
						<td align="center" height="21" >
				<logic:iterate name="records" id="assistance" property="assistance"> <%=assistance%>	
					<br>
					</logic:iterate></td></logic:notEmpty>
				<logic:notEmpty name="records" property="donors">
						<td align="center" height="21" >
				<logic:iterate name="records" id="donors" property="donors"> <%=donors%>	
					<br>
					</logic:iterate></td></logic:notEmpty>
					<logic:notEmpty name="records" property="sectors">
							<td align="center" height="21" >
				<logic:iterate name="records" id="sectors" property="sectors"> <%=sectors%>	
					<br>
					</logic:iterate></td></logic:notEmpty>
				<logic:notEmpty name="records" property="regions">
						<td align="center" height="21" >
				<logic:iterate name="records" id="regions" property="regions"> <%=regions%>	
					<br>
					</logic:iterate></td></logic:notEmpty>
				<logic:notEmpty name="records" property="contacts">
						<td align="center" height="21" >
				<logic:iterate name="records" id="contacts" property="contacts"> <%=contacts%>	
					<br>
					</logic:iterate></td></logic:notEmpty>
				<logic:notEmpty name="records" property="modality">
						<td align="center" height="21" >
				<logic:iterate name="records" id="modality" property="modality"> <%=modality%>	
					<br>
					</logic:iterate></td></logic:notEmpty>
				<logic:notEmpty name="records" property="ampId">
						<td align="center" height="21" >
				<bean:write name="records" property="ampId" /></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="ampFund">
				<logic:iterate name="records"  property="ampFund" id="ampFund" 	type="org.digijava.module.aim.helper.AmpFund">
				<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="ampFund" property="commAmount" value="0">
							<bean:write name="ampFund" property="commAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="ampFund" property="disbAmount" value="0">
							<bean:write name="ampFund" property="disbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="ampFund" property="expAmount" value="0">
							<bean:write name="ampFund" property="expAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="ampFund" property="plCommAmount" value="0">
							<bean:write name="ampFund" property="plCommAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
							<bean:write name="ampFund" property="plDisbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69" >
							<logic:notEqual name="ampFund" property="plExpAmount" value="0">
							<bean:write name="ampFund" property="plExpAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>

				<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
				<% i = i + 1;
					int j = Integer.parseInt(measureCnt);
					j = j + 1;
					if(i > (j*3))
					{

				%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
							<bean:write name="ampFund" property="unDisbAmount" />
							</logic:notEqual>
						</td>
						<%
						i = 0;
						}
						%>
				</logic:equal>
					</logic:iterate></logic:notEmpty>
				</logic:iterate>
			</tr>
			</logic:iterate>
		
              
              <%  i = 0; %>
			  <tr><td align="left" colspan=<bean:write name="aimAdvancedReportForm" property="dimColumns" />><b> Total</b></td>
			  <logic:iterate name="aimAdvancedReportForm"  property="totFund" id="totFund" type="org.digijava.module.aim.helper.AmpFund">
			  <logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
			  <% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="totFund" property="commAmount" value="0">
							<bean:write name="totFund" property="commAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="totFund" property="disbAmount" value="0">
							<bean:write name="totFund" property="disbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="totFund" property="expAmount" value="0">
							<bean:write name="totFund" property="expAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="totFund" property="plCommAmount" value="0">
							<bean:write name="totFund" property="plCommAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="totFund" property="plDisbAmount" value="0">
							<bean:write name="totFund" property="plDisbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="totFund" property="plExpAmount" value="0">
							<bean:write name="totFund" property="plExpAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
				<% i = i + 1;
					int j = Integer.parseInt(measureCnt);
					j = j + 1;
					if(i > (j*3))
					{

				%>
				
						<td align="right" height="21" width="69">
							<logic:notEqual name="totFund" property="unDisbAmount" value="0">
							<bean:write name="totFund" property="unDisbAmount" />
							</logic:notEqual>
						</td>
						<%
						i = 0;
						}
						%>
						
				</logic:equal>
					</logic:iterate>
				</tr>
		</logic:notEmpty>
	
                     </table>
		</logic:equal>
				<!-- end no hierarchy -->

		<logic:equal name="aimAdvancedReportForm"  property="hierarchyFlag" value="true"> 
		<logic:empty name="aimAdvancedReportForm" property="multiReport"> 
			<tr bgcolor="#F4F4F2">
				<!--<td align="center" height="21" width="73" ><b>Sector</b></td>-->
				<td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/> align="center" height="21" >
				<b>
					<digi:trn key="aim:noRecords">No Records</digi:trn>
				</b>
				</td>
			</tr>
		</logic:empty>
		<logic:notEmpty name="aimAdvancedReportForm" property="multiReport"> 
		<bean:define id="measureCnt" >
					<bean:write property="measureCount" name="aimAdvancedReportForm"/>
					</bean:define>
					<% 
						int i = 0;
					%>
		    <table width="100%"  border="0" cellpadding="1" cellspacing="1" bgcolor="#C6C7C4">
			<logic:iterate name="aimAdvancedReportForm"  property="multiReport" id="multiReport" type="org.digijava.module.aim.helper.multiReport">
				<logic:iterate name="multiReport"  property="hierarchy" id="hierarchy" type="org.digijava.module.aim.helper.AdvancedHierarchyReport">
        			<tr bgcolor="#F4F4F2">
					<!--<td align="center" height="21" width="73" ><b>Sector</b></td>-->
					<td colspan=100 align="left" height="21" >
						<bean:write name="hierarchy" property="label" /> :
						<b><u><bean:write name="hierarchy" property="name" /></u></b>
					</td>
					</tr>

					<!-- Hierarchy 1 -->
					<logic:notEmpty name="hierarchy"  property="project">
                    <tr bgcolor="#F4F4F2">
					<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
						<td align="center" height="21" width="42" ><div align="center"><strong>
							<bean:write name="titles" property="columnName" /></strong></div>
						</td>
					</logic:iterate>
					<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
						<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center" >
						<div align="center"><strong><%=fiscalYearRange%></strong></div>
						</td>
					</logic:iterate>
					<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="fundColumns" /> align="center" >
					<b>Total 	</b>
					</td>
					</tr>
					<tr bgcolor="#FFFFFF">
					<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
						<td>&nbsp;</td>
					</logic:iterate>
					<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
						<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
						<td height="21" width="23" align="center" >
						<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
						<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>
						</a>
						</td>
						</logic:equal>
						<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
						<td height="21" width="23" align="center" >
						<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
						<digi:trn key="aim:actualDisbursement">Actual Disbursement</digi:trn>
						</a>
						</td>
						</logic:equal>
						<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
						<td height="21" width="23" align="center" >
						<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
						<digi:trn key="aim:actualExpenditure">Actual Expenditure</digi:trn>
						</a>
						</td>
						</logic:equal>
						<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
						<td height="21" width="23" align="center" >
						<a title="<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>">
						<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>
						</a>
						</td>
						</logic:equal>
						<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
						<td height="21" width="23" align="center" >
						<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
						<digi:trn key="aim:plannedDisbursement">Planned Disbursement</digi:trn>
						</a>
						</td>
						</logic:equal>
						<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
						<td height="21" width="23" align="center" >
						<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
						<digi:trn key="aim:plannedExpenditure">Planned Expenditure</digi:trn>
						</a>
						</td>
						</logic:equal>

					</logic:iterate>
			
					<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
					<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>
					</a>
					</td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
					<digi:trn key="aim:actualDisbursement">Actual Disbursement</digi:trn>
					</a>
					</td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
					<digi:trn key="aim:actualExpenditure">Actual Expenditure</digi:trn>
					</a>
					</td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>">
					<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>
					</a>
					</td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
					<digi:trn key="aim:plannedDisbursement">Planned Disbursement</digi:trn>
					</a>
					</td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
					<td height="21" width="23" align="center" >
					<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
					<digi:trn key="aim:plannedExpenditure">Planned Expenditure</digi:trn>
					</a>
					</td>
					</logic:equal>

					<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
					<td height="21" width="23" align="center" >
					<digi:trn key="aim:undisbursed">Undisbursed</digi:trn>
					</td>
					</logic:equal>

					

					<logic:iterate name="hierarchy"  property="project" id="project" type="org.digijava.module.aim.helper.Report">
						<tr bgcolor="#F4F4F2">
						<logic:iterate name="project"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
							<logic:notEmpty name="records" property="title">
							<td align="center" height="21" >
							<bean:write name="records" property="title" /></td></logic:notEmpty>
							<logic:notEmpty name="records" property="actualStartDate">
							<td align="center" height="21" >
							<bean:write name="records" property="actualStartDate" />
							</td></logic:notEmpty>
							<logic:notEmpty name="records" property="actualCompletionDate">
							<td align="center" height="21" >
							<bean:write name="records" property="actualCompletionDate" /></td></logic:notEmpty>
							<logic:notEmpty name="records" property="status">
							<td align="center" height="21" >
							<bean:write name="records" property="status" /></td></logic:notEmpty>
							<logic:notEmpty name="records" property="level">
							<td align="center" height="21" >
							<bean:write name="records" property="level" /></td></logic:notEmpty>
							<logic:notEmpty name="records" property="objective">
							<td align="center" height="21" >
							<bean:write name="records" property="objective" /></td></logic:notEmpty>
							<logic:notEmpty name="records" property="description">
							<td align="center" height="21" width="800">
							<bean:define id="descriptionKey">
							<bean:write name="records" property="description" />
							</bean:define>						
							<digi:edit key="<%=descriptionKey%>" />
							</td></logic:notEmpty>
							<logic:notEmpty name="records" property="assistance">
							<td align="center" height="21" >
							<logic:iterate name="records" id="assistance" property="assistance"> <%=assistance%>	
								<br>
							</logic:iterate></td></logic:notEmpty>
							<logic:notEmpty name="records" property="donors">
							<td align="center" height="21" >
							<logic:iterate name="records" id="donors" property="donors"> <%=donors%>	
							<br>
							</logic:iterate></td></logic:notEmpty>
							<logic:notEmpty name="records" property="sectors">
							<td align="center" height="21" >
							<logic:iterate name="records" id="sectors" property="sectors"> <%=sectors%>	
							<br>
							</logic:iterate></td></logic:notEmpty>
							<logic:notEmpty name="records" property="regions">
							<td align="center" height="21" >
							<logic:iterate name="records" id="regions" property="regions"> <%=regions%>	
							<br>
							</logic:iterate></td></logic:notEmpty>
							<logic:notEmpty name="records" property="contacts">
							<td align="center" height="21" >
							<logic:iterate name="records" id="contacts" property="contacts"> <%=contacts%>	
							<br>
							</logic:iterate></td></logic:notEmpty>
							<logic:notEmpty name="records" property="modality">
							<td align="center" height="21" >
							<logic:iterate name="records" id="modality" property="modality"> <%=modality%>	
							<br>
							</logic:iterate></td></logic:notEmpty>
							<logic:notEmpty name="records" property="ampId">
							<td align="center" height="21" >
							<bean:write name="records" property="ampId" /></td></logic:notEmpty>
							<logic:notEmpty name="records" property="ampFund">
							<logic:iterate name="records"  property="ampFund" id="ampFund" 	type="org.digijava.module.aim.helper.AmpFund">
								<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
								<% i = i + 1;	%>
								<td align="right" height="21" width="69">
								<logic:notEqual name="ampFund" property="commAmount" value="0">
								<bean:write name="ampFund" property="commAmount" />
								</logic:notEqual>
								</td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" 	value="true">
								<% i = i + 1;	%>
								<td align="right" height="21" width="69">
								<logic:notEqual name="ampFund" property="disbAmount" value="0">
								<bean:write name="ampFund" property="disbAmount" />
								</logic:notEqual>
								</td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="acExpFlag" 	value="true">
								<% i = i + 1;	%>
								<td align="right" height="21" width="69">
								<logic:notEqual name="ampFund" property="expAmount" value="0">
								<bean:write name="ampFund" property="expAmount" />
								</logic:notEqual>
								</td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
								<% i = i + 1;	%>
								<td align="right" height="21" width="69">
								<logic:notEqual name="ampFund" property="plCommAmount" value="0">
								<bean:write name="ampFund" property="plCommAmount" />
								</logic:notEqual>
								</td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
								<% i = i + 1;	%>
								<td align="right" height="21" width="69">
								<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
								<bean:write name="ampFund" property="plDisbAmount" />
								</logic:notEqual>
								</td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
								<% i = i + 1;	%>
								<td align="right" height="21" width="69" >
								<logic:notEqual name="ampFund" property="plExpAmount" value="0">
								<bean:write name="ampFund" property="plExpAmount" />
								</logic:notEqual>
								</td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
								<% i = i + 1;
								int j = Integer.parseInt(measureCnt);
								j = j + 1;
								if(i > (j*3))
								{
								%>
								<td align="right" height="21" width="69">
								<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
								<bean:write name="ampFund" property="unDisbAmount" />
								</logic:notEqual>
								</td>
								<%
								i = 0;
								}
								%>
								</logic:equal>
							</logic:iterate></logic:notEmpty>
						</logic:iterate>
						</tr>
					</logic:iterate>

			<%  i = 0; %>
			  <tr><td align="left" colspan=<bean:write name="aimAdvancedReportForm" property="dimColumns" />><b> <bean:write name="hierarchy" property="name" /> &nbsp;Total</b></td>
			  <logic:iterate name="hierarchy"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
			  <logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
			  <% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
							<bean:write name="fundSubTotal" property="commAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
							<bean:write name="fundSubTotal" property="disbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
							<bean:write name="fundSubTotal" property="expAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
							<bean:write name="fundSubTotal" property="plCommAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
							<bean:write name="fundSubTotal" property="plDisbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
							<bean:write name="fundSubTotal" property="plExpAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
				<% i = i + 1;
					int j = Integer.parseInt(measureCnt);
					j = j + 1;
					if(i > (j*3))
					{

				%>
				
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
							<bean:write name="fundSubTotal" property="unDisbAmount" />
							</logic:notEqual>
						</td>
						<%
						i = 0;
						}
						%>
						
				</logic:equal>
				</logic:iterate></tr>
			      
				</logic:notEmpty>

<!-- Hierarchy 2-->
<logic:notEmpty name="hierarchy"  property="levels">
	
<logic:iterate name="hierarchy"  property="levels" id="levels" type="org.digijava.module.aim.helper.AdvancedHierarchyReport">
	<tr bgcolor="#F4F4F2">
	<!--<td align="center" height="21" width="73" ><b>Sector</b></td>-->
	<td colspan=100 align="left" height="21" >
	<bean:write name="levels" property="label" /> :
	<b><u><bean:write name="levels" property="name" /></u></b>
	</td>
	</tr>

	<logic:notEmpty name="levels" property="project">
    <tr bgcolor="#F4F4F2">
	<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
		<td align="center" height="21" width="42" ><div align="center"><strong>
		<bean:write name="titles" property="columnName" /></strong></div>
		</td>
	</logic:iterate>
	<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
		<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center" >
		<div align="center"><strong><%=fiscalYearRange%></strong></div>
		</td>
	</logic:iterate>
	<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="fundColumns" /> align="center" >
	<b>Total 	</b>
	</td>
	</tr>
	<tr bgcolor="#FFFFFF">
	<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
	<td>&nbsp;</td>
	</logic:iterate>
	<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
		<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
		<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>
		</a>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
		<digi:trn key="aim:actualDisbursement">Actual Disbursement</digi:trn>
		</a>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
		<digi:trn key="aim:actualExpenditure">Actual Expenditure</digi:trn>
		</a>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>">
		<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>
		</a>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
		<digi:trn key="aim:plannedDisbursement">Planned Disbursement</digi:trn>
		</a>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
		<digi:trn key="aim:plannedExpenditure">Planned Expenditure</digi:trn>
		</a>
		</td>
		</logic:equal>
	</logic:iterate>
			
	<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
	<td height="21" width="23" align="center" >
	<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
	<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>
	</a>
	</td>
	</logic:equal>
	<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
	<td height="21" width="23" align="center" >
	<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
	<digi:trn key="aim:actualDisbursement">Actual Disbursement</digi:trn>
	</a>
	</td>
	</logic:equal>
	<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
	<td height="21" width="23" align="center" >
	<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
	<digi:trn key="aim:actualExpenditure">Actual Expenditure</digi:trn>
	</a>
	</td>
	</logic:equal>
	<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
	<td height="21" width="23" align="center" >
	<a title="<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>">
	<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>
	</a>
	</td>
	</logic:equal>
	<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
	<td height="21" width="23" align="center" >
	<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
	<digi:trn key="aim:plannedDisbursement">Planned Disbursement</digi:trn>
	</a>
	</td>
	</logic:equal>
	<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
	<td height="21" width="23" align="center" >
	<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
	<digi:trn key="aim:plannedExpenditure">Planned Expenditure</digi:trn>
	</a>
	</td>
	</logic:equal>
	<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
	<td height="21" width="23" align="center" >
	<digi:trn key="aim:undisbursed">Undisbursed</digi:trn>
	</td>
	</logic:equal>
	<logic:iterate name="levels"  property="project" id="project" type="org.digijava.module.aim.helper.Report">
		<tr bgcolor="#F4F4F2">
		<logic:iterate name="project"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
			<logic:notEmpty name="records" property="title">
			<td align="center" height="21" >
			<bean:write name="records" property="title" /></td></logic:notEmpty>
			<logic:notEmpty name="records" property="actualStartDate">
			<td align="center" height="21" >
			<bean:write name="records" property="actualStartDate" />
			</td></logic:notEmpty>
			<logic:notEmpty name="records" property="actualCompletionDate">
			<td align="center" height="21" >
			<bean:write name="records" property="actualCompletionDate" /></td></logic:notEmpty>
			<logic:notEmpty name="records" property="status">
			<td align="center" height="21" >
			<bean:write name="records" property="status" /></td></logic:notEmpty>
			<logic:notEmpty name="records" property="level">
			<td align="center" height="21" >
			<bean:write name="records" property="level" /></td></logic:notEmpty>
			<logic:notEmpty name="records" property="objective">
			<td align="center" height="21" >
			<bean:write name="records" property="objective" /></td></logic:notEmpty>
			<logic:notEmpty name="records" property="description">
			<td align="center" height="21" width="800">
			<bean:define id="descriptionKey">
			<bean:write name="records" property="description" />
			</bean:define>						
			<digi:edit key="<%=descriptionKey%>" />
			</td></logic:notEmpty>
			<logic:notEmpty name="records" property="assistance">
			<td align="center" height="21" >
			<logic:iterate name="records" id="assistance" property="assistance"> <%=assistance%>	
			<br>
			</logic:iterate></td></logic:notEmpty>
			<logic:notEmpty name="records" property="donors">
			<td align="center" height="21" >
			<logic:iterate name="records" id="donors" property="donors"> <%=donors%>	
			<br>
			</logic:iterate></td></logic:notEmpty>
			<logic:notEmpty name="records" property="sectors">
			<td align="center" height="21" >
			<logic:iterate name="records" id="sectors" property="sectors"> <%=sectors%>	
			<br>
			</logic:iterate></td></logic:notEmpty>
			<logic:notEmpty name="records" property="regions">
			<td align="center" height="21" >
			<logic:iterate name="records" id="regions" property="regions"> <%=regions%>	
			<br>
			</logic:iterate></td></logic:notEmpty>
			<logic:notEmpty name="records" property="contacts">
			<td align="center" height="21" >
			<logic:iterate name="records" id="contacts" property="contacts"> <%=contacts%>	
			<br>
			</logic:iterate></td></logic:notEmpty>
			<logic:notEmpty name="records" property="modality">
			<td align="center" height="21" >
			<logic:iterate name="records" id="modality" property="modality"> <%=modality%>	
			<br>
			</logic:iterate></td></logic:notEmpty>
			<logic:notEmpty name="records" property="ampId">
			<td align="center" height="21" >
			<bean:write name="records" property="ampId" /></td></logic:notEmpty>
			<logic:notEmpty name="records" property="ampFund">
			<logic:iterate name="records"  property="ampFund" id="ampFund" 	type="org.digijava.module.aim.helper.AmpFund">
				<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
				<% i = i + 1;	%>
				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="commAmount" value="0">
				<bean:write name="ampFund" property="commAmount" />
				</logic:notEqual>
				</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" 	value="true">
				<% i = i + 1;	%>
				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="disbAmount" value="0">
				<bean:write name="ampFund" property="disbAmount" />
				</logic:notEqual>
				</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" 	value="true">
				<% i = i + 1;	%>
				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="expAmount" value="0">
				<bean:write name="ampFund" property="expAmount" />
				</logic:notEqual>
				</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
				<% i = i + 1;	%>
				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="plCommAmount" value="0">
				<bean:write name="ampFund" property="plCommAmount" />
				</logic:notEqual>
				</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
				<% i = i + 1;	%>
				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
				<bean:write name="ampFund" property="plDisbAmount" />
				</logic:notEqual>
				</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
				<% i = i + 1;	%>
				<td align="right" height="21" width="69" >
				<logic:notEqual name="ampFund" property="plExpAmount" value="0">
				<bean:write name="ampFund" property="plExpAmount" />
				</logic:notEqual>
				</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
				<% i = i + 1;
				int j = Integer.parseInt(measureCnt);
				j = j + 1;
				if(i > (j*3))
				{
				%>
				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
				<bean:write name="ampFund" property="unDisbAmount" />
				</logic:notEqual>
				</td>
				<%
				i = 0;
				}
				%>
				</logic:equal>
			</logic:iterate></logic:notEmpty>
		</logic:iterate>
		</tr>
	</logic:iterate>
	</logic:notEmpty>
	
	
	<!--code begin for hierarchy 3 -->
	<logic:notEmpty name="levels" property="levels">
	<logic:iterate name="levels"  property="levels" id="level" type="org.digijava.module.aim.helper.AdvancedHierarchyReport">
		<tr bgcolor="#F4F4F2">
		<!--<td align="center" height="21" width="73" ><b>Sector</b></td>-->
		<td colspan=100 align="left" height="21" >
		<bean:write name="level" property="label" /> :
		<b><u><bean:write name="level" property="name" /></u></b>
		</td>
		</tr>
					
		<tr bgcolor="#F4F4F2">
		<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
			<td align="center" height="21" width="42" ><div align="center"><strong>
			<bean:write name="titles" property="columnName" /></strong></div>
			</td>
		</logic:iterate>
		<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
			<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center" >
			<div align="center"><strong><%=fiscalYearRange%></strong></div>
			</td>
		</logic:iterate>
		<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="fundColumns" /> align="center" >
		<b>Total 	</b>
		</td>
		</tr>
		<tr bgcolor="#FFFFFF">
		<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
			<td>&nbsp;</td>
		</logic:iterate>
					
		<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
			<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
			<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>
			</a>
			</td>
			</logic:equal>
			<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
			<digi:trn key="aim:actualDisbursement">Actual Disbursement</digi:trn>
			</a>
			</td>
			</logic:equal>
			<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
			<digi:trn key="aim:actualExpenditure">Actual Expenditure</digi:trn>
			</a>
			</td>
			</logic:equal>
			<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>">
			<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>
			</a>
			</td>
			</logic:equal>
			<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
			<digi:trn key="aim:plannedDisbursement">Planned Disbursement</digi:trn>
			</a>
			</td>
			</logic:equal>
			<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
			<digi:trn key="aim:plannedExpenditure">Planned Expenditure</digi:trn>
			</a>
			</td>
			</logic:equal>
		</logic:iterate>
			
		<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
		<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>
		</a>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
		<digi:trn key="aim:actualDisbursement">Actual Disbursement</digi:trn>
		</a>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
		<digi:trn key="aim:actualExpenditure">Actual Expenditure</digi:trn>
		</a>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>">
		<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>
		</a>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
		<digi:trn key="aim:plannedDisbursement">Planned Disbursement</digi:trn>
		</a>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
		<digi:trn key="aim:plannedExpenditure">Planned Expenditure</digi:trn>
		</a>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
		<td height="21" width="23" align="center" >
		<digi:trn key="aim:undisbursed">Undisbursed</digi:trn>
		</td>
		</logic:equal>

		<logic:iterate name="level"  property="project" id="project" type="org.digijava.module.aim.helper.Report">
			<tr bgcolor="#F4F4F2">
			<logic:iterate name="project"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
				<logic:notEmpty name="records" property="title">
				<td align="center" height="21" >
				<bean:write name="records" property="title" /></td></logic:notEmpty>
				<logic:notEmpty name="records" property="actualStartDate">
				<td align="center" height="21" >
				<bean:write name="records" property="actualStartDate" />
				</td></logic:notEmpty>
				<logic:notEmpty name="records" property="actualCompletionDate">
				<td align="center" height="21" >
				<bean:write name="records" property="actualCompletionDate" /></td></logic:notEmpty>
				<logic:notEmpty name="records" property="status">
				<td align="center" height="21" >
				<bean:write name="records" property="status" /></td></logic:notEmpty>
				<logic:notEmpty name="records" property="level">
				<td align="center" height="21" >
				<bean:write name="records" property="level" /></td></logic:notEmpty>
				<logic:notEmpty name="records" property="objective">
				<td align="center" height="21" >
				<bean:write name="records" property="objective" /></td></logic:notEmpty>
				<logic:notEmpty name="records" property="description">
				<td align="center" height="21" width="800">
				<bean:define id="descriptionKey">
				<bean:write name="records" property="description" />
				</bean:define>						
				<digi:edit key="<%=descriptionKey%>" />
				</td></logic:notEmpty>
				<logic:notEmpty name="records" property="assistance">
				<td align="center" height="21" >
				<logic:iterate name="records" id="assistance" property="assistance"> <%=assistance%>	
				<br>
				</logic:iterate></td></logic:notEmpty>
				<logic:notEmpty name="records" property="donors">
				<td align="center" height="21" >
				<logic:iterate name="records" id="donors" property="donors"> <%=donors%>	
				<br>
				</logic:iterate></td></logic:notEmpty>
				<logic:notEmpty name="records" property="sectors">
				<td align="center" height="21" >
				<logic:iterate name="records" id="sectors" property="sectors"> <%=sectors%>	
				<br>
				</logic:iterate></td></logic:notEmpty>
				<logic:notEmpty name="records" property="regions">
				<td align="center" height="21" >
				<logic:iterate name="records" id="regions" property="regions"> <%=regions%>	
				<br>
				</logic:iterate></td></logic:notEmpty>
				<logic:notEmpty name="records" property="contacts">
				<td align="center" height="21" >
				<logic:iterate name="records" id="contacts" property="contacts"> <%=contacts%>	
				<br>
				</logic:iterate></td></logic:notEmpty>
				<logic:notEmpty name="records" property="modality">
				<td align="center" height="21" >
				<logic:iterate name="records" id="modality" property="modality"> <%=modality%>	
				<br>
				</logic:iterate></td></logic:notEmpty>
				<logic:notEmpty name="records" property="ampId">
				<td align="center" height="21" >
				<bean:write name="records" property="ampId" /></td></logic:notEmpty>
				<logic:notEmpty name="records" property="ampFund">
				<logic:iterate name="records"  property="ampFund" id="ampFund" 	type="org.digijava.module.aim.helper.AmpFund">
					<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
					<% i = i + 1;	%>
					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="commAmount" value="0">
					<bean:write name="ampFund" property="commAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" 	value="true">
					<% i = i + 1;	%>
					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="disbAmount" value="0">
					<bean:write name="ampFund" property="disbAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acExpFlag" 	value="true">
					<% i = i + 1;	%>
					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="expAmount" value="0">
					<bean:write name="ampFund" property="expAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
					<% i = i + 1;	%>
					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="plCommAmount" value="0">
					<bean:write name="ampFund" property="plCommAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
					<% i = i + 1;	%>
					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
					<bean:write name="ampFund" property="plDisbAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
					<% i = i + 1;	%>
					<td align="right" height="21" width="69" >
					<logic:notEqual name="ampFund" property="plExpAmount" value="0">
					<bean:write name="ampFund" property="plExpAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
					<% i = i + 1;
					int j = Integer.parseInt(measureCnt);
					j = j + 1;
					if(i > (j*3))
					{
					%>
					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
					<bean:write name="ampFund" property="unDisbAmount" />
					</logic:notEqual>
					</td>
					<%
					i = 0;
					}
					%>
					</logic:equal>
				</logic:iterate></logic:notEmpty>
			</logic:iterate>
			</tr>
		</logic:iterate>
		<%  i = 0; %>
		<tr><td align="left" colspan=<bean:write name="aimAdvancedReportForm" property="dimColumns" />><b> <bean:write name="level" property="name" /> &nbsp;Total</b></td>
		<logic:iterate name="level"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
			<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
			<% i = i + 1;	%>
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
			<bean:write name="fundSubTotal" property="commAmount" />
			</logic:notEqual>
			</td>
			</logic:equal>
			<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
			<% i = i + 1;	%>
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
			<bean:write name="fundSubTotal" property="disbAmount" />
			</logic:notEqual>
			</td>
			</logic:equal>
			<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
			<% i = i + 1;	%>
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
			<bean:write name="fundSubTotal" property="expAmount" />
			</logic:notEqual>
			</td>
			</logic:equal>
			<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
			<% i = i + 1;	%>
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
			<bean:write name="fundSubTotal" property="plCommAmount" />
			</logic:notEqual>
			</td>
			</logic:equal>
			<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
			<% i = i + 1;	%>
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
			<bean:write name="fundSubTotal" property="plDisbAmount" />
			</logic:notEqual>
			</td>
			</logic:equal>
			<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
			<% i = i + 1;	%>
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
			<bean:write name="fundSubTotal" property="plExpAmount" />
			</logic:notEqual>
			</td>
			</logic:equal>
			<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
			<% i = i + 1;
			int j = Integer.parseInt(measureCnt);
			j = j + 1;
			if(i > (j*3))
			{
			%>
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
			<bean:write name="fundSubTotal" property="unDisbAmount" />
			</logic:notEqual>
			</td>
			<%
			i = 0;
			}
			%>
			</logic:equal>
		</logic:iterate></tr>
	</logic:iterate>
	</logic:notEmpty>
	<%  i = 0; %>
	<tr><td align="left" colspan=<bean:write name="aimAdvancedReportForm" property="dimColumns" />><b> <bean:write name="levels" property="name" /> &nbsp;Total</b></td>
	<logic:iterate name="levels"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
		<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
		<% i = i + 1;	%>
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
		<bean:write name="fundSubTotal" property="commAmount" />
		</logic:notEqual>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
		<% i = i + 1;	%>
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
		<bean:write name="fundSubTotal" property="disbAmount" />
		</logic:notEqual>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
		<% i = i + 1;	%>
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
		<bean:write name="fundSubTotal" property="expAmount" />
		</logic:notEqual>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
		<% i = i + 1;	%>
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
		<bean:write name="fundSubTotal" property="plCommAmount" />
		</logic:notEqual>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
		<% i = i + 1;	%>
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
		<bean:write name="fundSubTotal" property="plDisbAmount" />
		</logic:notEqual>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
		<% i = i + 1;	%>
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
		<bean:write name="fundSubTotal" property="plExpAmount" />
		</logic:notEqual>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
		<% i = i + 1;
		int j = Integer.parseInt(measureCnt);
		j = j + 1;
		if(i > (j*3))
		{
		%>
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
		<bean:write name="fundSubTotal" property="unDisbAmount" />
		</logic:notEqual>
		</td>
		<%
		i = 0;
		}
		%>
		</logic:equal>
	</logic:iterate></tr>
	
	<!-- code ends for hierarchy three -->
</logic:iterate>
<%  i = 0; %>
<tr><td align="left" colspan=<bean:write name="aimAdvancedReportForm" property="dimColumns" />><b> <bean:write name="hierarchy" property="name" /> &nbsp;Total</b></td>
			  <logic:iterate name="hierarchy"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
			  <logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
			  <% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
							<bean:write name="fundSubTotal" property="commAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
							<bean:write name="fundSubTotal" property="disbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
							<bean:write name="fundSubTotal" property="expAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
							<bean:write name="fundSubTotal" property="plCommAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
							<bean:write name="fundSubTotal" property="plDisbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
							<bean:write name="fundSubTotal" property="plExpAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
				<% i = i + 1;
					int j = Integer.parseInt(measureCnt);
					j = j + 1;
					if(i > (j*3))
					{

				%>
				
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
							<bean:write name="fundSubTotal" property="unDisbAmount" />
							</logic:notEqual>
						</td>
						<%
						i = 0;
						}
						%>
						
				</logic:equal>
				</logic:iterate></tr>

			       
				</logic:notEmpty>
				
			 </logic:iterate>
			   <%  i = 0; %>
			  <tr><td align="left" colspan=<bean:write name="aimAdvancedReportForm" property="dimColumns" />><b> Grand Total</b></td>
			  <logic:iterate name="multiReport"  property="fundTotal" id="fundTotal" type="org.digijava.module.aim.helper.AmpFund">
			  <logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
			  <% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundTotal" property="commAmount" value="0">
							<bean:write name="fundTotal" property="commAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundTotal" property="disbAmount" value="0">
							<bean:write name="fundTotal" property="disbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundTotal" property="expAmount" value="0">
							<bean:write name="fundTotal" property="expAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundTotal" property="plCommAmount" value="0">
							<bean:write name="fundTotal" property="plCommAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundTotal" property="plDisbAmount" value="0">
							<bean:write name="fundTotal" property="plDisbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
				<% i = i + 1;	%>
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundTotal" property="plExpAmount" value="0">
							<bean:write name="fundTotal" property="plExpAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
				<% i = i + 1;
					int j = Integer.parseInt(measureCnt);
					j = j + 1;
					if(i > (j*3))
					{

				%>
				
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundTotal" property="unDisbAmount" value="0">
							<bean:write name="fundTotal" property="unDisbAmount" />
							</logic:notEqual>
						</td>
						<%
						i = 0;
						}
						%>
						
				</logic:equal>
				</logic:iterate>
				   
				</logic:iterate>	
					
				</tr>
		</logic:notEmpty>
	
                     </table>


		</logic:equal>
		</td></tr>
		<tr bgcolor="#F4F4F2">

          </table>
			  
            </td>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
            </tr>
			
            
			<tr>
          <td width="14" class="r-dotted-lg">&nbsp;</td>
                <td width="100%" valign="middle" ><table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2">
              		<tr bgcolor="#F4F4F2"> 
		                <logic:notEmpty name="aimAdvancedReportForm" property="pages">
							<tr>
							<td>
<!--							
								Page <bean:write name="aimAdvancedReportForm" property="page" />
									<digi:trn key="aim:ofPages">
								of Pages :
								</digi:trn>
-->

<!-- -------------------------------  Prevoius Links     ---------------------------       -->
								<bean:define id="currPage" name="aimAdvancedReportForm" property="page" type="java.lang.Integer" /> 
								<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
								<c:set target="${urlParams2}" property="page">
									<%=(currPage.intValue()-1)%>
								</c:set>
								<c:set target="${urlParams2}" property="ampReportId">
									<bean:write name="aimAdvancedReportForm" property="createdReportId" />
								</c:set>
								
								<logic:notEqual name="aimAdvancedReportForm" property="page"
								value="1">
								  <bean:define id="translation">
										<digi:trn key="aim:clickToViewPreviousPage">Click here to view Previous page</digi:trn>
									</bean:define>
									<digi:link href="/viewAdvancedReport.do" name="urlParams2" title="<%=translation%>" >
										Previous
									</digi:link>
									&nbsp
								</logic:notEqual>
								
								<logic:equal name="aimAdvancedReportForm" property="page"
								value="1">
									<digi:trn key="aim:prev">Previous</digi:trn> &nbsp
								</logic:equal>	
<!----------------------------------END   -----------------------------------------------     -->									

								
								<logic:iterate name="aimAdvancedReportForm" property="pages" id="pages" type="java.lang.Integer">
									<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlParams1}" property="page">
										<%=pages%>
									</c:set>
									<c:set target="${urlParams1}" property="ampReportId">
										<bean:write name="aimAdvancedReportForm" property="createdReportId" />
									</c:set>
								
									<%  int curr = currPage.intValue();
										int cnt = pages.intValue();
										System.out.println(curr + " Comparison : " + cnt);
									%>
									<% if( curr != cnt ) { %>
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAllPages">Click here to view All pages</digi:trn>
									</bean:define>
									<digi:link href="/viewAdvancedReport.do" name="urlParams1" title="<%=translation%>" >
										<%=pages%>
									</digi:link>
									<% } else { %>
									<%=pages%>
									<% } %>
										|&nbsp; 
								</logic:iterate>
								
								
<!-- -------------------------------  Next Links -------------------------------       -->									
								<bean:define id="currPage" name="aimAdvancedReportForm" property="page" type="java.lang.Integer" /> 
								<jsp:useBean id="urlParams3" type="java.util.Map" class="java.util.HashMap"/>
								<c:set target="${urlParams3}" property="page">
									<%=(currPage.intValue()+1)%>
								</c:set>

								<c:set target="${urlParams3}" property="ampReportId">
									<bean:write name="aimAdvancedReportForm" property="createdReportId" />
								</c:set>
								<bean:define name="aimAdvancedReportForm" id="allPages" property="pages" 
								type="java.util.Collection" />
								<% if(allPages.size() == currPage.intValue()) { %>	
									&nbsp; <digi:trn key="aim:next">Next</digi:trn>  
								<% } else { %>
								  <bean:define id="translation">
										<digi:trn key="aim:clickToViewNextPage">Click here to go to Next page</digi:trn>
									</bean:define>
									<digi:link href="/viewAdvancedReport.do" name="urlParams3" title="<%=translation%>" >
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

