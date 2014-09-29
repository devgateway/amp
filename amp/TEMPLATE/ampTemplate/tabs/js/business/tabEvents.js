/*https://gist.github.com/jonnyreeves/2474026*/
/*https://github.com/icereval/backbone-documentmodel*/
define([ 'marionette', 'collections/contents', 'models/content', 'views/dynamicContentView', 'text!views/html/filtersWrapperTemplate.html',
		'text!views/html/filtersItemTemplate.html', 'models/filter', 'collections/filters', 'models/tab',
		'text!views/html/invisibleTabLinkTemplate.html', 'jqgrid', 'util/columnsMapping', 'text!views/html/legendsTemplate.html' ],
		function(Marionette, Contents, Content, DynamicContentView, filtersTemplate, filtersItemTemplate, Filter, Filters, Tab,
				invisibleTabLinkTemplate, jqGrid, columnsMapping, legendsTemplate) {

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
							return transformData(obj, grouping, tableStructure.hierarchies);
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
					colNames : columnsMapping.createJQGridColumnNames(tableStructure, grouping),
					colModel : columnsMapping.createJQGridColumnModel(tableStructure),
					height : 400,
					autowidth : true,
					shrinkToFit : true,
					forceFit : false,
					viewrecords : true,
					loadtext : 'Loading...',
					headertitles : true,
					gridview : true,
					rownumbers : false,
					rowNum : 1000,
					pager : "#tab_grid_pager_" + id,
					emptyrecords : 'No records to view',
					grouping : grouping,
					groupingView : columnsMapping.createJQGridGroupingModel(tableStructure, grouping),
					gridComplete : function() {
					}
				});
			}

			/*
			 * Before trying to render the data from server we need to make some
			 * transformations and cleanups.
			 */
			function transformData(data, grouping, hierarchies) {
				var rows = [];
				getContentRecursively(data.reportContents /* data.pageArea */, rows, null);
				if (grouping) {
					postProcessHierarchies(rows, hierarchies);
				}
				return rows;
			}

			/*
			 * The data from server uses a hierarchy format where not all values
			 * are set in all subnodes (children), so we have to add them
			 * manually before rendering.
			 */
			function postProcessHierarchies(rows, hierarchies) {
				$.each(rows, function(i, row) {
					$.each(hierarchies.models, function(j, hierarchy) {
						if (row[hierarchy.get('columnName')] != undefined) {
							hierarchy.set('lastValue', row[hierarchy.get('columnName')]);
						} else {
							row[hierarchy.get('columnName')] = hierarchy.get('lastValue');
						}
					});
				});
			}

			function getContentRecursively(obj, rows, parent) {
				if (obj != undefined && obj != null) {
					if (obj.children == null || obj.children.length == 0) {
						// console.log(obj.contents);
						var row = {
							id : 0
						};
						$.each(obj.contents, function(key, element) {
							var colName = columnsMapping.getMap()[key];
							if (colName != undefined && colName != null) {
								if (element.displayedValue != null && element.displayedValue != "") {
									row[colName] = element.displayedValue;
								} else {
									row[colName] = getParentContent(key, parent);
								}
								row['id'] = Math.random();
							}
						});
						// console.log(row);
						rows.push(row);
					} else {
						$(obj.children).each(function(i, item) {
							getContentRecursively(item, rows, obj.contents);
						});
					}
				}
			}

			/*
			 * The endpoint doesnt fill the hierarchical values after the 1st
			 * node so we need to take some values from the parent by recursion.
			 */
			function getParentContent(key, parent) {
				if (parent != undefined && parent != null) {
					if (parent[key].displayedValue != null && parent[key].displayedValue.indexOf(' Totals') > 0) {
						return parent[key].displayedValue.substring(0, parent[key].displayedValue.indexOf(' Totals'));
					} else {
						getParentContent(key, parent.parent);
					}
				} else {
					return 'NULL';
				}
			}

			function retrieveTabContent(selectedTabIndex) {
				var id = app.TabsApp.tabItemsView.collection.models[selectedTabIndex].get('id');

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
					// TODO: make complex view for adding more info in this
					// section.
					var Legend = Backbone.Model.extend({});
					var LegendView = Marionette.ItemView.extend({
						template : _.template(legendsTemplate),
						className : 'legends-container'
					});
					var legend = new Legend({
						currencyCode : firstContent.get('reportMetadata').get('reportSpec').get('settings').get('currencyCode')
					});
					var legendView = new LegendView({
						model : legend
					});
					dynamicLayoutView.legends.show(legendView);

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