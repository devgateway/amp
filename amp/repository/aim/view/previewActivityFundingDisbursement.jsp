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
							<td align="right" bgcolor="#FFFFFF"><field:display
								name="Adjustment Type Disbursement"
								feature="Funding Information">
								<digi:trn
									key='<%="aim:disbursements:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
									<bean:write name="fundingDetail" property="adjustmentTypeName" />
								</digi:trn>
							</field:display></td>

							<td align="right"><field:display name="Date Disbursement"
								feature="Funding Information">
								<bean:write name="fundingDetail" property="transactionDate" />
							</field:display></td>
							<td align="right"><field:display
								name="Amount Disbursement" feature="Funding Information">
								<!--<FONT color=blue>*</FONT>-->
								<bean:write name="fundingDetail" property="transactionAmount" />
							</field:display> <field:display name="Currency Disbursement"
								feature="Funding Information">
								<bean:write name="fundingDetail" property="currencyCode" />
							</field:display>&nbsp;</td>
							<td height="18"><field:display
								name="Exchange Rate" feature="Funding Information">
								<bean:write name="fundingDetail" property="formattedRate" />
							</field:display></td>
						</tr>
			</logic:equal>
		</logic:equal>
	</logic:iterate>
	<!-- End Planned Disbursements -->
	
	<tr>
		<td colspan="2" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn
			key='aim:subtotalplanneddisbursement'>
                                                                                		    SUBTOTAL PLANNED DISBURSEMENT:	                                                                                	</digi:trn>
		</td>
		<td nowrap="nowrap" align="right" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000">
			<c:if test="${not empty funding.subtotalPlannedDisbursements}">
          		  ${funding.subtotalPlannedDisbursements} ${aimEditActivityForm.currCode}
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
	<logic:iterate name="funding" property="fundingDetails"
		id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<logic:equal name="fundingDetail" property="transactionType" value="1">
			<logic:equal name="fundingDetail" property="adjustmentType" value="1">
						<tr bgcolor="#ffffff">
							<td align="right" bgcolor="#FFFFFF"><field:display
								name="Adjustment Type Disbursement"
								feature="Funding Information">
								<digi:trn
									key='<%="aim:disbursements:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
									<bean:write name="fundingDetail" property="adjustmentTypeName" />
								</digi:trn>
							</field:display></td>

							<td align="right" align="right"><field:display name="Date Disbursement"
								feature="Funding Information">
								<bean:write name="fundingDetail" property="transactionDate" />
							</field:display></td>
							<td align="right"><field:display name="Amount Disbursement"
								feature="Funding Information">
								<!--<FONT color=blue>*</FONT>-->
								<bean:write name="fundingDetail" property="transactionAmount" />
							</field:display> <field:display name="Currency Disbursement"
								feature="Funding Information">
								<bean:write name="fundingDetail" property="currencyCode" />
							</field:display>&nbsp;</td>
							<td height="18"><field:display name="Exchange Rate"
								feature="Funding Information">
								<bean:write name="fundingDetail" property="formattedRate" />
							</field:display></td>
						</tr>
			</logic:equal>
		</logic:equal>
	</logic:iterate>
	<!-- End Actual Disbursements -->

	<tr>
		<td colspan="2" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000"><digi:trn
			key='aim:subtotalActualdisbursement'>
                                                                                    SUBTOTAL ACTUAL DISBURSEMENT </digi:trn>
		</td>
		<td nowrap="nowrap" align="right" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000">
			 <c:if test="${not empty funding.subtotalDisbursements}">
                                  ${funding.subtotalDisbursements} ${aimEditActivityForm.currCode}
             </c:if>&nbsp;
        </td>
                   
		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000">&nbsp;</td>
	</tr>
	<tr><td colspan="4" height="7px"></td></tr>
</c:if>

