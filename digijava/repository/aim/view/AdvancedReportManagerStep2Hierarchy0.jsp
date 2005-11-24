<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimAdvancedReportForm" />
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
	<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">				
	<c:if test="${addedMeasures.measureName == 'Cumulative Balance'}">
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

<!-- for colspan -->
		 <bean:size name="aimAdvancedReportForm" property="addedColumns" id="cols"/>					
	<tr background="#F4F4F2"><td align="right" colspan=<bean:write name="cols"/> > <b> Sub Total :</b></td>
<!--	<td colspan="<%=cnt%>"> </td> -->
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