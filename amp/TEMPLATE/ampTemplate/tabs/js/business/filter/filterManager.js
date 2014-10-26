define([ 'filtersWidget', 'business/grid/gridManager', 'jquery' ], function(FiltersWidget, GridManager, jQuery) {

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
			jQuery(container).hide();
		});
		app.TabsApp.listenTo(app.TabsApp.filtersWidget, 'apply', function(data) {
			console.log('filters apply');
			var filters = app.TabsApp.filtersWidget.view.serializedFilters;

			GridManager.filter(app.TabsApp.currentId, filters);

			console.log(filters);
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
			Years : {
				startYear : 1998,
				endYear : 2020
			},
			Donor : []
		};
		_.each(data.models, function(item, i) {
			switch (item.get('name')) {
			case 'Financing Instrument':
				blob.FinancingInstrumentsList = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Contracting Agency':
				blob['Contracting Agency'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Executing Agency':
				blob['Executing Agency'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Implementing Agency':
				blob['Implementing Agency'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Beneficiary Agency':
				blob['Beneficiary Agency'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Responsible Organization':
				blob['Responsible Organization'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Type Of Assistance':
				blob['TypeOfAssistanceList'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Status':
				blob['ActivityStatusList'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'National Planning Objectives':
				blob['National Plan Objective'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Primary Program':
				blob['Primary Program'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Approval Status':
				blob['ActivityApprovalStatus'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Secondary Program':
				blob['Secondary Program'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'On/Off/Treasury Budget':
				blob['ActivityBudgetList'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Donor Type':
				var auxDonorTypes = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				blob.Donor = _.union(blob.Donor, auxDonorTypes);
				break;
			case 'Donor Group':
				var auxDonors = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				blob.Donor = _.union(blob.Donor, auxDonors);
				break;
			case 'Donor Agency':
				var auxDonorAgencies = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				blob.Donor = _.union(blob.Donor, auxDonorAgencies);
				break;
			}
		});

		console.log(blob);
		return blob;
	};

	return FilterManager;
});