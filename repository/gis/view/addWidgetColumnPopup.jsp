<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script type="text/javascript">
<!--
	function addColumnToWidget(myForm){
		myForm.target=window.opener.name;
		myForm.submit();
		window.close();
	}
	function refreshParent(){
		window.opener.refreshThis();
	}
	//window.onload = refreshParent;
	
//-->
</script>

<digi:instance id="cForm" property="gisTableWidgetCreationForm"/>
<digi:form action="/adminTableWidgets.do?actType=addColumn">
	<table width="100%">
		<tr>
			<td align="right" nowrap="nowrap">Name:</td>
			<td width="100%"><html:text name="cForm" property="colName"/></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">Code:</td>
			<td width="100%"><html:text name="cForm" property="colCode"/></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">CSS class:</td>
			<td width="100%"><html:text name="cForm" property="colCssClass"/></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">HTML Style:</td>
			<td width="100%"><html:text name="cForm" property="colHtmlStyle"/></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">Pattern:</td>
			<td width="100%"><html:text name="cForm" property="colPattern"/></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">Width:</td>
			<td width="100%"><html:text name="cForm" property="colWidth"/></td>
		</tr>
		<tr>
			<td width="100%" align="center" colspan="2"><input type="button" onclick="addColumnToWidget(this.form)" value="Add" title="Submit"></td>
		</tr>
	</table>
</digi:form>