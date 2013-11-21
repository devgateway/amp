<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/category" prefix="category"%>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Expenditures" 
		parentModule="/Activity Form/Funding/Funding Group/Funding Item">

<!-- expenditures -->
<digi:instance property="aimEditActivityForm" />

<c:if test="${aimEditActivityForm.funding.showPlanned}">
<c:if test="${!empty funding.plannedExpendituresDetails}">
<tr bgcolor="#FFFFCC">
	<td colspan="5" style="text-transform: uppercase"><a
		title='<digi:trn key="aim:ExpenditureofFund">Amount effectively spent by the implementing agency</digi:trn>'>

	<digi:trn key="aim:plannedexpenditures">Planned Expenditures</digi:trn>
	</a></td>
</tr>

<c:if test="${!empty funding.fundingDetails}">
	<logic:iterate name="funding" property="fundingDetails"
		id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<logic:equal name="fundingDetail" property="transactionType" value="2">
			<logic:equal name="fundingDetail" property="adjustmentTypeName.value" value="Planned">

						<tr bgcolor="#ffffff">
							<td align="right" bgcolor="#FFFFFF">
							<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table/Adjustment Type"
								parentModule="/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table">
								<b>
									<digi:trn key='<%="aim:expenditures:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
										<bean:write name="fundingDetail" property="adjustmentTypeName.value"/>
									</digi:trn>
								</b>
							</module:display>
							</td>
							<td align="right">
								<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table/Transaction Date"
									parentModule="/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table">
									<b><bean:write name="fundingDetail" property="transactionDate"/></b>
								</module:display>
							</td>

							<td align="right" colspan="2"><!-- no recipient organisation -->
								<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table/Amount"
									parentModule="/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table">
									<b><bean:write name="fundingDetail" property="transactionAmount"/></b>&nbsp;																								
								</module:display>
								<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table/Currency"
									parentModule="/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table">
									<b><bean:write name="fundingDetail" property="currencyCode"/></b>&nbsp;
								</module:display>
								</td>
							<td align="left" bgcolor="#ffffff"></td>
						</tr>
						<tr>
							<td colspan="4"></td>
						</tr>

			</logic:equal>
		</logic:equal>
	</logic:iterate>
</c:if>

	<tr>
		<td colspan="2" bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase">
			<digi:trn key="aim:subtotalActualExpenditures">Subtotal Planned Expenditures</digi:trn>:
		</td>
		<td colspan="2" nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000">
			<c:if test="${not empty funding.subtotalPlannedExpenditures}">
            	<b>${funding.subtotalPlannedExpenditures} ${aimEditActivityForm.currCode}</b>
            </c:if> &nbsp;
           </td>
		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000">&nbsp;</td>
	</tr>
	</c:if>
	</c:if>
	<tr>
		<td colspan="4" height="7px"></td>
	</tr>
	<c:if test="${aimEditActivityForm.funding.showActual}">
	<c:if test="${!empty funding.actualExpendituresDetails}">
	<tr bgcolor="#FFFFCC">
		<td colspan="5" style="text-transform: uppercase">
			<a title='<digi:trn key="aim:ExpenditureofFund">Amount effectively spent by the implementing agency</digi:trn>'>
				<digi:trn key="aim:actualexpenditures">Actual Expenditures</digi:trn>:
			</a>
		</td>
	</tr>
	<c:if test="${!empty funding.fundingDetails}">
	<logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
	<!--Actual-->
		<logic:equal name="fundingDetail" property="transactionType" value="2">
			<logic:equal name="fundingDetail" property="adjustmentTypeName.value" value="Actual">

						<tr bgcolor="#ffffff">
							<td align="right" bgcolor="#FFFFFF">
							<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table/Adjustment Type"
								parentModule="/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table">
								<digi:trn key='<%="aim:expenditures:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
									<b><bean:write name="fundingDetail" property="adjustmentTypeName.value"/></b>
								</digi:trn>
							</module:display>
							</td>
							<td align="right">
								<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table/Transaction Date"
									parentModule="/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table">
									<b><bean:write name="fundingDetail" property="transactionDate"/></b>
								</module:display>
							</td>
							<td align="right">
								<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table/Amount"
										parentModule="/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table">
									<b><bean:write name="fundingDetail" property="transactionAmount"/></b>&nbsp;
								</module:display>		
								<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table/Currency"
										parentModule="/Activity Form/Funding/Funding Group/Funding Item/Expenditures/Expenditures Table">
									<b><bean:write name="fundingDetail" property="currencyCode"/></b>
								</module:display>&nbsp;
							</td>
							<td align="left" bgcolor="#ffffff"></td>

						</tr>
						<field:display name="Classification Expenditure" feature="Funding Information">
							<tr>
								<td colspan="4">
									<b><bean:write name="fundingDetail" property="classification"/></b>
								</td>
							</tr>
						</field:display>

			</logic:equal>
		</logic:equal>
	</logic:iterate>
	</c:if>
	<tr>
		<td colspan="2" bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase">
			<digi:trn key="aim:subtotalplannedExpenditures">Subtotal Actual Expenditures</digi:trn>:
		</td>
		<td colspan="2" nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000">
			<c:if test="${not empty funding.subtotalExpenditures}">
 				<b>${funding.subtotalExpenditures} ${aimEditActivityForm.currCode}</b>
 			</c:if> &nbsp;
           </td>
		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000">&nbsp;</td>
	</tr>
	</c:if>
	</c:if>
	<tr>
		<td colspan="4" height="7px"></td>
	</tr>

</module:display>
<!-- expenditures -->

