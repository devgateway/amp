<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>

<%@page import="java.math.BigDecimal"%>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.ampModule.aim.form.EditActivityForm"--%>

<feature:display name="Contracting" ampModule="Contracting">
    <fieldset>
        <legend>
		<span class=legend_label id="ipalink" style="cursor: pointer;">
			<digi:trn>IPA Contracting</digi:trn>
		</span>
        </legend>
        <div id="ipadiv" class="toggleDiv">
            <table width="100%">
                <tr>
                    <td><!-- contents -->
                        <logic:notEmpty name="aimEditActivityForm" property="contracts">
                            <table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#DBDBDB">
                                <c:forEach items="${aimEditActivityForm.contracts.contracts}" var="contract" varStatus="idx">
                                    <tr>
                                        <td bgColor=#f4f4f2 align="center" vAlign="top">
                                            <table width="100%" border="0" cellspacing="2" cellpadding="2" align="left" class="box-border-nopadding">
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Info/Contract Name" parentModule="/Activity Form/Contracts/Contract Item/Contract Info">
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:name" >Contract name:</digi:trn>
                                                        </td>
                                                        <td><span class="word_break bold">${contract.contractName}</span></td>
                                                    </tr>
                                                </ampModule:display>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Info/Contract Description" parentModule="/Activity Form/Contracts/Contract Item/Contract Info">										<tr>
                                                    <td align="left">
                                                        <digi:trn key="aim:IPA:popup:description">Description:</digi:trn>
                                                    </td>
                                                    <td><span class="word_break bold">${contract.description}</span></td>
                                                </tr>
                                                </ampModule:display>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Info/Activity Type" parentModule="/Activity Form/Contracts/Contract Item/Contract Info">										<tr>
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:actCat">Activity Category:</digi:trn>
                                                        </td>
                                                        <td>
                                                            <c:if test="${not empty contract.activityCategory}">
                                                                <span class="word_break bold">${contract.activityCategory.value}</span>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </ampModule:display>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Info/Contract Type" parentModule="/Activity Form/Contracts/Contract Item/Contract Info">
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:type">Type</digi:trn>:
                                                        </td>
                                                        <td>
                                                            <c:if test="${not empty contract.type}">
                                                                <span class="word_break bold">${contract.type.value}</span>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </ampModule:display>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Details/Start of Tendering" parentModule="/Activity Form/Contracts/Contract Item/Contract Details">
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:startOfTendering">Start of Tendering:</digi:trn>
                                                        </td>
                                                        <td><span class="word_break bold">${contract.formattedStartOfTendering}</span></td>
                                                    </tr>
                                                </ampModule:display>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Details/Signature" parentModule="/Activity Form/Contracts/Contract Item/Contract Details">
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:signatureOfContract">Signature of Contract:</digi:trn>
                                                        </td>
                                                        <td><span class="word_break bold">${contract.formattedSignatureOfContract}</span></td>
                                                    </tr>
                                                </ampModule:display>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Organizations" parentModule="/Activity Form/Contracts/Contract Item">
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:contractOrg">Contract Organization:</digi:trn>
                                                        </td>
                                                        <td>
                                                            <c:if test="${not empty contract.organization}">
                                                                <span class="word_break bold">${contract.organization.name}</span>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </ampModule:display>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Details/Contractor Name" parentModule="/Activity Form/Contracts/Contract Item/Contract Details">
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:contractOrg">Contract Organization</digi:trn>:
                                                        </td>
                                                        <td><span class="word_break bold">${contract.contractingOrganizationText}</span></td>
                                                    </tr>
                                                </ampModule:display>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Details/Completion" parentModule="/Activity Form/Contracts/Contract Item/Contract Details">
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:contractCompletion">Contract Completion:</digi:trn>
                                                        </td>
                                                        <td><span class="word_break bold">${contract.formattedContractCompletion}</span></td>
                                                    </tr>
                                                </ampModule:display>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Details/Status" parentModule="/Activity Form/Contracts/Contract Item/Contract Details">
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:status">Status:</digi:trn>
                                                        </td>
                                                        <td>
                                                            <c:if test="${not empty contract.status}">
                                                                <span class="word_break bold">${contract.status.value}</span>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </ampModule:display>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/Contract Total Value" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation">
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:totalAmount">Total Amount</digi:trn>:
                                                        </td>
                                                        <td>
                                                            <b>
                                                                    ${contract.totalAmount}
                                                                    ${contract.totalAmountCurrency.currencyCode}
                                                            </b>
                                                        </td>
                                                    </tr>
                                                </ampModule:display>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Contract Total Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
                                                    <tr>
                                                        <td align="left" colspan="2">
                                                            <digi:trn key="aim:IPA:popup:totalECContribution">Total EC Contribution:</digi:trn>
                                                        </td>
                                                    </tr>
                                                </ampModule:display>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IB Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:IB">IB</digi:trn>:
                                                        </td>
                                                        <td>
                                                            <b>
                                                                    ${contract.totalECContribIBAmount}
                                                                    ${contract.totalAmountCurrency.currencyCode}
                                                            </b>
                                                        </td>
                                                    </tr>
                                                </ampModule:display>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/INV Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:INV">INV:</digi:trn>
                                                        </td>
                                                        <td>
                                                            <b>
                                                                    ${contract.totalECContribINVAmount}
                                                                    ${contract.totalAmountCurrency.currencyCode}
                                                            </b>
                                                        </td>
                                                    </tr>
                                                </ampModule:display>
                                                <tr>
                                                    <td align="left" colspan="2">
                                                        <digi:trn key="aim:IPA:popup:totalNationalContribution">Total National Contribution:</digi:trn>
                                                    </td>
                                                </tr>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Central Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:Central">Central</digi:trn>:
                                                        </td>
                                                        <td>
                                                            <b>
                                                                    ${contract.totalNationalContribCentralAmount}
                                                                    ${contract.totalAmountCurrency.currencyCode}
                                                            </b>
                                                        </td>
                                                    </tr>
                                                </ampModule:display>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/Regional Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:Regional">Regional</digi:trn>:
                                                        </td>
                                                        <td>
                                                            <b>
                                                                    ${contract.totalNationalContribRegionalAmount}
                                                                    ${contract.totalAmountCurrency.currencyCode}
                                                            </b>
                                                        </td>
                                                    </tr>
                                                </ampModule:display>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IFI Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:IFIs">IFIs</digi:trn>:
                                                        </td>
                                                        <td>
                                                            <b>
                                                                    ${contract.totalNationalContribIFIAmount}
                                                                    ${contract.totalAmountCurrency.currencyCode}
                                                            </b>
                                                        </td>
                                                    </tr>
                                                </ampModule:display>
                                                <tr>
                                                    <td align="left" colspan="2">
                                                        <digi:trn key="aim:IPA:popup:totalPrivateContribution">Total Private Contribution:</digi:trn>
                                                    </td>
                                                </tr>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts/IB Amount" parentModule="/Activity Form/Contracts/Contract Item/Funding Allocation/EU Amounts">
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:IB">IB:</digi:trn>
                                                        </td>
                                                        <td>
                                                            <b>
                                                                    ${contract.totalPrivateContribAmount}
                                                                    ${contract.totalAmountCurrency.currencyCode}
                                                            </b>
                                                        </td>
                                                    </tr>
                                                </ampModule:display>
                                                <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements" parentModule="/Activity Form/Contracts/Contract Item">
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:totalDisbursements">Total Disbursements</digi:trn>:
                                                        </td>
                                                        <td>
                                                            <b>${contract.totalDisbursements}</b> &nbsp;
                                                            <logic:empty name="contract" property="dibusrsementsGlobalCurrency">
                                                                &nbsp; <b>${aimEditActivityForm.currCode}</b>
                                                            </logic:empty>
                                                            <logic:notEmpty name="contract" property="dibusrsementsGlobalCurrency">
                                                                &nbsp; <b>${contract.dibusrsementsGlobalCurrency.currencyCode}</b>
                                                            </logic:notEmpty>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:totalFundingDisbursements">Total Funding Disbursements</digi:trn>:
                                                        </td>
                                                        <td>
                                                            <b>${contract.fundingTotalDisbursements}</b> &nbsp;
                                                            <logic:empty name="contract" property="dibusrsementsGlobalCurrency">
                                                                &nbsp;<b>${contract.totalAmountCurrency}</b>
                                                            </logic:empty>
                                                            <logic:notEmpty name="contract" property="dibusrsementsGlobalCurrency">
                                                                &nbsp;<b>${contract.dibusrsementsGlobalCurrency.currencyCode}</b>
                                                            </logic:notEmpty>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:contractExecutionRate">Contract Execution Rate</digi:trn>:
                                                        </td>
                                                        <td>
                                                            &nbsp; <b>${contract.executionRate}</b>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn key="aim:IPA:popup:contractFundingExecutionRate">Contract Funding Execution Rate</digi:trn>:
                                                        </td>
                                                        <td>&nbsp; <b>${contract.fundingExecutionRate}</b></td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="2">
                                                            <digi:trn key="aim:IPA:popup:disbursements">Disbursements:</digi:trn>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>&nbsp;</td>
                                                        <td>
                                                            <logic:notEmpty name="contract" property="disbursements">
                                                                <table>
                                                                    <c:forEach items="${contract.disbursements}" var="disbursement">
                                                                        <tr>
                                                                            <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Adjustment Type" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
                                                                                <td align="left" valign="top">
                                                                                    <b><digi:trn>${disbursement.adjustmentType.value}</digi:trn></b>
                                                                                        <%-- 															<c:if test="${disbursement.adjustmentType==0}"> --%>
                                                                                        <%-- 																<b><digi:trn key="aim:actual">Actual</digi:trn></b> --%>
                                                                                        <%-- 															</c:if>  --%>
                                                                                        <%-- 															<c:if test="${disbursement.adjustmentType==1}"> --%>
                                                                                        <%-- 																<b><digi:trn key="aim:planned">Planned</digi:trn></b> --%>
                                                                                        <%-- 															</c:if>														 --%>
                                                                                </td>
                                                                            </ampModule:display>
                                                                            <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
                                                                                <td align="left" valign="top">
                                                                                    <b>${disbursement.amount}</b>
                                                                                </td>
                                                                            </ampModule:display>
                                                                            <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
                                                                                <td align="left" valign="top">
                                                                                    <b>${disbursement.currency.currencyCode}</b>
                                                                                </td>
                                                                            </ampModule:display>
                                                                            <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
                                                                                <td align="left" valign="top">
                                                                                    <b>${disbursement.disbDate}</b>
                                                                                </td>
                                                                            </ampModule:display>
                                                                        </tr>
                                                                    </c:forEach>
                                                                </table>
                                                            </logic:notEmpty></td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="2">
                                                            <digi:trn key="aim:IPA:popup:fundingDisbursements">Funding Disbursements:</digi:trn>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>&nbsp;</td>
                                                        <td>
                                                            <logic:notEmpty name="aimEditActivityForm"
                                                                            property="funding.fundingDetails">
                                                                <table width="100%">
                                                                    <tr>
                                                                        <td>
                                                                            <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Adjustment Type" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
                                                                                <digi:trn key="aim:adjustmentTyeDisbursement">Adjustment Type Disbursement</digi:trn>
                                                                            </ampModule:display>
                                                                        </td>
                                                                        <td>
                                                                            <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
                                                                                <digi:trn key="aim:amountDisbursement">Amount Disbursement</digi:trn>
                                                                            </ampModule:display>
                                                                        </td>
                                                                        <td>
                                                                            <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
                                                                                <digi:trn key="aim:currencyDisbursement">Currency Disbursement</digi:trn>
                                                                            </ampModule:display>
                                                                        </td>
                                                                        <td>
                                                                            <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
                                                                            <digi:trn key="aim:dateDisbursement">Date Disbursement</digi:trn>
                                                                            </ampModule:display>
                                                                    </tr>
                                                                    <c:forEach items="${aimEditActivityForm.funding.fundingDetails}" var="fundingDetail">
                                                                        <logic:equal name="contract" property="contractName" value="${fundingDetail.contract.contractName}">
                                                                            <c:if test="${fundingDetail.transactionType == 1}">
                                                                                <tr>
                                                                                    <td align="center" valign="top">
                                                                                            <%-- 			                                                   <digi:trn>${fundingDetail.adjustmentTypeName.value}</digi:trn> --%>

                                                                                            <%-- 																	<c:if test="${fundingDetail.adjustmentType==0}"> --%>
                                                                                            <%-- 																		<digi:trn key="aim:actual">Actual</digi:trn> --%>
                                                                                            <%-- 																	</c:if>  --%>
                                                                                            <%-- 																	<c:if test="${fundingDetail.adjustmentType==1}"> --%>
                                                                                            <%-- 																		<digi:trn key="aim:planned">Planned</digi:trn> --%>
                                                                                            <%-- 																	</c:if>																 --%>
                                                                                    </td>
                                                                                    <td align="center" valign="top">
                                                                                        <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Amount" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
                                                                                            <b>${fundingDetail.transactionAmount}</b>
                                                                                        </ampModule:display>
                                                                                    </td>

                                                                                    <td align="center" valign="top">
                                                                                        <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Currency" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
                                                                                            <b>${fundingDetail.currencyCode}</b>
                                                                                        </ampModule:display>
                                                                                    </td>
                                                                                    <td align="center" valign="top">
                                                                                        <ampModule:display name="/Activity Form/Contracts/Contract Item/Contract Disbursements/Transaction Date" parentModule="/Activity Form/Contracts/Contract Item/Contract Disbursements">
                                                                                            <b>${fundingDetail.transactionDate}</b>
                                                                                        </ampModule:display>
                                                                                    </td>
                                                                                </tr>
                                                                            </c:if>
                                                                        </logic:equal>
                                                                    </c:forEach>
                                                                </table>
                                                            </logic:notEmpty></td>
                                                    </tr>
                                                </ampModule:display>
                                                <field:display name="Contracting Amendments" feature="Contracting">
                                                    <bean:define id="ct" name="contract" type="org.digijava.ampModule.aim.dbentity.IPAContract"/>
                                                    <tr>
                                                        <td align="left">
                                                            <digi:trn>Contracts financed by the lessor</digi:trn>:
                                                        </td>
                                                        <td>&nbsp;
                                                            <% if(ct.getDonorContractFundinAmount()!=null){ %>
                                                            <b><%=BigDecimal.valueOf(ct.getDonorContractFundinAmount()).toPlainString()%></b>
                                                            <%}%>
                                                            &nbsp;&nbsp;&nbsp;&nbsp;
                                                            <b>${contract.donorContractFundingCurrency.currencyCode}</b>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td align="left"><digi:trn>Total amount of the contract by the lessor</digi:trn>:
                                                        </td>
                                                        <td>&nbsp;
                                                            <%if(ct.getDonorContractFundinAmount()!=null){ %>
                                                            <b><%=BigDecimal.valueOf(ct.getTotAmountDonorContractFunding()).toPlainString()%></b>
                                                            <%}%>
                                                            &nbsp;&nbsp;&nbsp;&nbsp;
                                                            <b>${contract.totalAmountCurrencyDonor.currencyCode}</b>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td align="left"><digi:trn>Total contract amount from state</digi:trn>:
                                                        </td>
                                                        <td>&nbsp;
                                                            <%if(ct.getDonorContractFundinAmount()!=null){ %>
                                                            <b><%=BigDecimal.valueOf(ct.getTotAmountCountryContractFunding()).toPlainString()%></b>
                                                            <%}%>
                                                            &nbsp;&nbsp;&nbsp;&nbsp;
                                                            <b>${contract.totalAmountCurrencyCountry.currencyCode}</b>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="2"><b><digi:trn>Amendments :</digi:trn></b>											</td>
                                                    </tr>
                                                    <tr>
                                                        <td>&nbsp;</td>
                                                        <td><logic:notEmpty name="contract"
                                                                            property="amendments"
                                                        >
                                                            <table width="100%">
                                                                <tr>
                                                                    <th><digi:trn>Amount</digi:trn></th>
                                                                    <th><digi:trn>Currency</digi:trn></th>
                                                                    <th><digi:trn>Date</digi:trn></th>
                                                                    <th><digi:trn>Reference</digi:trn></th>
                                                                </tr>
                                                                <c:forEach items="${contract.amendments}"
                                                                           var="amendment"
                                                                >
                                                                    <bean:define id="am" name="amendment"
                                                                                 type="org.digijava.ampModule.aim.dbentity.IPAContractAmendment"
                                                                    />
                                                                    <tr>
                                                                        <td align="center" valign="top">
                                                                            <b>${amendment.amoutStr}</b>
                                                                        </td>
                                                                        <td align="center" valign="top">
                                                                            <b>${amendment.currency.currencyCode}</b>
                                                                        </td>
                                                                        <td align="center" valign="top">
                                                                            <b>${amendment.amendDate}</b>
                                                                        </td>
                                                                        <td align="center" valign="top">
                                                                            <b>${amendment.reference}</b>
                                                                        </td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </table>
                                                        </logic:notEmpty></td>
                                                    </tr>
                                                </field:display>
                                            </table>								</td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </logic:notEmpty></td>
                </tr>
            </table>
        </div>
    </fieldset>
    <!-- end IPA Contracting -->
</feature:display>
