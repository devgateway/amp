<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="beProjectForm"/>
	
<digi:form action="/addEditDeleteProject.do?action=save" method="post">
	<html:hidden name="beProjectForm" property="id"/>
		<digi:errors/>
<div style="padding:25px;" class="budget-table">
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
			<td colspan="2">
				<html:submit value="Save" styleClass="button"/>
			</td>
		</tr>
	</table>	
    </div>	
</digi:form>