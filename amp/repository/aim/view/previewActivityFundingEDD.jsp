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
<module:display name="/Activity Form/Funding/Funding Group/Funding Item/EDD" 
														parentModule="/Activity Form/Funding/Funding Group/Funding Item">
<c:if test="${aimEditActivityForm.funding.showPlanned}">
<c:if test="${!empty funding.plannedEDDDetails}">
	<tr bgcolor="#ffffff">
		<td height="20" colspan="4" bgcolor="#FFFFCC"
			style="text-transform: uppercase"><a
			title='<digi:trn>Estimated Donor Disbursements</digi:trn>'>
	
		<digi:trn>Planned EDD</digi:trn>
		</a></td>
	</tr>
	
	<!-- Start Planned EDD -->
	<c:if test="${!empty funding.plannedEDDDetails}">	
	<logic:iterate name="funding" property="plannedEDDDetails"
		id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<tr bgcolor="#ffffff">
							<td align="right" bgcolor="#FFFFFF">
								<b>
									<digi:trn><bean:write name="fundingDetail" property="adjustmentTypeName.value" /></digi:trn>
								</b>
							</td>
							<td align="right">
								<b><bean:write name="fundingDetail" property="transactionDate" /></b>
							</td>
							<td align="right">
								<b><bean:write name="fundingDetail" property="transactionAmount" /></b>
								<b><bean:write name="fundingDetail" property="currencyCode" /></b>
								&nbsp;
							</td>
							<td height="18">
								<c:if test="${aimEditActivityForm.funding.fixerate == true}">								
									<b><bean:write name="fundingDetail" property="formattedRate" /></b>
								</c:if>
							</td>
		</tr>
	</logic:iterate>
	</c:if>
	
	<!-- End Planned EDD -->
	<tr>
		<td colspan="2" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000; text-transform: uppercase">
			<digi:trn>Subtotal Planned EDD</digi:trn>
		</td>
		<td nowrap="nowrap" align="right" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000">
			<c:if test="${not empty funding.subtotalPlannedEDD}">
          		  <b>${funding.subtotalPlannedEDD} ${aimEditActivityForm.currCode}</b>
            </c:if>&nbsp;
			</td>
		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000">&nbsp;</td>
	</tr>
	</c:if>
	</c:if>
	
	<c:if test="${aimEditActivityForm.funding.showActual}">
<c:if test="${!empty funding.actualEDDDetails}">
	
<tr><td colspan="4" height="7px"></td></tr>
	<tr bgcolor="#ffffff">
		<td colspan="4" bgcolor="#FFFFCC" 
			style="text-transform: uppercase; text-transform: uppercase"><digi:trn>Estimated Donor Disbursements</digi:trn>:</td>
	</tr>

	<!-- Start Actual EDD -->
	<c:if test="${!empty funding.actualEDDDetails}">	
	<logic:iterate name="funding" property="actualEDDDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
						<tr bgcolor="#ffffff">
							<td align="right" bgcolor="#FFFFFF">
								<b>
									<digi:trn><bean:write name="fundingDetail" property="adjustmentTypeName.value" /></digi:trn>
								</b>										
							</td>

							<td align="right" align="right">
								<b><bean:write name="fundingDetail" property="transactionDate" /></b>								
							</td>
							<td align="right">
								<b><bean:write name="fundingDetail" property="transactionAmount" /></b>
								
								<b><bean:write name="fundingDetail" property="currencyCode" /></b>&nbsp;
																
								<logic:present name="fundingDetail" property="recipientOrganisation">
									&nbsp;&nbsp;[<b><bean:write name="fundingDetail" property="recipientOrganisation.name" /></b> as <b><bean:write name="fundingDetail" property="recipientOrganisationRole.name" /></b>]&nbsp;
								</logic:present>
							</td>
							<td height="18">
								<c:if test="${aimEditActivityForm.funding.fixerate == true}">
									<b><bean:write name="fundingDetail" property="formattedRate" /></b>
								</c:if>
							</td>
						</tr>
	</logic:iterate>
	</c:if>
	<!-- End Actual EDD -->

	<tr>
		<td colspan="2" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000"><digi:trn>Subtotal Actual EDD</digi:trn>:
		</td>
		<td nowrap="nowrap" align="right" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000">
			 <c:if test="${not empty funding.subtotalActualEDD}">
                <b>${funding.subtotalActualEDD} ${aimEditActivityForm.currCode}</b>
             </c:if>&nbsp;
        </td>
                   
		<td bgcolor="#eeeeee" style="border-top: 1px solid #000000">&nbsp;</td>
	</tr>
	</c:if>
	</c:if>
	</module:display>



	<tr><td colspan="4" height="7px"></td></tr>


