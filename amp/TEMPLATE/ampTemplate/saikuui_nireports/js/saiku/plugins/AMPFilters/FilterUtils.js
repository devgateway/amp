var FilterUtils = {};

FilterUtils.parseValue = function(elem, v) {
	Saiku.logger.log("FilterUtils.parseValue");
	var theName = elem.valueToName ? elem.valueToName[v] : v;
	return {id: v, name: theName};
};


FilterUtils.processFilters =  function(filters){
	var processedFilters = {}
	for ( var propertyName in filters) {
		var filter = filters[propertyName];
		if(Array.isArray(filter)){
			var values = [];
			_.each(filter, function(item) {
				var value = isNaN(item) ? item : parseInt(item);
				values.push(value);
			});
			processedFilters[propertyName] = values; 
		} else {					
			processedFilters[propertyName] = isNaN(filter) ? filter : parseInt(filter);					
		}
	}
	return processedFilters;
	
}