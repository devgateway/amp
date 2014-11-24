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
		<span><a href="/aim/admin.do" class="comment" title="<digi:trn>Click here to goto Admin Home</digi:trn>"><digi:trn>Admin Home</digi:trn></a>&nbsp;&gt;&nbsp;<digi:trn>Budget Export Manager</digi:trn></span>
		<br>
		

<!-- MAIN CONTENT PART START -->
<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center>
  <tr>
    <td class="main_side_1">
	<div class="wht">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign=top width=712>
	<table class="inside" width=712 cellpadding="0" cellspacing="0" border=1>
<tr>
<td colspan=4 align=center background="/TEMPLATE/ampTemplate/images/ins_header.gif" class=inside><b class="ins_header"><digi:trn>Budget Export Manager</digi:trn></b></td>
</tr>
<tr>
    <td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside width=30%><b class="ins_header" style="font-size:11px;"><digi:trn>Name</digi:trn></b></td>
    <td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside width=30%><b class="ins_header" style="font-size:11px;"><digi:trn>Description</digi:trn></b></td>
    <td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside width=40%><b class="ins_header" style="font-size:11px;"><digi:trn>Options</digi:trn></b></td>
    </tr>
    
    <logic:present name="beBudgetExportForm" property="projects">
			<logic:notEmpty name="beBudgetExportForm" property="projects">
				<logic:iterate name="beBudgetExportForm" property="projects" id="project">
    
<tr>
    <td class=inside><bean:write name="project" property="name"/></td>
    <td class=inside><bean:write name="project" property="description"/></td>
	<td class=inside>
		<a href="/budgetexport/addEditDeleteProject.do?action=edit&id=<bean:write name="project" property="id"/>"><digi:trn>Edit</digi:trn></a> | 
		<a class="delete_confirm" href="/budgetexport/addEditDeleteProject.do?action=delete&id=<bean:write name="project" property="id"/>"><digi:trn>Delete</digi:trn></a> | 
		<a href="/budgetexport/showMapingRuleList.do?id=<bean:write name="project" property="id"/>"><digi:trn>Mapping Rules</digi:trn></a> | 
		<a target="_blank" href="/budgetexport/showBudgetReport.do?ampReportId=${project.ampReportId}&projectId=${project.id}"><digi:trn>Show Report</digi:trn></a>
	</td>
</tr>

</logic:iterate>
			</logic:notEmpty>
		</logic:present>
		<logic:present name="beBudgetExportForm" property="projects">
			<logic:empty name="beBudgetExportForm" property="projects">
				<tr><td colspan="3" class=inside>No Projects</td></tr>
			</logic:empty>
		</logic:present>
		<logic:notPresent name="beBudgetExportForm" property="projects">
			<tr><td colspan="3" class=inside>No Projects</td></tr>
		</logic:notPresent>
		
	</table>
	</td>
    <td width=20>&nbsp;</td>
	    <td valign="top" width=220>
		<table align="center" width="100%" cellspacing="0" cellpadding="0" border="0" style="font-size:12px;">
								<tbody><tr>
									<td style="border-bottom:1px solid #ccc;">
										<!-- Other Links -->
										<table width="100" cellspacing="0" cellpadding="0">
											<tbody><tr>
												<td bgcolor="#c9c9c7" style="font-size:12px;" class="box-title">
													&nbsp;&nbsp;<digi:trn>Other links</digi:trn>
												</td>
												<td width="17" height="17" background="images/corner-r.gif"></td>
											</tr>
										</tbody></table>
									</td>
								</tr>
								<tr>
									<td bgcolor="#ffffff" style="border-right:1px solid #ccc;">
										<table width="100%" cellspacing="1" cellpadding="5" class="inside">
											<tbody><tr>
												<td>
													<img width="15" height="10" src="images/arrow-014E86.gif">
														
													
													
													 <a href="/aim/admin.do" title="<digi:trn>Click here to goto Admin Home</digi:trn>"><digi:trn>Admin Home</digi:trn></a>
												</td>
											</tr>
											<tr>
												<td>
													<img width="15" height="10" src="images/arrow-014E86.gif">
														
													
													
													 <a href="/budgetexport/addEditDeleteProject.do?action=add" title="<digi:trn>Click here to Add Export Project</digi:trn>"><digi:trn>Add Project</digi:trn></a>
												</td>
											</tr>
											


											<!-- end of other links -->
										</tbody></table>
									</td>
								</tr>
							</tbody></table>
		</td>
  </tr>
</table>

	</div>
	
	</td>
  </tr>
</table>
</digi:form>
<!-- MAIN CONTENT PART END -->

<script language="javascript">
	$('.delete_confirm').click(function (obj) {
		if (!confirm ("Are you shure you want to delete budget export project ?")) {
			obj.preventDefault();
		}
	});
</script>
	