var _ = require('underscore');
var Backbone = require('backbone');
var NameUtil = require('./name-util');
var Constants = require('./constants');
var LocalStorageService = require('../utils/local-storage-service.js');
var validator = {};

validator.nameindicators = function(options){	
	var deferred = $.Deferred();
	var errorMessages = [];
	var defaultLanguage = options.settings.get('default-language');
	var name = _.isUndefined(options.model.get('name'))  ? {} : options.model.get('name');

	if(_.isUndefined(name[defaultLanguage]) || _.isEmpty(name[defaultLanguage])){
		var translatedDefaultLang = options.translator.translateSync('amp.gis-layers-manager:message-defaultLanguageLabel', 'The Default Language')
		errorMessages.push(((options.languages.length > 1) ? (translatedDefaultLang + " '" + defaultLanguage + "' " ) : '') + options.translator.translateSync('amp.gis-layers-manager:validation-NameRequired','Indicator Name is required'));
		deferred.resolve(errorMessages);
	}

	if(options.model.get('accessTypeId') === Constants.AccessType.TEMPORARY){
		var layers = LocalStorageService.getAll();
		var langs = _.keys(name);
		_.each(langs, function (lang) {
			if (!_.isUndefined(name[lang]) && !_.isNull(name[lang])) {
				_.each(layers, function (layer) {
					if (name[lang] == layer.name[lang]) {
						errorMessages.push(lang + ' : ' + options.translator.translateSync('amp.gis-layers-manager:validation-LayerWithNameExists', 'There is another layer with the specified layer name. Layer name must be unique.'));
					}
				});
			}
		});
		deferred.resolve(errorMessages);
	} else {
		var oldName = !_.isUndefined(options.clone) ? options.clone.name : {};
		NameUtil.isNameAvailable(options.model.get('name'), oldName).then(function (results) {
			var keys = _.keys(results);
			_.each(keys, function (key) {
				if (results[key].result === true) {
					errorMessages.push(key + ' : ' + options.translator.translateSync('amp.gis-layers-manager:validation-LayerWithNameExists', 'There is another layer with the specified layer name. Layer name must be unique.'));
				}
			});
			deferred.resolve(errorMessages);
		});
	}
    return deferred;	
};

validator.definemeasurelabels = function(options){
	var deferred = $.Deferred();
	var errorMessages = [];	
	var defaultLanguage = options.settings.get('default-language');
	var unit = _.isUndefined(options.model.get('unit'))  ? {} : options.model.get('unit');
	if(_.isUndefined(unit[defaultLanguage]) || _.isEmpty(unit[defaultLanguage])){
	     var translatedDefaultLang = options.translator.translateSync('amp.gis-layers-manager:message-defaultLanguageLabel','The Default Language')
		 errorMessages.push(((options.languages.length > 1) ? (translatedDefaultLang + " '" + defaultLanguage + "' " ) : '') + options.translator.translateSync('amp.gis-layers-manager:validation-MeasureLabelRequired','Measure Label is required'));
		 deferred.resolve(errorMessages); 		
	}
	if(_.isUndefined(options.model.get('indicatorTypeId')) || !validator.isInt(options.model.get('indicatorTypeId'))){		
		errorMessages.push(options.translator.translateSync('amp.gis-layers-manager:validation-IndicatorLayerTypeRequired','Indicator Layer Type is required'));		
	}
	deferred.resolve(errorMessages);
	return deferred;
};

validator.defineadmlevels = function(options){
	var deferred = $.Deferred();
	var errorMessages = [];
	if(_.isUndefined(options.model.get('admLevelId')) || !validator.isInt(options.model.get('admLevelId'))){		
		errorMessages.push(options.translator.translateSync('amp.gis-layers-manager:validation-ADMLevelRequired','ADM Level is required'));		
	}
	
	deferred.resolve(errorMessages);
	return deferred;
};

validator.exportfile = function(options){
	var deferred = $.Deferred();
	var errorMessages = [];
	deferred.resolve(errorMessages);
	return deferred;
};

validator.reimportfile = function(options){
	var deferred = $.Deferred();
	var errorMessages = [];
	if((_.isUndefined(options.model.get('numberOfImportedRecords')) || (options.model.get('numberOfImportedRecords') === 0)) &&  ((_.isUndefined(options.model.get('values')) || options.model.get('values').length === 0))){
		errorMessages.push(options.translator.translateSync('amp.gis-layers-manager:validation-IndicatorDataRequired','Indicator data required. Please import indicator data before proceeding to the next step'));
	}	
	deferred.resolve(errorMessages);
	return deferred;
};

validator.addnotes = function(options){
	var deferred = $.Deferred();
	var errorMessages = [];
	deferred.resolve(errorMessages);
	return deferred;
};

validator.reviewandsave = function(options){
	var deferred = $.Deferred();
	var errorMessages = [];
	deferred.resolve(errorMessages);
	return deferred;
};

validator.definecolorscheme = function(options){
	var deferred = $.Deferred();
	var errorMessages = [];
	if(_.isUndefined(options.model.get('numberOfClasses')) || options.model.get('numberOfClasses') == ''){
		errorMessages.push(options.translator.translateSync('amp.gis-layers-manager:validation-NumberofClassesRequired','Number of Classes is required'));
	}
	if(!validator.isInt(options.model.get('numberOfClasses'))){
		errorMessages.push(options.translator.translateSync('amp.gis-layers-manager:validation-NumberofClassesInvalid','Number of Classes is invalid'));	
	}
    if(parseInt(Number(options.model.get('numberOfClasses'))) <= 0){
        errorMessages.push(options.translator.translateSync('amp.gis-layers-manager:validation-NumberofClassesisZero','Number of Classes should be more than 0'));
    }
	if(_.isUndefined(options.model.get('colorRampId'))){
		errorMessages.push(options.translator.translateSync('amp.gis-layers-manager:validation-ColorRequired','Color is required'));		
	}

	if (!_.isUndefined(options.model.get('colorRampId')) && !_.isUndefined(options.model.get('numberOfClasses'))) {
	    var numberOfColors = Constants.NUMBER_OF_COLORS[options.model.get('colorRampId')];
	    if (options.model.get('numberOfClasses') > numberOfColors) {
	    	errorMessages.push(options.translator.translateSync('amp.gis-layers-manager:validation-ClassesMoreThanColors','The number of classes exceeds the number of colors in the selected palette. For multi-color palettes the number of classes should be less or equal to the number of colors.'));
	    }				
	}

    var classes = options.model.get('numberOfClasses');
    var numberOfValues = 0;
    if(options.model.has('values')) {
		var tmp = {};
		var values = _.each(options.model.get('values'), function (item) {
			tmp[item.value] = item.value;
		});
        numberOfValues = Object.keys(tmp).length;
	} else {
        numberOfValues = options.model.get('numberOfImportedRecords');
	}
    if (classes > numberOfValues) {
        errorMessages.push(options.translator.translateSync('amp.gis-layers-manager:validation-MoreClassesThanValues', 'There are more classes than values in re-imported excel file'));
    }

	deferred.resolve(errorMessages);
	return deferred;
};

validator.isInt = function(input) {
	  return !isNaN(input) && 
	         parseInt(Number(input)) == input && 
	         !isNaN(parseInt(input, 10));
}
module.exports = validator;