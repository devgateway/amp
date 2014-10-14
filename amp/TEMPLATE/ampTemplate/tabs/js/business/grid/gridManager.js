define([ 'business/grid/columnsMapping', 'jqgrid' ], function(columnsMapping) {

	"use strict";

	function GridManager() {
		if (!(this instanceof GridManager)) {
			throw new TypeError("GridManager constructor cannot be called as a function.");
		}
	}

	GridManager.prototype = {
		constructor : GridManager
	};

	GridManager.populateGrid = function(id, dynamicLayoutView, firstContent) {
		var TableSectionView = Marionette.ItemView.extend({
			template : '#grid-template'
		});
		var tableSectionView = new TableSectionView();
		dynamicLayoutView.results.show(tableSectionView);

		// Define grid structure.
		var tableStructure = extractMetadata(firstContent);
		var grouping = (tableStructure.hierarchies.length > 0) ? true : false;
		var grid = jQuery("#tab_grid");
		jQuery(grid).attr("id", "tab_grid_" + id);
		var pager = jQuery("#tab_grid_pager");
		jQuery(pager).attr("id", "tab_grid_pager_" + id);

		var rowNum = 0;
		jQuery(grid).jqGrid({
			caption : false,
			/* url : '/rest/data/report/' + id + '/result/', */
			url : '/rest/data/report/' + id + '/result/jqGrid',
			datatype : 'json',
			jsonReader : {
				repeatitems : false,
				root : function(obj) {
					rowNum = obj.recordsPerPage;
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
			height : 200,
			autowidth : true,
			shrinkToFit : true,
			forceFit : false,
			viewrecords : true,
			loadtext : 'Loading...',
			headertitles : true,
			gridview : true,
			rownumbers : false,
			rowNum : rowNum,
			pager : "#tab_grid_pager_" + id,
			emptyrecords : 'No records to view',
			grouping : grouping,
			groupingView : columnsMapping.createJQGridGroupingModel(tableStructure, grouping),
			gridComplete : function() {
				// console.log(jQuery(grid).css("width"));
				// columnsMapping.recalculateColumnsWidth(grid,
				// jQuery(grid).css("width"));
				jQuery(grid).find(">tbody>tr.jqgrow:odd").addClass("myAltRowClassEven");
				jQuery(grid).find(">tbody>tr.jqgrow:even").addClass("myAltRowClassOdd");
			}
		});
	};

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

	/*
	 * Before trying to render the data from server we need to make some
	 * transformations and cleanups.
	 */
	function transformData(data, grouping, hierarchies) {
		var rows = [];
		getContentRecursively(/* data.reportContents */data.pageArea, rows, null);
		if (grouping) {
			postProcessHierarchies(rows, hierarchies);
		}
		// console.log(rows);
		return rows;
	}

	/*
	 * The data from server uses a hierarchy format where not all values are set
	 * in all subnodes (children), so we have to add them manually before
	 * rendering.
	 */
	function postProcessHierarchies(rows, hierarchies) {
		jQuery.each(rows, function(i, row) {
			jQuery.each(hierarchies.models, function(j, hierarchy) {
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
				jQuery.each(obj.contents, function(key, element) {
					var colName = null;
					if (columnsMapping.getMap()[key] != undefined) {
						colName = columnsMapping.getMap()[key].name;
					}
					if (colName != undefined && colName != null) {
						if (element.value != null && element.value != "") {
							row[colName] = element.value;
						} else {
							row[colName] = getParentContent(key, parent);
						}
						row['id'] = Math.random();
					}
				});
				// console.log(row);
				rows.push(row);
			} else {
				jQuery(obj.children).each(function(i, item) {
					getContentRecursively(item, rows, obj.contents);
				});
			}
		}
	}

	/*
	 * The endpoint doesnt fill the hierarchical values after the 1st node so we
	 * need to take some values from the parent by recursion.
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

	return GridManager;
});