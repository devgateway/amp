<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<bean:define id="bean" name="listable"  type="org.dgfoundation.amp.PropertyListable" scope="request" toScope="page"/>

<table width="100%">
<tr><td colspan="2" align="center"><i><bean:write name="listable" property="beanName"/></i></td></tr>
<logic:iterate id="prop" name="bean" property="propertiesMap">
<tr><td align="right"><b><bean:write name="prop" property="key"/>:</b></td>
<td><bean:write name="prop" property="value"/></td>
</tr>
</logic:iterate>
</table>