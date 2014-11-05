define(function() {

	"use strict";

	// We can define here some properties like min-width, formats, etc.
	var map = new Object();
	map['Project Title'] = {
		width : 550,
		fixed : false
	};
	map['AMP ID'] = {
		width : 80
	};

	function findInMapByColumnName(name) {
		var ret = undefined;
		$.each(map, function(i, item) {
			if (item.name == name) {
				ret = item;
			}
		});
		if (ret != undefined) {
			console.log('Found column: ' + name);
		} else {
			console.warn('Not Found column: ' + name);
		}
		return ret;
	}

	function ColumnsMapping() {
		if (!(this instanceof ColumnsMapping)) {
			throw new TypeError("ColumnsMapping constructor cannot be called as a function.");
		}
	}

	ColumnsMapping.getMap = function() {
		return map;
	};

	ColumnsMapping.createJQGridColumnNames = function(metadata, isGrouped) {
		var ret = [];
		ret.push(''); // edit icon column.
		ret.push(''); // activity id.
		ret.push(''); // approval status.
		$(metadata.columns.models).each(function(i, item) {
			var colName = item.get('columnName');
			var auxColName = "";
			// If we are using grouping then change the title of the first
			// non-grouped column.
			if ((metadata.hierarchies.models.length) == i && isGrouped) {
				$(metadata.hierarchies.models).each(function(j, item2) {
					auxColName += item2.get('columnName') + ' / ';
				});
				ret.push(auxColName + colName);
			} else {
				ret.push(colName);
			}
		});
		$(metadata.measures.models).each(function(i, item) {
			ret.push(item.get('measureName'));
		});
		console.log(ret);
		return ret;
	};

	ColumnsMapping.createJQGridColumnModel = function(metadata) {
		var ret = [];
		ret.push({
			name : 'editColumn',
			width : 25,
			sortable : false,
			fixed : true,
			formatter : function() {
				return "";
			}
		});

		ret.push({
			name : 'Activity Id',
			width : 0,
			hidden : true
		});
		ret.push({
			name : 'Approval Status',
			hidden : true
		});

		$(metadata.columns.models).each(function(i, item) {
			var column = {
				name : item.get('columnName'),
				classes : 'wrap-cell',
				sortable : true
			};
			// Show current group name (not natively supported by jqGrid).
			if (i == (metadata.hierarchies.models.length)) {
				column.summaryType = 'groupName';
				column.summaryTpl = 'TOTAL [{0}]';
			}
			var mappedColumn = findInMapByColumnName(item.get('columnName'));
			if (mappedColumn != undefined && mappedColumn.width != undefined) {
				column.width = mappedColumn.width;
				column.fixed = (mappedColumn.fixed != undefined) ? mappedColumn.fixed : false;
			}
			/*column.sorttype = function(cellValue, obj) {
				alert(cellValue + obj);
				// return groupOrder[obj.groupId];
				// return obj.groupOrder;
				return "";
			};*/
			ret.push(column);
		});
		/*
		 * $(metadata.hierarchies.models).each(function(i, item) { ret.push({
		 * name : item.get('columnName') }); });
		 */
		$(metadata.measures.models).each(function(i, item) {
			ret.push({
				name : item.get('measureName'),
				width : 105,
				fixed : true,
				summaryType : 'sum',
				align : "right",
				sorttype : 'number',
				formatter : 'number'
			});
		});
		console.log(ret);
		return ret;
	};

	ColumnsMapping.createJQGridGroupingModel = function(metadata, grouping) {
		if (grouping) {
			var ret = {};
			var fields = [];
			var hiddenFields = [];
			var styleText = [];
			var summary = [];
			$(metadata.hierarchies.models).each(function(i, item) {
				fields.push(item.get('columnName'));
				hiddenFields.push(false);
				styleText.push('<b>{0} - ({1})</b>');
				summary.push(true);
			});
			ret.groupField = fields;
			ret.groupColumnShow = hiddenFields;
			ret.groupText = styleText;
			ret.groupCollapse = true;

			ret.showSummaryOnHide = false;
			ret.groupSummary = summary;
			console.log(ret);
			return ret;
		} else {
			return {};
		}
	};

	ColumnsMapping.recalculateColumnsWidth = function(grid, widthText) {
		/*
		 * var width = widthText.substring(0, widthText.length - 2); var columns =
		 * $(grid).jqGrid('getGridParam', 'colModel'); // Recalculate 1st column
		 * (edit icon), the desired width is 23px to // 25px. var newWidth =
		 * $(grid).jqGrid('setColProp', 'editColumn', { widthOrg : newWidth });
		 * var gw = $(grid).jqGrid('getGridParam', 'width');
		 * $(grid).jqGrid('setGridWidth', gw);
		 */
	};

	ColumnsMapping.prototype = {
		constructor : ColumnsMapping
	};

	return ColumnsMapping;
});