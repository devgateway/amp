<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/advanceReportManager.js"/>"></script>
<script language="JavaScript">
	function saveReport()
	{
		alert("Your report is being saved");
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=SaveReport" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}

function gotoStep() {

	<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=5" />
	document.aimAdvancedReportForm.action = "<%= step %>";
	document.aimAdvancedReportForm.target = "_self";
	document.aimAdvancedReportForm.submit();
}
</script>


<digi:instance property="aimAdvancedReportForm" />
<digi:form action="/advancedReportManager.do" method="post">
<input type="hidden" name="isAdd" >

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
<tr>
	<td>
		<jsp:include page="teamPagesHeader.jsp" flush="true" />
	</td>
</tr>

<tr>

<td width="100%" vAlign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 vAlign="top" align="left" border=0>
	<tr><td class=r-dotted-lg align=left vAlign=top >	&nbsp;</td>
	<td>
	<table><tr><td>

	<table cellPadding=5 cellSpacing=0 width="100%">
	<tr><td height=33><span class=crumb>
	<bean:define id="translation">
	<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
	</bean:define>
	<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
	<digi:trn key="aim:portfolio">Portfolio</digi:trn></digi:link>&nbsp;&gt;&nbsp;						<bean:define id="translation"><digi:trn key="aim:clickToGotoStep1">Click here to goto Step 1</digi:trn></bean:define>
	<digi:link href="/advancedReportManager.do?check=forward" styleClass="comment" title="<%=translation%>" ><digi:trn key="aim:reportBuilder:selectcolumn">Report Builder : Select Column</digi:trn>&gt;&gt;</digi:link>&nbsp;&nbsp;<digi:link href="/advancedReportManager.do?check=5" styleClass="comment" title="<%=translation%>" ><digi:trn key="aim:reportBuilder:selectrows">		Report Builder : Select Rows</digi:trn>&gt;&gt;</digi:link>&nbsp;&nbsp;							<digi:link href="/advancedReportManager.do?check=SelectMeasuress" styleClass="comment" title="<%=translation%>" ><digi:trn key="aim:reportBuilder:selectmeasures">Report Builder : Select Measures</digi:trn>&gt;&gt;</digi:link>&nbsp;&nbsp;<digi:link href="/advancedReportManager.do?check=4" styleClass="comment" title="<%=translation%>" ><digi:trn key="aim:reportBuilder:reportDetails">Report Builder : Report Details</digi:trn>&gt;&gt;</digi:link>&nbsp;&nbsp;<digi:trn key="aim:reportBuilder:htmlReport">Report Builder : HTML Report</digi:trn>					
	</td></tr>
	</table>	
	</td></tr>

	<tr>
	<td height=16 vAlign=right align=center><span class=subtitle-blue>Report Builder : Report Creation	</span></td>
	</tr>
	
	<tr colspan="2">
	<td class=box-title align="center" valign="top">&nbsp;<td>
	</tr>

	<TR>
	<TD vAlign="top" align="center">
	<TABLE width="100%" cellSpacing=0 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4" class="box-border-nopadding">
	<TR><TD bgcolor="#f4f4f4">
	<TABLE width="100%" cellSpacing=1 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4">
	<tr width="100%" valign="top"><td height="20">
	<table bgcolor="#f4f4f4" align="left" valign="bottom" cellPadding=0 cellspacing=1 height="20">
	<tr><td noWrap align=left> 
	<bean:define id="translation">
	<digi:trn key="aim:clickToSelectColumns">Click here to Select Columns</digi:trn>
	</bean:define>
	<digi:link href="/advancedReportManager.do?check=forward"   styleClass="sub-nav" title="<%=translation%>" >
	
	1 :   Select Columns
	</digi:link>
	</td>											
	<td noWrap align=left>
	<bean:define id="translation">
	<digi:trn key="aim:clickToselectrows/hierarchies">Click here to select rows/hierarchies</digi:trn>
	</bean:define>
	<digi:link href="/advancedReportManager.do?check=SelectRows"  styleClass="sub-nav" title="<%=translation%>" >
	
	2 : <digi:trn key="aim:SelectRows/hierarchies">Select rows/hierarchies</digi:trn>
	</digi:link>
	</td>										
	<td noWrap align=left>
	<bean:define id="translation">
	<digi:trn key="aim:clickToSelectMeasures">Click here to Select Measures</digi:trn>
	</bean:define>
	<digi:link href="/advancedReportManager.do?check=SelectMeasures"  styleClass="sub-nav" title="<%=translation%>" > 
	
	3 : <digi:trn key="aim:SelectMeasures">Select Measures</digi:trn>
	</digi:link>
	</td>											
	<td noWrap align=left> 
	<bean:define id="translation">
	<digi:trn key="aim:clickToViewReportDetails">Click here to view Report Details</digi:trn>
	</bean:define>
	<digi:link href="/advancedReportManager.do?check=4"  styleClass="sub-nav" title="<%=translation%>" >

	4 : <digi:trn key="aim:ReportDetails">Report Details</digi:trn>
	</digi:link>
	</td>
	</tr>
	</table>	
	</td></tr>
	<TR><td noWrap valign=top align=left>
	<table cellpadding=0 cellspacing=1 valign=top align=left>	
	<tr><td valign=top>
	<bean:define id="translation">
	<digi:trn key="aim:clickToGenerateReport">Click here to Generate Reports</digi:trn>
	</bean:define>
	<digi:link href="/advancedReportManager.do?check=5"  styleClass="sub-nav3" title="<%=translation%>" >
	
	5 : <digi:trn key="aim:GenerateReport">Generate Report</digi:trn>
	</digi:link>
	</td>	
	<td noWrap valign=top align=left>
	<bean:define id="translation">
	<digi:trn key="aim:clickToGenerateReport">Click here to Generate Chart</digi:trn>
	</bean:define>
	<digi:link href="/advancedReportManager.do?check=5"  styleClass="sub-nav" title="<%=translation%>" onclick="javascript:alert('Charts Coming Soon...');">
	
	6 : <digi:trn key="aim:GenerateChart">Generate Chart</digi:trn>
	</digi:link>
	</td></tr></table>
	</td></tr>

	<TR bgColor=#f4f4f2>
	<TD vAlign="top" align="left" width="100%"></TD>
	</TR>				
	<TR bgColor=#f4f4f2>
	<TD vAlign="top" align="center" width="100%" bgColor=#f4f4f2>
	<TABLE width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2 >
	<TR>
	<c:if test="${!empty aimAdvancedReportForm.finalData}">
	<TD width="100%" align="center"  valign=top>
	<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" align="center" bgColor=#f4f4f2 class="box-border-nopadding" border=0>

	<!-- begin no hierarchy -->
	<logic:equal name="aimAdvancedReportForm"  property="hierarchyFlag" value="false">			
	<tr><td bgColor=#f4f4f2>
	<TABLE width="100%" cellPadding=2 cellSpacing=0 vAlign="top" align="top" bgColor=#f4f4f2 border=1 style="border-collapse: collapse">
	<tr bgcolor="#cccccc">
	<c:if test="${!empty aimAdvancedReportForm.addedColumns}">
	<logic:iterate name="aimAdvancedReportForm" id="addedColumns"	property="addedColumns" >
		<td align="center"  class=box-title>
		<c:out value="${addedColumns.columnName}"/>
		</td>
	</logic:iterate>
	</c:if>
																	
	<bean:size name="aimAdvancedReportForm" property="addedMeasures" id="measureSize"/>
	<%
		int fcnt = measureSize.intValue();
		int flag = 0;
	%>              
	<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">				<c:if test="${addedMeasures.measureName == 'Cumulative Balance'}">
		<%fcnt = fcnt-1;
		flag = 1;
		%>
		</c:if>
	</logic:iterate>
	
	<%
	if(flag == 0 || fcnt > 0)
	{
	%>
	<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
		<td height="21" width="69" colspan=<%=fcnt%> align="center" >
		<strong><%=fiscalYearRange%></strong>
		</td>
	</logic:iterate>

	<%
	}
	%>

	<td width="69" colspan=7 align=center >
	<b> Total 	</b>
	</td>
    </tr> 	
	
	<tr> <!-- heading start -->
	<logic:iterate name="aimAdvancedReportForm"  property="addedColumns" id="addedColumns">
	<td></td>
	</logic:iterate>
	
	<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
		<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">		

			<c:if test="${addedMeasures.measureName == 'Actual Commitments'}">
			<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
			<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Actual Disbursements'}">
			<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
			<digi:trn key="aim:actualDisbursement">Actual Disbursement</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Actual Expenditures'}">
			<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
			<digi:trn key="aim:actualExpenditure">Actual Expenditure</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>											
			
			<c:if test="${addedMeasures.measureName == 'Planned Commitments'}">
			<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>">
			<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Planned Disbursements'}">
			<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
			<digi:trn key="aim:plannedDisbursement">Planned Disbursement</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>
	
			<c:if test="${addedMeasures.measureName == 'Planned Expenditures'}">
			<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
			<digi:trn key="aim:plannedExpenditure">Planned Expenditure</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>
		</logic:iterate>
	</logic:iterate>

	<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">				<c:if test="${addedMeasures.measureName == 'Actual Commitments'}">
		<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
		<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>
		</a>
		</td>
		</logic:equal>
		</c:if>
		
		<c:if test="${addedMeasures.measureName == 'Actual Disbursements'}">
		<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
		<digi:trn key="aim:actualDisbursement">Actual Disbursement</digi:trn>
		</a>
		</td>
		</logic:equal>
		</c:if>
		
		<c:if test="${addedMeasures.measureName == 'Actual Expenditures'}">
		<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
		<digi:trn key="aim:actualExpenditure">Actual Expenditure</digi:trn>
		</a>
		</td>
		</logic:equal>
		</c:if>											
														
		<c:if test="${addedMeasures.measureName == 'Planned Commitments'}">
		<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>">
		<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>
		</a>
		</td>
		</logic:equal>
		</c:if>

		<c:if test="${addedMeasures.measureName == 'Planned Disbursements'}">
		<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
		<digi:trn key="aim:plannedDisbursement">Planned Disbursement</digi:trn>
		</a>
		</td>
		</logic:equal>
		</c:if>

		<c:if test="${addedMeasures.measureName == 'Planned Expenditures'}">
		<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
		<td height="21" width="23" align="center" >
		<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
		<digi:trn key="aim:plannedExpenditure">Planned Expenditure</digi:trn>
		</a>
		</td>
		</logic:equal>
		</c:if>

		<c:if test="${addedMeasures.measureName == 'Cumulative Balance'}">
		<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
		<td height="21" width="23" align="center" >
		<digi:trn key="aim:cumulativeBalance">Cumulative Balance</digi:trn>
		</td>
		</logic:equal>
		</c:if>
	</logic:iterate>
	</tr> <!-- Heading end -->

	<logic:notEmpty name="aimAdvancedReportForm" property="report"> 
	<logic:iterate name="aimAdvancedReportForm"  property="report" id="report" type="org.digijava.module.aim.helper.Report">
		<tr bgcolor="#F4F4F2">
		<logic:iterate name="report"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
			<logic:notEmpty name="records" property="title">
			<td align="center" height="21">
			<bean:write name="records" property="title" />
			</td>
			</logic:notEmpty>

			<logic:notEmpty name="records" property="actualStartDate">
			<td align="center" height="21">
			<bean:write name="records" property="actualStartDate" /></td></logic:notEmpty>

			<logic:notEmpty name="records" property="actualCompletionDate">
			<td align="center" height="21">
			<bean:write name="records" property="actualCompletionDate" /></td></logic:notEmpty>
			
			<logic:notEmpty name="records" property="status">
			<td align="center" height="21">
			<bean:write name="records" property="status" /></td></logic:notEmpty>

			<logic:notEmpty name="records" property="level">
			<td align="center" height="21">
			<bean:write name="records" property="level" /></td></logic:notEmpty>

			<logic:notEmpty name="records" property="objective">
			<td align="center" height="21">
			<bean:write name="records" property="objective" /></td></logic:notEmpty>

			<logic:notEmpty name="records" property="description">
			<td align="center" height="21" width="800">
			<bean:define id="descriptionKey">
			<bean:write name="records" property="description" />
			</bean:define>						
			<digi:edit key="<%=descriptionKey%>" />
			</td></logic:notEmpty>
				
			<logic:notEmpty name="records" property="assistance">
			<td align="center" height="21">
			<logic:iterate name="records" id="assistance" property="assistance"> <%=assistance%>	
			<br>
			</logic:iterate></td></logic:notEmpty>

			<logic:notEmpty name="records" property="donors">
			<td align="center" height="21">
			<logic:iterate name="records" id="donors" property="donors"> <%=donors%>	
			<br>
			</logic:iterate></td></logic:notEmpty>

			<logic:notEmpty name="records" property="sectors">
			<td align="center" height="21">
			<logic:iterate name="records" id="sectors" property="sectors"> <%=sectors%>	
			<br>
			</logic:iterate></td></logic:notEmpty>

			<logic:notEmpty name="records" property="regions">
			<td align="center" height="21">
			<logic:iterate name="records" id="regions" property="regions"> <%=regions%>	
			<br>
			</logic:iterate></td></logic:notEmpty>
			<logic:notEmpty name="records" property="contacts">
			<td align="center" height="21">
			<logic:iterate name="records" id="contacts" property="contacts"> <%=contacts%>	
			<br>
			</logic:iterate></td></logic:notEmpty>

			<logic:notEmpty name="records" property="modality">
			<td align="center" height="21" >
			<logic:iterate name="records" id="modality" property="modality"> <%=modality%>	
			<br>
			</logic:iterate></logic:notEmpty>

			<logic:notEmpty name="records" property="year">
			<bean:write name="records" property="year" /></logic:notEmpty>

			<logic:notEmpty name="records" property="ampId">
			<td align="center" height="21" >
			<bean:write name="records" property="ampId" /></td></logic:notEmpty>

			<logic:notEmpty name="records" property="ampFund">
			<% int i=0;%>
			<logic:iterate name="records"  property="ampFund" id="ampFund" 	type="org.digijava.module.aim.helper.AmpFund">
				<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">
					<%i = i + 1; 
					%>					
					<c:if test="${addedMeasures.measureName == 'Actual Commitments'}">
					<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
					<td align="right" height="21" width="69" >
					<logic:notEqual name="ampFund" property="commAmount" value="0">
					<bean:write name="ampFund" property="commAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					</c:if>

					<c:if test="${addedMeasures.measureName == 'Actual Disbursements'}">
					<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="disbAmount" value="0">
					<bean:write name="ampFund" property="disbAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					</c:if>
						
					<c:if test="${addedMeasures.measureName == 'Actual Expenditures'}">
					<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="expAmount" value="0">
					<bean:write name="ampFund" property="expAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					</c:if>
						
					<c:if test="${addedMeasures.measureName == 'Planned Commitments'}">
					<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="plCommAmount" value="0">
					<bean:write name="ampFund" property="plCommAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					</c:if>
						
					<c:if test="${addedMeasures.measureName == 'Planned Disbursements'}">
					<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
					<bean:write name="ampFund" property="plDisbAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					</c:if>
						
					<c:if test="${addedMeasures.measureName == 'Planned Expenditures'}">
					<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="plExpAmount" value="0">
					<bean:write name="ampFund" property="plExpAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					</c:if>

					<c:if test="${addedMeasures.measureName == 'Cumulative Balance'}">
					<%
						if(i > ((fcnt+1)*3))
						{
					%>
					<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
					<bean:write name="ampFund" property="unDisbAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					<%
						}
					%>
					</c:if>
						
				</logic:iterate>
			</logic:iterate>	
			</logic:notEmpty>								
		</logic:iterate>
		</tr>
	</logic:iterate>
										
	<bean:size name="aimAdvancedReportForm" property="addedColumns" id="mSize"/>
	<%
		int cnt = mSize.intValue();
		cnt = cnt - 1;
		int i =0;
	%>              
					
	<tr background="#F4F4F2"><td align="right"> <b> Sub Total :</b></td>
	<td colspan="<%=cnt%>"> </td>
	<logic:iterate name="aimAdvancedReportForm"  property="totFund" id="totFund" type="org.digijava.module.aim.helper.AmpFund">
		<%i = i + 1; 
		%>		
		<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
		<td align="right" height="21" width="69" >
		<logic:notEqual name="totFund" property="commAmount" value="0">
		<b><bean:write name="totFund" property="commAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="totFund" property="disbAmount" value="0">
		<b><bean:write name="totFund" property="disbAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="totFund" property="expAmount" value="0">
		<b><bean:write name="totFund" property="expAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="totFund" property="plCommAmount" value="0">
		<b><bean:write name="totFund" property="plCommAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="totFund" property="plDisbAmount" value="0">
		<b><bean:write name="totFund" property="plDisbAmount" />	</b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="totFund" property="plExpAmount" value="0">
		<b><bean:write name="totFund" property="plExpAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
		<%
		if(i > ((cnt+1)*3))
		{
		%>
		<td align="right" height="21" width="69" >
		<logic:notEqual name="totFund" property="unDisbAmount" value="0">
		<b><bean:write name="totFund" property="unDisbAmount" /></b>
		</logic:notEqual>
		</td>
		<%
		}
		%>
		</logic:equal>
	</logic:iterate>
	</tr>																
	</logic:notEmpty>
	</TABLE></td></tr>
	</logic:equal>
	<!-- end of no hierarchy -->

	<!-- begin of hierarchy -->
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
	<tr>
	<td bgColor=#f4f4f2>
	<TABLE width="100%" cellPadding=2 cellSpacing=0 vAlign="top" align="top" bgColor=#f4f4f2 border=1 style="border-collapse: collapse">
	<logic:iterate name="aimAdvancedReportForm"  property="multiReport" id="multiReport" type="org.digijava.module.aim.helper.multiReport">
		<logic:iterate name="multiReport"  property="hierarchy" id="hierarchy" type="org.digijava.module.aim.helper.AdvancedHierarchyReport">
    	<tr bgcolor="#F4F4F2">
		<!--<td align="center" height="21" width="73" ><b>Sector</b></td>-->
		<td colspan=100 align="left" height="21" >
		<bean:write name="hierarchy" property="label" /> :
		<b><u><bean:write name="hierarchy" property="name" /></u></b>
		</td>
		</tr>
	
		<logic:notEmpty name="hierarchy"  property="project">
		<tr bgcolor="#cccccc">
		
		<c:if test="${!empty aimAdvancedReportForm.addedColumns}">
		<logic:iterate name="aimAdvancedReportForm" id="addedColumns"	property="addedColumns" >
			<td align="center"  class=box-title>
			<c:out value="${addedColumns.columnName}"/>
			</td>
		</logic:iterate>
		</c:if>
																	
		<bean:size name="aimAdvancedReportForm" property="addedMeasures" id="measureSize"/>
		              
		
		<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">			<c:if test="${addedMeasures.measureName == 'Cumulative Balance'}">
			
			</c:if>
		</logic:iterate>
		<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
			<td height="21" width="69" colspan="30" align="center" >
			<strong><%=fiscalYearRange%></strong>
			</td>
		</logic:iterate>
		
		<td width="69" colspan=7 align=center >
		<b> Total 	</b>
		</td></tr>
		
		<tr>
		<logic:iterate name="aimAdvancedReportForm"  property="addedColumns" id="addedColumns">
		<td></td>
		</logic:iterate>
		
		<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
			<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">		

				<c:if test="${addedMeasures.measureName == 'Actual Commitments'}">
				<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
				<td height="21" width="23" align="center" >
				<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
				<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>
				</a>
				</td>
				</logic:equal>
				</c:if>

				<c:if test="${addedMeasures.measureName == 'Actual Disbursements'}">
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
				<td height="21" width="23" align="center" >
				<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
				<digi:trn key="aim:actualDisbursement">Actual Disbursement</digi:trn>
				</a>
				</td>
				</logic:equal>
				</c:if>

				<c:if test="${addedMeasures.measureName == 'Actual Expenditures'}">
				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
				<td height="21" width="23" align="center" >
				<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the 	implementing agency</digi:trn>">
				<digi:trn key="aim:actualExpenditure">Actual Expenditure</digi:trn>
				</a>
				</td>
				</logic:equal>
				</c:if>											
			
				<c:if test="${addedMeasures.measureName == 'Planned Commitments'}">
				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
				<td height="21" width="23" align="center" >
				<a title="<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>">
				<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>
				</a>
				</td>
				</logic:equal>
				</c:if>

				<c:if test="${addedMeasures.measureName == 'Planned Disbursements'}">
				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
				<td height="21" width="23" align="center" >
				<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
				<digi:trn key="aim:plannedDisbursement">Planned Disbursement</digi:trn>
				</a>
				</td>
				</logic:equal>
				</c:if>
	
				<c:if test="${addedMeasures.measureName == 'Planned Expenditures'}">
				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
				<td height="21" width="23" align="center" >
				<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
				<digi:trn key="aim:plannedExpenditure">Planned Expenditure</digi:trn>
				</a>
				</td>
				</logic:equal>
				</c:if>
			</logic:iterate>
		</logic:iterate>

		<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">			<c:if test="${addedMeasures.measureName == 'Actual Commitments'}">
			<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
			<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>
		
			<c:if test="${addedMeasures.measureName == 'Actual Disbursements'}">
			<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
			<digi:trn key="aim:actualDisbursement">Actual Disbursement</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>
		
			<c:if test="${addedMeasures.measureName == 'Actual Expenditures'}">
			<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
			<digi:trn key="aim:actualExpenditure">Actual Expenditure</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>											
														
			<c:if test="${addedMeasures.measureName == 'Planned Commitments'}">
			<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>">
			<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>
	
			<c:if test="${addedMeasures.measureName == 'Planned Disbursements'}">
			<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
			<digi:trn key="aim:plannedDisbursement">Planned Disbursement</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Planned Expenditures'}">
			<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
			<digi:trn key="aim:plannedExpenditure">Planned Expenditure</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Cumulative Balance'}">
			<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
			<td height="21" width="23" align="center" >
			<digi:trn key="aim:cumulativeBalance">Cumulative Balance</digi:trn>
			</td>
			</logic:equal>
			</c:if>
		</logic:iterate>
		</tr> <!-- Heading end -->
				
		<logic:iterate name="hierarchy"  property="project" id="project" type="org.digijava.module.aim.helper.Report">
			<tr bgcolor="#F4F4F2">
			<logic:iterate name="project"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
				<logic:notEmpty name="records" property="title">
				<td align="center" height="21">
				<bean:write name="records" property="title" />
				</td>
				</logic:notEmpty>

				<logic:notEmpty name="records" property="actualStartDate">
				<td align="center" height="21">
				<bean:write name="records" property="actualStartDate" /></td></logic:notEmpty>

				<logic:notEmpty name="records" property="actualCompletionDate">
				<td align="center" height="21">
				<bean:write name="records" property="actualCompletionDate" /></td></logic:notEmpty>

				<logic:notEmpty name="records" property="status">
				<td align="center" height="21">
				<bean:write name="records" property="status" /></td></logic:notEmpty>

				<logic:notEmpty name="records" property="level">
				<td align="center" height="21">
				<bean:write name="records" property="level" /></td></logic:notEmpty>

				<logic:notEmpty name="records" property="objective">
				<td align="center" height="21">
				<bean:write name="records" property="objective" /></td></logic:notEmpty>

				<logic:notEmpty name="records" property="description">
				<td align="center" height="21" width="800">
				<bean:define id="descriptionKey">
				<bean:write name="records" property="description" />
				</bean:define>						
				<digi:edit key="<%=descriptionKey%>" />
				</td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="assistance">
				<td align="center" height="21">
				<logic:iterate name="records" id="assistance" property="assistance"> <%=assistance%>	
				<br>
				</logic:iterate></td></logic:notEmpty>
	
				<logic:notEmpty name="records" property="donors">
				<td align="center" height="21">
				<logic:iterate name="records" id="donors" property="donors"> <%=donors%>	
				<br>
				</logic:iterate></td></logic:notEmpty>

				<logic:notEmpty name="records" property="sectors">
				<td align="center" height="21">
				<logic:iterate name="records" id="sectors" property="sectors"> <%=sectors%>	
				<br>
				</logic:iterate></td></logic:notEmpty>
	
				<logic:notEmpty name="records" property="regions">
				<td align="center" height="21">
				<logic:iterate name="records" id="regions" property="regions"> <%=regions%>	
				<br>
				</logic:iterate></td></logic:notEmpty>

				<logic:notEmpty name="records" property="contacts">
				<td align="center" height="21">
				<logic:iterate name="records" id="contacts" property="contacts"> <%=contacts%>	
				<br>
				</logic:iterate></td></logic:notEmpty>

				<logic:notEmpty name="records" property="modality">
				<td align="center" height="21" >
				<logic:iterate name="records" id="modality" property="modality"> <%=modality%>	
				<br>
				</logic:iterate></logic:notEmpty>

				<logic:notEmpty name="records" property="year">
				<bean:write name="records" property="year" /></logic:notEmpty>

				<logic:notEmpty name="records" property="ampId">
				<td align="center" height="21" >
				<bean:write name="records" property="ampId" /></td></logic:notEmpty>

				<logic:notEmpty name="records" property="ampFund">
				<% int i=0;%>
				<logic:iterate name="records"  property="ampFund" id="ampFund" 	type="org.digijava.module.aim.helper.AmpFund">
					<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">
						
						<c:if test="${addedMeasures.measureName == 'Actual Commitments'}">
						<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
						<td align="right" height="21" width="69" >
						<logic:notEqual name="ampFund" property="commAmount" value="0">
						<bean:write name="ampFund" property="commAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>

						<c:if test="${addedMeasures.measureName == 'Actual Disbursements'}">
						<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
						<td align="right" height="21" width="69">
						<logic:notEqual name="ampFund" property="disbAmount" value="0">
						<bean:write name="ampFund" property="disbAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>
						
						<c:if test="${addedMeasures.measureName == 'Actual Expenditures'}">
						<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
						<td align="right" height="21" width="69">
						<logic:notEqual name="ampFund" property="expAmount" value="0">
						<bean:write name="ampFund" property="expAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>
						
						<c:if test="${addedMeasures.measureName == 'Planned Commitments'}">
						<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
						<td align="right" height="21" width="69">
						<logic:notEqual name="ampFund" property="plCommAmount" value="0">
						<bean:write name="ampFund" property="plCommAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>
						
						<c:if test="${addedMeasures.measureName == 'Planned Disbursements'}">
						<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
						<td align="right" height="21" width="69">
						<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
						<bean:write name="ampFund" property="plDisbAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>
						
						<c:if test="${addedMeasures.measureName == 'Planned Expenditures'}">
						<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
						<td align="right" height="21" width="69">
						<logic:notEqual name="ampFund" property="plExpAmount" value="0">
						<bean:write name="ampFund" property="plExpAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>

						<c:if test="${addedMeasures.measureName == 'Cumulative Balance'}">
						
						<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
						<td align="right" height="21" width="69">
						<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
						<bean:write name="ampFund" property="unDisbAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						
						</c:if>
						
					</logic:iterate>
				</logic:iterate>	
				</logic:notEmpty>								
			</logic:iterate>
			</tr>
		</logic:iterate>
		
		<bean:size name="aimAdvancedReportForm" property="addedColumns" id="mSize"/>
					
		<tr background="#F4F4F2"><td align="right"> <b> Sub Total :</b></td>
		<td colspan="30">
		</td>
		<logic:iterate name="hierarchy"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
				
		<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
		<td align="right" height="21" width="69" >
		<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
		<b><bean:write name="fundSubTotal" property="commAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>
	
		<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
		<b><bean:write name="fundSubTotal" property="disbAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>
		
		<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
		<b><bean:write name="fundSubTotal" property="expAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
		<b><bean:write name="fundSubTotal" property="plCommAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
		<b><bean:write name="fundSubTotal" property="plDisbAmount" />	</b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
		<b><bean:write name="fundSubTotal" property="plExpAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>

		<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
		
		<td align="right" height="21" width="69" >
		<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
		<b><bean:write name="fundSubTotal" property="unDisbAmount" /></b>
		</logic:notEqual>
		</td>
		
		</logic:equal>
	</logic:iterate>
	</tr>																
	</logic:notEmpty>

	<logic:notEmpty name="hierarchy"  property="levels">
	<logic:iterate name="hierarchy"  property="levels" id="levels" type="org.digijava.module.aim.helper.AdvancedHierarchyReport">
		<tr bgcolor="#F4F4F2">
		<!--<td align="center" height="21" width="73" ><b>Sector</b></td>-->
		<td colspan=100 align="left" height="21" >
		<bean:write name="levels" property="label" /> :
		<b><u><bean:write name="levels" property="name" /></u></b>
		</td>
		</tr>
		
		<tr bgcolor="#cccccc">
		<c:if test="${!empty aimAdvancedReportForm.addedColumns}">
		<logic:iterate name="aimAdvancedReportForm" id="addedColumns"	property="addedColumns" >
		<td align="center"  class=box-title>
		<c:out value="${addedColumns.columnName}"/>
		</td>
		</logic:iterate>
		</c:if>
		<bean:size name="aimAdvancedReportForm" property="addedMeasures" id="measureSize"/>
		              
		
		<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">			<c:if test="${addedMeasures.measureName == 'Cumulative Balance'}">
			
			</c:if>
		</logic:iterate>
		
		
		<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
			<td height="21" width="69" colspan="30" align="center" >
			<strong><%=fiscalYearRange%></strong>
			</td>
		</logic:iterate>
		
	
		
		<td width="69" colspan=7 align=center >
		<b> Total 	</b>
		</td>
		</tr>
		<tr>
		<logic:iterate name="aimAdvancedReportForm"  property="addedColumns" id="addedColumns">
			<td>
			</td>
		</logic:iterate>
		
		<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
			<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">		<c:if test="${addedMeasures.measureName == 'Actual Commitments'}">
				<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
				<td height="21" width="23" align="center" >
				<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
				<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>
				</a>
				</td>
				</logic:equal>
				</c:if>

				<c:if test="${addedMeasures.measureName == 'Actual Disbursements'}">
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
				<td height="21" width="23" align="center" >
				<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
				<digi:trn key="aim:actualDisbursement">Actual Disbursement</digi:trn>
				</a>
				</td>
				</logic:equal>
				</c:if>
				
				<c:if test="${addedMeasures.measureName == 'Actual Expenditures'}">
				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
				<td height="21" width="23" align="center" >
				<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
				<digi:trn key="aim:actualExpenditure">Actual Expenditure</digi:trn>
				</a>
				</td>
				</logic:equal>
				</c:if>											

				<c:if test="${addedMeasures.measureName == 'Planned Commitments'}">
				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
				<td height="21" width="23" align="center" >
				<a title="<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>">
				<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>
				</a>
				</td>
				</logic:equal>
				</c:if>

				<c:if test="${addedMeasures.measureName == 'Planned Disbursements'}">
				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
				<td height="21" width="23" align="center" >
				<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
				<digi:trn key="aim:plannedDisbursement">Planned Disbursement</digi:trn>
				</a>
				</td>
				</logic:equal>
				</c:if>

				<c:if test="${addedMeasures.measureName == 'Planned Expenditures'}">
				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
				<td height="21" width="23" align="center" >
				<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
				<digi:trn key="aim:plannedExpenditure">Planned Expenditure</digi:trn>
				</a>
				</td>
				</logic:equal>
				</c:if>
			</logic:iterate>
		</logic:iterate>

		<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">			<c:if test="${addedMeasures.measureName == 'Actual Commitments'}">
			<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
			<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Actual Disbursements'}">
			<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
			<digi:trn key="aim:actualDisbursement">Actual Disbursement</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Actual Expenditures'}">
			<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
			<digi:trn key="aim:actualExpenditure">Actual Expenditure</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>											

			<c:if test="${addedMeasures.measureName == 'Planned Commitments'}">
			<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>">
			<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Planned Disbursements'}">
			<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
			<digi:trn key="aim:plannedDisbursement">Planned Disbursement</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Planned Expenditures'}">
			<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
			<td height="21" width="23" align="center" >
			<a title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
			<digi:trn key="aim:plannedExpenditure">Planned Expenditure</digi:trn>
			</a>
			</td>
			</logic:equal>
			</c:if>

			<c:if test="${addedMeasures.measureName == 'Cumulative Balance'}">
			<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
			<td height="21" width="23" align="center" >
			<digi:trn key="aim:cumulativeBalance">Cumulative Balance</digi:trn>
			</td>
			</logic:equal>
			</c:if>
		</logic:iterate>
	   	</tr>
		<logic:iterate name="levels"  property="project" id="project" type="org.digijava.module.aim.helper.Report">
			<tr bgcolor="#F4F4F2">
			<logic:iterate name="project"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">

				<logic:notEmpty name="records" property="title">
				<td align="center" height="21">
				<bean:write name="records" property="title" />
				</td>
				</logic:notEmpty>
				
				<logic:notEmpty name="records" property="actualStartDate">
				<td align="center" height="21">
				<bean:write name="records" property="actualStartDate" /></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="actualCompletionDate">
				<td align="center" height="21">
				<bean:write name="records" property="actualCompletionDate" /></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="status">
				<td align="center" height="21">
				<bean:write name="records" property="status" /></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="level">
				<td align="center" height="21">
				<bean:write name="records" property="level" /></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="objective">
				<td align="center" height="21">
				<bean:write name="records" property="objective" /></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="description">
				<td align="center" height="21" width="800">
				<bean:define id="descriptionKey">
				<bean:write name="records" property="description" />
				</bean:define>						
				<digi:edit key="<%=descriptionKey%>" />
				</td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="assistance">
				<td align="center" height="21">
				<logic:iterate name="records" id="assistance" property="assistance"> <%=assistance%>	
				<br>
				</logic:iterate></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="donors">
				<td align="center" height="21">
				<logic:iterate name="records" id="donors" property="donors"> <%=donors%>	
				<br>
				</logic:iterate></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="sectors">
				<td align="center" height="21">
				<logic:iterate name="records" id="sectors" property="sectors"> <%=sectors%>	
				<br>
				</logic:iterate></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="regions">
				<td align="center" height="21">
				<logic:iterate name="records" id="regions" property="regions"> <%=regions%>	
				<br>
				</logic:iterate></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="contacts">
				<td align="center" height="21">
				<logic:iterate name="records" id="contacts" property="contacts"> <%=contacts%>	
				<br>
				</logic:iterate></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="modality">
				<td align="center" height="21" >
				<logic:iterate name="records" id="modality" property="modality"> <%=modality%>	
				<br>
				</logic:iterate></logic:notEmpty>
				
				<logic:notEmpty name="records" property="year">
				<bean:write name="records" property="year" /></logic:notEmpty>
				
				<logic:notEmpty name="records" property="ampId">
				<td align="center" height="21" >
				<bean:write name="records" property="ampId" /></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="ampFund">
				<% int i=0;%>
				<logic:iterate name="records"  property="ampFund" id="ampFund" 	type="org.digijava.module.aim.helper.AmpFund">
					<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">
											
						<c:if test="${addedMeasures.measureName == 'Actual Commitments'}">
						<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
						<td align="right" height="21" width="69" >
						<logic:notEqual name="ampFund" property="commAmount" value="0">
						<bean:write name="ampFund" property="commAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>
						
						<c:if test="${addedMeasures.measureName == 'Actual Disbursements'}">
						<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
						<td align="right" height="21" width="69">
						<logic:notEqual name="ampFund" property="disbAmount" value="0">
						<bean:write name="ampFund" property="disbAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>
						
						<c:if test="${addedMeasures.measureName == 'Actual Expenditures'}">
						<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
						<td align="right" height="21" width="69">
						<logic:notEqual name="ampFund" property="expAmount" value="0">
						<bean:write name="ampFund" property="expAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>
						
						<c:if test="${addedMeasures.measureName == 'Planned Commitments'}">
						<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
						<td align="right" height="21" width="69">
						<logic:notEqual name="ampFund" property="plCommAmount" value="0">
						<bean:write name="ampFund" property="plCommAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>
						
						<c:if test="${addedMeasures.measureName == 'Planned Disbursements'}">
						<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
						<td align="right" height="21" width="69">
						<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
						<bean:write name="ampFund" property="plDisbAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>
						
						<c:if test="${addedMeasures.measureName == 'Planned Expenditures'}">
						<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
						<td align="right" height="21" width="69">
						<logic:notEqual name="ampFund" property="plExpAmount" value="0">
						<bean:write name="ampFund" property="plExpAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>

													
						<c:if test="${addedMeasures.measureName == 'Cumulative Balance'}">
						<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
						<td align="right" height="21" width="69">
						<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
						<bean:write name="ampFund" property="unDisbAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>
					</logic:iterate>
				</logic:iterate>
				</logic:notEmpty>								
			</logic:iterate>
			</tr>
		</logic:iterate>	

		<bean:size name="aimAdvancedReportForm" property="addedColumns" id="mSize"/>
					
		<tr background="#F4F4F2"><td align="right"> <b> Sub Sub Total :</b></td>
		<td colspan="30">
		</td>
		<logic:iterate name="levels"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
					
			<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
			<td align="right" height="21" width="69" >
			<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
			<b><bean:write name="fundSubTotal" property="commAmount" /></b>
			</logic:notEqual>
			</td>
			</logic:equal>

			<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
			<b><bean:write name="fundSubTotal" property="disbAmount" /></b>
			</logic:notEqual>
			</td>
			</logic:equal>

			<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
			<b><bean:write name="fundSubTotal" property="expAmount" /></b>
			</logic:notEqual>
			</td>
			</logic:equal>
			
			<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
			<b><bean:write name="fundSubTotal" property="plCommAmount" /></b>
			</logic:notEqual>
			</td>
			</logic:equal>

			<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
			<b><bean:write name="fundSubTotal" property="plDisbAmount" />	</b>
			</logic:notEqual>
			</td>
			</logic:equal>

			<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
			<b><bean:write name="fundSubTotal" property="plExpAmount" /></b>
			</logic:notEqual>
			</td>
			</logic:equal>

			<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
			
			<td align="right" height="21" width="69" >
			<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
			<b><bean:write name="fundSubTotal" property="unDisbAmount" /></b>
			</logic:notEqual>
			</td>
			
			</logic:equal>
		</logic:iterate>
		</tr>			
	</logic:iterate>
	
	<bean:size name="aimAdvancedReportForm" property="addedColumns" id="mSize"/>
	
	 <tr background="#F4F4F2"><td align="right"> <b> Sub Total :</b></td>
	 <td colspan="30">
	 </td>
	 <logic:iterate name="hierarchy"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
			
		<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
		<td align="right" height="21" width="69" >
		<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
		<b><bean:write name="fundSubTotal" property="commAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>
		
		<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
		<b><bean:write name="fundSubTotal" property="disbAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>
		
		<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
		<b><bean:write name="fundSubTotal" property="expAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>
		
		<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
		<b><bean:write name="fundSubTotal" property="plCommAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>
		
		<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
		<b><bean:write name="fundSubTotal" property="plDisbAmount" />	</b>
		</logic:notEqual>
		</td>
		</logic:equal>
		
		<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
		<b><bean:write name="fundSubTotal" property="plExpAmount" /></b>
		</logic:notEqual>
		</td>
		</logic:equal>
		
		<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
		
		<td align="right" height="21" width="69" >
		<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
		<b><bean:write name="fundSubTotal" property="unDisbAmount" /></b>
		</logic:notEqual>
		</td>
		
		</logic:equal>
	</logic:iterate>
	</tr>			
	</logic:notEmpty>
				
 </logic:iterate>

 <tr background="#F4F4F2"><td align="right"> <b> Grand Total :</b></td>
 <td colspan="30">
 </td>
 <logic:iterate name="multiReport"  property="fundTotal" id="fundTotal" type="org.digijava.module.aim.helper.AmpFund">
			
	<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
	<td align="right" height="21" width="69" >
	<logic:notEqual name="fundTotal" property="commAmount" value="0">
	<b><bean:write name="fundTotal" property="commAmount" /></b>
	</logic:notEqual>
	</td>
	</logic:equal>

	<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
	<td align="right" height="21" width="69">
	<logic:notEqual name="fundTotal" property="disbAmount" value="0">
	<b><bean:write name="fundTotal" property="disbAmount" /></b>
	</logic:notEqual>
	</td>
	</logic:equal>

	<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
	<td align="right" height="21" width="69">
	<logic:notEqual name="fundTotal" property="expAmount" value="0">
	<b><bean:write name="fundTotal" property="expAmount" /></b>
	</logic:notEqual>
	</td>
	</logic:equal>

	<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
	<td align="right" height="21" width="69">
	<logic:notEqual name="fundTotal" property="plCommAmount" value="0">
	<b><bean:write name="fundTotal" property="plCommAmount" /></b>
	</logic:notEqual>
	</td>
	</logic:equal>

	<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
	<td align="right" height="21" width="69">
	<logic:notEqual name="fundTotal" property="plDisbAmount" value="0">
	<b><bean:write name="fundTotal" property="plDisbAmount" />	</b>
	</logic:notEqual>
	</td>
	</logic:equal>
	
	<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
	<td align="right" height="21" width="69">
	<logic:notEqual name="fundTotal" property="plExpAmount" value="0">
	<b><bean:write name="fundTotal" property="plExpAmount" /></b>
	</logic:notEqual>
	</td>
	</logic:equal>

	<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
	
	<td align="right" height="21" width="69" >
	<logic:notEqual name="fundTotal" property="unDisbAmount" value="0">
	<b><bean:write name="fundTotal" property="unDisbAmount" /></b>
	</logic:notEqual>
	</td>
	
	</logic:equal>
</logic:iterate>
</logic:iterate>
</tr>			
</TABLE>
</td>
</tr>
</logic:notEmpty>
</logic:equal>
				<!-- end of hierarchy -->


												</TABLE>
												</TD>
												</c:if>
												<c:if test="${empty aimAdvancedReportForm.finalData}">
												<td align=center>
													<TABLE width="100%" height="100" cellPadding=2 cellSpacing=0 vAlign="top" align="center" bgcolor="#f4f4f2">
														<tr bgcolor="#eeeeee">	<td class=box-title align=center>
																<b>No Data present	</b>
														</td></tr>
													</table>
												</td>
												</c:if>
											</TR>
							                <logic:notEmpty name="aimAdvancedReportForm" property="pages">
												<tr>
												<td>
					
					<!-- -------------------------------  Prevoius Links     ---------------------------       -->
													<bean:define id="currPage" name="aimAdvancedReportForm" property="page" type="java.lang.Integer" /> 
													<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParams2}" property="page">
														<%=(currPage.intValue()-1)%>
													</c:set>
													<logic:notEqual name="aimAdvancedReportForm" property="page"
													value="1">
													  <bean:define id="translation">
															<digi:trn key="aim:clickToViewPreviousPage">Click here to view Previous page</digi:trn>
														</bean:define>
														<digi:link href="/advancedReportManager.do?check=5" name="urlParams2" title="<%=translation%>" >
															Previous
														</digi:link>
														&nbsp
													</logic:notEqual>
													
													<logic:equal name="aimAdvancedReportForm" property="page"
													value="1">
														<digi:trn key="aim:prev">Previous</digi:trn> &nbsp
													</logic:equal>	

													<logic:notEmpty name="aimAdvancedReportForm" property="finalData">
													  <bean:define id="translation">
															<digi:trn key="aim:clickToViewAllRecords">Click here to view All Records</digi:trn>
														</bean:define>
														<digi:link href="/advancedReportManager.do?check=5&page=all" title="<%=translation%>" >
															All
														</digi:link>
														&nbsp
													</logic:notEmpty>

					<!----------------------------------END   -----------------------------------------------     -->									
					
													
													<logic:iterate name="aimAdvancedReportForm" property="pages" id="pages" type="java.lang.Integer">
														<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams1}" property="page">
															<%=pages%>
														</c:set>
													
														<%  int curr = currPage.intValue();
															int cnt = pages.intValue();
															//logger.info(curr + " Comparison : " + cnt);
														%>
														<% if( curr != cnt ) { %>
														<bean:define id="translation">
															<digi:trn key="aim:clickToViewSpecificPages">Click here to view Specified Page</digi:trn>
														</bean:define>
														<digi:link href="/advancedReportManager.do?check=5" name="urlParams1" title="<%=translation%>" >
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
														
													<bean:define name="aimAdvancedReportForm" id="allPages" property="pages" 
													type="java.util.Collection" />
													<% if(allPages.size() == currPage.intValue()) { %>	
														&nbsp; <digi:trn key="aim:next">Next</digi:trn>  
													<% } else { %>
													  <bean:define id="translation">
															<digi:trn key="aim:clickToViewNextPage">Click here to go to Next page</digi:trn>
														</bean:define>
														<digi:link href="/advancedReportManager.do?check=5" name="urlParams3" title="<%=translation%>" >
															Next
														</digi:link>
														&nbsp;	
													<% } %>
					<!-- ------------------------------------------------------------------------------  -->									
													
												</td>
												</tr>
											</logic:notEmpty>					


											<tr bgcolor="#f4f4f2">
												<td align="center" colspan="2" bgcolor="#f4f4f2">
													<input type=button name=back value="<< Previous"   class="dr-menu" onclick="javascript:history.back()">
													<input type="button" name="Cancel" value=" Cancel " class="dr-menu" onclick="return quitAdvRptMngr()" >
													<input type=button name=next value="  Chart Creation  " class="dr-menu" onclick="javascript:gotoStep()" >
													<input type=button name=back value=" Save Report "   class="dr-menu" onclick="saveReport()">						
													
												</td>
											</tr>
										</TABLE>
									</TD>
								</TR>	
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</TD>
			</TR>
		</table>
	</td>	
	<td class=r-dotted-lg align=left vAlign=top >	&nbsp;</td>
</tr>
</table>
</td>	
</TR>
</TABLE>
</digi:form>