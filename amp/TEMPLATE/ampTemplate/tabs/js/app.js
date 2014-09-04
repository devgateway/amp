'use strict';

// Global variable for dev purposes.
var app = app || {};

define([ 'marionette', 'collections/tabs', 'models/tab', 'views/tabItemView', 'views/tabItemsView', 'views/tabBodyView',
		'views/tabBodysView', 'text!views/html/regions.html', 'business/tabEvents', 'jquery', 'jqueryui' ], function(Marionette, Tabs, Tab,
		TabItemView, TabItemsView, TabBodyView, TabBodysView, regionsHtml, TabEvents, jQuery) {

	// Load the regions html into the DOM.
	var tabsObject = jQuery('#tabs-container');
	tabsObject.append(regionsHtml);

	// Create our Marionette app.
	app.TabsApp = new Marionette.Application();

	// Define 2 regions where the tab and the content will be drawn.
	// Each region is mapped to a <section> element on the html.
	app.TabsApp.addRegions({
		'tabsRegion' : '#tabs-section',
		'tabsBodyRegion' : '#tabs-body-section'
	});

	app.TabsApp.on('start', function() {
		console.log('app started');
		Backbone.history.start();
	});

	/*
	 * We need to group those tabs that are not visible so assuming the REST
	 * endpoint will return a list with the visible tabs FIRST we do the
	 * following: 1) Clone the original list of tabs. 2) Iterate over the
	 * original list from bottom to top (no visible tabs first) and move all
	 * invisible tabs to the new list. 3) Add a new "More Tabs.." tab if needed.
	 */
	var originalTabs = new Tabs();
	var notVisibleTabs = originalTabs.clone();
	var hasMoreTabs = false;
	notVisibleTabs.reset();
	for ( var i = originalTabs.models.length - 1; i >= 0; i--) {
		if (originalTabs.models[i].get('visible') == false) {
			var row = originalTabs.models.splice(i, 1);
			notVisibleTabs.push(row);
			hasMoreTabs = true
		}
	}
	if (hasMoreTabs) {
		var moreTabsTab = new Tab({
			id : -1,
			name : 'More Tabs...',
			visible : true
		});
		originalTabs.push(moreTabsTab);
	}

	// Instantiate both CollectionView containers with the data to
	// create the
	// tabs.
	var tabs2 = originalTabs;
	var tabs = new TabItemsView({
		collection : originalTabs
	});
	var content = new TabBodysView({
		// If we iterate tabs object again then TabContentsView will throw
		// an error.
		collection : tabs2
	});

	// Render both CollectionView containers, each one on a region.
	// Basically what we do is render each CollectionView using its
	// template and
	// into the region it belongs.
	app.TabsApp.tabsRegion.show(tabs);
	app.TabsApp.tabsBodyRegion.show(content);

	// Save the tabs collection for later usage.
	app.tabs = tabs;

	var tabEvents = new TabEvents();
	// JQuery create the tabs and assign some events to our event manager class.
	tabsObject.tabs({
		activate : function(event, ui) {
			tabEvents.onActivateTab(event, ui);
		},
		create : function(event, ui) {
			tabEvents.onCreateTab(event, ui);
		}
	});

	app.TabsApp.start();

});