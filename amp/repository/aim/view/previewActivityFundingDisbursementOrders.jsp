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

	<!-- Disbursement orders-->
	<!-- Comment added by mouhamad for burkina AMP-3361
	<tr bgcolor="#ffffff">
		<td height="25" colspan="5" bgcolor="#FFFFCC" style="text-transform: uppercase"><a
			title='<digi:trn key="aim:FundRelease">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor </digi:trn>'>
		<digi:trn key="aim:planneddisbursementOrders">PLANNED  DISBURSMENT ORDERS</digi:trn>

	  </a></td>
	</tr>

	<c:if test="${!empty funding.fundingDetails}">
		<logic:iterate name="funding" property="fundingDetails"
			id="fundingDetail"
			type="org.digijava.module.aim.helper.FundingDetail">
			<logic:equal name="fundingDetail" property="transactionType"
				value="4">
				<logic:equal name="fundingDetail" property="adjustmentType"
					value="0">

					<tr bgcolor="#ffffff">

						<td width="25%" align="right" bgcolor="#ffffff"><field:display
							name="Adjustment Type of Disbursement Order"
							feature="Disbursement Orders">
							<digi:trn
								key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
								<bean:write name="fundingDetail" property="adjustmentTypeName" />
							</digi:trn>
						</field:display></td>
						<td width="25%" align="right" bgcolor="#ffffff"><field:display
							name="Date of Disbursement Order" feature="Disbursement Orders">
							<bean:write name="fundingDetail" property="transactionDate" />
						</field:display></td>
						<td width="25%" align="right" bgcolor="#ffffff"><field:display
							name="Amount of Disbursement Order" feature="Disbursement Orders">
							<!--<FONT color=blue>*</FONT>-->
							<bean:write name="fundingDetail" property="transactionAmount" />
						</field:display><field:display name="Currency of Disbursement Order"
							feature="Disbursement Orders">
							<bean:write name="fundingDetail" property="currencyCode" />
						</field:display></td>


					
		                <td width="25%" bgcolor="#ffffff">&nbsp;</td>
				  </tr>
				</logic:equal>
			</logic:equal>
		</logic:iterate>
	</c:if>
	 -->
	<!-- tr>
		<td colspan="2" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000; text-transform: uppercase;"><digi:trn
			key='aim:totalPlannedDisbursementOrder'>
            TOTAL<a title='&lt;digi:trn key=&quot;aim:FundRelease&quot;&gt;Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor &lt;/digi:trn&gt;'>
			PLANNED <digi:trn key="aim:actualdisbursementOrders"> DISBURSMENT ORDERS</digi:trn>
			</a></digi:trn>
	  </td><TD align="right" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000; text-transform: uppercase;"></TD>
	    <TD align="right" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000; text-transform: uppercase;">&nbsp;</TD>
	</tr-->
	<tr bgcolor="#ffffff">
		<td height="7" colspan="5" bgcolor="#ffffff"></td>
	</tr>

	<tr bgcolor="#ffffff">
		<td colspan="5" bgcolor="#FFFFCC" style="text-transform: uppercase"><a
			title='<digi:trn key="aim:FundRelease">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor </digi:trn>'>
		<digi:trn key="aim:actualdisbursementOrders">ACTUAL DISBURSMENT ORDERS</digi:trn>

		</a></td>
	</tr>

	<c:if test="${!empty funding.fundingDetails}">
		<logic:iterate name="funding" property="fundingDetails"
			id="fundingDetail"
			type="org.digijava.module.aim.helper.FundingDetail">
			<logic:equal name="fundingDetail" property="transactionType"
				value="4">
				<logic:equal name="fundingDetail" property="adjustmentType"
					value="1">

					<tr bgcolor="#ffffff">

						<td width="25%" align="right" bgcolor="#ffffff"><field:display
							name="Adjustment Type of Disbursement Order"
							feature="Disbursement Orders">
							<digi:trn
								key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
								<bean:write name="fundingDetail" property="adjustmentTypeName" />
							</digi:trn>
						</field:display></td>
						<td width="25%" align="right" bgcolor="#ffffff"><field:display
							name="Date of Disbursement Order" feature="Disbursement Orders">
							<bean:write name="fundingDetail" property="transactionDate" />
						</field:display></td>
						<td width="25%" align="right" bgcolor="#ffffff"><field:display
							name="Amount of Disbursement Order" feature="Disbursement Orders">
							<!--<FONT color=blue>*</FONT>-->
							<bean:write name="fundingDetail" property="transactionAmount" />
						</field:display><field:display name="Currency of Disbursement Order"
							feature="Disbursement Orders">
							<bean:write name="fundingDetail" property="currencyCode" />
						</field:display> &nbsp;</td>


						<td width="25%" bgcolor="#ffffff">&nbsp;</td>
				  </tr>
				</logic:equal>
			</logic:equal>
		</logic:iterate>
	</c:if>
	<tr>
		<td colspan="2" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000; text-transform: uppercase;">
	<digi:trn
			key='aim:subtotalActualDisbursementOrder'>
            	<a
			title='<digi:trn key="aim:FundRelease"> Release of funds to,
			or the purchase of goods or services for a recipient; by
			extension, the amount thus spent. Disbursements record the actual
			international transfer of financial resources, or of goods or
			services valued at the cost to the donor</digi:trn>'>
	  SUBTOTAL ACTUAL DISBURSMENT ORDERS </a></digi:trn>
	</td>
	  <td nowrap="nowrap" align="right" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000; text-transform: uppercase;">
			<c:if test="${not empty funding.subtotalActualDisbursementsOrders}">
           			${funding.subtotalActualDisbursementsOrders} ${aimEditActivityForm.currCode}
            </c:if> &nbsp;
           </td>
	  <TD align="right" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000; text-transform: uppercase;">&nbsp;</TD>
  </tr>



	<tr bgcolor="#ffffff">
		<td height="7" colspan="5" bgcolor="#ffffff"></td>
	</tr>
