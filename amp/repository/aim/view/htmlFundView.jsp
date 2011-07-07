<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>


<digi:instance property="aimAdvancedReportForm" />


<logic:equal name="aimAdvancedReportForm" property="reportType"
	value="donor">
	<logic:equal name="typeAssist" value="true">
		<td width="12">Total</td>
	</logic:equal>
</logic:equal>

<logic:equal name="aimAdvancedReportForm" property="acCommFlag"
	value="true">
	<td width="12%"><logic:notEqual name="ampFund" property="commAmount"
		value="0">
		<bean:write name="ampFund" property="commAmount" />
	</logic:notEqual></td>
</logic:equal>
<logic:equal name="aimAdvancedReportForm" property="acDisbFlag"
	value="true">
	<td width="12%"><logic:notEqual name="ampFund" property="disbAmount"
		value="0">
		<bean:write name="ampFund" property="disbAmount" />
	</logic:notEqual></td>
</logic:equal>
<logic:equal name="aimAdvancedReportForm" property="acExpFlag"
	value="true">
	<td width="12%"><logic:notEqual name="ampFund" property="expAmount"
		value="0">
		<bean:write name="ampFund" property="expAmount" />
	</logic:notEqual></td>
</logic:equal>
<logic:equal name="aimAdvancedReportForm" property="plCommFlag"
	value="true">
	<td width="13%"><logic:notEqual name="ampFund" property="plCommAmount"
		value="0">
		<bean:write name="ampFund" property="plCommAmount" />
	</logic:notEqual></td>
</logic:equal>
<logic:equal name="aimAdvancedReportForm" property="plDisbFlag"
	value="true">
	<td width="13%"><logic:notEqual name="ampFund" property="plDisbAmount"
		value="0">
		<bean:write name="ampFund" property="plDisbAmount" />
	</logic:notEqual></td>
</logic:equal>
<logic:equal name="aimAdvancedReportForm" property="plExpFlag"
	value="true">
	<td width="13%"><logic:notEqual name="ampFund" property="plExpAmount"
		value="0">
		<bean:write name="ampFund" property="plExpAmount" />
	</logic:notEqual></td>
</logic:equal>
<logic:equal name="aimAdvancedReportForm" property="acBalFlag"
	value="true">
	<td width="13%"><logic:notEqual name="ampFund" property="unDisbAmount"
		value="0">
		<bean:write name="ampFund" property="unDisbAmount" />
	</logic:notEqual></td>
</logic:equal>
