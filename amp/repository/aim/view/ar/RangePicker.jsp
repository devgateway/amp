<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>

<%@page import="org.digijava.module.aim.util.FeaturesUtil"%>
<%@page import="org.digijava.module.aim.dbentity.AmpGlobalSettings"%>
<%@page import="java.util.Collections"%>
<bean:define id="reportMeta" name="reportMeta"
	type="org.digijava.module.aim.dbentity.AmpReports" scope="session"
	toScope="page" />


<digi:instance property="aimReportsFilterPickerForm" />

<digi:form action="/reportsFilterPicker.do"
	name="aimReportsFilterPickerForm2" type="aimReportsFilterPickerForm">
<div>	
	<table width="100%" style="vertical-align: top;" align="center" cellpadding="7px" cellspacing="7px">
		<tr>
			<td><digi:trn key="rep:filer:NumberYearsInRange">Number of Years in Range</digi:trn><br />
			<html:select property="countYear" onchange="changeRange();"
				styleClass="inp-text">
				<html:optionsCollection property="countYears"
					label="wrappedInstance" value="wrappedInstance" />
			</html:select></td>
			<td><html:hidden property="countYearFrom" /></td>
		</tr>
		<tr>
			<td><digi:trn key="rep:filer:DefaultStarYear">Default Start Year</digi:trn><br />
			<html:select property="fromYear" styleClass="inp-text">
				<html:option value="-1">
					<digi:trn key="rep:filer:All">All</digi:trn>
				</html:option>
				<html:optionsCollection property="fromYears" label="wrappedInstance"
					value="wrappedInstance" />
			</html:select></td>
			<td><digi:trn key="rep:filer:DefaultEndYear">Default End Year</digi:trn><br />
			<html:select property="toYear" styleClass="inp-text">
				<html:option value="-1">
					<digi:trn key="rep:filer:All">All</digi:trn>
				</html:option>
				<html:optionsCollection property="toYears" label="wrappedInstance"
					value="wrappedInstance" />
			</html:select></td>
		</tr>
	</table>
	
	<br />
	<table width="100%">
		<tr>
			<td align="center" colspan="5"><html:hidden
				property="ampReportId" /> <html:hidden property="defaultCurrency" />
			<html:submit styleClass="buton" onclick="return checkRangeValues()"
				property="apply">
				<digi:trn key="rep:filer:ApplyRanges">Apply Ranges</digi:trn>
			</html:submit>&nbsp; <html:button onclick="rangeReset();" styleClass="buton"
				property="reset">
				<digi:trn key="rep:filer:ResetRanges">Reset</digi:trn>
			</html:button></td>
		</tr>
	</table>
	</div>
</digi:form>