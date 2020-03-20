'use strict';

// Global variable for dev purposes.
var app = app || {};

define(
		[ 'marionette', 'collections/tabs', 'models/tab', 'views/tabItemView', 'views/tabItemsView',
				'views/tabBodyView', 'views/tabBodysView', 'business/tabEvents', 'util/tabUtils',
				'business/filter/filterManager','business/settings/settingsManager', 'translationManager',
				'business/widgets/documentsWidgetManager', 'jquery', 'jqueryui','jqueryuii18n'],
		function(Marionette, Tabs, Tab, TabItemView, TabItemsView, TabBodyView, TabBodysView, TabEvents, TabUtils,
				FilterManager, SettingsManager, TranslationManager, DocumentsWidgetManager, jQuery, jqueryui) {

			// AMP-21281 for IE there is a problem when the cache is enable, the tabs are n
			if(/MSIE \d\d*/i.test(navigator.userAgent) || /rv:11.0/i.test(navigator.userAgent)) {
				jQuery.ajaxSetup({
					cache : false
				});
			}
			var tabContainer = jQuery('#tabs-container');
			
			//the languages should be retrieved when the tabs are loading.
			TranslationManager.getAvailableLanguages();

			// Create our Marionette app.
			app.TabsApp = new Marionette.Application();
			TranslationManager.searchAndTranslate();
			
			// Initialize constants.
			app.TabsApp.COLUMN_ACTIVITY_ID = 'Actvity Id';
			app.TabsApp.TOTAL_COLUMNS_DATA_SUFIX = ' Totals';
			app.TabsApp.TOTAL_COLUMNS_NAME_SUFIX = '[Totals]';
			app.TabsApp.COLUMN_TYPE_MEASURE = 'MEASURE';
			app.TabsApp.COLUMNS_WITH_IDS = ['AMP ID', 'Team'];
			app.TabsApp.DEFAULT_ONE_PAGER_PARAMETER = 'activity';
			app.TabsApp.MANAGER_TYPE = 'Management';

			// Define 2 regions where the tab and the content will be drawn.
			// Each region is mapped to a <section> element on the html.
			app.TabsApp.addRegions({
				'documentsWidgetRegion' : '#resources-widget-container',				
				'tabsRegion' : '#tabs-section',
				'tabsBodyRegion' : '#tabs-body-section'
			});

			app.TabsApp.on('start', function() {
				console.log('app started');

				// Initialize some variables we will need in order to maintain
				// the width
				// of the app.
				app.TabsApp.maxAppWidth = 1000;
				app.TabsApp.mainTableContainer = "main-desktop-container";

				Backbone.history.start();
			});

			/*
			 * We need to group those tabs that are not visible, so assuming the REST endpoint will return a list with
			 * the visible tabs FIRST, then we do the following: 1) Iterate over the original list from bottom to top
			 * (no visible tabs first) and move all invisible tabs to the new list. 2) Add a new "More Tabs.." tab if
			 * needed plus a hidden tab we will use to allocate content for the invisible tabs.
			 */
			var tabsCollectionData = new Tabs();
            tabsCollectionData.fetchData();
            var tabsCollection = tabsCollectionData.clone();
			
			tabsCollection.on("change:isOtherTabNowVisible", function(model, value, options) {
				if (value === true) {
					jQuery("#tab-link-" + model.get('id')).parent().show(250);
				} else {
					jQuery("#tab-link-" + model.get('id')).parent().hide(250);
				}
			}, this);
			
			if (_.filter(tabsCollection.models, function(item) {return (item.get('visible') === true);}).length !== 0) {
				var hasMoreTabs = false;
				if (_.find(tabsCollection.models, function(val) {
						return val.get('visible') === false;
					})) {
					var moreTabsTab = new Tab({
						id : -1,
						name : "<span data-i18n='tabs.common:moreTabs'>More Tabs...</span>",
						visible : true
					});
					tabsCollection.push(moreTabsTab);
					hasMoreTabs = true;
				}
				TabUtils.shortenTabNames(tabsCollection.models);

				// Instantiate both CollectionView containers with the data to
				// create the tabs.
				var tabsCollectionCopy = tabsCollection;
				var tabItemsView = new TabItemsView({
					collection : tabsCollection
				});
				var tabBodysView = new TabBodysView({
					// If we iterate tabs object again then TabContentsView will
					// throw
					// an error.
					collection : tabsCollectionCopy
				});

				// Render both CollectionView containers, each one on a region.
				// Basically what we do is render each CollectionView using its template
				// and into the region it belongs.
				try {
					app.TabsApp.tabsRegion.show(tabItemsView, {
						forceShow : true
					});
				} catch (e) {
					// alert(e);
					setTimeout(function() {
						window.location.reload(1);
					}, 2000);
				}
				app.TabsApp.tabsBodyRegion.show(tabBodysView, {
					forceShow : true
				});

				// Save the tabs collection for later usage.
				app.TabsApp.tabItemsView = tabItemsView;
				app.TabsApp.tabContainer = tabContainer;
				app.TabsApp.tabsCollection = tabsCollection;
				app.TabsApp.tabUtils = TabUtils;
                app.TabsApp.tabsCollectionData = tabsCollectionData;

				// Use only one instance of filters for all tabs.
				FilterManager.initializeFilterWidget();
				
				SettingsManager.initialize();
				app.TabsApp.settingsWidget.definitions.loaded.done(function() {
					// This class manages how to retrieve content and render each tab.
					var tabEvents = new TabEvents();

					// JQuery create the tabs and assign some events to our event
					// manager class.
					TabUtils.createTabs(tabContainer, {
						activate : function(event, ui) {
							tabEvents.onActivateTab(event, ui);
						},
						create : function(event, ui) {
							tabEvents.onCreateTab(event, ui);
						}
					});

					// If we are grouping tabs under the last "more tabs..." tab then we
					// need to hide the "invisible" tabs.
					if (hasMoreTabs) {
						TabUtils.hideInvisibleTabs(tabsCollection.models);
					}

					// Define public function to resize the tab panel.
					app.TabsApp.resizePanel = function(originalWidth, grow) {
						TabUtils.resizePanel(app.TabsApp.currentTab.get('id'), app.TabsApp.maxAppWidth);
					};
				});
				
			} else {
				var message = TranslationManager
						.getTranslated('Click on one of the tabs to display activities. You can add more tabs by using the Tab Manager.');
				var NoTabsView = Marionette.ItemView.extend({
					tagName : 'div',
					className : 'noTabsMessage',
					template : "<span>" + message + "</span>"
				});
				var noTabsView = new NoTabsView();
				app.TabsApp.tabsRegion.show(noTabsView);
			}

			// Load Documents widget.
			if (jQuery("#resources-widget-container").length) {
				DocumentsWidgetManager.showDocumentsWidget();
			}

			app.TabsApp.start();
		});