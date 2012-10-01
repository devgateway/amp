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
<script type="text/javascript">
var trnAlertBadFile = "<digi:trn jsFriendly='true'>Invalid file extension</digi:trn>";

</script>

<digi:ref href="/repository/budgetexport/view/css/budgetexport.css" type="text/css" rel="stylesheet" />	
	

	<span><a href="/aim/admin.do" class="comment" title="<digi:trn>Click here to goto Admin Home</digi:trn>"><digi:trn>Admin Home</digi:trn></a>&nbsp;&gt;&nbsp;<a href="/budgetexport/showProjectList.do" class="comment" title="<digi:trn>Click here to goto Project List</digi:trn>"><digi:trn>Budget Export Projects</digi:trn></a>&nbsp;&gt;&nbsp;<digi:trn>Manage Mapings</digi:trn></span>

	
<digi:form action="/mapActions.do" method="post" enctype="multipart/form-data">
	<html:hidden name="beMapActionsForm" property="id"/>
	<html:hidden name="beMapActionsForm" property="ruleId"/>
	<html:hidden name="beMapActionsForm" property="projectId"/>
	
	

<!-- MAIN CONTENT PART START -->
<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center>
  <tr>
    <td class="main_side_1">
	<div class="wht">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign=top width=712>
	<table class="inside" width="100%" cellpadding="0" cellspacing="0" border=1>
<tr>
<td colspan=4 align=center background="images/ins_header.gif" class=inside><b class="ins_header"><digi:trn>Manage Mappings</digi:trn></b></td>
</tr>
<tr>
		<logic:equal name="beMapActionsForm" property="additionalLabelCol" value="false">
    	<td background="images/ins_bg.gif" class=inside width=50%><b class="ins_header" style="font-size:11px;"><digi:trn>AMP Label</digi:trn></b></td>
    </logic:equal>
    <logic:equal name="beMapActionsForm" property="additionalLabelCol" value="true">
    	<td background="images/ins_bg.gif" class=inside width=40%><b class="ins_header" style="font-size:11px;"><digi:trn>AMP Label</digi:trn></b></td>
    	<td background="images/ins_bg.gif" class=inside width=10%>&nbsp</td>
    </logic:equal>
    <td background="images/ins_bg.gif" class=inside width=50%><b class="ins_header" style="font-size:11px;"><digi:trn>External Code/Label</digi:trn></b></td>
    <td background="images/ins_bg.gif" class=inside width=150><b class="ins_header" style="font-size:11px;">&nbsp;&nbsp;<digi:trn>Matching</digi:trn>&nbsp;&nbsp;</b></td>
    </tr>

<logic:present name="beMapActionsForm" property="ampEntityMappedItems">
					<logic:iterate name="beMapActionsForm" property="ampEntityMappedItems" id="ampEntityMappedItem">
						<tr>
							<td nowrap class="inside">
								<span style="height:12px; width:400px; max-width:400px; overflow-x:hidden; display:block;" title="<bean:write name="ampEntityMappedItem" property="ampEntity.label"/>">
									<bean:write name="ampEntityMappedItem" property="ampEntity.label"/>
								</span>
							</td>
							<logic:equal name="beMapActionsForm" property="additionalLabelCol" value="true">
								<td nowrap class="inside">
									<span style="height:12px; width:100px; max-width:100px; overflow-x:hidden; display:block;" title="<bean:write name="ampEntityMappedItem" property="ampEntity.additionalSearchString"/>">
										<bean:write name="ampEntityMappedItem" property="ampEntity.additionalSearchString"/>
									</span>
								</td>
							</logic:equal>
							<td class="be_autocomplete_cell inside">
								<table width="100%" style="border-collapse:collapse;border-color:silver;" border="1">
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
											<!--
											<td width="1">&nbsp;</td>
											-->
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


	</table>
	<br />
	<table width="100%"  border="0">
  <tr>
    <td>
    	<input type="button" class="buttonx" value="<digi:trn>Automatic matching</digi:trn>" onClick="automatch(this)">
			<input type="button" class="buttonx" value="<digi:trn>Save</digi:trn>" onClick="saveMapping(this)"/>	
    </td>
    <td align=right style="font-size:11px;">

    	<logic:present name="beMapActionsForm" property="rule.csvItems">
				<bean:size name="beMapActionsForm" property="rule.csvItems" id="csvItemCount"/>
				<digi:trn>CSV Items uploaded for current rule</digi:trn> (<bean:write name="csvItemCount"/>)
			</logic:present>
			<html:file name="beMapActionsForm" property="upload" styleClass="button"/>
			<input type="button" class="buttonx" value="<digi:trn>Upload</digi:trn>" onClick="uploadFile(this)"/>
    </td>
  </tr>
</table>

	</td>
    
  </tr>
</table>

	</div>
	
	</td>
  </tr>
</table>

<!-- MAIN CONTENT PART END -->


	
	
	
	
</digi:form>

<script language="JavaScript" src="/repository/budgetexport/view/js/budgetexport.js"></script>