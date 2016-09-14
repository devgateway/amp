var CommonFilterUtils = {};

CommonFilterUtils.getDateIntervalType = function(element) {
	console.log("CommonFilterUtils.getDateIntervalType");
	// element can be a simple object or a Backbone Model.
	var isModel = (element.get !== undefined);
	var min = isModel ? element.get('min') : element.min;
	var max = isModel ? element.get('max') : element.max;
	if (min === undefined)
		return "max";
	if (max === undefined)
		return "min";
	var valueToNameMin = isModel ? element.get('valueToName').attributes[min] : element.valueToName[min];	
	var valueToNameMax = isModel ? element.get('valueToName').attributes[max] : element.valueToName[max];
	if (valueToNameMin === undefined) {
		return "max";
	}
	if (valueToNameMax === undefined) {
		return "min";
	}		
	return "both";
};

CommonFilterUtils.convertJavaFiltersToJS = function(data) {
	console.log('CommonFilterUtils.convertJavaFiltersToJS');
	
	if (data === undefined) {
		return
	}
	// Since this function now is common for Tabs and Saiku we need to transform
	// backbone objects from tabs to plain js objects.
	if (data.models !== undefined) {
		data = _.pluck(data.models, 'attributes');
	}	
	
	// Define some basic defaults needed in the widget filter.
	var blob = {
		otherFilters : {
			date : {
				end : '',
				start : ''
			}
		},
		columnFilters : {			
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
			case 'Expenditure Class':
			case 'Donor Agency':
				blob.columnFilters[item.name] = _.map(item.values, function(item_) {
					return parseInt(item_.id);
				});
				break;
			
			// cases where the filter only accepts boolean values.
			case 'Disaster Response Marker':
			case 'Humanitarian Aid':
				blob.columnFilters[item.name] = _.map(item.values, function(item_) {
					return parseInt(item_);
				});
				break;
	
			// cases where columnFilter matches item name + ' Id'
			case 'Contracting Agency':
			case 'Executing Agency':
			case 'Implementing Agency':
			case 'Beneficiary Agency':
				blob.columnFilters[item.name] = _.map(item.values, function(item_) {
					return parseInt(item_.id);
				});
				break;
			
			case 'National Planning Objectives':
			case 'Primary Program':
			case 'Secondary Program':
			case 'Tertiary Program':
				blob.columnFilters[item.name + ' Level 1'] = _.map(item.values, function(
						item_) {
					return parseInt(item_.id);
				});
				for (var i = 2; i < 9; i++) {
					blob.columnFilters[item.name + ' Level ' + i] = blob.columnFilters[item.name + ' Level 1'];
				}
				break;
				
			case 'Funding Organization':
				blob.columnFilters['Donor Id'] = _.map(item.values, function(item_) {
					return parseInt(item_.id);
				});

			case 'Primary Sector':
			case 'Secondary Sector':
			case 'Tertiary Sector':	
				blob.columnFilters[item.name] = _.map(item.values, function(item_) {
					return parseInt(item_.id);
				});
				blob.columnFilters[item.name + ' Id'] = blob.columnFilters[item.name];
				blob.columnFilters[item.name + ' Sub-Sector'] = blob.columnFilters[item.name];
				blob.columnFilters[item.name + ' Sub-Sub-Sector'] = blob.columnFilters[item.name];
				break;
				
			case 'DATE':
				// FilterUtils.fillDateBlob(blob.otherFilters.date,
				// item.attributes);
				var newDate = {};
				_.map(item.values, function(item_, i) {						
					if (item_.dateIntervalType === "min") {
						newDate['start'] = item_.name;
					} else if (item_.dateIntervalType === "max") {
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
			case 'Effective Funding Date':
			case 'Closing Funding Date':
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
			case 'computedYear':
				var year = _.isEmpty(item.values) ? null : item.values[0].id; 
				blob.otherFilters['computedYear'] = {year: year, displayName: year};
				break;
			default:
				console.info(item);
				break;
		}
	});
	return blob;
};

/**
 * We will use this function to change the structure of the filters sent to the
 * backend until we make a refactor of the Filter Widget to match the structure
 * the EPs expects.
 */
CommonFilterUtils.transformParametersForBackend = function(filters) {
	if (filters) {
		if (filters.otherFilters && filters.otherFilters['computedYear']) {
			// Computed Year is a new filter option and it has a structure
			// different to the rest of the filters.
			filters.computedYear = filters.otherFilters['computedYear'].year
		}
	}
};