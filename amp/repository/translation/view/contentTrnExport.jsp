<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/c.tld" prefix="c"%>

<digi:instance property="contentTrnImportExportForm"/>
<h1 class="admintitle">
			<span class="subtitle-blue"><digi:trn key="gis:widgetPlaceMan:pageHeader">Export Content Translations</digi:trn></span>
</h1>

<digi:form action="/contentTrnImportExport.do?action=export" method="post">
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
					<td nowrap="nowrap" class="inside" width="10">
						<input type="checkbox" id="selAll" checked>
					</td>
				</tr>
		
		
		
	<logic:iterate id="existingTrnType" name="contentTrnImportExportForm" property="existingTrnTypes">
		<tr>
			<td nowrap="nowrap" class="inside"><bean:write name="existingTrnType" property="objectDisplayName"/></td>
			<td nowrap="nowrap" class="inside"><bean:write name="existingTrnType" property="langsAsString"/></td>
				<td nowrap="nowrap" class="inside" width="10">
					<html:multibox name="contentTrnImportExportForm" property="selectedContentTypes">
						<bean:write name="existingTrnType" property="objectType"/>
					</html:multibox>
				</td>
		</tr>	
	</logic:iterate>
	</table>	
	<html:submit value="Export"/>
	</digi:form>
	
		</td>
	</tr>
</table>

<script language="javascript">
	
	$("input[name='selectedContentTypes']").bind( "change", function() {
		if ($("input[name='selectedContentTypes']:not(:checked)").size() > 0) {
			$("#selAll").prop("checked", false);
		} else {
			$("#selAll").prop("checked", true);
		}
		
	});
	
	$("#selAll").bind( "change", function() {
		$("input[name='selectedContentTypes']").prop("checked", $("#selAll").prop("checked"));
	});
</script>