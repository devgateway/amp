function uploadFile(obj) {
		obj.form.action=obj.form.action + "?action=upload";
		obj.form.submit();
		
	}
	
	function saveMapping(obj) {
		obj.form.action=obj.form.action + "?action=save";
		obj.form.submit();
	}
	
	function automatch (obj) {
		obj.form.action=obj.form.action + "?action=automatch";
		obj.form.submit();
	}
	
	
	var activeAutocompleteField = null;
	var autocompleteActionDelayHandler = null;
	
	var autocompKeyPress = function (e) {
		if (autocompleteActionDelayHandler != null) {
			window.clearTimeout(autocompleteActionDelayHandler);
		}
		autocompleteActionDelayHandler = window.setTimeout(autocompleteRequestSend, 1000);
	}
	
	var autocompleteRequestSend = function () {
		var url = "../../budgetexport/mapActions.do?action=autocomplete";
		var searchIn = activeAutocompleteInput.parent().hasClass("be_autocomplete_label_cell")?"label":"code";
		//console.log(activeAutocompleteInput);
		$.ajax({
		  type: 'POST',
		  url: url,
		  data:{searchStr: activeAutocompleteInput.val(), searchIn:searchIn},
		  success: autocompleteRequestSuccess,
		  dataType: "xml"
		});
	}
	
	var autocompleteRequestSuccess = function (data, textStatus, jqXHR) {
		$("#autocomplete_dropdown").remove();
		var results = data.getElementsByTagName("item");
		
		if (results != null && results.length > 0) {
			var resultIdx;
			var componentSrc = [];
			componentSrc.push("<div id='autocomplete_dropdown' class='autosuggest_dropdown_container'>");
			componentSrc.push("<table>");
			for (resultIdx = 0; resultIdx < results.length; resultIdx ++) {
				var result = results[resultIdx];
				var code = result.attributes.getNamedItem("code").value;
				var label = result.childNodes[0].nodeValue;
				
				componentSrc.push("<tr><td nowrap><div class='autosuggest_dropdown_item' onClick='setManualMapping(this, ");
				componentSrc.push(code);
				componentSrc.push(",\"");
				componentSrc.push(label);
				componentSrc.push("\")'>(");
				componentSrc.push(code);
				componentSrc.push(") ");
				componentSrc.push(label);
				componentSrc.push("</div></td></tr>");
			}
			componentSrc.push("</table>");
			componentSrc.push("</div>");
			
			activeAutocompleteInput.parent().append(componentSrc.join(""));
		}
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
		activeAutocompleteField = textFld;
		
	});
	
	function setManualMapping(obj, code, label) {
		var rowAmpId = activeAutocompleteCell.parent().parent().find(".amp_id_holder").val();
		activeAutocompleteInput.val(obj.innerHTML);
		activeAutocompleteInput.css("display","none");
		activeAutocompleteCell.parent().parent().find(".be_autocomplete_code_cell").find(".be_autocomplete_static_text").html(code).css("display","block");
		activeAutocompleteCell.parent().parent().find(".be_autocomplete_label_cell").find(".be_autocomplete_static_text").html(label).css("display","block");

		activeAutocompleteInput.remove();
		$("#autocomplete_dropdown").remove();

		var url = "../../budgetexport/mapActions.do?action=updateMappingItem";
		$.ajax({
		  type: 'POST',
		  url: url,
		  data:{importedCode:code, importedLabel:label, ampObjId:rowAmpId},
		  success: manualMappingSuccess		 
		});
	}
	
	
	var manualMappingSuccess = function (data, textStatus, jqXHR) {
		var matchLevelCell = activeAutocompleteCell.parent().parent().parent().parent().parent().next();
		matchLevelCell.attr("bgcolor", "blue");
	}
	
	var activeAutocompleteInput = null;
	var activeAutocompleteCell = null;
	
	autocompleteClick = function (e) {
		$("#autocomplete_dropdown").remove();
		if (activeAutocompleteInput) {
			activeAutocompleteInput.remove();
			if (activeAutocompleteCell) {
				activeAutocompleteCell.css("display","block");
			}
		}
		
			
		activeAutocompleteCell = $(this);
		activeAutocompleteCell.css("display","none");
		
		var autocompliteInputMarkup = "<input type='text' class='be_autocomplete_input'>";
		activeAutocompleteCell.parent().append(autocompliteInputMarkup);
		activeAutocompleteInput = activeAutocompleteCell.parent().find(".be_autocomplete_input");
		activeAutocompleteInput.focus();
		activeAutocompleteInput.val(activeAutocompleteCell.html().trim());
		activeAutocompleteInput.keypress(autocompKeyPress);
		
		
	};
	
	
	
	$(".be_autocomplete_static_text").click(autocompleteClick);
	//$(".be_autocomplete_label_cell").click(autocompleteClick);
	
	