<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<div class="budgetcontainer">
	<span class="crumb"><a href="/aim/admin.do" class="comment" title="Click here to goto Admin Home">Admin Home</a>&nbsp;&gt;&nbsp;Budget export manager</span>
	<h1 class="subtitle-blue">Budget export manager</h1> 
<digi:instance property="beBudgetExportForm"/>

    
<digi:form action="/showProjectList.do" method="post">
<div style="padding:15px;" class="budget-table">
	<table style="border-collapse:collapse;" border="0"style="margin:25px;">
		<logic:present name="beBudgetExportForm" property="projects">
			<logic:notEmpty name="beBudgetExportForm" property="projects">
				<tr>
					<td class="box-title" style="background:#d7eafd;text-align:center;padding:3px;">Name</td>
					<td class="box-title" style="background:#d7eafd;text-align:center;padding:3px;">Description</td>
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
								[&nbsp;<a href="/budgetexport/addEditDeleteProject.do?action=edit&id=<bean:write name="project" property="id"/>">Edit</a>&nbsp;]
							</td class="budget-table">
							<td class="budget-table">
								[&nbsp;<a href="/budgetexport/addEditDeleteProject.do?action=delete&id=<bean:write name="project" property="id"/>">Delete</a>&nbsp;]
							</td>
							<td class="budget-table">
								[&nbsp;<a href="/budgetexport/showMapingRuleList.do?id=<bean:write name="project" property="id"/>">Mapping rules</a>&nbsp;]
							</td>
						</tr>
					</logic:iterate>
			</logic:notEmpty>
		</logic:present>
		<logic:present name="beBudgetExportForm" property="projects">
			<logic:empty name="beBudgetExportForm" property="projects">
				<tr><td>No Projects</td></tr>
			</logic:empty>
		</logic:present>
		<logic:notPresent name="beBudgetExportForm" property="projects">
			<tr><td>No Projects</td></tr>
		</logic:notPresent>
		
	</table>		
        </div>
        <div class="otherlinkstable">
        
								<table align="center" cellpadding="0" cellspacing="0" width="120" border="0">	
							<tbody><tr>
								<td>
									<!-- Other Links -->
									<table cellpadding="0" cellspacing="0" width="100">
										<tbody><tr>
											<td bgcolor="#c9c9c7" class="box-title">
												Other links
											</td>
											<td background="module/aim/images/corner-r.gif" height="17" width="17">
												&nbsp;
											</td>
										</tr>
									</tbody></table>
								</td>
							</tr>
							<tr>
								<td bgcolor="#ffffff" class="box-border">
									<table cellpadding="5" cellspacing="1" width="100%">
										
										<tbody><tr>
											<td>
											
												
												
												<img src="/TEMPLATE/ampTemplate/module/aim/images/arrow-014E86.gif" height="10" width="15">
												
												<a href="/budgetexport/addEditDeleteProject.do?action=add" title="Click here to Add a Sector">Add Project</a>
											</td>
										</tr>
										<tr>
											<td>
												<img src="/TEMPLATE/ampTemplate/module/aim/images/arrow-014E86.gif" height="10" width="15">
												
												<a href="/aim/admin.do" title="Click here to goto Admin Home">Admin Home</a>
											</td>
										</tr>
										<!-- end of other links -->
									</tbody></table>
								</td>
							</tr>
						</tbody></table>
								
        </div>
        <div style="clear:both;"></div>
        </digi:form>
        </div>
