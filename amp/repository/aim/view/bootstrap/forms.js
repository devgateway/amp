/* utils for Forms in AMP-Bootstrap
 * @author Dolghier Constantin
 * 
 * 
*/

/**
 * throws Exception if any of the arguments references a non-existant div
 */
function checkExistence()
{
	for (var i = 0; i < arguments.length; i++) {
	    var elem = $(arguments[i]);
	    if (elem.size() != 1){
	    	alert("Generic AJAX form error: element " + arguments[i] + " does not exist");
	    	throw "error";
	    }
	}
}

//function checkPercentValidation(rootElem)
//{
//	$(rootElem + "[class*=validate-percentage-").each(function(){
//		if (!$(this).hasAttr('percentage-group'))
//			alert("Generic AJAX form error: element " + $(this) + " has validate-percentage, but no percentage-group attr");
//	});
//}

function is_valid_percentage(str)
{
	return !(isNaN(str) || (str < 0) || (str > 100) || (str.length == 0));
}

function floatEquals(flt, val){
	return Math.abs(flt - val) < 0.0001;
}

function floatDiffers(flt, val){ return !floatEquals(flt, val);}

/**
 * GENERIC, e.g. non-controller-related, validation
 */

/**
 * validates an input with a generic function and highlights any found error
 * @param inputItem
 * @param validation_function
 * @param error_message
 * @returns {Boolean}
 */
function amp_bootstrap_form_simple_validation(inputItem, validation_function, error_message)
{
	if ((inputItem.value.length > 0) && (!validation_function(inputItem.value)))
	{
		show_error_message("Error", error_message);
		setValidationStatus($(inputItem), 'has-error');
		//$(inputItem).focus();
		return false;
	}
	setValidationStatus($(inputItem), 'has-success'); // ok
	clean_all_error_messages();
	return true;
}

/**
 * returns true IFF all elements have "validation ok" after onblur being fired on them all
 * @param bigDivSelector
 * @returns {Boolean}
 */
function amp_bootstrap_form_validate(bigDivSelector){
	try{
		debugger;
		forced_pnotify_stack = {"dir1": "down", "dir2": "left"}; // stack all errors	
		global_disable_cleaning_error_messages = true; // make a big queue
		
		var inputItemsSelector =  bigDivSelector + " input, " + bigDivSelector + " select, " + bigDivSelector + " textarea";
		var elem_to_focus_on = $('#pledgeForm_validate');
	
		setValidationStatus($(inputItemsSelector), "dummy_class"); // remove all validation statuses
		$(inputItemsSelector).each(function(){
			var elem = this;
			if (!hasValidationError($(elem)))
			{
				if ($(elem).is('select'))
				{
					var oldval = $(elem).val();
					$(elem).trigger('change');
					if ($(elem).val() != oldval)
						$(elem).val(oldval);
				}
				else
				{
					elem_to_focus_on.focus();
					elem.focus();
					elem_to_focus_on.focus();
				}			
				
			}
		});
		var faultyElements = [];
		$(inputItemsSelector).each(function(){
			if (hasValidationError($(this)))
				faultyElements.push(this);
			});
		return faultyElements.length == 0;
	}
	catch(e){forced_pnotify_stack = null; console.log(e);show_error_message("Validation", "error validating!");}
	finally{
		forced_pnotify_stack = null;
		global_disable_cleaning_error_messages = null;
	}
	return false;
}

function selectHasValue(selectVal){
	if (typeof(selectVal) == 'undefined')
		return false;
	if (selectVal == null)
		return false;
	if (selectVal == '')
		return false;
	if (isNaN(selectVal))
		return false;
	return parseInt(selectVal) > 0;
}

function init_validation(divId)
{
$(document).ready(function(){
	$(divId + ' .validate-phone-number').blur(function(){
		return amp_bootstrap_form_simple_validation(this, looksLikePhoneNumber, please_enter_phone_number_message);
	});

	$(divId + '.validate-email-address').blur(function(){
		return amp_bootstrap_form_simple_validation(this, looksLikeEmail, please_enter_email_message);
	});
	
	$(divId + '.validate-mandatory-number').blur(function(){
		var inputItem = this;
		if ((inputItem.value.length == 0) || (!looksLikeNumber(inputItem.value)))
		{
			show_error_message("Error", please_enter_number_message);
			setValidationStatus($(inputItem), 'has-error');
			return false;
		}
		setValidationStatus($(inputItem), 'has-success'); // ok
		clean_all_error_messages();
		return true;
	});

	$(divId + 'select.validate-mandatory').change(function(){
		return amp_bootstrap_form_simple_validation(this, selectHasValue, please_enter_something_message); 
	});
	
	$(divId + 'input.validate-mandatory, ' + divId + ' textarea.validate-mandatory').blur(function(){
		var elem = $(this);
		var error = false;
		// <input>
		// validate-min-length-5
		var classList = elem.attr('class').split(/\s+/); // generate a list of the classes the element has
		var validate_min_length = 1;
		for(var i = 0; i < classList.length; i++){
			var className = classList[i];
			if (className.indexOf("validate-min-length-") == 0){
				validate_min_length = parseInt(className.substring("validate-min-length-".length));
			};
		}
		error = elem.val().length < validate_min_length;
		if (error){
			show_error_message("Error", please_enter_something_message);
			setValidationStatus(elem, 'has-error');
			return false;
		}
		setValidationStatus(elem, 'has-success'); // ok
		clean_all_error_messages();
		return true;
	});
	
	$(divId + '.validate-year').blur(function(){
		return amp_bootstrap_form_simple_validation(this, isYearValidator, please_enter_year_message);
	});
});
}


function amp_bootstrap_forms_check_percentage(inputItem, itemsClass)
{
	if (!is_valid_percentage(inputItem.value))
	{
		show_error_message("Error", "Not a valid percentage");
		setValidationStatus($(inputItem), 'has-error');
		//$(inputItem).focus();
		return false;
	}
	setValidationStatus($('.' + itemsClass), 'has-success'); // ok
	clean_all_error_messages();
	var totalValue = 0;
	var foundError = false;
	$('.' + itemsClass).each(function(i, obj) {
		if (is_valid_percentage(obj.value))
			totalValue += parseFloat(obj.value);
		else
			foundError = true;
	});
	if (foundError || (floatDiffers(totalValue, 0) && floatDiffers(totalValue, 100))) {
		show_error_message("Error", "Sum of percentages should be either 0 or 100");
		setValidationStatus($('.' + itemsClass), 'has-warning'); // ok
		//$(inputItem).focus();
		return false;  
	}
	return true;
}

/**
 * returns an Array of (name, value) (good for jquery post) of all elements under a selector
 * @param selector
 * @returns
 */
function getFormData(selector)
{
	var relevantElems = selector + " input, " + selector + " select, " + selector + " textarea";
	return $(relevantElems).serializeArray();
}

/**
 * constructs an instance with the structure:
 * <div id="$masterDivId>
 * 		<div id="masterDivId_data">
 * 		..
 * 		(whatever#masterDivId_data_add).click -> show masterDivId_change + disable masterDivId_data
 * 		</div>
 * 		<div id="masterDivId_change">
 * 			<select id="selects[0].id"> onChange -> ajaxPost to ajaxPage{extraAction = "selects[0].action", selects[0].attr=$(this).val())}>
 * 			....
 * 			....
 * 			$('#masterDivId_change_submit').click -> ajaxPost + hide masterDivId_change + enable masterDivId_data);
 * 			$('#masterDivId_change_cancel').click -> hide masterDivId_change + enable masterDivId_data);
 * 		</div>
 *</div>
 */
function InteractiveFormArea(masterDivId, ajaxPage, submitAttrName, actionName, selects){
	var _self = this;
	if (masterDivId.charAt(0) != '#')
	{
		debugger;
		alert('ERROR! please prefix masterDivId with #');
		return;
	}
	_self.masterDivId = masterDivId;
	_self.dataDivId = _self.masterDivId + "_data";		// the <div> which holds the data per se - ajax refreshed
	_self.changeDivId = _self.masterDivId + "_change";	// the <div> which holds the dropdowns / buttons which change data - ajax refreshed
	_self.addItemsButtonId = _self.dataDivId + "_add";		// the "Add Program / Sector / Location" button which triggers showing the larger modify area
	checkExistence(_self.masterDivId, _self.dataDivId, _self.changeDivId, _self.addItemsButtonId);
	//checkPercentageValidation(_self.masterDivId);
	_self.ajaxPage = ajaxPage;						// the AJAX page to call for all the interactive stuff
	_self.submitAttrName = submitAttrName;			// on submit will do an ajax post of the form {extraAction=submitActionName,  submitAttrName: ids.join(,)}
	_self.actionName = actionName;					// base name for the "submit", "show_data" and "show_add"
	_self.submitActionName = _self.actionName + "_submit";
	_self.refreshDataActionName = _self.actionName + "_refresh_data";
	_self.refreshAddActionName = _self.actionName + "_refresh_add";
	_self.selects = selects;						// the SELECT items, described like above
	
	_self.registerJsEvents();
}

InteractiveFormArea.prototype.onDelete = function(element_id){
	var _self = this;
	var zzz = getFormData(_self.dataDivId);
	zzz.push({name: 'extraAction', value: _self.actionName + "_delete"});
	zzz.push({name: 'id', value: element_id});
	$.post(_self.ajaxPage,
			zzz,
			function(data){
				_self.refreshDataArea();
			});
};

/**
 * click on dropdown change
 * @param id
 * @param callback
 */
InteractiveFormArea.prototype.makePost = function(id, callback)
{
	_self = this;
	var select = null;
	var is_last = false;
	// find the configured select by id - stupid solution (temporary)
	for(var i = 0; i < _self.selects.length; i++)
		if (_self.selects[i].id == id)
		{
			select = _self.selects[i];
			is_last = (i == _self.selects.length - 1);
			break;
		}
	if (select == null)
	{
		alert('id not configured: ' + id);
		return;
	}
	attrname = select.attr;
	
	var postConfig = getFormData(_self.dataDivId);
	postConfig.push({name: 'extraAction', value: _self.actionName + '_' + select.action});
	postConfig.push({name: attrname, value: _self.getIdsOf(select, -1)});

	$.post(_self.ajaxPage,
			postConfig,
			function (data) {
				if (!is_last)
					_self.refreshAddArea(callback);
			});
};

/**
 * gets the selected values of a SELECT configured as given in the class' constructor
 * @param selectConfig
 * @returns
 */
InteractiveFormArea.prototype.getIdsOf = function(selectConfig, defaultValue)
{
	defaultValue = (typeof defaultValue === 'undefined') ? "" : defaultValue;
	var selectedIds = [];
	$('#' + selectConfig.id + ' option:selected').each(function ()
		{
			if (($(this).attr('value') != '0') && ($(this).attr('value') != '-1'))
				selectedIds.push($(this).attr('value'));
		});
	return selectedIds.length > 0 ? selectedIds.join(',') : defaultValue;
};

/**
 * called when validating percentages
 * @param elem
 * @returns
 */
InteractiveFormArea.prototype.onBlur = function(elem){
	var jElem = $(elem);
	var classList = jElem.attr('class').split(/\s+/); // generate a list of the classes the element has
	for(var i = 0; i < classList.length; i++){
		var className = classList[i];
		if (className.indexOf("validate-percentage-") == 0){
			return amp_bootstrap_forms_check_percentage(elem, className);	
		}
	};
	
};

/**
 * process a <select> controlled by the controller - e.g. update the "addition" area
 * @param elem
 */
InteractiveFormArea.prototype.selectChanged = function(elem){
	this.makePost($(elem).attr('id'), null);
};

InteractiveFormArea.prototype.cancelClicked = function(elem){
	this.hideAddArea();
};

/**
 * call this one to trigger "add new item"
 * @param elem the element triggering the event
 */
InteractiveFormArea.prototype.addNewItem = function(elem){ 
	this.submitClicked(elem, true);
};

InteractiveFormArea.prototype.submitClicked = function(elem, noIds) {
	_self = this;
	
	var selectedIds = '';
	if (!noIds)
	{
		this.hideAddArea();
		selectedIds = this.getIdsOf(this.selects.last());		
		if (selectedIds == '')
		{
			// nothing selected -> get outta here
			return;
		}
	}
	// we have data to POST -> now post it and refresh
	var zzz = getFormData(_self.dataDivId);
	zzz.push({name: this.submitAttrName, value: selectedIds});
	zzz.push({name: 'extraAction', value: _self.submitActionName});

	$.post(this.ajaxPage, 
			zzz,
			function(data) {
				if (data.trim() == 'ok'){
					_self.refreshDataArea();
				} else {
					show_error_message('Error adding locations', data);
				}
		});	
};

/**
 * shows the "Add Program / Sector / Location / Percentage" area
 * @param elem the element the click on which triggered the event. Ignored
 */
InteractiveFormArea.prototype.showAdditionArea = function(elem){
	_self = this;
	_self.refreshAddArea(function(){ // ajax-refresh the area, then show it
		$(_self.addItemsButtonId).hide();
		$(_self.dataDivId).disable();
		$(_self.changeDivId).show();
	});
};

/**
 * events to trigger when starting up an instance. Now only hides the "addition" area
 */
InteractiveFormArea.prototype.registerJsEvents = function() {
	_self = this;
		
	var documentReadyFunction = function(id) {
		return function() {
			$(id).hide();};}(_self.changeDivId);
	documentReadyFunction();
};

/**
 * hides the "addition" area (submit/cancel buttons + select's)
 */
InteractiveFormArea.prototype.hideAddArea = function() {
	_self = this;
	$(_self.changeDivId).hide();
	$(_self.addItemsButtonId).show();
	$(_self.dataDivId).enable();
};

/**
 * refreshes the data area
 */
InteractiveFormArea.prototype.refreshDataArea = function() {
	_self = this;
	amp_bootstrap_form_update_area(_self.ajaxPage, _self.refreshDataActionName, _self.dataDivId);
};

/**
 * refreshes the "addition" area
 * @param callback a callback to call on success. Might be null
 */
InteractiveFormArea.prototype.refreshAddArea = function(callback) {
	_self = this;
	amp_bootstrap_form_update_area(_self.ajaxPage, _self.refreshAddActionName, _self.changeDivId, callback);
};

function amp_bootstrap_form_update_area(url, action, replaceId, callback)
{
	if (replaceId.charAt(0) != '#')
		replaceId = '#' + replaceId;
	/*$.post(url,
			{extraAction: action},
			function(data)
			{
				$('#' + replaceId).html(data);
				if (callback != null)
					callback();
			});*/
	$(replaceId).load(url, {extraAction: action}, function(data){
		init_amp_magic(replaceId);
		if (callback != null)
			callback();
	});
}
