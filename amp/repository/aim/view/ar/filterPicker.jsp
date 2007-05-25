<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<bean:define id="reportMeta" name="reportMeta"
	type="org.digijava.module.aim.dbentity.AmpReports" scope="session"
	toScope="page" />

<digi:form action="/reportsFilterPicker.do">
	<table>
		<tr>
			<td colspan="3"><b>Currency</b></td>
		</tr>
		<tr>
			<td colspan="3">
			<html:select property="currency" style="width: 400px">
				<html:optionsCollection property="currencies"
					value="ampCurrencyId" label="currencyName" />
			</html:select>
			</td>
		</tr>


		<tr>
			<td><b>Fiscal Calendar</b></td>
			<td><b>From Year</b></td>
			<td><b>To Year</b></td>
		</tr>
		<tr>
			<td>
			<html:select property="calendar" style="width: 200px">
				<html:optionsCollection property="calendars"
					value="ampFiscalCalId" label="name" />
			</html:select>
			</td>
	
			<td>
			<html:select property="fromYear">
				<html:optionsCollection property="fromYears" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>

			<td>
			<html:select property="toYear">
				<html:optionsCollection property="toYears" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>
		</tr>


		<tr>
			<td colspan="3"><b>Sector</b></td>
		</tr>
		<tr>
			<td colspan="3">
			<html:select multiple="true" property="selectedSectors" size="3" style="width: 400px">
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
			<html:select multiple="true" property="selectedStatuses" size="3" style="width: 400px"> 
				<html:optionsCollection property="statuses"
					value="ampStatusId" label="name" />
			</html:select>
			</td>
		</tr>
		
		<tr>
			<td colspan="3"><b>Donor</b></td>
		</tr>
		<tr>
			<td colspan="3">
			<html:select multiple="true" property="selectedDonors" size="3" style="width: 400px">
				<html:optionsCollection property="donors"
					value="ampOrgId" label="name" />
			</html:select>
			</td>
		</tr>

		<tr>
			<td colspan="3"><b>Risks</b></td>
		</tr>
		<tr>
			<td colspan="3">
			<html:select multiple="true" property="selectedRisks" size="3">
				<html:optionsCollection property="risks"
					value="ampIndRiskRatingsId" label="ratingName" />
			</html:select>
			</td>
		</tr>
		
	<tr>
	<td align="center" colspan="3">
	<html:submit property="apply">OK</html:submit>
	</td>
	</tr>
	

	</table>
</digi:form>