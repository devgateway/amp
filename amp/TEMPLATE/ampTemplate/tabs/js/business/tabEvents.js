/*https://gist.github.com/jonnyreeves/2474026*/
/*https://github.com/icereval/backbone-documentmodel*/
define([ 'marionette', 'collections/contents', 'models/content', 'views/dynamicContentView', 'text!views/html/filtersWrapperTemplate.html',
		'text!views/html/filtersItemTemplate.html', 'models/filter', 'collections/filters', 'models/tab',
		'text!views/html/invisibleTabLinkTemplate.html', 'jqgrid' ], function(Marionette, Contents, Content, DynamicContentView,
		filtersTemplate, filtersItemTemplate, Filter, Filters, Tab, invisibleTabLinkTemplate, jqGrid) {

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

	function extractMetadata(content) {
		var Metadata = Backbone.DocumentModel.extend({
			defausts : {
				columns : [],
				measures : [],
				hierarchies : []
			}
		});
		var metadata = new Metadata();
		var metadataJson = content.get('reportMetadata').get('reportSpec');
		metadata.columns = metadataJson.get('columns');
		metadata.hierarchies = metadataJson.get('hierarchies');
		metadata.measures = metadataJson.get('measures');
		return metadata;
	}

	function extractFilters(content) {
		var filters = new Filters();
		var filtersJson = content.get('reportMetadata').get('reportSpec').get('filters').get('filterRules');
		$(filtersJson.keys()).each(function(i, item) {
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

	function createJQGridColumnNames(metadata) {
		var ret = [];
		$(metadata.columns.models).each(function(i, item) {
			ret.push(item.get('columnName'));
		});
		$(metadata.hierarchies.models).each(function(i, item) {
			ret.push(item.get('columnName'));
		});
		$(metadata.measures.models).each(function(i, item) {
			ret.push(item.get('measureName'));
		});
		console.log(ret);
		return ret;
	}

	function createJQGridColumnModel(metadata) {
		var ret = [];
		$(metadata.columns.models).each(function(i, item) {
			ret.push({
				name : item.get('columnName')
			});
		});
		$(metadata.hierarchies.models).each(function(i, item) {
			ret.push({
				name : item.get('columnName')
			});
		});
		$(metadata.measures.models).each(function(i, item) {
			ret.push({
				name : item.get('measureName')
			});
		});
		console.log(ret);
		return ret;
	}

	function populateGrid(id, dynamicLayoutView, firstContent) {
		$.ajax({
			async : true,
			url : '/rest/data/report/' + id + '/result/pages/1',
			dataType : 'json'
		}).done(function(data) {
			// TODO: Move grid section elsewhere.
			var TableSectionView = Marionette.ItemView.extend({
				template : '#grid-template'
			});
			var tableSectionView = new TableSectionView();
			dynamicLayoutView.results.show(tableSectionView);

			// Define grid structure.
			var tableStructure = extractMetadata(firstContent);
			var grid = $("#main-dynamic-content-region_" + id + " #tab_grid");
			$(grid).jqGrid({
				caption : 'Caption',
				datatype : 'local',
				height : 250,
				width : 900,
				autowidth : true,
				shrinkToFit : true,
				viewrecords : false,
				emptyrecords : 'No records to view',
				colNames : createJQGridColumnNames(tableStructure),
				colModel : createJQGridColumnModel(tableStructure)
			});
			console.log(data);

			var rows = [];
			getContent(data, rows);
			console.log(rows);
			$.each(rows, function(i, item) {
				$(grid).jqGrid('addRowData', i + 1, item);
			});

		}).fail(function(data) {
			console.error("error");
		}).always(function() {
			console.log("complete");
		});
		;
	}

	function matchColumns(key) {
		var map = new Object();
		map['[Project Title]'] = 'Project Title';
		map['[Region Name]'] = 'Region';
		map['[Total Measures][Actual Commitments]'] = 'Actual Commitments';

		return map[key];
	}

	function getContent(obj, rows) {
		if (obj.children == null || obj.children.length == 0) {
			// console.log(obj.contents);
			var row = {};
			$.each(obj.contents, function(key, element) {
				var colName = matchColumns(key);
				if (colName != undefined && colName != null) {
					row[colName] = element.displayedValue;
				}
			})
			rows.push(row);
		} else {
			$(obj.children).each(function(i, item) {
				getContent(item, rows);
			});
		}
	}

	function retrieveTabContent(selectedTabIndex) {
		var id = app.TabsApp.tabItemsView.collection.models[selectedTabIndex].get('id');

		// Create a region where the dynamic content will be rendered inside
		// the tab.
		var regionsName = '#main-dynamic-content-region_' + id;
		app.TabsApp.addRegions({
			'filtersRegion' : regionsName
		});

		if (id >= 0) {
			// Get collection with data we will use to render the tab content.
			var firstContent = new Content({
				id : id
			});

			// TODO: Move filters section elsewhere.
			// Create collection of Filters.
			var filters = extractFilters(firstContent);

			// Define the views.
			var FilterItemView = Marionette.ItemView.extend({
				tagName : 'div',
				className : 'round-filter',
				template : $(filtersItemTemplate, '#template-filters').html(),
				events : {
					'click' : "testclick"
				},
				testclick : function() {
					console.log('testclick');
				}
			});
			var CompositeItemView = Marionette.CompositeView.extend({
				template : $(filtersTemplate, '#template-table-filters').html(),
				childView : FilterItemView
			});
			var compositeView = new CompositeItemView({
				collection : filters
			});

			// Render views.
			var dynamicLayoutView = new DynamicContentView();
			app.TabsApp.filtersRegion.show(dynamicLayoutView);
			dynamicLayoutView.filters.show(compositeView);

			// Create accordion for filters area.
			$("#main-dynamic-content-region_" + id + " #filters-collapsible-area").accordion({
				collapsible : true,
				active : false
			});
			// Create jQuery buttons.
			$("#main-dynamic-content-region_" + id + " .buttonify").button();

			// --------------------------------------------------------------------------------------//
			populateGrid(id, dynamicLayoutView, firstContent);

		} else if (id == -1) {
			// "More Tabs..." tab.
			var ItemView = Marionette.ItemView.extend({
				model : Tab,
				template : $(invisibleTabLinkTemplate, '#invisibleTabLink').html()
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
		},
		onActivateTab : function(event, ui) {
			console.log('activate tab');

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
			// Put loading animation.
			// $(panel).html(putAnimation());
			// Simulate time consuming content.
			setTimeout(function() {
				retrieveTabContent(selectedTabIndex);
			}, 10);
		}
	};

	return TabEvents;
});