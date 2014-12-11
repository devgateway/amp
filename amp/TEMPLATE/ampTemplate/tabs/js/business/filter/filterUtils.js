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
					var auxItem = {};
					auxItem.id = element.get('value');
					auxItem.name = element.get('valueToName').attributes[element.get('value')];
					content.push(auxItem);
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
	FilterUtils.updateFiltersRegion = function(filtersFromWidgetWithNames) {
		// First we cleanup current values.
		app.TabsApp.filters.models = [];
		app.TabsApp.dynamicContentRegion.currentView.filters.currentView.render();

		if (filtersFromWidgetWithNames.columnFilters != undefined) {
			for ( var propertyName in filtersFromWidgetWithNames.columnFilters) {
				var auxProperty = filtersFromWidgetWithNames.columnFilters[propertyName];
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
				// Update list collection of Filter used in legends.
				app.TabsApp.filters.models.push(filter);
			}
		}
		if (filtersFromWidgetWithNames.otherFilters != undefined) {
			var dateContent = filtersFromWidgetWithNames.otherFilters["date"];
			if (dateContent != undefined) {
				var filter = new Filter({
					name : "Start Date",
					values : [ {
						id : dateContent.start,
						name : dateContent.start
					} ]
				});
				app.TabsApp.filters.models.push(filter);
				var filter = new Filter({
					name : "End Date",
					values : [ {
						id : dateContent.end,
						name : dateContent.end
					} ]
				});
				app.TabsApp.filters.models.push(filter);
			}
		}
		app.TabsApp.dynamicContentRegion.currentView.filters.currentView.render();
	};

	FilterUtils.convertJavaFiltersToJS = function(data) {
		// This conversion is needed only one time when we load default
		// filters for a tab not after applying a new filter.
		if (app.TabsApp.serializedFilters == null) {
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
				switch (item.get('name')) {

				// cases where columnFilter matches item name
				case 'Responsible Organization':
				case 'Type Of Assistance':
				case 'Financing Instrument':
				case 'Status':
				case 'Approval Status':
				case 'Donor Group':
				case 'Donor Type':
				case 'On/Off/Treasury Budget':
					blob.columnFilters[item.get('name')] = _.map(item.get('values'), function(item_) {
						return parseInt(item_.id);
					});
					break;

				// cases where columnFilter matches item name + ' Id'
				case 'Contracting Agency':
				case 'Executing Agency':
				case 'Implementing Agency':
				case 'Beneficiary Agency':
					blob.columnFilters[item.get('name') + ' Id'] = _.map(item.get('values'), function(item_) {
						return parseInt(item_.id);
					});
					break;
				case 'National Planning Objectives':
					blob.columnFilters['National Planning Objectives Level 1 Id'] = _.map(item.get('values'), function(
							item_) {
						return parseInt(item_.id);
					});
					break;
				case 'Primary Program':
					blob.columnFilters['Primary Program Level 1 Id'] = _.map(item.get('values'), function(item_) {
						return parseInt(item_.id);
					});
					break;
				case 'Secondary Program':
					blob.columnFilters['Secondary Program Level 3 Id'] = _.map(item.get('values'), function(item_) {
						return parseInt(item_.id);
					});
					break;
				case 'Donor Agency':
					blob.columnFilters['Donor Id'] = _.map(item.get('values'), function(item_) {
						return parseInt(item_.id);
					});
					break;
				case 'Contracting Agency Groups':
					blob.columnFilters['Contracting Agency Id'] = _.map(item.get('values'), function(item_) {
						return parseInt(item_.id);
					});
					break;
				case 'Primary Sector':
					// NOTE: Since the filter widget (arbitrarily) uses 3 different fields for Primary Sectors we
					// triplicate the values coming from the endpoint.
					blob.columnFilters['Primary Sector Id'] = _.map(item.get('values'), function(item_) {
						return parseInt(item_.id);
					});
					blob.columnFilters['Primary Sector Sub-Sector Id'] = blob.columnFilters['Primary Sector Id'];
					blob.columnFilters['Primary Sector Sub-Sub-Sector Id'] = blob.columnFilters['Primary Sector Id'];
					break;
				case 'Secondary Sector':
					// NOTE: Since the filter widget (arbitrarily) uses 3 different fields for Secondary Sectors we
					// triplicate the values coming from the endpoint.
					blob.columnFilters['Secondary Sector Id'] = _.map(item.get('values'), function(item_) {
						return parseInt(item_.id);
					});
					blob.columnFilters['Secondary Sector Sub-Sector Id'] = blob.columnFilters['Secondary Sector Id'];
					blob.columnFilters['Secondary Sector Sub-Sub-Sector Id'] = blob.columnFilters['Secondary Sector Id'];
					break;
				default:
					console.error(item);
					break;
				}
			});
			console.log("use blob");
			console.log(blob);
			return blob;
		} else {
			console.log("use serializedFilter");
			console.log(app.TabsApp.serializedFilters);
			return app.TabsApp.serializedFilters;
		}
	};

	FilterUtils.widgetFiltersToJavaFilters = function(originalFilters) {
		/*
		 * if (originalFilters != null && originalFilters.columnFilters != undefined) { if
		 * (originalFilters.columnFilters["Primary Sector Id"] != undefined || originalFilters.columnFilters["Primary
		 * Sector Sub-Sector Id"] != undefined || originalFilters.columnFilters["Primary Sector Sub-Sub-Sector Id"] !=
		 * undefined) { if (originalFilters.columnFilters["Primary Sector Id"] == undefined) {
		 * originalFilters.columnFilters["Primary Sector Id"] = []; } originalFilters.columnFilters["Primary Sector Id"] =
		 * originalFilters.columnFilters["Primary Sector Id"] .concat(originalFilters.columnFilters["Primary Sector
		 * Sub-Sector Id"]); originalFilters.columnFilters["Primary Sector Id"] = originalFilters.columnFilters["Primary
		 * Sector Id"] .concat(originalFilters.columnFilters["Primary Sector Sub-Sub-Sector Id"]); } }
		 */
		return originalFilters;
	};

	FilterUtils.prototype = {
		constructor : FilterUtils
	};

	return FilterUtils;
});
