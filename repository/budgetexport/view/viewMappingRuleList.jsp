<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<div class="budgetcontainer">
	<span class="crumb"><a href="/aim/admin.do" class="comment" title="Click here to goto Admin Home">Admin Home</a>&nbsp;&gt;&nbsp;<a href="/budgetexport/showProjectList.do" class="comment" title="Click here to goto Project List">Budget Export Projects</a>&nbsp;&gt;&nbsp;Manage Mapping Rules</span>
	<h1 class="subtitle-blue">Mapping Rules</h1> 
<digi:instance property="beMapingForm"/>
	<div style="padding:15px;" class="budget-table">
<digi:form action="/showMapingRuleList" method="post">
	<html:hidden name="beMapingForm" property="id"/>
	<table style="border-collapse:collapse;" border="0">
		<logic:present name="beMapingForm" property="rules">
			<logic:notEmpty name="beMapingForm" property="rules">
				<tr class="box-title" style="background:#d7eafd;text-align:center;padding:3px;">
					<td nowrap class="box-title" style="background:#d7eafd;text-align:center;padding:3px;">Name</td>
					<td nowrap class="box-title" style="background:#d7eafd;text-align:center;padding:3px;">AMP Column</td>
					<td nowrap class="box-title" style="background:#d7eafd;text-align:center;padding:3px;">Header</td>
					<td nowrap colspan="3" class="box-title" style="background:#d7eafd;text-align:center;padding:3px;">&nbsp;</td>
					<td nowrap class="box-title" style="background:#d7eafd;text-align:center;padding:3px;">Total/Mapped</td>
					<td nowrap class="box-title" style="background:#d7eafd;text-align:center;padding:3px;">CSV items</td>
				</tr>
					<logic:iterate name="beMapingForm" property="rules" id="rule">
						<tr>
							<td nowrap class="budget-table">
								<bean:write name="rule" property="name"/>
							</td>
							<td nowrap class="budget-table">
								<bean:write name="rule" property="ampColumn.columnName"/>
							</td>
							<td nowrap class="budget-table">
								<bean:write name="rule" property="header"/>
							</td>
							<td nowrap class="budget-table">
								[&nbsp;<a href="/budgetexport/addEditDeleteMapRule.do?action=edit&id=<bean:write name="rule" property="id"/>&curProjectId=<bean:write name="beMapingForm" property="id"/>">Edit</a>&nbsp;]
							</td>
							<td nowrap class="budget-table">
								[&nbsp;<a href="/budgetexport/addEditDeleteMapRule.do?action=delete&id=<bean:write name="rule" property="id"/>&curProjectId=<bean:write name="beMapingForm" property="id"/>">Delete</a>&nbsp;]
							</td>
							<td nowrap class="budget-table">
								[&nbsp;<a href="/budgetexport/mapActions.do?action=view&ruleId=<bean:write name="rule" property="id"/>&projectId=<bean:write name="beMapingForm" property="id"/>">Manage map</a>&nbsp;]
							</td>
							<td nowrap class="budget-table">
								(<bean:write name="rule" property="totalAmpEntityCount"/>/<bean:write name="rule" property="mappedAmpEntityCount"/>)
							</td>
							<td nowrap class="budget-table">
								<bean:write name="rule" property="csvItemCount"/>
							</td>
						</tr>
					</logic:iterate>
			</logic:notEmpty>
		</logic:present>

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
											<td nowrap>
											
												
												
												<img src="/TEMPLATE/ampTemplate/module/aim/images/arrow-014E86.gif" height="10" width="15">
												
												<a href="/budgetexport/addEditDeleteMapRule.do?action=add&curProjectId=<bean:write name="beMapingForm" property="id"/>" title="Click here to Add a Rule">Add New Rule</a>
											</td>
										</tr>
										
										
										
										<tr>
											<td nowrap>
												<img src="/TEMPLATE/ampTemplate/module/aim/images/arrow-014E86.gif" height="10" width="15">
												
												<a href="/aim/admin.do" title="Click here to goto Admin Home">Admin Home</a>
											</td>
										</tr>
										
										<tr>
											<td nowrap>
												<img src="/TEMPLATE/ampTemplate/module/aim/images/arrow-014E86.gif" height="10" width="15">
												
												<a target="_blank" href="/budgetexport/reportsWrapper.do?ampReportId=${beMapingForm.ampReportId}&projectId=${beMapingForm.id}">
											<digi:trn>Show Report</digi:trn>
										</a>
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
