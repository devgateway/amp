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
	 * We need to group those tabs that are not visible, so assuming the REST
	 * endpoint will return a list with the visible tabs FIRST, then we do the
	 * following: 1) Iterate over the original list from bottom to top (no
	 * visible tabs first) and move all invisible tabs to the new list. 2) Add a
	 * new "More Tabs.." tab if needed plus a hidden tab we will use to allocate
	 * content for the invisible tabs.
	 */
	var tabsCollection = new Tabs();
	tabsCollection.fetchData();
	var notVisibleTabsCollection = new Tabs();
	var hasMoreTabs = false;
	for ( var i = tabsCollection.models.length - 1; i >= 0; i--) {
		if (tabsCollection.models[i].get('visible') == false) {
			var row = tabsCollection.models.splice(i, 1);
			notVisibleTabsCollection.push(row);
			hasMoreTabs = true;
		}
	}
	if (hasMoreTabs) {
		var hiddenTab = new Tab({
			id : -2,
			name : '',
			visible : true
		});
		tabsCollection.push(hiddenTab);
		var moreTabsTab = new Tab({
			id : -1,
			name : 'More Tabs...',
			visible : true
		});
		tabsCollection.push(moreTabsTab);
	}

	// Instantiate both CollectionView containers with the data to
	// create the
	// tabs.
	var tabsCollectionCopy = tabsCollection;
	var tabItemsView = new TabItemsView({
		collection : tabsCollection
	});
	var tabBodysView = new TabBodysView({
		// If we iterate tabs object again then TabContentsView will throw
		// an error.
		collection : tabsCollectionCopy
	});

	// Render both CollectionView containers, each one on a region.
	// Basically what we do is render each CollectionView using its
	// template and
	// into the region it belongs.
	app.TabsApp.tabsRegion.show(tabItemsView);
	app.TabsApp.tabsBodyRegion.show(tabBodysView);

	// Save the tabs collection for later usage.
	app.TabsApp.tabItemsView = tabItemsView;
	app.TabsApp.notVisibleTabsCollection = notVisibleTabsCollection;

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

	// If we are grouping tabs under the last "more tabs..." tab then we need to
	// create another hidden tab for later use.
	if (hasMoreTabs) {
		var hiddenTab = $("#tabs-section ul li")[app.TabsApp.tabItemsView.collection.models.length - 2];
		$(hiddenTab).hide();
		// tabsObject.tabs("refresh");
	}

	app.TabsApp.start();

});