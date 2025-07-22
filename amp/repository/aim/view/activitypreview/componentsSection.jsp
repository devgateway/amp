<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/aim" prefix="aim"%>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.module.aim.form.EditActivityForm"--%>

<module:display name="/Activity Form/Components" parentModule="/Activity Form">
    <fieldset>
        <legend>
		<span class=legend_label id="componentlink" style="cursor: pointer;">
			<digi:trn>Components</digi:trn>
		</span>
        </legend>
        <div id="componentdiv" class="toggleDiv">
            <logic:equal name="globalSettings" scope="application" property="showComponentFundingByYear" value="false">
                <c:if test="${!empty aimEditActivityForm.components.selectedComponents}">
                    <c:forEach var="comp" items="${aimEditActivityForm.components.selectedComponents}">
                        <table width="100%" cellSpacing="1" cellPadding="1">
                            <tr> <td> <table width="100%" cellSpacing="2" cellPadding="1" class="box-border-nopadding">
                                <tr>
                                    <td>
                                        <b> <c:out value="${comp.title}" /> </b>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <i> <digi:trn key="aim:description">Description</digi:trn>
                                            :</i> <c:out value="${comp.description}" />
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <i> <digi:trn key="aim:description">Component Type</digi:trn>
                                            :</i> <c:out value="${comp.typeName}" />
                                    </td>
                                </tr>
                                <tr>
                                    <td class="prv_right">
                                        <span class="word_break bold"><digi:trn>Component Fundings</digi:trn></span>
                                    </td>
                                </tr>
                                <module:display name="/Activity Form/Components/Component/Components Commitments"
                                                parentModule="/Activity Form/Components/Component">
                                    <c:if test="${!empty comp.commitments}">
                                        <tr>
                                            <td class="prv_right">
                                                <table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding">
                                                    <tr>
                                                        <td width="100" style="padding-left:5px;" vAlign="top" bgcolor="#f0f0f0">
                                                            <digi:trn key="aim:commitments">Commitments</digi:trn>
                                                        </td>
                                                        <td class="prv_right">
                                                            <c:forEach var="fd" items="${comp.commitments}">
                                                                <table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee" class="component-funding-table">
                                                                    <tr>
                                                                        <module:display name="/Activity Form/Components/Component/Components Commitments"
                                                                                        parentModule="/Activity Form/Components/Component">
                                                                            <td width="100">
                                                                                <digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
                                                                                    <b><c:out value="${fd.adjustmentTypeName}" /></b>
                                                                                </digi:trn>
                                                                            </td>
                                                                        </module:display>
                                                                        <module:display name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Amount"
                                                                                        parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
                                                                            <td align="right" width="100">
                                                                                <b><c:out value="${fd.transactionAmount}"/></b>
                                                                            </td>
                                                                        </module:display>
                                                                        <module:display name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Currency"
                                                                                        parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
                                                                            <td class="prv_right">
                                                                                <b><c:out value="${fd.currencyCode}"/></b>
                                                                            </td>
                                                                        </module:display>
                                                                        <module:display name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Transaction Date"
                                                                                        parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
                                                                            <td width="80">
                                                                                <b><c:out value="${fd.transactionDate}"/></b>
                                                                            </td>
                                                                        </module:display>
                                                                    </tr>
                                                                    <module:display name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Component Organization"
                                                                                    parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
                                                                        <tr>
                                                                            <td width="100">
                                                                                <b><digi:trn>Organisation</digi:trn>:</b>
                                                                            </td>
                                                                            <td colspan="3" style="padding-left: 15px">
                                                                                <logic:notEmpty property="componentOrganisation"
                                                                                                name="fd">
                                                                                    <c:out value="${fd.componentOrganisation.name}"/>
                                                                                </logic:notEmpty>
                                                                            </td>
                                                                        </tr>
                                                                    </module:display>
                                                                    <module:display name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Component Second Responsible Organization"
                                                                                    parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
                                                                        <tr>
                                                                            <td width="100">
                                                                                <b><digi:trn>Component Second Responsible Organization</digi:trn>:</b>
                                                                            </td>
                                                                            <td colspan="3" style="padding-left: 15px">
                                                                                <logic:notEmpty property="componentSecondResponsibleOrganization"
                                                                                                name="fd">
                                                                                    <c:out value="${fd.componentSecondResponsibleOrganization.name}"/>
                                                                                </logic:notEmpty>
                                                                            </td>
                                                                        </tr>
                                                                    </module:display>
                                                                    <module:display name="/Activity Form/Components/Component/Components Commitments/Commitment Table/Description"
                                                                                    parentModule="/Activity Form/Components/Component/Components Commitments/Commitment Table">
                                                                        <tr>
                                                                            <td width="100">
                                                                                <b><digi:trn>Description</digi:trn></b>
                                                                            </td>
                                                                            <td colspan="3" style="padding-left: 15px">
                                                                                <b><c:out value="${fd.componentTransactionDescription}" /></b>
                                                                            </td>
                                                                        </tr>
                                                                    </module:display>
                                                                </table>
                                                                <hr />
                                                            </c:forEach>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </c:if>
                                </module:display>
                                <module:display name="/Activity Form/Components/Component/Components Disbursements"
                                                parentModule="/Activity Form/Components/Component">
                                    <c:if test="${!empty comp.disbursements}">
                                        <tr>
                                            <td class="prv_right">
                                                <table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
                                                    <tr>
                                                        <td width="100" style="padding-left:5px;" vAlign="top" bgcolor="#f0f0f0">
                                                            <digi:trn key="aim:disbursements">Disbursements</digi:trn>
                                                        </td>
                                                        <td class="prv_right">
                                                            <c:forEach var="fd" items="${comp.disbursements}">
                                                                <table width="100%" cellSpacing="1" cellPadding="1"
                                                                       bgcolor="#eeeeee"
                                                                       class="component-funding-table">
                                                                    <tr>
                                                                        <module:display name="/Activity Form/Components/Component/Components Disbursements"
                                                                                        parentModule="/Activity Form/Components/Component">
                                                                            <td width="50">
                                                                                <digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
                                                                                    <b><c:out value="${fd.adjustmentTypeName}" /></b>
                                                                                </digi:trn>
                                                                            </td>
                                                                        </module:display>
                                                                        <module:display name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Amount"
                                                                                        parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
                                                                            <td align="right" width="100">
                                                                                <b><c:out value="${fd.transactionAmount}"/></b>
                                                                            </td>
                                                                        </module:display>

                                                                        <module:display name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Currency"
                                                                                        parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
                                                                            <td class="prv_right">
                                                                                <b><c:out value="${fd.currencyCode}"/></b>
                                                                            </td>
                                                                        </module:display>
                                                                        <module:display name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Transaction Date"
                                                                                        parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
                                                                            <td width="70">
                                                                                <b><c:out value="${fd.transactionDate}"/></b>
                                                                            </td>
                                                                        </module:display>
                                                                    </tr>
                                                                    <module:display name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Component Organization"
                                                                                    parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
                                                                        <tr>
                                                                            <td width="100">
                                                                                <b><digi:trn>Organisation</digi:trn>:</b>
                                                                            </td>
                                                                            <td colspan="3" style="padding-left: 15px">
                                                                                <logic:notEmpty property="componentOrganisation"
                                                                                                name="fd">
                                                                                    <c:out value="${fd.componentOrganisation.name}"/>
                                                                                </logic:notEmpty>
                                                                            </td>
                                                                        </tr>
                                                                    </module:display>
                                                                    <module:display name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Component Second Responsible Organization"
                                                                                    parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
                                                                        <tr>
                                                                            <td width="100">
                                                                                <b><digi:trn>Component Second Responsible Organization</digi:trn>:</b>
                                                                            </td>
                                                                            <td colspan="3" style="padding-left: 15px">
                                                                                <logic:notEmpty property="componentSecondResponsibleOrganization"
                                                                                                name="fd">
                                                                                    <c:out value="${fd.componentSecondResponsibleOrganization.name}"/>
                                                                                </logic:notEmpty>
                                                                            </td>
                                                                        </tr>
                                                                    </module:display>

                                                                    <module:display name="/Activity Form/Components/Component/Components Disbursements/Disbursement Table/Description"
                                                                                    parentModule="/Activity Form/Components/Component/Components Disbursements/Disbursement Table">
                                                                        <tr>
                                                                            <td width="100">
                                                                                <b><digi:trn>Description</digi:trn></b>
                                                                            </td>
                                                                            <td colspan="3" style="padding-left: 15px">
                                                                                <b><c:out value="${fd.componentTransactionDescription}" /></b>
                                                                            </td>
                                                                        </tr>
                                                                    </module:display>
                                                                </table>
                                                                <hr />
                                                            </c:forEach>

                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </c:if>
                                </module:display>

                                <module:display name="/Activity Form/Components/Component/Components Expenditures"
                                                parentModule="/Activity Form/Components/Component">
                                    <c:if test="${!empty comp.expenditures}">
                                        <tr>
                                            <td class="prv_right">
                                                <table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" class="box-border-nopadding">
                                                    <tr>
                                                        <td width="100" bgcolor="#f0f0f0" vAlign="top" style="padding-left:5px;">
                                                            <digi:trn key="aim:expenditures">Expenditures</digi:trn>
                                                        </td>
                                                        <td class="prv_right">
                                                            <c:forEach var="fd" items="${comp.expenditures}">
                                                                <table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee" class="component-funding-table">
                                                                    <tr>
                                                                        <module:display name="/Activity Form/Components/Component/Components Expeditures"
                                                                                        parentModule="/Activity Form/Components/Component">
                                                                            <td width="50">
                                                                                <digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
                                                                                    <b><c:out value="${fd.adjustmentTypeName}" /></b>
                                                                                </digi:trn>
                                                                            </td>
                                                                        </module:display>
                                                                        <module:display name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Amount"
                                                                                        parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
                                                                            <td align="right" width="100">
                                                                                <b><c:out value="${fd.transactionAmount}"/></b>
                                                                            </td>
                                                                        </module:display>
                                                                        <module:display name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Currency"
                                                                                        parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
                                                                            <td class="prv_right">
                                                                                <b><c:out value="${fd.currencyCode}"/></b>
                                                                            </td>
                                                                        </module:display>
                                                                        <module:display name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Transaction Date"
                                                                                        parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
                                                                            <td width="70">
                                                                                <b><c:out value="${fd.transactionDate}"/></b>
                                                                            </td>
                                                                        </module:display>
                                                                    </tr>
                                                                    <module:display name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Component Organization"
                                                                                    parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
                                                                        <tr>
                                                                            <td width="100">
                                                                                <b><digi:trn>Organisation</digi:trn>:</b>
                                                                            </td>
                                                                            <td colspan="3" style="padding-left: 15px">
                                                                                <logic:notEmpty property="componentOrganisation"
                                                                                                name="fd">
                                                                                    <c:out value="${fd.componentOrganisation.name}"/>
                                                                                </logic:notEmpty>
                                                                            </td>
                                                                        </tr>
                                                                    </module:display>
                                                                    <module:display name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Component Second Responsible Organization"
                                                                                    parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
                                                                        <tr>
                                                                            <td width="100">
                                                                                <b><digi:trn>Component Second Responsible Organization</digi:trn>:</b>
                                                                            </td>
                                                                            <td colspan="3" style="padding-left: 15px">
                                                                                <logic:notEmpty property="componentSecondResponsibleOrganization"
                                                                                                name="fd">
                                                                                    <c:out value="${fd.componentSecondResponsibleOrganization.name}"/>
                                                                                </logic:notEmpty>
                                                                            </td>
                                                                        </tr>
                                                                    </module:display>

                                                                    <module:display name="/Activity Form/Components/Component/Components Expenditures/Expenditure Table/Description"
                                                                                    parentModule="/Activity Form/Components/Component/Components Expenditures/Expenditure Table">
                                                                        <tr>
                                                                            <td width="100">
                                                                                <b><digi:trn>Description</digi:trn></b>
                                                                            </td>
                                                                            <td colspan="3" style="padding-left: 15px">
                                                                                <b><c:out value="${fd.componentTransactionDescription}" /></b>
                                                                            </td>
                                                                        </tr>
                                                                    </module:display>
                                                                </table>
                                                                <hr />
                                                            </c:forEach>

                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </c:if>
                                </module:display>
                                <tr>
                                    <td class="prv_right">
                                        <FONT color='blue'>
                                            <jsp:include page="../utils/amountUnitsUnformatted.jsp">
                                                <jsp:param value="* " name="amount_prefix"/>
                                            </jsp:include>
                                        </FONT>
                                    </td>
                                </tr>
                            </table> </td> </tr>
                        </table>
                    </c:forEach>
                </c:if>
            </logic:equal>
            <logic:equal name="globalSettings" scope="application" property="showComponentFundingByYear" value="true">
                <c:if test="${!empty aimEditActivityForm.components.selectedComponents}">
                    <c:forEach var="comp" items="${aimEditActivityForm.components.selectedComponents}">
                        <table width="100%" cellSpacing="1" cellPadding="1">
                            <tr>
                                <td>
                                    <table width="100%" cellSpacing="2" cellPadding="1" class="box-border-nopadding">
                                        <tr>
                                            <td>
                                                <span class="word_break bold"><c:out value="${comp.title}" /></span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <span class="italic"><digi:trn key="aim:component_code">Component code</digi:trn></span>

                                            :<span class="word_break"><c:out value="${comp.code}" /></span>
                                            </td>
                                        </tr>
                                        <c:if test="${!empty comp.url} }">
                                            <tr>
                                                <td>
                                                    <a href="<c:out value="${comp.url}"/>" target="_blank">
                                                        <digi:trn key="aim:preview_link_to_component">Link to component</digi:trn>&nbsp;
                                                        <c:out value="${comp.code}"/>
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:if>
                                        <tr>
                                            <td class="prv_right"><b>
                                                <digi:trn key="aim:fundingOfTheComponent">Finance of the component</digi:trn></b>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="prv_right">
                                                <table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding">
                                                    <c:forEach var="financeByYearInfo" items="${comp.financeByYearInfo}">
                                                        <tr>
                                                            <td valign="top" width="100" bgcolor="#f0f0f0">
                                                                <c:out value="${financeByYearInfo.key}"/></td>
                                                            <c:set var="financeByYearInfoMap" value="${financeByYearInfo.value}"/>
                                                            <td class="prv_right">
                                                                <table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
                                                                    <fmt:timeZone value="US/Eastern">
                                                                        <tr>
                                                                            <td width="50" bgcolor="#f0f0f0">
                                                                                <digi:trn key="aim:preview_plannedcommitments_sum">Planned Commitments Sum</digi:trn>														</td>
                                                                            <td align="right" width="100" bgcolor="#f0f0f0">
                                                                                <aim:formatNumber value="${financeByYearInfoMap['MontoProgramado']}"/>USD														</td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td width="50" bgcolor="#f0f0f0">
                                                                                <digi:trn key="aim:preview_actualcommitments_sum">Actual Commitments Sum</digi:trn>														</td>
                                                                            <td align="right" width="100" bgcolor="#f0f0f0">
                                                                                <aim:formatNumber value="${financeByYearInfoMap['MontoReprogramado']}"/> USD														</td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td width="50" bgcolor="#f0f0f0">
                                                                                <digi:trn key="aim:preview_plannedexpenditures_sum">Actual Expenditures Sum</digi:trn>														</td>
                                                                            <td align="right" width="100" bgcolor="#f0f0f0">
                                                                                <aim:formatNumber value="${financeByYearInfoMap['MontoEjecutado']}"/>USD														</td>
                                                                        </tr>
                                                                    </fmt:timeZone>
                                                                </table>											</td>
                                                        </tr>
                                                        <tr>
                                                            <td>&nbsp;</td>
                                                            <td>&nbsp;</td>
                                                        </tr>
                                                    </c:forEach>
                                                </table>								</td>
                                        </tr>
                                    </table>						</td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                            </tr>
                        </table>
                    </c:forEach>
                </c:if>
            </logic:equal>
        </div>
    </fieldset>
</module:display>
