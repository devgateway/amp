var CommonFilterUtils = {};

/**
 * This function will calculate the MD5 string of the parameters that define a report's result: id, filters, etc.
 * Also will make some cleanup and reordering to get the same MD5 for the "same" parameters.
 */
CommonFilterUtils.calculateMD5FromParameters = function (model, id, lang, timestamp) {	
	var parameters = {filters: {}, settings: {}, id: null, lang : null, timestamp : null};
	
	parameters.id = id;
	parameters.lang = lang;
	parameters.timestamp = timestamp;
	
	if (model.queryModel.filters) {
		// Everything non-date filters.
		if (model.queryModel.filters) {
			var filters = {};
			for (var property in model.queryModel.filters) {
				// To avoid problems with prototype´s properties.
				if (model.queryModel.filters.hasOwnProperty(property)) {
					// Sort ID's.					
					if(_.isArray(model.queryModel.filters[property])){
						filters[property] = _.sortBy(model.queryModel.filters[property], function(item) {return item;});
					}else{
						filters[property] = model.queryModel.filters[property];
					}					
				}
			}
			// Now sort the properties of the object so stringify will return the same string.
			var auxPropertiesArray = [];
			for (var property in filters) {
				auxPropertiesArray.push(property);
			}
			var sortedFilters = {};
			auxPropertiesArray = auxPropertiesArray.sort();
			_.each(auxPropertiesArray, function(item) {
				sortedFilters[item] = filters[item];
			})
			parameters.filters = sortedFilters;
		}
		
		
	}    	
	if (model.queryModel.settings) {
		var auxSettings = model.queryModel.settings;
		// Now sort the properties of the object so stringify will return the same string.
		var auxPropertiesArray = [];
		for (var property in auxSettings) {
			auxPropertiesArray.push(property);
		}
		var sortingSettings = {};
		auxPropertiesArray = auxPropertiesArray.sort();
		_.each(auxPropertiesArray, function(item) {
			sortingSettings[item] = auxSettings[item];
		})
		parameters.settings = sortingSettings;    		
	}
	
	if (model.queryModel.sorting) {
		// No need to rearrange sorting parameters.
		parameters.sorting = model.queryModel.sorting; 
	}
	
	var md5 = objectHash.MD5(JSON.stringify(parameters));	
	return md5;
};