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
	
<digi:form action="/mapActions.do" method="post" enctype="multipart/form-data">
	<html:hidden name="beMapActionsForm" property="id"/>
	<html:hidden name="beMapActionsForm" property="ruleId"/>
	<html:hidden name="beMapActionsForm" property="projectId"/>
	
	<table><tr><td>
	
	<table style="border-collapse:collapse;" border="1">
		<tbody>
			<tr>
				<td width="300">
					AMP Label
				</td>
				<td width="300">
					External code/label
				</td>
				<td width="20">
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
				<td colspan="3">
					<table width="100%">
						<tr>
							<td align="left">
								<input type="button" class="button" value="Automatic matching" onClick="automatch(this)">
								<input type="button" class="button" value="Save" onClick="saveMapping(this)"/>	
							</td>
							<td align="right">
								<html:file name="beMapActionsForm" property="upload"/>
								<input type="button" value="Upload" onClick="uploadFile(this)"/>
							</tr>
						</tr>
					</table>
				</td>
			</tr>
	</tbody>
</table>
</digi:form>

<script language="JavaScript" src="/repository/budgetexport/view/js/budgetexport.js"></script>