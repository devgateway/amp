<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="beMapRuleForm"/>
	
<digi:form action="/addEditDeleteMapRule.do?action=save" method="post">
	<html:hidden name="beMapRuleForm" property="id"/>
	<html:hidden name="beMapRuleForm" property="curProjectId"/>
	<table>
		<tr>
			<td>Name</td>
			<td>
				<html:text name="beMapRuleForm" property="name"/>
			</td>
		</tr>
		<tr>
			<td>AMP Column</td>
			<td>
				<bean:define id="availCols" name="beMapRuleForm" property="availColumns"/>
				<html:select name="beMapRuleForm" property="ampColumnId">
					<html:options collection="availCols" property="columnId" labelProperty="columnName"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td>Has header</td>
			<td>
				<html:checkbox name="beMapRuleForm" property="header"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<html:submit value="Save"/>
			</td>
		</tr>
	</table>		
</digi:form>