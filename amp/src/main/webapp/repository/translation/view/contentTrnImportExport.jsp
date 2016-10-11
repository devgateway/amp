<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:instance property="contentTrnImportExportForm"/>
<h1 class="admintitle">
			<span class="subtitle-blue"><digi:trn key="gis:widgetPlaceMan:pageHeader">Content Translation Manager</digi:trn></span>
</h1>

<table width="100%" border="0" cellpadding="0">
	<tr>
		<td>	
	
			<table border="0" width="100%"  class="inside">
				<tr style="background-color:#c7d4db">
					<td nowrap="nowrap" class="inside" width="90%">
						<strong><digi:trn key="gis:widgetPlacesTable:lastRenderTime">Object Type</digi:trn></strong>
					</td>
					<td nowrap="nowrap" class="inside" width="10%">
						<strong><digi:trn key="gis:widgetPlacesTable:placeName">Content Languages</digi:trn></strong>
					</td>
				</tr>
		
		
		
	<logic:iterate id="existingTrnType" name="contentTrnImportExportForm" property="existingTrnTypes">
		<tr>
			<td nowrap="nowrap" class="inside"><bean:write name="existingTrnType" property="objectDisplayName"/></td>
			<td nowrap="nowrap" class="inside"><bean:write name="existingTrnType" property="langsAsString"/></td>
		</tr>	
	</logic:iterate>
	</table>	
	
	
		</td>
	</tr>
	<tr><td><a href="/translation/contentTrnImportExport.do?action=showExport">Export</a></td></tr>
	<tr><td><a href="/translation/contentTrnImportExport.do?action=showImport">Import</a></td></tr>
</table>