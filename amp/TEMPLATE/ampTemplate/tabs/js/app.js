var app = app || {};

$(function() {
	'use strict';

	// Create our Marionette app.
	app.TabsApp = new Backbone.Marionette.Application();

	// Define 2 regions where the tab and the content will be drawn.
	// Each region is mapped to a <section> element on the html.
	app.TabsApp.addRegions({
		'tabsRegion' : '#tabs-section',
		'tabsContentRegion' : '#tabs-content-section'
	});

	app.TabsApp.on('initialize:after', function() {
		Backbone.history.start();
	});

	// Create fake data.
	var tabs = new app.Tabs();
	var tab1 = new app.Tab(
			{
				name : 'tab1',
				title : 'Im Tab number 1',
				order : 0,
				content : 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.'
			});
	tabs.add(tab1);
	var tab2 = new app.Tab(
			{
				name : 'tab2',
				title : 'Tab number 2',
				order : 1,
				content : 'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit,'
			});
	tabs.add(tab2);

	// Instantiate both CollectionView containers with the data to create the
	// tabs.
	var tabs2 = tabs;
	var tabs = new app.TabItemsView({
		collection : tabs
	});
	var content = new app.TabContentsView({
		collection : tabs2
	// If we iterate tabs object again then TabContentsView will throw an error.
	});

	// Render both CollectionView containers, each one on a region.
	// Basically what we do is render each CollectionView using its template and
	// into the region it belongs.
	app.TabsApp.tabsRegion.show(tabs);
	app.TabsApp.tabsContentRegion.show(content);

	// JQuery create the tabs.
	$('#tabs-container').tabs();

})