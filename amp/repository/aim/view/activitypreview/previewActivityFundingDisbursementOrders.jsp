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
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<digi:instance property="aimEditActivityForm" />

<c:set var="transaction" value="Disbursment Orders/Disbursment Orders Table" scope="page"/>
	<!-- Disbursement orders-->
	<tr bgcolor="#ffffff">
		<td height="7" colspan="5" bgcolor="#ffffff"></td>
	</tr>

	<tr bgcolor="#ffffff">
		<td colspan="5" bgcolor="#FFFFCC" style="text-transform: uppercase"><a
			title='<digi:trn jsFriendly="true" key="aim:FundRelease">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor </digi:trn>'>
		<digi:trn key="aim:actualdisbursementOrders">Actual Disbursment Orders</digi:trn>

		</a></td>
	</tr>

	<c:if test="${!empty funding.fundingDetails}">
		<logic:iterate name="funding" property="fundingDetails"
			id="fundingDetail"
			type="org.digijava.ampModule.aim.helper.FundingDetail">
			<logic:equal name="fundingDetail" property="transactionType"
				value="4">
				<logic:equal name="fundingDetail" property="adjustmentTypeName.value"
					value="Actual">
						<%@include file="previewActivityFundingDetail.jspf" %>
				</logic:equal>
			</logic:equal>
		</logic:iterate>
	</c:if>
	<tr>
		<td colspan="2" class="preview-funding-total">
            	<a
			title='<digi:trn jsFriendly="true" key="aim:FundRelease"> Release of funds to,
			or the purchase of goods or services for a recipient; by
			extension, the amount thus spent. Disbursements record the actual
			international transfer of financial resources, or of goods or
			services valued at the cost to the donor</digi:trn>'>
	  <digi:trn>Subtotal Actual Disbursment Orders</digi:trn> </a>
	</td>
	  <td nowrap="nowrap" class="preview-align preview-funding-total">
			<c:if test="${not empty funding.subtotalActualDisbursementsOrders}">
           			<span dir="ltr">${funding.subtotalActualDisbursementsOrders}</span> ${aimEditActivityForm.currCode}
            </c:if> &nbsp;
           </td>
	  <TD align="right" class="preview-funding-total">&nbsp;</TD>
  </tr>



	<tr bgcolor="#ffffff">
		<td height="7" colspan="5" bgcolor="#ffffff"></td>
	</tr>
