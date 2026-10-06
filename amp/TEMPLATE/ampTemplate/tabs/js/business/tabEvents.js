/*https://gist.github.com/jonnyreeves/2474026*/
/*https://github.com/icereval/backbone-documentmodel*/

define([ 'marionette', 'models/content', 'models/legend', 'views/dynamicContentView', 'text!views/html/filtersWrapperTemplate.html',
		'text!views/html/filtersItemTemplate.html', 'models/tab', 'text!views/html/invisibleTabLinkTemplate.html',
		'text!views/html/legendsTemplate.html', 'business/grid/gridManager', 'translationManager',
		'business/filter/filterUtils', 'util/tabUtils', 'jquery', 'jqueryui' ,'models/settings'], function(Marionette, Content, Legend, DynamicContentView,
		filtersTemplate, filtersItemTemplate, Tab, invisibleTabLinkTemplate, legendsTemplate, gridManager, TranslationManager, FilterUtils,
		TabUtils, jQuery, Settings) {

	"use strict";
	//var settingsForCurrency = app.TabsApp.settings;

	function TabEvents() {
		// Constructor
		function TabEvents() {
		}
	}

	// Some private method.
	function putAnimation() {
		return '<span><img src="/TEMPLATE/ampTemplate/tabs/css/images/ajax-loader.gif"/></span>';
	}

	function retrieveTabContent(selectedTabIndex) {
		app.TabsApp.currentTab = app.TabsApp.tabItemsView.collection.models[selectedTabIndex];
		app.TabsApp.currentId = app.TabsApp.currentTab.get('id');

		// Create a region where the dynamic content will be rendered
		// inside
		// the tab.
		var regionsName = '#main-dynamic-content-region_' + app.TabsApp.currentTab.get('id');
		app.TabsApp.addRegions({
			'dynamicContentRegion' : regionsName
		});

		if (app.TabsApp.currentTab.get('id') >= 0) {
			// Get collection with data we will use to render the tab
			// content.
			var firstContent = new Content({
				id : app.TabsApp.currentTab.get('id')
			});

			// --------------------------------------------------------------------------------------//
			// TODO: Move filters section elsewhere.
			// Create collection of Filters used for legends.
			app.TabsApp.rawFilters = firstContent.filtersToJSON();
			app.TabsApp.filtersWidget.loaded.done(function() {
				// includeLocationChildren is not part of the filters but the spec :((( so we add it manually.
				var filters = firstContent.filtersToJSON();
				var includeLocationChildren = firstContent.get('reportMetadata').get('reportSpec').get('includeLocationChildren');
				if (filters) {
					filters.filters.includeLocationChildren = includeLocationChildren;
				}
				app.TabsApp.filtersWidget.deserialize(filters, {
					silent : true
				});
				
				app.TabsApp.filters = FilterUtils.extractFilters(app.TabsApp.filtersWidget.serializeToModels());
				
				// Variable to save the current serialized filters from widget.
				app.TabsApp.serializedFilters = null;
				// Save default sorters if any.
				app.TabsApp.currentSorting = FilterUtils.extractSorters(firstContent.get('reportMetadata').get('reportSpec').get('sorters'),
						firstContent.get('reportMetadata').get('reportSpec').get('columns'),
						firstContent.get('reportMetadata').get('reportSpec').get('measures'),
						firstContent.get('reportMetadata').get('reportSpec').get('hierarchies'));
				// Define the views.
				var FilterItemView = Marionette.ItemView.extend({
					tagName : 'div',
					className : 'round-filter-group',
					template : jQuery(filtersItemTemplate, '#template-filters').html(),
					events : {
						'click' : "testclick"
					},
					testclick : function() {					
					}
				});
				var CompositeItemView = Marionette.CompositeView.extend({
					template : jQuery(filtersTemplate, '#template-table-filters').html(),
					childView : FilterItemView
				});
				var compositeView = new CompositeItemView({
					collection : app.TabsApp.filters
				});

				app.TabsApp.settingsWidget.restoreFromSaved(firstContent.get('reportMetadata').get('settings').toJSON());			
				app.TabsApp.numericFormatOptions = firstContent.get('reportMetadata').get('settings').attributes;

				// Render views.
				var dynamicLayoutView = new DynamicContentView({
					id : app.TabsApp.currentTab.get('id'),
					filters : app.TabsApp.filters
				});
				app.TabsApp.dynamicContentRegion.show(dynamicLayoutView);
				
				dynamicLayoutView.filters.show(compositeView);
				
				// Create accordion for filters area.
				jQuery("#main-dynamic-content-region_" + app.TabsApp.currentTab.get('id') + " #filters-collapsible-area").accordion({
					collapsible : true,
					active : false
				});

				// --------------------------------------------------------------------------------------//
				// TODO: make complex view for adding more info in this
				// section.
				var LegendView = Marionette.ItemView.extend({
					template : _.template(legendsTemplate),
					className : 'legends-container',
					onShow : function() {
						jQuery(document).tooltip({
							items : ('#show-legends-link-' + app.TabsApp.currentTab.get('id')),
							content : function() {
								return jQuery('#show_legend_pop_box').html();
							}
						});
					}
				});
				var units = "";
				switch (firstContent.get('reportMetadata').get('reportSpec').get('settings').get('unitsOption')) {
				case 'AMOUNTS_OPTION_UNITS':
					units = TranslationManager.getTranslated("Amounts in units");
					break;
				case 'AMOUNTS_OPTION_THOUSANDS':
					units = TranslationManager.getTranslated("Amounts in thousands");
					break;
				case 'AMOUNTS_OPTION_MILLIONS':
					units = TranslationManager.getTranslated("Amounts in millions");
					break;
				}
				
				
				
				var currencyCode = firstContent.get('reportMetadata').get('settings').get(app.TabsApp.settingsWidget.Constants.CURRENCY_ID) || app.TabsApp.settingsWidget.definitions.getDefaultCurrencyId();
				var currency  = app.TabsApp.settingsWidget.definitions.findCurrencyById(currencyCode);
                var currencyValue = currencyCode;
                //TODO this is a work around in case we disable a currency that we have active in a tab.
				//TODO nevertheless we have in value the same as in code, it does not use the currency label.
				//TODO we will provide the proper fix in AMP-28464
                if(currency){
					currencyValue = currency.value;
				}
				
				var legend = new Legend({
					currencyCode : currencyCode,
					currencyValue : currencyValue,
					units : units,
					id : app.TabsApp.currentTab.get('id')
				});
				var legendView = new LegendView({
					model : legend
				});
				dynamicLayoutView.legends.show(legendView);

				TranslationManager.searchAndTranslate();

				// --------------------------------------------------------------------------------------//
				gridManager.populateGrid(app.TabsApp.currentTab.get('id'), dynamicLayoutView, firstContent);
			});			
			

		} else if (app.TabsApp.currentTab.get('id') == -1) {
			// "More Tabs..." tab.
			var ItemView = Marionette.ItemView.extend({
				model : Tab,
				template : jQuery(invisibleTabLinkTemplate, '#invisibleTabLink').html()
			});

			var InvisibleTabsCollectionView = Marionette.CollectionView.extend({
				childView : ItemView,
				tagName : 'ul'
			});

			app.TabsApp.dynamicContentRegion.show(new InvisibleTabsCollectionView({
				collection : app.TabsApp.tabsCollection
			}));
		}
	}

	// "Class" methods definition here.
	TabEvents.prototype = {
		constructor : TabEvents,
		onCreateTab : function(event, ui) {
			var existDefaultTab = jQuery("#tabs-container").attr("data-tab-id");
			if (existDefaultTab != "null" && existDefaultTab != "" && existDefaultTab != undefined) {
				TabUtils.activateTabById(Number(existDefaultTab));
			}
			this.onActivateTab(event, ui);

			TranslationManager.searchAndTranslate();
		},
		onActivateTab : function(event, ui) {
			// Restart app variables defined for the active tab.
			app.TabsApp.serializedFilters = null;
			app.TabsApp.filters = null;
			app.TabsApp.rawFilters = null;
			app.TabsApp.currentGrid = null;
			app.TabsApp.currentTab = null;
			app.TabsApp.numericFormatOptions = null;

			// TODO: move this logic elsewhere.
			var panel = null;
			var selectedTabIndex = 0;
			if (ui.panel != undefined) {
				panel = ui.panel;
				selectedTabIndex = ui.tab.index();
			} else {
				panel = ui.newPanel;
				selectedTabIndex = ui.newTab.index();
			}
			retrieveTabContent(selectedTabIndex);
			TranslationManager.searchAndTranslate();
		}
	};

	return TabEvents;
});