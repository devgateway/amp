<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<div class="budgetcontainer">
	<span class="crumb"><a href="/aim/admin.do" class="comment" title="Click here to goto Admin Home">Admin Home</a>&nbsp;&gt;&nbsp;<a href="/budgetexport/showProjectList.do" class="comment" title="Click here to goto Project List">Budget Export Projects</a>&nbsp;&gt;&nbsp;Add/Edit Budget Export Project</span>
	<h1 class="subtitle-blue">Add/Edit Budget Export Project</h1> 
<digi:instance property="beProjectForm"/>
	
<digi:form action="/addEditDeleteProject.do?action=save" method="post">
	<html:hidden name="beProjectForm" property="id"/>
		<digi:errors/>
<div style="padding:15px;" class="budget-table">
	<table>
		<tr>
			<td>Name</td>
			<td>
				<html:text name="beProjectForm" property="name"/>
			</td>
		</tr>
				<tr>
			<td>Description</td>
			<td>
				<html:text name="beProjectForm" property="description"/>
			</td>
		</tr>
		<tr>
			<td>Report</td>
			<td>
				
				<bean:define id="availReports" name="beProjectForm" property="availReports"/>
				<html:select name="beProjectForm" property="selReport">
					<html:options collection="availReports" property="id" labelProperty="name"/>
				</html:select>
				
			</td>
		<tr>
			<td colspan="2">
				<html:submit value="Save" styleClass="button"/>
			</td>
		</tr>
	</table>	
    </div>	
    <div style="clear:both;"></div>
    </div>
</digi:form>