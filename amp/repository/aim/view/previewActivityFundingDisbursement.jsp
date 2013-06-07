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
<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursements" 
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
<c:if test="${aimEditActivityForm.funding.showPlanned}">
<c:if test="${!empty funding.plannedDisbursementDetails}">
	<tr bgcolor="#ffffff">
		<td height="20" colspan="4" bgcolor="#FFFFCC"
			style="text-transform: uppercase"><a
			title='<digi:trn key="aim:FundRelease">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor </digi:trn>'>
	
		<digi:trn key="aim:planneddisbursements">Planned Disbursement</digi:trn>
		</a></td>
	</tr>
	
	<!-- Start Planned Disbursements -->
	<c:if test="${!empty funding.fundingDetails}">	
	<logic:iterate name="funding" property="fundingDetails"
		id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<logic:equal name="fundingDetail" property="transactionType" value="1">
			<logic:equal name="fundingDetail" property="adjustmentTypeName.value" value="Planned">
			
						<tr bgcolor="#ffffff">
							<td align="right" bgcolor="#FFFFFF">
								<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Adjustment Type"
									parentModule="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table">
									<digi:trn key='<%="aim:disbursements:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
										<b><bean:write name="fundingDetail" property="adjustmentTypeName.value" /></b>
									</digi:trn>
								</module:display>
							</td>
							<td align="right">
								<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Transaction Date"
									parentModule="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table">
									<b><bean:write name="fundingDetail" property="transactionDate" /></b>
								</module:display>
							</td>
							<td align="right">
								<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Amount"
									parentModule="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table">
									<b><bean:write name="fundingDetail" property="transactionAmount" /></b>
								</module:display>
								<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Currency"
									parentModule="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table">
										<b><bean:write name="fundingDetail" property="currencyCode" /></b>
								</module:display>
								&nbsp;</td>
							<td height="18">
							<c:if test="${aimEditActivityForm.funding.fixerate == true}">
								<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/exchangeRate"
									parentModule="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table">
								<b><bean:write name="fundingDetail" property="formattedRate" /></b>
								</module:display>
							</c:if>
							</td>
						</tr>
			</logic:equal>
		</logic:equal>
	</logic:iterate>
	</c:if>
	
	<!-- End Planned Disbursements -->
	<tr>
		<td colspan="2" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000; text-transform: uppercase">
			<digi:trn key='aim:subtotalplanneddisbursement'>Subtotal Planned Disbursement</digi:trn>:
		</td>
		<td nowrap="nowrap" align="right" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000">
			<c:if test="${not empty funding.subtotalPlannedDisbursements}">
          		  <b>${funding.subtotalPlannedDisbursements} ${aimEditActivityForm.currCode}</b>
            </c:if>&nbsp;
			</td>
		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000">&nbsp;</td>
	</tr>
	</c:if>
	</c:if>
	
	<c:if test="${aimEditActivityForm.funding.showActual}">
<c:if test="${!empty funding.actualDisbursementDetails}">
	
<tr><td colspan="4" height="7px"></td></tr>
	<tr bgcolor="#ffffff">
		<td colspan="4" bgcolor="#FFFFCC" 
			style="text-transform: uppercase; text-transform: uppercase"><digi:trn
			key="aim:actualisbursements">Actual Disbursement</digi:trn>:</td>
	</tr>

	<!-- Start Actual Disbursements -->
	<c:if test="${!empty funding.fundingDetails}">	
	<logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<logic:equal name="fundingDetail" property="transactionType" value="1">
			<logic:equal name="fundingDetail" property="adjustmentTypeName.value" value="Actual">
						<tr bgcolor="#ffffff">
							<td align="right" bgcolor="#FFFFFF">
								<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Adjustment Type"
									parentModule="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table">	
										<digi:trn key='<%="aim:disbursements:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
											<b><bean:write name="fundingDetail" property="adjustmentTypeName.value" /></b>
										</digi:trn>
								</module:display>
							</td>

							<td align="right" align="right">
								<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Transaction Date"
									parentModule="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table">	
										<b><bean:write name="fundingDetail" property="transactionDate" /></b>
								</module:display>								
							</td>
							<td align="right">
								<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Amount"
									parentModule="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table">
									<b><bean:write name="fundingDetail" property="transactionAmount" /></b>
								</module:display>
								<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Currency"
									parentModule="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table">
									<b><bean:write name="fundingDetail" property="currencyCode" /></b>&nbsp;
								</module:display>
							</td>
							<td height="18">
							<c:if test="${aimEditActivityForm.funding.fixerate == true}">
								<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/exchangeRate"
									parentModule="/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table">
								<b><bean:write name="fundingDetail" property="formattedRate" /></b>
								</module:display>
							</c:if>
							</td>
						</tr>
			</logic:equal>
		</logic:equal>
	</logic:iterate>
	</c:if>
	<!-- End Actual Disbursements -->

	<tr>
		<td colspan="2" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000"><digi:trn
			key='aim:subtotalActualdisbursement'>Subtotal Actual Disbursement </digi:trn>:
		</td>
		<td nowrap="nowrap" align="right" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000">
			 <c:if test="${not empty funding.subtotalDisbursements}">
                <b>${funding.subtotalDisbursements} ${aimEditActivityForm.currCode}</b>
             </c:if>&nbsp;
        </td>
                   
		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000">&nbsp;</td>
	</tr>
	</c:if>
	</c:if>
	</module:display>



	<tr><td colspan="4" height="7px"></td></tr>


