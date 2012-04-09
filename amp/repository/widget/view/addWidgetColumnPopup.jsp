<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>


<script type="text/javascript">
	function addColumnToWidget(myForm){
		if (validateMandatories()){
			myForm.target=window.opener.name;
			myForm.submit();
			window.close();
		}
	}
	$(document).ready(function(){
		var mainTextBox = document.getElementsByName('colName')[0];
		mainTextBox.focus();
	});

	function validateMandatories(){
		var name = ""+document.getElementsByName("colName")[0].value;
		if (name==""){
			alert('<digi:trn jsFriendly="true">Please add a name!</digi:trn>');
			return false;
		}
		return true;
	}
</script>

<style>
body {font-family:Arial, Helvetica, sans-serif}
.buttonx, .dr-menu {background-color:#5E8AD1; border-top: 1px solid #99BAF1; border-left:1px solid #99BAF1; border-right:1px solid #225099; border-bottom:1px solid #225099; font-size:11px; color:#FFFFFF; font-weight:bold; padding-left:5px; padding-right:5px; padding-top:3px; padding-bottom:3px;}
.inputx, td input.inp-text, table#addUserContainer tr td input, td select {border:1px solid #D0D0D0; background-color:#FFFFFF}
.inputx_sm_200 {border:1px solid #D0D0D0; background-color:#FFFFFF; font-size:11px; width:250px;}
hr {border: 0; color: #E5E5E5; background-color: #E5E5E5; height: 1px; width: 100%; text-align: left;}


</style>


<digi:instance id="cForm" property="gisTableWidgetCreationForm"/>

<digi:form action="/adminTableWidgets.do?actType=addColumn">
	<html:hidden name="cForm" property="colId"/>
	<html:hidden name="cForm" property="colColumnEdit"/>
	
	<table width="100%" cellpadding="5" style="font-size:12px;">
		<tr bgcolor="#c7d4db">
			<td colspan="2" align="center">
				<span class="subtitle-blue">
					<digi:trn key="gis:addwidgetcolumn:pageTitle"><b>Add new column</b></digi:trn>
				</span>			</td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap" width="40%">
				<font color="red">*</font><strong><digi:trn key="gis:addwidgetcolumn:typeTitle">Type:</digi:trn></strong></td>
			<td width="60%">
				<c:if test="${cForm.colColumnEdit}">
					<html:select name="cForm" property="colSelectedType" tabindex="1" disabled="true">
						<html:optionsCollection name="cForm" property="columnTypes" label="label" styleClass="inputx_sm_200" value="value"/>
					</html:select>
				</c:if>
				<c:if test="${not cForm.colColumnEdit}">
					<html:select name="cForm" property="colSelectedType" tabindex="1">
						<html:optionsCollection name="cForm" property="columnTypes" label="label" styleClass="inputx_sm_200" value="value"/>
					</html:select>
				</c:if>			</td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">
				<font color="red">*</font><strong><digi:trn key="gis:addwidgetcolumn:nameTitle">Name:</digi:trn></strong></td>
			<td><html:text name="cForm" property="colName" styleClass="inputx" tabindex="2"/></td>
		</tr>
                <field:display name="Table Column Code" feature="Table Widgets">
		<tr>
			<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:addwidgetcolumn:codeTitle">Code:</digi:trn></strong></td>
			<td><html:text name="cForm" styleClass="inputx" property="colCode" tabindex="3"/></td>
		</tr>
                </field:display>
                <field:display name="Table Column CSS class" feature="Table Widgets">
		<tr>
			<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:addwidgetcolumn:cssClassTitle">CSS class:</digi:trn></strong></td>
			<td><html:text name="cForm" styleClass="inputx" property="colCssClass" tabindex="4"/></td>
		</tr>
                </field:display>
                <field:display name="Table Column HTML Style" feature="Table Widgets">
		<tr>
			<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:addwidgetcolumn:htmlStyleTitle">HTML Style:</digi:trn></strong></td>
			<td><html:text name="cForm" styleClass="inputx" property="colHtmlStyle" tabindex="5"/></td>
		</tr>
                </field:display>
                <field:display name="Table Column Pattern" feature="Table Widgets">
		<tr>
			<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:addwidgetcolumn:patternTitle">Pattern:</digi:trn></strong></td>
			<td><html:text name="cForm" property="colPattern" styleClass="inputx" tabindex="6"/></td>
		</tr>
                </field:display>
                 <field:display name="Table Column Widgth" feature="Table Widgets">
		<tr>
			<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:addwidgetcolumn:widthTitle">Width:</digi:trn></strong></td>
			<td><html:text name="cForm" property="colWidth" styleClass="inputx" tabindex="7"/></td>
		</tr>
                </field:display>
		<tr>
			<td colspan="2">
				<hr>			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<c:set var="cancelButton"><digi:trn key="gis:cancelButton">Cancel</digi:trn></c:set>
				<input type="button" value="${cancelButton}" class="buttonx" onclick="javascript:window.close()" tabindex="9">
			
				<c:set var="addButton"><digi:trn key="gis:addButton">Add</digi:trn>
				</c:set>
				<input type="button" onclick="addColumnToWidget(this.form)" class="buttonx" value="${addButton}" title="Submit" tabindex="8">			</td>
		</tr>
	</table>

</digi:form>

