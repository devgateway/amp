<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/fieldVisibility.tld" prefix="field" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/featureVisibility.tld" prefix="feature" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/moduleVisibility.tld" prefix="module" %>

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
					<feature:display module="Funding" name="Expenditures">
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
					 




