<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/category.tld" prefix="category"%>

<%@ taglib uri="/src/main/webapp/WEB-INF/fieldVisibility.tld" prefix="field"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/featureVisibility.tld" prefix="feature"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/moduleVisibility.tld" prefix="module"%>

<digi:instance property="aimEditActivityForm" />
<c:set var="transaction" value="MTEF Projections/MTEF Projections Table" scope="page"/>
<c:if test="${!empty funding.mtefDetails}">
    <tr bgcolor="#FFFFCC">
        <td height="20" colspan="3" valign="bottom" bgcolor="#FFFFCC" style="text-transform: uppercase;">
            <a><digi:trn key="aim:plannedexpenditures">MTEF Projections</digi:trn></a>
        </td>
		<td height="20" bgcolor="#FFFFCC" align="center">
			<c:if test="${aimEditActivityForm.funding.fixerate == true}">
				<b> <digi:trn key="aim:exchange">Exchange Rate</digi:trn> </b>
			</c:if>
		</td>        
    </tr>
	
	<logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<logic:equal name="fundingDetail" property="transactionType" value="3">
		    <logic:equal name="fundingDetail" property="projectionTypeName.value" value="pipeline">
                <c:set var="showFiscalYear" value="true" />
                <%@include file="previewActivityFundingDetail.jspf" %>
                <c:remove var="showFiscalYear"/>
            </logic:equal>
		</logic:equal>
   	</logic:iterate>
	<tr>
		<td colspan="2" class="preview-funding-total">
            <digi:trn key='aim:subtotalActualdisbursementPipeline'>Subtotal MTEF Projections Pipeline</digi:trn>:
		</td>
		<td colspan="2" nowrap="nowrap" class="preview-align preview-funding-total">
			 <c:if test="${not empty funding.subtotalMTEFsPipeline}">
                <b><span dir="ltr">${funding.subtotalMTEFsPipeline}</span> ${aimEditActivityForm.currCode}</b>
             </c:if>&nbsp;
        </td>
                   
		<td class="preview-funding-total">&nbsp;</td>
	</tr>



	<logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		<logic:equal name="fundingDetail" property="transactionType" value="3">
		    <logic:equal name="fundingDetail" property="projectionTypeName.value" value="projection">
                <c:set var="showFiscalYear" value="true" />
                <%@include file="previewActivityFundingDetail.jspf" %>
                <c:remove var="showFiscalYear"/>
            </logic:equal>
		</logic:equal>
   	</logic:iterate>
	<tr>
		<td colspan="2" class="preview-funding-total">
            <digi:trn key='aim:subtotalActualdisbursementPipeline'>Subtotal MTEF Projections Projection</digi:trn>:
		</td>
		<td colspan="2" nowrap="nowrap" class="preview-align preview-funding-total">
			 <c:if test="${not empty funding.subtotalMTEFsProjection}">
                <b><span dir="ltr">${funding.subtotalMTEFsProjection}</span> ${aimEditActivityForm.currCode}</b>
             </c:if>&nbsp;
        </td>

		<td class="preview-align preview-funding-total">&nbsp;</td>
	</tr>

</c:if>

<tr>
    <td colspan="4" height="7px"></td>
</tr>



