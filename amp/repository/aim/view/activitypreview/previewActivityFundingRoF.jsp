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
<ampModule:display name="/Activity Form/Funding/Funding Group/Funding Item/Release of Funds"
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
<c:set var="transaction" value="Release of Funds/Release of Funds Table" scope="page"/>
<c:if test="${aimEditActivityForm.funding.showPlanned}">
<c:if test="${!empty funding.plannedRoFDetails}">
	<tr bgcolor="#ffffff">
		<td height="20" colspan="4" bgcolor="#FFFFCC"
			style="text-transform: uppercase"><a
			title='<digi:trn jsFriendly="true">Release of funds to</digi:trn>'>
	
		<digi:trn>Planned Release of Funds</digi:trn>
		</a></td>
	</tr>
	
	<!-- Start Planned Disbursements -->
	<c:if test="${!empty funding.plannedRoFDetails}">	
	<logic:iterate name="funding" property="plannedRoFDetails"
		id="fundingDetail" type="org.digijava.ampModule.aim.helper.FundingDetail">
		<%@include file="previewActivityFundingDetail.jspf" %>
	</logic:iterate>
	</c:if>
	
	<!-- End Planned RoF -->
	<tr>
		<td colspan="2" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000; text-transform: uppercase">
			<digi:trn>Subtotal Planned Release of Funds</digi:trn>
		</td>
		<td nowrap="nowrap" class="preview-align preview-funding-total" bgcolor="#eeeeee">
			<c:if test="${not empty funding.subtotalPlannedRoF}">
          		  <b><span dir="ltr">${funding.subtotalPlannedRoF}</span> ${aimEditActivityForm.currCode}</b>
            </c:if>&nbsp;
			</td>
		<td bgcolor="#eeeeee" class="preview-align preview-funding-total">&nbsp;</td>
	</tr>
	</c:if>
	</c:if>
	
<c:if test="${aimEditActivityForm.funding.showActual}">
<c:if test="${!empty funding.actualRoFDetails}">
	
<tr><td colspan="4" height="7px"></td></tr>
	<tr bgcolor="#ffffff">
		<td colspan="4" bgcolor="#FFFFCC" style="text-transform: uppercase">
            <a title="">
                <digi:trn>Release of Funds</digi:trn>:
            </a>
        </td>
	</tr>

	<!-- Start Actual Release of Funds -->
	<c:if test="${!empty funding.actualRoFDetails}">	
	<logic:iterate name="funding" property="actualRoFDetails" id="fundingDetail" type="org.digijava.ampModule.aim.helper.FundingDetail">
		<%@include file="previewActivityFundingDetail.jspf" %>
	</logic:iterate>
	</c:if>
	<!-- End Actual RoF -->

	<tr>
		<td colspan="2" bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase">
            <digi:trn>Subtotal Actual RoF</digi:trn>:
		</td>
		<td nowrap="nowrap" class="preview-align preview-funding-total" bgcolor="#eeeeee">
			 <c:if test="${not empty funding.subtotalActualRoF}">
                <b><span dir="ltr">${funding.subtotalActualRoF}</span> ${aimEditActivityForm.currCode}</b>
             </c:if>&nbsp;
        </td>
                   
		<td bgcolor="#eeeeee" class="preview-align preview-funding-total">&nbsp;</td>
	</tr>
</c:if>
</c:if>

<c:if test="${aimEditActivityForm.funding.showPipeline}">
<c:if test="${!empty funding.pipelineRoFDetails}">
	
<tr><td colspan="4" height="7px"></td></tr>
	<tr bgcolor="#ffffff">
		<td colspan="4" bgcolor="#FFFFCC" 
			style="text-transform: uppercase; text-transform: uppercase"><digi:trn>Release of Funds</digi:trn>:</td>
	</tr>

	<!-- Start Pipeline Release of Funds -->
	<c:if test="${!empty funding.pipelineRoFDetails}">	
	<logic:iterate name="funding" property="pipelineRoFDetails" id="fundingDetail" type="org.digijava.ampModule.aim.helper.FundingDetail">
		<%@include file="previewActivityFundingDetail.jspf" %>
	</logic:iterate>
	</c:if>
	<!-- End Actual RoF -->

	<tr>
		<td colspan="2" bgcolor="#eeeeee" class="preview-funding-total"><digi:trn>Subtotal Pipeline RoF</digi:trn>:
		</td>
		<td nowrap="nowrap" class="preview-align preview-funding-total" bgcolor="#eeeeee">
			 <c:if test="${not empty funding.subtotalPipelineRoF}">
                <b><span dir="ltr">${funding.subtotalPipelineRoF}</span> ${aimEditActivityForm.currCode}</b>
             </c:if>&nbsp;
        </td>
                   
		<td bgcolor="#eeeeee" class="preview-funding-total">&nbsp;</td>
	</tr>
</c:if>
</c:if>

</ampModule:display>



	<tr><td colspan="4" height="7px"></td></tr>


