define(function() {

	"use strict";

	// TODO: We need to receive the same column name from both endpoints!!!
	// We can define here some properties like min-width, formats, etc.
	var map = new Object();
	map['[Project Title]'] = {
		name : 'Project Title',
		width : 550,
		fixed : false
	};
	map['[Region Name]'] = {
		name : 'Region'
	};
	map['[Total Measures][Actual Commitments]'] = {
		name : 'Actual Commitments'
	};
	map['[Actual Commitments]'] = {
		name : 'Actual Commitments'
	};
	map['[Planned Commitments]'] = {
		name : 'Planned Commitments'
	};
	map['[Total Measures][Actual Disbursements]'] = {
		name : 'Actual Disbursements'
	};
	map['[Actual Disbursements]'] = {
		name : 'Actual Disbursements'
	};
	map['[AMP ID]'] = {
		name : 'AMP ID',
		width: 80
	};
	map['[Level 0 Sector]'] = {
		name : 'Primary Sector'
	};
	map['[Level 1 Sector]'] = {
		name : 'Primary Sector Sub-Sector'
	};
	map['[Level 2 Sector]'] = {
		name : 'Primary Sector Sub-Sub-Sector'
	};
	map['[Level 2 Sector]'] = {
		name : 'Secondary Sector'
	};
	map['[Donor Agency]'] = {
		name : 'Donor Agency'
	};
	map['[Organization Name]'] = {
		name : 'Donor Agency'
	};
	map['[Category Name]'] = {
		name : 'Status'
	};
	map['[Project Description]'] = {
		name : 'Project Description'
	};
	map['[Activity Created On]'] = {
		name : 'Activity Created On'
	};
	map['[Program Level 1 Name]'] = {
		name : 'National Planning Objectives Level 1'
	};

	function findInMapByColumnName(name) {
		var ret = undefined;
		$.each(map, function(i, item) {
			if (item.name == name) {
				ret = item;
			}
		});
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
				return "<img src='/TEMPLATE/ampTemplate/img_2/ico_edit.gif'/>";
			}
		});
		$(metadata.columns.models).each(function(i, item) {
			var column = {
				name : item.get('columnName'),
				classes : 'wrap-cell'
			};
			var mappedColumn = findInMapByColumnName(item.get('columnName'));
			if (mappedColumn != undefined && mappedColumn.width != undefined) {
				column.width = mappedColumn.width;
				column.fixed = (mappedColumn.fixed != undefined) ? mappedColumn.fixed : false;
			}
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
				fixed : true
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
			ret.groupCollapse = true;
			// ret.groupSummary = summary;
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