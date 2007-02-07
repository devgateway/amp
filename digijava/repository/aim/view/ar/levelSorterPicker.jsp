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

<digi:form action="/viewNewAdvancedReport.do">
	<table>
		<tr>
			<td align="right">Hierarchy Level:</td>
			<td><html:select property="levelPicked">
				<html:optionsCollection name="reportMeta" property="hierarchies"
					value="levelId" label="column.columnName" />
			</html:select></td>
		</tr>
		<tr>
			<td align="right">Sort by:</td>
			<td><html:select property="levelSorter">
				<html:option value="Title">Hierarchy Title</html:option>
				<html:optionsCollection name="report" property="trailCells"
					value="column.absoluteColumnName" label="column.absoluteColumnName" />
			</html:select></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><html:submit property="applySorter">Apply Sorting</html:submit></td>
		</tr>
	</table>
</digi:form>
