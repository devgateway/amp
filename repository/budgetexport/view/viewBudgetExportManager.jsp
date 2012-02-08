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
<div style="padding:25px;" class="budget-table">
	<table style="border-collapse:collapse;" border="1"style="margin:25px;">
		<logic:present name="beBudgetExportForm" property="projects">
			<logic:notEmpty name="beBudgetExportForm" property="projects">
				<tr>
					<td class="box-title" style="background:#d7eafd;text-align:center;">Name</td>
					<td class="box-title" style="background:#d7eafd;text-align:center;">Description</td>
                    			<td class="box-title" style="background:#d7eafd;text-align:center;"colspan="3"></td>
				</tr>
					<logic:iterate name="beBudgetExportForm" property="projects" id="project">
						<tr>
							<td class="budget-table">
								<bean:write name="project" property="name"/>
							</td>
							<td class="budget-table">
								<bean:write name="project" property="description"/>
							</td>
							<td class="budget-table">
								<a href="/budgetexport/addEditDeleteProject.do?action=edit&id=<bean:write name="project" property="id"/>">edit</a>
							</td class="budget-table">
							<td class="budget-table">
								<a href="/budgetexport/addEditDeleteProject.do?action=delete&id=<bean:write name="project" property="id"/>">delete</a>
							</td>
							<td class="budget-table">
								<a href="/budgetexport/showMapingRuleList.do?id=<bean:write name="project" property="id"/>">mapping rules</a>
							</td>
						</tr>
					</logic:iterate>
			</logic:notEmpty>
		</logic:present>
		<tr>
			<td colspan="5" align="right" class="budget-table">
				<a href="/budgetexport/addEditDeleteProject.do?action=add">Add new project</a>
			</td>		
		</tr>
	</table>		
    </div>
</digi:form>