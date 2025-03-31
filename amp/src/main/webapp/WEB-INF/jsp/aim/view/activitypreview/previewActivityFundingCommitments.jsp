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

<!--start commitments-->
<module:display name="/Activity Form/Funding/Funding Group/Funding Item/Commitments" 
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
<c:set var="transaction" value="Commitments/Commitments Table" scope="page"/>
<c:if test="${aimEditActivityForm.funding.showPlanned}">
	
<c:if test="${!empty funding.plannedCommitmentsDetails}">
	
	<tr bgcolor="#ffffff">
		<td height="20" colspan="3" valign="bottom" bgcolor="#FFFFCC"
			style="text-transform: uppercase;"><a
			title='<digi:trn jsFriendly="true" key="aim:PlannedCommitmentsmade">A firm obligation expressed in writing and backed by the necessary funds, undertaken by an official donor to provide specified assistance to a recipient country</digi:trn>'>
		<digi:trn key="aim:plannedcommitments">Planned Commitments </digi:trn>
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
		<logic:equal name="fundingDetail" property="transactionType" value="0">
			<logic:equal name="fundingDetail" property="adjustmentTypeName.value" value="Planned">
				<%@include file="previewActivityFundingDetail.jspf" %>
			</logic:equal>
		</logic:equal>
	</logic:iterate>
	</c:if>
	<tr>
		<td colspan="2" class="preview-funding-total">
			<digi:trn key='aim:subtotalplannedcommittment'> Subtotal Planned Commitments</digi:trn>:
		</td>
		<td colspan="2" nowrap="nowrap" class="preview-align preview-funding-total">
				<c:if test="${not empty funding.subtotalPlannedCommitments}">
                	<b><span dir="ltr">${funding.subtotalPlannedCommitments}</span> ${aimEditActivityForm.currCode}</b>
                </c:if>&nbsp;
            </td>
 	<td align="right" class="preview-funding-total">&nbsp;</td>
	</tr>
</c:if>
</c:if>	

<c:if test="${aimEditActivityForm.funding.showActual}">
	<c:if test="${!empty funding.actualCommitmentsDetails}">
	<tr><td colspan="4" height="7px"></td></tr>
	<tr>
		<td height="20" colspan="3" valign="bottom" bgcolor="#FFFFCC" style="text-transform: uppercase">
			<a title='<digi:trn jsFriendly="true" key="aim:PlannedCommitmentsmade">A firm obligation expressed in writing and backed by the necessary funds, undertaken by an official donor to provide specified assistance to a recipient country</digi:trn>'>
			<digi:trn key="aim:actualcommitments">Actual Commitments </digi:trn> </a>
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
			<logic:equal name="fundingDetail" property="adjustmentTypeName.value" value="Actual">
				<%@include file="previewActivityFundingDetail.jspf" %>
			</logic:equal>
		</logic:equal>
	</logic:iterate>
	</c:if>
	<tr>
		<td colspan="2" class="preview-funding-total"><digi:trn
			key='aim:subtotalactualcommittment'>Subtotal Actual Commitments </digi:trn>:
		</td>
		<td colspan="2" nowrap="nowrap" class="preview-align preview-funding-total">
			<c:if test="${not empty funding.subtotalActualCommitments}">
           		<b><span dir="ltr">${funding.subtotalActualCommitments}</span> ${aimEditActivityForm.currCode}</b>
            </c:if>&nbsp;
        </td>    
		<td class="preview-align preview-funding-total">&nbsp;</td>
	</tr>
	</c:if>
</c:if>	
	
<c:if test="${aimEditActivityForm.funding.showPipeline}">
	<c:if test="${!empty funding.pipelineCommitmentsDetails}">
	<tr><td colspan="4" height="7px"></td></tr>
		<!-- PIPELINE COMMITMENTS -->
        <tr>
            <td height="20" colspan="3" valign="bottom" bgcolor="#FFFFCC" style="text-transform: uppercase"><a>
                <digi:trn>Pipeline Commitments </digi:trn> </a>
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
				<digi:trn> Subtotal Pipeline Commitments</digi:trn>: 
			</td>
			<td colspan="2" nowrap="nowrap" class="preview-align preview-funding-total">
				<c:if test="${not empty funding.subtotalPipelineCommitments}">
					<b><span dir="ltr">${funding.subtotalPipelineCommitments}</span> ${aimEditActivityForm.currCode}</b>
				</c:if>&nbsp;
			</td>    
			<td class="preview-funding-total">&nbsp;</td>
        </tr>
        </c:if>
</c:if>	

<c:if test="${aimEditActivityForm.funding.showOfficialDevelopmentAid}">
	<c:if test="${!empty funding.officialDevelopmentAidCommitmentsDetails}">
	<tr><td colspan="4" height="7px"></td></tr>
		<!-- ODA COMMITMENTS -->
        <tr>
            <td height="20" colspan="3" valign="bottom" bgcolor="#FFFFCC" style="text-transform: uppercase"><a>
                <digi:trn>ODA Commitments</digi:trn> </a>
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
					<logic:equal name="fundingDetail" property="adjustmentTypeName.value" value="Official Development Aid">
						<%@include file="previewActivityFundingDetail.jspf" %>
					</logic:equal>
				</logic:equal>
        	</logic:iterate>
        </c:if>
        <tr>
			<td colspan="2" class="preview-funding-total">
				<digi:trn>Subtotal ODA Commitments</digi:trn>: 
			</td>
			<td colspan="2" nowrap="nowrap" class="preview-align preview-funding-total">
				<c:if test="${not empty funding.subtotalOfficialDevelopmentAidCommitments}">
					<b><span dir="ltr">${funding.subtotalOfficialDevelopmentAidCommitments}</span> ${aimEditActivityForm.currCode}</b>
				</c:if>&nbsp;
			</td>    
			<td>&nbsp;</td>
        </tr>
        </c:if>
</c:if>	

<c:if test="${aimEditActivityForm.funding.showBilateralSsc}">
	<c:if test="${!empty funding.bilateralSscCommitmentsDetails}">
	<tr><td colspan="4" height="7px"></td></tr>
		<!-- ODA COMMITMENTS -->
        <tr>
            <td height="20" colspan="3" valign="bottom" bgcolor="#FFFFCC" style="text-transform: uppercase"><a>
                <digi:trn>Bilateral SSC Commitments</digi:trn> </a>
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
					<logic:equal name="fundingDetail" property="adjustmentTypeName.value" value="Bilateral SSC">
						<%@include file="previewActivityFundingDetail.jspf" %>
					</logic:equal>
				</logic:equal>
        	</logic:iterate>
        </c:if>
        <tr>
			<td colspan="2" class="preview-funding-total">
				<digi:trn>Subtotal Bilateral SSC Commitments</digi:trn>: 
			</td>
			<td colspan="2" nowrap="nowrap" class="preview-align preview-funding-total">
				<c:if test="${not empty funding.subtotalBilateralSscCommitments}">
					<b><span dir="ltr">${funding.subtotalBilateralSscCommitments}</span> ${aimEditActivityForm.currCode}</b>
				</c:if>&nbsp;
			</td>    
			<td class="preview-funding-total">&nbsp;</td>
        </tr>
        </c:if>
</c:if>	
<c:if test="${aimEditActivityForm.funding.showTriangularSsc}">
	<c:if test="${!empty funding.triangularSscCommitmentsDetails}">
	<tr><td colspan="4" height="7px"></td></tr>
		<!-- ODA COMMITMENTS -->
        <tr>
            <td height="20" colspan="3" valign="bottom" bgcolor="#FFFFCC" style="text-transform: uppercase"><a>
                <digi:trn>Triangular SSC Commitments</digi:trn> </a>
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
					<logic:equal name="fundingDetail" property="adjustmentTypeName.value" value="Triangular SSC">
						<%@include file="previewActivityFundingDetail.jspf" %>
					</logic:equal>
				</logic:equal>
        	</logic:iterate>
        </c:if>
        <tr>
			<td colspan="2" class="preview-funding-total">
				<digi:trn>Subtotal Triangular SSC Commitments</digi:trn>: 
			</td>
			<td colspan="2" nowrap="nowrap" class="preview-align preview-funding-total">
				<c:if test="${not empty funding.subtotalTriangularSscCommitments}">
					<b><span dir="ltr">${funding.subtotalTriangularSscCommitments}</span> ${aimEditActivityForm.currCode}</b>
				</c:if>&nbsp;
			</td>    
			<td class="preview-align preview-funding-total">&nbsp;</td>
        </tr>
        </c:if>
</c:if>	
        <tr><td colspan="4" height="7px"></td></tr>
     </module:display>
<!-- End commitments-->
