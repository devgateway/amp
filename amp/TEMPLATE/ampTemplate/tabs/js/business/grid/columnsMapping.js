define([ 'util/tabUtils' ], function(TabUtils) {

	"use strict";

	// We can define here some properties like min-width, formats, etc.
	var map = new Object();
	map['Project Title'] = {
		width : 575,
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
			console.log('Found column with special settings: ' + name);
		} else {
			//console.warn('Not Found column: ' + name);
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
		ret.push(''); // Draft.
		ret.push(''); // Team.
		$(metadata.columns.models).each(
				function(i, item) {
					var colName = item.get('columnName');
					var auxColName = "";
					// If we are using grouping then change the title of the
					// first
					// non-grouped column.
					if ((metadata.hierarchies.models.length) == i && isGrouped) {
						$(metadata.hierarchies.models).each(
								function(j, item2) {
									auxColName += "<span data-i18n='tabs.common:column" + item2.get('columnName') + "'>"
											+ item2.get('columnName') + "</span>" + ' / ';
								});
						ret.push(auxColName + "<span data-i18n='tabs.common:column" + colName + "'>" + colName + "</span>");
					} else {
						ret.push("<span data-i18n='tabs.common:column" + colName + "'>" + colName + "</span>");
					}
				});
		$(metadata.measures.models).each(function(i, item) {
			ret.push("<span data-i18n='tabs.common:column" + item.get('measureName') + "'>" + item.get('measureName') + "</span>");
		});
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
			name : app.TabsApp.COLUMN_ACTIVITY_ID,
			width : 0,
			hidden : true
		});
		ret.push({
			name : 'Approval Status',
			hidden : true
		});
		ret.push({
			name : 'Draft',
			hidden : true
		});
		ret.push({
			name : 'Team',
			hidden : true
		});
		$(metadata.columns.models).each(function(i, item) {
			var column = {
				name : item.get('columnName'),
				classes : 'wrap-cell' + (item.get('columnName') === metadata.projectTitleColumn? ' wrap-title': ''),
				sortable : true
			};
			// No need to show a "summary row" if we want to match old tabs
			// style.
			/*
			 * // Show current group name (not natively supported by jqGrid). if
			 * (i == (metadata.hierarchies.models.length)) { column.summaryType =
			 * 'groupName'; column.summaryTpl = 'TOTAL [{0}]'; }
			 */
			var mappedColumn = findInMapByColumnName(item.get('columnName'));
			if (mappedColumn != undefined && mappedColumn.width != undefined) {
				column.width = mappedColumn.width;
				column.fixed = (mappedColumn.fixed != undefined) ? mappedColumn.fixed : false;
			}
			ret.push(column);
		});
		$(metadata.measures.models).each(function(i, item) {
			ret.push({
				name : item.get('measureName'),
				width : 110,
				fixed : true,
				// No need to show a "summary row" if we want to match old tabs
				// style.
				/*
				 * summaryType : function(val, name, record) { // This function
				 * is called only when the group summary is // calculated. //
				 * HOW IT WORKS: In order to display all numbers with the //
				 * format used by the backend we use 'displayedValue' for // the
				 * measures column, but that value is a string, so in // order
				 * to calculate the SUM for each group we have to // convert it
				 * back to its float representation, make the sum // and convert
				 * it to string again (respecting the backend // format). var
				 * currentVal = -1; if (val != undefined && val != null &&
				 * typeof (val) === 'string' && val != "") { val =
				 * TabUtils.stringToNumber(val,
				 * app.TabsApp.numericFormatOptions); } currentVal =
				 * record[name]; currentVal =
				 * TabUtils.stringToNumber(currentVal,
				 * app.TabsApp.numericFormatOptions); currentVal = currentVal +
				 * val; return TabUtils.numberToString(currentVal,
				 * app.TabsApp.numericFormatOptions); },
				 */
				align : "right",
				sorttype : 'number',
				unformat : function(data) {
					// This function is called automatically on 'getCol'. No
					// need to implement the related function 'format' since we
					// get the raw value formatted from the backend.
					return TabUtils.stringToNumber(data, app.TabsApp.numericFormatOptions);
				},
				reportColumnType : app.TabsApp.COLUMN_TYPE_MEASURE
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
				// styleText.push("<b>{0} - ({1} </b>" + "<b
				// data-i18n='tabs.common:outOf'>out of</b>" +
				// "<b>@@totalChildrenCount@@)</b>");
				styleText.push("<b style='word-wrap: break-word;'>{0} - (@@currentActivitiesCount@@/@@totalActivitiesCount@@)</b>");
				summary.push(true);
			});
			ret.groupField = fields;
			ret.groupColumnShow = hiddenFields;
			ret.groupText = styleText;
			ret.groupCollapse = true;

			ret.showSummaryOnHide = false;

			// No need to show a "summary row" if we want to match old tabs
			// style.
			/* ret.groupSummary = summary; */

			console.log(ret);
			return ret;
		} else {
			return {};
		}
	};

	ColumnsMapping.recalculateColumnsWidth = function(grid, widthText) {
	};

	ColumnsMapping.prototype = {
		constructor : ColumnsMapping
	};

	return ColumnsMapping;
});