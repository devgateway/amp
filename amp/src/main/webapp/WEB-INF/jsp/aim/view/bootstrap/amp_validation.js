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
//		result = _self.validatorFunc(elem.value.trim());
		result = _self.validatorFunc(elem);
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
AmpValidator.prototype.validateAndHighlight = function(elem, prevError){
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
	if (!prevError)
		setValidationStatus($(elem), 'has-success'); // ok
	return true;
};

function amp_validator_hasMinimumLength(vml){
	return function(inp){
		return inp.value.length >= vml;
	};
}

function amp_validator_check_percentage(itemsClass){
	return function(inputItem){		
		if (!is_valid_percentage(inputItem.value))
			return {success: false, error_message: not_valid_percentage_message, validate_class: false};
			
		var totalValue = 0;
		var foundError = false;
		$('.' + itemsClass).each(function(i, obj) {
			if (is_valid_percentage(obj.value))
				totalValue += parseFloat(obj.value);
			else
				foundError = true;
		});
		if (foundError || (floatDiffers(totalValue, 0) && floatDiffers(totalValue, 100)))
			return {success: false, error_message: sum_percentages_message, validate_class: itemsClass};
			
		return {success: true};
	};
}

function amp_validator_check_year(itemsClass){
	return function(inputItem){	
		if (!isYearValidator(inputItem.value))
			return {success: false, error_message: please_enter_year_message};
		return {success: true};			
	};
}

function amp_validator_check_year_range(itemsClass){
	return function(inputItem){	
		var yearRangeStartItem = $('.validate-year-range-start.' + itemsClass);
		if (!isYearValidator(yearRangeStartItem.val()))
			return {success: true};
		var yearRangeEndItem = $('.validate-year-range-end.' + itemsClass);
		if (!isYearValidator(yearRangeEndItem.val()))
			return {success: true};
		//if either startDate or endDate are invalid, it's up to the other validators to catch them
		var yearStart = yearRangeStartItem.val();
		var yearEnd = yearRangeEndItem.val();
		if (yearEnd < yearStart)
			return {success: false, error_message: start_year_end_year_message, validate_class: itemsClass};
		return {success: true};			
	};
}

function amp_validator_check_date(itemsClass){
	return function(inputItem){		
		var defaultDateFormat = 'YYYY-MM-DD';
		var dateFormat = defaultDateFormat;
		if (inputItem.dataset)
			dateFormat = inputItem.dataset.dateFormat ? inputItem.dataset.dateFormat : defaultDateFormat;
		var date = moment(inputItem.value, dateFormat);
		if (!date.isValid())
			return {success: false, error_message: please_enter_date_message};
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
		//if either startDate or endDate are invalid, it's up to the other validators to catch them
		if (!dateStart.isValid())
			return {success: true};
		if (!dateEnd.isValid())
			return {success: true};
		if (dateEnd < dateStart)
			return {success: false, error_message: start_date_end_date_message, validate_class: itemsClass};
		return {success: true};			
	};
}

function selectHasValue(selectVal){
	if (typeof(selectVal.value) == 'undefined')
		return false;
	if (selectVal.value == null)
		return false;
	if (selectVal.value == '')
		return false;
	if (isNaN(selectVal.value))
		return false;
	return parseInt(selectVal.value) > 0;
}

function get_validators_for_element(elem)
{
	var validators = [];
	
	if ($(elem).is('.validate-phone-number'))
		validators.push(new AmpValidator(looksLikePhoneNumber, please_enter_phone_number_message));
	
	if ($(elem).is('.validate-email-address'))
		validators.push(new AmpValidator(function(elem){return (elem.value.length == 0) || looksLikeEmail(elem.value);}, please_enter_email_message));
	
	if ($(elem).is('select.validate-mandatory'))
		validators.push(new AmpValidator(selectHasValue, please_enter_something_message));

	if ($(elem).is('.validate-mandatory-number'))
		validators.push(new AmpValidator(function(value){return (value.value.length > 0) && looksLikeAmount(value.value);}, please_enter_number_message).setAllowEmpty(false));

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
		validators.push(new AmpValidator(amp_validator_hasMinimumLength(validate_min_length), please_enter_something_message));
	}

	if ($(elem).is('.validate-year'))
		return new AmpValidator(isYearValidator, please_enter_year_message);
	
	if ($(elem).is('.validate-year-range-start, .validate-year-range-end')){
		var classList = $(elem).attr('class').split(/\s+/); // generate a list of the classes the element has
		for(var i = 0; i < classList.length; i++){
			var className = classList[i];
			if (className.indexOf("validate-year-range-group-") == 0){
				validators.push(new AmpValidator(amp_validator_check_year_range(className)).setAllowEmpty(false).setErrMsg(please_enter_year_message)); //amp_bootstrap_forms_check_percentage(elem, className);
				validators.push(new AmpValidator(amp_validator_check_year(className)));
			}
		};				
		//this has no reference anywhere -- has it ever worked?
//		validators.push(new AmpValidator(isYearRangeStartValidator(elem)).setAllowEmpty(false));
	}

	if ($(elem).is('.validate-date-range-start, .validate-date-range-end')){
		var classList = $(elem).attr('class').split(/\s+/); // generate a list of the classes the element has
		for(var i = 0; i < classList.length; i++){
			var className = classList[i];
			if (className.indexOf("validate-date-range-group-") == 0){
				validators.push(new AmpValidator(amp_validator_check_date_range(className)).setAllowEmpty(false).setErrMsg(please_enter_date_message)); //amp_bootstrap_forms_check_percentage(elem, className);
				validators.push(new AmpValidator(amp_validator_check_date(className)));
			}
		};				
		//this has no reference anywhere -- has it ever worked?
//		validators.push(new AmpValidator(isYearRangeStartValidator(elem)).setAllowEmpty(false));
	}
	
	if ($(elem).is('input.validate-percentage'))
	{
		var classList = $(elem).attr('class').split(/\s+/); // generate a list of the classes the element has
		for(var i = 0; i < classList.length; i++){
			var className = classList[i];
			if (className.indexOf("validate-percentage-") == 0){
				validators.push(new AmpValidator(amp_validator_check_percentage(className)).setAllowEmpty(false)); //amp_bootstrap_forms_check_percentage(elem, className);	
			}
		};
	}
	if (validators.length > 0)
		return validators;
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
			var validators = get_validators_for_element(this);
			if (validators){
				var prevError = false;
				for (i = 0; i < validators.length; i++){
					prevError |= validators[i].validateAndHighlight(this, prevError);
				}
			}
		});
	});
}
