'use strict';

define([ 'marionette', 'collections/tabs', 'models/tab', 'views/tabItemView', 'views/tabItemsView', 'views/tabContentView',
		'views/tabContentsView', 'text!views/html/regions.html', [ 'TabsCollectionStorage' ], 'jquery', 'jqueryui' ], function(Marionette,
		Tabs, Tab, TabItemView, TabItemsView, TabContentView, TabContentsView, regionsHtml, tabsCollectionStorage, jQuery) {

	var app = app || {};

	// Load the regions html into the DOM.
	jQuery('#tabs-container').append(regionsHtml);

	// Create our Marionette app.
	app.TabsApp = new Marionette.Application();

	// Define 2 regions where the tab and the content will be drawn.
	// Each region is mapped to a <section> element on the html.
	app.TabsApp.addRegions({
		'tabsRegion' : '#tabs-section',
		'tabsContentRegion' : '#tabs-content-section'
	});

	app.TabsApp.on('start', function() {
		console.log('app started');
		Backbone.history.start();
	});

	// Create test data.
	var tabs = new Tabs();

	// Instantiate both CollectionView containers with the data to
	// create the
	// tabs.
	var tabs2 = tabs;
	var tabs = new TabItemsView({
		collection : tabs
	});
	var content = new TabContentsView({
		collection : tabs2
	// If we iterate tabs object again then TabContentsView will throw
	// an error.
	});

	// Render both CollectionView containers, each one on a region.
	// Basically what we do is render each CollectionView using its
	// template and
	// into the region it belongs.
	app.TabsApp.tabsRegion.show(tabs);
	app.TabsApp.tabsContentRegion.show(content);

	// JQuery create the tabs.
	jQuery('#tabs-container').tabs();

	app.TabsApp.start();

});