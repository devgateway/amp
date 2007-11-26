<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<script type="text/javascript">
showHideUnDedicated(checkBox,id) {
	if(checkBox.checked==true) document.getElementById(id)
}

</script>

<digi:form action="/managePermMap.do">
<script type="text/javascript">
function submitForm(mode) {
	permissionMapForm.mode.value=mode;
	permissionMapForm.submit();
}
</script>

<html:hidden property="mode"/>

<digi:errors/>

<table>

<tr><td align="right">Permissible Category</td><td>
<html:select property="permissibleCategory" onchange="submitForm('permissibleCategoryPicked')">
<html:option value="select">--Select--</html:option>
<html:optionsCollection property="_availablePermissibleCategories" value="simpleName" label="simpleName"/>
</html:select>
</td></tr>
<logic:notEmpty name="permissionMapForm" property="permissibleCategory">
<tr><td colspan="2"><b>You can...</b></td></tr>
<tr><td align="right"><b>Assign a global permission to the entire class:</b></td>
<td>
<html:select property="permissionId">
<html:option value="0">--None--</html:option>
<html:optionsCollection property="_availablePermissions" value="id" label="name"/>
</html:select>
<html:button property="saveGlobal" onclick="submitForm('saveGlobal')">Assign</html:button>
<digi:link href="/managePerm.do?list" title="EDIT PERMISSIONS">
<digi:img src="module/gateperm/images/edit.gif" border="0" />&nbsp;Edit Permissions
</digi:link>

</td>
<tr><td colspan="2"><b>OR...</b></td></tr>
</logic:notEmpty>
<tr><td valign="top" align="right"><b>Assign a specific permission for each object of this class:</b></td>
<td>
<logic:notEmpty name="permissionMapForm" property="permissionMaps">
<html:button property="saveDetailed" onclick="submitForm('saveDetailed')">Assign All</html:button>
<table>
<thead>
<tr><th>Object Label</th><th>Assigned Permission</th></tr>
</thead>
<tbody>
<logic:iterate id="permissionMap" name="permissionMapForm" property="permissionMaps">
<tr>
<td><bean:write name="permissionMap" property="objectLabel"/></td>
<td>

<logic:equal name="permissionMap" property="dedicated" value="true">
<b>dedicated</b>
<input type="checkbox" onchange="javascript:showHideUnDedicated(this,'obj-<bean:write name="permissionMap" property="objectIdentifier"/>')"/>
<div id="obj-<bean:write name="permissionMap" property="objectIdentifier"/>" style='position:relative;display:none;'>
</logic:equal>
<html:select name="permissionMap" property="permissionId" indexed="true">
<html:option value="0">--Global--</html:option>
<html:optionsCollection property="_availablePermissions" value="id" label="name"/>
</html:select>

<logic:equal name="permissionMap" property="dedicated" value="true">
</div>
</logic:equal>

</td>
</tr>
</logic:iterate>
</tbody>
</table>
<html:submit property="saveDetailed">Assign All</html:submit>
</logic:notEmpty>
</td></tr>
</table>
</digi:form>

