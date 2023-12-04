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
<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Estimated Disbursements"
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
<c:set var="transaction" value="Estimated Disbursements/Estimated Disbursements Table" scope="page"/>
<c:if test="${aimEditActivityForm.funding.showPlanned}">
<c:if test="${!empty funding.plannedEDDDetails}">
	<tr bgcolor="#ffffff">
		<td height="20" colspan="4" bgcolor="#FFFFCC" style="text-transform: uppercase">
            <a title='<digi:trn jsFriendly="true">Estimated Donor Disbursements</digi:trn>'>
	        	<digi:trn>Planned EDD</digi:trn>
		    </a>
        </td>
	</tr>
	
	<!-- Start Planned EDD -->
	<c:if test="${!empty funding.plannedEDDDetails}">	
	<logic:iterate name="funding" property="plannedEDDDetails"
		id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
			<%@include file="previewActivityFundingDetail.jspf" %>
	</logic:iterate>
	</c:if>
	
	<!-- End Planned EDD -->
	<tr>
		<td colspan="2" class="preview-funding-total">
			<digi:trn>Subtotal Planned EDD</digi:trn>
		</td>
		<td nowrap="nowrap" class="preview-align preview-funding-total">
			<c:if test="${not empty funding.subtotalPlannedEDD}">
          		  <b><span dir="ltr">${funding.subtotalPlannedEDD}</span> ${aimEditActivityForm.currCode}</b>
            </c:if>&nbsp;
			</td>
		<td class="preview-funding-total">&nbsp;</td>
	</tr>
	</c:if>
	</c:if>
	
	<c:if test="${aimEditActivityForm.funding.showActual}">
<c:if test="${!empty funding.actualEDDDetails}">
	
<tr><td colspan="4" height="7px"></td></tr>
	<tr bgcolor="#ffffff">
		<td colspan="4" bgcolor="#FFFFCC" style="text-transform: uppercase">
            <a title="">
                <digi:trn>Estimated Donor Disbursements</digi:trn>:
            </a>
        </td>
	</tr>

	<!-- Start Actual EDD -->
	<c:if test="${!empty funding.actualEDDDetails}">	
	<logic:iterate name="funding" property="actualEDDDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<%@include file="previewActivityFundingDetail.jspf" %>
	</logic:iterate>
	</c:if>
	<!-- End Actual EDD -->

	<tr>
		<td colspan="2" class="preview-funding-total">
            <digi:trn>Subtotal Actual EDD</digi:trn>:
		</td>
		<td nowrap="nowrap" class="preview-align preview-funding-total">
			 <c:if test="${not empty funding.subtotalActualEDD}">
                <b><span dir="ltr">${funding.subtotalActualEDD}</span> ${aimEditActivityForm.currCode}</b>
             </c:if>&nbsp;
        </td>
                   
		<td class="preview-funding-total">&nbsp;</td>
	</tr>
</c:if>
</c:if>

<c:if test="${aimEditActivityForm.funding.showPipeline}">
<c:if test="${!empty funding.pipelineEDDDetails}">
	
<tr><td colspan="4" height="7px"></td></tr>
	<tr bgcolor="#ffffff">
		<td colspan="4" bgcolor="#FFFFCC" 
			style="text-transform: uppercase; text-transform: uppercase"><digi:trn>Estimated Disbursements</digi:trn>:</td>
	</tr>

	<!-- Start Pipeline EDD -->
	<c:if test="${!empty funding.pipelineEDDDetails}">	
	<logic:iterate name="funding" property="pipelineEDDDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<%@include file="previewActivityFundingDetail.jspf" %>
	</logic:iterate>
	</c:if>
	<!-- End Actual EDD -->

	<tr>
		<td colspan="2" class="preview-funding-total"><digi:trn>Subtotal Pipeline EDD</digi:trn>:
		</td>
		<td nowrap="nowrap" class="preview-align preview-funding-total">
			 <c:if test="${not empty funding.subtotalPipelineEDD}">
                <b><span dir="ltr">${funding.subtotalPipelineEDD}</span> ${aimEditActivityForm.currCode}</b>
             </c:if>&nbsp;
        </td>
                   
		<td class="preview-funding-total">&nbsp;</td>
	</tr>
</c:if>
</c:if>

</module:display>



	<tr><td colspan="4" height="7px"></td></tr>


