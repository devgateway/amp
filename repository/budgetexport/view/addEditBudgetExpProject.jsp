<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<div class="budgetcontainer">
	<span class="crumb"><a href="/aim/admin.do" class="comment" title="Click here to goto Admin Home">Admin Home</a>&nbsp;&gt;&nbsp;Sector Manager</span>
	<h1 class="subtitle-blue">Budget editor</h1> 
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
    </div>	<div class="otherlinkstable">
        
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
												
												<a href="#" title="Click here to Add a Sector">Add Budget</a>
											</td>
										</tr>
										
										
										<tr>
											<td>
												<img src="/TEMPLATE/ampTemplate/module/aim/images/arrow-014E86.gif" height="10" width="15">
												
												<a href="#" title="Click here to the Schemes">View Budgets</a>
											</td>
										</tr>
										
										<tr>
											<td>
												<img src="/TEMPLATE/ampTemplate/module/aim/images/arrow-014E86.gif" height="10" width="15">
												
												<a href="#" title="Click here to goto Admin Home">Admin Home</a>
											</td>
										</tr>
										<!-- end of other links -->
									</tbody></table>
								</td>
							</tr>
						</tbody></table>
								
        </div>
    <div style="clear:both;"></div>
    </div>
</digi:form>