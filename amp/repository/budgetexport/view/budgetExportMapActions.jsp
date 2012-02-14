<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ page import="org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapItem" %>

<digi:instance property="beMapActionsForm"/>


<digi:ref href="/repository/budgetexport/view/css/budgetexport.css" type="text/css" rel="stylesheet" />	
	
<div class="budgetcontainer">
	<span class="crumb"><a href="/aim/admin.do" class="comment" title="Click here to goto Admin Home">Admin Home</a>&nbsp;&gt;&nbsp;<a href="/budgetexport/showProjectList.do" class="comment" title="Click here to goto Project List">Budget Export Projects</a>&nbsp;&gt;&nbsp;Manage Mapings</span>
	<h1 class="subtitle-blue">Manage Mappings</h1>
	
	
<digi:form action="/mapActions.do" method="post" enctype="multipart/form-data">
	<html:hidden name="beMapActionsForm" property="id"/>
	<html:hidden name="beMapActionsForm" property="ruleId"/>
	<html:hidden name="beMapActionsForm" property="projectId"/>
	
	<table width="100%" border="0"><tr><td width="60%">
		<table><tr><td valign="top"><br><br>
		
		<table style="border-collapse:collapse;" border="1" width="100%">
			<tbody>
				<tr>
					<td class="box-title" style="background:#d7eafd;text-align:center;padding:3px;" width="300">
						AMP Label
					</td>
					<td class="box-title" style="background:#d7eafd;text-align:center;padding:3px;" width="300">
						External code/label
					</td>
					<td class="box-title" style="background:#d7eafd;text-align:center;padding:3px;" width="20">
						Matching
					</td>
				</tr>
				<logic:present name="beMapActionsForm" property="ampEntityMappedItems">
					<logic:iterate name="beMapActionsForm" property="ampEntityMappedItems" id="ampEntityMappedItem">
						<tr>
							<td nowrap width="400" style="width:400px;">
								<span style="height:12px; width:400px; max-width:400px; overflow-x:hidden; display:block;" title="<bean:write name="ampEntityMappedItem" property="ampEntity.label"/>">
									<bean:write name="ampEntityMappedItem" property="ampEntity.label"/>
								</span>
							</td>
							<td class="be_autocomplete_cell">
								<table width="100%">
									<tbody>
										<tr>
											<td width="50" class="be_autocomplete_code_cell">
												<html:hidden name="ampEntityMappedItem" property="ampEntity.uniqueId" styleClass="amp_id_holder"/>
												<div class="be_autocomplete_static_text">
													<logic:present name="ampEntityMappedItem" property="mapItem">
														<bean:write name="ampEntityMappedItem" property="mapItem.importedCode"/>
													</logic:present>
												</div>
											</td>
											<td align="left" class="be_autocomplete_label_cell">
												<div class="be_autocomplete_static_text">
													<logic:present name="ampEntityMappedItem" property="mapItem">
														<bean:write name="ampEntityMappedItem" property="mapItem.importedLabel"/>
													</logic:present>
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</td>
							<logic:present name="ampEntityMappedItem" property="mapItem">
								<logic:equal name="ampEntityMappedItem" property="mapItem.matchLevel" value="1">
									<td width="10" bgcolor="yellow">&nbsp</td>
								</logic:equal>
								<logic:equal name="ampEntityMappedItem" property="mapItem.matchLevel" value="2">
									<td width="10" bgcolor="green">&nbsp</td>
								</logic:equal>
								<logic:equal name="ampEntityMappedItem" property="mapItem.matchLevel" value="3">
									<td width="10" bgcolor="blue">&nbsp</td>
								</logic:equal>
							</logic:present>
							<logic:notPresent name="ampEntityMappedItem" property="mapItem">
								<td width="10" bgcolor="red">&nbsp</td>
							</logic:notPresent>
						</tr>
					</logic:iterate>
				</logic:present>
				<tr>
					<td colspan="3" align="left">
						<table width="100%"><tr><td align="left">
							<input type="button" class="button" value="Automatic matching" onClick="automatch(this)">
							<input type="button" class="button" value="Save" onClick="saveMapping(this)"/>	
						</td><td align="right">
							<logic:present name="beMapActionsForm" property="rule.csvItems">
								<bean:size name="beMapActionsForm" property="rule.csvItems" id="csvItemCount"/>
								CSV Items uploaded for current rule (<bean:write name="csvItemCount"/>)
							</logic:present>
							<html:file name="beMapActionsForm" property="upload" styleClass="button"/>
							<input type="button" class="button" value="Upload" onClick="uploadFile(this)"/>	
						</td></tr></table>
					</td>
				</tr>
		</tbody>
	</table>
</td>
<%--
<td width="40%" valign="top" height="100%">
        
						<table align="center" cellpadding="0" cellspacing="0" width="100%" height="100%" border="0">	
							<tbody>
								<tr>
								<td>
									<!-- Other Links -->
									<table cellpadding="0" cellspacing="0" width="100">
										<tbody>
											<tr>
												<td bgcolor="#c9c9c7" class="box-title">
													CSV File
												</td>
												<td background="module/aim/images/corner-r.gif" height="17" width="17">
													&nbsp;
												</td>
											</tr>
										</tbody>
									</table>
								</td>
							</tr>
							<tr>
								<td bgcolor="#ffffff" class="box-border" align="right">
									<html:file name="beMapActionsForm" property="upload"/>
									<input type="button" value="Upload" onClick="uploadFile(this)"/>
								</td>
							</tr>
							<tr><td height="100%" bgcolor="#ffffff" valign="top" style="padding-top:0px; padding-bottom:0px;">
								<div style="width:100%; height:300px; overflow-x:hidden; overflow-y:auto; position:block; border:1px solid silver;">
									<table>
										<logic:present name="beMapActionsForm" property="rule.csvItems">
											<logic:iterate name="beMapActionsForm" property="rule.csvItems" id="csvItem">
												<tr>
													<td>
														<bean:write name="csvItem" property="code"/>
													</td>
													<td>
														<bean:write name="csvItem" property="label"/>
													</td>
												</tr>
											</logic:iterate>
										</logic:present>
									</table>
								</div>
							</td></tr>	
						</tbody>
					</table>

</td>
--%>

</tr></table>
	
</digi:form>
</div>
<script language="JavaScript" src="/repository/budgetexport/view/js/budgetexport.js"></script>