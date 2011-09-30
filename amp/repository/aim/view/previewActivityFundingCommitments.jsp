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
<feature:display name="Planned Commitments" module="Measures">
	<tr bgcolor="#ffffff">
		<td height="20" colspan="3" valign="bottom" bgcolor="#FFFFCC"
			style="text-transform: uppercase;"><a
			title='<digi:trn key="aim:PlannedCommitmentsmade">A firm obligation expressed in writing and backed by the necessary funds, undertaken by an official donor to provide specified assistance to a recipient country</digi:trn>'>
		<digi:trn key="aim:plannedcommitments">PLANNED COMMITMENTS </digi:trn>
		</a></td>
		<td bgcolor="#FFFFCC">
			<c:if test="${aimEditActivityForm.funding.fixerate == true}">
				<b> <digi:trn key="aim:exchange">Exchange Rate</digi:trn> </b>
			</c:if>
		</td>
	</tr>
</feature:display>
<c:if test="${!empty funding.fundingDetails}">
<module:display name="/Activity Form/Donor Funding/Funding Item/Commitments/Add Commitment" 
	parentModule="/Activity Form/Donor Funding/Funding Item/Commitments">
	<logic:iterate name="funding" property="fundingDetails"
		id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<logic:equal name="fundingDetail" property="transactionType" value="0">
			<logic:equal name="fundingDetail" property="adjustmentType" value="0">
						<tr bgcolor="#ffffff">
							<td height="18" width="40%" align="right" bgcolor="#ffffff">
								<field:display name="Adjustment Type Commitment" feature="Commitments">
								<digi:trn key='<%="aim:commitments:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
									<b><bean:write name="fundingDetail" property="adjustmentTypeName" /></b>
								</digi:trn>
							</field:display>
							</td>
							<td height="18" align="right">
								<field:display name="Date Commitment" feature="Commitments">
									<b><bean:write name="fundingDetail" property="transactionDate"/></b>
								</field:display>
							</td>
							<td height="18" align="right" bgcolor="#ffffff">
							<field:display name="Amount Commitment" feature="Commitments">
								<!-- <font color="blue">*</font> -->
								<b><bean:write name="fundingDetail" property="transactionAmount" /></b>
							</field:display> 
								<field:display name="Currency Commitment" feature="Commitments">
									<b><bean:write name="fundingDetail" property="currencyCode"/></b>
								</field:display> &nbsp;</td>
							<td height="18">
								<field:display name="Exchange Rate" feature="Funding Information">
									<b><bean:write name="fundingDetail" property="formattedRate" /></b>
								</field:display>
							</td>
						</tr>
			</logic:equal>
		</logic:equal>
	</logic:iterate>
	<tr>
		<td colspan="2" bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase;">
			<digi:trn key='aim:subtotalplannedcommittment'> SUBTOTAL PLANNED COMMITMENTS:</digi:trn>
		</td>
		<td nowrap="nowrap" align="right" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000">
				<c:if test="${not empty funding.subtotalPlannedCommitments}">
                	<b>${funding.subtotalPlannedCommitments} ${aimEditActivityForm.currCode}</b>
                </c:if>&nbsp;
            </td>
      		<td align="right" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000">&nbsp;</td>
	</tr>
	</module:display>

	<tr><td colspan="4" height="7px"></td></tr>
	<tr>
		<td height="20" colspan="3" valign="bottom" bgcolor="#FFFFCC" style="text-transform: uppercase">
			<a title='<digi:trn key="aim:PlannedCommitmentsmade">A firm obligation expressed in writing and backed by the necessary funds, undertaken by an official donor to provide specified assistance to a recipient country</digi:trn>'>
			<digi:trn key="aim:actualcommitments">ACTUAL COMMITMENTS </digi:trn> </a>
		</td>
		<td height="20" bgcolor="#FFFFCC">
			<c:if test="${aimEditActivityForm.funding.fixerate == true}">
				<b> <digi:trn key="aim:exchange">Exchange Rate</digi:trn> </b>
			</c:if>
		</td>
	</tr>
	<logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<logic:equal name="fundingDetail" property="transactionType" value="0">
			<logic:equal name="fundingDetail" property="adjustmentType" value="1">
						<tr bgcolor="#ffffff">
							<td width="40%" align="right"  bgcolor="#FFFFFF">
							<module:display name="/Activity Form/Donor Funding/Funding Item/Commitments/Commitments Table/Adjustment Type"
								parentModule="/Activity Form/Donor Funding/Funding Item/Commitments/Commitments Table">
								<digi:trn key='<%="aim:commitments:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
									<b><bean:write name="fundingDetail" property="adjustmentTypeName" /></b>
								</digi:trn>
							</module:display>
							</td>
							<td height="18" align="right">
								<module:display name="/Activity Form/Donor Funding/Funding Item/Commitments/Commitments Table/Transaction Date"
									parentModule="/Activity Form/Donor Funding/Funding Item/Commitments/Commitments Table">
									<b><bean:write name="fundingDetail" property="transactionDate" /></b>
								</module:display>
							</td>
							<td height="18" align="right">
							<module:display name="/Activity Form/Donor Funding/Funding Item/Commitments/Commitments Table/Amount" 
								parentModule="/Activity Form/Donor Funding/Funding Item/Commitments/Commitments Table">
								<b><bean:write name="fundingDetail" property="transactionAmount" /></b>
							</module:display>
							<module:display name="/Activity Form/Donor Funding/Funding Item/Commitments/Commitments Table/Currency"
								parentModule="/Activity Form/Donor Funding/Funding Item/Commitments/Commitments Table">
								<b><bean:write name="fundingDetail" property="currencyCode" /></b>
							</module:display> &nbsp;
							</td>
							<!--Exchange Rate not found-->
							<td height="18">
								<field:display name="Exchange Rate" feature="Funding Information">
									<b><bean:write name="fundingDetail" property="formattedRate" /></b>
								</field:display>
							</td>
						</tr>
			</logic:equal>
		</logic:equal>
	</logic:iterate>
	<tr>
		<td colspan="2" bgcolor="#eeeeee"
			style="border-top: 1px solid #000000; text-transform: uppercase"><digi:trn
			key='aim:subtotalactualcommittment'> SUBTOTAL ACTUAL COMMITMENTS: </digi:trn>
		</td>
		<td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000;">
			<c:if test="${not empty funding.subtotalActualCommitments}">
           		<b>${funding.subtotalActualCommitments} ${aimEditActivityForm.currCode}</b>
            </c:if>&nbsp;
        </td>    
		<td align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="4" height="7px"></td></tr>
		<!-- PIPELINE COMMITMENTS -->
        <tr>
        	<td colspan="4" height="7px"></td>
        </tr>
        <tr>
            <td height="20" colspan="3" valign="bottom" bgcolor="#FFFFCC" style="text-transform: uppercase"><a>
                <digi:trn>PIPELINE COMMITMENTS </digi:trn> </a>
                </td>
                <td height="20" bgcolor="#FFFFCC">
                	<c:if test="${aimEditActivityForm.funding.fixerate == true}">
                        <b> <digi:trn key="aim:exchange">Exchange Rate</digi:trn> </b>
                	</c:if>
                </td>
        </tr>
        <logic:iterate name="funding" property="fundingDetails"
                id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
                <logic:equal name="fundingDetail" property="transactionType" value="0">
                        <logic:equal name="fundingDetail" property="adjustmentType" value="2">
                                                <tr bgcolor="#ffffff">
                                                        <td width="40%" align="right"  bgcolor="#FFFFFF">
                                                                <field:display name="Adjustment Type Commitment" feature="Commitments">
                                                                 <digi:trn>
                                                                 	<b><bean:write name="fundingDetail" property="adjustmentTypeName" /></b>
                                                                 </digi:trn>
                                                                </field:display>
                                                        </td>
                                                        <td height="18" align="right"><field:display name="Date Commitment"
                                                                feature="Commitments">
                                                                <b><bean:write name="fundingDetail" property="transactionDate"/></b>
                                                        	</field:display>
                                                        </td>
                                                        <td height="18" align="right"><field:display
                                                                name="Amount Commitment" feature="Commitments">
                                                                <!-- <font color="blue">*</font>-->
                                                            <b><bean:write name="fundingDetail" property="transactionAmount" /></b>
                                                        </field:display> <field:display name="Currency Commitment"
                                                                feature="Commitments">
                                                                <b><bean:write name="fundingDetail" property="currencyCode" /></b>
                                                        </field:display>&nbsp;</td>
                                                        <td height="18"><field:display name="Exchange Rate"
                                                                feature="Funding Information">
                                                                <b><bean:write name="fundingDetail" property="formattedRate" /></b>
                                                        </field:display></td>
                                                </tr>
                        </logic:equal>
                </logic:equal>
        </logic:iterate>
        <tr>
                <td colspan="2" bgcolor="#eeeeee" style="border-top: 1px solid #000000; text-transform: uppercase">
                        <digi:trn> SUBTOTAL PIPELINE COMMITMENTS: </digi:trn>
                </td>
                <td nowrap="nowrap" align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000;">
                	<c:if test="${not empty funding.subtotalPipelineCommitments}">
                    	<b>${funding.subtotalPipelineCommitments} ${aimEditActivityForm.currCode}</b>
            		</c:if>&nbsp;
        		</td>    
                <td align="right" bgcolor="#eeeeee" style="border-top: 1px solid #000000">&nbsp;</td>
        </tr>
        <tr><td colspan="4" height="7px"></td></tr>
</c:if>
<!-- End commitments-->
