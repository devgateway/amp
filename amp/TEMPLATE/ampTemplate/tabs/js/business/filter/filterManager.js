define([ 'filtersWidget', 'business/grid/gridManager', 'business/filter/filterUtils', 'jquery','underscore' ], function(
		FiltersWidget, GridManager, FilterUtils, jQuery,_) {

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
		var containerName = "#filter-popup";
		var container = jQuery(containerName);

		// Create the FilterWidget instance.
		app.TabsApp.filtersWidget = new FiltersWidget({
			el : containerName,
			draggable : true,
			caller: 'TAB'
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
			// Save just applied filters in case the user hits "reset" button.
			app.TabsApp.serializedFilters = app.TabsApp.filtersWidget.serialize();
			console.log(app.TabsApp.serializedFilters);
			// Get list of human friendly applied filters we will use in the
			// accordion.
			var readableFilters = app.TabsApp.filtersWidget.serializeToModels();
			console.log(readableFilters);

			// Change the format of the object before sending it to the endpoint
			// for refiltering.
			var auxFilters = app.TabsApp.serializedFilters;
			GridManager.filter(app.TabsApp.currentTab.get('id'), auxFilters, app.TabsApp.appliedSettings);

			// Update the accordion with the newly applied filters.
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

	FilterManager.saveTab = function(dialogView) {
		var transformedFilters = FilterUtils.widgetFiltersToJavaFilters(app.TabsApp.serializedFilters);
		var reportsInputs = jQuery('[id^="newTabNameInput"]');
		var reportNames = _.map(reportsInputs, function(input)
				{ 
				var id= input.id;
				var lang = id.substring(id.indexOf('_') + 1);
				var value=input.value;
				return {lang:lang,name:value}; 
		});
		var data = JSON.stringify({
			filters : transformedFilters,
			reportData : reportNames
		});
		var tabId = app.TabsApp.currentTab.get('id');
		jQuery.ajax({
			url : "/rest/data/report/saveTab/" + tabId,
			dataType : 'text',
			method : 'post',
			contentType : 'application/json; charset=utf-8',
			data : data
		}).done(function(data, textStatus, jqXHR) {
			if(data == undefined) {
				var searchedId = 'newTabNameInput_' + window.currentLocale;
				var searchedIdEn = 'newTabNameInput_en';
				var newTabName = jQuery('[id="' + searchedId + '"]').val();
				if (!newTabName)
					newTabName = jQuery('[id="' + searchedIdEn + '"]').val();
				jQuery('#tab-link-'+tabId).prop('title', newTabName);
				jQuery('#tab-link-'+tabId).html(newTabName);
				jQuery(dialogView.el).dialog('close');
				app.TabsApp.tabsCollection.fetchData();
			} else {
				alert(data);
			}
		});
	};

	return FilterManager;
});