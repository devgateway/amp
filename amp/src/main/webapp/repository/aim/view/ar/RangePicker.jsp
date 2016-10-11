
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
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>

<%
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>
<bean:define id="reportMeta" name="reportCD" property="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" toScope="page"/>


<digi:instance property="aimReportsFilterPickerForm" />

<digi:form action="/reportsFilterPicker.do" name="aimReportsFilterPickerForm2" type="aimReportsFilterPickerForm">


<script type="text/javascript">
	function rreset(){
		document.aimReportsFilterPickerForm2.renderStartYear.value=-1;
		document.aimReportsFilterPickerForm2.renderEndYear.value=-1;
	}
</script>

<div>	
	<table width="100%" align="center" cellpadding="0" cellspacing="0" style="vertical-align: top;">
		
		<tr>
		  <td align="center" nowrap="nowrap"><digi:trn key="rep:filer:rangeStarYear">Range Start Year</digi:trn></td>
		  <td align="center" nowrap="nowrap"><digi:trn key="rep:filer:rangeEndYear">Range End Year</digi:trn></td>
	  </tr>
		<tr>
			<td width="50%" align="center" nowrap="nowrap"><br />
			<html:select  styleClass="inp-text" property="renderStartYear">
				<html:option value="-1">
					<digi:trn key="rep:filer:All">All</digi:trn>
				</html:option>
				<html:optionsCollection property="fromYears" label="wrappedInstance" value="wrappedInstance" />
			</html:select></td>
			<td width="50%" align="center" nowrap="nowrap"><br />
			<html:select property="renderEndYear" styleClass="inp-text">
				<html:option value="-1">
					<digi:trn key="rep:filer:All">All</digi:trn>
				</html:option>
				<html:optionsCollection property="toYears" label="wrappedInstance" value="wrappedInstance" />
			</html:select></td>
		</tr>
	</table>
	
	
	<table width="100%">
		<tr>
			<td height="40" colspan="5" align="center">
			<%-- <html:hidden property="ampReportId" /> --%>
			
			<input type="hidden" name="ampReportId" value="${reportCD.ampReportId}" />
			<input type="hidden" name="reportContextId" value="${reportCD.contextId}" />

			
			<html:submit styleClass="dr-menu" onclick="return checkRangeValues()"
				property="apply">
				<digi:trn key="rep:filer:ApplyRanges">Apply Ranges</digi:trn>
			</html:submit>&nbsp; <html:button onclick="javascript:rreset();" styleClass="dr-menu"
				property="reset">
				<digi:trn key="rep:filer:ResetRanges">Reset</digi:trn>
			</html:button></td>
		</tr>
	</table>
	
</div>
</digi:form>