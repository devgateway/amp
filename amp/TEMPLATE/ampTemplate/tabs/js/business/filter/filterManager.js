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
			var serializedFilters = app.TabsApp.filtersWidget.serialize();
			var readableFilters = app.TabsApp.filtersWidget.serializeToModels();
			console.log(serializedFilters);
			console.log(readableFilters);

			var filters = serializedFilters.columnFilters;
			filters.date = serializedFilters.otherFilters.date;
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

	return FilterManager;
});