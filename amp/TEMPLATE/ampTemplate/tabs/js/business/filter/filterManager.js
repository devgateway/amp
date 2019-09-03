define(['business/grid/gridManager', 'business/filter/filterUtils', 'jquery','underscore' ], function(
		 GridManager, FilterUtils, jQuery,_) {

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
		app.TabsApp.filtersWidget = new ampFilter({
			el : containerName,
			draggable : true,
			caller: 'TAB'
		});

		// Register apply and cancel buttons.
		app.TabsApp.listenTo(app.TabsApp.filtersWidget, 'cancel', function() {
			jQuery(container).hide();
		});
		app.TabsApp.listenTo(app.TabsApp.filtersWidget, 'apply', function(data) {			
			// Save just applied filters in case the user hits "reset" button.
			app.TabsApp.serializedFilters = data || {};
			
			// Get list of human friendly applied filters we will use in the
			// accordion.
			GridManager.filter(app.TabsApp.currentTab.get('id'), app.TabsApp.serializedFilters.filters, app.TabsApp.settingsWidget.toAPIFormat());
			// Update the accordion with the newly applied filters.
			FilterUtils.updateFiltersRegion(app.TabsApp.filtersWidget.serializeToModels());

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
		// If filterWidget was never opened.
		if (app.TabsApp.serializedFilters === null) {
			app.TabsApp.filtersWidget.deserialize(app.TabsApp.rawFilters, {
				silent : true
			});
			app.TabsApp.serializedFilters = app.TabsApp.filtersWidget.serialize();
		}
				
		var reportsInputs = jQuery('[id^="newTabNameInput"]');
		var reportNames = _.map(reportsInputs, function(input)
				{ 
				var id= input.id;
				var lang = id.substring(id.indexOf('_') + 1);
				var value=input.value;
				return {lang:lang,name:value}; 
		});
		var sidx = (app.TabsApp.currentTab.get('currentSorting') !== null ? app.TabsApp.currentTab.get('currentSorting').sidx : null);
		var sord = (app.TabsApp.currentTab.get('currentSorting') !== null ? app.TabsApp.currentTab.get('currentSorting').sord : null);
		var data = JSON.stringify({
			filters : app.TabsApp.serializedFilters ? app.TabsApp.serializedFilters.filters : {},
		    'include-location-children': app.TabsApp.serializedFilters ? app.TabsApp.serializedFilters['include-location-children'] : true,
			reportData : reportNames,
			sidx: sidx,
			sord: sord,
			settings: app.TabsApp.settingsWidget.toAPIFormat()
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
                app.TabsApp.tabsCollectionData.fetchData();
                app.TabsApp.tabUtils.shortenTabNames(app.TabsApp.tabsCollectionData.models);
                app.TabsApp.tabsCollection._byId[tabId] = app.TabsApp.tabsCollectionData._byId[tabId];
				jQuery('#tab-link-'+tabId).prop('title', app.TabsApp.tabsCollection._byId[tabId].get('name'));
				jQuery('#tab-link-'+tabId).html(app.TabsApp.tabsCollection._byId[tabId].get('shortName'));
				jQuery(dialogView.el).dialog('close');

                // AMP-19587: TODO: remove the next line. If we fetch the tabs collection we
                // loose the 'more tabs' tab and initial context of the tabs @see app.js LOC 117-140
                //app.TabsApp.tabsCollection.fetchData();
            } else {
                alert(data);
			}
		});
	};

	return FilterManager;
});