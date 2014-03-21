/* utils for Forms in AMP-Bootstrap
 * @author Dolghier Constantin
 * 
 * 
*/

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
	if (masterDivId[0] != '#')
	{
		alert('ERROR! please prefix masterDivId with #');
		return;
	}
	_self.masterDivId = masterDivId;
	_self.dataDivId = _self.masterDivId + "_data";		// the <div> which holds the data per se - ajax refreshed
	_self.changeDivId = _self.masterDivId + "_change";	// the <div> which holds the dropdowns / buttons which change data - ajax refreshed
	_self.addItemsButtonId = _self.dataDivId + "_add";		// the "Add Program / Sector / Location" button which triggers showing the larger modify area 	
	_self.ajaxPage = ajaxPage;						// the AJAX page to call for all the interactive stuff
	_self.submitAttrName = submitAttrName;			// on submit will do an ajax post of the form {extraAction=submitActionName,  submitAttrName: ids.join(,)}
	_self.actionName = actionName;					// base name for the "submit", "show_data" and "show_add"
	_self.submitActionName = _self.actionName + "_submit";
	_self.refreshDataActionName = _self.actionName + "_refresh_data";
	_self.refreshAddActionName = _self.actionName + "_refresh_add";
	_self.selects = selects;						// the SELECT items, described like above
	
	_self.registerJsEvents();
}

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
	postConfig = {};
	postConfig['extraAction'] = select.action;
	postConfig[attrname] = _self.getIdsOf(select, -1); 

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

InteractiveFormArea.prototype.registerJsEvents = function() {
	_self = this;
	// register change listeners for Select's
	for(var i = 0; i < _self.selects.length; i++)
	{
		$(_self.masterDivId).on('change', '#' + _self.selects[i].id, function() {_self.makePost($(this).attr('id'), null);});
	}
	
	$(_self.masterDivId).on('click', _self.changeDivId + '_cancel', function() //click on "Cancel" under "Add Program / Sector / Location"
	{
		_self.hideAddArea();			
	});

	$(_self.masterDivId).on('click', _self.changeDivId + '_submit', function() //click on "Submit" under "Add Program / Sector / Location"
	{
		_self.hideAddArea();
		var selectedIds = _self.getIdsOf(_self.selects.last());		
		if (selectedIds == '')
		{
			// nothing selected -> get outta here
			return;
		}
		// we have data to POST -> now post it and refresh
		var zzz = {};
		zzz[_self.submitAttrName] = selectedIds;
		zzz['extraAction'] = _self.submitActionName;
 
		$.post(_self.ajaxPage, 
				zzz,
		function(data) {
			if (data.trim() == 'ok'){
				_self.refreshDataArea();
			} else {
				show_error_message('Error adding locations', data);
			}
		});
	});	

	$(document).ready(function(){
		
		$(_self.changeDivId).hide();
		
		$(_self.addItemsButtonId).click(function() //click on "Add Program/Sector/Location"
		{
			_self.refreshAddArea(function(){ // ajax-refresh the area, then show it
				$(_self.addItemsButtonId).hide();
				$(_self.dataDivId).disable();
				$(_self.changeDivId).show();
			});
		});	
	});
};

/**
 * hides the "Add" area (submit/cancel buttons + select's)
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

InteractiveFormArea.prototype.refreshAddArea = function(callback) {
	_self = this;
	amp_bootstrap_form_update_area(_self.ajaxPage, _self.refreshAddActionName, _self.changeDivId, callback);
};

function amp_bootstrap_form_update_area(url, action, replaceId, callback)
{
	if (replaceId[0] == '#')
		replaceId = replaceId.substring(1);
	$.post(url,
			{extraAction: action},
			function(data)
			{
				$('#' + replaceId).html(data);
				if (callback != null)
					callback();
			});
}
