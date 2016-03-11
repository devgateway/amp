var FilterUtils = {};
/**
 * TODO: Make these functions a reusable lib so it can be used also on "tabs/js/business/filter/filterUtils.js" (almost same code).
 * 		Now changes done here have to be replicated there and vice-versa.
 */

FilterUtils.getDateIntervalType = function(element) {
	Saiku.logger.log("FilterUtils.getDateIntervalType");
	var min = element.min;
	var max = element.max;
	if (min === undefined)
		return "max";
	if (max === undefined)
		return "min";
	if (element.valueToName[min] === undefined) {
		return "max";
	}
	if (element.valueToName[max] === undefined) {
		return "min";
	}		
	return "both";
};

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
				var dateIntervalType = FilterUtils.getDateIntervalType(element, item, i);
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
						content.push(item);
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

FilterUtils.convertJavaFiltersToJS = function(data) {
	Saiku.logger.log("FilterUtils.convertJavaFiltersToJS");
	// Define some basic defaults needed in the widget filter.
	var blob = {
		otherFilters : {
			/*date : {
				end : '',
				start : ''
			}*/
		},
		columnFilters : {
			/*"Donor Id" : []*/
		}
	};
	
	_.each(data, function(item, i) {
		switch (item.name) {

		// cases where columnFilter matches item name
		case 'Responsible Organization':
		case 'Type Of Assistance':
		case 'Financing Instrument':
		case 'Funding Status':	
		case 'Status':
		case 'Approval Status':
		case 'Donor Group':
		case 'Donor Type':
		case 'Mode of Payment':
		case 'On/Off/Treasury Budget':
		case 'Zone':
		case 'Region':
		case 'District':
		case 'Humanitarian Aid':
		case 'Disaster Response Marker':
			blob.columnFilters[item.name] = _.map(item.values, function(item_) {
				return parseInt(item_.id);
			});
			break;

		// cases where columnFilter matches item name + ' Id'
		case 'Contracting Agency':
		case 'Executing Agency':
		case 'Implementing Agency':
		case 'Beneficiary Agency':
			blob.columnFilters[item.name + ' Id'] = _.map(item.values, function(item_) {
				return parseInt(item_.id);
			});
			break;
		
		case 'National Planning Objectives':
		case 'Primary Program':
		case 'Secondary Program':
		case 'Tertiary Program':
			blob.columnFilters[item.name + ' Level 1 Id'] = _.map(item.values, function(
					item_) {
				return parseInt(item_.id);
			});
			blob.columnFilters[item.name + ' Level 2 Id'] = blob.columnFilters[item.name + ' Level 1 Id'];
			break;
			
		case 'Donor Agency':
			blob.columnFilters['Donor Id'] = _.map(item.values, function(item_) {
				return parseInt(item_.id);
			});
			break;
			
		case 'Funding Organization':
			blob.columnFilters['Donor Id'] = _.map(item.values, function(item_) {
				return parseInt(item_.id);
			});
		/*case 'Contracting Agency Groups':
			blob.columnFilters['Contracting Agency Id'] = _.map(item.values, function(item_) {
				return parseInt(item_.id);
			});
			break;*/
		case 'Primary Sector':
		case 'Secondary Sector':
		case 'Tertiary Sector':	
			blob.columnFilters[item.name + ' Id'] = _.map(item.values, function(item_) {
				return parseInt(item_.id);
			});
			blob.columnFilters[item.name + ' Sub-Sector Id'] = blob.columnFilters[item.name + ' Id'];
			blob.columnFilters[item.name + ' Sub-Sub-Sector Id'] = blob.columnFilters[item.name + ' Id'];
			break;
			
//		case 'Start Date':
//			blob.otherFilters.date.start = '2019-12-31';
//			break;
//			
//		case 'End Date':
//			blob.otherFilters.date.end = '2029-12-31';
//			break;

		case 'DATE':
			//FilterUtils.fillDateBlob(blob.otherFilters.date, item.attributes);
			var newDate = {};
			_.map(item.values, function(item_, i) {						
				if (item_.type === "min") {
					newDate['start'] = item_.name;
				} else if (item_.type === "max") {
					newDate['end'] = item_.name;
				}
				return newDate;
			});
			blob.otherFilters['date'] = newDate;
			break;

		case 'Team':
			blob.columnFilters['Workspaces'] = _.map(item.values, function(item_) {
				return parseInt(item_.id);
			});
			break;
			
		case 'Actual Approval Date':
		case 'Actual Completion Date':
		case 'Actual Start Date':
		case 'Current Completion Date':
		case 'Donor Commitment Date':
		case 'Final Date for Contracting':
		case 'Final Date for Disbursements':
		case 'Funding Classification Date':
		case 'Funding end date':
		case 'Funding start date':
		case 'Original Completion Date':
		case 'Proposed Approval Date':
		case 'Proposed Completion Date':
		case 'Proposed Start Date':
			
			var newDate = {};
			_.map(item.values, function(item_, i) {						
				if(i === 0) {
					newDate['start'] = item_.name;
				} else if(i === 1) {
					newDate['end'] = item_.name;
				}
				return newDate;
			});
			blob.otherFilters[item.name] = newDate;
			break;
		default:
			console.info(item);
			break;
		}
	});
	return blob;
};