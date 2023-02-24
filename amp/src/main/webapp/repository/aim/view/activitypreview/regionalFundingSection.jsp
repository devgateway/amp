<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.module.aim.form.EditActivityForm"--%>

<module:display name="/Activity Form/Regional Funding" parentModule="/Activity Form">
    <fieldset>
        <legend>
		<span class=legend_label id="regionalfundinglink" style="cursor: pointer;">
			<digi:trn>Regional Funding</digi:trn>
		</span>
        </legend>
        <div id="regionalfundingdiv" class="toggleDiv">
            <c:if test="${!empty aimEditActivityForm.funding.regionalFundings}">
                <table width="100%" cellSpacing="1" cellPadding="3" bgcolor="#aaaaaa">
                    <c:forEach var="regFunds" items="${aimEditActivityForm.funding.regionalFundings}">
                        <tr>
                            <td class="prv_right">
                                <table width="100%" cellSpacing="1" cellPadding="1">
                                    <tr>
                                        <td class="prv_right">
                                            <span class="word_break bold"><c:out value="${regFunds.regionName}"/></span>
                                        </td>
                                    </tr>
                                    <module:display name="/Activity Form/Regional Funding/Region Item/Commitments" parentModule="/Activity Form/Regional Funding/Region Item">
                                        <c:if test="${!empty regFunds.commitments}">
                                            <tr>
                                                <td class="prv_right">
                                                    <table width="100%" cellSpacing="1" cellPadding="0" class="box-border-nopadding" border="1"> <tr>
                                                        <td valign="top" width="100" bgcolor="#f0f0f0">
                                                            <digi:trn>Commitments</digi:trn>
                                                        </td>
                                                        <td class="prv_right">
                                                            <table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
                                                                <c:forEach var="fd" items="${regFunds.commitments}">
                                                                    <tr> <td width="50" bgcolor="#f0f0f0">
                                                                        <digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
                                                                            <c:out value="${fd.adjustmentTypeName}" />
                                                                        </digi:trn></td>
                                                                        <td align="right" width="100" bgcolor="#f0f0f0">
                                                                            <c:out value="${fd.transactionAmount}"/>
                                                                        </td>
                                                                        <td class="prv_right">
                                                                            <c:out value="${fd.currencyCode}"/>
                                                                        </td>
                                                                        <td bgcolor="#f0f0f0" width="70">
                                                                            <c:out value="${fd.transactionDate}"/>
                                                                        </td>
                                                                        <td class="prv_right"></td> </tr>
                                                                </c:forEach>
                                                            </table>
                                                        </td>
                                                    </tr> </table>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </module:display>
                                    <module:display name="/Activity Form/Regional Funding/Region Item/Disbursements"
                                                    parentModule="/Activity Form/Regional Funding/Region Item">
                                        <c:if test="${!empty regFunds.disbursements}">
                                            <tr>
                                                <td class="prv_right">
                                                    <table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
                                                        <tr>
                                                            <td valign="top" width="100" bgcolor="#f0f0f0">
                                                                <digi:trn key="aim:disbursements">Disbursements</digi:trn>
                                                            </td>
                                                            <td class="prv_right">
                                                                <table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
                                                                    <c:forEach var="fd" items="${regFunds.disbursements}">
                                                                        <tr>
                                                                            <td width="50" bgcolor="#f0f0f0">
                                                                                <digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
                                                                                    <c:out value="${fd.adjustmentTypeName}" />
                                                                                </digi:trn>
                                                                            </td>
                                                                            <td align="right" width="100" bgcolor="#f0f0f0">
                                                                                <c:out value="${fd.transactionAmount}"/>
                                                                            </td>
                                                                            <td class="prv_right">
                                                                                <c:out value="${fd.currencyCode}"/>
                                                                            </td>
                                                                            <td bgcolor="#f0f0f0" width="70">
                                                                                <c:out value="${fd.transactionDate}"/>
                                                                            </td>
                                                                            <td class="prv_right"></td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </module:display>

                                    <module:display name="/Activity Form/Regional Funding/Region Item/Expenditures"
                                                    parentModule="/Activity Form/Regional Funding/Region Item">
                                        <c:if test="${!empty regFunds.expenditures}">
                                            <tr>
                                                <td class="prv_right">
                                                    <table width="100%" cellSpacing="1" cellPadding="1" class="box-border-nopadding">
                                                        <tr>
                                                            <td valign="top" width="100" bgcolor="#f0f0f0">
                                                                <digi:trn key="aim:expenditures">Expenditures</digi:trn>
                                                            </td>
                                                            <td class="prv_right">
                                                                <table width="100%" cellSpacing="1" cellPadding="1" bgcolor="#eeeeee">
                                                                    <c:forEach var="fd" items="${regFunds.expenditures}">
                                                                        <tr>
                                                                            <td width="50" bgcolor="#f0f0f0">
                                                                                <digi:trn key="aim:${fd.adjustmentTypeNameTrimmed}">
                                                                                    <c:out value="${fd.adjustmentTypeName}" />
                                                                                </digi:trn>
                                                                            </td>
                                                                            <td align="right" width="100" bgcolor="#f0f0f0">
                                                                                <c:out value="${fd.transactionAmount}"/>
                                                                            </td>
                                                                            <td class="prv_right">
                                                                                <c:out value="${fd.currencyCode}"/></td>
                                                                            <td bgcolor="#f0f0f0" width="70">
                                                                                <c:out value="${fd.transactionDate}"/>
                                                                            </td>
                                                                            <td class="prv_right"></td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </module:display>
                                </table>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td class="prv_right">
                            <FONT color='blue'>
                                <jsp:include page="../utils/amountUnitsUnformatted.jsp">
                                    <jsp:param value="* " name="amount_prefix"/>
                                </jsp:include>
                            </FONT>
                        </td>
                    </tr>
                </table>
            </c:if>
        </div>
    </fieldset>
</module:display>
