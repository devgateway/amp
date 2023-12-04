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
<c:set var="transaction" value="Disbursements/Disbursements Table" scope="page"/>
<c:if test="${aimEditActivityForm.funding.showPlanned}">
<c:if test="${!empty funding.plannedDisbursementDetails}">
	<tr bgcolor="#ffffff">
		<td height="20" colspan="3" valign="bottom" bgcolor="#FFFFCC" style="text-transform: uppercase;">
			<b><a title='<digi:trn jsFriendly="true" key="aim:FundRelease">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor </digi:trn>'>
	        	<digi:trn key="aim:planneddisbursements">Planned Disbursement</digi:trn>
		    </a></b>
        </td>
		<td height="20" bgcolor="#FFFFCC" align="center">
			<c:if test="${aimEditActivityForm.funding.fixerate == true}">
				<b> <digi:trn key="aim:exchange">Exchange Rate</digi:trn> </b>
			</c:if>
		</td>
	</tr>
	
	<!-- Start Planned Disbursements -->
	<c:if test="${!empty funding.fundingDetails}">	
	<logic:iterate name="funding" property="fundingDetails"
		id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<logic:equal name="fundingDetail" property="transactionType" value="1">
			<logic:equal name="fundingDetail" property="adjustmentTypeName.value" value="Planned">			
				<%@include file="previewActivityFundingDetail.jspf" %>
			</logic:equal>
		</logic:equal>
	</logic:iterate>
	</c:if>

	<!-- End Planned Disbursements -->
	<tr>
		<td colspan="2" class="preview-funding-total">
			<digi:trn key='aim:subtotalplanneddisbursement'>Subtotal Planned Disbursement</digi:trn>:
		</td>
		<td colspan="2" nowrap="nowrap" class="preview-align preview-funding-total">
			<c:if test="${not empty funding.subtotalPlannedDisbursements}">
          		  <b><span dir="ltr">${funding.subtotalPlannedDisbursements}</span> ${aimEditActivityForm.currCode}</b>
            </c:if>&nbsp;
			</td>
		<td class="preview-funding-total">&nbsp;</td>
	</tr>
	</c:if>
	</c:if>
	
	<c:if test="${aimEditActivityForm.funding.showActual}">
    <c:if test="${!empty funding.actualDisbursementDetails}">
	
    <tr>
        <td colspan="4" height="7px"></td>
    </tr>
	<tr bgcolor="#ffffff">
		<td height="20" colspan="3" valign="bottom" bgcolor="#FFFFCC" style="text-transform: uppercase;">
			<b><digi:trn key="aim:actualisbursements">Actual Disbursement</digi:trn>:</b>
        </td>
		<td height="20" bgcolor="#FFFFCC" align="center">
			<c:if test="${aimEditActivityForm.funding.fixerate == true}">
				<b> <digi:trn key="aim:exchange">Exchange Rate</digi:trn> </b>
			</c:if>
		</td>
	</tr>

	<!-- Start Actual Disbursements -->
	<c:if test="${!empty funding.fundingDetails}">	
	<logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<logic:equal name="fundingDetail" property="transactionType" value="1">
			<logic:equal name="fundingDetail" property="adjustmentTypeName.value" value="Actual">
				<%@include file="previewActivityFundingDetail.jspf" %>
			</logic:equal>
		</logic:equal>
	</logic:iterate>
	</c:if>
	<!-- End Actual Disbursements -->

	<tr>
		<td colspan="2" class="preview-funding-total">
            <digi:trn key='aim:subtotalActualdisbursement'>Subtotal Actual Disbursement</digi:trn>:
		</td>
		<td colspan="2" nowrap="nowrap" class="preview-align preview-funding-total">
			 <c:if test="${not empty funding.subtotalDisbursements}">
                <b><span dir="ltr">${funding.subtotalDisbursements}</span> ${aimEditActivityForm.currCode}</b>
             </c:if>&nbsp;
        </td>
                   
		<td class="preview-funding-total">&nbsp;</td>
	</tr>
	</c:if>
	</c:if>
	
	<c:if test="${aimEditActivityForm.funding.showPipeline}">
<c:if test="${!empty funding.pipelineDisbursementDetails}">
	
<tr><td colspan="4" height="7px"></td></tr>
	<tr bgcolor="#ffffff">
		<td height="20" colspan="3" valign="bottom" bgcolor="#FFFFCC" style="text-transform: uppercase">
			<digi:trn>Pipeline Disbursement</digi:trn>:
		</td>
		<td height="20" bgcolor="#FFFFCC" align="center">
			<c:if test="${aimEditActivityForm.funding.fixerate == true}">
				<b> <digi:trn key="aim:exchange">Exchange Rate</digi:trn> </b>
			</c:if>
		</td>
	</tr>

	<!-- Start Pipeline Disbursements -->
	<c:if test="${!empty funding.fundingDetails}">	
	<logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<logic:equal name="fundingDetail" property="transactionType" value="1">
			<logic:equal name="fundingDetail" property="adjustmentTypeName.value" value="Pipeline">
				<%@include file="previewActivityFundingDetail.jspf" %>
			</logic:equal>
		</logic:equal>
	</logic:iterate>
	</c:if>
	<!-- End Pipeline Disbursements -->

	<tr>
		<td colspan="2" class="preview-funding-total"><digi:trn>Subtotal Pipeline Disbursement </digi:trn>:
		</td>
		<td colspan="2" nowrap="nowrap" class="preview-align preview-funding-total">
			 <c:if test="${not empty funding.subtotalDisbursements}">
                <b><span dir="ltr">${funding.subtotalPipelineDisbursements}</span> ${aimEditActivityForm.currCode}</b>
             </c:if>&nbsp;
        </td>
                   
		<td class="preview-funding-total">&nbsp;</td>
	</tr>
	</c:if>
	</c:if>
	
	<tr><td colspan="4" height="7px"></td></tr>
</module:display>






