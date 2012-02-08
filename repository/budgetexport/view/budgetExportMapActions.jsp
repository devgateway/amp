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

    <div style="padding:25px;" class="budget-table">
<digi:ref href="/repository/budgetexport/view/css/budgetexport.css" type="text/css" rel="stylesheet" />	
	
<digi:form action="/mapActions.do" method="post" enctype="multipart/form-data">
	<html:hidden name="beMapActionsForm" property="id"/>
	<html:hidden name="beMapActionsForm" property="ruleId"/>
		<html:hidden name="beMapActionsForm" property="projectId"/>
	<html:file name="beMapActionsForm" property="upload"/>
	<input type="button" value="Upload" onClick="uploadFile(this)"/>
	
	<%--
	<table style="border-collapse:collapse;" border="1">
		<tr>
			<td>
				External code
			</td>
			<td>
				External label
			</td>			
			<td>
				AMP Label
			</td>
			<td>
				Match level
			</td>
		</tr>
			<logic:present name="beMapActionsForm" property="mapItems">
				<logic:iterate name="beMapActionsForm" property="mapItems" id="item">
					<tr>
						<td>
							<bean:write name="item" property="importedCode"/>
						</td>
						<td>
							<bean:write name="item" property="importedLabel"/>
						</td>			
						<td class="autosuggestable">
							<div class="autosuggest_static_container">
								<logic:present name="item" property="ampLabel">
									<bean:write name="item" property="ampLabel"/>
								</logic:present>
							</div>
							<input type="text" value="<bean:write name="item" property="ampLabel"/>" class="autosuggest_textfield">
							<input type="hidden" value="<bean:write name="item" property="importedCode"/>" class="imported_code">
								
						</td>
						
						<logic:equal name="item" property="matchLevel" value="0">
							<td bgcolor="red">
							None
						</logic:equal>
						<logic:equal name="item" property="matchLevel" value="1">
							<td bgcolor="yellow">
							Some
						</logic:equal>
						<logic:equal name="item" property="matchLevel" value="2">
							<td bgcolor="green">
							Exact
						</logic:equal>
						<logic:equal name="item" property="matchLevel" value="3">
							<td bgcolor="blue">
							Manual
						</logic:equal>
					</td>
				</tr>
			</logic:iterate>
		</logic:present>
		
		
	</table>
	
	--%>
	
	<table><tr><td>
	
	<table style="border-collapse:collapse;" border="1">
		<tr>
			<td>
				AMP Label
			</td>
			<td>
				External code/label
			</td>
			<td>
				Matching
			</td>
		</tr>
			<logic:present name="beMapActionsForm" property="ampEntityMappedItems">
				<logic:iterate name="beMapActionsForm" property="ampEntityMappedItems" id="ampEntityMappedItem">
					<tr>
						<td>
							<bean:write name="ampEntityMappedItem" property="ampEntity.label"/>
						</td>
						<td class="be_autocomplete_cell">
							<table width="100%">
								<tr>
									<td width="35" class="be_autocomplete_code_cell">
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
	</table>
	
</td><td>
	
	<%--
	<table style="border-collapse:collapse;" border="1">
		<tr>
			<td>
				Code
			</td>
			<td>
				Label
			</td>
		</tr>
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
	</table>--%>
</td>
</tr>
</table>

<input type="button" class="button" value="Automatic matching" onClick="automatch(this)">
<input type="button" class="button" value="Save" onClick="saveMapping(this)"/>	
	 </div>
</digi:form>

<script language="JavaScript" src="/repository/budgetexport/view/js/budgetexport.js"></script>