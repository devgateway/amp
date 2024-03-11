<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c"%>

<%String width = request.getParameter("width");%>

<digi:instance property="aimAdvancedReportForm" />
<logic:iterate name="aimAdvancedReportForm" property="addedMeasures"
	id="addedMeasures">

	<c:if test="${addedMeasures.measureName == 'Actual Commitments'}">
		<logic:equal name="aimAdvancedReportForm" property="acCommFlag"
			value="true">
			<td height="21" width="<%=width%>" align="center"><a
				title="<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn>">
			<digi:trn key="aim:actualCommitment">Actual Commitment</digi:trn> </a>
			</td>
		</logic:equal>
	</c:if>

	<c:if test="${addedMeasures.measureName == 'Actual Disbursements'}">
		<logic:equal name="aimAdvancedReportForm" property="acDisbFlag"
			value="true">
			<td height="21" width="<%=width%>" align="center"><a
				title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
			<digi:trn key="aim:actualDisbursement">Actual Disbursement</digi:trn>
			</a></td>
		</logic:equal>
	</c:if>

	<c:if test="${addedMeasures.measureName == 'Actual Expenditures'}">
		<logic:equal name="aimAdvancedReportForm" property="acExpFlag"
			value="true">
			<td height="21" width="<%=width%>" align="center"><a
				title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the 	implementing agency</digi:trn>">
			<digi:trn key="aim:actualExpenditure">Actual Expenditure</digi:trn> </a></td>
		</logic:equal>
	</c:if>

	<c:if test="${addedMeasures.measureName == 'Planned Commitments'}">
		<logic:equal name="aimAdvancedReportForm" property="plCommFlag"
			value="true">
			<td height="21" width="<%=width%>" align="center"><a
				title="<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn>">
			<digi:trn key="aim:plannedCommitment">Planned Commitment</digi:trn> </a></td>
		</logic:equal>
	</c:if>

	<c:if test="${addedMeasures.measureName == 'Planned Disbursements'}">
		<logic:equal name="aimAdvancedReportForm" property="plDisbFlag"
			value="true">
			<td height="21" width="<%=width%>" align="center"><a
				title="<digi:trn key="aim:DisbursementofFund">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">
			<digi:trn key="aim:plannedDisbursement">Planned Disbursement</digi:trn>
			</a></td>
		</logic:equal>
	</c:if>

	<c:if test="${addedMeasures.measureName == 'Planned Expenditures'}">
		<logic:equal name="aimAdvancedReportForm" property="plExpFlag"
			value="true">
			<td height="21" width="<%=width%>" align="center"><a
				title="<digi:trn key="aim:ExpenditureofFunds">Amount effectively spent by the implementing agency</digi:trn>">
			<digi:trn key="aim:plannedExpenditure">Planned Expenditure</digi:trn>
			</a></td>
		</logic:equal>
	</c:if>
</logic:iterate>
