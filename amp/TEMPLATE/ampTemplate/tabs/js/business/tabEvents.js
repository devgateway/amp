/*https://gist.github.com/jonnyreeves/2474026*/
/*https://github.com/icereval/backbone-documentmodel*/

define([ 'marionette', 'collections/contents', 'models/content', 'views/dynamicContentView', 'text!views/html/filtersWrapperTemplate.html',
		'text!views/html/filtersItemTemplate.html', 'models/filter', 'collections/filters', 'models/tab',
		'text!views/html/invisibleTabLinkTemplate.html', 'text!views/html/legendsTemplate.html', 'business/grid/gridManager',
		'business/translations/translationManager', 'jquery', 'jqueryui' ], function(Marionette, Contents, Content, DynamicContentView,
		filtersTemplate, filtersItemTemplate, Filter, Filters, Tab, invisibleTabLinkTemplate, legendsTemplate, gridManager,
		TranslationManager, jQuery) {

	"use strict";

	function TabEvents() {
		// Constructor
		function TabEvents() {
		}
	}

	// Some private method.
	function putAnimation() {
		return '<span><img src="/TEMPLATE/ampTemplate/tabs/css/images/ajax-loader.gif"/></span>';
	}

	function extractFilters(content) {
		var filters = new Filters();
		var filtersJson = content.get('reportMetadata').get('reportSpec').get('filters').get('filterRules');
		jQuery(filtersJson.keys()).each(function(i, item) {
			var subElement = filtersJson.get(item);
			if (subElement instanceof Backbone.Collection) {
				if (item.indexOf('ElementType = ENTITY') > -1) {
					var name = item.substring(item.indexOf('[') + 1, item.indexOf(']'));
					var element = subElement.models[0];
					var content = [];
					if (element.get('value') != null) {
						content = element.get('value');
					} else if (element.get('values') != null) {
						_.each(element.get('values').models, function(item, i) {
							content.push(item.get('value'));
						});
					}

					var auxFilter = new Filter({
						name : name,
						values : content
					});
					filters.add(auxFilter);
				} else if (item.indexOf('ElementType = DATE') > -1) {

				} else if (item.indexOf('ElementType = YEAR') > -1) {

				}
			}
		});
		return filters;
	}

	function retrieveTabContent(selectedTabIndex) {
		var id = app.TabsApp.tabItemsView.collection.models[selectedTabIndex].get('id');
		app.TabsApp.currentId = id;

		// Create a region where the dynamic content will be rendered
		// inside
		// the tab.
		var regionsName = '#main-dynamic-content-region_' + id;
		app.TabsApp.addRegions({
			'filtersRegion' : regionsName
		});

		if (id >= 0) {
			// Get collection with data we will use to render the tab
			// content.
			var firstContent = new Content({
				id : id
			});

			// --------------------------------------------------------------------------------------//
			// TODO: Move filters section elsewhere.
			// Create collection of Filters.
			var filters = extractFilters(firstContent);

			// Define the views.
			var FilterItemView = Marionette.ItemView.extend({
				tagName : 'div',
				className : 'round-filter',
				template : jQuery(filtersItemTemplate, '#template-filters').html(),
				events : {
					'click' : "testclick"
				},
				testclick : function() {
					console.log('testclick');
				}
			});
			var CompositeItemView = Marionette.CompositeView.extend({
				template : jQuery(filtersTemplate, '#template-table-filters').html(),
				childView : FilterItemView
			});
			var compositeView = new CompositeItemView({
				collection : filters
			});

			// Render views.
			var dynamicLayoutView = new DynamicContentView({
				id : id,
				filters : filters
			});
			app.TabsApp.filtersRegion.show(dynamicLayoutView);
			dynamicLayoutView.filters.show(compositeView);

			// Create accordion for filters area.
			jQuery("#main-dynamic-content-region_" + id + " #filters-collapsible-area").accordion({
				collapsible : true,
				active : false
			});

			// --------------------------------------------------------------------------------------//
			// TODO: make complex view for adding more info in this
			// section.
			var Legend = Backbone.Model.extend({});
			var LegendView = Marionette.ItemView.extend({
				template : _.template(legendsTemplate),
				className : 'legends-container',
				onShow : function() {
					jQuery(document).tooltip({
						items : ('#show-legends-link-' + id),
						content : function() {
							return jQuery('#show_legend_pop_box').html();
						}
					});
				}
			});
			var legend = new Legend({
				currencyCode : firstContent.get('reportMetadata').get('reportSpec').get('settings').get('currencyCode'),
				id : id
			});
			var legendView = new LegendView({
				model : legend
			});
			dynamicLayoutView.legends.show(legendView);

			// --------------------------------------------------------------------------------------//
			gridManager.populateGrid(id, dynamicLayoutView, firstContent);

		} else if (id == -1) {
			// "More Tabs..." tab.
			var ItemView = Marionette.ItemView.extend({
				model : Tab,
				template : jQuery(invisibleTabLinkTemplate, '#invisibleTabLink').html()
			});

			var InvisibleTabsCollectionView = Marionette.CollectionView.extend({
				childView : ItemView,
				tagName : 'ul'
			});

			app.TabsApp.filtersRegion.show(new InvisibleTabsCollectionView({
				collection : app.TabsApp.tabsCollection
			}));
		}
	}

	// "Class" methods definition here.
	TabEvents.prototype = {
		constructor : TabEvents,
		onCreateTab : function(event, ui) {
			console.log('create tab');
			this.onActivateTab(event, ui);

			TranslationManager.searchAndTranslate();
		},
		onActivateTab : function(event, ui) {
			console.log('activate tab');

			// This tab is refreshed so we reset the filter widget status.
			app.TabsApp.currentTabOpenedFilters = false;
			app.TabsApp.serializedFilters = null;

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
		}
	};

	return TabEvents;
});