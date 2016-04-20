var FilterUtils = {};

FilterUtils.parseValue = function(elem, v) {
	Saiku.logger.log("FilterUtils.parseValue");
	var theName = elem.valueToName ? elem.valueToName[v] : v;
	return {id: v, name: theName};
};

FilterUtils.extractFilters = function(content) {
	Saiku.logger.log("FilterUtils.extractFilters");
	var self = this;
	var filters = new Array();
	var filtersColumnsJson = content.columnFilterRules;
	var keys = [];
	for(var k in filtersColumnsJson) keys.push(k);
	$(keys).each(function(i, item) {
		var subElement = filtersColumnsJson[item];
		if (subElement instanceof Array) {
			var element = subElement[0];
			var content = [];
			if (item === 'DATE') {
				var dateIntervalType = CommonFilterUtils.getDateIntervalType(element, item, i);
			}
			if (element.values !== null) {
				_.each(element.values, function(v) {
					content.push(self.parseValue(element, v));
				})
			}
			if (element.value !== null) {
				content.push(self.parseValue(element, element.value));
			} else if (element.valueToName !== null) {
				// This should be .models but the way the endpoint returns
				// the data breaks backbone.
				_.each(element.valueToName, function(item_, i) {
					// Need to do this because of how js parses these data
					// and adds an extra element.
					if (i !== undefined && item_ !== undefined) {
						var item = {};
						item.id = i;
						item.name = item_;
						if (dateIntervalType !== undefined) {
							item.dateIntervalType = dateIntervalType;
							item.type = element["min"] === i ? "min" : "max";
						}
						if(_.isUndefined(_.findWhere(content,{id:item.id, name:item.name}))){
						   content.push(item);
						}
						
					}
				});
			}
			//translate filter values
			_.each(content,function(item, i) {
				//for now only true or false were asked to be translated. 
				//Avoid doing a ajax call for all values if we only need 2.
				if (item.name === "true" || item.name === "false") {
					item.trnName = item.name/*TranslationManager.getTranslated(item.name)*/;
				 }
				else {
					item.trnName = item.name;
				}
			});
			var auxFilter = {
				trnName : item/*TranslationManager.getTranslated(item)*/,
				name: item,
				values : content
			};
			filters.push(auxFilter);
		}
	});

	var filtersDateColumnsJson = content.columnDateFilterRules;
	var keys = [];
	for(var k in filtersDateColumnsJson) keys.push(k);
	$(keys).each(function(i, item) {
		var subElement = filtersDateColumnsJson[item];
		if (subElement instanceof Array) {
			var element = subElement.models ? subElement.models[0] : subElement[0];
			var content = [];	
			if (element.value !== null) {
				var auxItem = {};
				auxItem.id = element.value;
				auxItem.name = element.valueToName[element.value];
				content.push(auxItem);
			} else if (element.valueToName !== null) {
				// This should be .models but the way the endpoint returns
				// the data breaks backbone.
				_.each(element.valueToName, function(j, item2) {
					// Need to do this because of how js parses these data
					// and adds an extra element.
					if (j !== undefined) {
						var item_ = {};
						item_.id = i;
						item_.name = j;
						content.push(item_);
					}
				});
			}
			var auxFilter = {
				trnName : item/*TranslationManager.getTranslated(item)*/,
				name: item,
				values : content
			};
			filters.push(auxFilter);
		}
	});
	return filters;
};