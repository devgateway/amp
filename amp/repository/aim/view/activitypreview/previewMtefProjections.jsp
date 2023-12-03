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
        <logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.ampModule.aim.helper.FundingDetail">
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
        <logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.ampModule.aim.helper.FundingDetail">
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

