'use strict';

// Global variable for dev purposes.
var app = app || {};

define([ 'marionette', 'collections/tabs', 'models/tab', 'views/tabItemView', 'views/tabItemsView', 'views/tabContentView',
    'views/tabContentsView', 'text!views/html/regions.html', 'business/tabEvents', 'jquery', 'jqueryui' ], function (Marionette, Tabs, Tab, TabItemView, TabItemsView, TabContentView, TabContentsView, regionsHtml, TabEvents, jQuery) {

    // Load the regions html into the DOM.
    var tabsObject = jQuery('#tabs-container');
    tabsObject.append(regionsHtml);

    // Create our Marionette app.
    app.TabsApp = new Marionette.Application();

    // Define 2 regions where the tab and the content will be drawn.
    // Each region is mapped to a <section> element on the html.
    app.TabsApp.addRegions({
        'tabsRegion': '#tabs-section',
        'tabsContentRegion': '#tabs-content-section'
    });

    app.TabsApp.on('start', function () {
        console.log('app started');
        Backbone.history.start();
    });

    // Instantiate the collection of Tab (now the collection handles that but
    // should be moved elsewhere)
    var tabs = new Tabs();

    // Instantiate both CollectionView containers with the data to
    // create the
    // tabs.
    var tabs2 = tabs;
    var tabs = new TabItemsView({
        collection: tabs
    });
    var content = new TabContentsView({
        // If we iterate tabs object again then TabContentsView will throw
        // an error.
        collection: tabs2
    });

    // Render both CollectionView containers, each one on a region.
    // Basically what we do is render each CollectionView using its
    // template and
    // into the region it belongs.
    app.TabsApp.tabsRegion.show(tabs);
    app.TabsApp.tabsContentRegion.show(content);

    // Save the tabs collection for later usage.
    app.tabs = tabs;

    var tabEvents = new TabEvents();
    // JQuery create the tabs and assign some events to our event manager class.
    tabsObject.tabs({activate: function (event, ui) {
        tabEvents.onActivateTab(event, ui)
    },
        create: function (event, ui) {
            tabEvents.onCreateTab(event, ui)
        }});

    app.TabsApp.start();

});