/*https://gist.github.com/jonnyreeves/2474026*/
/*https://github.com/icereval/backbone-documentmodel*/
define([ 'marionette', 'collections/contents', 'models/content', 'views/dynamicContentView', 'text!views/html/filtersWrapperTemplate.html',
		'text!views/html/filtersItemTemplate.html', 'models/filter', 'collections/filters', 'models/tab',
		'text!views/html/invisibleTabLinkTemplate.html', 'jqgrid' ], function(Marionette, Contents, Content, DynamicContentView,
		filtersTemplate, filtersItemTemplate, Filter, Filters, Tab, invisibleTabLinkTemplate, jqGrid) {

	"use strict";

	// TODO: We need to receive the same column name from both endpoints!!!
	var map = new Object();
	map['[Project Title]'] = 'Project Title';
	map['[Region Name]'] = 'Region';
	map['[Total Measures][Actual Commitments]'] = 'Actual Commitments';
	map['[Actual Commitments]'] = 'Actual Commitments';
	map['[Total Measures][Actual Disbursements]'] = 'Actual Disbursements';
	map['[Actual Disbursements]'] = 'Actual Disbursements';
	map['[AMP ID]'] = 'AMP ID';
	map['[Level 0 Sector]'] = 'Primary Sector';
	map['[Level 1 Sector]'] = 'Primary Sector Sub-Sector';
	map['[Level 2 Sector]'] = 'Primary Sector Sub-Sub-Sector';
	map['[Donor Agency]'] = 'Donor Agency';

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
		ret.push('');
		$(metadata.columns.models).each(function(i, item) {
			ret.push(item.get('columnName'));
		});
		/*
		 * $(metadata.hierarchies.models).each(function(i, item) {
		 * ret.push(item.get('columnName')); });
		 */
		$(metadata.measures.models).each(function(i, item) {
			ret.push(item.get('measureName'));
		});
		console.log(ret);
		return ret;
	}

	function createJQGridColumnModel(metadata) {
		var ret = [];
		ret.push({
			name : 'editColumn',
			width : 5,
			sortable : false
		});
		$(metadata.columns.models).each(function(i, item) {
			ret.push({
				name : item.get('columnName')
			});
		});
		/*
		 * $(metadata.hierarchies.models).each(function(i, item) { ret.push({
		 * name : item.get('columnName') }); });
		 */
		$(metadata.measures.models).each(function(i, item) {
			ret.push({
				name : item.get('measureName'),
				width : 30
			});
		});
		console.log(ret);
		return ret;
	}

	function createJQGridGroupingModel(metadata, grouping) {
		if (grouping) {
			var ret = {};
			var fields = [];
			var hiddenFields = [];
			var styleText = [];
			// var summary = [];
			$(metadata.hierarchies.models).each(function(i, item) {
				fields.push(item.get('columnName'));
				hiddenFields.push(false);
				styleText.push('<b>{0} - ({1})</b>');
				// summary.push(true);
			});
			ret.groupField = fields;
			ret.groupColumnShow = hiddenFields;
			ret.groupText = styleText;
			// ret.groupSummary = summary;
			console.log(ret);
			return ret;
		} else {
			return {};
		}
	}

	function populateGrid(id, dynamicLayoutView, firstContent) {
		// TODO: Move grid section elsewhere.
		var TableSectionView = Marionette.ItemView.extend({
			template : '#grid-template'
		});
		var tableSectionView = new TableSectionView();
		dynamicLayoutView.results.show(tableSectionView);

		// Define grid structure.
		var tableStructure = extractMetadata(firstContent);
		var grouping = (tableStructure.hierarchies.length > 0) ? true : false;
		var grid = $("#tab_grid");
		$(grid).attr("id", "tab_grid_" + id);
		var pager = $("#tab_grid_pager");
		$(pager).attr("id", "tab_grid_pager_" + id);

		$(grid).jqGrid({
			caption : false,
			url : '/rest/data/report/' + id + '/result/',
			/* url : '/rest/data/report/' + id + '/result/jqGrid', */
			datatype : 'json',
			jsonReader : {
				repeatitems : false,
				root : function(obj) {
					return transformData(obj);
				},
				page : function(obj) {
					return obj.currentPageNumber;
				},
				total : function(obj) {
					return obj.totalPageCount;
				},
				records : function(obj) {
					return obj.totalRecords;
				}
			},
			colNames : createJQGridColumnNames(tableStructure),
			colModel : createJQGridColumnModel(tableStructure),
			height : 250,
			autowidth : true,
			shrinkToFit : true,
			viewrecords : true,
			loadtext : 'Loading...',
			headertitles : true,
			gridview : true,
			rownumbers : false,
			rowNum : 100, /* TODO: get this parameter from global settings. */
			pager : "#tab_grid_pager_" + id,
			emptyrecords : 'No records to view',
			grouping : grouping,
			groupingView : createJQGridGroupingModel(tableStructure, grouping),
			gridComplete : function() {
				// alert('nada');
			}
		});
	}

	function transformData(data) {
		// console.log(data);
		var rows = [];
		getContentRecursively(data.reportContents /* data.pageArea */, rows, null);
		return rows;
	}

	function getContentRecursively(obj, rows, parent) {
		if (obj != undefined && obj != null) {
			if (obj.children == null || obj.children.length == 0) {
				// console.log(obj.contents);
				var row = {
					id : 0
				};
				$.each(obj.contents, function(key, element) {
					var colName = map[key];
					if (colName != undefined && colName != null) {
						if (element.displayedValue != null && element.displayedValue != "") {
							row[colName] = element.displayedValue;
						} else {
							// TODO: This section uses data from the parent node
							// just because now some hierarchical reports
							// come with empty columns after the 1st children
							// node. Remove it when that problem is fixed.
							if (parent != undefined) {
								if (parent[key].displayedValue.indexOf(' Totals') > 0) {
									row[colName] = parent[key].displayedValue.substring(0, parent[key].displayedValue.indexOf(' Totals'));
								} else {
									row[colName] = parent[key].displayedValue;
								}
							} else {
								// TODO: If the data where ok this section wount
								// be needed either.
								row[colName] = 'Undefined';
							}
						}
						row['id'] = Math.random();
					}
				});
				console.log(row);
				rows.push(row);
			} else {
				$(obj.children).each(function(i, item) {
					getContentRecursively(item, rows, obj.contents);
				});
			}
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