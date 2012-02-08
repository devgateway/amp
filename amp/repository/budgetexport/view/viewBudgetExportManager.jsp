<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="beBudgetExportForm"/>
	
<digi:form action="/showProjectList.do" method="post">
	<table style="border-collapse:collapse;" border="1">
		<logic:present name="beBudgetExportForm" property="projects">
			<logic:notEmpty name="beBudgetExportForm" property="projects">
				<tr>
					<td>Name</td>
					<td>Description</td>
				</tr>
					<logic:iterate name="beBudgetExportForm" property="projects" id="project">
						<tr>
							<td>
									<a href="/budgetexport/mapActions.do?action=view&projectId=<bean:write name="project" property="id"/>"><bean:write name="project" property="name"/></a>
							</td>
							<td>
								<bean:write name="project" property="description"/>
							</td>
							<td>
								<a href="/budgetexport/addEditDeleteProject.do?action=edit&id=<bean:write name="project" property="id"/>">edit</a>
							</td>
							<td>
								<a href="/budgetexport/addEditDeleteProject.do?action=delete&id=<bean:write name="project" property="id"/>">delete</a>
							</td>
							<td>
								<a href="/budgetexport/showMapingRuleList.do?id=<bean:write name="project" property="id"/>">mapping rules</a>
							</td>
						</tr>
					</logic:iterate>
			</logic:notEmpty>
		</logic:present>
		<tr>
			<td colspan="5" align="right">
				<a href="/budgetexport/addEditDeleteProject.do?action=add">Add new project</a>
			</td>		
		</tr>
	</table>		
</digi:form>