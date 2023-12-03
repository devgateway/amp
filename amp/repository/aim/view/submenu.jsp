<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule" %>

<digi:instance property="aimFinancialOverviewForm" />

<digi:context name="digiContext" property="context"/>

<logic:equal name="aimFinancialOverviewForm" property="sessionExpired" value="true">
	<jsp:include page="../../../repository/aim/view/sessionExpired.jsp"  />
</logic:equal>

<logic:equal name="aimFinancialOverviewForm" property="sessionExpired" value="false">
<jsp:useBean id="urlSubTabs" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlSubTabs}" property="ampActivityId">
	<bean:write name="aimFinancialOverviewForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlSubTabs}" property="ampFundingId">
	<bean:write name="aimFinancialOverviewForm" property="ampFundingId"/>
</c:set>
<c:set target="${urlSubTabs}" property="tabIndex" value="1"/>
<c:set target="${urlSubTabs}" property="transactionType" value="0"/>

<jsp:useBean id="urlAll" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlAll}" property="ampActivityId">
	<bean:write name="aimFinancialOverviewForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlAll}" property="ampFundingId">
	<bean:write name="aimFinancialOverviewForm" property="ampFundingId"/>
</c:set>
<c:set target="${urlAll}" property="tabIndex" value="1"/>

<jsp:useBean id="urlDiscrepancy" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlDiscrepancy}" property="ampActivityId">
	<bean:write name="aimFinancialOverviewForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlDiscrepancy}" property="tabIndex" value="1"/>
<c:set target="${urlDiscrepancy}" property="transactionType" value="0"/>

<TABLE cellspacing="0" cellpadding="0" align="center" vAlign="top" border="0" width="100%">
<TR>
<TD vAlign="top" align="left">
		<!-- contents -->
			<digi:trn key="aim:overview">OVERVIEW</digi:trn>
		 </td>
</tr>
<tr>
<TD vAlign="top" align="left">
					<c:set var="translation">
						<digi:trn key="aim:clickToViewCommitments">Click here to view Commitments</digi:trn>
					</c:set>
					<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >
					<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>
					</digi:link>
		</td>
</tr>
<tr>
		<TD vAlign="top" align="left">
					<c:set target="${urlSubTabs}" property="transactionType" value="1"/>
					<c:set var="translation">
						<digi:trn key="aim:clickToViewDisbursements">Click here to view Disbursements</digi:trn>
					</c:set>
					<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >
					<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>
					</digi:link>
		</td>
</tr>
<tr>
		<TD vAlign="top" align="left">
					<c:set target="${urlSubTabs}" property="transactionType" value="2"/>
					<c:set var="translation">
						<digi:trn key="aim:clickToViewExpenditures">Click here to view Expenditures</digi:trn>
					</c:set>
					<feature:display ampModule="Funding" name="Expenditures">
						<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="${translation}" >
						<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>
						</digi:link> 
					</feature:display>
		</td>

</tr>
<tr>
			<TD vAlign="top" align="left">
					<digi:link href="/viewYearlyDiscrepancy.do" name="urlDiscrepancy" styleClass="sub-nav2" title="${translation}" >
					<digi:trn key="aim:discrepancy">DISCREPANCY</digi:trn>
					</digi:link>
		</td>
</tr>
<tr>
		<TD vAlign="top" align="left">
					<c:set var="translation">
						<digi:trn key="aim:clickToViewAll">Click here to view All</digi:trn>
					</c:set>
					<digi:link href="/viewYearlyComparisons.do" name="urlAll" styleClass="sub-nav2" title="${translation}" >
					<digi:trn key="aim:all">ALL</digi:trn>
					</digi:link>	
				
</td>
</tr>
</table>
</logic:equal>
					 




