<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>



<bean:define id="reportMeta" name="reportMeta"
	type="org.digijava.module.aim.dbentity.AmpReports" scope="session"
	toScope="page" />


<digi:instance property="aimReportsFilterPickerForm"/>

<digi:form action="/reportsFilterPicker.do">

<html:hidden property="ampReportId"/>
	<table>
		
		<tr>
			<td colspan="4"><b><digi:trn key="rep:filter:projectid">Project ID</digi:trn> </b></td>
		</tr>
		<tr>
			<td colspan="3">
			<html:text  property="text" style="width: 400px" styleClass="inp-text" onchange="checkProjectId(this)"/>
			</td>
			<td colspan="1"><font color="red">*<digi:trn key="aim:chanheFilters:numericAmpId">Amp Id must contain only numbers</digi:trn></font></td>
		</tr>

		<tr>
			<td colspan="4"><b><digi:trn key="rep:filer:Currency">Currency</digi:trn></b></td>
		</tr>
		<tr>
			<td colspan="4">
			<html:select property="currency" style="width: 400px" styleClass="inp-text">
				<html:optionsCollection property="currencies"
					value="ampCurrencyId" label="currencyName" />

			</html:select>
			</td>
		</tr>


		<tr>
			<bean:define id="calendars" name="aimReportsFilterPickerForm" property="calendars" type="java.util.Collection"/>
			<% int size =  ((java.util.Collection)calendars).size();
			   boolean condition = (size != 1);
			   if (condition){%>
			<td colspan="2"><b><digi:trn key="rep:filer:fiscalCalendar">Fiscal Calendar</digi:trn></b></td>
			<% } %>
			<td align="center"><b><digi:trn key="rep:filer:FromYear">From Year</digi:trn></b></td>
			<td align="center"><b><digi:trn key="rep:filer:ToYear">To Year</digi:trn></b></td>
		</tr>
		<tr>
			<% if (condition){%>
			<td colspan="2">
			<html:select property="calendar" style="width: 270px" styleClass="inp-text">
				<html:optionsCollection property="calendars"
					value="ampFiscalCalId" label="name" />
			</html:select>
			</td>
			<% } %>
			<td align="center">
			<html:select property="fromYear" styleClass="inp-text">
				<html:option value="-1"><digi:trn key="rep:filer:All">All</digi:trn></html:option>
				<html:optionsCollection property="fromYears" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>

			<td align="center">
			<html:select property="toYear" styleClass="inp-text">
				<html:option value="-1"><digi:trn key="rep:filer:All">All</digi:trn></html:option>
				<html:optionsCollection property="toYears" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>
		</tr>


		<tr>
			<td colspan="4"><b><digi:trn key="rep:filer:Sector">Sector</digi:trn></b></td>
		</tr>
		<tr>
			<td colspan="4" styleClass="inp-text">
			<html:select multiple="true" property="selectedSectors" size="3" style="width: 400px" styleClass="inp-text">
				<html:optionsCollection property="sectors"
					value="ampSectorId" label="name" />
			</html:select>
			</td>
		</tr>




		<%-- <tr>
			<td colspan="4"><b><digi:trn key="rep:filer:donor">Donor</digi:trn></b></td>
		</tr>
		<tr>
			<td colspan="4">
			<html:select multiple="true" property="selectedDonors" size="3" style="width: 400px" styleClass="inp-text">
				<html:optionsCollection property="donors"
					value="ampOrgGrpId" label="orgGrpName" />
			</html:select>
			</td>
		</tr>--%>
		<tr>
			<td colspan="1"><b><digi:trn key="rep:filer:DonorType">Donor Type</digi:trn></b></td>
			<td colspan="3"><b><digi:trn key="rep:filer:DonorGroup">Donor Group</digi:trn></b></td>
		</tr>
		<tr>
			<td colspan="1">
				<html:select style="width: 190px" multiple="true" property="selectedDonorTypes" size="3" styleClass="inp-text">
				<html:optionsCollection property="donorTypes" value="ampOrgTypeId" label="orgType" />
				</html:select>
			</td>
			<td colspan="3">
				<html:select multiple="true" property="selectedDonorGroups" size="3" styleClass="inp-text">
				<html:optionsCollection style="width: 195px" property="donorGroups" value="ampOrgGrpId" label="orgGrpName" />
				</html:select>
			</td>
		</tr>
		<tr>
			<td><b><digi:trn key="rep:filer:Status">Status</digi:trn></b></td>
			<field:display name="Risk" feature="Activity">
			<td><b><digi:trn key="rep:filer:Risks">Risks</digi:trn></b></td>
			</field:display>
			<field:display  name="Line Ministry Rank" feature="Planning">
			<td>
			<b><digi:trn key="rep:filer:LineMinRank">Line Min. Rank</digi:trn></b>
			</td>
			</field:display>
			<field:display  name="Planning Ministry Rank" feature="Planning">
			<td>
			<b><digi:trn key="rep:filer:PlanningMinRank">Planning Min. Rank</digi:trn></b>
			</td>
			</field:display>
		</tr>
		<tr>
			<td valign="top" >
					<category:showoptions outerstyle="width: 190px" styleClass="inp-text" property="selectedStatuses" size="3" name="aimReportsFilterPickerForm" multiselect="true" keyName="<%=org.digijava.module.aim.helper.CategoryConstants.ACTIVITY_STATUS_KEY%>"/>
			</td>
			<field:display name="Risk" feature="Activity">
			<td valign="top" >
			<html:select multiple="true" property="selectedRisks" size="3" styleClass="inp-text">
				<html:optionsCollection property="risks"
					value="ampIndRiskRatingsId" label="ratingName" />
			</html:select>
			</td>
			</field:display>
			<field:display  name="Line Ministry Rank" feature="Planning">
			<td valign="top" >
			<html:select property="lineMinRank" style="width: 50px" styleClass="inp-text">
				<html:option value="-1"><digi:trn key="rep:filer:All">All</digi:trn></html:option>
				<html:optionsCollection property="actRankCollection" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>
			</field:display>
			<field:display  name="Planning Ministry Rank" feature="Planning">
			<td valign="top" >
			<html:select property="planMinRank" style="width: 50px" styleClass="inp-text">
				<html:option value="-1"><digi:trn key="rep:filer:All">All</digi:trn></html:option>
				<html:optionsCollection property="actRankCollection" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>
			</field:display>

		</tr>
		<tr>
			<td colspan="4"><b><digi:trn key="rep:filter:Location">Region</digi:trn></b></td>
		</tr>
		<tr>
			<td colspan="4">
			<html:select property="regionSelected" style="width: 400px" styleClass="inp-text">
				<html:option value="-1"><digi:trn key="rep:filer:All">All</digi:trn></html:option>
				<html:optionsCollection property="regionSelectedCollection" label="region" value="regionId" />
			</html:select>
			</td>
		</tr>
		<tr>
			<td colspan="4"><b><digi:trn key="rep:filer:financingInstrument">Financing Instrument</digi:trn></b></td>
		</tr>
		<tr>
			<td colspan="4">
			<category:showoptions size="3" outerstyle="width: 250px" styleClass="inp-text" name="aimReportsFilterPickerForm" property="selectedFinancingInstruments" multiselect="true" keyName="<%=org.digijava.module.aim.helper.CategoryConstants.FINANCING_INSTRUMENT_KEY %>"/>
			</td>
		</tr>
	<logic:notEqual name="widget" value="true" scope="request">
		<tr>
			<td colspan="4"><b><digi:trn key="rep:filter:pageSize"> Page Size</digi:trn></b></td>
		</tr>
		<tr>
			<td colspan="4">
			<html:select property="pageSize" style="width: 100px" styleClass="inp-text">
				<html:optionsCollection property="pageSizes"
					 label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>
		</tr>
</logic:notEqual>

	<field:display name="Government Approval Procedures" feature="Budget">
		<tr>
			<td colspan="4">
				<digi:trn key="rep:filter:govAppProc"> Government Approval Procedures </digi:trn>
			</td>
		</tr>
		<tr>
			<td colspan="4" valign="top" align="left">
				<digi:trn key="rep:filter:yes">Yes</digi:trn><html:radio property="governmentApprovalProcedures" value="true"/> &nbsp;&nbsp;<digi:trn key="rep:filter:no"> No</digi:trn><html:radio property="governmentApprovalProcedures" value="false"/>
			</td>
		</tr>
	</field:display>

	<field:display name="Joint Criteria" feature="Budget">
		<tr>
			<td colspan="4">
				<digi:trn key="rep:filter:jointCriteria"> Joint Criteria</digi:trn>
			</td>
		</tr>
		<tr>
			<td colspan="4" valign="top" align="left">
				<digi:trn key="rep:filter:yes">Yes</digi:trn><html:radio property="jointCriteria" value="true"/> &nbsp;&nbsp;<digi:trn key="rep:filter:no"> No</digi:trn><html:radio property="jointCriteria" value="false"/>
			</td>
		</tr>
	</field:display>
	<tr>
	<td>&nbsp;</td>
	</tr>

	<tr>
	<td align="center"  colspan="4">
	
	<html:submit styleClass="buton" property="apply"><digi:trn key="rep:filer:Apply">Apply</digi:trn></html:submit>&nbsp;
	
	<html:button  onclick="resetFilter();" styleClass="buton"  property="reset"><digi:trn key="rep:filer:Reset">Reset</digi:trn></html:button>
	
	<html:hidden  property="defaultCurrency"/>
		
	 
	</td>

	</tr>
	</table>
</digi:form>
