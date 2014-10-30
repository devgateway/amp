define([ 'filtersWidget', 'business/grid/gridManager', 'business/filter/filterUtils', 'jquery' ], function(FiltersWidget, GridManager,
		FilterUtils, jQuery) {

	"use strict";

	function FilterManager() {
		if (!(this instanceof FilterManager)) {
			throw new TypeError("FilterManager constructor cannot be called as a function.");
		}
	}

	FilterManager.prototype = {
		constructor : FilterManager
	};

	FilterManager.initializeFilterWidget = function() {
		var containerName = "#filters-container";
		var container = jQuery(containerName);

		// Create the FilterWidget instance.
		app.TabsApp.filtersWidget = new FiltersWidget({
			el : containerName,
			draggable : true
		});

		// Register apply and cancel buttons.
		app.TabsApp.listenTo(app.TabsApp.filtersWidget, 'cancel', function() {
			console.log('filters cancel');
			// app.TabsApp.serializedFilters =
			// app.TabsApp.filtersWidget.serialize();
			jQuery(container).hide();
		});
		app.TabsApp.listenTo(app.TabsApp.filtersWidget, 'apply', function(data) {
			console.log('filters apply');
			var filters = app.TabsApp.filtersWidget.serialize();
			var readableFilters = app.TabsApp.filtersWidget.serializeToModels();
			console.log(filters);
			console.log(readableFilters);

			GridManager.filter(app.TabsApp.currentId, filters);

			FilterUtils.updateFiltersRegion(readableFilters);

			jQuery(container).hide();
		});

		// Workaround for the problem of widget not being fully loaded with
		// data when the 'loaded' event has been triggered :(
		// So we call showFilters to load all data in the widget and
		// immediately after we hide it.
		app.TabsApp.filtersWidget.showFilters();
		jQuery(container).hide();

	};

	FilterManager.convertJavaFiltersToJS = function(data) {
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

	return FilterManager;
});