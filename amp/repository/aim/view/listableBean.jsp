<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<!-- the PropertyListable bean defined as attribute of the request -->
<bean:define id="bean" name="listable"
	type="org.dgfoundation.amp.PropertyListable" scope="request"
	toScope="page" />
<!-- style of display -->
<bean:define id="style" name="listableStyle" type="java.lang.String"
	scope="request" toScope="page" />
<!-- trn tags prefix -->
<bean:define id="prefix" name="listableTrnPrefix"
	type="java.lang.String" scope="request" toScope="page" />


<!-- Display the bean in a table style -->
<logic:equal name="style" value="table">
	<table width="100%">
		<tr>
			<td colspan="2" align="center"><i><bean:write name="listable"
				property="beanName" /></i></td>
		</tr>
		<logic:iterate id="prop" name="bean" property="propertiesMap">
			<tr>
				<td align="right"><b> <digi:trn key="${prefix}:${prop.key}">${prop.key}</digi:trn>
				:</b></td>
				<td><bean:write name="prop" property="value" /></td>
			</tr>
		</logic:iterate>
	</table>
</logic:equal>

<!-- Display the bean in a comma separated list style -->
<logic:equal name="style" value="list">
	<i><bean:write name="listable" property="beanName" /></i>
	<logic:iterate id="prop" name="bean" property="propertiesMap">
		<b> <digi:trn key="${prefix}:${prop.key}">${prop.key}</digi:trn>
		:</b>
		<bean:write name="prop" property="value" />;
</logic:iterate>
</logic:equal>
