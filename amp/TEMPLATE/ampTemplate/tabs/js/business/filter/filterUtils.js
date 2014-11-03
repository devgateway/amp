define([ 'models/filter', 'collections/filters', 'jquery' ], function(Filter, Filters, jQuery) {

	"use strict";

	function FilterUtils() {
		if (!(this instanceof FilterUtils)) {
			throw new TypeError("FilterUtils constructor cannot be called as a function.");
		}
	}

	FilterUtils.extractFilters = function(content) {
		var filters = new Filters();
		var filtersColumnsJson = content.get('columnFilterRules');
		jQuery(filtersColumnsJson.keys()).each(function(i, item) {
			var subElement = filtersColumnsJson.get(item);
			if (subElement instanceof Backbone.Collection) {
				var element = subElement.models[0];
				var content = [];
				if (element.get('value') != null) {
					var item = {};
					item.id = element.get('value');
					item.name = element.get('value');
					content.push(item);
				} else if (element.get('valueToName') != null) {
					// This should be .models but the way the endpoint returns
					// the data breaks backbone.
					_.each(element.get('valueToName').attributes, function(item_, i) {
						// Need to do this because of how js parses these data
						// and adds an extra element.
						if (i != undefined && item_ != undefined) {
							var item = {};
							item.id = i;
							item.name = item_;
							content.push(item);
						}
					});
				}
				var auxFilter = new Filter({
					name : item,
					values : content
				});
				filters.add(auxFilter);
			}
		});
		// TODO: implement the same for content.get('columnDateFilterRules')
		return filters;
	};

	// TODO: after we are sure tab's default filters are EXACTLY the same than
	// widget filters we can simplify these 2 methods into just one.
	FilterUtils.updateFiltersRegion = function(filtersFromWidget) {
		// First we cleanup current values.
		app.TabsApp.filters.models = [];
		app.TabsApp.dynamicContentRegion.currentView.filters.currentView.render();

		if (filtersFromWidget.columnFilters != undefined) {
			for ( var propertyName in filtersFromWidget.columnFilters) {
				var auxProperty = filtersFromWidget.columnFilters[propertyName];
				var content = [];
				_.each(auxProperty, function(item, i) {
					var auxItem = {};
					auxItem.id = item.get('id');
					auxItem.name = item.get('name');
					content.push(auxItem);
				});
				var filter = new Filter({
					name : propertyName,
					values : content
				});
				app.TabsApp.filters.models.push(filter);
			}
		}
		app.TabsApp.dynamicContentRegion.currentView.filters.currentView.render();
	};

	FilterUtils.convertJavaFiltersToJS = function(data) {
		// Define some basic defaults needed in the widget filter.
		var blob = {
			otherFilters : {
				date : {
					end : '2015-12-31',
					start : '1980-01-01'
				}
			},
			columnFilters : {
				"Donor Id" : []
			}
		};
		_.each(data.models, function(item, i) {
			/*
			 * var filterName = item.get('name'); blob.columnFilters[filterName] =
			 * _.map(item.get('values'), function(item.id) { return
			 * parseInt(item.id); });
			 */
			switch (item.get('name')) {
			case 'Financing Instrument':
				blob.columnFilters["Financing Instrument"] = _.map(item.get('values'), function(item) {
					return parseInt(item.id);
				});
				break;
			case 'Contracting Agency':
				blob.columnFilters['Contracting Agency Id'] = _.map(item.get('values'), function(item) {
					return parseInt(item.id);
				});
				break;
			case 'Executing Agency':
				blob.columnFilters['Executing Agency Id'] = _.map(item.get('values'), function(item) {
					return parseInt(item.id);
				});
				break;
			case 'Implementing Agency':
				blob.columnFilters['Implementing Agency Id'] = _.map(item.get('values'), function(item) {
					return parseInt(item.id);
				});
				break;
			case 'Beneficiary Agency':
				blob.columnFilters['Beneficiary Agency Id'] = _.map(item.get('values'), function(item) {
					return parseInt(item.id);
				});
				break;
			case 'Responsible Organization':
				blob.columnFilters['Responsible Organization'] = _.map(item.get('values'), function(item) {
					return parseInt(item.id);
				});
				break;
			case 'Type Of Assistance':
				blob.columnFilters['Type Of Assistance'] = _.map(item.get('values'), function(item) {
					return parseInt(item.id);
				});
				break;
			case 'Status':
				blob.columnFilters['ActivityStatusList'] = _.map(item.get('values'), function(item) {
					return parseInt(item.id);
				});
				break;
			case 'National Planning Objectives':
				blob.columnFilters['National Planning Objectives Level 1 Id'] = _.map(item.get('values'), function(item) {
					return parseInt(item.id);
				});
				break;
			case 'Primary Program':
				blob.columnFilters['Primary'] = _.map(item.get('values'), function(item) {
					return parseInt(item.id);
				});
				break;
			case 'Approval Status':
				blob.columnFilters['Approval Status'] = _.map(item.get('values'), function(item) {
					return parseInt(item.id);
				});
				break;
			case 'Secondary Program':
				blob.columnFilters['Secondary'] = _.map(item.get('values'), function(item) {
					return parseInt(item.id);
				});
				break;
			case 'On/Off/Treasury Budget':
				blob.columnFilters['ActivityBudgetList'] = _.map(item.get('values'), function(item) {
					return parseInt(item.id);
				});
				break;
			/*
			 * case 'Donor Type': var auxDonorTypes = _.map(item.get('values'),
			 * function(item) { return parseInt(item); });
			 * blob.columnFilters["Donor Id"] = _.union(blob["Donor Id"],
			 * auxDonorTypes); break;
			 */
			/*
			 * case 'Donor Group': var auxDonors = _.map(item.get('values'),
			 * function(item) { return parseInt(item); });
			 * blob.columnFilters["Donor Id"] = _.union(blob["Donor Id"],
			 * auxDonors); break;
			 */
			case 'Donor Agency':
				var auxDonorAgencies = _.map(item.get('values'), function(item) {
					return parseInt(item.id);
				});
				blob.columnFilters["Donor Id"] = _.union(blob["Donor Id"], auxDonorAgencies);
				break;
			case 'Primary Sector':
				blob.columnFilters['Primary Sector Id'] = _.map(item.get('values'), function(item) {
					return parseInt(item.id);
				});
				break;
			}
		});

		console.log(blob);
		return blob;
	};

	FilterUtils.prototype = {
		constructor : FilterUtils
	};

	return FilterUtils;
});