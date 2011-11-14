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
<digi:instance property="aimEditActivityForm" />


<field:display name="Planned Disbursement Preview" feature="Disbursement">
	<tr bgcolor="#ffffff">
		<td height="20" colspan="4" bgcolor="#FFFFCC"
			style="text-transform: uppercase"><a
			title='<digi:trn key="aim:FundRelease">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor </digi:trn>'>
	
		<digi:trn key="aim:planneddisbursements">PLANNED DISBURSEMENT</digi:trn>
		</a></td>
	</tr>
</field:display>

<c:if test="${!empty funding.fundingDetails}">

	<field:display name="Planned Disbursement Preview" feature="Disbursement">
	
	<!-- Start Planned Disbursements -->
	<logic:iterate name="funding" property="fundingDetails"
		id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<logic:equal name="fundingDetail" property="transactionType" value="1">
			<logic:equal name="fundingDetail" property="adjustmentType" value="0">
						<tr bgcolor="#ffffff">
							<td align="right" bgcolor="#FFFFFF">
								<module:display name="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table/Adjustment Type"
									parentModule="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table">
									<digi:trn key='<%="aim:disbursements:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
										<b><bean:write name="fundingDetail" property="adjustmentTypeName" /></b>
									</digi:trn>
								</module:display>
							</td>
							<td align="right">
								<module:display name="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table/Transaction Date"
									parentModule="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table">
									<b><bean:write name="fundingDetail" property="transactionDate" /></b>
								</module:display>
							</td>
							<td align="right">
								<module:display name="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table/Amount"
									parentModule="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table">
									<b><bean:write name="fundingDetail" property="transactionAmount" /></b>
								</module:display>
								<module:display name="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table/Currency"
									parentModule="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table">
										<b><bean:write name="fundingDetail" property="currencyCode" /></b>
								</module:display>
								&nbsp;</td>
							<td height="18">
							<!-- Exchange Rate not found -->
							<c:if test="${aimEditActivityForm.funding.fixerate == true}">
								<module:display name="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table/Exchange Rate"
									parentModule="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table">
								<b><bean:write name="fundingDetail" property="formattedRate" /></b>
								</module:display>
							</c:if>
							</td>
						</tr>
			</logic:equal>
		</logic:equal>
	</logic:iterate>
	<!-- End Planned Disbursements -->
	
	<tr>
		<td colspan="2" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000; text-transform: uppercase">
			<digi:trn key='aim:subtotalplanneddisbursement'>SUBTOTAL PLANNED DISBURSEMENT:</digi:trn>
		</td>
		<td nowrap="nowrap" align="right" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000">
			<c:if test="${not empty funding.subtotalPlannedDisbursements}">
          		  <b>${funding.subtotalPlannedDisbursements} ${aimEditActivityForm.currCode}</b>
            </c:if>&nbsp;
			</td>
		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000">&nbsp;</td>
	</tr>
	
	</field:display>
	
<tr><td colspan="4" height="7px"></td></tr>
	<tr bgcolor="#ffffff">
		<td colspan="4" bgcolor="#FFFFCC"
			style="text-transform: uppercase; text-transform: uppercase"><digi:trn
			key="aim:actualisbursements">ACTUAL DISBURSEMENT</digi:trn></td>
	</tr>

	<!-- Start Actual Disbursements -->
	<logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<logic:equal name="fundingDetail" property="transactionType" value="1">
			<logic:equal name="fundingDetail" property="adjustmentType" value="1">
						<tr bgcolor="#ffffff">
							<td align="right" bgcolor="#FFFFFF">
								<module:display name="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table/Adjustment Type"
									parentModule="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table">	
										<digi:trn key='<%="aim:disbursements:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
											<b><bean:write name="fundingDetail" property="adjustmentTypeName" /></b>
										</digi:trn>
								</module:display>
							</td>

							<td align="right" align="right">
								<module:display name="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table/Transaction Date"
									parentModule="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table">	
										<b><bean:write name="fundingDetail" property="transactionDate" /></b>
								</module:display>								
							</td>
							<td align="right">
								<module:display name="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table/Amount"
									parentModule="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table">
									<b><bean:write name="fundingDetail" property="transactionAmount" /></b>
								</module:display>
								<module:display name="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table/Currency"
									parentModule="/Activity Form/Donor Funding/Funding Item/Disbursements/Disbursements Table">
									<b><bean:write name="fundingDetail" property="currencyCode" /></b>&nbsp;
								</module:display>
							</td>
							<td height="18">
								<field:display name="Exchange Rate" feature="Funding Information">
									<b><bean:write name="fundingDetail" property="formattedRate" /></b>
								</field:display>
							</td>
						</tr>
			</logic:equal>
		</logic:equal>
	</logic:iterate>
	<!-- End Actual Disbursements -->

	<tr>
		<td colspan="2" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000"><digi:trn
			key='aim:subtotalActualdisbursement'>SUBTOTAL ACTUAL DISBURSEMENT </digi:trn>
		</td>
		<td nowrap="nowrap" align="right" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000">
			 <c:if test="${not empty funding.subtotalDisbursements}">
                <b>${funding.subtotalDisbursements} ${aimEditActivityForm.currCode}</b>
             </c:if>&nbsp;
        </td>
                   
		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000">&nbsp;</td>
	</tr>
	<tr><td colspan="4" height="7px"></td></tr>
</c:if>

