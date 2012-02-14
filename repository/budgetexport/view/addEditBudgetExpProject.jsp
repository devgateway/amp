<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<div class="budgetcontainer">
	<span class="crumb"><a href="/aim/admin.do" class="comment" title="<digi:trn>Click here to goto Admin Home</digi:trn>"><digi:trn>Admin Home</digi:trn></a>&nbsp;&gt;&nbsp;<a href="/budgetexport/showProjectList.do" class="comment" title="<digi:trn>Click here to goto Project List</digi:trn>"><digi:trn>Budget Export Projects</digi:trn></a>&nbsp;&gt;&nbsp;<digi:trn>Add/Edit Budget Export Project</digi:trn></span>
	<h1 class="subtitle-blue"><digi:trn>Add/Edit Budget Export Project</digi:trn></h1> 
<digi:instance property="beProjectForm"/>
	
<digi:form action="/addEditDeleteProject.do?action=save" method="post">
	<html:hidden name="beProjectForm" property="id"/>
		<digi:errors/>
<div style="padding:15px;" class="budget-table">
	<table>
		<tr>
			<td><digi:trn>Name</digi:trn></td>
			<td>
				<html:text name="beProjectForm" property="name"/>
			</td>
		</tr>
				<tr>
			<td><digi:trn>Description</digi:trn></td>
			<td>
				<html:text name="beProjectForm" property="description"/>
			</td>
		</tr>
		<tr>
			<td><digi:trn>Report</digi:trn></td>
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