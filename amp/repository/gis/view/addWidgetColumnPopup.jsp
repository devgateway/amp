<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>


<script type="text/javascript">
	function addColumnToWidget(myForm){
		myForm.target=window.opener.name;
		myForm.submit();
		window.close();
	}
	$(document).ready(function(){
		var mainTextBox = document.getElementsByName('colName')[0];
		mainTextBox.focus();
	});
</script>

<digi:instance id="cForm" property="gisTableWidgetCreationForm"/>
<digi:form action="/adminTableWidgets.do?actType=addColumn">
	<table width="100%" cellpadding="5">
		<tr bgcolor="#d7eafd">
			<td colspan="2" align="center">
				<span class="subtitle-blue">
					<digi:trn key="gis:addwidgetcolumn:pageTitle">Add new column</digi:trn>
				</span>
			</td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap" width="30%">
				<font color="red">*</font><strong><digi:trn key="gis:addwidgetcolumn:nameTitle">Name:</digi:trn></strong></td>
			<td width="100%"><html:text name="cForm" property="colName" tabindex="1"/></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:addwidgetcolumn:codeTitle">Code:</digi:trn></strong></td>
			<td width="100%"><html:text name="cForm" property="colCode" tabindex="2"/></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:addwidgetcolumn:cssClassTitle">CSS class:</digi:trn></strong></td>
			<td width="100%"><html:text name="cForm" property="colCssClass" tabindex="3"/></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:addwidgetcolumn:htmlStyleTitle">HTML Style:</digi:trn></strong></td>
			<td width="100%"><html:text name="cForm" property="colHtmlStyle" tabindex="4"/></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:addwidgetcolumn:patternTitle">Pattern:</digi:trn></strong></td>
			<td width="100%"><html:text name="cForm" property="colPattern" tabindex="5"/></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:addwidgetcolumn:widthTitle">Width:</digi:trn></strong></td>
			<td width="100%"><html:text name="cForm" property="colWidth" tabindex="6"/></td>
		</tr>
		<tr>
			<td colspan="2">
				<hr>
			</td>
		</tr>
		<tr>
			<td align="right">
				<c:set var="cancelButton"><digi:trn key="gis:cancelButton">Cancel</digi:trn></c:set>
				<input type="button" value="${cancelButton}" onclick="javascript:window.close()" tabindex="8">
			</td>
			<td width="100%">
				<c:set var="addButton"><digi:trn key="gis:addButton">Add</digi:trn></c:set>
				<input type="button" onclick="addColumnToWidget(this.form)" value="${addButton}" title="Submit" tabindex="7">
			</td>
		</tr>
	</table>
</digi:form>