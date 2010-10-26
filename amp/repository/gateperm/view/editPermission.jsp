<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>


<digi:form action="/managePerm.do">
<script type="text/javascript">
function submitForm(mode) {
	permissionForm.mode.value=mode;
	permissionForm.submit();
}
</script>


<digi:errors/>

<table>
<tr><td align="right">Name: </td><td><html:text property="name"/></td></tr>

<tr><td align="right">Description: </td><td><html:textarea property="description"/></td></tr>


<tr><td align="right">Permission Type: </td><td>
<logic:equal name="permissionForm" property="id" value="0">
<html:select property="type" onchange="submitForm('type')">
<html:option value="Gate">Gate</html:option>
<html:option value="Composite">Composite</html:option>
</html:select>
</logic:equal>
<logic:notEqual name="permissionForm" property="id" value="0">
<html:select property="type" disabled="true">
<html:option value="Gate">Gate</html:option>
<html:option value="Composite">Composite</html:option>
</html:select>
</logic:notEqual>
</td></tr>

<html:hidden property="mode"/>
<html:hidden property="id"/>

<logic:equal name="permissionForm" property="type" value="Gate">
<tr><td align="right">Actions:</td><td>
<html:select property="actions" multiple="true">
<html:optionsCollection property="_availableActions" value="wrappedInstance" label="wrappedInstance"/>
</html:select>
</td></tr>

<tr><td align="right">Gate Init:</td><td>
<html:select property="gateTypeName" onchange="submitForm('gate')">
<html:option value="unselected">--Select--</html:option>
<html:optionsCollection property="_availableGateTypes" value="name" label="simpleName"/>
</html:select>
</td></tr>

<tr><td align="right">

</td><td>

</td></tr>

</logic:equal>


<logic:equal name="permissionForm" property="type" value="Composite">	
<tr><td align="right">Permissions:</td><td>
<html:select property="permissions" multiple="true">
<html:optionsCollection property="_availablePermissions" value="id" label="name"/>
</html:select>
</td></tr>	
</logic:equal>

<logic:notEmpty name="permissionForm" property="gateParameters">
<tr><td align="right">Parameters:</td><td>
<table>
<logic:iterate id="gateParameter" name="permissionForm" property="gateParameters">
<tr><td align="right">
	<bean:write name="gateParameter" property="category"/> :</td><td>
	<html:textarea indexed="true" name="gateParameter" property="value.value"/>
	<i><bean:write name="gateParameter" property="value.category"/></i>
</td></tr>
</logic:iterate>
</table>
</td></tr>	
</logic:notEmpty>
	
	
<tr><td align="right"> </td><td>
<html:button property="save" onclick="submitForm('save')">Save</html:button>
<html:cancel property="list">Cancel</html:cancel>

</td></tr>

</table>

</digi:form>