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
	
<style>
	.autosuggest_textfield {
		font-family: Verdana,Tahoma,Arial,sans-serif; 
		font-size: 10px;
		font-weight: normal;
		background-color:#fbfe9e;
		border:none;
		display:none;
		width:100%;
	}
	
	.autosuggest_static_container {
		cursor:pointer;
		width:100%;
		height:100%;
		border:none;
	}
	
	.autosuggest_dropdown_container {
		border:1px solid black;
		position:absolute;
		background-color:white;
	}

	div.autosuggest_dropdown_item {
		cursor:pointer;
		width:100%;		
	}	
	
	div.autosuggest_dropdown_item:hover {
		background-color:blue;
		color:white;
	}

	div.autosuggest_dropdown_item:active {
		background-color:navy;
		color:white;
	}	
	
</style>	
	
<digi:form action="/mapActions.do" method="post" enctype="multipart/form-data">
	<html:hidden name="beMapActionsForm" property="id"/>
	<html:hidden name="beMapActionsForm" property="ruleId"/>
	<html:file name="beMapActionsForm" property="upload"/>
	<input type="button" value="Upload" onClick="uploadFile(this)"/>
	
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
	<input type="button" value="Save" onClick="saveMapping(this)"/>
</digi:form>

<script language="javascript">
	function uploadFile(obj) {
		obj.form.action=obj.form.action + "?action=upload";
		obj.form.submit();
		
	}
	
	function saveMapping(obj) {
		obj.form.action=obj.form.action + "?action=save";
		obj.form.submit();
		
	}
	
	
	var activeAutocompliteField = null;
	var autocompliteActionDelayHandler = null;
	
	var autocompKeyPress = function (e) {
		if (autocompliteActionDelayHandler != null) {
			window.clearTimeout(autocompliteActionDelayHandler);
		}
		
		autocompliteActionDelayHandler = window.setTimeout(autocompliteRequestSend, 1000);
	}
	
	var autocompliteRequestSend = function () {
		var url = "../../budgetexport/mapActions.do?action=autocomplite";
		$.ajax({
		  type: 'POST',
		  url: url,
		  data:{searchStr: activeAutocompliteField.val()},
		  success: autocompliteRequestSuccess,
		  dataType: "xml"
		});
	}
	
	var autocompliteRequestSuccess = function (data, textStatus, jqXHR) {
		$("#autocomplite_dropdown").remove();
		var results = data.getElementsByTagName("item");
		var resultIdx;
		var componentSrc = [];
		componentSrc.push("<div id='autocomplite_dropdown' class='autosuggest_dropdown_container'>");
		componentSrc.push("<table>");
		for (resultIdx = 0; resultIdx < results.length; resultIdx ++) {
			var result = results[resultIdx];
			var id = result.attributes.getNamedItem("id").value;
			var label = result.childNodes[0].nodeValue;
			
			componentSrc.push("<tr><td><div class='autosuggest_dropdown_item' onClick='setManualMapping(this, ");
			componentSrc.push(id);
			componentSrc.push(")'>");
			componentSrc.push(label);
			componentSrc.push("</div></td></tr>");
		}
		componentSrc.push("</table>");
		componentSrc.push("</div>");
		
		activeAutocompliteField.parent().append(componentSrc.join(""));
		
		//console.log (componentSrc.join(""));
	}
	
	
	$(".autosuggestable").click(function (e) {
		$(".autosuggest_static_container").css("display","block");
		$(".autosuggest_textfield").css("display","none");
		var jqObj = $(this);
		jqObj.find(".autosuggest_static_container").css("display","none");
		
		var textFld = jqObj.find(".autosuggest_textfield");
		textFld.css("display","block");
		textFld[0].focus();	
		
		textFld.keypress(autocompKeyPress);
		activeAutocompliteField = textFld;
		
	});
	
	function setManualMapping(obj, id) {
		var importedCode = activeAutocompliteField.parent().find(".imported_code").val();
		activeAutocompliteField.val(obj.innerHTML);
		activeAutocompliteField.css("display","none");
		activeAutocompliteField.parent().find(".autosuggest_static_container").html(obj.innerHTML.trim());
		activeAutocompliteField.parent().find(".autosuggest_static_container").css("display","block");
		$("#autocomplite_dropdown").remove();
		console.log(id + " - " + importedCode);

		var url = "../../budgetexport/mapActions.do?action=updateMappingItem";
		$.ajax({
		  type: 'POST',
		  url: url,
		  data:{importedCode:importedCode, ampObjId:id},
		  success: manualMappingSuccess		 
		});
	}
	
	
	var manualMappingSuccess = function (data, textStatus, jqXHR) {
		var matchLevelCell = activeAutocompliteField.parent().next();
		matchLevelCell.attr("bgcolor", "blue");
		matchLevelCell.html("Manual");
	}
	
	
	
</script>