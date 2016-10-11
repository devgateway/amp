<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<digi:instance property="beMapingForm"/>
<digi:form action="/showMapingRuleList" method="post">
	<html:hidden name="beMapingForm" property="id"/>
<div class="budgetcontainer">
	<span><a href="/aim/admin.do" class="comment" title="<digi:trn>Click here to goto Admin Home</digi:trn>"><digi:trn>Admin Home</digi:trn></a>&nbsp;&gt;&nbsp;<a href="/budgetexport/showProjectList.do" class="comment" title="<digi:trn>Click here to goto Project List</digi:trn>">Budget Export Projects</a>&nbsp;&gt;&nbsp;<digi:trn>Manage Mapping Rules</digi:trn></span>

	        
        
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
<td colspan=7 align=center background="/TEMPLATE/ampTemplate/images/ins_header.gif" class=inside><b class="ins_header"><digi:trn>Mapping Rules</digi:trn></b></td>
</tr>
<tr>
    <td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside><b class="ins_header" style="font-size:11px;"><b><digi:trn>Name</digi:trn></b></td>
    <td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside><b class="ins_header" style="font-size:11px;"><b><digi:trn>AMP Column</digi:trn></b></td>
    <td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside><b class="ins_header" style="font-size:11px;"><b><digi:trn>Header</digi:trn></b></td>
	<td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside><b class="ins_header" style="font-size:11px;"><b><digi:trn>Options</digi:trn></b></td>
	<td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside><b class="ins_header" style="font-size:11px;"><b><digi:trn>Total</digi:trn>/<digi:trn>Mapped</digi:trn></b></td>
	<td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside><b class="ins_header" style="font-size:11px;"><b><digi:trn>CSV items</digi:trn></b></td>
	<td background="/TEMPLATE/ampTemplate/images/ins_bg.gif" class=inside><b class="ins_header" style="font-size:11px;">&nbsp;</td>
    </tr>
    <logic:present name="beMapingForm" property="rules">
			<logic:notEmpty name="beMapingForm" property="rules">
				<logic:iterate name="beMapingForm" property="rules" id="rule">
				<tr>
				    <td class=inside><bean:write name="rule" property="name"/></td>
				    <td class=inside><bean:write name="rule" property="ampColumn.columnName"/></td>
				    <td class=inside><bean:write name="rule" property="header"/></td>
				    <td class=inside>
				    	<a href="/budgetexport/addEditDeleteMapRule.do?action=edit&id=<bean:write name="rule" property="id"/>&curProjectId=<bean:write name="beMapingForm" property="id"/>"><digi:trn>Edit</digi:trn></a> | 
				    	<a class="delete_confirm" href="/budgetexport/addEditDeleteMapRule.do?action=delete&id=<bean:write name="rule" property="id"/>&curProjectId=<bean:write name="beMapingForm" property="id"/>"><digi:trn>Delete</digi:trn></a> | 
				    	<a href="/budgetexport/mapActions.do?action=view&ruleId=<bean:write name="rule" property="id"/>&projectId=<bean:write name="beMapingForm" property="id"/>"><digi:trn>Manage map</digi:trn></a>
				    </td>
				    <td class=inside><bean:write name="rule" property="totalAmpEntityCount"/>/<bean:write name="rule" property="mappedAmpEntityCount"/></td>
				    <td class=inside><bean:write name="rule" property="csvItemCount"/></td>
				    <td class=inside><a href="/budgetexport/exportMapping.do?ruleId=<bean:write name="rule" property="id"/>"><img src="images/icons/xls.gif"></a></td>
				</tr>
			</logic:iterate>
		</logic:notEmpty>
	</logic:present>
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
                      	<td><img width="15" height="10" src="images/arrow-014E86.gif" />
                      		<a href="/budgetexport/addEditDeleteMapRule.do?action=add&curProjectId=<bean:write name="beMapingForm" property="id"/>" title="<digi:trn>Click here to Add a Rule</digi:trn>"><digi:trn>Add New Rule</digi:trn></a>
                      	</td>
											</tr>
											<tr>
                      	<td><img width="15" height="10" src="images/arrow-014E86.gif" />
                      		<a target="_blank" href="/budgetexport/showBudgetReport.do?ampReportId=${beMapingForm.ampReportId}&projectId=${beMapingForm.id}">
														<digi:trn><digi:trn>Show Report</digi:trn></digi:trn>
													</a>
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



<!-- MAIN CONTENT PART END -->

</digi:form>
<script language="javascript">
	$('.delete_confirm').click(function (obj) {
		if (!confirm ("Are you shure you want to delete budget export mapping ?")) {
			obj.preventDefault();
		}
	});
</script>