
function validateFile(oForm) {
    var arrInputs = oForm.getElementsByTagName("input");
    var validExtension = ".csv";
    for (var i = 0; i < arrInputs.length; i++) {
        var oInput = arrInputs[i];
        if (oInput.type == "file") {
            var sFileName = oInput.value;
            if (sFileName.length > 0) {
            	if (sFileName.substr(sFileName.length - validExtension.length, validExtension.length).toLowerCase() == validExtension){
            		return true;
            	} else {
            		return false;
            	}
            }
        }
    }
    return false;
}

function uploadFile(obj) {
	
		if (validateFile(obj.form)){
			obj.form.setAttribute("accept-charset", "UTF-8");
			obj.form.action=obj.form.action + "?action=upload";
			obj.form.submit();
		} else {
			alert(trnAlertBadFile);
		}
	}
	
	function updateFromService(obj) {
			obj.form.setAttribute("accept-charset", "UTF-8");
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
		autocompleteActionDelayHandler = window.setTimeout(autocompleteRequestSendEvt, 500);
		e.stopPropagation();
	}
	
	var autocompleteRequestSendEvt = function () {
		autocompleteRequestSend();
	}
	
	var autocompleteRequestSend = function (getAll) {
		var url = "../../budgetexport/mapActions.do?action=autocomplete";
		var searchIn = activeAutocompleteInput.parent().hasClass("be_autocomplete_label_cell")?"label":"code";
		var searchString = getAll==true?"":activeAutocompleteInput.val();
		$.ajax({
		  type: 'POST',
		  url: url,
		  data:{searchStr:searchString , searchIn:searchIn},
		  success: autocompleteRequestSuccess,
		  dataType: "xml"
		});
	}
	
	var autocompleteRequestSuccess = function (data, textStatus, jqXHR) {
		$("#autocomplete_dropdown").prev().remove();
		$("#autocomplete_dropdown").remove();
		var results = data.getElementsByTagName("item");
		if (results != null && results.length > 0) {
			var resultIdx;
			var componentSrc = [];
			componentSrc.push("<br><div id='autocomplete_dropdown' class='autosuggest_dropdown_container'");
			if (jQuery.browser.msie) {
				componentSrc.push(" style='height:250px' ");
			}
			componentSrc.push(">");
			componentSrc.push("<table width='100%'>");
			for (resultIdx = 0; resultIdx < results.length; resultIdx ++) {
				var result = results[resultIdx];
				var code = result.attributes.getNamedItem("code").value;
				var label = result.childNodes[0].nodeValue;
				
				componentSrc.push("<tr><td nowrap><div class='autosuggest_dropdown_item' onClick='setManualMapping(this, \"");
				componentSrc.push(code);
				componentSrc.push("\",\"");
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
		} else if (activeAutocompleteInput.parent().hasClass("be_autocomplete_code_cell")) {
			var componentSrc = [];
			componentSrc.push("<br><div id='autocomplete_dropdown' class='autosuggest_dropdown_container autosuggest_custom_code' onClick='setCustomCode(this)'>");
			componentSrc.push("Set this code");
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
	
	function setCustomCode (obj) {
		var rowAmpId = activeAutocompleteCell.parent().parent().find(".amp_id_holder").val();
		var CUSTOM_CODE = "CUSTOM CODE";
		
		activeAutocompleteInput.css("display","none");
		activeAutocompleteCell.parent().parent().find(".be_autocomplete_code_cell").find(".be_autocomplete_static_text").html(activeAutocompleteInput.val()).css("display","block");
		activeAutocompleteCell.parent().parent().find(".be_autocomplete_label_cell").find(".be_autocomplete_static_text").html(CUSTOM_CODE).css("display","block");

		activeAutocompleteInput.remove();
		$("#autocomplete_dropdown").prev().remove();
		$("#autocomplete_dropdown").remove();

		var url = "../../budgetexport/mapActions.do?action=updateMappingItem";
		$.ajax({
		  type: 'POST',
		  url: url,
		  data:{importedCode:activeAutocompleteInput.val(), importedLabel:CUSTOM_CODE, ampObjId:rowAmpId},
		  success: manualMappingSuccess		 
		});
	}
	
	function setManualMapping(obj, code, label) {
		var rowAmpId = activeAutocompleteCell.parent().parent().find(".amp_id_holder").val();
		activeAutocompleteInput.val(obj.innerHTML);
		activeAutocompleteInput.css("display","none");
		activeAutocompleteCell.parent().parent().find(".be_autocomplete_code_cell").find(".be_autocomplete_static_text").html(code).css("display","block");
		activeAutocompleteCell.parent().parent().find(".be_autocomplete_label_cell").find(".be_autocomplete_static_text").html(label).css("display","block");

		activeAutocompleteInput.remove();
		$("#autocomplete_dropdown").prev().remove();
		$("#autocomplete_dropdown").remove();

		var url = "../../budgetexport/mapActions.do?action=updateMappingItem";
		$.ajax({
		  type: 'POST',
		  url: url,
		  data:{importedCode:code, importedLabel:label, ampObjId:rowAmpId},
		  success: manualMappingSuccess		 
		});
	}
	
	var curentApprovalToggleItem = null;
	
	function toggleApproval(rowAmpId) {
		curentApprovalToggleItem = $("#approval_btn_" + rowAmpId);
		var approved = curentApprovalToggleItem.hasClass("map_approved") ? false:true;
		var url = "../../budgetexport/mapActions.do?action=toggleApprovalStatus";
		$.ajax({
		  type: 'POST',
		  url: url,
		  data:{approved:approved, ampObjId:rowAmpId},
		  success: toggleApprovalSuccess		 
		});
	}
	
	var toggleApprovalSuccess = function (data, textStatus, jqXHR) {
		curentApprovalToggleItem.toggleClass("map_approved").toggleClass("map_unapproved");
		if (curentApprovalToggleItem.hasClass("map_approved")) {
			curentApprovalToggleItem.find("img").attr("src", "img_2/ico_validate.gif");
		} else {
			curentApprovalToggleItem.find("img").attr("src", "img_2/ico_validate_red.gif");
		}
		
	}
	
	function setManualMapping(obj, code, label) {
		var rowAmpId = activeAutocompleteCell.parent().parent().find(".amp_id_holder").val();
		activeAutocompleteInput.val(obj.innerHTML);
		activeAutocompleteInput.css("display","none");
		activeAutocompleteCell.parent().parent().find(".be_autocomplete_code_cell").find(".be_autocomplete_static_text").html(code).css("display","block");
		activeAutocompleteCell.parent().parent().find(".be_autocomplete_label_cell").find(".be_autocomplete_static_text").html(label).css("display","block");

		activeAutocompleteInput.remove();
		$("#autocomplete_dropdown").prev().remove();
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
		activeAutocompleteCell.parent().parent().parent().parent().parent().next().attr("bgcolor", "blue");
		activeAutocompleteCell.parent().parent().parent().parent().parent().next().next().find("img").attr("src", "img_2/ico_validate.gif");
		activeAutocompleteCell.parent().parent().parent().parent().parent().next().next().find("a").toggleClass("map_approved").toggleClass("map_unapproved");
		
	}
	
	var activeAutocompleteInput = null;
	var activeAutocompleteCell = null;
	
	autocompleteClick = function (e) {
		$("#autocomplete_dropdown").prev().remove();
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
		activeAutocompleteInput.keyup(autocompKeyPress);
		autocompleteRequestSend(true);
		
		
	};
	
	
	
	$(".be_autocomplete_static_text").click(autocompleteClick);
	//$(".be_autocomplete_label_cell").click(autocompleteClick);
	
	