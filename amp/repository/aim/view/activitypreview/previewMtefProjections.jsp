<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://digijava.org" prefix="digi"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://digijava.org/CategoryManager" prefix="category"%>

<%@ taglib uri="http://digijava.org/fields" prefix="field"%>
<%@ taglib uri="http://digijava.org/features" prefix="feature"%>
<%@ taglib uri="http://digijava.org/modules" prefix="module"%>

<!-- mtefs -->
<digi:instance property="aimEditActivityForm" />
<c:set var="transaction" value="MTEF Projections/MTEF Projections Table" scope="page"/>
<c:if test="${!empty funding.mtefDetails}">
	<tr bgcolor="#FFFFCC">
		<td colspan="3" style="text-transform: uppercase">
			<a title='<digi:trn jsFriendly="true">Medium-Term Expenditure Framework Projections</digi:trn>'>
				<digi:trn>MTEF Projections</digi:trn>:
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
            <logic:equal name="fundingDetail" property="transactionType" value="3">
                <logic:equal name="fundingDetail" property="projectionTypeName.value" value="pipeline">
                    <c:set var="showFiscalYear" value="true" />
                    <%@include file="previewActivityFundingDetail.jspf" %>
                    <c:remove var="showFiscalYear"/>
                </logic:equal>
            </logic:equal>
        </logic:iterate>
	</c:if>
	<tr>
		<td colspan="2" class="preview-funding-total">
			<digi:trn>Subtotal MTEFs Pipeline</digi:trn>:
		</td>
		<td colspan="2" nowrap="nowrap" class="preview-align preview-funding-total">
			<c:if test="${not empty funding.subtotalMTEFsPipeline}">
 				<b><span dir="ltr">${funding.subtotalMTEFsPipeline}</span> ${aimEditActivityForm.currCode}</b>
 			</c:if> &nbsp;
           </td>
	</tr>

	<c:if test="${!empty funding.fundingDetails}">
        <logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
            <logic:equal name="fundingDetail" property="transactionType" value="3">
                <logic:equal name="fundingDetail" property="projectionTypeName.value" value="projection">
                    <c:set var="showFiscalYear" value="true" />
                    <%@include file="previewActivityFundingDetail.jspf" %>
                    <c:remove var="showFiscalYear"/>
                </logic:equal>
            </logic:equal>
        </logic:iterate>
	</c:if>
	<tr>
		<td colspan="2" class="preview-funding-total">
			<digi:trn>Subtotal MTEFs Projection</digi:trn>:
		</td>
		<td colspan="2" nowrap="nowrap" class="preview-align preview-funding-total">
			<c:if test="${not empty funding.subtotalMTEFsProjection}">
 				<b><span dir="ltr">${funding.subtotalMTEFsProjection}</span> ${aimEditActivityForm.currCode}</b>
 			</c:if> &nbsp;
           </td>
	</tr>
</c:if> 
<%--	</c:if>--%>
	<tr>
		<td colspan="5" height="7px"></td>
	</tr>
<c:remove var="transaction"/>
<!-- MTEFs end -->

