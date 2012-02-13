<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<div class="budgetcontainer">
	<span class="crumb"><a href="/aim/admin.do" class="comment" title="Click here to goto Admin Home">Admin Home</a>&nbsp;&gt;&nbsp;<a href="/budgetexport/showProjectList.do" class="comment" title="Click here to goto Project List">Budget Export Projects</a>&nbsp;&gt;&nbsp;Add/Edit Mapping Rule</span>
	<h1 class="subtitle-blue">Budget export manager</h1> 
<digi:instance property="beMapRuleForm"/>
	
<digi:form action="/addEditDeleteMapRule.do?action=save" method="post">
	<html:hidden name="beMapRuleForm" property="id"/>
	<html:hidden name="beMapRuleForm" property="curProjectId"/>
		<digi:errors/>
<div style="padding:25px;" class="budget-table">
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
 </div>
 <div style="clear:both;"></div>
	</digi:form>
</div>
