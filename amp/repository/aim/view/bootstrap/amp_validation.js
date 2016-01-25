/**
 * generic field validator object
 */

function AmpValidator(validatorFunc, errMsg){
	_self = this;
	_self.validatorFunc = validatorFunc;
	_self.errMsg = errMsg;
	_self.allowEmpty = true;
}

AmpValidator.prototype.setAllowEmpty = function(newVal){
	this.allowEmpty = newVal;
	return this;
};

AmpValidator.prototype.setErrMsg = function(errMsg){
	this.errMsg = errMsg;
	return this;
};

/**
 * input: DOM element
 * @param elem
 * @returns {success: boolean, error_message: string, validate_class}
 */
AmpValidator.prototype.validate = function(elem){
	var _self = this;
	var result;
	if ((elem.value.trim().length == 0) && (!_self.allowEmpty))
		result = {success: false, error_message: please_enter_something_message, validate_class: false};
	else
		result = _self.validatorFunc(elem.value.trim());
	//if ('success' in result)
	if (result.hasOwnProperty('success'))
		return result; // validator returned a complex object
	return {success: result, error_message: _self.errMsg, validate_class: false};
};

/**
 * validates and highlights, according to validate.{success, error_message, validate_class}
 * @param elem
 * @returns {Boolean}
 */
AmpValidator.prototype.validateAndHighlight = function(elem){
	var _self = this;
	var res = _self.validate(elem);
	if (!res.success)
	{
		show_error_message("Error", res.error_message);
		if (res.validate_class)
			setValidationStatus($('.' + res.validate_class), 'has-warning');
		else
			setValidationStatus($(elem), 'has-error');
		return false;
	}
	setValidationStatus($(elem), 'has-success'); // ok
	return true;
};

function amp_validator_hasMinimumLength(vml){
	return function(inp){
		return inp.length >= vml;
	};
}

function amp_validator_check_percentage(itemsClass){
	return function(inputItem){		
		if (!is_valid_percentage(inputItem))
			return {success: false, error_message: "Not a valid percentage", validate_class: false};
			
		var totalValue = 0;
		var foundError = false;
		$('.' + itemsClass).each(function(i, obj) {
			if (is_valid_percentage(obj.value))
				totalValue += parseFloat(obj.value);
			else
				foundError = true;
		});
		if (foundError || (floatDiffers(totalValue, 0) && floatDiffers(totalValue, 100)))
			return {success: false, error_message: "Sum of percentages should be either 0 or 100", validate_class: itemsClass};
			
		return {success: true};
	};
}

function amp_validator_check_year_range(itemsClass){
	return function(inputItem){	
		
		var yearRangeStartItem = $('.validate-year-range-start.' + itemsClass);
		if (!isYearValidator(yearRangeStartItem.val()))
			return {success: false, error_message: please_enter_year_message};

		var yearRangeEndItem = $('.validate-year-range-end.' + itemsClass);
		if (!isYearValidator(yearRangeEndItem.val()))
			return {success: false, error_message: please_enter_year_message};
		
		var yearStart = yearRangeStartItem.val();
		var yearEnd = yearRangeEndItem.val();
		if (yearEnd < yearStart)
			return {success: false, error_message: "Start Year should be before End Year", validate_class: itemsClass};
			
		return {success: true};			
	};
}

function amp_validator_check_date_range(itemsClass){
	return function(inputItem){		
		  
		var dateRangeStartItem = $('.validate-date-range-start.' + itemsClass);
		var dateRangeEndItem = $('.validate-date-range-end.' + itemsClass);
		var dateFormat = dateRangeStartItem.data('date-format') ? dateRangeStartItem.data('date-format') : 'YYYY-MM-DD';
		var dateStart = moment(dateRangeStartItem.val(), dateFormat);
		var dateEnd = moment(dateRangeEndItem.val(), dateFormat);

		if (!dateStart.isValid())
			return {success: false, error_message: please_enter_date_message};

		if (!dateEnd.isValid())
			return {success: false, error_message: please_enter_date_message};
		
		
		if (dateEnd < dateStart)
			return {success: false, error_message: "Start Date should be before End Date", validate_class: itemsClass};
			
		return {success: true};			
	};
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

function get_validator_for_element(elem)
{
	if ($(elem).is('.validate-phone-number'))
		return new AmpValidator(looksLikePhoneNumber, please_enter_phone_number_message);
	
	if ($(elem).is('.validate-email-address'))
		return new AmpValidator(function(elem){return (elem.length == 0) || looksLikeEmail(elem);}, please_enter_email_message);
	
	if ($(elem).is('select.validate-mandatory'))
		return new AmpValidator(selectHasValue, please_enter_something_message);

	if ($(elem).is('.validate-mandatory-number'))
		return new AmpValidator(function(value){return (value.length > 0) && looksLikeAmount(value);}, please_enter_number_message).setAllowEmpty(false);

	if ($(elem).is('input.validate-mandatory') || $(elem).is('textarea.validate-mandatory'))
	{
		var classList = $(elem).attr('class').split(/\s+/); // generate a list of the classes the element has
		var validate_min_length = 1;
		for(var i = 0; i < classList.length; i++){
			var className = classList[i];
			if (className.indexOf("validate-min-length-") == 0){
				validate_min_length = parseInt(className.substring("validate-min-length-".length));
			};
		}
		return new AmpValidator(amp_validator_hasMinimumLength(validate_min_length), please_enter_something_message);
	}

	if ($(elem).is('.validate-year'))
		return new AmpValidator(isYearValidator, please_enter_year_message);
	
	if ($(elem).is('.validate-year-range-start, .validate-year-range-end')){
		var classList = $(elem).attr('class').split(/\s+/); // generate a list of the classes the element has
		for(var i = 0; i < classList.length; i++){
			var className = classList[i];
			if (className.indexOf("validate-year-range-group-") == 0){
				return new AmpValidator(amp_validator_check_year_range(className)).setAllowEmpty(false).setErrMsg(please_enter_year_message); //amp_bootstrap_forms_check_percentage(elem, className);	
			}
		};				
		return new AmpValidator(isYearRangeStartValidator(elem)).setAllowEmpty(false);
	}

	if ($(elem).is('.validate-date-range-start, .validate-date-range-end')){
		var classList = $(elem).attr('class').split(/\s+/); // generate a list of the classes the element has
		for(var i = 0; i < classList.length; i++){
			var className = classList[i];
			if (className.indexOf("validate-date-range-group-") == 0){
				return new AmpValidator(amp_validator_check_date_range(className)).setAllowEmpty(false).setErrMsg(please_enter_date_message); //amp_bootstrap_forms_check_percentage(elem, className);	
			}
		};				
		return new AmpValidator(isYearRangeStartValidator(elem)).setAllowEmpty(false);
	}
	
	if ($(elem).is('input.validate-percentage'))
	{
		var classList = $(elem).attr('class').split(/\s+/); // generate a list of the classes the element has
		for(var i = 0; i < classList.length; i++){
			var className = classList[i];
			if (className.indexOf("validate-percentage-") == 0){
				return new AmpValidator(amp_validator_check_percentage(className)).setAllowEmpty(false); //amp_bootstrap_forms_check_percentage(elem, className);	
			}
		};
	}
	return false;
}

/**
 * initializes validation engine for elements within a div
 * @param divId
 */
function init_validation(divId)
{
	if (divId.charAt(0) != '#')
		divId = '#' + divId;
	$(document).ready(function(){
		var inputItemsSelector =  divId + " input, " + divId + " select, " + divId + " textarea";
		$(inputItemsSelector).blur(function()
		{
			var validator = get_validator_for_element(this);
			if (validator)
				return validator.validateAndHighlight(this);
		});
	});
}
