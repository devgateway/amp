<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<div class="budgetcontainer">
	<span class="crumb"><a href="/aim/admin.do" class="comment" title="<digi:trn>Click here to goto Admin Home</digi:trn>"><digi:trn>Admin Home</digi:trn></a>&nbsp;&gt;&nbsp;<a href="/budgetexport/showProjectList.do" class="comment" title="<digi:trn>Click here to goto Project List</digi:trn>"><digi:trn>Budget Export Projects</digi:trn></a>&nbsp;&gt;&nbsp;<digi:trn>Add/Edit Mapping Rule</digi:trn></span>
	<h1 class="subtitle-blue"><digi:trn>Budget export manager</digi:trn></h1> 
<digi:instance property="beMapRuleForm"/>
	
<digi:form action="/addEditDeleteMapRule.do?action=save" method="post">
	<html:hidden name="beMapRuleForm" property="id"/>
	<html:hidden name="beMapRuleForm" property="curProjectId"/>
		<digi:errors/>
<div style="padding:25px;" class="budget-table">
	<table>
		<tr>
			<td><digi:trn>Name</digi:trn></td>
			<td>
				<html:text name="beMapRuleForm" property="name"/>
			</td>
		</tr>
		<tr>
			<td><digi:trn>AMP Column</digi:trn></td>
			<td>
				<bean:define id="availCols" name="beMapRuleForm" property="availColumns"/>
				<html:select name="beMapRuleForm" property="ampColumnId">
					<html:options collection="availCols" property="columnId" labelProperty="columnName"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td><digi:trn>Has Header</digi:trn></td>
			<td>
				<html:checkbox name="beMapRuleForm" property="header"/>
			</td>
		</tr>
		<tr>
			<td><digi:trn>Allow "None" Mapping</digi:trn></td>
			<td>
				<html:checkbox name="beMapRuleForm" property="allowNone"/>
			</td>
		</tr>
		<tr>
			<td><digi:trn>Allow "All" Mapping</digi:trn></td>
			<td>
				<html:checkbox name="beMapRuleForm" property="allowAll"/>
			</td>
		</tr>
		<tr>
			<td><digi:trn>CSV Column Delimiter</digi:trn></td>
			<td>
				<html:select name="beMapRuleForm" property="csvColDelimiter">
					<html:option value="0">Coma Separated</html:option>
					<html:option value="1">Tab Separated</html:option>
				</html:select>
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
