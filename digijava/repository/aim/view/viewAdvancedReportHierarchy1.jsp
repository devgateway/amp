<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimAdvancedReportForm" />
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
                    <logic:equal name="aimAdvancedReportForm" property="option" value="A">	
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
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="option" value="Q">	
                    <tr bgcolor="#F4F4F2">
					<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
					<td align="center" height="21" width="42" rowspan="2" ><div align="center"><strong>
					<bean:write name="titles" property="columnName" /></strong></div>
					</td></logic:iterate>
					<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
						<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="quarterColumns" /> align="center"><strong><%=fiscalYearRange%></strong>
						</td>
					</logic:iterate>
					
					
				<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="fundColumns" /> align="center" rowspan="2">
					<b>Total 	</b>
				</td>
			
				</tr>
				<tr bgcolor="#F4F4F2">
				<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
					<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q1</strong></td>
					<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q2</strong></td>
					<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q3</strong></td>
					<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q4</strong></td>
				</logic:iterate>
				</tr>
				</logic:equal>
					<tr bgcolor="#FFFFFF">
					<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
						<td>&nbsp;</td>
					</logic:iterate>
					<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
					<logic:iterate name="aimAdvancedReportForm"  property="options" id="options">
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
					
								<td align="right" height="21" width="69">
								<logic:notEqual name="ampFund" property="commAmount" value="0">
								<bean:write name="ampFund" property="commAmount" />
								</logic:notEqual>
								</td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" 	value="true">
					
								<td align="right" height="21" width="69">
								<logic:notEqual name="ampFund" property="disbAmount" value="0">
								<bean:write name="ampFund" property="disbAmount" />
								</logic:notEqual>
								</td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="acExpFlag" 	value="true">
					
								<td align="right" height="21" width="69">
								<logic:notEqual name="ampFund" property="expAmount" value="0">
								<bean:write name="ampFund" property="expAmount" />
								</logic:notEqual>
								</td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
					
								<td align="right" height="21" width="69">
								<logic:notEqual name="ampFund" property="plCommAmount" value="0">
								<bean:write name="ampFund" property="plCommAmount" />
								</logic:notEqual>
								</td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
					
								<td align="right" height="21" width="69">
								<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
								<bean:write name="ampFund" property="plDisbAmount" />
								</logic:notEqual>
								</td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
					
								<td align="right" height="21" width="69" >
								<logic:notEqual name="ampFund" property="plExpAmount" value="0">
								<bean:write name="ampFund" property="plExpAmount" />
								</logic:notEqual>
								</td>
								</logic:equal>
								<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
								<logic:notEmpty name="ampFund" property="unDisbAmount">
								<td align="right" height="21" width="69">
								<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
								<bean:write name="ampFund" property="unDisbAmount" />
								</logic:notEqual>
								</td></logic:notEmpty>
								</logic:equal>
							</logic:iterate></logic:notEmpty>
						</logic:iterate>
						</tr>
					</logic:iterate>


			  <tr><td align="left" colspan=<bean:write name="aimAdvancedReportForm" property="dimColumns" />><b> <bean:write name="hierarchy" property="name" /> &nbsp;Total</b></td>
			  <logic:iterate name="hierarchy"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">

			  <logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
							<bean:write name="fundSubTotal" property="commAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
			
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
							<bean:write name="fundSubTotal" property="disbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>

				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
							<bean:write name="fundSubTotal" property="expAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>

				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
							<bean:write name="fundSubTotal" property="plCommAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>

				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
							<bean:write name="fundSubTotal" property="plDisbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>

				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
							<bean:write name="fundSubTotal" property="plExpAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				
				<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
					<logic:notEmpty name="fundSubTotal" property="unDisbAmount">				
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
							<bean:write name="fundSubTotal" property="unDisbAmount" />
							</logic:notEqual>
						</td>
					</logic:notEmpty>	
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
    <logic:equal name="aimAdvancedReportForm" property="option" value="A">	
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
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="option" value="Q">	
                    <tr bgcolor="#F4F4F2">
					<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
					<td align="center" height="21" width="42" rowspan="2" ><div align="center"><strong>
					<bean:write name="titles" property="columnName" /></strong></div>
					</td></logic:iterate>
					<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
						<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="quarterColumns" /> align="center"><strong><%=fiscalYearRange%></strong>
						</td>
					</logic:iterate>
					
					
				<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="fundColumns" /> align="center" rowspan="2">
					<b>Total 	</b>
				</td>
			
				</tr>
				<tr bgcolor="#F4F4F2">
				<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
					<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q1</strong></td>
					<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q2</strong></td>
					<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q3</strong></td>
					<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q4</strong></td>
				</logic:iterate>
				</tr>
				</logic:equal>
	<tr bgcolor="#FFFFFF">
	<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
	<td>&nbsp;</td>
	</logic:iterate>
	<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
	<logic:iterate name="aimAdvancedReportForm"  property="options" id="options">
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

				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="commAmount" value="0">
				<bean:write name="ampFund" property="commAmount" />
				</logic:notEqual>
				</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" 	value="true">

				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="disbAmount" value="0">
				<bean:write name="ampFund" property="disbAmount" />
				</logic:notEqual>
				</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" 	value="true">

				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="expAmount" value="0">
				<bean:write name="ampFund" property="expAmount" />
				</logic:notEqual>
				</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">

				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="plCommAmount" value="0">
				<bean:write name="ampFund" property="plCommAmount" />
				</logic:notEqual>
				</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">

				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
				<bean:write name="ampFund" property="plDisbAmount" />
				</logic:notEqual>
				</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">

				<td align="right" height="21" width="69" >
				<logic:notEqual name="ampFund" property="plExpAmount" value="0">
				<bean:write name="ampFund" property="plExpAmount" />
				</logic:notEqual>
				</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
				<logic:notEmpty name="ampFund" property="unDisbAmount">
				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
				<bean:write name="ampFund" property="unDisbAmount" />
				</logic:notEqual>
				</td>
				</logic:notEmpty>
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
					
		<logic:equal name="aimAdvancedReportForm" property="option" value="A">	
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
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="option" value="Q">	
                    <tr bgcolor="#F4F4F2">
					<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
					<td align="center" height="21" width="42" rowspan="2" ><div align="center"><strong>
					<bean:write name="titles" property="columnName" /></strong></div>
					</td></logic:iterate>
					<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
						<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="quarterColumns" /> align="center"><strong><%=fiscalYearRange%></strong>
						</td>
					</logic:iterate>
					
					
				<td height="21" width="69" colspan=<bean:write name="aimAdvancedReportForm" property="fundColumns" /> align="center" rowspan="2">
					<b>Total 	</b>
				</td>
			
				</tr>
				<tr bgcolor="#F4F4F2">
				<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
					<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q1</strong></td>
					<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q2</strong></td>
					<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q3</strong></td>
					<td height="21" colspan=<bean:write name="aimAdvancedReportForm" property="measureCount" /> align="center"><strong>Q4</strong></td>
				</logic:iterate>
				</tr>
				</logic:equal>
		<tr bgcolor="#FFFFFF">
		<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
			<td>&nbsp;</td>
		</logic:iterate>
					
		<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
		<logic:iterate name="aimAdvancedReportForm"  property="options" id="options">
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

					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="commAmount" value="0">
					<bean:write name="ampFund" property="commAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" 	value="true">

					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="disbAmount" value="0">
					<bean:write name="ampFund" property="disbAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acExpFlag" 	value="true">

					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="expAmount" value="0">
					<bean:write name="ampFund" property="expAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">

					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="plCommAmount" value="0">
					<bean:write name="ampFund" property="plCommAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">

					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
					<bean:write name="ampFund" property="plDisbAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">

					<td align="right" height="21" width="69" >
					<logic:notEqual name="ampFund" property="plExpAmount" value="0">
					<bean:write name="ampFund" property="plExpAmount" />
					</logic:notEqual>
					</td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
					<logic:notEmpty name="ampFund" property="unDisbAmount">
					<td align="right" height="21" width="69">
					<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
					<bean:write name="ampFund" property="unDisbAmount" />
					</logic:notEqual>
					</td>
					</logic:notEmpty>
					</logic:equal>
				</logic:iterate></logic:notEmpty>
			</logic:iterate>
			</tr>
		</logic:iterate>

		<tr><td align="left" colspan=<bean:write name="aimAdvancedReportForm" property="dimColumns" />><b> <bean:write name="level" property="name" /> &nbsp;Total</b></td>
		<logic:iterate name="level"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
			<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">

			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
			<bean:write name="fundSubTotal" property="commAmount" />
			</logic:notEqual>
			</td>
			</logic:equal>
			<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">

			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
			<bean:write name="fundSubTotal" property="disbAmount" />
			</logic:notEqual>
			</td>
			</logic:equal>
			<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">

			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
			<bean:write name="fundSubTotal" property="expAmount" />
			</logic:notEqual>
			</td>
			</logic:equal>
			<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">

			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
			<bean:write name="fundSubTotal" property="plCommAmount" />
			</logic:notEqual>
			</td>
			</logic:equal>
			<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">

			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
			<bean:write name="fundSubTotal" property="plDisbAmount" />
			</logic:notEqual>
			</td>
			</logic:equal>
			<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">

			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
			<bean:write name="fundSubTotal" property="plExpAmount" />
			</logic:notEqual>
			</td>
			</logic:equal>
			<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
			<logic:notEmpty name="fundSubTotal" property="unDisbAmount">
			<td align="right" height="21" width="69">
			<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
			<bean:write name="fundSubTotal" property="unDisbAmount" />
			</logic:notEqual>
			</td>
			</logic:notEmpty>
			</logic:equal>
		</logic:iterate></tr>
	</logic:iterate>
	</logic:notEmpty>

	<tr><td align="left" colspan=<bean:write name="aimAdvancedReportForm" property="dimColumns" />><b> <bean:write name="levels" property="name" /> &nbsp;Total</b></td>
	<logic:iterate name="levels"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
		<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">

		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
		<bean:write name="fundSubTotal" property="commAmount" />
		</logic:notEqual>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">

		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
		<bean:write name="fundSubTotal" property="disbAmount" />
		</logic:notEqual>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">

		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
		<bean:write name="fundSubTotal" property="expAmount" />
		</logic:notEqual>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">

		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
		<bean:write name="fundSubTotal" property="plCommAmount" />
		</logic:notEqual>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">

		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
		<bean:write name="fundSubTotal" property="plDisbAmount" />
		</logic:notEqual>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">

		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
		<bean:write name="fundSubTotal" property="plExpAmount" />
		</logic:notEqual>
		</td>
		</logic:equal>
		<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
		<logic:notEmpty name="fundSubTotal" property="unDisbAmount">
		<td align="right" height="21" width="69">
		<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
		<bean:write name="fundSubTotal" property="unDisbAmount" />
		</logic:notEqual>
		</td>
		</logic:notEmpty>
	</logic:equal>
	</logic:iterate></tr>


<!-- code ends for hierarchy 3 -->

</logic:iterate>

<tr><td align="left" colspan=<bean:write name="aimAdvancedReportForm" property="dimColumns" />><b> <bean:write name="hierarchy" property="name" /> &nbsp;Total</b></td>
			  <logic:iterate name="hierarchy"  property="fundSubTotal" id="fundSubTotal" type="org.digijava.module.aim.helper.AmpFund">
			  <logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">

						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="commAmount" value="0">
							<bean:write name="fundSubTotal" property="commAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">

						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="disbAmount" value="0">
							<bean:write name="fundSubTotal" property="disbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">

						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="expAmount" value="0">
							<bean:write name="fundSubTotal" property="expAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">

						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="plCommAmount" value="0">
							<bean:write name="fundSubTotal" property="plCommAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">

						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="plDisbAmount" value="0">
							<bean:write name="fundSubTotal" property="plDisbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">

						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="plExpAmount" value="0">
							<bean:write name="fundSubTotal" property="plExpAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
				<logic:notEmpty name="fundSubTotal" property="unDisbAmount">
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundSubTotal" property="unDisbAmount" value="0">
							<bean:write name="fundSubTotal" property="unDisbAmount" />
							</logic:notEqual>
						</td>
				</logic:notEmpty>		
				</logic:equal>
				</logic:iterate></tr>

			       
				</logic:notEmpty>
				
			 </logic:iterate>

			  <tr><td align="left" colspan=<bean:write name="aimAdvancedReportForm" property="dimColumns" />><b> Grand Total</b></td>
			  <logic:iterate name="multiReport"  property="fundTotal" id="fundTotal" type="org.digijava.module.aim.helper.AmpFund">
			  <logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">

						<td align="right" height="21" width="69">
							<logic:notEqual name="fundTotal" property="commAmount" value="0">
							<bean:write name="fundTotal" property="commAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">

						<td align="right" height="21" width="69">
							<logic:notEqual name="fundTotal" property="disbAmount" value="0">
							<bean:write name="fundTotal" property="disbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">

						<td align="right" height="21" width="69">
							<logic:notEqual name="fundTotal" property="expAmount" value="0">
							<bean:write name="fundTotal" property="expAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">

						<td align="right" height="21" width="69">
							<logic:notEqual name="fundTotal" property="plCommAmount" value="0">
							<bean:write name="fundTotal" property="plCommAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">

						<td align="right" height="21" width="69">
							<logic:notEqual name="fundTotal" property="plDisbAmount" value="0">
							<bean:write name="fundTotal" property="plDisbAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">

						<td align="right" height="21" width="69">
							<logic:notEqual name="fundTotal" property="plExpAmount" value="0">
							<bean:write name="fundTotal" property="plExpAmount" />
							</logic:notEqual>
						</td>
				</logic:equal>
				<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
				<logic:notEmpty name="fundTotal" property="unDisbAmount">				
						<td align="right" height="21" width="69">
							<logic:notEqual name="fundTotal" property="unDisbAmount" value="0">
							<bean:write name="fundTotal" property="unDisbAmount" />
							</logic:notEqual>
						</td>
				</logic:notEmpty>		
				</logic:equal>
				</logic:iterate>
				   
				</logic:iterate>	
					
				</tr>
		</logic:notEmpty>
	
                     </table>
