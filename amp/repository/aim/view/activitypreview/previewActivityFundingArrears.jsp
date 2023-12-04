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

<!--start arrears -->
<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Arrears"
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
<c:set var="transaction" value="Arrears/Arrears Table" scope="page"/>
<c:if test="${aimEditActivityForm.funding.showPlanned}">
	
<c:if test="${!empty funding.plannedArrearsDetails}">
	
	<tr bgcolor="#ffffff">
		<td height="20" colspan="3" valign="bottom" bgcolor="#FFFFCC"
			style="text-transform: uppercase;"><a
			title='<digi:trn jsFriendly="true" key="aim:PlannedArreartsmade">PLANNED ARREARS DESCRIPTION TEXT</digi:trn>'>
		<digi:trn key="aim:plannedarrears">Planned Arrears </digi:trn>
		</a></td>
		<td height="20" bgcolor="#FFFFCC" align="center">
			<c:if test="${aimEditActivityForm.funding.fixerate == true}">
				<b> <digi:trn key="aim:exchange">Exchange Rate</digi:trn> </b>
			</c:if>
		</td>
	</tr>
 	

	
	<c:if test="${!empty funding.fundingDetails}">
	<logic:iterate name="funding" property="fundingDetails"
		id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<logic:equal name="fundingDetail" property="transactionType" value="10">
			<logic:equal name="fundingDetail" property="adjustmentTypeName.value" value="Planned">
				<%@include file="previewActivityFundingDetail.jspf" %>
			</logic:equal>
		</logic:equal>
	</logic:iterate>
	</c:if>
	<tr>
		<td colspan="2" class="preview-funding-total">
			<digi:trn key='aim:subtotalplannedarrears'>Subtotal Planned Arrears</digi:trn>:
		</td>
		<td colspan="2" nowrap="nowrap" class="preview-align preview-funding-total">
				<c:if test="${not empty funding.subtotalPlannedArrears}">
                	<b><span dir="ltr">${funding.subtotalPlannedArrears}</span> ${aimEditActivityForm.currCode}</b>
                </c:if>&nbsp;
            </td>
 	<td class="preview-funding-total">&nbsp;</td>
	</tr>
</c:if>
</c:if>	

<c:if test="${aimEditActivityForm.funding.showActual}">
	<c:if test="${!empty funding.actualArrearsDetails}">
	<tr><td colspan="4" height="7px"></td></tr>
	<tr>
		<td height="20" colspan="3" valign="bottom" bgcolor="#FFFFCC" style="text-transform: uppercase">
			<a title='<digi:trn jsFriendly="true" key="aim:ActualArrearsmade">ACTUAL ARREARS DESCRIPTION</digi:trn>'>
				<digi:trn key="aim:actualArrears">Actual Arrears</digi:trn> 
			</a>
		</td>
		<td height="20" bgcolor="#FFFFCC" align="center">
			<c:if test="${aimEditActivityForm.funding.fixerate == true}">
				<b> <digi:trn key="aim:exchange">Exchange Rate</digi:trn> </b>
			</c:if>
		</td>
	</tr>
	<c:if test="${!empty funding.fundingDetails}">
	<logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<logic:equal name="fundingDetail" property="transactionType" value="10">
			<logic:equal name="fundingDetail" property="adjustmentTypeName.value" value="Actual">
				<%@include file="previewActivityFundingDetail.jspf" %>
			</logic:equal>
		</logic:equal>
	</logic:iterate>
	</c:if>
	<tr>
		<td colspan="2" class="preview-funding-total"><digi:trn
			key='aim:subtotalactualarrears'>Subtotal Actual Arrears</digi:trn>:
		</td>
		<td colspan="2" nowrap="nowrap" class="preview-align preview-funding-total">
			<c:if test="${not empty funding.subtotalActualArrears}">
           		<b><span dir="ltr">${funding.subtotalActualArrears}</span> ${aimEditActivityForm.currCode}</b>
            </c:if>&nbsp;
        </td>    
		<td class="preview-funding-total">&nbsp;</td>
	</tr>
	</c:if>
</c:if>	
	
<c:if test="${aimEditActivityForm.funding.showPipeline}">
	<c:if test="${!empty funding.pipelineArrearsDetails}">
	<tr><td colspan="4" height="7px"></td></tr>
		<!-- PIPELINE ARREARS -->
        <tr>
            <td height="20" colspan="3" valign="bottom" bgcolor="#FFFFCC" style="text-transform: uppercase"><a>
                <digi:trn>Pipeline Arrears</digi:trn> </a>
			</td>
			<td height="20" bgcolor="#FFFFCC" align="center">
				<c:if test="${aimEditActivityForm.funding.fixerate == true}">
					<b> <digi:trn key="aim:exchange">Exchange Rate</digi:trn> </b>
				</c:if>
			</td>
        </tr>
        <c:if test="${!empty funding.fundingDetails}">
        	<logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
				<logic:equal name="fundingDetail" property="transactionType" value="0">
					<logic:equal name="fundingDetail" property="adjustmentTypeName.value" value="Pipeline">
						<%@include file="previewActivityFundingDetail.jspf" %>
					</logic:equal>
				</logic:equal>
        	</logic:iterate>
        </c:if>
        <tr>
			<td colspan="2" class="preview-funding-total">
				<digi:trn> Subtotal Pipeline Arrears</digi:trn>: 
			</td>
			<td colspan="2" nowrap="nowrap" class="preview-align preview-funding-total">
				<c:if test="${not empty funding.subtotalPipelineArrears}">
					<b><span dir="ltr">${funding.subtotalPipelineArrears}</span> ${aimEditActivityForm.currCode}</b>
				</c:if>&nbsp;
			</td>    
			<td class="preview-funding-total">&nbsp;</td>
        </tr>
        </c:if>
</c:if>	


        <tr><td colspan="4" height="7px"></td></tr>
     </module:display>
<!-- End arrears-->
