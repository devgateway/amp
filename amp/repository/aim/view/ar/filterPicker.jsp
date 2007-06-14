<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%> 
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>

<bean:define id="reportMeta" name="reportMeta"
	type="org.digijava.module.aim.dbentity.AmpReports" scope="session"
	toScope="page" />


<digi:instance property="aimReportsFilterPickerForm"/>

<digi:form action="/reportsFilterPicker.do">

<html:hidden property="ampReportId"/>
	<table>
	<tr>
		<td class=clsTableTitleCol colspan="3" align="center">Select filters</td>
	</tr>
		
		<tr>
			<td colspan="3"><b>Currency</b></td>
		</tr>
		<tr>
			<td colspan="3">
			<html:select property="currency" style="width: 400px" styleClass="inp-text">
			<html:option value="-1">All</html:option>
				<html:optionsCollection property="currencies"
					value="ampCurrencyId" label="currencyName" />
			</html:select>
			</td>
		</tr>


		<tr>
			<td><b>Fiscal Calendar</b></td>
			<td align="center"><b>From Year</b></td>
			<td align="center"><b>To Year</b></td>
		</tr>
		<tr>
			<td>
			<html:select property="calendar" style="width: 200px" styleClass="inp-text">
				<html:option value="-1">All</html:option>			
				<html:optionsCollection property="calendars"
					value="ampFiscalCalId" label="name" />
			</html:select>
			</td>
	
			<td align="center">
			<html:select property="fromYear" styleClass="inp-text">
				<html:option value="-1">All</html:option>			
				<html:optionsCollection property="fromYears" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>

			<td align="center">
			<html:select property="toYear" styleClass="inp-text">
				<html:option value="-1">All</html:option>			
				<html:optionsCollection property="toYears" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>
		</tr>


		<tr>
			<td colspan="3"><b>Sector</b></td>
		</tr>
		<tr>
			<td colspan="3" styleClass="inp-text">
			<html:select multiple="true" property="selectedSectors" size="3" style="width: 400px" styleClass="inp-text">
				<html:optionsCollection property="sectors"
					value="ampSectorId" label="name" />
			</html:select>
			</td>
		</tr>
		
		<tr>
			<td colspan="3"><b>Status</b></td>
		</tr>
		<tr>
			<td colspan="3">
			
		<category:showoptions styleClass="inp-text" property="selectedStatuses" size="3" name="aimReportsFilterPickerForm" multiselect="true" keyName="<%=org.digijava.module.aim.helper.CategoryConstants.ACTIVITY_STATUS_KEY%>"/>
			
			</td>
		</tr>
		
		<tr>
			<td colspan="3"><b>Donor</b></td>
		</tr>
		<tr>
			<td colspan="3">
			<html:select multiple="true" property="selectedDonors" size="3" style="width: 400px" styleClass="inp-text">
				<html:optionsCollection property="donors"
					value="ampOrgId" label="name" />
			</html:select>
			</td>
		</tr>

		<tr>
			<td><b>Risks</b></td>
			<td>
			<b>Line Ministry Rank</b>
			</td>
			<td>
			<b>Planning Ministry Rank</b>
			</td>
			
		</tr>
		<tr>
			<td>
			<html:select multiple="true" property="selectedRisks" size="3" styleClass="inp-text">
				<html:optionsCollection property="risks"
					value="ampIndRiskRatingsId" label="ratingName" />
			</html:select>
			</td>
			<td>
			<html:select property="lineMinRank" style="width: 50px" styleClass="inp-text">
				<html:option value="-1">All</html:option>						
				<html:optionsCollection property="actRankCollection" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>
			<td>
			<html:select property="planMinRank" style="width: 50px" styleClass="inp-text">
				<html:option value="-1">All</html:option>						
				<html:optionsCollection property="actRankCollection" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>
			
		</tr>



	<tr>
	<td>&nbsp;</td>
	</tr>
		
	<tr>
	<td align="center" colspan="3">
	<html:submit property="apply">Apply</html:submit>&nbsp;
	<html:submit property="reset">Reset</html:submit>
	</td>
		
	</tr>
	

	</table>
</digi:form>